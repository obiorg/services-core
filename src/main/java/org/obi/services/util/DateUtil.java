/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.obi.services.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * DateUtil
 * 
 *
 * @author r.hendrick
 *
 */
public class DateUtil {

    /**
     * Now return the current date time from local date time of systeme default
     * <br>
     * now is base on LocalDateTime and zone
     *
     * @return the current date time from local date time of system default
     */
    public static Date now() {
        return Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Maintenant is an overloaded mehtod now for frensh programming
     *
     * @return current date time from local date time
     */
    public static Date maintenant() {
        return now();
    }

    /**
     * today return the current day
     *
     * @return the actual day and time
     */
    public static Date today() {
        LocalDateTime ldt = LocalDateTime.ofInstant((new Date()).toInstant(), ZoneId.systemDefault());
        return Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Give the actual year of current system infos
     *
     * @return the actual year
     */
    public static Integer year() {
        LocalDateTime ldt = LocalDateTime.ofInstant((new Date()).toInstant(), ZoneId.systemDefault());
        return ldt.getYear();
    }

    /**
     * Give the actual month value
     *
     * @return the actual month value
     */
    public static Integer month() {
        LocalDateTime ldt = LocalDateTime.ofInstant((new Date()).toInstant(), ZoneId.systemDefault());
        return ldt.getMonthValue();
    }

    /**
     * Give the actual day of the month
     *
     * @return the actual day of the month
     */
    public Integer dayOfMonth() {
        LocalDateTime ldt = LocalDateTime.ofInstant((new Date()).toInstant(), ZoneId.systemDefault());
        return ldt.getDayOfMonth();
    }

    /**
     * This method recrate the local date and time of today with last time
     * 23:59:59
     *
     * @return the actual day with last time of the day 23:59:59
     */
    public Date todayLastTime() {
        LocalDateTime ldt = LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 59, 59));
        return Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Convert a date specified by following paremeter to date time
     *
     * @param year the year between 1900...xxxx
     * @param month the month between 1...12
     * @param day the day number between 1...31
     * @param hour the hour number between 0...23
     * @param min the minutes number between 0...59
     * @param sec the secondes number between 0...59
     * @return the corresponding date time from the specifies parameters
     */
    public static Date toDate(Integer year, Integer month, Integer day, Integer hour, Integer min, Integer sec) {
        LocalDateTime ldt = LocalDateTime.of(LocalDate.of(year, month, day), LocalTime.of(hour, min, sec));
        return Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Convert a date specified by following parameter to date time without
     * specifying the second
     *
     * @param year the year between 1900...xxxx
     * @param month the month between 1...12
     * @param day the day number between 1...31
     * @param hour the hour number between 0...23
     * @param min the minutes number between 0...59
     * @return the corresponding date time from the specifies parameters
     */
    public static Date toDate(Integer year, Integer month, Integer day, Integer hour, Integer min) {
        return toDate(year, month, day, hour, min, 0);
    }

    /**
     * Method permettant de traduire la variable de deux date
     *
     * @param value a value
     * @param filter a object filtred
     * @param locale a local
     * @return true if ok
     * @throws ParseException an error
     */
    public boolean handleDateRangeFilters(Object value, Object filter, Locale locale) throws ParseException {
        String filterText = (filter == null) ? null : filter.toString().trim();
        if (filterText == null || filterText.equals("")) {
            return true;
        }
        if (value == null) {
            return false;
        }

        //{"start":"2016-04-18","end":"2016-05-31"}
        if (!filterText.contains("start")) {
            return false;
        }
        String strDate = filterText;
        strDate = strDate.replace("\"", "").replace(":", "")
                .replace("{", "").replace("}", "")
                .replace("start", "").replace("end", "");
        String dates[] = strDate.split(",");
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.FRENCH);
        Date begin = format.parse(dates[0]);
        Date end = format.parse(dates[1]);

        //Extend limite in order to make it containt
        Calendar calValue = Calendar.getInstance();
        Calendar calBegin = Calendar.getInstance();
        Calendar calEnd = Calendar.getInstance();
        calValue.setTime((Date) value);
        calBegin.setTime((Date) begin);
        calEnd.setTime((Date) end);
        calBegin.add(Calendar.DAY_OF_MONTH, -1);
        calEnd.add(Calendar.DAY_OF_MONTH, +1);
        begin = calBegin.getTime();
        end = calEnd.getTime();

        //Check contain
        if (value instanceof Date) {
            Date date = (Date) value;
            if (date.before(begin) && !date.equals(begin)) {
                return false;
            }
            if (date.after(end) && !date.equals(end)) {
                return false;
            }
            return true;
        }
        return false;
    }
}
