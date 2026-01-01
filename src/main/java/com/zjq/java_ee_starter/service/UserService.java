package com.zjq.java_ee_starter.service;

import org.springframework.stereotype.Service;
import java.util.List;
import com.zjq.java_ee_starter.mapper.*;
import com.zjq.java_ee_starter.entity.*;
import com.zjq.java_ee_starter.dto.UserDTO;

@Service
public class UserService {
    private final UserMapper userMapper;

    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public List<User> findAllUsers() {
        return userMapper.findAllUsers();
    }

    public User findUserbyId(Integer id) {
        return userMapper.findUserbyId(id);
    }

    public User findUserbyName(String userName) {
        return userMapper.findUserbyName(userName);
    }

    public List<User> findUsersbyMultipleCondition(User user) {
        return userMapper.selectUsersXml(user);
    }

    public List<User> findUsersbyEmails(String[] emailList) {
        return userMapper.selectUsersByEmailListXml(emailList);
    }

    public boolean saveUser(User user) {
        if (user.getId() == null) {
            // 新增用户
            return userMapper.insertUser(user) > 0;
        } else {
            // 更新用户
            return userMapper.updateUserbyId(user) > 0;
        }
    }

    public boolean deleteUserbyId(Integer id) {
        return userMapper.deleteUserbyId(id) > 0;
    }

    public int countUsers() {
        return userMapper.countUsers();
    }

    /**
     * 验证用户登录
     */
    public boolean validateUser(String userName, String password) {
        User user = findUserbyName(userName);
        if (user != null) {
            // 实际项目中这里应该使用加密密码比较
            return user.getPassword().equals(password) && user.getStatus() == 1;
        }
        return false;
    }

    /**
     * 用户注册
     */
    public void register(UserDTO userDto) {
        // 检查用户名是否已存在
        if (findUserbyName(userDto.getUserName()) != null) {
            throw new RuntimeException("用户名已存在");
        }

        // 检查邮箱是否已存在
        if (userMapper.findUserbyEmail(userDto.getEmail()) != null) {
            throw new RuntimeException("邮箱已被注册");
        }

        User user = new User();
        user.setUserName(userDto.getUserName());
        user.setPassword(userDto.getPassword()); // 实际项目中应该加密
        user.setEmail(userDto.getEmail());
        user.setPhone(userDto.getPhone());
        user.setRole(userDto.getRole() != null ? userDto.getRole() : 0); // 默认注册用户
        user.setStatus(1); // 默认启用

        if (!saveUser(user)) {
            throw new RuntimeException("注册失败");
        }
    }
}
