package com.example.basicauthentication.controller;

import com.example.basicauthentication.dto.ProjectDto;
import com.example.basicauthentication.dto.SearchDto;
import com.example.basicauthentication.entity.Project;
import com.example.basicauthentication.entity.User;
import com.example.basicauthentication.repository.ProjectRepository;
import com.example.basicauthentication.repository.UserRepository;
import com.example.basicauthentication.service.JRepostService;
import com.example.basicauthentication.service.ProjectServiceImpl;
import net.bytebuddy.asm.Advice;
import net.sf.jasperreports.engine.JRException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

@Controller
@RequestMapping("/projects")
public class ProjectController {
    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    ProjectServiceImpl projectServiceImpl;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private JRepostService service;

    @GetMapping("/newproject")
    public String createNewPage(Model model){
        List<User> usersList=userRepository.findAll();
        List<User> updatedList=new ArrayList<User>();
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        String username = authentication.getName();
        User loggedInUser=userRepository.findByEmail(username);
        for(User user:usersList){
            if(user.getId()!=loggedInUser.getId()){
                updatedList.add(user);
            }
        }
        System.out.println(usersList);
        Project project=new Project();
        model.addAttribute("project",project);
        model.addAttribute("allUserList",updatedList);
        return "view/addProject";
    }

    @PostMapping(value="/createNewProject",consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String createNewProject(ProjectDto project, BindingResult bindingResult){
      /*  if(bindingResult.hasErrors()){
            return "view/addProject";
        }*/

        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        String username = authentication.getName();
        User loggedInUser=userRepository.findByEmail(username);
        System.out.println(loggedInUser.toString());
        project.setOwner(loggedInUser.getId());
        projectServiceImpl.saveProject(project);
//        System.out.println(project.toString());
        return "redirect:/projects/projectList";
//        return "view/projectList";
    }

    @GetMapping("/projectList")
    public String projectList(Model model){
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        String username = authentication.getName();
        User loggedInUser=userRepository.findByEmail(username);
//        System.out.println(loggedInUser);
        List<Project> list=projectServiceImpl.getAllProjects();
//        list.forEach(l-> System.out.println(l.getStartDate().getClass().getName()));

        model.addAttribute("projects",projectServiceImpl.getAllProjects());
        model.addAttribute("currentUser",loggedInUser);
        return "view/projectList";
    }

    @GetMapping("/showFormForUpdateProject/{id}")
    public String updateProject(@PathVariable("id") Long id,Model model){



        List<User> usersList=userRepository.findAll();
        List<User> updatedList=new ArrayList<User>();
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        String username = authentication.getName();
        User loggedInUser=userRepository.findByEmail(username);
        for(User user:usersList){
            if(user.getId()!=loggedInUser.getId()){
                updatedList.add(user);
            }
        }
//        Optional<Project> project=projectServiceImpl.getById(id)
//        model.addAttribute("project",project);
//        return "view/updateProject";

        model.addAttribute("allUserList",updatedList);

        Project np=projectServiceImpl.getById(id).orElse(null);
        List <Long> ids=np.getMembersIdList();
        List <User> names=new ArrayList<>();
        for( Long uid:ids){
            names.add(userRepository.findById(uid).orElse(null));
        }




        projectServiceImpl.getById(id).ifPresent(project -> model.addAttribute("project", project));
        model.addAttribute("existingMembers",names);
        return "view/updateProject";

    }



    @PostMapping(value = "/updateProject/{id}",consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String confirmUpdate(@PathVariable("id") Long id, ProjectDto project, Model model){

//        Project np=projectServiceImpl.getById(id).orElse(null);
//        if(np.getStartDate().isAfter(np.getEndDate())){
//            model.addAttribute("dateError","Invalid start or enddate.");
//            return "view/updateProject";
//        }

        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        String username = authentication.getName();
        User loggedInUser=userRepository.findByEmail(username);

        Project pe=projectServiceImpl.updateProject(project,id);

        return "redirect:/projects/projectList";

    }

    @GetMapping("/deleteProject/{id}")
    public String deleteProject(@PathVariable("id") Long id){
        Optional<Project> project=projectServiceImpl.getById(id);
        projectServiceImpl.deleteProject(project);
        return "redirect:/projects/projectList";
    }


    @GetMapping("/getProject")
    public List<Project> getProject(){
        List<Project> projectList=(List<Project>) projectRepository.findAll();
        return projectList;
    }
    @GetMapping("/jasperpdf/export/{startDate}/{endDate}")
    public void createPDF(HttpServletResponse response, @PathVariable("startDate")@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate, @PathVariable("endDate")@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) throws IOException, JRException {

        List<Project> reportList=projectServiceImpl.findProjectByStartDateAndEndDate(startDate,endDate);
        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=pdf_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);

        service.exportJasperReport(response,reportList);
    }

    @PostMapping(value="/search",consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String filteredList(SearchDto searchDto, Model model){
        System.out.println(searchDto.getSrStartDate());
        System.out.println(searchDto.getSrEndDate());

        System.out.println(projectServiceImpl.findProjectByStartDateAndEndDate(searchDto.getSrStartDate(),searchDto.getSrEndDate()).toString());
        model.addAttribute("projects",projectServiceImpl.findProjectByStartDateAndEndDate(searchDto.getSrStartDate(),searchDto.getSrEndDate()));
        model.addAttribute("srStartDate",searchDto.getSrStartDate());
        model.addAttribute("srEndDate",searchDto.getSrEndDate());
        return "view/generationPage";
    }

    @GetMapping("/profile")
    public String profile(Model model){
        /*SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        String username = authentication.getName();
        User loggedInUser=userRepository.findByEmail(username);
        model.addAttribute("currentUser",loggedInUser);
        model.addAttribute("projects",projectServiceImpl.findProjectByOwnerId(loggedInUser.getId()));
        return "view/profile";*/


        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        String username = authentication.getName();
        User loggedInUser = userRepository.findByEmail(username);

        if(loggedInUser.getProfilePicture()!=null) {
            byte[] profilePicture = loggedInUser.getProfilePicture();
            String encodedImage = Base64.getEncoder().encodeToString(profilePicture);
            encodedImage = "data:image/png;base64,"+encodedImage;
            model.addAttribute("encodedImage", encodedImage);
        }

//        String profilePictureBase64 = java.util.Base64.getEncoder().encodeToString(profilePicture);
        // Add the profile picture to the model
        model.addAttribute("currentUser", loggedInUser);



        model.addAttribute("projects", projectServiceImpl.findProjectByOwnerId(loggedInUser.getId()));
        return "view/profile";
    }

}
