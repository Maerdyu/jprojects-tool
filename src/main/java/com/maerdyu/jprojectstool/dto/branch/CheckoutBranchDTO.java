package com.maerdyu.jprojectstool.dto.branch;

import lombok.Data;

import java.util.List;

/**
 * @author jinchun
 * @date 2021/03/05 14:41
 **/
@Data
public class CheckoutBranchDTO {
    private String branchName;
    private String newBranchName;
    private List<String> projectNames;
}