package com.github.biggerball.mockrecorder.storage.entity;

import lombok.Data;

@Data
public class ReadDbReq {
    String traceId;
    String className;
    String methodName;
    String fromTime;
    String toTime;
}
