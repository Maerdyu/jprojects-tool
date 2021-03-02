package com.maerdyu.jprojectstool.dto;

import lombok.Builder;
import lombok.Data;

/**
 * @author jinchun
 * @date 2021/03/01 16:09
 **/
@Data
@Builder
public class Branch {
    private String name;
    private Boolean isCurrent;
    private Boolean isRemote;
}