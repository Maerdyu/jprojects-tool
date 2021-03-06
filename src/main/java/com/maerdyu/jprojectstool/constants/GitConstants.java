package com.maerdyu.jprojectstool.constants;

/**
 * @author jinchun
 * @date 2021/03/03 13:49
 **/
public class GitConstants {
    private GitConstants() {
    }

    public static final String REMOTE_GIT_PREFIX = "refs/remotes/";
    public static final String LOCAL_GIT_PREFIX = "refs/heads/";
    public static final String REMOTE_BRANCH_PREFIX = "origin/";
    public static final String ORIGIN = "origin";
    public static final String PUBLIC_REPO_PREFIX = "http";
    public static final String GIT_REPO_PROPERTIES_NAME = "filepath";
    public static final String GIT_KEY_PROPERTIES_NAME = "privateKeyPath";
}