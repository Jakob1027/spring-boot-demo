package com.jakob.springbootdemo.service;

import com.jakob.springbootdemo.dto.UserRegisterRequest;
import com.jakob.springbootdemo.entity.User;
import com.jakob.springbootdemo.exception.NotFound;
import com.jakob.springbootdemo.repository.UserRepository;
import com.jakob.springbootdemo.vo.UserVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    private final BCryptPasswordEncoder encoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new NotFound(String.format("用户 %s 不存在", username)));
    }

    public void save(UserRegisterRequest userRegisterRequest) {
        checkUsernameNotExist(userRegisterRequest.getUsername());
        User user = User.of(userRegisterRequest);
        user.setPassword(encoder.encode(userRegisterRequest.getPassword()));
        List<String> roles = new ArrayList<>();
        roles.add("USER");
        user.setRoles(roles);
        userRepository.save(user);
    }

    public Page<UserVO> getAll(int pageNum, int pageSize) {
        return userRepository.findAll(PageRequest.of(pageNum, pageSize)).map(User::toUserVO);
    }

    private void checkUsernameNotExist(String username) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException(String.format("用户名 %s 已存在", username));
        }
    }

    public void deleteUser(String username) {
        userRepository.deleteByUsername(username);
    }
}
