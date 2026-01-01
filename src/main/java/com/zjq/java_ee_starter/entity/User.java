package com.zjq.java_ee_starter.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class User {
    private Integer id;
    private String userName;
    private String password;
    private String email;
    private String phone;
    private Integer role; // 0-用户, 1-管理员
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    // public User(String userName, String password,
    // String email, String phone, Integer status) {
    // this.userName = userName;
    // this.password = password;
    // this.email = email;
    // this.phone = phone;
    // this.status = status;
    // }
}
