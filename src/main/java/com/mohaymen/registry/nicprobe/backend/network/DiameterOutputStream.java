package com.mohaymen.registry.nicprobe.backend.network;

import com.google.gson.Gson;
import com.mohaymen.registry.nicprobe.backend.dao.DiameterRepository;
import com.mohaymen.registry.nicprobe.backend.bl.*;
import com.mohaymen.registry.nicprobe.backend.model.DiameterModel;
import com.mohaymen.registry.nicprobe.backend.model.DiameterPojo;
import com.mohaymen.registry.nicprobe.backend.model.ResponseInfoModel;
import org.apache.commons.exec.LogOutputStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.graylog2.gelfclient.GelfMessage;
import org.graylog2.gelfclient.GelfMessageBuilder;
import org.graylog2.gelfclient.transport.GelfTransport;
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
    private GelfTransport transport;
    @Autowired
    private GelfMessageBuilder gelfMessageBuilder;
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
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }


    public void save(ArrayList<DiameterModel> requests) {
        DiameterModel temp;
        try {
            if (requests != null && requests.size() != 0) {
                for (DiameterModel request : requests) {
                    try {
                        temp=diameterRepository.findById(request.getId()).get();
                    }catch (Exception e){
                        temp=null;
                    }

                    if(temp!=null){
                        List<DiameterModel> reqAndResp=new ArrayList<>(2);
                        reqAndResp.add(temp);
                        reqAndResp.add(request);
                        calcResponseTime.diameterCalculate(reqAndResp);
                    }else {
                        diameterRepository.save(request);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void send(ResponseInfoModel responseInfoModel) {

        GelfMessage message = gelfMessageBuilder.message(objectMapper.toJson(responseInfoModel))
                .build();

        try {
            transport.send(message);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
