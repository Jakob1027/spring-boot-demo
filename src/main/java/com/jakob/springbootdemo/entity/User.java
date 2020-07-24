package com.jakob.springbootdemo.entity;

import com.jakob.springbootdemo.dto.UserRegisterRequest;
import com.jakob.springbootdemo.vo.UserVO;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document("USER")
public class User {

    @Id
    private String id;

    private String username;
    private String password;
    private String fullname;
    private Boolean enabled;
    private List<String> roles;

    public static User of(UserRegisterRequest request) {
        User user = new User();
        user.username = request.getUsername();
        user.fullname = request.getFullname();
        user.enabled = true;
        return user;
    }

    public UserVO toUserVO() {
        UserVO userVO = new UserVO();
        userVO.setUsername(this.username);
        userVO.setFullname(this.fullname);
        return userVO;
    }
}
