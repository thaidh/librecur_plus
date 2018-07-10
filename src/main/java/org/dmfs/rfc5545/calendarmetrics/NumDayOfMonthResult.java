package org.dmfs.rfc5545.calendarmetrics;

/**
 * Created by cpu10639 on 22/06/2018.
 */

public class NumDayOfMonthResult {
    public NumDayOfMonthResult() {
        numDay = 0;
        firstDayOfMonth = new int[]{0, 0, 0, 0};
    }

    public NumDayOfMonthResult(int numDay, int[] firstDayOfMonth) {
        this.numDay = numDay;
        this.firstDayOfMonth = firstDayOfMonth;
    }

    int numDay;
    public int getNumDay() {
        return numDay;
    }

    int[] firstDayOfMonth;
    public int[] getFirstDayOfMonth() {
        return firstDayOfMonth;
    }


}
