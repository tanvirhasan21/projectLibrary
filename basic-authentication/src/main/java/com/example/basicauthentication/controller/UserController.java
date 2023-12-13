package com.example.basicauthentication.controller;

import com.example.basicauthentication.dto.UserDto;
import com.example.basicauthentication.entity.User;
import com.example.basicauthentication.repository.UserRepository;
import com.example.basicauthentication.service.UserServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserServiceImpl userServiceImpl;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/newuser")
    public String userForm(Model model){
        User user=new User();
        model.addAttribute("user",user);
        return "view/addUser";

    }

    /*@PostMapping(value="/createNewUser",consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public  String createNewUser(@Valid User user, BindingResult bindingResult,Model model){
        if(bindingResult.hasErrors()){
            return "view/addUser";
        }

        if(userRepository.findByEmail(user.getEmail())!=null){
            model.addAttribute("repeatingEmail","email already exists.");
            return "view/addUser";
        }
        User newUser=userServiceImpl.saveUser(user);

//        model.addAttribute("registerSuccess","registration successful.");
        return "view/login";
    }*/


    @PostMapping(value = "/createNewUser", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String createNewUser(@Valid UserDto userDto, BindingResult bindingResult, @RequestParam("profilePicture") MultipartFile profilePicture, Model model) {
        if (bindingResult.hasErrors()) {
            return "view/addUser";
        }

        if (userRepository.findByEmail(userDto.getEmail()) != null) {
            model.addAttribute("repeatingEmail", "Email already exists.");
            return "view/addUser";
        }

        if (!profilePicture.isEmpty()) {

            try {
                User user=new User();
                BeanUtils.copyProperties(userDto,user);
                byte[] profilePictureBytes = profilePicture.getBytes();
                user.setProfilePicture(profilePictureBytes);
                User newUser = userServiceImpl.saveUser(user);
            } catch (IOException e) {
                // Handle the file processing error
                model.addAttribute("fileError", "Error processing the profile picture file.");
                return "view/addUser";
            }
        }



        // Handle the user registration success and redirection
        // ...

        return "view/login";
    }




    @GetMapping("/userList")
    public String userList(Model model){
        List<User> userList=userServiceImpl.getAllUsers();

        return "view/projectList";
    }

    @GetMapping("/login")
    public String login(){ return "view/login";
    }

    @GetMapping("/showUpdateForm")
    public String updateForm(Model model){
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        String username = authentication.getName();
        User loggedInUser=userRepository.findByEmail(username);
        System.out.println(loggedInUser);
        User user=new User();

        if(loggedInUser.getProfilePicture()!=null) {
            byte[] profilePicture = loggedInUser.getProfilePicture();
            String encodedImage = Base64.getEncoder().encodeToString(profilePicture);
            encodedImage = "data:image/png;base64,"+encodedImage;
            model.addAttribute("encodedImage", encodedImage);
        }

        model.addAttribute("user",user);
        model.addAttribute("loggedInUser",loggedInUser);
        return "view/updateUser";
    }

    @PostMapping(value="/updateUser", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String performUpdate(UserDto user,Model model,@RequestParam("profilePicture") MultipartFile profilePicture){
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        String username = authentication.getName();
        User loggedInUser=userRepository.findByEmail(username);
        User updatedUser=new User();
        if (!profilePicture.isEmpty()) {
            try {
                BeanUtils.copyProperties(user,updatedUser);
                byte[] profilePictureBytes = profilePicture.getBytes();
                updatedUser.setProfilePicture(profilePictureBytes);
                User newUser = userServiceImpl.updateUser(updatedUser, loggedInUser);
            } catch (IOException e) {
                // Handle the file processing error
                model.addAttribute("fileError", "Error processing the profile picture file.");
                return "view/addUser";
            }
        }else{
            BeanUtils.copyProperties(user,updatedUser);
            User newUser = userServiceImpl.updateUser(updatedUser,loggedInUser);
        }
        return "redirect:/logout";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/"; // Redirect to your login page
    }




}
