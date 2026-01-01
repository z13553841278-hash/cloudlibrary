package com.zjq.java_ee_starter.controller;

import com.zjq.java_ee_starter.dto.LoginDTO;
import com.zjq.java_ee_starter.dto.UserDTO;
import com.zjq.java_ee_starter.service.LoginService;
import com.zjq.java_ee_starter.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

    private final LoginService loginService;
    private final UserService userService;

    public LoginController(LoginService loginService, UserService userService) {
        this.loginService = loginService;
        this.userService = userService;
    }

    /**
     * 显示登录页面
     */
    @GetMapping("/user/login")
    public String showLoginPage(
            Model model,
            HttpSession session,
            HttpServletRequest request) {
        // 如果已登录，重定向到首页
        if (loginService.isLoggedIn(session)) {
            return "redirect:" + request.getRequestURI();
        }

        // 添加登录表单对象
        model.addAttribute("loginDTO", new LoginDTO());
        return "user/login";
    }

    /**
     * 处理登录请求
     */
    @PostMapping("/user/login")
    public String processLogin(@ModelAttribute LoginDTO loginDto,
            HttpSession session,
            Model model) {
        String userName = loginDto.getUserName();
        String password = loginDto.getPassword();

        if (loginService.login(userName, password, session)) {
            // 登录成功，重定向到首页
            return "redirect:/";
        } else {
            // 登录失败，返回错误信息
            model.addAttribute("error", "用户名或密码错误");
            model.addAttribute("loginDTO", loginDto);
            return "user/login";
        }
    }

    /**
     * 处理退出请求
     */
    @GetMapping("/user/logout")
    public String logout(HttpSession session) {
        loginService.logout(session);
        return "redirect:/user/login";
    }

    /**
     * 显示注册页面
     */
    @GetMapping("/user/register")
    public String showRegisterPage(Model model, HttpSession session) {
        // 如果已登录，重定向到首页
        if (loginService.isLoggedIn(session)) {
            return "redirect:/";
        }

        model.addAttribute("userDTO", new UserDTO());
        return "user/register";
    }

    /**
     * 处理注册请求
     */
    @PostMapping("/user/register")
    public String processRegister(@ModelAttribute UserDTO userDto, Model model) {
        try {
            userService.register(userDto);
            model.addAttribute("success", "注册成功，请登录");
            return "redirect:/user/login";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("userDTO", userDto);
            return "user/register";
        }
    }
}
