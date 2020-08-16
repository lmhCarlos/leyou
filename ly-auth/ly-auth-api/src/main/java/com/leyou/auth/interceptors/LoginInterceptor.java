package com.leyou.auth.interceptors;

import com.leyou.auth.constants.JwtConstants;
import com.leyou.auth.dto.Payload;
import com.leyou.auth.dto.UserDetails;
import com.leyou.auth.utils.JwtUtils;
import com.leyou.auth.utils.UserContext;
import com.leyou.common.execption.LyException;
import com.leyou.common.utils.CookieUtils;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    private final JwtUtils jwtUtils;

    public LoginInterceptor(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            String jwt = CookieUtils.getCookieValue(request, JwtConstants.COOKIE_NAME);
            Payload payload = jwtUtils.parseJwt(jwt);
            UserDetails userDetails = payload.getUserDetail();
            UserContext.setUser(userDetails);

//            System.out.println("woshifdsfafadfsdafa");
            return true;
        } catch (JwtException e) {
            throw new LyException("JWT无效或过期!",401);
        }catch (IllegalArgumentException e){
            throw new LyException("用户未登录!",401);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // 业务结束后，移除用户
        UserContext.removeUser();
    }
}
