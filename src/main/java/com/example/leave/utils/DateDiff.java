package com.example.leave.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author tungvd
 */
public class DateDiff {
    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
    }

    private static Calendar cal = Calendar.getInstance();

    public static int getHour(Date date){
        cal.setTime(date);
        return cal.get(Calendar.HOUR_OF_DAY);
    }

    public static int getMinutes(Date date){
        cal.setTime(date);
        return cal.get(Calendar.MINUTE);
    }

    public static int getDate(Date date){
        cal.setTime(date);
        return cal.get(Calendar.DATE);
    }

    public static int getMonth(Date date){
        cal.setTime(date);
        return cal.get(Calendar.MONTH);
    }
}
