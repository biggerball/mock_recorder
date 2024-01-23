package com.github.biggerball.storage.handler;

import com.github.biggerball.entity.ParamRecord;
import com.github.biggerball.storage.entity.ReadDbReq;
import com.github.biggerball.storage.entity.ResultVO;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface DbHandler {

    ResultVO write(@RequestBody ParamRecord paramRecord);

    ResultVO read(@RequestBody ReadDbReq req);
}
