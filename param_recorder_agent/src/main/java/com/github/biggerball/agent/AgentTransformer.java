package com.github.biggerball.agent;

import com.github.biggerball.entity.ParamRecord;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.bytebuddy.agent.builder.AgentBuilder.Transformer;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.asm.AsmVisitorWrapper;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;

import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

public class AgentTransformer implements Transformer {

    @Override
    public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder,
                                            TypeDescription typeDescription,
                                            ClassLoader classLoader,
                                            JavaModule javaModule) {
        final AsmVisitorWrapper methodsVisitor =
                Advice.to(EnterAdvice.class, ExitAdviceMethods.class)
                        .on(ElementMatchers.isMethod().and(ElementMatchers.named("read")));

        return builder.visit(methodsVisitor);
    }

    private static class EnterAdvice {
        @Advice.OnMethodEnter
        static ParamRecord enter(@Advice.This Object object,  @Advice.Origin Method method) {
            String className;
            if (object instanceof Proxy) {
                Class<?>[] interfaces = ((Proxy) object).getClass().getInterfaces();
                className = interfaces[0].getName();
            } else {
                className = object.getClass().getName();
            }
            return new ParamRecord("traceId", className, method.getName(), System.currentTimeMillis());
        }
    }

    private static class ExitAdviceMethods {
        @Advice.OnMethodExit(onThrowable = Throwable.class)
        static void exit(@Advice.Origin final Executable executable,
                         @Advice.Return final Object returnValue,
                         @Advice.Enter ParamRecord paramRecord) {

            Gson gson = new GsonBuilder().setPrettyPrinting().setDateFormat("yyyy-MM-dd HH:mm:ss.SSS").create();

            paramRecord.addReturnValue(returnValue.getClass().getName(), gson.toJson(returnValue));

            System.out.println("return: " + gson.toJson(paramRecord));
        }
    }
}
