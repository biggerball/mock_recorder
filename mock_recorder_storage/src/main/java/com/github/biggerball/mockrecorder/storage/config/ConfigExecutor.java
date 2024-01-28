package com.github.biggerball.mockrecorder.storage.config;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ConfigExecutor {
    public static final ThreadPoolExecutor EXECUTOR = new ThreadPoolExecutor(5, 40, 30,
            TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
}
