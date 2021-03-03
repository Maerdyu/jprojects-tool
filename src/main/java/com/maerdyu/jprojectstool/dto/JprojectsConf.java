package com.maerdyu.jprojectstool.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author jinchun
 * @date 2021/03/03 11:36
 **/
@Data
@Builder
public class JprojectsConf {
    private List<Project> projects;
}