package com.mohaymen.registry.demoregistry.backend.elk;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter

@Entity
@Table(name = "Diameter")
public class DiameterModel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "id_sequence")
    @SequenceGenerator(name = "id_sequence",sequenceName = "ID_SEQ",initialValue = 1)
    private Long id;
    @Column(name="hopByhopId")
    private String hopByhopId;
    @Column(name = "sessionId")
    private String sessionId;
    @Column(name = "time")
    private String time;
    @Column(name = "endToEndId")
    private String endToEndId;
    @Column(name = "status",columnDefinition="tinyint(1) default 0")
    private int status;
}
