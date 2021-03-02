package com.maerdyu.jprojectstool.utils;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.maerdyu.jprojectstool.dto.Project;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.lib.Config;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.JschConfigSessionFactory;
import org.eclipse.jgit.transport.OpenSshConfig;
import org.eclipse.jgit.transport.SshSessionFactory;
import org.eclipse.jgit.transport.SshTransport;
import org.eclipse.jgit.util.FS;

import java.io.File;
import java.io.IOException;
import java.util.Set;

/**
 * @author jinchun
 * @date 2021/03/01 15:47
 **/
@Slf4j
public class GitUtil {

    private static final JschConfigSessionFactory jschConfigSessionFactory = new JschConfigSessionFactory() {
        @Override
        protected void configure(OpenSshConfig.Host host, Session session) {
            session.setConfig("StrictHostKeyChecking", "no");
        }

        @Override
        protected JSch getJSch(OpenSshConfig.Host hc, FS fs) throws JSchException {
            JSch jSch = super.getJSch(hc, fs);
            jSch.removeAllIdentity();
            // 在windows上需要用puttygen转换id_rsa文件
            jSch.addIdentity("/jproject_conf/key.ppk");
            return jSch;
        }
    };

    static {
        SshSessionFactory.setInstance(jschConfigSessionFactory);
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

    public static void pull(Project project) {
        try {
            String path = project.getPath();
            File file = new File(path);
            log.info("start pull {}", file.getName());
            FileRepositoryBuilder builder = new FileRepositoryBuilder();
            Repository repo = builder.readEnvironment().findGitDir(file).build();
            Git git = new Git(repo);
            PullResult call = git.pull().setTransportConfigCallback(transport -> {
                SshTransport sshTransport = (SshTransport) transport;
                sshTransport.setSshSessionFactory(jschConfigSessionFactory);
            }).call();
            log.info("pull {} result", call.isSuccessful());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void printRemote(Project project){
        try {
            String path = project.getPath();
            File file = new File(path);
            log.info("start pull {}", file.getName());
            FileRepositoryBuilder builder = new FileRepositoryBuilder();
            Repository repo = builder.readEnvironment().findGitDir(file).build();
            Config storedConfig = repo.getConfig();
            Set<String> remotes = storedConfig.getSubsections("remote");

            for (String remoteName : remotes) {
                String url = storedConfig.getString("remote", remoteName, "url");
                System.out.println(remoteName + " " + url);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}