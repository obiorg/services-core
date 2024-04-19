/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.obi.services.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

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

    /**
     * Display Time ZOne
     *
     * From a id of time zone display will format correct time zone in formt
     * [GMT]SIGN[HH:MM] ZONE
     *
     * @param tz the timezone id
     * @return a string format display of time zone [GMT]SIGN[HH:MM] ZONE
     */
    public static String displayTimeZone(TimeZone tz) {

        long hours = TimeUnit.MILLISECONDS.toHours(tz.getRawOffset());
        long minutes = TimeUnit.MILLISECONDS.toMinutes(tz.getRawOffset()) - TimeUnit.HOURS.toMinutes(hours);
        // avoid -4:-30 issue
        minutes = Math.abs(minutes);

        String result = "";
        if (hours >= 0) {
            result = String.format("[GMT]+[%02d:%02d] \t %s", hours, minutes, tz.getID());
        } else {
            result = String.format("[GMT]-[%02d:%02d] \t %s", hours, minutes, tz.getID());
        }

        return result;

    }

    /**
     * Time Zones Get a list of available TimeZone defined by ID
     *
     * It will convert in presentable way of display string list using
     * #{@link DateUtil#displayTimeZone(java.util.TimeZone)}.
     *
     * @return a string list of time zone represent as GMT TIME ZONE
     */
    public static List<String> timeZones() {
        List<String> tzs = new ArrayList<>();
        String[] ids = TimeZone.getAvailableIDs();
        for (String id : ids) {
            tzs.add(displayTimeZone(TimeZone.getTimeZone(id)));
        }
        return tzs;
    }

    /**
     * Time ZonesId
     *
     * Will create a string list of time zones id.
     *
     * @return string list of time zone id
     */
    public static List<String> timeZonesId() {
        List<String> tzs = new ArrayList<>();
        String[] ids = TimeZone.getAvailableIDs();
        for (String id : ids) {
            tzs.add(id);
        }
        return tzs;
    }

    /**
     * Order Time Zone
     *
     * Create an ordered (sorted) list of timezone extracted from
     * {@link DateUtil#timeZones()}
     *
     * @return sorted list of timezone extract from timeZones()
     */
    public static List<String> orderTimeZones() {
        List<String> tzs = timeZones();
        Collections.sort(tzs);
        return tzs;
    }

    /**
     * ZoneId
     *
     * Recover zoneId from index of {@link DateUtil#timeZones}
     *
     * @param positionInOrderTimeZone position in list of ordered time zone see
     * {@link DateUtil#timeZones}
     * @return corresponding zoneId of the index position in the orderTimeZones
     * see {@link DateUtil#timeZones}
     */
    public static String zoneIdOf(Integer positionInOrderTimeZone) {
        String timeZone = orderTimeZones().get(positionInOrderTimeZone);
        if (timeZone == null || timeZone.isEmpty()) {
            return null;
        }
        String clean = timeZone.split("\t")[1]
                .replace("[", "")
                .replace("]", "")
                .replace(" ", "");
        return clean;
    }

    /**
     * Now Formatted
     *
     * Allow to recover a string datetime from local define by time zone id from
     * index issue from {@link DateUtil#orderTimeZones()}.
     *
     * @param orderTimeZonesIndex is position index in orderTimeZone list
     * @param pattern correspond to display datetime output ex: "yyyy-MM-dd
     * hh:mm:ss.SSS"
     * @return string formatted datetime pattern
     */
    public static String nowFormatted(Integer orderTimeZonesIndex, String pattern) {
        LocalDateTime dt = LocalDateTime.now(ZoneId.of(
                zoneIdOf(orderTimeZonesIndex)));
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(pattern);
        return dt.format(dtf);
    }

    /**
     * Now Formatted
     *
     * Surcharge method allow to recover a string datetime from local define by
     * time zone id from index issue from {@link DateUtil#orderTimeZones()}.
     * Default pattern is set to "yyyy-MM-dd hh:mm:ss.SSS"
     *
     * @param orderTimeZonesIndex is position index in orderTimeZone list
     * @return string formatted datetime pattern
     */
    public static String nowFormatted(Integer orderTimeZonesIndex) {
        return nowFormatted(orderTimeZonesIndex, "yyyy-MM-dd hh:mm:ss.SSS");
    }

    /**
     * Collect now instant of timezone
     *
     * @param orderTimeZonesIndex is position index in orderTimeZone list
     * @return instant of specify index orderTimeZone
     */
    public static Instant instantOf(Integer orderTimeZonesIndex) {
        LocalDateTime dt = LocalDateTime.now(
                ZoneId.of(zoneIdOf(orderTimeZonesIndex)));
        return dt.atZone(
                ZoneId.of(zoneIdOf(orderTimeZonesIndex))).toInstant();
    }

    public static Instant instantOf(Integer orderTimeZonesIndex, LocalDateTime dt) {
        return dt.atZone(
                ZoneId.of(zoneIdOf(orderTimeZonesIndex))).toInstant();
    }

    /**
     * Convert {@link DateUtil#instantOf(java.lang.Integer) }
     *
     * @param orderTimeZonesIndex is position index in orderTimeZone list
     * @return millis of epoch instant since
     */
    public static Long epochMilliOf(Integer orderTimeZoneIndex) {
        return instantOf(orderTimeZoneIndex).toEpochMilli();
    }

    /**
     * Convert
     * {@link DateUtil#instantOf(java.lang.Integer, java.time.LocalDateTime)}
     *
     * @param orderTimeZonesIndex is position index in orderTimeZone list
     * @return millis of epoch instant since
     */
    public static Long epochMilliOf(Integer orderTimeZoneIndex, LocalDateTime dt) {
        return instantOf(orderTimeZoneIndex, dt).toEpochMilli();
    }

    /**
     * sql datetime of
     *
     * Provide a structure datetime define as "yyyy-MM-dd hh:mm:ss.SSS"
     * specified by dt
     *
     * @param dt a {@code LocalDateTime} to be converted as string fixed pattern
     * @return a string from LocalDateTime in pattern "yyyy-MM-dd hh:mm:ss.SSS"
     */
    public static String sqlDTof(LocalDateTime dt) {
        return ""
                + dt.getYear() + "-"
                + dt.getMonthValue() + "-"
                + dt.getDayOfMonth() + " "
                + dt.getHour() + ":"
                + dt.getMinute() + ":"
                + dt.getSecond() + "."
                + String.valueOf(dt.getNano()).substring(0, 3);
    }

    /**
     * sql datetime convert
     *
     * Convert a local datetime to a pattern sql "yyyy-MM-dd hh:mm:ss.SSS" using
     * {@link DateUtil#sqlDTof(java.time.LocalDateTime)} thus convert this one
     * using sql convert fonction like "CONVERT(datetime, '" + sqlDTConvert(dt)
     * + "', 121)"
     *
     * @param dt a {@code LocalDateTime} to be converted as string fixed pattern
     * @return "CONVERT(datetime, '" + sqlDTConvert(dt) + "', 121)"
     */
    public static String sqlDTConvert(LocalDateTime dt) {
        return "CONVERT(datetime, '"
                + sqlDTof(dt)
                + "', 121)";
    }

    /**
     * Return now from orderTimeZoneIndex
     *
     * @param orderTimeZonesIndex index in order timeZone
     * @return actual LocalDateTime now
     */
    public static LocalDateTime now(Integer orderTimeZonesIndex) {
        return LocalDateTime.now(ZoneId.of(
                zoneIdOf(orderTimeZonesIndex)));
    }

    /**
     * Get part hour from a display time zone
     *
     * @param timeZone is like [GMT]+[%02d:%02d] \t %s or like [GMT]-[%02d:%02d]
     * \t %s
     * @return
     */
    public static Integer hourFromDisplayTimeZone(String timeZone) {
        if (timeZone == null) {
            return 0;
        }
        if (timeZone.isEmpty()) {
            return 0;
        }
        String clean = timeZone.replace("[GMT]", "").split("\t")[0].replace("+", "").replace("-", "").replace("[", "").replace("]", "");
        String[] a = clean.split(":");
        if (a.length >= 1) {
            return Integer.valueOf(a[0]);
        }
        return 0;
    }

    /**
     * Get part hour from a display time zone
     *
     * @param timeZone is like [GMT]+[%02d:%02d] \t %s or like [GMT]-[%02d:%02d]
     * \t %s
     * @return
     */
    public static Integer minFromDisplayTimeZone(String timeZone) {
        if (timeZone == null) {
            return 0;
        }
        if (timeZone.isEmpty()) {
            return 0;
        }
        String clean = timeZone.replace("[GMT]", "").split("\t")[0].replace("+", "").replace("-", "").replace("[", "").replace("]", "").replace(" ", "");
        String[] a = clean.split(":");
        if (a.length >= 2) {
            return Integer.valueOf(a[1]);
        }
        return 0;
    }

    /**
     * Hour in OrderTimeZone List from sepecify position
     *
     * @param position in time zone ordered list see
     * {@link  DateUtil#orderTimeZones()}
     * @return hour from the time zone defined by position in index of order
     * time zone
     */
    public static Integer hourAtOrderTimeZonePosition(Integer position) {
        return hourFromDisplayTimeZone(orderTimeZones().get(position));
    }

    /**
     * Minutes in OrderTimeZone List from sepecify position
     *
     * @param position in time zone ordered list see
     * {@link  DateUtil#orderTimeZones()}
     * @return minute from the time zone defined by position in index of order
     * time zone
     */
    public static Integer minAtOrderTimeZonePosition(Integer position) {
        return minFromDisplayTimeZone(orderTimeZones().get(position));
    }

    /**
     * HTZ hours of time zone saved
     *
     * @param o integer value from a index save from ordered time zone
     * @return hours of time zone save in config
     */
    public static Integer hTZ(Object o) {
        Integer gmt = 0;
        if (o != null) {
            gmt = Integer.valueOf(o.toString());
        }
        return hourAtOrderTimeZonePosition(gmt);
    }

    /**
     * MTZ minute of time zone saved
     *
     * @param o integer value from a index save from ordered time zone
     * @return minute of time zone saved in config
     */
    public static Integer mTZ(Object o) {
        Integer gmt = 0;
        if (o != null) {
            gmt = Integer.valueOf(o.toString());
        }
        return minAtOrderTimeZonePosition(gmt);
    }

    /**
     * STZ secode corresponding of time zone saved mean combination of hour and
     * minute
     *
     * @param h hour to be converted in second
     * @param m minutes to be converted in second
     * @return GMT hour and minute in second
     */
    public static Integer sTZ(Object h, Object m) {
        return (hTZ(h) * 3600) + (mTZ(m) * 60);
    }

    /**
     * Recover GMT Writing from time zone specify by index from orderTimeZone
     *
     * @param positionInOrderTimeZone position in order time zone
     * @return GMT time like ZoneId like GMT+02:00
     */
    public static String GMTFromTimeZone(Integer positionInOrderTimeZone) {
        String timeZone = orderTimeZones().get(positionInOrderTimeZone);
        if (timeZone == null) {
            return null;
        }
        if (timeZone.isEmpty()) {
            return null;
        }
        String clean = timeZone.split("\t")[0].replace("[", "").replace("]", "").replace(" ", "");
        return clean;
    }

    /**
     * Local date time formatted allow to get current date time in formatted
     * pattern
     *
     * @param indexOrderTimeZone current index in order time zone
     * @param pattern is define by yyyy MM dd HH mm ss SSS n
     * @return a string of local date time formated by parameter
     */
    public static String localDateTimeFormatted(Integer indexOrderTimeZone, String pattern) {
        String gmt = GMTFromTimeZone(indexOrderTimeZone);
        LocalDateTime dateTimeNow = LocalDateTime.now(ZoneId.of(gmt));
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(pattern);
        return dtf.format(dateTimeNow);
    }

    /**
     * Local Date Time formatted surcharge method of
     * {@link DateUtil#localDateTimeFormatted(java.lang.Integer, java.lang.String)}
     * In this case, pattern is predefined as "yyyy-MM-dd HH:mm:ss.SSS"
     *
     * @param indexOrderTimeZone current index in order time zone
     * @return a string of local date time formated by parameter
     */
    public static String localDateTimeFormatted(Integer indexOrderTimeZone) {
        String gmt = GMTFromTimeZone(indexOrderTimeZone);
        LocalDateTime dateTimeNow = LocalDateTime.now(ZoneId.of(gmt));
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        return dtf.format(dateTimeNow);
    }

    /**
     * Local date time formatted from Instant and zoneId define in
     * indexOrderTimeZone
     *
     *
     *
     * @param indexOrderTimeZone current index in order time zone
     * @return a string of local date time formated by parameter
     */
    public static String localDateTimeFormattedFromZoneId(Integer indexOrderTimeZone) {
        String gmt = GMTFromTimeZone(indexOrderTimeZone);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        ZonedDateTime zdt = Instant.now().atZone(ZoneId.of(gmt));
        return dtf.format(zdt);
    }

    /**
     * localDTFFZoneId
     *
     * Surcharge method of
     * {@link  DateUtil#localDateTimeFormattedFromZoneId(java.lang.Integer)}
     *
     * @param indexOrderTimeZone current index in order time zone
     * @return a string of local date time formated by parameter
     */
    public static String localDTFFZoneId(Integer indexOrderTimeZone) {
        return localDateTimeFormattedFromZoneId(indexOrderTimeZone);
    }

    /**
     * toLocalDateTime<p>
     * Allow to convert a specify #{code Timestamp} to a LocalDateTime
     *
     * @param timestamp the timestamp to convert to local date time
     * @return the timestamp converted to local date time
     */
    public static LocalDateTime toLocalDateTime(Timestamp timestamp) {
        if (timestamp == null) {
            timestamp = Timestamp.valueOf(LocalDateTime.now());
        }
        return timestamp.toLocalDateTime();
    }
}
