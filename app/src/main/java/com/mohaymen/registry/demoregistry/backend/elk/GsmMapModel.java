package com.mohaymen.registry.demoregistry.backend.elk;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
@Getter
@Setter

@Entity
@Table(name = "Gsm_Map")
public class GsmMapModel implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "id_sequence")
    @SequenceGenerator(name = "id_sequence",sequenceName = "ID_SEQ",initialValue = 1)
    private Long id;

    @Column(name = "tid")
    private String tid;
    @Column(name = "time")
    private String time;
    @Column(name = "invokeId")
    private String invokeId;
    @Column(name = "calledDigit")
    private String calledDigit;
    @Column(name = "callingDigit")
    private String callingDigit;
}
