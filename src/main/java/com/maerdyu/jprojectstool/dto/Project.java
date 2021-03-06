package com.maerdyu.jprojectstool.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author jinchun
 * @date 2021/03/01 15:40
 **/
@Data
@Builder
public class Project {
    private String path;
    private String name;
    private Boolean isPrivate;
    private List<Branch> branches;
    private String url;
    private String status;
    private String errorMsg;
}