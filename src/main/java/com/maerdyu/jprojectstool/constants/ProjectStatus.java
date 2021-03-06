package com.maerdyu.jprojectstool.constants;

/**
 * @author jinchun
 * @date 2021/03/03 13:41
 **/
public enum ProjectStatus {
    /**
     * 初始化
     */
    INIT,
    /**
     * 正在更新代码
     */
    PULLING,
    /**
     * 更新完成
     */
    PULLED,
    /**
     * 正在切换分支
     */
    CHECKING,
    /**
     * 切换分支完成
     */
    CHECKED,
    ;
}