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
     * 更新失败
     */
    PULL_FAILED,
    /**
     * 正在切换分支
     */
    CHECKING,
    /**
     * 切换分支完成
     */
    CHECKED,
    /**
     * 切换分支失败
     */
    CHECK_FAILED,
    /**
     * 正在删除本地分支
     */
    LOCAL_DELETING,
    /**
     * 删除本地分支完成
     */
    LOCAL_DELETED,
    /**
     * 删除本地分支失败
     */
    LOCAL_DELETE_FAILED,
    /**
     * 正在删除远程分支
     */
    REMOTE_DELETING,
    /**
     * 删除远程分支完成
     */
    REMOTE_DELETED,
    /**
     * 删除远程分支失败
     */
    REMOTE_DELETE_FAILED,
    /**
     * 正在创建远程分支
     */
    PUSHING,
    /**
     * 创建远程分支完成
     */
    PUSHED,
    /**
     * 创建远程分支失败
     */
    PUSH_FAILED,
    ;
}