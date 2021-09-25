package com.zbx.utilapi.config;

import org.aopalliance.intercept.Interceptor;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * @创建人 zbx
 * @创建时间 2021/9/11
 * @描述
 */
public class AuthInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if("OPTIONS".equals(request.getMethod().toUpperCase())) {
            return true;
        }
        String auth = request.getHeader("auth");
        if (auth!=null && auth.equals("4QrcOUm6Wau+VuBX8g+IPg==")) {
            return true;
        }
        PrintWriter writer = response.getWriter();
        writer.println("403");
//        writer.flush();
        writer.close();
        return false;
    }
}
