package com.uykazdal.gezginapp.helper;

import java.util.Date;

public class DateHelper {

    public static String getUniqueTimeStamp() {
        Long time = new Date().getTime();
        String timeStr = String.valueOf(time);
        return timeStr.substring(7, timeStr.length());
        }
}
