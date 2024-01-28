package com.github.biggerball.mockrecorder.agent;

import com.github.biggerball.mockrecorder.agent.advisor.Enhancer;
import com.github.biggerball.mockrecorder.agent.common.Pair;
import com.github.biggerball.mockrecorder.agent.util.GlobalConfig;
import com.github.biggerball.mockrecorder.agent.util.matcher.NameMatcher;

import java.lang.instrument.Instrumentation;
import java.util.List;
import java.util.stream.Collectors;

public class SimpleAgent {

    public static void premain(String agentArgs, Instrumentation inst) {
        GlobalConfig.initGlobalConfig(agentArgs);

        List<Pair<String, String>> matchPatterns = GlobalConfig.readPatternFromFile("pattern_match");
        if (matchPatterns.isEmpty()) {
            System.out.println("not found match pattern, agent fail");
            return;
        }
        List<Pair<String, String>> ignorePatterns = GlobalConfig.readPatternFromFile("pattern_ignore");
        List<NameMatcher> nameMatchers = matchPatterns.stream().map(o -> new NameMatcher(o.getFirst(), o.getSecond())).collect(Collectors.toList());
        List<NameMatcher> ignoreMatchers = ignorePatterns.stream().map(o -> new NameMatcher(o.getFirst(), o.getSecond())).collect(Collectors.toList());

        inst.addTransformer(new Enhancer(nameMatchers, ignoreMatchers));
    }
}
