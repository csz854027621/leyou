package com.leyou.sms;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
public class SmsTest {

    public static void main(String[] args) {

        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou",

                "LTAI4FqPsaMFnV6KU8JScwU1",
                "4197IokSnQRql8jJC0aLDwhfvw6Esr");
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", "13755748007");
        request.putQueryParameter("SignName", "游乐商城");
        request.putQueryParameter("TemplateCode", "SMS_180046435");
        request.putQueryParameter("TemplateParam", "{code:555555}");

        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }

    }
}
