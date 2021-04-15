package com.example.leave.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * @author tungvd
 */
public class DayOfWeek {
    public static int getDayOfWeek(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_WEEK);
    }

    public static int saturdaySundayCount(Date d1, Date d2) {
        Calendar c1 = Calendar.getInstance();
        c1.setTime(d1);

        Calendar c2 = Calendar.getInstance();
        c2.setTime(d2);

        int sundays = 0;
        int saturday = 0;

        while (c2.after(c1)) {
            if (c2.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY ){
                sundays++;
            }else if (c2.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY){
                saturday++;
            }

            c2.add(Calendar.DATE, -1);
        }

        System.out.println("Saturday Count = " + saturday);
        System.out.println("Sunday Count = " + sundays);
        return saturday + sundays;
    }

    public static int workingDayCount(Date d1, Date d2) {
        Calendar c1 = Calendar.getInstance();
        c1.setTime(d1);

        Calendar c2 = Calendar.getInstance();
        c2.setTime(d2);

        int workingDays = 0;

        while (!c1.after(c2)) {
            if (c1.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && c1.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                workingDays++;
            }
            c1.add(Calendar.DATE, 1);
        }

        System.out.println("WorkingDays Count = " + workingDays);
        return workingDays;
    }
}
