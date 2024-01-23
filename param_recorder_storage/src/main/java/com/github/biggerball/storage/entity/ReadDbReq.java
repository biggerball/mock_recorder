package com.github.biggerball.storage.entity;

import lombok.Data;

@Data
public class ReadDbReq {
    String traceId;
    String className;
    String methodName;
    String fromTime;
    String toTime;
}
