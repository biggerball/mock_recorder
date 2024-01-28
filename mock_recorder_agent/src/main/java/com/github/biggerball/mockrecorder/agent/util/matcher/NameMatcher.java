package com.github.biggerball.mockrecorder.agent.util.matcher;

import com.github.biggerball.mockrecorder.agent.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class NameMatcher {

    List<Matcher> classMatchers;

    List<Matcher> methodMatchers;

    public NameMatcher(String classPattern, String methodPattern) {
        this.classMatchers = buildMatcher(classPattern);
        this.methodMatchers = buildMatcher(methodPattern);
    }

    public boolean matchClass(String className) {
        for (Matcher classMatcher : classMatchers) {
            if (classMatcher.matching(className)) {
                return true;
            }
        }
        return false;
    }

    public boolean matchMethod(String methodName) {
        for (Matcher methodMatcher : methodMatchers) {
            if (methodMatcher.matching(methodName)) {
                return true;
            }
        }
        return false;
    }

    public List<Matcher> buildMatcher(String pattern) {
        if (StringUtils.isEmpty(pattern)) {
            return new ArrayList<>(0);
        }
        String[] patterns = pattern.split("\\|");
        List<Matcher> matchers = new ArrayList<>(patterns.length);
        for (String namePattern : pattern.split("\\|")) {
            if (StringUtils.isEmpty(namePattern)) {
                continue;
            }
            namePattern = namePattern.replace(".", "/");
            matchers.add(new WildcardMatcher(namePattern));
        }
        return matchers;
    }
}
