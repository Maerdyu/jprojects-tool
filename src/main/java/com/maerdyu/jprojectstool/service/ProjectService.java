package com.maerdyu.jprojectstool.service;

import com.maerdyu.jprojectstool.dto.JprojectsConf;
import com.maerdyu.jprojectstool.dto.Project;
import com.maerdyu.jprojectstool.utils.DataUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;

/**
 * @author jinchun
 * @date 2021/03/05 15:42
 **/
@Component
public class ProjectService {

    @Resource
    private JprojectsConf jprojectsConf;
    @Resource
    private GitOperateService gitOperateService;

    public void reloadProject(String projectName) {
        Project project = gitOperateService.findProjectByName(projectName);
        reloadProject(project);
    }

    public void reloadProject(Project project) {
        String path = project.getPath();
        Project newProject = DataUtil.buildProjectByFile(new File(path));
        List<Project> projects = jprojectsConf.getProjects();
        projects.stream().filter(p -> p.getName().equals(newProject.getName())).forEach(p -> p = newProject);
    }
}