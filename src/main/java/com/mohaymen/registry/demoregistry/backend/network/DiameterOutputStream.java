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
public class DiameterOutputStream extends LogOutputStream {

    @Autowired
    private DiameterRepository diameterRepository;
    @Autowired
    private CalcResponseTime calcResponseTime;

    @Autowired
    private Gson objectMapper;

    private static final Logger logger = LogManager.getLogger(DiameterOutputStream.class);

    @Override
    protected void processLine(String line, int level) {
        if (line.startsWith("{\"timestamp\"")) {
            save(convert(line));
        }
    }


    public ArrayList<DiameterModel> convert(String json) {
        ArrayList<DiameterModel> list = null;
        DiameterPojo diameterPojo=null;
        try {
            diameterPojo = objectMapper.fromJson(json, DiameterPojo.class);
        }catch (Exception ex){
            //nothing to do ...
        }
            ///
        try {
            if(diameterPojo!=null) {
                list = new ArrayList<>();
                for (String sessionId : diameterPojo.getLayers().getDiameter_Session_Id()) {
                    DiameterModel diameter = new DiameterModel();
                    diameter.setTime(diameterPojo.getTimestamp());
                    diameter.setId(sessionId);
                    list.add(diameter);
                }

                for (int i = 0; i < list.size(); i++) {
                    list.get(i).setId(diameterPojo.getLayers().getDiameter_Session_Id().get(i));
                    list.get(i).setEpochTime(diameterPojo.getLayers().getFrame_time_epoch().get(0));
                    list.get(i).setSrcIp(diameterPojo.getLayers().getIp_src().get(0));
                    list.get(i).setDesIp(diameterPojo.getLayers().getIp_dst().get(0));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }


    public void save(ArrayList<DiameterModel> requests) {
        try {
            if (requests != null && requests.size() != 0) {
                for (DiameterModel request : requests) {
                    if (!diameterRepository.findById(request.getId()).isPresent()) {
                        diameterRepository.save(request);
                    } else {
                        List<DiameterModel> reqAndResp = new ArrayList<>(2);
                        reqAndResp.add(request);
                        reqAndResp.add(diameterRepository.findById(request.getId()).get());
                        calcResponseTime.diameterCalculate(reqAndResp);
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
