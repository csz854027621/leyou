package com.leyou.auth.service;

import com.leyou.auth.client.AuthClient;
import com.leyou.auth.config.RsaInfoConfig;
import com.leyou.auth.pojo.UserInfo;
import com.leyou.auth.utils.JwtUtils;
import com.leyou.auth.utils.RsaUtils;
import com.leyou.user.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;
import java.security.PublicKey;

@Service
@EnableConfigurationProperties(RsaInfoConfig.class)
public class AuthService {

    @Autowired
    private AuthClient authClient;

    @Autowired
    private RsaInfoConfig rsaInfoConfig;

    /**
     * 办法token
     * @param username
     * @param password
     * @return
     */
    public String authVerify(String username, String password) {
        User user = authClient.queryByUsernameAndPassword(username, password);
        if (user == null) return null;
        UserInfo userInfo = new UserInfo();
        userInfo.setId(user.getId());
        userInfo.setUsername(user.getUsername());
        try {
            RsaUtils.generateKey(rsaInfoConfig.getPubKeyPath(),
                    rsaInfoConfig.getPriKeyPath(), rsaInfoConfig.getSecret());
            PrivateKey privateKey = RsaUtils.getPrivateKey(rsaInfoConfig.getPriKeyPath());

            //获取token
            String s = JwtUtils.generateToken(userInfo, privateKey, rsaInfoConfig.getExpire());
            return s;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        //返回token
    }

    public UserInfo parseToken(String token) {
        try {

            PublicKey publicKey = RsaUtils.getPublicKey(rsaInfoConfig.getPubKeyPath());
            UserInfo infoFromToken = JwtUtils.getInfoFromToken(token, publicKey);
            return infoFromToken;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


    }
}
