package org.dmfs.rfc5545.calendarmetrics;

import org.dmfs.rfc5545.Instance;
import org.dmfs.rfc5545.Weekday;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by cpu10639 on 22/06/2018.
 */

public class ChineseCalendarMetrics  extends CalendarMetrics{
    public ChineseCalendarMetrics(Weekday weekStart, int minDaysInFirstWeek) {

        super(weekStart, minDaysInFirstWeek);

    }

    public static int isEndOfMonth = -1;
    public void setIsEndOfMonth(int value) {
        isEndOfMonth = value;
    }

    public static int getNumDayOfYear(int year) {
        int result = 0;
        for (int i = 0; i < 12; i++) {
            NumDayOfMonthResult temp = LunarCalendarUtils.getNumDayOfMonth(year, i, 0);
            result += temp.getNumDay();
            if (temp.getFirstDayOfMonth()[3] == 1) {
                result += LunarCalendarUtils.getNumDayOfMonth(year, i, 1).getNumDay();
            }
        }

        return result;
    }

    public final static CalendarMetricsFactory FACTORY = new CalendarMetricsFactory()
    {

        @Override
        public CalendarMetrics getCalendarMetrics(Weekday weekStart)
        {
            return new ChineseCalendarMetrics(weekStart, 4);
        }


        public String toString()
        {
            return CALENDAR_SCALE_ALIAS;
        };
    };

    public final static String CALENDAR_SCALE_ALIAS = "CHINESE";
    public final static String CALENDAR_SCALE_NAME = "CHINA";

    @Override
    public int getMaxMonthDayNum() {
        return 30;
    }

    @Override
    public int getMaxYearDayNum() {
        return 385;
    }

    @Override
    public int getMaxWeekNoNum() {
        return 55;
    }

    boolean isLeapYear(int year)
    {
        return (year & 0x3) == 0 && year % 100 != 0 || year % 400 == 0;
    }

    private final static int[] DAYS_PER_MONTH = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

    @Override
    public int getDaysPerPackedMonth(int year, int packedMonth) { //input la thang va nam duong

        if (packedMonth == 1 && isLeapYear(year))
        {
            return DAYS_PER_MONTH[packedMonth] + 1;
        }
        else
        {
            return DAYS_PER_MONTH[packedMonth];
        }

//        return LunarCalendarUtils.getNumDayOfMonth(year, packedMonth, 0).getNumDay();

    }

    @Override
    public int getPackedMonthOfYearDay(int year, int yearDay) {
//        int result = 0;
//        int totalDay = 0;
//        while (totalDay < yearDay) {
//            result++;
//            totalDay += LunarCalendarUtils.getNumDayOfMonth(year, result);
//        }
//        return --result;

        int monthDay = getMonthAndDayOfYearDay(year, yearDay);
        return monthDay & (256 << 8);
    }

    @Override
    public int getDayOfMonthOfYearDay(int year, int yearDay) {
//        int result = 0;
//        int count = 0;
//        int totalDay = 0;
//        int numDayOfMonth = 0;
//        while (totalDay < yearDay) {
//            count++;
//            numDayOfMonth = LunarCalendarUtils.getNumDayOfMonth(year, count);
//            totalDay += numDayOfMonth;
//        }
//
//        if (totalDay == yearDay)
//            return 1;
//        else
//            return numDayOfMonth - (totalDay - yearDay);

        int monthDay = getMonthAndDayOfYearDay(year, yearDay);
        return monthDay & 256;

    }

    @Override
    public int getMonthAndDayOfYearDay(int year, int yearDay) {

        int month = 0;
        int day = 0;
        int totalDay = 0;
        int numDayOfMonth = 0;
        while (totalDay < yearDay) {
            month++;
            numDayOfMonth = LunarCalendarUtils.getNumDayOfMonth(year, month, 0).getNumDay();
            totalDay += numDayOfMonth;
        }

        month--;

        if (totalDay == yearDay)
            day = 1;
        else
            day = numDayOfMonth - (totalDay - yearDay);

        return (month << 8) + day;

    }

    @Override
    public int getYearDaysForPackedMonth(int year, int packedMonth) {

        int result = 0;
        for (int i = 0; i < packedMonth - 1; i++) {
            NumDayOfMonthResult temp = LunarCalendarUtils.getNumDayOfMonth(year, i, 0);
            result += temp.getNumDay();
            if (temp.getFirstDayOfMonth()[3] == 1) {
                result += LunarCalendarUtils.getNumDayOfMonth(year, i, 1).getNumDay();
            }
        }

        return result;
    }

    @Override
    public int getMonthsPerYear(int year) {


        return getDaysPerYear(year) > 360 ? 13 : 12;
    }

    @Override
    public int getDaysPerYear(int year) {

        int result = 0;
        for (int i = 0; i < 12; i++) {
            NumDayOfMonthResult temp = LunarCalendarUtils.getNumDayOfMonth(year, i, 0);
            result += temp.getNumDay();
            if (temp.getFirstDayOfMonth()[3] == 1) {
                result += LunarCalendarUtils.getNumDayOfMonth(year, i, 1).getNumDay();
            }
        }

        return result;
    }

    @Override
    public int getWeeksPerYear(int year) {

        int yd1st = getYearDayOfFirstWeekStart(year);
        int yearDays = getDaysPerYear(year) - yd1st + 1;
        int fullweeks = yearDays / 7;
        int remainingDays = yearDays % 7;

        return 7 - remainingDays >= minDaysInFirstWeek ? fullweeks : fullweeks + 1;
    }

    @Override
    public int getWeekOfYear(int year, int yearDay) {
        return 0;
    }

    @Override
    public int getDayOfYear(int year, int packedMonth, int dayOfMonth) {

        int result = 0;
        for (int i = 0; i < packedMonth - 1; i++) {
            NumDayOfMonthResult temp = LunarCalendarUtils.getNumDayOfMonth(year, i, 0);
            result += temp.getNumDay();
            if (temp.getFirstDayOfMonth()[3] == 1) {
                result += LunarCalendarUtils.getNumDayOfMonth(year, i, 1).getNumDay();
            }
        }

        return result + dayOfMonth;
    }

    @Override
    public int getYearDayOfIsoYear(int year, int weekOfYear, int dayOfWeek) {

        return weekOfYear * 7 - 7 + (dayOfWeek - weekStartInt + 7) % 7 + getYearDayOfFirstWeekStart(year);
    }

    @Override
    public int getWeekDayOfFirstYearDay(int year) {

        int y = year - 1;
        return (1 + 5 * (y & 3) + 4 * (y % 100) + 6 * (y % 400)) % 7;
    }

    @Override
    public int getYearDayOfFirstWeekStart(int year) {

        int jan1stWeekDay = getWeekDayOfFirstYearDay(year);

        int diff = weekStartInt - jan1stWeekDay;

        int yd = 1 + diff;

        return yd > minDaysInFirstWeek ? yd - 7 : yd < minDaysInFirstWeek - 6 ? yd + 7 : yd;
    }

    @Override
    public int getYearDayOfWeekStart(int year, int week) {

        return getYearDayOfFirstWeekStart(year) + (week - 1) * 7;
    }

    @Override
    public long toMillis(TimeZone timeZone, int year, int packedMonth, int dayOfMonth, int hours, int minutes, int seconds, int millis) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, packedMonth, dayOfMonth, hours, minutes, seconds);
        cal.setTimeZone(timeZone);

        long timestamp = cal.getTimeInMillis();
        return timestamp;
    }


    long getTimeStamp(int year, int yearDay, int hours, int minutes, int seconds, int millis)
    {
        long result = (year - 1970) * 365;
        result = (result + yearDay - 1 + numLeapDaysSince1970(year)) * 24;
        result = (result + hours) * 60;
        result = (result + minutes) * 60;
        result = (result + seconds) * 1000 + millis;

        return result;
    }


    int numLeapDaysSince1970(int year)

    {

        int prevYear = year - 1; // don't include year itself
        int leapYears = prevYear >> 2; // leap years since year 0
        int nonLeapYears = prevYear / 100; // non leap years that are divisible by 4 since year 0
        int yetLeapYears = nonLeapYears >> 2; // leap years that are divisible by 400 since year 0
        return (leapYears - 492) - (nonLeapYears - 19) + (yetLeapYears - 4); // the number of leap days is just the number of leap years
    }

    @Override
    public long toInstance(long timestamp, TimeZone timeZone) {

        Calendar cal = Calendar.getInstance();
//        if (timeZone != TimeZone.getDefault())
//        {
//            timestamp += (timeZone.getOffset(timestamp) - TimeZone.getDefault().getOffset(timestamp));
//        }
//        Date date = new Date(timestamp);
//        cal.setTime(date);

        cal.setTimeZone(timeZone);
        cal.setTimeInMillis(timestamp);

        int year1 = cal.get(Calendar.YEAR);
        int month1 = cal.get(Calendar.MONTH);
        int day1 = cal.get(Calendar.DAY_OF_MONTH);
        int hour1 = cal.get(Calendar.HOUR_OF_DAY);
        int minute1 = cal.get(Calendar.MINUTE);
        int second1 = cal.get(Calendar.SECOND);


        return Instance.make(year1, month1, day1, hour1, minute1, second1);


//        long localTime = timestamp;
//        if (timeZone != null)
//        {
//            localTime += timeZone.getOffset(timestamp);
//        }
//
//        // get the time of the day in milliseconds
//        int time = (int) (localTime % (24L * 3600L * 1000L));
//
//        // remove the time from the date
//        localTime -= time;
//
//        // adjust negative dates
//        if (time < 0)
//        {
//            time += 24 * 3600 * 1000;
//            localTime -= 24 * 3600 * 1000;
//        }
//
//        // the number of days that have passed since 0001-01-01
//        final int daysSince1 = (int) (localTime / (24 * 3600 * 1000L) + 365 * 1969 + 477);
//
//        // the number of full 400 year cycles and the remaining days
//        final int c400 = daysSince1 / (400 * 365 + 97);
//        final int c400Remainder = daysSince1 % (400 * 365 + 97);
//
//        // the number of full 100 year cycles and the remainder
//        final int c100 = Math.min((c400Remainder / (100 * 365 + 24)), 3 /* there are at most 3 full 100 year cycles in <400 years */);
//        final int c100Remainder = c400Remainder - c100 * (100 * 365 + 24);
//
//        // the number of 4 year cycles and the remaining days
//        final int c4 = Math.min((int) (c100Remainder / (4 * 365 + 1)), 24 /* there are at most 24 full 4 year cycles in <100 years */);
//        final int c4Remainder = (int) (c100Remainder - c4 * (4 * 365 + 1));
//
//        // the number of full years and the remaining days of the last year
//        final int c1 = Math.min(c4Remainder / 365, 3 /* there are at most 3 full year cycles in <4 years */);
//        final int c1Remainder = c4Remainder - 365 * c1 + 1 /* the first yearday is 1 not 0 */;
//
//        int year = ((c400 << 2) + c100) * 100 + (c4 << 2) + c1 + 1;
//
//        final int monthAndDay = getMonthAndDayOfYearDay(year, c1Remainder);
//
//        final int minutes = time / 60000;
//
//        return Instance.make(year, packedMonth(monthAndDay), dayOfMonth(monthAndDay), minutes / 60, minutes % 60, time / 1000 % 60);


    }

    @Override
    public long nextMonth(long instance)
    {
        double TZ = TimeZone.getTimeZone("Asia/Ho_Chi_Minh").getOffset(instance) / 3600000;

        int[] solar = new int[] {Instance.dayOfMonth(instance), Instance.month(instance) + 1, Instance.year(instance)};

        int start = LunarCalendarUtils.jdFromDate(solar[0], solar[1], solar[2]);

        int[] lunar = LunarCalendarUtils.convertSolar2Lunar(solar[0], solar[1], solar[2], TZ);

        if (ChineseCalendarMetrics.isEndOfMonth == -1) {
            int numDayOfNextMonth = LunarCalendarUtils.getNumDayOfMonth(lunar[2], lunar[1] - 1, lunar[3]).getNumDay();

            if (numDayOfNextMonth == 30 && lunar[0] == 30) {
                setIsEndOfMonth(1);
            } else {
                setIsEndOfMonth(0);
            }
        }

        int[] lunar1;

        if (isEndOfMonth == 1) {
            solar = LunarCalendarUtils.jdToDate(start + 30);
            lunar1 = LunarCalendarUtils.convertSolar2Lunar(solar[0], solar[1], solar[2], TZ);

            /* Doan code nay lay luon truong hop thang co 29 ngay */
//            if (lunar1[0] == 1) {
//                solar = LunarCalendarUtils.jdToDate(start + 29);
//            }

            /*Doan code nay skip qua nhung thang 29 ngay */
            int padding = 0;
            while (lunar[0] != lunar1[0]) {
                if (lunar1[0] == 1) {
                    padding += 29;
                } else {
                    padding += 30;
                }

                solar = LunarCalendarUtils.jdToDate(start + padding);
                lunar1 = LunarCalendarUtils.convertSolar2Lunar(solar[0], solar[1], solar[2], TZ);
            }


        } else {
            int padding = 29;
            solar = LunarCalendarUtils.jdToDate(start + 29);
            lunar1 = LunarCalendarUtils.convertSolar2Lunar(solar[0], solar[1], solar[2], TZ);

            while (lunar1[0] != lunar[0]) {
                padding++;
                solar = LunarCalendarUtils.jdToDate(start + padding);
                lunar1 = LunarCalendarUtils.convertSolar2Lunar(solar[0], solar[1], solar[2], TZ);

            }
        }

        /*Doan code nay skip qua thang 29 ngay nhung xu ly cham hon */
//        int padding = 29;
//        solar = LunarCalendarUtils.jdToDate(start + 29);
//        lunar1 = LunarCalendarUtils.convertSolar2Lunar(solar[0], solar[1], solar[2], TZ);
//
//        while (lunar1[0] != lunar[0]) {
//            padding++;
//            solar = LunarCalendarUtils.jdToDate(start + padding);
//            lunar1 = LunarCalendarUtils.convertSolar2Lunar(solar[0], solar[1], solar[2], TZ);
//
//
//        }



        return Instance.setYear(Instance.setMonth(Instance.setDayOfMonth(instance, solar[0]), solar[1] - 1), solar[2]);




    }


    @Override
    public long nextMonth(long instance, int n)
    {

        long newInstance = instance;
        for (int i = 0; i < n; i++) {
            newInstance = nextMonth(newInstance);
        }

        return newInstance;
    }


    @Override
    public long prevMonth(long instance)
    {

        int newMonth = Instance.month(instance) - 1;

        if (newMonth >= 0)
        {
            return Instance.setMonth(instance, newMonth);
        }
        else
        {
            return Instance.setYear(Instance.setMonth(instance, getDaysPerYear(Instance.year(instance)) - 1), Instance.year(instance) - 1);
        }
    }


    @Override
    public long prevMonth(long instance, int n)
    {

        if (n < 0)
        {
            throw new IllegalArgumentException("n must be >=0");
        }
        if (n == 0)
        {
            return instance;
        }

        int newMonth = Instance.month(instance) - n;
        int maxMonthsPerYear = getDaysPerYear(Instance.year(instance));

        if (newMonth >= 0)
        {
            return Instance.setMonth(instance, newMonth);
        }
        else
        {
            return Instance.setYear(Instance.setMonth(instance, (maxMonthsPerYear + newMonth % maxMonthsPerYear) % maxMonthsPerYear), Instance.year(instance)
                    + newMonth / maxMonthsPerYear);
        }
    }


    @Override
    public long nextDay(long instance)
    {

        int day = Instance.dayOfMonth(instance) + 1;
        int year = Instance.year(instance);
        int month = Instance.month(instance);

        if (day > getDaysPerPackedMonth(year, month))
        {
            day = 1;
            if (++month == getDaysPerYear(Instance.year(instance)))
            {
                instance = Instance.setYear(instance, year + 1);
                month = 0;
            }
            instance = Instance.setMonth(instance, month);
        }
        return Instance.setDayOfMonth(instance, day);
    }


    @Override
    public long nextDay(long instance, int n)
    {

        if (n < 0)
        {
            throw new IllegalArgumentException("n must be >=0");
        }
        if (n == 0)
        {
            return instance;
        }

        int year = Instance.year(instance);
        int month = Instance.month(instance);
        int day = Math.min(Instance.dayOfMonth(instance), getDaysPerPackedMonth(year, month));

        int yearDay = getDayOfYear(year, month, day) + n;
        int yearDays;
        while (yearDay > (yearDays = getDaysPerYear(year)))
        {
            yearDay -= yearDays;
            year++;
        }

        int monthAndDay = getMonthAndDayOfYearDay(year, yearDay);
        return Instance.setYear(Instance.setMonthAndDayOfMonth(instance, packedMonth(monthAndDay), dayOfMonth(monthAndDay)), year);
    }


    @Override
    public long prevDay(long instance)
    {

        int day = Math.min(Instance.dayOfMonth(instance) - 1, getDaysPerPackedMonth(Instance.year(instance), Instance.month(instance)));

        if (day <= 0)
        {
            int year = Instance.year(instance);
            int month = Instance.month(instance) - 1;
            if (month <= -1)
            {
                instance = Instance.setYear(instance, --year);
                month = getDaysPerYear(Instance.year(instance)) - 1;
            }
            day = getDaysPerPackedMonth(year, month);
            instance = Instance.setMonth(instance, month);
        }
        return Instance.setDayOfMonth(instance, day);
    }


    @Override
    public long prevDay(long instance, int n)
    {

        if (n < 0)
        {
            throw new IllegalArgumentException("n must be >=0");
        }
        if (n == 0)
        {
            return instance;
        }

        int year = Instance.year(instance);
        int month = Instance.month(instance);
        int day = Math.min(Instance.dayOfMonth(instance), getDaysPerPackedMonth(year, month) + 1);

        int yearDay = getDayOfYear(year, month, day) - n;
        while (yearDay < 1)
        {
            year--;
            yearDay += getDaysPerYear(year);
        }

        int monthAndDay = getMonthAndDayOfYearDay(year, yearDay);
        return Instance.setYear(Instance.setMonthAndDayOfMonth(instance, packedMonth(monthAndDay), dayOfMonth(monthAndDay)), year);
    }
}
