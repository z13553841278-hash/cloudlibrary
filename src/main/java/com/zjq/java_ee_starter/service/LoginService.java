package com.zjq.java_ee_starter.service;

import com.zjq.java_ee_starter.entity.User;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;

@Service
public class LoginService {
    private final UserService userService;

    public LoginService(UserService userService) {
        this.userService = userService;
    }

    /**
     * 用户登录
     */
    public boolean login(String userName, String password, HttpSession session) {
        if (userService.validateUser(userName, password)) {
            // 登录成功，将用户信息存入session
            User user = userService.findUserbyName(userName);
            if (user != null) {
                session.setAttribute("loginUser", user);
                session.setAttribute("userId", user.getId());
                session.setAttribute("userName", user.getUserName());
                session.setAttribute("role", user.getRole());
                return true;
            }
        }
        return false;
    }

    /**
     * 用户退出
     */
    public void logout(HttpSession session) {
        session.removeAttribute("loginUser");
        session.invalidate();
    }

    /**
     * 检查用户是否已登录
     */
    public boolean isLoggedIn(HttpSession session) {
        return session.getAttribute("loginUser") != null;
    }

    /**
     * 获取当前登录用户
     */
    public User getLoginUser(HttpSession session) {
        return (User) session.getAttribute("loginUser");
    }
}
