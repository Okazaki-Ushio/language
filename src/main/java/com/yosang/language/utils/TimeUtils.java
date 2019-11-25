package com.yosang.language.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @AUTHOR YoSang
 * @DATE 11/7/2019
 */

public class TimeUtils {

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static DateTimeFormatter dtf=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static String nowSimpleDate(){
        Long time = System.currentTimeMillis();
        return sdf.format(new Date(time));
    }

    public static String nowTimeStamp(){
        Long time = System.currentTimeMillis();
        return time/1000+"";
    }

    public static String getTimeStampBySimpleDate(String time){
        try {
            long timeStamp = sdf.parse(time).getTime();
            return timeStamp/1000+"";
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getSimpleDateByTimeStamp(String time){
        return sdf.format(new Date(Long.valueOf(time) * 1000));
    }

    public static LocalDateTime getLocalDateTimeBySimpleDate(String time){
        return LocalDateTime.parse(time, dtf);
    }

    public static LocalDateTime getLocalDateTimeByTimeStamp(String time){
        return new Date(Long.valueOf(time) * 1000).toInstant().atOffset(ZoneOffset.of("+8")).toLocalDateTime();
    }

    public static boolean isToday(LocalDateTime localDateSTime) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime todayMidNight=LocalDateTime.of(now.getYear(),now.getMonth(),now.getDayOfMonth(),0,0);
        LocalDateTime tomorrow=todayMidNight.plusDays(1);
        return localDateSTime.isBefore(tomorrow)&&localDateSTime.isAfter(todayMidNight);
    }
}
