package com.mohaymen.registry.demoregistry.backend.elk;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter

@RedisHash("ResponseInfo")
public class ResponseInfoModel {

    private String id;

    private String timeF;

    private String timeL;

    private String epochTimeF;
    private String epochTimeL;
    private long respEpochTime;

    private int type;

    private long responseTime;
}
