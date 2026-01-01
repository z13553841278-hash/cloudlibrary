package com.zjq.java_ee_starter.controller;

import com.zjq.java_ee_starter.service.*;
import com.zjq.java_ee_starter.dto.*;
import com.zjq.java_ee_starter.entity.*;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.List;

// @RestController
@Controller
@RequestMapping(value = "/user")
public class UserController {
    private final UserService userService;
    private final LoginService loginService;

    public UserController(UserService userService, LoginService loginService) {
        this.userService = userService;
        this.loginService = loginService;
    }

    // 用户列表页面
    @GetMapping("/list")
    public String list(Model model, HttpSession session) {
        System.out.println("=== 开始处理 /user/list 请求 ===");

        List<User> users = userService.findAllUsers();
        System.out.println("查询到用户数量: " + users.size());

        model.addAttribute("users", users);
        model.addAttribute("userCount", userService.countUsers());
        model.addAttribute("loginUser", loginService.getLoginUser(session));

        System.out.println("=== 准备返回视图: user/list ===");
        return "user/list";
    }

    // 添加用户页面
    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        return "user/form";
    }

    // 编辑用户页面
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Integer id, Model model) {
        User user = userService.findUserbyId(id);
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        model.addAttribute("userDTO", userDTO);
        return "user/form";
    }

    // 保存用户（添加或更新）
    @PostMapping("/save")
    public String save(@Valid @ModelAttribute UserDTO userDTO,
            BindingResult result, Model model) {
        // 验证错误处理
        if (result.hasErrors()) {
            return "user/form";
        }

        // 用户名唯一性检查
        if (userDTO.getId() == null) {
            User existingUser = userService.findUserbyName(userDTO.getUserName());
            if (existingUser != null) {
                result.rejectValue("userName", "error.user", "用户名已存在");
                return "user/form";
            }
        }

        // 转换为User实体
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);

        // 保存用户
        boolean success = userService.saveUser(user);
        if (!success) {
            model.addAttribute("error", "保存失败");
            return "user/form";
        }

        return "redirect:/user/list";
    }

    // 删除用户
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        userService.deleteUserbyId(id);
        return "redirect:/user/list";
    }

    // 查看用户详情
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Integer id, Model model) {
        User user = userService.findUserbyId(id);

        // 模拟业务异常
        // if (true) {
        // throw new RuntimeException("业务异常");
        // }

        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        model.addAttribute("userDTO", userDTO);
        return "user/detail";
    }
}
