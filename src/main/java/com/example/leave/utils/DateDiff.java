package com.example.leave.utils;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author tungvd
 */
public class DateDiff {
    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillieS = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillieS,TimeUnit.MILLISECONDS);
    }

    public static long getDaysDiff(Date date1, Date date2){
        long difference_In_Time = date2.getTime() - date1.getTime();
        long difference_In_Days
                = (difference_In_Time
                / (1000 * 60 * 60 * 24))
                % 365;
        return difference_In_Days;
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

    public static Date getFirstDateOfMonth(Date date){
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        return cal.getTime();
    }

    public static Date getLastDateOfMonth(Date date){
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        return cal.getTime();
    }
}
