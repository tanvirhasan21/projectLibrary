package com.example.basicauthentication.controller;

import com.example.basicauthentication.entity.User;
import com.example.basicauthentication.repository.UserRepository;
import com.example.basicauthentication.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class UserFetcherController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserServiceImpl userServiceImpl;
    private String name;



    @GetMapping("/getAllUsers")
    public List<User> getAllUser(){
        List<User> userList=userServiceImpl.getAllUsers();
        return userList;
    }

    @GetMapping("/getName/{id}")
    public String getUser(@PathVariable("id") Long id){
        Optional<User> user=userRepository.findById(id);
        String username=user.get().getName();
        System.out.println(user.toString());
        return username;
    }
}
