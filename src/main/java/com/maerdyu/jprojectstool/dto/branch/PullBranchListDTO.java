package com.maerdyu.jprojectstool.dto.branch;

import lombok.Data;

import java.util.List;

/**
 * @author jinchun
 * @date 2021/03/06 09:03
 **/
@Data
public class PullBranchListDTO {
    private List<String> projectNames;
    private Boolean diff;
}