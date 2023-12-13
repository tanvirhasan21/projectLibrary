package com.example.basicauthentication.service;

import com.example.basicauthentication.dto.ProjectDto;
import com.example.basicauthentication.entity.Project;
import com.example.basicauthentication.entity.User;
import com.example.basicauthentication.repository.ProjectRepository;
import com.example.basicauthentication.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    UserRepository userRepository;
    DateTimeFormatter formatter=DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public void saveProject(ProjectDto project) {

        Project project1 = new Project();
        List<User> members= new ArrayList<>();
        BeanUtils.copyProperties(project,project1);

        List<Long> idList=project.getMembersList();
        for (int i = 0; i < idList.size(); i++) {
            User user=userRepository.findById(idList.get(i)).orElse(null);
            members.add(user);
        }


        project1.setMembers(members);


        project1.setOwnerId(userRepository.findById(project.getOwner()).get().getId());
        String name=userRepository.findById(project.getOwner()).get().getName();
        project1.setOwner(name);

        project1.setStartDate(project.getStartDate());
        project1.setEndDate(project.getEndDate());
        project1.setStatus(project.getStatus());
        Project pe=this.projectRepository.save(project1);


    }




    public Project updateProject(ProjectDto project,Long id) {
        Project newProject = projectRepository.findById(id).get();
        newProject.setName(project.getName());
        newProject.setIntro(project.getIntro());

//        newProject.setOwnerId(userRepository.findById(project.getOwner()).get().getId());
//        newProject.setOwner(project.getOwner());
        newProject.setStatus(project.getStatus());
        if(project.getStartDate()!=null){
            newProject.setStartDate(project.getStartDate());
        }
        if(project.getEndDate()!=null){
            newProject.setEndDate(project.getEndDate());
        }


        List<User> members= new ArrayList<>();

        List<Long> idList=project.getMembersList();
//        System.out.println(idList.toString());
        for (int i = 0; i < idList.size(); i++) {
//            System.out.println(idList.get(i));
            User user=userRepository.findById(idList.get(i)).orElse(null);
//            System.out.println(user.toString());
            members.add(user);
//            System.out.println(members.toString());
        }

        if(members.size()>0){
            newProject.setMembers(members);
        }




//        System.out.println(project.getMembers());
//        newProject.setMembers(project.getMembers());
//        newProject.getStartDate(project.getStartDate());


        Project updatedProject = projectRepository.save(newProject);
        return updatedProject;
    }

    @Override
    public void deleteProject(Optional<Project> project) {

        projectRepository.deleteById(project.get().getId());
    }


    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Optional<Project> getById(Long id) {
        if(projectRepository.existsById(id)){
            Optional<Project> pe=projectRepository.findById(id);
            return pe;
        }

        return null;
    }


    @Override
    public List<Project> findProjectByStartDateAndEndDate(LocalDate startDate, LocalDate endDate) {
        List<Project> projectsByDate = new ArrayList<>();
        List<Project> projects = projectRepository.findAll();
        for(Project project: projects)
        {
            if(project.getStartDate()!=null && project.getEndDate()!=null){
                if((project.getStartDate().isAfter(startDate) || project.getStartDate().isEqual(startDate) ) && (project.getEndDate().isBefore(endDate) || project.getStartDate().isEqual(endDate)))
                    projectsByDate.add(project);
            }

        }

        return projectsByDate;
    }

    @Override
    public List<Project> findProjectByOwnerId(Long id) {
        List<Project> profileProject=new ArrayList<>();
        List<Project> projects=projectRepository.findAll();
        for (Project project:projects){
            if(project.getOwnerId()==id){
                profileProject.add(project);
            }
        }
        return profileProject;
    }


}
