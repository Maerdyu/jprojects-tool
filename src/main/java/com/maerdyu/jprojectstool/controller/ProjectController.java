package com.maerdyu.jprojectstool.controller;

import com.maerdyu.jprojectstool.dto.JprojectsConf;
import com.maerdyu.jprojectstool.dto.Project;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping
    public List<Project> listProjects(){
        return jprojectsConf.getProjects();
    }
}