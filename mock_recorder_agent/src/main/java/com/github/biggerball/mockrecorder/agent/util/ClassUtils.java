package com.github.biggerball.mockrecorder.agent.util;


import com.alibaba.bytekit.utils.AsmUtils;

/**
 *
 * @author hengyunabc 2018-10-18
 *
 */
public class ClassUtils {


    public static boolean isLambdaClass(Class<?> clazz) {
        return clazz.getName().contains("$$Lambda$");
    }

    public static String getOriginNameFromCGLib(String name) {
        int i = name.indexOf("$$EnhancerBySpringCGLIB$$");
        if (i > 0) {
            return name.substring(0, i);
        }
        i = name.indexOf("$$EnhancerByCGLIB$$");
        if (i > 0) {
            return name.substring(0, i);
        }
        throw new RuntimeException(String.format("class %s not match cglib", name));
    }
}
