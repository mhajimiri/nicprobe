package com.mohaymen.registry.demoregistry.backend.elk;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Scope("prototype")
public class CalcResponseTime {
    @Autowired
    private ResponseInfoRepository responseInfoRepository;
    @Autowired
    private GsmMapRepository gsmMapRepository;
    @Autowired
    private DiameterRepository diameterRepository;


    public void GsmMapCalculate(){
        List<GsmMapModel> list=gsmMapRepository.findSameReqAndResp();
        System.out.println("req and resp >>>>>>>>>>>>>"+list.size());
    }

    public void DiameterCalculate(){
        ArrayList<DiameterModel> list;
    }
}
