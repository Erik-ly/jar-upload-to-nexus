package utils;

import org.apache.commons.lang.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * 校验字符串工具
 *
 * @author Erik
 * @date 2019/8/22
 */
public class CheckStringUtils {

    /**
     * 判断多个字符串是否含有空值
     * @param strs 多个字符串
     * @return 全都不为空则返回 true ； 至少有一个空值则返回 false
     */
    public static boolean strIsNull(String...strs){
        boolean isNull = false;

        for (String str: strs) {

            if (StringUtils.isEmpty(str)){
//                System.out.println(str + "is empty");
                isNull = true;
            }
        }

        return isNull;
    }


    /**
     * 去除字符串中的空格、回车、换行符、制表符等
     * @param str
     * @return
     */
    public static String replaceSpecialStr(String str){

        String repStr = "";

        if (!strIsNull(str)){
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            repStr = m.replaceAll("");
        }

        return repStr;
    }


}
