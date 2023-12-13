package com.example.basicauthentication.service;

import com.example.basicauthentication.dto.UserDto;
import com.example.basicauthentication.entity.User;

import java.util.List;

public interface UserService {
    User saveUser(User user);
    User updateUser(User newuser,User olduser);
    User findUserByEmail(String name);
    List<User> getAllUsers();
}
