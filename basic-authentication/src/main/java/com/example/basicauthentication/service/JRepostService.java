package com.example.basicauthentication.service;

import com.example.basicauthentication.entity.Project;
import com.example.basicauthentication.repository.ProjectRepository;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JRepostService {

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    ProjectServiceImpl projectService;

    public void exportJasperReport(HttpServletResponse response, List<Project> list) throws JRException, IOException {
        List<Project> projectList = list;
        //Get file and compile it
        File file = ResourceUtils.getFile("classpath:projectreport.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(projectList);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy", "tanvir");
        //Fill Jasper report
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        //Export report
        JasperExportManager.exportReportToPdfStream(jasperPrint,response.getOutputStream());
    }
}
