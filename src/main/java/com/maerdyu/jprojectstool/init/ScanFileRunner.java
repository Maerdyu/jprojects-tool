package com.maerdyu.jprojectstool.init;

import com.maerdyu.jprojectstool.dto.JprojectsConf;
import com.maerdyu.jprojectstool.dto.Project;
import com.maerdyu.jprojectstool.service.GitOperateService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author jinchun
 * @date 2021/03/01 15:22
 **/
@Component
public class ScanFileRunner implements ApplicationRunner {

    @Resource
    private JprojectsConf jprojectsConf;
    @Resource
    private GitOperateService gitOperateService;

    @Override
    public void run(ApplicationArguments args) {
        List<Project> projects = jprojectsConf.getProjects();
        projects.stream().filter(p -> "jprojects-tool".equals(p.getName())).forEach(p -> {
            try {
//                gitOperateService.pushRemoteBranch(p, "test1");
//                gitOperateService.deleteRemoteBranch(p, "origin/test1");
                System.out.println("finish");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

}