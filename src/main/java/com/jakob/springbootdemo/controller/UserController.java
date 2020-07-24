package com.jakob.springbootdemo.controller;

import com.jakob.springbootdemo.dto.UserRegisterRequest;
import com.jakob.springbootdemo.service.UserService;
import com.jakob.springbootdemo.utils.CurrentUserUtils;
import com.jakob.springbootdemo.vo.UserVO;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    private final CurrentUserUtils currentUserUtils;

    public UserController(CurrentUserUtils currentUserUtils, UserService userService) {
        this.currentUserUtils = currentUserUtils;
        this.userService = userService;
    }

    @PostMapping("/sign-up")
    public ResponseEntity signUp(@RequestBody @Valid UserRegisterRequest userRegisterRequest) {
        userService.save(userRegisterRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    public ResponseEntity<Page<UserVO>> getAllUser(@RequestParam(value = "pageNum", defaultValue = "0") int pageNum, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        System.out.println("当前访问该接口的用户为：" + currentUserUtils.currentUser().getUsername());
        Page<UserVO> page = userService.getAll(pageNum, pageSize);
        return ResponseEntity.ok().body(page);
    }

    @DeleteMapping("/{username}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity deleteUser(@PathVariable String username) {
        userService.deleteUser(username);
        return ResponseEntity.ok().build();
    }
}
