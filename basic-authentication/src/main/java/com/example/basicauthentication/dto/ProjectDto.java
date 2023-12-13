package com.example.basicauthentication.dto;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.util.List;
@Data
@ToString
public class ProjectDto {
//    @NotEmpty(message = "Project name cannot be empty.")
    private String name;
//    @NotEmpty(message = "Intro cannot be empty.")
    private String intro;
    private Long owner;
    private String status;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    private List<Long> membersList;
}
