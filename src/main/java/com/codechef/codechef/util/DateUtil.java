package com.codechef.codechef.util;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

public class DateUtil {
    public static List<Integer> daysInMonth(int month, int year) {
        YearMonth yearMonth = YearMonth.of(year, month);
        List<Integer> days = new ArrayList<>();
        for (int day = 1; day <= yearMonth.lengthOfMonth(); day++) {
            days.add(day);
        }
        return days;
    }
}
