package com.example.leave.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * @author tungvd
 */
public class DayOfWeek {
    public static int getDayNumberOld(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_WEEK);
    }
}
