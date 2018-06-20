package com.mohaymen.registry.demoregistry.backend.elk;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter

@Entity
@Table(name = "Response_Info")
public class ResponseInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "id_sequence")
    @SequenceGenerator(name = "id_sequence",sequenceName = "ID_SEQ",initialValue = 1)
    private Long id;

    @Column(name = "uniqId")
    private String uniqId;

    @Column(name = "type")
    private int type;

    @Column(name = "responseTime")
    private long responseTime;
}
