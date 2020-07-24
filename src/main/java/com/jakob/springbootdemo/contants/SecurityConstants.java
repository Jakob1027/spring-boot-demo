package com.jakob.springbootdemo.contants;

/**
 * Spring Security 相关配置常量
 */
public class SecurityConstants {

    /**
     * 登录地址
     */
    public static final String AUTH_LOGIN_URL = "/auth/login";

    /**
     * 角色的key
     **/
    public static final String ROLE_CLAIMS = "rol";

    /**
     * JWT签名密钥硬编码到应用程序代码中，应该存放在环境变量或.properties文件中。
     */
    public static final String JWT_SECRET_KEY = "C*F-JaNdRgUkXn2r5u8x/A?D(G+KbPeShVmYq3s6v9y$B&E)H@McQfTjWnZr4u7w";

    /**
     * 过期时间
     */
    public static final long EXPIRATION = 60 * 60L;

    /**
     * remember 的过期时间
     */
    public static final long EXPIRATION_REMEMBER = 7 * 24 * 3600;


    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String TOKEN_TYPE = "JWT";

    private SecurityConstants() {
    }
}
