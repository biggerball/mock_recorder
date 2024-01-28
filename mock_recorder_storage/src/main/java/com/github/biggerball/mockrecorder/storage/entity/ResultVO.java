package com.github.biggerball.mockrecorder.storage.entity;

import lombok.Data;

@Data
public class ResultVO {
    private int code;
    private Object data;
    private String errMsg;

    public static ResultVO success(Object data) {
        ResultVO resultVO = new ResultVO();
        resultVO.data = data;
        return resultVO;
    }

    public static ResultVO error(String errMsg) {
        ResultVO resultVO = new ResultVO();
        resultVO.code = -1;
        resultVO.errMsg = errMsg;
        return resultVO;
    }
}
