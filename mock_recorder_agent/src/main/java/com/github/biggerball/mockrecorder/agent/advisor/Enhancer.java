package com.github.biggerball.mockrecorder.agent.advisor;

import com.alibaba.bytekit.asm.MethodProcessor;
import com.alibaba.bytekit.asm.interceptor.InterceptorProcessor;
import com.alibaba.bytekit.asm.interceptor.parser.DefaultInterceptorClassParser;
import com.alibaba.bytekit.asm.location.filter.DefaultLocationFilter;
import com.alibaba.bytekit.utils.AsmUtils;
import com.alibaba.deps.org.objectweb.asm.ClassReader;
import com.alibaba.deps.org.objectweb.asm.Opcodes;
import com.alibaba.deps.org.objectweb.asm.tree.ClassNode;
import com.alibaba.deps.org.objectweb.asm.tree.MethodNode;
import com.github.biggerball.mockrecorder.agent.util.ArthasCheckUtils;
import com.github.biggerball.mockrecorder.agent.util.matcher.NameMatcher;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Enhancer implements ClassFileTransformer {

    List<NameMatcher> nameMatchers;
    List<NameMatcher> ignoreMatchers;

    public Enhancer(List<NameMatcher> nameMatchers, List<NameMatcher> ignoreMatchers) {
        this.nameMatchers = nameMatchers;
        this.ignoreMatchers = ignoreMatchers;
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        try {
            ClassNode classNode = new ClassNode(Opcodes.ASM9);
            ClassReader classReader = AsmUtils.toClassNode(classfileBuffer, classNode);
            // remove JSR https://github.com/alibaba/arthas/issues/1304
            classNode = AsmUtils.removeJSRInstructions(classNode);

            //match type
            Optional<NameMatcher> nameMatcher = classNameMatch(classNode.name, classNode.interfaces, this.nameMatchers);
            if (!nameMatcher.isPresent()) {
                return null;
            }
            Optional<NameMatcher> ignoreMatcher = classNameMatch(classNode.name, classNode.interfaces, this.ignoreMatchers);



            System.out.printf("className:%s classes: %s, super: %s\n", className, classNode.interfaces, classNode.superName);
            //match method
            List<MethodNode> matchedMethods = new ArrayList<MethodNode>();
            for (MethodNode methodNode : classNode.methods) {
                if (!isIgnore(methodNode, nameMatcher.get(), ignoreMatcher.orElse(null))) {
                    matchedMethods.add(methodNode);
                }
            }
            if (matchedMethods.isEmpty()) {
                return null;
            }

            // https://github.com/alibaba/arthas/issues/1690
            if (AsmUtils.isEnhancerByCGLIB(className)) {
                for (MethodNode methodNode : matchedMethods) {
                    if (AsmUtils.isConstructor(methodNode)) {
                        AsmUtils.fixConstructorExceptionTable(methodNode);
                    }
                }
            }



            // 生成增强字节码
            DefaultInterceptorClassParser defaultInterceptorClassParser = new DefaultInterceptorClassParser();

            final List<InterceptorProcessor> interceptorProcessors = new ArrayList<InterceptorProcessor>();

            interceptorProcessors.addAll(defaultInterceptorClassParser.parse(SpyInterceptors.SpyInterceptor1.class));
            interceptorProcessors.addAll(defaultInterceptorClassParser.parse(SpyInterceptors.SpyInterceptor2.class));
            interceptorProcessors.addAll(defaultInterceptorClassParser.parse(SpyInterceptors.SpyInterceptor3.class));

            DefaultLocationFilter defaultLocationFilter = new DefaultLocationFilter();
            for (MethodNode methodNode : matchedMethods) {
                MethodProcessor methodProcessor = new MethodProcessor(classNode, methodNode, defaultLocationFilter);
                for (InterceptorProcessor interceptor : interceptorProcessors) {
                    try {
                        interceptor.process(methodProcessor);

                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            }


            // https://github.com/alibaba/arthas/issues/1223 , V1_5 的major version是49
            if (AsmUtils.getMajorVersion(classNode.version) < 49) {
                classNode.version = AsmUtils.setMajorVersion(classNode.version, 49);
            }

            byte[] enhanceClassByteArray = AsmUtils.toBytes(classNode, loader, classReader);




            System.out.printf("className:%s classes: %s, super: %s\n", className, classNode.interfaces, classNode.superName);
            return enhanceClassByteArray;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Optional<NameMatcher> classNameMatch(String className, List<String> interfaces, List<NameMatcher> matchers) {
        for (NameMatcher nameMatcher : matchers) {
            if (nameMatcher.matchClass(className)) {
                return Optional.of(nameMatcher);
            }
            for (String name : interfaces) {
                if (nameMatcher.matchClass(name)) {
                    return Optional.of(nameMatcher);
                }
            }
        }
        return Optional.empty();
    }

    /**
     * 是否抽象属性
     */
    private boolean isAbstract(int access) {
        return (Opcodes.ACC_ABSTRACT & access) == Opcodes.ACC_ABSTRACT;
    }

    /**
     * 是否需要忽略
     */
    private boolean isIgnore(MethodNode methodNode, NameMatcher methodNameMatcher, NameMatcher ignoreMatcher) {
        if (null == methodNode || null == methodNode.name || isAbstract(methodNode.access)) {
            return true;
        }
        if (ignoreMatcher != null && ignoreMatcher.matchMethod(methodNode.name)) {
            return true;
        }
        if (methodNode.name.equals("<clinit>") || methodNode.name.equals("<init>") || methodNode.name.contains("CGLIB$")) {
            return true;
        }
        return !methodNameMatcher.matchMethod(methodNode.name);
    }
}
