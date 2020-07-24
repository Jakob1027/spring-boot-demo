package com.jakob.springbootdemo.config;

import com.jakob.springbootdemo.exception.JwtAccessDeniedHandler;
import com.jakob.springbootdemo.exception.JwtAuthenticationEntryPoint;
import com.jakob.springbootdemo.filter.JwtAuthenticationFilter;
import com.jakob.springbootdemo.filter.JwtAuthorizationFilter;
import com.jakob.springbootdemo.service.UserDetailsServiceImpl;
import com.jakob.springbootdemo.utils.JwtTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Spring Security 配置类
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsServiceImpl).passwordEncoder(bCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and()
                // 禁用 CSRF
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/auth/login", "/users/sign-up").permitAll()
                // 指定路径下的资源需要认证了用户才能访问
                .antMatchers("/**").authenticated()
                .antMatchers(HttpMethod.DELETE, "/**").hasRole("ADMIN")
                // 其他都放行
                .anyRequest().permitAll()
                .and()
                // 添加自定义Filter
                .addFilter(new JwtAuthenticationFilter(authenticationManager(), jwtTokenUtils))
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), userDetailsServiceImpl, jwtTokenUtils))
                // 不需要Session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                // 授权异常处理
                .exceptionHandling().authenticationEntryPoint(new JwtAuthenticationEntryPoint())
                .accessDeniedHandler(new JwtAccessDeniedHandler());
        http.headers().frameOptions().disable();


        super.configure(http);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**");
    }
}
