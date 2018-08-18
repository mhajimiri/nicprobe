package com.mohaymen.registry.nicprobe.backend.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

@Getter
@Setter

@RedisHash("ResponseInfo")
public class ResponseInfoModel {
    private String id;
    private int type;
    private long responseTime;
}
