import bean.PomInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 上传 jar 包方法
 * @author Erik
 * @date 2019/8/15
 */
public class UploadJar {

    private static Logger logger = LoggerFactory.getLogger(UploadJar.class);

    public static boolean uploadJar(String osName, PomInfo pomInfo, String jarFilePath, String repositoryId, String url){

        boolean uploadStatus = false;

        String mvnHead = null;
        if (osName.startsWith("Windows")){
            mvnHead = "cmd /c mvn deploy:deploy-file "; //结尾带空格
        }else {
            mvnHead = "mvn deploy:deploy-file "; //结尾带空格
        }

        String installStr = mvnHead +
                "-Dfile=" + jarFilePath +
                " -DgroupId=" + pomInfo.getGroupId() +
                " -DartifactId=" + pomInfo.getArtifactId() +
                " -Dversion=" + pomInfo.getVersion() +
                " -Dpackaging=jar" +
                " -DrepositoryId=" + repositoryId +
                " -Durl=" + url;

        logger.info("installStr:==========={}", installStr);

        try {
            Runtime rt = Runtime.getRuntime();
//            Process process = rt.exec("cmd /c mvn -V");
            Process process = rt.exec(installStr);
            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = null;
            while((line=br.readLine())!=null){
                System.out.println(new String(line.getBytes(), "GBK"));
            }
            br.close();

            int result = 10;
            try {
                result = process.waitFor();

                if (result == 0){
                    uploadStatus = true;
                }

                logger.info("result========={}", result);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return uploadStatus;

    }

}
