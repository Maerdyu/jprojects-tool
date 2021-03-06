package com.maerdyu.jprojectstool.controller;

import com.maerdyu.jprojectstool.dto.JprojectsConf;
import com.maerdyu.jprojectstool.dto.Project;
import com.maerdyu.jprojectstool.service.FileScanService;
import com.maerdyu.jprojectstool.service.ProjectService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author jinchun
 * @date 2021/03/05 10:48
 **/
@RestController
@RequestMapping("/projects")
public class ProjectController {

    @Resource
    private JprojectsConf jprojectsConf;
    @Resource
    private ProjectService projectService;
    @Resource
    private FileScanService fileScanService;

    @GetMapping
    public List<Project> listProjects() {
        return jprojectsConf.getProjects();
    }

    @PostMapping("/reload/file")
    public void reloadFile(@RequestParam String path) {
        fileScanService.loadFile(path);
    }

    @PostMapping("/reload/project/{projectName}")
    public void reloadProject(@PathVariable String projectName){
        projectService.reloadProject(projectName);
    }
}