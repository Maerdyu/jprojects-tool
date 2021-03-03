package com.maerdyu.jprojectstool.service;

import com.maerdyu.jprojectstool.dto.Branch;
import com.maerdyu.jprojectstool.dto.JprojectsConf;
import com.maerdyu.jprojectstool.dto.Project;
import com.maerdyu.jprojectstool.utils.GitInfoUtil;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.JschConfigSessionFactory;
import org.eclipse.jgit.transport.SshTransport;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author jinchun
 * @date 2021/03/01 15:47
 **/
@Slf4j
@Service
public class GitOperateService {

    @Resource
    private JschConfigSessionFactory jschConfigSessionFactory;
    @Resource
    private JprojectsConf jprojectsConf;

    public PullResult pull(Project project) throws Exception {
        try (Repository repo = GitInfoUtil.genRepoByPath(project);
             Git git = new Git(repo)) {
            PullCommand command = git.pull();
            Boolean isPrivate = project.getIsPrivate();
            if (isPrivate != null && isPrivate) {
                command.setTransportConfigCallback(transport -> {
                    SshTransport sshTransport = (SshTransport) transport;
                    sshTransport.setSshSessionFactory(jschConfigSessionFactory);
                });
            }
            return command.call();
        }
    }

    public Boolean checkOut(Project project, String branchName) throws Exception {
        try (Repository repo = GitInfoUtil.genRepoByPath(project);
             Git git = new Git(repo)) {
            List<Branch> branches = project.getBranches();
            for (Branch branch : branches) {
                String name = branch.getName();
                if (branchName.equals(name) && !branch.getIsRemote()) {
                    git.checkout().setCreateBranch(false).setName(branchName).call();
                    return true;
                } else if (branch.getIsRemote() && branchName.equals(name.replace("origin/", ""))) {
                    git.checkout().setCreateBranch(true).setName(branchName).setStartPoint(name).call();
                    return true;
                }
            }
        }
        return false;
    }
}