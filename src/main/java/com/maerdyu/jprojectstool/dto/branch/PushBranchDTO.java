package com.maerdyu.jprojectstool.dto.branch;

import lombok.Data;

/**
 * @author jinchun
 * @date 2021/03/06 10:09
 **/
@Data
public class PushBranchDTO {
    private String projectName;
    private String remoteBranch;
    private String branchName;
}