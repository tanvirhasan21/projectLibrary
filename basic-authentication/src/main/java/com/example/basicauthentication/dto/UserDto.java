package com.example.basicauthentication.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import javax.management.relation.Role;
import java.util.List;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long id;

    private String name;

    private String email;
    private List<Role> roles;

    private String password;
    private MultipartFile profilePicture;
}
