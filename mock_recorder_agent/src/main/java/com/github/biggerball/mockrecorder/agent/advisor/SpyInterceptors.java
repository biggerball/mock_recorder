package com.github.biggerball.mockrecorder.agent.advisor;

import com.alibaba.bytekit.asm.binding.Binding;
import com.alibaba.bytekit.asm.interceptor.annotation.AtEnter;
import com.alibaba.bytekit.asm.interceptor.annotation.AtExceptionExit;
import com.alibaba.bytekit.asm.interceptor.annotation.AtExit;
import com.github.biggerball.mockrecorder.agent.entity.InvokeRecord;

/**
 * 
 * @author hengyunabc 2020-06-05
 *
 */
public class SpyInterceptors {
    public static class SpyInterceptor1 {

        @AtEnter(inline = true)
        public static void atEnter(@Binding.This Object target, @Binding.Class Class<?> clazz,
                                           @Binding.MethodInfo String methodInfo, @Binding.Args Object[] args) {
            SpyImpl.atEnter(clazz, methodInfo, target, args);
        }
    }
    
    public static class SpyInterceptor2 {
        @AtExit(inline = true)
        public static void atExit(@Binding.Return Object returnObj) {
            SpyImpl.atExit(returnObj);
        }
    }
    
    public static class SpyInterceptor3 {
        @AtExceptionExit(inline = true)
        public static void atExceptionExit(@Binding.Throwable Throwable throwable) {
            SpyImpl.atExceptionExit(throwable);
        }
    }

}
