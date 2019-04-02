package com.dongnaoedu.springcloud.config;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@Async
public class ScheduledService {
    private String remotePath = "http://172.17.1.247:18080/root/SpringCloudConfig.git";//远程库路径
    private String username = "root";
    private String password = "12345678";
    private UsernamePasswordCredentialsProvider usernamePasswordCredentialsProvider = new
            UsernamePasswordCredentialsProvider(username,password);
//    private String repoPath = "D:\\project\\";//windows 下载已有仓库到本地路径
//    private String tmpPath = "D:\\tmp\\";//windows 临时保存路径
    private String repoPath = "/var/gitRepo/";//liunux 下载已有仓库到本地路径
    private String tmpPath = "/var/temp/";//linux 临时保存路径
    /**克隆远程仓库，用于config-server的native引用*/
    @Scheduled(cron = "5,15,25,35,45 * * * * ?")
    public void task1() throws IOException, GitAPIException {
        try{
            /*没有仓库则克隆，有仓库则拉取更新*/
            if (!new File(repoPath).exists()){
                cloneRepo(repoPath,"1");
            }else {
                pullRepo(repoPath);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /**克隆远程仓库，被flume上传到hdfs，用于备份*/
    //每天 0点 8点 16点定时 克隆远程仓库到新建路径下
//    @Scheduled(cron = "0 0 0,8,16 * * ?")
    @Scheduled(cron = "5,15,25,35,45 * * * * ?")
    public void task2(){
        try{
            /*克隆远程仓库到 tmpPath路径下，然后通过 Flume上传到 分布式文件系统 hdfs*/
            cloneRepo(tmpPath,"2");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /*克隆远程仓库到 path路径下;state=1表示上传到目标路径，该路径用于config-server的native引用；flag=2表示上传到tmp路径，tmp路径下的文件被flume上传到hdfs，用于备份*/
    public void cloneRepo(String path,String state){
        try{
            //克隆代码库命令
            CloneCommand cloneCommand = Git.cloneRepository();
            SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
            String suffix = sdf.format(new Date());
            if (state.equals("1")){
                path = path;
            }else if (state.equals("2")){
                path = path + suffix;
            }
            Git git= cloneCommand.setURI(remotePath) //设置远程URI
                    .setBranch("master") //设置clone下来的分支
                    .setDirectory(new File(path)) //设置下载存放路径
                    .setCredentialsProvider(usernamePasswordCredentialsProvider) //设置权限验证
                    .call();
            /*压缩，然后通过flume将 zip压缩包发送到 hdfs*/
            if (state.equals("2")){
                ZIPUtil.compress(path,path + ".zip");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /*拉取远程仓库到 path路径下*/
    public void pullRepo(String path){
        try {
            Git git = new Git(new FileRepository(path+"/.git"));
            git.pull().setRemoteBranchName("master").
                    setCredentialsProvider(usernamePasswordCredentialsProvider).
                    call();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}