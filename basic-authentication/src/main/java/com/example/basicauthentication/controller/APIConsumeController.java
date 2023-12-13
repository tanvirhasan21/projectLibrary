package com.example.basicauthentication.controller;

import com.example.basicauthentication.dto.APIProjectDto;
import com.example.basicauthentication.service.APIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

@Controller
public class APIConsumeController {

    @Autowired
    APIService apiService;
    @GetMapping(value = "/v1/projects/consume")
    public String showProjects(Model model)
    {
        RestTemplate restTemplate=new RestTemplate();
        String url="http://localhost:8099/api/v1/projects";
        String response = restTemplate.getForObject(url, String.class);
        System.out.println(response);
        model.addAttribute("projects",apiService.getAllProjects());
        return "view/APIlist";
    }
}
