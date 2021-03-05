package com.maerdyu.jprojectstool.service;

import com.maerdyu.jprojectstool.dto.JprojectsConf;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author jinchun
 * @date 2021/03/01 15:22
 **/
@Component
public class BranchService {

    @Resource
    private JprojectsConf jprojectsConf;

}