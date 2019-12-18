package com.leyou.sms.conf;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "leyou.sms.info")
public class SmsConfig {

    private String accesskey;
    private String accessdeysecret;

    public String getAccesskey() {
        return accesskey;
    }

    public void setAccesskey(String accesskey) {
        this.accesskey = accesskey;
    }

    public String getAccessdeysecret() {
        return accessdeysecret;
    }

    public void setAccessdeysecret(String accessdeysecret) {
        this.accessdeysecret = accessdeysecret;
    }


}
