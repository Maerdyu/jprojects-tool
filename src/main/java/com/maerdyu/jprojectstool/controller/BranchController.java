package com.maerdyu.jprojectstool.controller;

import com.maerdyu.jprojectstool.dto.branch.CheckoutBranchDTO;
import com.maerdyu.jprojectstool.dto.branch.PullBranchListDTO;
import com.maerdyu.jprojectstool.dto.branch.PushBranchDTO;
import com.maerdyu.jprojectstool.service.BranchService;
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
    public void checkoutBranch(@PathVariable String projectName, @RequestBody CheckoutBranchDTO checkoutBranchDTO) {
        branchService.checkoutBranch(projectName, checkoutBranchDTO);
    }

    @PostMapping
    public void checkBranchList(@RequestBody CheckoutBranchDTO checkoutBranchDTO) {
        branchService.checkoutBranchList(checkoutBranchDTO);
    }

    @DeleteMapping("/{projectName}/{branchName}")
    public void deleteBranch(@PathVariable String projectName, @PathVariable String branchName) {
        branchService.deleteBranch(projectName, branchName);
    }

    @PostMapping("batch/pull")
    public void pullBranchList(@RequestBody PullBranchListDTO pullBranchListDTO) {
        branchService.pullBranchList(pullBranchListDTO);
    }

    @PostMapping("push")
    public void pushBranch(@RequestBody PushBranchDTO pushBranchDTO) {
        branchService.pushBranch(pushBranchDTO);
    }
}