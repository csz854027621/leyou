package com.leyou.sms.service;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.leyou.sms.conf.SmsConfig;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@EnableConfigurationProperties(SmsConfig.class)
public class SmsService {

    @Autowired
    SmsConfig smsConfig;


    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "user.login.sms.verify.queue", durable = "true",
                    ignoreDeclarationExceptions = "true")
            , exchange = @Exchange(name = "user.exchange.verify", type = ExchangeTypes.TOPIC), ignoreDeclarationExceptions = "true"
            , key = "user.send.verify")
    )
    public void sendSms(Map<String, String> map) throws Exception {


        if (map == null) return;
        String phone = map.get("phone");
        String verify = map.get("verify");
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou",
                smsConfig.getAccesskey(), smsConfig.getAccessdeysecret());

        IAcsClient client = new DefaultAcsClient(profile);
        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");

        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", phone);
        request.putQueryParameter("SignName", "游乐商城");
        request.putQueryParameter("TemplateCode", "SMS_180046435");
        request.putQueryParameter("TemplateParam", "{code:" + verify + "}");
        CommonResponse response = client.getCommonResponse(request);
        System.out.println(response.getData());

    }

}
