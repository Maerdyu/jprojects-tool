package com.maerdyu.jprojectstool.init;

import com.alibaba.fastjson.JSONObject;
import com.maerdyu.jprojectstool.dto.JprojectsConf;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

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
        System.out.println(JSONObject.toJSONString(jprojectsConf));
    }

}