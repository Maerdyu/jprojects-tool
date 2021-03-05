package com.maerdyu.jprojectstool.service;

import com.maerdyu.jprojectstool.dto.JprojectsConf;
import com.maerdyu.jprojectstool.dto.Project;
import com.maerdyu.jprojectstool.utils.DataUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author jinchun
 * @date 2021/03/01 16:38
 **/
@Service
public class FileScanService {

    @Resource
    private JprojectsConf jprojectsConf;

    public void loadFile(String path) {
        List<Project> projects = DataUtil.scanFile(path, true);
        jprojectsConf.setProjects(projects);
    }
}