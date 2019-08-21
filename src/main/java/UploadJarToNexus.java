import bean.PomInfo;
import utils.DateUtil;
import utils.FileScanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
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
        List<PomInfo> noJarList = new ArrayList<PomInfo>();

        String startTime = DateUtil.getNowDate();

//        String folderPath = "C:\\Users\\Erik\\Desktop\\testJar";
//        String repositoryId = "jarUploadTest";
//        String url = "http://10.180.210.148:8088/repository/jarUploadTest/";

        String folderPath = args[0];
        String repositoryId = args[1];
        String url = args[2];

        //扫描 pom 文件
        List<String> fileList = FileScanner.getFiles(folderPath);

        //识别操作系统
        String osName = System.getProperty("os.name");
        logger.info("该操作系统是：{}", osName);

        for (String pomFile : fileList) {
            PomInfo pomInfo = AnalyzePom.getPomInfo(pomFile);

            logger.info("pomFile:{} groupId:{} ArtifactId:{} Version:{}", pomInfo.getPomFile(), pomInfo.getGroupId(), pomInfo.getArtifactId(), pomInfo.getVersion());

            //拼接得到 jar 包路径
            String jarFilePath = pomFile.substring(0,pomFile.lastIndexOf("."))+ ".jar";

            //判断 jar 包是否存在
            File jarFile = new File(jarFilePath);
            if (!jarFile.exists()){
                noJarList.add(pomInfo);
                continue;
            }

            //jar 包存在，而且 pom 文件解析成功，则上传 jar 包
            if (pomInfo.isStatus()){

                //上传 jar 包
                boolean uploadJarResult = UploadJar.uploadJar(osName, pomInfo, jarFilePath, repositoryId, url);
                if (uploadJarResult){
                    jarUploadSucList.add(pomInfo);
                }else {
                    jarUploadFailList.add(pomInfo);
                }
            }else {
                analyzePomFailList.add(pomInfo);
            }

        }

        logger.info("==========解析 pom 文件失败列表==========");
        for (PomInfo pomInfo : analyzePomFailList) {
            logger.info("pomFile:{} groupId:{} ArtifactId:{} Version:{}", pomInfo.getPomFile(), pomInfo.getGroupId(), pomInfo.getArtifactId(), pomInfo.getVersion());
        }

        logger.info("==========只有 pom 文件，没有 jar 包的列表==========");
        for (PomInfo pomInfo : noJarList) {
            logger.info("pomFile:{} groupId:{} ArtifactId:{} Version:{}", pomInfo.getPomFile(), pomInfo.getGroupId(), pomInfo.getArtifactId(), pomInfo.getVersion());
        }

        logger.info("==========上传 jar 包失败列表==========");
        for (PomInfo pomInfo : jarUploadFailList) {
            logger.info("pomFile:{} groupId:{} ArtifactId:{} Version:{}", pomInfo.getPomFile(), pomInfo.getGroupId(), pomInfo.getArtifactId(), pomInfo.getVersion());
        }

        //统计信息
        String endTime = DateUtil.getNowDate();

        logger.info("\n\n ==========统计信息==========" +
                "\n 开始时间 :" + startTime +
                "\n 结束时间 :" + endTime +
                "\n 共扫描到 " + fileList.size() + " 个 pom 文件" +
                "\n 解析成功 " + (fileList.size() - analyzePomFailList.size()) + " 个 pom 文件" +
                "\n 解析失败 " + analyzePomFailList.size() + " 个 pom 文件" +
                "\n 只有 pom 文件没有 jar 包的有 " + noJarList.size() + " 个" +
                "\n 上传成功 " + jarUploadSucList.size() + " 个 jar 包" +
                "\n 上传失败 " + jarUploadFailList.size() + " 个 jar 包");

    }

}
