import bean.PomInfo;
import utils.DateUtil;
import utils.FileScanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Erik
 * @date 2019/8/15
 */
public class UploadJarToNexus {

    private static Logger logger = LoggerFactory.getLogger(UploadJarToNexus.class);

    public static void main(String[] args) {

        List<PomInfo> jarUploadSucList = new ArrayList<PomInfo>();
        List<PomInfo> jarUploadFailList = new ArrayList<PomInfo>();
        List<PomInfo> analyzePomFailList = new ArrayList<PomInfo>();

        String startTime = DateUtil.getNowDate();

        String folderPath = "C:\\Users\\Erik\\Desktop\\testJar";
        String repositoryId = "jarUploadTest";
        String url = "http://10.180.210.148:8088/repository/jarUploadTest/";

//        String folderPath = args[0];
//        String repositoryId = args[1];
//        String url = args[2];

        List<String> fileList = FileScanner.getFiles(folderPath);

        //识别操作系统
        String osName = System.getProperty("os.name");
        logger.info("该操作系统是：{}", osName);

        for (String pomFile : fileList) {
            PomInfo pomInfo = AnalyzePom.getPomInfo(pomFile);

            logger.info("pomFile:{} groupId:{} ArtifactId:{} Version:{}", pomInfo.getPomFile(), pomInfo.getGroupId(), pomInfo.getArtifactId(), pomInfo.getVersion());

            if (pomInfo.isStatus()){

                //上传 jar 包
                boolean uploadJarResult = UploadJar.uploadJar(osName, pomInfo, repositoryId, url);
                if (uploadJarResult){
                    jarUploadSucList.add(pomInfo);
                }else {
                    jarUploadFailList.add(pomInfo);
                }
            }else {
                analyzePomFailList.add(pomInfo);
            }

        }

        String endTime = DateUtil.getNowDate();

        logger.info("==========统计信息==========" +
                "\n 开始时间 :" + startTime +
                "\n 结束时间 :" + endTime +
                "\n 共扫描到 " + fileList.size() + " 个 pom 文件" +
                "\n 解析成功 " + (fileList.size() - analyzePomFailList.size()) + " 个 pom 文件" +
                "\n 解析失败 " + analyzePomFailList.size() + " 个 pom 文件" +
                "\n 上传成功 " + jarUploadSucList.size() + " 个 jar 包" +
                "\n 上传失败 " + jarUploadFailList.size() + " 个 jar 包");

//        logger.info("开始时间 :{}",startTime);
//        logger.info("结束时间 :{}",endTime);
//
//        logger.info("共扫描到 {} 个 pom 文件", fileList.size());
//        logger.info("解析成功 {} 个 pom 文件", fileList.size() - analyzePomFailList.size());
//        logger.info("解析失败 {} 个 pom 文件", analyzePomFailList.size());
//        logger.info("上传成功 {} 个 jar 包", jarUploadSucList.size());
//        logger.info("上传失败 {} 个 jar 包", jarUploadFailList.size());

        logger.info("==========解析 pom 文件失败列表==========");
        for (PomInfo pomInfo : analyzePomFailList) {
            logger.info("pomFile:{} groupId:{} ArtifactId:{} Version:{}", pomInfo.getPomFile(), pomInfo.getGroupId(), pomInfo.getArtifactId(), pomInfo.getVersion());
        }

        logger.info("==========上传 jar 包失败列表==========");
        for (PomInfo pomInfo : jarUploadFailList) {
            logger.info("pomFile:{} groupId:{} ArtifactId:{} Version:{}", pomInfo.getPomFile(), pomInfo.getGroupId(), pomInfo.getArtifactId(), pomInfo.getVersion());
        }

    }





}
