package com.maerdyu.jprojectstool.utils;

import cn.hutool.core.util.StrUtil;
import com.maerdyu.jprojectstool.dto.Branch;
import com.maerdyu.jprojectstool.dto.Project;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.lib.Config;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.maerdyu.jprojectstool.constants.GitConstants.*;

/**
 * @author jinchun
 * @date 2021/03/02 15:18
 **/
public class GitInfoUtil {

    private GitInfoUtil() {
    }

    public static Boolean isGitRepo(File f) {
        File[] files = f.listFiles();
        assert files != null;
        for (File file : files) {
            if (".git".equals(file.getName())) {
                return true;
            }
        }
        return false;
    }

    public static String getRemoteUrl(File file) {
        try {
            FileRepositoryBuilder builder = new FileRepositoryBuilder();
            Repository repo = builder.readEnvironment().findGitDir(file).build();
            Config storedConfig = repo.getConfig();
            Set<String> remotes = storedConfig.getSubsections("remote");

            for (String remoteName : remotes) {
                String url = storedConfig.getString("remote", remoteName, "url");
                if (StrUtil.isNotBlank(url)) {
                    return url;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Branch> getBranchs(File file) {
        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        try (Repository repo = builder.readEnvironment().findGitDir(file).build();
             Git git = new Git(repo)) {
            List<Ref> call = git.branchList().setListMode(ListBranchCommand.ListMode.ALL).call();
            return call.stream().map(GitInfoUtil::buildBranchByRef).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public static Repository genRepoByPath(Project project) throws IOException {
        File file = new File(project.getPath());
        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        return builder.readEnvironment().findGitDir(file).build();
    }

    private static Branch buildBranchByRef(Ref ref) {
        String name = ref.getName();
        String branchName;
        String simpleName;
        boolean isRemote = false;
        if (name.startsWith(REMOTE_GIT_PREFIX)) {
            branchName = name.replace(REMOTE_GIT_PREFIX, "");
            simpleName = name.replace(REMOTE_GIT_PREFIX + REMOTE_BRANCH_PREFIX, "");
            isRemote = true;
        } else {
            branchName = name.replace(LOCAL_GIT_PREFIX, "");
            simpleName = branchName;
        }
        return Branch.builder().name(branchName).simpleName(simpleName).isRemote(isRemote).build();
    }
}