package com.maerdyu.jprojectstool.service;

import com.maerdyu.jprojectstool.dto.Project;
import com.maerdyu.jprojectstool.dto.branch.CheckoutBranchDTO;
import com.maerdyu.jprojectstool.dto.branch.PullBranchListDTO;
import com.maerdyu.jprojectstool.dto.branch.PushBranchDTO;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

import static com.maerdyu.jprojectstool.constants.GitConstants.REMOTE_BRANCH_PREFIX;

/**
 * @author jinchun
 * @date 2021/03/01 15:22
 **/
@Component
public class BranchService {

    @Resource
    private GitOperateService gitOperateService;
    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    public void checkoutBranch(String projectName, CheckoutBranchDTO checkoutBranchDTO) {
        Project project = gitOperateService.findProjectByName(projectName);
        gitOperateService.checkOutReomte(project, checkoutBranchDTO.getNewBranchName(), checkoutBranchDTO.getBranchName());
    }

    private void checkoutBranch(Project project, String newBranchName, String remoteBranch) {
        gitOperateService.checkOutReomte(project, newBranchName, remoteBranch);
    }

    public void deleteBranch(String projectName, String branchName) {
        Project project = gitOperateService.findProjectByName(projectName);
        if (branchName.startsWith(REMOTE_BRANCH_PREFIX)) {
            gitOperateService.deleteRemoteBranch(project, branchName);
        } else {
            gitOperateService.deleteLocalBranch(project, branchName);
        }

    }

    public void checkoutBranchList(CheckoutBranchDTO checkoutBranchDTO) {
        List<String> projectNames = checkoutBranchDTO.getProjectNames();
        projectNames.forEach(projectName -> threadPoolTaskExecutor.execute(() -> checkoutBranch(projectName, checkoutBranchDTO)));
    }

    public void pullBranchList(PullBranchListDTO pullBranchListDTO) {
        List<String> projectNames = pullBranchListDTO.getProjectNames();

        projectNames.forEach(projectName -> threadPoolTaskExecutor.execute(() -> this.pullBranch(projectName)));
    }

    private void pullBranch(String projectName) {
        Project project = gitOperateService.findProjectByName(projectName);
        gitOperateService.pull(project);
    }

    public void pushBranch(PushBranchDTO pushBranchDTO) {
        String projectName = pushBranchDTO.getProjectName();
        Project project = gitOperateService.findProjectByName(projectName);
        String branchName = pushBranchDTO.getBranchName();
        checkoutBranch(project, branchName, pushBranchDTO.getRemoteBranch());
        gitOperateService.pushRemoteBranch(project, branchName);
    }
}