package com.github.biggerball.mockrecorder.storage.handler;

import com.github.biggerball.mockrecorder.storage.config.ConfigExecutor;
import com.github.biggerball.mockrecorder.storage.entity.InvokeRecord;
import com.github.biggerball.mockrecorder.storage.entity.ReadDbReq;
import com.github.biggerball.mockrecorder.storage.entity.ResultVO;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;

public abstract class DbHandler {

    public abstract ResultVO write(@RequestBody InvokeRecord invokeRecord);

    protected ResultVO asyncWriteDb(Runnable function) {
        ConfigExecutor.EXECUTOR.execute(function);
        return ResultVO.success(null);
    }

    public abstract ResultVO read(@RequestBody ReadDbReq req);

    protected void crateConfigFile(String path) throws IOException {
        File matchPattern = new File(path + "/pattern_match");
        if (!matchPattern.exists()) {
            matchPattern.createNewFile();
        }
        File ignorePattern = new File(path + "/pattern_ignore");
        if (!ignorePattern.exists()) {
            List<String> content = Arrays.asList(
                    "java.util.concurrent.ThreadFactory#*"
            );
            Files.write(ignorePattern.toPath(), content, StandardOpenOption.CREATE);
        }
    }
}
