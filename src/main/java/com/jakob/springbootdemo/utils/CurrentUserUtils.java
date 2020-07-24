package com.jakob.springbootdemo.utils;

import com.jakob.springbootdemo.entity.User;
import com.jakob.springbootdemo.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CurrentUserUtils {
    private final UserService userService;

    public CurrentUserUtils(UserService userService) {
        this.userService = userService;
    }

    public User currentUser() {
        return userService.findByUsername(getCurrentUsername());
    }

    private static String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() != null) {
            return (String) authentication.getPrincipal();
        }
        return null;
    }
}

