package com.example.basicauthentication.service;


import com.example.basicauthentication.entity.Project;
import org.springframework.stereotype.Service;

import java.util.List;


public interface APIService {
    List<Project> getAllProjects();
}
