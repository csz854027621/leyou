package com.leyou.auth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@ConfigurationProperties(prefix = "leyou.jwt")
public class RsaInfoConfig {

    private String secret;
    private String pubKeyPath;
    private String priKeyPath;
    private Integer expire;

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getPubKeyPath() {
        return pubKeyPath;
    }

    public void setPubKeyPath(String pubKeyPath) {
        this.pubKeyPath = pubKeyPath;
    }

    public String getPriKeyPath() {
        return priKeyPath;
    }

    public void setPriKeyPath(String priKeyPath) {
        this.priKeyPath = priKeyPath;
    }

    public Integer getExpire() {
        return expire;
    }

    public void setExpire(Integer expire) {
        this.expire = expire;
    }
}
