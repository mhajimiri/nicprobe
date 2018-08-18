package com.mohaymen.registry.nicprobe.backend.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;
import java.io.Serializable;
@Getter
@Setter

@RedisHash("GsmMap")
public class GsmMapModel implements Serializable{
    private String id;
    private String time;
}
