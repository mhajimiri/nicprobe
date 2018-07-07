package com.mohaymen.registry.demoregistry.backend.elk;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
public class CalcResponseTime {
    @Autowired
    private ResponseInfoRepository responseInfoRepository;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private Gson objectMapper;


    public void GsmMapCalculate(List<GsmMapModel> reqAndResp) {
        try {
            if (!reqAndResp.isEmpty() && reqAndResp != null && reqAndResp.size() == 2) {
                if(reqAndResp.get(0).getSrcIp().equals(reqAndResp.get(1).getDesIp())) {
                    long resp0 = Long.parseLong(reqAndResp.get(0).getTime());
                    long resp1 = Long.parseLong(reqAndResp.get(1).getTime());
                    long respTime = Math.abs(resp1 - resp0);
                    //
                    long r0 = Long.parseLong(reqAndResp.get(0).getEpochTime().replace(".", ""));
                    long r1 = Long.parseLong(reqAndResp.get(1).getEpochTime().replace(".", ""));
                    long respEpochTime = Math.abs(r1 - r0);
                    //
                    ResponseInfoModel responseInfoModel = new ResponseInfoModel();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                    responseInfoModel.setTimeF(sdf.format(new Date(resp0)));
                    responseInfoModel.setTimeL(sdf.format(new Date(resp1)));
                    responseInfoModel.setEpochTimeF(String.valueOf(r0));
                    responseInfoModel.setEpochTimeL(String.valueOf(r1));
                    responseInfoModel.setId(reqAndResp.get(0).getId());
                    responseInfoModel.setResponseTime(respTime);
                    responseInfoModel.setRespEpochTime(respEpochTime);
                    responseInfoModel.setType(1);
//                responseInfoRepository.save(responseInfoModel);
                    send(responseInfoModel);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void diameterCalculate(List<DiameterModel> reqAndResp) {
        try {
            if (!reqAndResp.isEmpty() && reqAndResp != null && reqAndResp.size() == 2) {
                if(reqAndResp.get(0).getSrcIp().equals(reqAndResp.get(1).getDesIp())) {
                    long resp0 = Long.parseLong(reqAndResp.get(0).getTime());
                    long resp1 = Long.parseLong(reqAndResp.get(1).getTime());
                    long respTime = Math.abs(resp1 - resp0);
                    //
                    long r0 = Long.parseLong(reqAndResp.get(0).getEpochTime().replace(".", ""));
                    long r1 = Long.parseLong(reqAndResp.get(1).getEpochTime().replace(".", ""));
                    long respEpochTime = Math.abs(r1 - r0);
                    //
                    ResponseInfoModel responseInfoModel = new ResponseInfoModel();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                    responseInfoModel.setTimeF(sdf.format(new Date(resp0)));
                    responseInfoModel.setTimeL(sdf.format(new Date(resp1)));
                    responseInfoModel.setEpochTimeF(String.valueOf(r0));
                    responseInfoModel.setEpochTimeL(String.valueOf(r1));
                    responseInfoModel.setId(reqAndResp.get(0).getId());
                    responseInfoModel.setResponseTime(respTime);
                    responseInfoModel.setRespEpochTime(respEpochTime);
                    responseInfoModel.setType(2);
//                responseInfoRepository.save(responseInfoModel);
                    send(responseInfoModel);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void send(ResponseInfoModel responseInfoModel) {
        GrayLogJson grayLogJson=new GrayLogJson();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        grayLogJson.setLevel("1");
        grayLogJson.set_some_info("test");
        grayLogJson.setHost("MTN");
        grayLogJson.setVersion("1.1");
        grayLogJson.setShort_message(objectMapper.toJson(responseInfoModel));
        HttpEntity entity = new HttpEntity<GrayLogJson>(grayLogJson, headers);
        restTemplate.exchange("http://172.16.1.162:12202/gelf", HttpMethod.POST, entity, String.class);
    }
}
