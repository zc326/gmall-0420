package com.atguigu.gmall.cart.interceptor;

import com.atguigu.gmall.cart.config.JwtProperties;
import com.atguigu.gmall.common.bean.UserInfo;
import com.atguigu.gmall.common.utils.CookieUtils;
import com.atguigu.gmall.common.utils.JwtUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.UUID;

@Component
@EnableConfigurationProperties(JwtProperties.class)
public class LoginInterceptor implements HandlerInterceptor {

    private static final ThreadLocal<UserInfo> THREAD_LOCAL = new ThreadLocal<>();

    @Autowired
    private JwtProperties properties;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        UserInfo userInfo = new UserInfo();

        String userKey = CookieUtils.getCookieValue(request, this.properties.getUserKey());
        if (StringUtils.isBlank(userKey)){
            userKey = UUID.randomUUID().toString();
            CookieUtils.setCookie(request, response, this.properties.getUserKey(), userKey, this.properties.getExpire());
        }

        userInfo.setUserkey(userKey);

        String token = CookieUtils.getCookieValue(request, this.properties.getCookieName());
        if (StringUtils.isBlank(token)){
            THREAD_LOCAL.set(userInfo);
            return true;
        }

        try {
            Map<String, Object> map = JwtUtils.getInfoFromToken(token, this.properties.getPublicKey());
            userInfo.setUserId(Long.valueOf(map.get("userId").toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        THREAD_LOCAL.set(userInfo);

//        request.setAttribute("userId", "xxxxx");
//        THREAD_LOCAL.set(new UserInfo(10l,  "233uoew-23arwe-23432sdf-234s"));

//        System.out.println("拦截器的前置方法");
        return true;
    }
    public static UserInfo getUserInfo(){
        return THREAD_LOCAL.get();
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        THREAD_LOCAL.remove();
    }
}
