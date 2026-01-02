package com.zjq.java_ee_starter.dto;

import lombok.Data;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Data
public class UserDTO {
    private Integer id;

    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 20, message = "用户名长度3-20字符")
    private String userName;

    @Size(min = 6, max = 20, message = "密码长度6-20字符")
    private String password;

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;

    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    private Integer role; // 0-用户 1-管理员
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
