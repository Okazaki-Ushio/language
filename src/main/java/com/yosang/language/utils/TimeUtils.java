package com.yosang.language.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @AUTHOR YoSang
 * @DATE 11/7/2019
 */

public class TimeUtils {

    public static String now(){
        Long time = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date(time));
    }
}
