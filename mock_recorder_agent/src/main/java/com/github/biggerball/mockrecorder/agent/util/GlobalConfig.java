package com.github.biggerball.mockrecorder.agent.util;

import com.github.biggerball.mockrecorder.agent.common.Pair;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class GlobalConfig {

    public static String serverAddr = "http:127.0.0.1:10086";

    public static String configPath = "/data/mock_recorder/";

    private static boolean console = false;

    public static void initGlobalConfig(String args) {
        if (StringUtils.isEmpty(args)) {
            return;
        }
        for (String arg : args.split(",")) {
            if (StringUtils.isEmpty(arg)) {
                continue;
            }
            String[] info = arg.split("=");
            if (info.length != 2) {
                continue;
            }
            switch (info[0]) {
                case "serverAddr":
                    serverAddr = info[1];
                    break;
                case "configPath":
                    configPath = info[1];
                    break;
                case "console":
                    console = Boolean.parseBoolean(info[1]);
            }
        }
    }

    public static List<Pair<String, String>> readPatternFromFile(String patternName) {
        String fileName = configPath + patternName;
        File configFile = new File(fileName);
        if (!configFile.exists()) {
            System.out.println("pattern file " + fileName + " not found");
            return new ArrayList<>();
        }
        Path path = Paths.get(configFile.toURI());
        List<Pair<String, String>> patterns = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(path); // 读取所有行
            for (String line : lines) {
                if (StringUtils.isEmpty(line)) {
                    continue;
                }
                String[] classMethod = line.split("#");
                if (classMethod.length != 2) {
                    continue;
                }
                patterns.add(new Pair<>(StringUtils.trimAllWhitespace(classMethod[0]), StringUtils.trimAllWhitespace(classMethod[1])));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return patterns;
    }
}
