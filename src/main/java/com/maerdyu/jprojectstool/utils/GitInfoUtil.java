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
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author jinchun
 * @date 2021/03/02 15:18
 **/
public class GitInfoUtil {

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
            return call.stream().map(f -> Branch.builder().name(f.getName()).build()).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Repository genRepoByPath(Project project) throws IOException {
        File file = new File(project.getPath());
        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        return builder.readEnvironment().findGitDir(file).build();
    }
}