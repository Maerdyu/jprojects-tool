package com.maerdyu.jprojectstool.service;

import cn.hutool.core.util.StrUtil;
import com.maerdyu.jprojectstool.dto.Project;
import com.maerdyu.jprojectstool.utils.GitInfoUtil;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.lib.Config;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.JschConfigSessionFactory;
import org.eclipse.jgit.transport.SshTransport;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.Set;

/**
 * @author jinchun
 * @date 2021/03/01 15:47
 **/
@Slf4j
@Service
public class GitOperateService {

    @Resource
    private JschConfigSessionFactory jschConfigSessionFactory;

    public PullResult pull(Project project) {
        try (Repository repo = GitInfoUtil.genRepoByPath(project);
             Git git = new Git(repo))
        {
            PullCommand command = git.pull();
            Boolean isPrivate = project.getIsPrivate();
            if (isPrivate != null && isPrivate) {
                command.setTransportConfigCallback(transport -> {
                    SshTransport sshTransport = (SshTransport) transport;
                    sshTransport.setSshSessionFactory(jschConfigSessionFactory);
                });
            }
            PullResult call = command.call();
            System.out.printf("project %s pull end%n", project.getPath());
            return call;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}