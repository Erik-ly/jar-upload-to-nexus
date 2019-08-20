package utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Erik
 * @date 2019/7/4
 */
public class DateUtil {

    public static String getNowDate(){

        SimpleDateFormat sdf = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");

        String nowDateStr = sdf.format(new Date());

        return nowDateStr;
    }

    public static void main(String[] args) {
        System.out.println(getNowDate());
    }

}
