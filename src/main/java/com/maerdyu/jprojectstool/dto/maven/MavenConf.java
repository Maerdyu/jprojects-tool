package com.maerdyu.jprojectstool.dto.maven;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author jinchun
 * @date 2021/03/26 15:19
 **/

@Data
public class MavenConf {

    private String groupId;
    private String artifactId;
    private String version;
    private Map<String, String> properties;
    private List<MavenConf> dependencies;
}