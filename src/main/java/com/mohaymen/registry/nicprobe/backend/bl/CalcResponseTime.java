package com.mohaymen.registry.nicprobe.backend.bl;

import com.google.gson.Gson;
import com.mohaymen.registry.nicprobe.backend.model.DiameterModel;
import com.mohaymen.registry.nicprobe.backend.model.GsmMapModel;
import com.mohaymen.registry.nicprobe.backend.model.ResponseInfoModel;
import org.graylog2.gelfclient.GelfMessage;
import org.graylog2.gelfclient.GelfMessageBuilder;
import org.graylog2.gelfclient.transport.GelfTransport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class CalcResponseTime {
    @Autowired
    private GelfTransport transport;
    @Autowired
    private GelfMessageBuilder gelfMessageBuilder;
    @Autowired
    private Gson objectMapper;

    public void GsmMapCalculate(List<GsmMapModel> reqAndResp) {

        try {
//                if (reqAndResp.get(0).getSrcIp().equals(reqAndResp.get(1).getDesIp())) {
                    long respTime = Math.abs(Long.parseLong(reqAndResp.get(1).getTime()) - Long.parseLong(reqAndResp.get(0).getTime()));
                    ResponseInfoModel responseInfoModel = new ResponseInfoModel();
                    responseInfoModel.setId(reqAndResp.get(0).getId());
                    responseInfoModel.setResponseTime(respTime);
                    responseInfoModel.setType(1);
                    send(responseInfoModel);
//                }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void diameterCalculate(List<DiameterModel> reqAndResp) {

        try {
//                if (reqAndResp.get(0).getSrcIp().equals(reqAndResp.get(1).getDesIp())) {
                    long respTime = Math.abs(Long.parseLong(reqAndResp.get(1).getTime()) - Long.parseLong(reqAndResp.get(0).getTime()));
                    ResponseInfoModel responseInfoModel = new ResponseInfoModel();
                    responseInfoModel.setId(reqAndResp.get(0).getId());
                    responseInfoModel.setResponseTime(respTime);
                    responseInfoModel.setType(2);
                    send(responseInfoModel);
//                }

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
