package test;

import utils.FileScanner;

import java.util.List;

/**
 * @author Erik
 * @date 2019/8/23
 */
public class Test {
    public static void main(String[] args) {

        String dir = "C:\\Users\\Erik\\Desktop\\testJar\\excalibur-instrument\\excalibur-instrument-mgr-api\\2.1";
//        File file = new File(dir);
//        String[] list = file.list();
//        for (String d : list) {
//
//            if (d.endsWith("jar")){
//                System.out.println(d + " is jar");
//            }
//
//        }

        List<String> files = FileScanner.getFiles(dir);
        for (String file :
                files) {
            System.out.println(file);
        }

    }
}
