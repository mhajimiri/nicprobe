package com.mohaymen.registry.demoregistry.backend.network;

import com.google.gson.Gson;
import com.mohaymen.registry.demoregistry.backend.elk.*;
import org.apache.commons.exec.LogOutputStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GsmMapOutputStream extends LogOutputStream {

    @Autowired
    private GsmMapRepository gsmMapRepository;

    @Autowired
    private CalcResponseTime calcResponseTime;

    @Autowired
    private Gson objectMapper;

    private static final Logger logger = LogManager.getLogger(GsmMapOutputStream.class);

    @Override
    protected void processLine(String line, int level) {
        if (line.startsWith("{\"timestamp\"")) {
            save(convert(line.trim()));
        }
    }

    public ArrayList<GsmMapModel> convert(String json) {
        ArrayList<GsmMapModel> list=null;
        GsmMapPojo gsmMapPojo=null;
        try {
            gsmMapPojo = objectMapper.fromJson(json, GsmMapPojo.class);
        }catch (Exception ex){
            //nothing to do ...
        }
            ///
        try {
            if(gsmMapPojo!=null) {
                list = new ArrayList<>();
                for (String invokeId : gsmMapPojo.getLayers().getTcap_invokeID()) {
                    GsmMapModel gsmMap = new GsmMapModel();
                    gsmMap.setTime(gsmMapPojo.getTimestamp());
                    gsmMap.setId(invokeId);
                    list.add(gsmMap);
                }

                for (int i = 0; i < list.size(); i++) {
                    list.get(i).setId(gsmMapPojo.getLayers().getTcap_invokeID().get(i) +
                            "#" + gsmMapPojo.getLayers().getTcap_tid().get(i));
                    list.get(i).setEpochTime(gsmMapPojo.getLayers().getFrame_time_epoch().get(0));
                    list.get(i).setSrcIp(gsmMapPojo.getLayers().getIp_src().get(0));
                    list.get(i).setDesIp(gsmMapPojo.getLayers().getIp_dst().get(0));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }


    public void save(ArrayList<GsmMapModel> requests) {
        try {
            if (requests != null && requests.size() != 0) {
                for (GsmMapModel request : requests) {
                    if(!gsmMapRepository.findById(request.getId()).isPresent()){
                        gsmMapRepository.save(request);
                    }else {
                        List<GsmMapModel> reqAndResp=new ArrayList<>(2);
                        reqAndResp.add(request);
                        reqAndResp.add(gsmMapRepository.findById(request.getId()).get());
                        calcResponseTime.GsmMapCalculate(reqAndResp);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
