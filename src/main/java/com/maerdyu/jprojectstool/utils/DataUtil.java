package com.maerdyu.jprojectstool.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.maerdyu.jprojectstool.constants.FilesEnum;
import com.maerdyu.jprojectstool.dto.Project;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author jinchun
 * @date 2021/03/01 15:50
 **/
public class DataUtil {

    public static void writeFile(String conf, String filePath) {
        FileUtil.writeString(conf, filePath, "UTF-8");
    }

    public static List<Project> scanFile(String path, Boolean loadConf) {
        assert path != null;
        File file = new File(path);
        File[] files = file.listFiles();
        assert files != null;
        List<Project> projects = Stream.of(files).filter(File::isDirectory).filter(GitUtil::isGitRepo).map(f -> Project.builder().path(f.getPath()).build()).collect(Collectors.toList());
        DataUtil.writeFile(JSONObject.toJSONString(projects), FilesEnum.PROJECTS.getName());
        return projects;
    }
}