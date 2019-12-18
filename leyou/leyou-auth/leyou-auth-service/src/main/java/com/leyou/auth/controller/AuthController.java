package com.leyou.auth.controller;

import com.leyou.auth.config.RsaInfoConfig;
import com.leyou.auth.pojo.UserInfo;
import com.leyou.auth.service.AuthService;
import com.leyou.auth.utils.JwtUtils;
import com.leyou.auth.utils.RsaUtils;
import com.leyou.common.utils.CookieUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.PrivateKey;

@Controller
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private RsaInfoConfig rsaInfoconfig;
    /**
     * 登录权限控制，颁发token
     * @param username
     * @param password
     * @param request
     * @param response
     * @return
     */
    @PostMapping("authVerify")
    public ResponseEntity<Void> authVerify(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            HttpServletRequest request, HttpServletResponse response
    ) {

        String s = authService.authVerify(username, password);
        if(s==null){
            return ResponseEntity.badRequest().build();
        }
        CookieUtils.setCookie(request,response,"LY_TOKEN",s, rsaInfoconfig.getExpire()*60);

        return ResponseEntity.ok().build();
    }

    /**
     * 解析token信息，回显前台
     * @param token
     * @param request
     * @param response
     * @return
     */
    @GetMapping("verify")
    public ResponseEntity<UserInfo> verify(
            @CookieValue("LY_TOKEN") String token,
            HttpServletRequest request, HttpServletResponse response
    ) {

        UserInfo userInfo = authService.parseToken(token);

        try {
            //重新生成token
            PrivateKey privateKey= RsaUtils.getPrivateKey(rsaInfoconfig.getPriKeyPath());
            String s = JwtUtils.generateToken(userInfo, privateKey, rsaInfoconfig.getExpire());
            CookieUtils.setCookie(request,response,"LY_TOKEN",s, rsaInfoconfig.getExpire()*60);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(userInfo);
    }




}
