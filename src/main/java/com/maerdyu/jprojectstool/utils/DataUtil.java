package com.maerdyu.jprojectstool.utils;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSON;
import com.maerdyu.jprojectstool.constants.FilesEnum;
import com.maerdyu.jprojectstool.constants.ProjectStatus;
import com.maerdyu.jprojectstool.dto.Project;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author jinchun
 * @date 2021/03/01 15:50
 **/
public class DataUtil {

    private DataUtil() {
    }

    public static void writeFile(String conf, String filePath) {
        FileUtil.writeString(conf, filePath, "UTF-8");
    }

    public static List<Project> scanFile(String path, Boolean loadConf) {
        if (path == null) {
            throw new IllegalArgumentException("文件路径不能为空");
        }
        File file = new File(path);
        File[] files = file.listFiles();
        assert files != null;
        List<Project> projects = Stream.of(files).filter(File::isDirectory).filter(GitInfoUtil::isGitRepo).map(DataUtil::buildProjectByFile).collect(Collectors.toList());
        if(loadConf == null || loadConf){
            DataUtil.writeFile(JSON.toJSONString(projects), FilesEnum.PROJECTS.getName());
        }
        return projects;
    }

    private static Project buildProjectByFile(File file) {
        Project.ProjectBuilder builder = Project.builder();
        builder.path(file.getPath()).name(file.getName()).status(ProjectStatus.INIT.name());
        String remoteUrl = GitInfoUtil.getRemoteUrl(file);
        builder.url(remoteUrl).isPrivate(true);
        if(remoteUrl != null && remoteUrl.contains("http")){
            builder.isPrivate(false);
        }
        builder.branches(GitInfoUtil.getBranchs(file));
        return builder.build();
    }

    public static String getProperties(String key) {
        Properties properties = new Properties();
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader("/jproject_conf/jprojects.properties"))) {
            properties.load(bufferedReader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties.getProperty(key);
    }

    public static Map<String, String> getProperties(List<String> keys){
        Map<String, String> pMap = new HashMap<>();
        keys.forEach(k->{
            String properties = getProperties(k);
            pMap.put(k, properties);
        });
        return pMap;
    }
}