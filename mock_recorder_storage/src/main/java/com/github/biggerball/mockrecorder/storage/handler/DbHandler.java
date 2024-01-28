package com.github.biggerball.mockrecorder.storage.handler;

import com.github.biggerball.mockrecorder.storage.config.ConfigExecutor;
import com.github.biggerball.mockrecorder.storage.entity.InvokeRecord;
import com.github.biggerball.mockrecorder.storage.entity.ReadDbReq;
import com.github.biggerball.mockrecorder.storage.entity.ResultVO;
import org.springframework.web.bind.annotation.RequestBody;

public abstract class DbHandler {

    public abstract ResultVO write(@RequestBody InvokeRecord invokeRecord);

    protected ResultVO asyncWriteDb(Runnable function) {
        ConfigExecutor.EXECUTOR.execute(function);
        return ResultVO.success(null);
    }

    public abstract ResultVO read(@RequestBody ReadDbReq req);
}
