package com.mohaymen.registry.demoregistry.backend.elk;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter

@RedisHash("Diameter")
public class DiameterModel implements Serializable {

    private String id;

    private String time;

    private String hopByhopId;

    private String endToEndId;

    private String srcIp;

    private String desIp;

    private String epochTime;
}
