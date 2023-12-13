package com.example.basicauthentication.service;


import com.example.basicauthentication.dto.ProjectDto;
import com.example.basicauthentication.entity.Project;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public interface ProjectService {
    List<Project>getAllProjects();
    void saveProject(ProjectDto project);
    Project updateProject(ProjectDto project,Long id);
    void deleteProject(Optional<Project> project);
    List<Project> findProjectByStartDateAndEndDate(LocalDate startDate, LocalDate endDate);
    List<Project> findProjectByOwnerId(Long id);
}
