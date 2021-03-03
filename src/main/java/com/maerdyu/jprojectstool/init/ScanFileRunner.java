package com.maerdyu.jprojectstool.init;

import com.alibaba.fastjson.JSONObject;
import com.maerdyu.jprojectstool.dto.JprojectsConf;
import com.maerdyu.jprojectstool.dto.Project;
import com.maerdyu.jprojectstool.service.GitOperateService;
import com.maerdyu.jprojectstool.utils.DataUtil;
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

    @Override
    public void run(ApplicationArguments args) {
//        String filepath = DataUtil.getProperties("filepath");
//        List<Project> projects = DataUtil.scanFile(filepath, true);
//        projects.forEach(GitInfoUtil::listRemote);
        System.out.println(JSONObject.toJSONString(jprojectsConf));
    }

}