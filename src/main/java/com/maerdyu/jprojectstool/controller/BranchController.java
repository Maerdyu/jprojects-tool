package com.maerdyu.jprojectstool.controller;

import com.maerdyu.jprojectstool.dto.Project;
import com.maerdyu.jprojectstool.dto.branch.BranchCheckoutDTO;
import com.maerdyu.jprojectstool.service.BranchService;
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
    private BranchService branchService;

    @PostMapping("/{projectName}/checkout")
    public void checkoutBranch(@PathVariable String projectName, @RequestBody BranchCheckoutDTO branchCheckoutDTO) {
        branchService.checkoutBranch(projectName, branchCheckoutDTO);
    }

    @DeleteMapping("/{projectName}/{branchName}")
    public void deleteBranch(@PathVariable String projectName, @PathVariable String branchName) {
        branchService.deleteBranch(projectName, branchName);
    }

    @PostMapping("batch/checkout")
    public void checkoutBranchList(@RequestBody BranchCheckoutDTO branchCheckoutDTO){

    }
}