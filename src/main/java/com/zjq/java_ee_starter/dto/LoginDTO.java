package com.zjq.java_ee_starter.dto;

import lombok.Data;

@Data
public class LoginDTO {
    private String userName;
    private String password;
    private Boolean rememberMe;
}
