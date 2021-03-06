package com.maerdyu.jprojectstool.service;

import com.maerdyu.jprojectstool.constants.GitConstants;
import com.maerdyu.jprojectstool.dto.Branch;
import com.maerdyu.jprojectstool.dto.JprojectsConf;
import com.maerdyu.jprojectstool.dto.Project;
import com.maerdyu.jprojectstool.utils.GitInfoUtil;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.JschConfigSessionFactory;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.transport.SshTransport;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static com.maerdyu.jprojectstool.constants.GitConstants.ORIGIN;
import static com.maerdyu.jprojectstool.constants.GitConstants.REMOTE_BRANCH_PREFIX;

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
    @Resource
    private ProjectService projectService;

    public PullResult pull(Project project) {
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
            PullResult call = command.call();
            if (call.isSuccessful()) {
                projectService.reloadProject(project);
            }

            return call;
        } catch (GitAPIException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Boolean checkOutSame(Project project, String branchName) {
        try (Repository repo = GitInfoUtil.genRepoByPath(project);
             Git git = new Git(repo)) {
            List<Branch> branches = project.getBranches();
            for (Branch branch : branches) {
                String name = branch.getName();
                Boolean isRemote = branch.getIsRemote();
                if (Boolean.FALSE.equals(isRemote) && branchName.equals(name)) {
                    git.checkout().setCreateBranch(false).setName(branchName).call();
                    return true;
                } else if (Boolean.TRUE.equals(isRemote) && branchName.equals(name.replace("origin/", ""))) {
                    git.checkout().setCreateBranch(true).setName(branchName).setStartPoint(name).call();
                    return true;
                }
            }
            addConfBranch(project, branchName, branchName, false);
        } catch (GitAPIException | IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Boolean checkOutReomte(Project project, String branchName, String remoteBranchName) {
        try (Repository repo = GitInfoUtil.genRepoByPath(project);
             Git git = new Git(repo)) {
            List<Branch> branches = project.getBranches();
            Optional<Branch> any = branches.stream().filter(branch -> branch.getIsRemote() && branch.getName().equals(remoteBranchName)).findAny();
            if (any.isPresent()) {
                git.checkout().setCreateBranch(true).setName(branchName).setStartPoint(remoteBranchName).call();
                return true;
            }
            addConfBranch(project, branchName, branchName, false);
        } catch (GitAPIException | IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Boolean deleteLocalBranch(Project project, String... branchNames) {
        try (Repository repo = GitInfoUtil.genRepoByPath(project);
             Git git = new Git(repo)) {
            git.branchDelete().setBranchNames(branchNames).call();

            deleteConfBranch(project, branchNames);
        } catch (GitAPIException | IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void deleteRemoteBranch(Project project, String... branchNames) {
        try (Repository repo = GitInfoUtil.genRepoByPath(project);
             Git git = new Git(repo)) {
            for (String branchName : branchNames) {
                String simpleName = branchName.replace(REMOTE_BRANCH_PREFIX, GitConstants.LOCAL_GIT_PREFIX);
                RefSpec refSpec = new RefSpec()
                        .setSource(null)
                        .setDestination(simpleName);
                git.push().setRefSpecs(refSpec).setRemote(ORIGIN).setTransportConfigCallback(transport -> {
                    SshTransport sshTransport = (SshTransport) transport;
                    sshTransport.setSshSessionFactory(jschConfigSessionFactory);
                }).call();

                deleteConfBranch(project, branchName);
            }
        } catch (GitAPIException | IOException e) {
            e.printStackTrace();
        }
    }

    public void pushRemoteBranch(Project project, String branchName) {
        try (Repository repo = GitInfoUtil.genRepoByPath(project);
             Git git = new Git(repo)) {
            git.push().setRemote(ORIGIN).setRefSpecs(new RefSpec(branchName + ":" + branchName)).setTransportConfigCallback(transport -> {
                SshTransport sshTransport = (SshTransport) transport;
                sshTransport.setSshSessionFactory(jschConfigSessionFactory);
            }).call();
            addConfBranch(project, REMOTE_BRANCH_PREFIX + branchName, branchName, true);
        } catch (GitAPIException | IOException e) {
            e.printStackTrace();
        }
    }

    public Project findProjectByName(String projectName) {
        List<Project> projects = jprojectsConf.getProjects();
        Optional<Project> any = projects.stream().filter(project -> project.getName().equals(projectName)).findFirst();
        return any.orElse(null);
    }

    private void deleteConfBranch(Project project, String... branchNames) {
        List<Branch> branches = project.getBranches();
        Iterator<Branch> iterator = branches.iterator();
        Stream.of(branchNames).forEach(b -> {
            while (iterator.hasNext()) {
                Branch next = iterator.next();
                if (b.equals(next.getName())) {
                    branches.remove(next);
                }
            }
        });
    }

    private void addConfBranch(Project project, String name, String simpleName, Boolean isRemote) {
        Branch branch = Branch.builder().name(name).simpleName(simpleName).isRemote(isRemote).build();
        List<Branch> branches = project.getBranches();
        branches.add(branch);
    }
}