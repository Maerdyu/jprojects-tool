package com.maerdyu.jprojectstool.init;

import com.maerdyu.jprojectstool.dto.Project;
import com.maerdyu.jprojectstool.utils.DataUtil;
import com.maerdyu.jprojectstool.utils.GitUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author jinchun
 * @date 2021/03/01 15:22
 **/
@Component
public class ScanFileRunner implements ApplicationRunner {

    @Value("${filepath}")
    private String filePath;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<Project> projects = DataUtil.scanFile(filePath, true);
//        projects.forEach(GitUtil::pull);
        projects.forEach(GitUtil::printRemote);
    }

}