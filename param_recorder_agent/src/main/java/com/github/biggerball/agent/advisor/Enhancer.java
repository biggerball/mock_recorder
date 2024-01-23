//package com.github.biggerball.agent.advisor;
//
//import com.alibaba.bytekit.asm.interceptor.InterceptorProcessor;
//import com.alibaba.bytekit.asm.interceptor.parser.DefaultInterceptorClassParser;
//import com.alibaba.bytekit.utils.AsmUtils;
//import com.github.biggerball.agent.SpyAPI;
//import com.github.biggerball.agent.util.matcher.Matcher;
//
//import java.lang.instrument.ClassFileTransformer;
//import java.lang.instrument.IllegalClassFormatException;
//import java.security.ProtectionDomain;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//import java.util.WeakHashMap;
//
//public class Enhancer implements ClassFileTransformer {
//    Matcher classNameMatcher;
//    Matcher methodNameMatcher;
//
//    private Set<Class<?>> matchingClasses = null;
//    private static final ClassLoader selfClassLoader = Enhancer.class.getClassLoader();
//
//    // 被增强的类的缓存
//    private final static Map<Class<?>/* Class */, Object> classBytesCache = new WeakHashMap<Class<?>, Object>();
////    private static SpyImpl spyImpl = new SpyImpl();
////
////    static {
////        SpyAPI.setSpy(spyImpl);
////    }
//    public Enhancer(Matcher classNameMatcher, Matcher methodNameMatcher) {
//        this.classNameMatcher = classNameMatcher;
//        this.methodNameMatcher = methodNameMatcher;
//    }
//
//
//    @Override
//    public byte[] transform(ClassLoader inClassLoader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
//        try {
//            // 检查classloader能否加载到 SpyAPI，如果不能，则放弃增强
//            try {
//                if (inClassLoader != null) {
//                    inClassLoader.loadClass(SpyAPI.class.getName());
//                }
//            } catch (Throwable e) {
////                logger.error("the classloader can not load SpyAPI, ignore it. classloader: {}, className: {}",
////                        inClassLoader.getClass().getName(), className, e);
//                return null;
//            }
//
//
//            // 生成增强字节码
//            DefaultInterceptorClassParser defaultInterceptorClassParser = new DefaultInterceptorClassParser();
//
//            final List<InterceptorProcessor> interceptorProcessors = new ArrayList<InterceptorProcessor>();
//
//            interceptorProcessors.addAll(defaultInterceptorClassParser.parse(SpyInterceptors.SpyInterceptor1.class));
//            interceptorProcessors.addAll(defaultInterceptorClassParser.parse(SpyInterceptors.SpyInterceptor2.class));
//            interceptorProcessors.addAll(defaultInterceptorClassParser.parse(SpyInterceptors.SpyInterceptor3.class));
//
//
//
//            // https://github.com/alibaba/arthas/issues/1223 , V1_5 的major version是49
//            if (AsmUtils.getMajorVersion(classNode.version) < 49) {
//                classNode.version = AsmUtils.setMajorVersion(classNode.version, 49);
//            }
//
//            byte[] enhanceClassByteArray = AsmUtils.toBytes(classNode, inClassLoader, classReader);
//
//            return enhanceClassByteArray;
//
//        } catch (Exception e) {
//
//        }
//
//    }
//}
