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
        List<String> noPomList = new ArrayList<String>();

        String startTime = DateUtil.getNowDate();

//        String folderPath = "C:\\Users\\Erik\\Desktop\\testJar";
//        String repositoryId = "jarUploadTest";
//        String url = "http://10.180.210.148:8088/repository/jarUploadTest/";

        String folderPath = args[0];
        String repositoryId = args[1];
        String url = args[2];

        //识别操作系统
        String osName = System.getProperty("os.name");
        logger.info("该操作系统是：{}", osName);

        //扫描获取 jar 包列表
        List<String> jarFileList = FileScanner.getFiles(folderPath);

        for (String jarFile : jarFileList) {

            //拼接得到 pom 包路径
            String pomFilePath = jarFile.substring(0,jarFile.lastIndexOf("."))+ ".pom";

            //判断 pom 文件是否存在
            File pomFile = new File(pomFilePath);
            if (!pomFile.exists()){
                noPomList.add(jarFile);
                continue;
            }

            //解析 pom 文件
            PomInfo pomInfo = AnalyzePom.getPomInfo(pomFilePath);
            logger.info("pomFile:{} groupId:{} ArtifactId:{} Version:{}", pomInfo.getPomFile(), pomInfo.getGroupId(), pomInfo.getArtifactId(), pomInfo.getVersion());

            //jar 包存在，而且 pom 文件解析成功，则上传 jar 包
            if (pomInfo.isStatus()){

                //上传 jar 包
                boolean uploadJarResult = UploadJar.uploadJar(osName, pomInfo, jarFile, repositoryId, url);
                if (uploadJarResult){
                    jarUploadSucList.add(pomInfo);
                }else {
                    jarUploadFailList.add(pomInfo);
                }
            }else {
                analyzePomFailList.add(pomInfo);
            }

        }

        logger.info("==========只有 jar 包，没有 pom 文件的列表==========");
        for (String jarFile : noPomList) {
            logger.info("jarFile:{} ", jarFile);
        }

        logger.info("==========解析 pom 文件失败列表==========");
        for (PomInfo pomInfo : analyzePomFailList) {
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
                "\n 共扫描到 " + jarFileList.size() + " 个 jar 包" +
                "\n 只有 jar 包，没有 pom 文件的有 " + noPomList.size() + " 个" +
                "\n 解析失败 " + analyzePomFailList.size() + " 个 pom 文件" +
                "\n 上传成功 " + jarUploadSucList.size() + " 个 jar 包" +
                "\n 上传失败 " + jarUploadFailList.size() + " 个 jar 包");

    }

}
