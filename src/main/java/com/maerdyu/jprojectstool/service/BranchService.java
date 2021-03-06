package com.maerdyu.jprojectstool.service;

import com.maerdyu.jprojectstool.dto.Project;
import com.maerdyu.jprojectstool.dto.branch.BranchCheckoutDTO;
import com.maerdyu.jprojectstool.dto.branch.PullBranchListDTO;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

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

    public void checkoutBranch(String projectName, BranchCheckoutDTO branchCheckoutDTO) {
        Project project = gitOperateService.findProjectByName(projectName);
        gitOperateService.checkOutReomte(project, branchCheckoutDTO.getNewBranchName(), branchCheckoutDTO.getBranchName());
    }

    public void deleteBranch(String projectName, String branchName) {
        Project project = gitOperateService.findProjectByName(projectName);
        gitOperateService.deleteLocalBranch(project, branchName);
    }

    public void checkoutBranchList(BranchCheckoutDTO branchCheckoutDTO) {
        List<String> projectNames = branchCheckoutDTO.getProjectNames();
        projectNames.forEach(projectName -> threadPoolTaskExecutor.execute(() -> checkoutBranch(projectName, branchCheckoutDTO)));
    }

    public void pullBranchList(PullBranchListDTO pullBranchListDTO) {
        List<String> projectNames = pullBranchListDTO.getProjectNames();

        projectNames.forEach(projectName -> threadPoolTaskExecutor.execute(() -> this.pullBranch(projectName)));
    }

    private void pullBranch(String projectName) {
        Project project = gitOperateService.findProjectByName(projectName);
        gitOperateService.pull(project);
    }
}