package com.mohaymen.registry.nicprobe.backend.network;

import com.google.gson.Gson;
import com.mohaymen.registry.nicprobe.backend.dao.GsmMapRepository;
import com.mohaymen.registry.nicprobe.backend.bl.*;
import com.mohaymen.registry.nicprobe.backend.model.GsmMapModel;
import com.mohaymen.registry.nicprobe.backend.model.GsmMapPojo;
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
public class GsmMapOutputStream extends LogOutputStream {

    @Autowired
    private GsmMapRepository gsmMapRepository;

    @Autowired
    private CalcResponseTime calcResponseTime;
    @Autowired
    private Gson objectMapper;
    @Autowired
    private GelfMessageBuilder gelfMessageBuilder;
    @Autowired
    private GelfTransport transport;

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
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

        //
    public void save(ArrayList<GsmMapModel> requests) {
        GsmMapModel temp;
        try {
            if (requests != null && requests.size() != 0) {
                for (GsmMapModel request : requests) {
                    try{
                        temp=gsmMapRepository.findById(request.getId()).get();
                    }catch (Exception e){
                        temp=null;
                    }

                    if(temp!=null){
                        List<GsmMapModel> reqAndResp=new ArrayList<>(2);
                        reqAndResp.add(temp);
                        reqAndResp.add(request);
                        calcResponseTime.GsmMapCalculate(reqAndResp);
                    }else {
                        gsmMapRepository.save(request);
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
