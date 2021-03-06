package com.maerdyu.jprojectstool.service;

import com.maerdyu.jprojectstool.constants.GitConstants;
import com.maerdyu.jprojectstool.constants.ProjectStatus;
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
import java.util.function.Function;
import java.util.stream.Stream;

import static com.maerdyu.jprojectstool.constants.GitConstants.*;

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

    public void pull(Project project) {
        project.setStatus(ProjectStatus.PULLING.name());
        gitOpRetry(git -> {
            PullCommand command = git.pull();
            Boolean isPrivate = project.getIsPrivate();
            if (isPrivate != null && isPrivate) {
                command.setTransportConfigCallback(transport -> {
                    SshTransport sshTransport = (SshTransport) transport;
                    sshTransport.setSshSessionFactory(jschConfigSessionFactory);
                });
            }
            try {
                PullResult call = command.call();
                if (call.isSuccessful()) {
                    projectService.reloadProject(project);
                    project.setStatus(ProjectStatus.PULLED.name());
                } else {
                    project.setStatus(ProjectStatus.PULL_FAILED.name());
                }
                return call.isSuccessful();
            } catch (GitAPIException e) {
                project.setErrorMsg(e.getMessage());
                project.setStatus(ProjectStatus.PULL_FAILED.name());
                return false;
            }

        }, project);
    }

    @SuppressWarnings("unused")
    public void checkOutSame(Project project, String branchName) {
        project.setStatus(ProjectStatus.CHECKING.name());
        gitOpRetry(git -> {
            try {
                List<Branch> branches = project.getBranches();
                for (Branch branch : branches) {
                    String name = branch.getName();
                    Boolean isRemote = branch.getIsRemote();
                    if (Boolean.FALSE.equals(isRemote) && branchName.equals(name)) {
                        git.checkout().setCreateBranch(false).setName(branchName).call();
                    } else if (Boolean.TRUE.equals(isRemote) && branchName.equals(name.replace("origin/", ""))) {
                        git.checkout().setCreateBranch(true).setName(branchName).setStartPoint(name).call();
                    }
                }
                addConfBranch(project, branchName, branchName, false);
                project.setStatus(ProjectStatus.CHECKED.name());
                return true;
            } catch (GitAPIException e) {
                project.setErrorMsg(e.getMessage());
                project.setStatus(ProjectStatus.CHECK_FAILED.name());
                return false;
            }
        }, project);
    }

    public void checkOutReomte(Project project, String branchName, String remoteBranchName) {
        project.setStatus(ProjectStatus.CHECKING.name());
        gitOpRetry(git -> {
            try {
                List<Branch> branches = project.getBranches();
                Optional<Branch> any = branches.stream().filter(branch -> branch.getIsRemote() && branch.getName().equals(remoteBranchName)).findAny();
                if (any.isPresent()) {
                    git.checkout().setCreateBranch(true).setName(branchName).setStartPoint(remoteBranchName).call();
                }
                addConfBranch(project, branchName, branchName, false);
                project.setStatus(ProjectStatus.CHECKED.name());
                return true;
            } catch (GitAPIException e) {
                project.setErrorMsg(e.getMessage());
                project.setStatus(ProjectStatus.CHECK_FAILED.name());
                return false;
            }
        }, project);

    }

    public void deleteLocalBranch(Project project, String... branchNames) {
        project.setStatus(ProjectStatus.LOCAL_DELETING.name());
        gitOpRetry(git -> {
            try {
                git.branchDelete().setBranchNames(branchNames).call();
                deleteConfBranch(project, branchNames);
                project.setStatus(ProjectStatus.LOCAL_DELETED.name());
                return true;
            } catch (GitAPIException e) {
                project.setErrorMsg(e.getMessage());
                project.setStatus(ProjectStatus.LOCAL_DELETE_FAILED.name());
                return false;
            }
        }, project);
    }

    public void deleteRemoteBranch(Project project, String... branchNames) {
        project.setStatus(ProjectStatus.REMOTE_DELETING.name());
        gitOpRetry(git -> {
            try {
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
                project.setStatus(ProjectStatus.REMOTE_DELETED.name());
                return true;
            } catch (GitAPIException e) {
                project.setErrorMsg(e.getMessage());
                project.setStatus(ProjectStatus.REMOTE_DELETE_FAILED.name());
                return true;
            }
        }, project);
    }

    public void pushRemoteBranch(Project project, String branchName) {
        project.setStatus(ProjectStatus.REMOTE_DELETING.name());
        gitOpRetry(git -> {
            try {
                git.push().setRemote(ORIGIN).setRefSpecs(new RefSpec(branchName + ":" + branchName)).setTransportConfigCallback(transport -> {
                    SshTransport sshTransport = (SshTransport) transport;
                    sshTransport.setSshSessionFactory(jschConfigSessionFactory);
                }).call();
                addConfBranch(project, REMOTE_BRANCH_PREFIX + branchName, branchName, true);
                project.setStatus(ProjectStatus.PUSHED.name());
                return true;
            } catch (GitAPIException e) {
                project.setErrorMsg(e.getMessage());
                project.setStatus(ProjectStatus.PUSH_FAILED.name());
                return false;
            }
        }, project);
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

    private void gitOpRetry(Function<Git, Boolean> op, Project project) {
        int retry = GIT_RETRY_TIMES;
        while (retry > 0) {
            try (Repository repo = GitInfoUtil.genRepoByPath(project);
                 Git git = new Git(repo)) {
                Boolean apply = op.apply(git);
                if (Boolean.TRUE.equals(apply)) {
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            retry--;
        }
    }
}