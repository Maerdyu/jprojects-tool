package com.maerdyu.jprojectstool.config;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.maerdyu.jprojectstool.constants.ThreadPoolConstants;
import com.maerdyu.jprojectstool.dto.JprojectsConf;
import com.maerdyu.jprojectstool.dto.Project;
import com.maerdyu.jprojectstool.utils.DataUtil;
import org.eclipse.jgit.transport.JschConfigSessionFactory;
import org.eclipse.jgit.transport.OpenSshConfig;
import org.eclipse.jgit.transport.SshSessionFactory;
import org.eclipse.jgit.util.FS;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.maerdyu.jprojectstool.constants.GitConstants.GIT_KEY_PROPERTIES_NAME;
import static com.maerdyu.jprojectstool.constants.GitConstants.GIT_REPO_PROPERTIES_NAME;

/**
 * @author jinchun
 * @date 2021/03/02 14:40
 **/
@Configuration
public class JprojectsConfig {

    @Bean
    @ConditionalOnMissingBean
    public JschConfigSessionFactory jschConfigSessionFactory() {
        JschConfigSessionFactory jschConfigSessionFactory = new JschConfigSessionFactory() {
            @Override
            protected void configure(OpenSshConfig.Host host, Session session) {
                session.setConfig("StrictHostKeyChecking", "no");
            }

            @Override
            protected JSch getJSch(OpenSshConfig.Host hc, FS fs) throws JSchException {
                JSch jSch = super.getJSch(hc, fs);
                jSch.removeAllIdentity();
                // 在windows上需要用puTTYgen转换id_rsa文件
                jSch.addIdentity(DataUtil.getProperties(GIT_KEY_PROPERTIES_NAME));
                return jSch;
            }
        };
        SshSessionFactory.setInstance(jschConfigSessionFactory);
        return jschConfigSessionFactory;
    }

    @Bean
    @Primary
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        List<String> props = new ArrayList<>();
        props.add(ThreadPoolConstants.CORE_SIZE);
        props.add(ThreadPoolConstants.MAX_SIZE);
        props.add(ThreadPoolConstants.QUEUE_SIZE);
        props.add(ThreadPoolConstants.KEEP_ALIVE);
        Map<String, String> pMap = DataUtil.getProperties(props);
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(Integer.parseInt(pMap.get(ThreadPoolConstants.CORE_SIZE)));
        threadPoolTaskExecutor.setMaxPoolSize(Integer.parseInt(pMap.get(ThreadPoolConstants.MAX_SIZE)));
        threadPoolTaskExecutor.setQueueCapacity(Integer.parseInt(pMap.get(ThreadPoolConstants.QUEUE_SIZE)));
        threadPoolTaskExecutor.setKeepAliveSeconds(Integer.parseInt(pMap.get(ThreadPoolConstants.KEEP_ALIVE)));
        return threadPoolTaskExecutor;
    }

    @Bean
    public JprojectsConf jprojectsConf() {
        String filepath = DataUtil.getProperties(GIT_REPO_PROPERTIES_NAME);
        List<Project> projects = DataUtil.scanFile(filepath, true);
        return JprojectsConf.builder().projects(projects).build();
    }

}