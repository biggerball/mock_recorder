package com.github.biggerball.agent;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.instrument.Instrumentation;
import java.util.UUID;

public class SimpleAgent {

    protected static final String traceId = UUID.randomUUID().toString();

    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("premain");
        new AgentBuilder.Default()
                .type(ElementMatchers.any())
                .transform(new AgentTransformer())
//        .with(AgentBuilder.Listener.StreamWriting.toSystemOut())
                .with(AgentBuilder.TypeStrategy.Default.REDEFINE)
                .installOn(inst);
    }
}
