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

        List<Pair<String, String>> patterns = GlobalConfig.readPatternFromFile();
        if (patterns.isEmpty()) {
            return;
        }
        List<NameMatcher> nameMatchers = patterns.stream().map(o -> new NameMatcher(o.getFirst(), o.getSecond())).collect(Collectors.toList());
        inst.addTransformer(new Enhancer(nameMatchers));
    }
}
