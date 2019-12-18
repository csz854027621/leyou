package com.leyou.configuration;

import com.leyou.auth.pojo.UserInfo;
import com.leyou.auth.utils.JwtUtils;
import com.leyou.auth.utils.RsaUtils;
import com.leyou.common.utils.CookieUtils;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.commons.lang.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.List;

@Component
@EnableConfigurationProperties({AllowPath.class, LeyouProperties.class})
public class PathZuulFilter extends ZuulFilter {

    @Autowired
    private AllowPath allowPath;


    @Autowired
    private LeyouProperties leyouProperties;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 10;
    }

    @Override
    public boolean shouldFilter() {

        List<String> allowPaths = allowPath.getAllowPaths();
        RequestContext context = RequestContext.getCurrentContext();
        String requestURL = context.getRequest().getRequestURL().toString();
        for (String path : allowPaths) {
            if (StringUtils.contains(requestURL, path)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public Object run() throws ZuulException {

        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();
        String token = CookieUtils.getCookieValue(request, leyouProperties.getCookieName());

        try {
            PublicKey publicKey = RsaUtils.getPublicKey(leyouProperties.getPubKeyPath());
            UserInfo infoFromToken = JwtUtils.getInfoFromToken(token, publicKey);
        } catch (Exception e) {
            e.printStackTrace();
            context.setSendZuulResponse(false);
            context.setResponseStatusCode(HttpStatus.BAD_REQUEST.value());
        }

        return null;
    }
}
