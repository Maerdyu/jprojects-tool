package com.maerdyu.jprojectstool.constants;

/**
 * @author jinchun
 * @date 2021/03/01 15:53
 **/
public enum FilesEnum {
    /**
     * 项目配置写入文件
     */
    PROJECTS("/jproject_conf/projects.json"),
    PROJECTS_PORPERTIES("/jproject_conf/jprojects.properties"),
    ;


    private final String name;

    FilesEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}