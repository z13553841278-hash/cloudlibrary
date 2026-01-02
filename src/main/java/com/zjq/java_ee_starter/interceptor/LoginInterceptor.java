package com.zjq.java_ee_starter.interceptor;

import com.zjq.java_ee_starter.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    // private final LoginService loginService;

    // public LoginInterceptor(LoginService loginService){
    // this.loginService = loginService;
    // }

    @Override
    public boolean preHandle(HttpServletRequest request,
            HttpServletResponse response,
            Object handler) throws Exception {

        HttpSession session = request.getSession();
        String requestURI = request.getRequestURI();
        String method = request.getMethod();

        System.out.println("=== 拦截器检查请求: " + method + " " + requestURI + " ===");

        // 允许通过的路径（登录页面、登录请求、静态资源等）
        if (requestURI.equals("/") ||
                requestURI.equals("/user/login") ||
                requestURI.equals("/user/logout") ||
                requestURI.equals("/user/register") || // 添加注册路径
                requestURI.startsWith("/css/") ||
                requestURI.startsWith("/js/") ||
                requestURI.startsWith("/images/")) {
            System.out.println("允许通过: " + requestURI);
            return true;
        }

        // 检查用户是否已登录
        // if (!loginService.isLoggedIn(session)) {
        if (session.getAttribute("loginUser") == null) {
            // 未登录，重定向到登录页面
            response.sendRedirect("/user/login");
            return false;
        }

        return true;
    }
}
