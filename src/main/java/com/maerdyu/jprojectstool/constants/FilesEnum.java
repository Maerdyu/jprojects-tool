package com.maerdyu.jprojectstool.constants;

/**
 * @author jinchun
 * @date 2021/03/01 15:53
 **/
public enum FilesEnum {
    PROJECTS("/jproject_conf/projects.json"),
    ;


    private String name;

    FilesEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}