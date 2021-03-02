package com.maerdyu.jprojectstool.config;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.maerdyu.jprojectstool.utils.DataUtil;
import org.eclipse.jgit.transport.JschConfigSessionFactory;
import org.eclipse.jgit.transport.OpenSshConfig;
import org.eclipse.jgit.transport.SshSessionFactory;
import org.eclipse.jgit.util.FS;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author jinchun
 * @date 2021/03/02 14:40
 **/
@Configuration
public class JprojectsConf {

    @Bean
    @ConditionalOnMissingBean
    public JschConfigSessionFactory jschConfigSessionFactory(){
        JschConfigSessionFactory jschConfigSessionFactory = new JschConfigSessionFactory() {
            @Override
            protected void configure(OpenSshConfig.Host host, Session session) {
                session.setConfig("StrictHostKeyChecking", "no");
            }

            @Override
            protected JSch getJSch(OpenSshConfig.Host hc, FS fs) throws JSchException {
                JSch jSch = super.getJSch(hc, fs);
                jSch.removeAllIdentity();
                // 在windows上需要用puttygen转换id_rsa文件
                jSch.addIdentity(DataUtil.getProperties("privateKeyPath"));
                return jSch;
            }
        };
        SshSessionFactory.setInstance(jschConfigSessionFactory);
        return jschConfigSessionFactory;
    }
}