import bean.PomInfo;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import utils.CheckStringUtils;

import java.util.Iterator;

/**
 * 解析 PomInfo 信息
 *
 * @author Erik
 * @date 2019/8/15
 */
public class AnalyzePom {

    private static String groupId = null;
    private static String artifactId = null;
    private static String version = null;

    public static PomInfo getPomInfo(String pomFile){

        groupId = null;
        artifactId = null;
        version = null;

        PomInfo pomInfo = new PomInfo();

        SAXReader saxReader = new SAXReader();

            try {
                Document document = saxReader.read(pomFile);
                Element employees = document.getRootElement();

                for (Iterator i = employees.elementIterator(); i.hasNext(); ) {
                    Element employee = (Element) i.next();

                    if (employee.getName().equals("groupId")){
                        groupId = employee.getText();
                    }

                    if (employee.getName().equals("artifactId")){
                        artifactId = employee.getText();
                    }

                    if (employee.getName().equals("version")){
                        version = employee.getText();
                    }

                    if (CheckStringUtils.strIsNull(groupId, artifactId, version)){

                        if (employee.getName().equals("parent")) {

                            for (Iterator j = employee.elementIterator(); j.hasNext(); ) {
                                Element node = (Element) j.next();

                                if (node.getName().equals("groupId")) {
                                    groupId = node.getText();
                                }

                                if (node.getName().equals("artifactId")) {
                                    artifactId = node.getText();
                                }

                                if (node.getName().equals("version")) {
                                    version = node.getText();
                                }
                            }
                        }
                    }
                }

                pomInfo.setPomFile(pomFile);
                pomInfo.setGroupId(groupId);
                pomInfo.setArtifactId(artifactId);
                pomInfo.setVersion(version);

                if (CheckStringUtils.strIsNull(groupId, artifactId, version)){
                    pomInfo.setStatus(false);
                }else {
                    pomInfo.setStatus(true);
                }

            } catch (DocumentException e) {
                e.printStackTrace();
            }

        return pomInfo;
    }
}
