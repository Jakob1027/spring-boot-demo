package com.jakob.springbootdemo.dto;

import lombok.Data;

/**
 * 用户登录请求DTO
 */
@Data
public class LoginRequest {
    private String username;
    private String password;
    private boolean rememberMe;
}
