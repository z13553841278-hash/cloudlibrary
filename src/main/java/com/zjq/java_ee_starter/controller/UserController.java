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
        // 设置默认值
        if (user.getRole() == null) {
            user.setRole(0); // 默认普通用户
        }
        if (user.getStatus() == null) {
            user.setStatus(1); // 默认启用
        }

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

    // 用户管理页面
    @GetMapping("/manage")
    public String manage(Model model, HttpSession session) {
        System.out.println("=== 开始处理 /user/manage 请求 ===");

        List<User> users = userService.findAllUsers();
        System.out.println("查询到用户数量: " + users.size());

        // 统计信息
        int totalUsers = userService.countUsers();
        int activeUsers = (int) users.stream().filter(u -> u.getStatus() == 1).count();
        int adminUsers = (int) users.stream().filter(u -> u.getRole() == 1).count();

        model.addAttribute("users", users);
        model.addAttribute("userCount", totalUsers);
        model.addAttribute("activeUserCount", activeUsers);
        model.addAttribute("adminCount", adminUsers);
        model.addAttribute("loginUser", loginService.getLoginUser(session));

        System.out.println("=== 准备返回视图: user/manage ===");
        return "user/manage";
    }

    // 管理页面添加用户
    @GetMapping("/manage/add")
    public String manageAddForm(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        model.addAttribute("isManageForm", true); // 标记为管理表单
        return "user/form";
    }

    // 管理页面编辑用户
    @GetMapping("/manage/edit/{id}")
    public String manageEditForm(@PathVariable Integer id, Model model) {
        User user = userService.findUserbyId(id);
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        model.addAttribute("userDTO", userDTO);
        model.addAttribute("isManageEdit", true); // 标记为管理编辑
        return "user/form";
    }

    // 管理页面更新用户
    @PostMapping("/manage/update")
    public String manageUpdate(@Valid @ModelAttribute UserDTO userDTO,
            BindingResult result, Model model) {
        // 验证错误处理（密码在编辑时可选）
        if (result.hasErrors()) {
            // 编辑时密码为空不报错
            if (userDTO.getId() != null && result.hasFieldErrors("password") &&
                    userDTO.getPassword() == null || userDTO.getPassword().isEmpty()) {
                // 移除密码错误
                result.getFieldErrors("password").clear();
            }
            if (result.hasErrors()) {
                model.addAttribute("isManageEdit", true);
                return "user/form";
            }
        }

        // 用户名唯一性检查（编辑时）
        if (userDTO.getId() != null) {
            User existingUser = userService.findUserbyName(userDTO.getUserName());
            if (existingUser != null && !existingUser.getId().equals(userDTO.getId())) {
                result.rejectValue("userName", "error.user", "用户名已存在");
                model.addAttribute("isManageEdit", true);
                return "user/form";
            }
        }

        // 转换为User实体
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);

        // 编辑时，如果密码为空，保留原密码
        if (userDTO.getId() != null && (userDTO.getPassword() == null || userDTO.getPassword().isEmpty())) {
            User existingUser = userService.findUserbyId(userDTO.getId());
            user.setPassword(existingUser.getPassword());
        }

        // 设置默认值
        if (user.getRole() == null) {
            user.setRole(0); // 默认普通用户
        }
        if (user.getStatus() == null) {
            user.setStatus(1); // 默认启用
        }

        // 更新用户
        boolean success = userService.saveUser(user);
        if (!success) {
            model.addAttribute("error", "更新失败");
            model.addAttribute("isManageEdit", true);
            return "user/form";
        }

        return "redirect:/user/manage";
    }

    // 管理页面保存用户（添加）
    @PostMapping("/manage/save")
    public String manageSave(@Valid @ModelAttribute UserDTO userDTO,
            BindingResult result, Model model) {
        System.out.println("=== 开始处理 /user/manage/save 请求 ===");
        System.out.println("UserDTO: " + userDTO);

        // 手动验证密码（添加时必填）
        if (userDTO.getPassword() == null || userDTO.getPassword().isEmpty()) {
            result.rejectValue("password", "error.password", "密码不能为空");
        }

        // 验证错误处理
        if (result.hasErrors()) {
            System.out.println("验证错误: " + result.getAllErrors());
            model.addAttribute("isManageForm", true);
            return "user/form";
        }

        // 用户名唯一性检查
        if (userDTO.getId() == null) {
            User existingUser = userService.findUserbyName(userDTO.getUserName());
            if (existingUser != null) {
                System.out.println("用户名已存在: " + userDTO.getUserName());
                result.rejectValue("userName", "error.user", "用户名已存在");
                model.addAttribute("isManageForm", true);
                return "user/form";
            }
        }

        // 转换为User实体
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        // 设置默认值
        if (user.getRole() == null) {
            user.setRole(0); // 默认普通用户
        }
        if (user.getStatus() == null) {
            user.setStatus(1); // 默认启用
        }

        System.out.println("准备保存用户: " + user);

        // 保存用户
        boolean success = userService.saveUser(user);
        System.out.println("保存结果: " + success);

        if (!success) {
            model.addAttribute("error", "保存失败");
            model.addAttribute("isManageForm", true);
            return "user/form";
        }

        System.out.println("=== 保存成功，重定向到 /user/manage ===");
        return "redirect:/user/manage";
    }

    // 管理页面删除用户
    @GetMapping("/manage/delete/{id}")
    public String manageDelete(@PathVariable Integer id) {
        userService.deleteUserbyId(id);
        return "redirect:/user/manage";
    }
}
