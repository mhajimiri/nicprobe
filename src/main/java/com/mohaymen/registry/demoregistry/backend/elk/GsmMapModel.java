package com.mohaymen.registry.demoregistry.backend.elk;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.*;
import java.io.Serializable;
@Getter
@Setter

@RedisHash("GsmMap")
public class GsmMapModel implements Serializable{

    private String id;

    private String time;

    private String srcIp;

    private String calledDigit;

    private String callingDigit;

    private String desIp;

    private String epochTime;
}
