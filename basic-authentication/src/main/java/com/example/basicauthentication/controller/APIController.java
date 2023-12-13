package com.example.basicauthentication.controller;

import com.example.basicauthentication.entity.Project;
import com.example.basicauthentication.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api")


public class APIController {
    @Autowired
    ProjectRepository projectRepository;

    @Autowired



    @GetMapping(value = "/v1/projects",produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Project> projectList(){
            List<Project> projects = projectRepository.findAll();
            return projects;
    }


}
