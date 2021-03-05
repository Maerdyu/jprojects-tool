package com.maerdyu.jprojectstool.controller;

import com.maerdyu.jprojectstool.dto.Project;
import com.maerdyu.jprojectstool.dto.branch.BranchCheckoutDTO;
import com.maerdyu.jprojectstool.service.GitOperateService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author jinchun
 * @date 2021/03/01 15:12
 **/

@RestController
@RequestMapping("/branch")
public class BranchController {

    @Resource
    private GitOperateService gitOperateService;

    @PostMapping("/{projectName}/checkout")
    public void checkoutBranch(@PathVariable String projectName, @RequestBody BranchCheckoutDTO branchCheckoutDTO) {
        Project project = gitOperateService.findProjectByName(projectName);
        gitOperateService.checkOutReomte(project, branchCheckoutDTO.getNewBranchName(), branchCheckoutDTO.getBranchName());
    }

    @DeleteMapping("/{projectName}/{branchName}")
    public void deleteBranch(@PathVariable String projectName, @PathVariable String branchName){
        Project project = gitOperateService.findProjectByName(projectName);
        gitOperateService.deleteLocalBranch(project, branchName);
    }
}