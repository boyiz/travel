package com.xunye.core.tools;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DateTools extends DateUtil {

    private static final Log logger = LogFactory.getLog(DateUtils.class);

    /**
     * 标准日期格式化
     */
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 时间后缀
     * 一天中最早
     */
    private static final String TIME_SUFFIX_FIRST = " 00:00:00";

    /**
     * 时间后缀
     * 一天中最晚
     */
    private static final String TIME_SUFFIX_LAST = " 23:59:59";

    /**
     * 季度枚举值
     */
    private static final String[] QUARTERLY_ARRAY = {
            "Q1", "Q2", "Q3", "Q4"
    };

    /**
     * 标准日期format
     */
    public static final String YYYYMMDD = "yyyy-MM-dd";

    /**
     * 中文日期
     */
    public static final String YYYYMMDD_ZH = "yyyy年MM月dd日";

    /**
     * 定义周一
     */
    public static final int FIRST_DAY_OF_WEEK = Calendar.MONDAY; // 中国周一是一周的第一天

    /**
     * 获取当月第一天
     */
    public static Date getCurMonthFirst() {
        Calendar thisMonthFirstDateCal = Calendar.getInstance();
        thisMonthFirstDateCal.set(Calendar.DAY_OF_MONTH, 1);
        return thisMonthFirstDateCal.getTime();
    }

    /**
     * 获取当月第一天
     * 返回字符串
     */
    public static String getCurMonthFirstStr() {
        Date date = getCurMonthFirst();
        return DATE_FORMAT.format(date);
    }

    /**
     * 获取当月最后一天
     */
    public static Date getCurMonthLast() {
        Calendar thisMonthEndDateCal = Calendar.getInstance();
        thisMonthEndDateCal.set(Calendar.DAY_OF_MONTH, thisMonthEndDateCal.getActualMaximum(Calendar.DAY_OF_MONTH));
        return thisMonthEndDateCal.getTime();
    }

    /**
     * 获取当月最后一天
     * 字面量
     */
    public static String getCurMonthLastStr() {
        Date curMonthLast = getCurMonthLast();
        return DATE_FORMAT.format(curMonthLast);
    }

    /**
     * 获取一天的最早时间
     * yyyy-MM-dd HH:mm:ss
     */
    public static Date getCurDayFirst() {
        String today = today();
        return parse(today + TIME_SUFFIX_FIRST, DatePattern.NORM_DATETIME_PATTERN);
    }

    /**
     * 获取一天的最早时间
     * yyyy-MM-dd HH:mm:ss
     */
    public static Date getCurDayLast() {
        String today = today();
        return parse(today + TIME_SUFFIX_LAST, DatePattern.NORM_DATETIME_PATTERN);
    }

    /**
     * LocalDate转Date
     *
     * @param localDate localDate
     * @return date
     */
    public static Date localDate2Date(LocalDate localDate) {
        if (null == localDate) {
            return null;
        }
        ZonedDateTime zonedDateTime = localDate.atStartOfDay(ZoneId.systemDefault());
        return Date.from(zonedDateTime.toInstant());
    }

    /**
     * Date转LocalDate
     *
     * @param date date
     */
    public static LocalDate date2LocalDate(Date date) {
        if (null == date) {
            return null;
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * 根据月份获取所在季度
     *
     * @param month 月份
     * @return 所在季度Q1/Q2/Q3/Q4
     */
    public static String getQuarterByMonth(int month) {
        int index = ((month - 1) / 3);
        return QUARTERLY_ARRAY[index];
    }

    /**
     * @param strDate
     * @return
     */
    public static Date parseDate(String strDate) {
        return parseDate(strDate, null);
    }

    /**
     * parseDate
     *
     * @param strDate
     * @param pattern
     * @return
     */
    public static Date parseDate(String strDate, String pattern) {
        Date date = null;
        try {
            if (pattern == null) {
                pattern = YYYYMMDD;
            }
            SimpleDateFormat format = new SimpleDateFormat(pattern);
            date = format.parse(strDate);
        } catch (Exception e) {
            logger.error("parseDate error:" + e);
        }
        return date;
    }

    /**
     * format date
     *
     * @param date
     * @return
     */
    public static String formatDate(Date date) {
        return formatDate(date, null);
    }

    /**
     * format date
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String formatDate(Date date, String pattern) {
        String strDate = null;
        try {
            if (pattern == null) {
                pattern = YYYYMMDD;
            }
            SimpleDateFormat format = new SimpleDateFormat(pattern);
            strDate = format.format(date);
        } catch (Exception e) {
            logger.error("formatDate error:", e);
        }
        return strDate;
    }

    /**
     * 取得日期：年
     *
     * @param date
     * @return
     */
    public static int getYear(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.YEAR);
    }

    /**
     * 取得日期：年
     *
     * @param date
     * @return
     */
    public static int getMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int month = c.get(Calendar.MONTH);
        return month + 1;
    }

    /**
     * 取得日期：年
     *
     * @param date
     * @return
     */
    public static int getDay(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 取得当天日期是周几
     *
     * @param date
     * @return
     */
    public static int getWeekDay(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int week_of_year = c.get(Calendar.DAY_OF_WEEK);
        return week_of_year - 1;
    }

    /**
     * 取得一年的第几周
     *
     * @param date
     * @return
     */
    public static int getWeekOfYear(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.WEEK_OF_YEAR);
    }

    /**
     * getWeekBeginAndEndDate
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String getWeekBeginAndEndDate(Date date, String pattern) {
        Date monday = getMondayOfWeek(date);
        Date sunday = getSundayOfWeek(date);
        return formatDate(monday, pattern) + " - "
                + formatDate(sunday, pattern);
    }

    /**
     * 根据日期取得对应周周一日期
     *
     * @param date
     * @return
     */
    public static Date getMondayOfWeek(Date date) {
        Calendar monday = Calendar.getInstance();
        monday.setTime(date);
        monday.setFirstDayOfWeek(FIRST_DAY_OF_WEEK);
        monday.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return monday.getTime();
    }

    /**
     * 根据日期取得对应周周日日期
     *
     * @param date
     * @return
     */
    public static Date getSundayOfWeek(Date date) {
        Calendar sunday = Calendar.getInstance();
        sunday.setTime(date);
        sunday.setFirstDayOfWeek(FIRST_DAY_OF_WEEK);
        sunday.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        return sunday.getTime();
    }

    /**
     * 取得月的剩余天数
     *
     * @param date
     * @return
     */
    public static int getRemainDayOfMonth(Date date) {
        int dayOfMonth = getDayOfMonth(date);
        int day = getPassDayOfMonth(date);
        return dayOfMonth - day;
    }

    /**
     * 取得月已经过的天数
     *
     * @param date
     * @return
     */
    public static int getPassDayOfMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 取得月天数
     *
     * @param date
     * @return
     */
    public static int getDayOfMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * 取得月第一天
     *
     * @param date
     * @return
     */
    public static Date getFirstDateOfMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
        return c.getTime();
    }

    /**
     * 取得月最后一天
     *
     * @param date
     * @return
     */
    public static Date getLastDateOfMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        return c.getTime();
    }

    /**
     * 取得季度第一天
     *
     * @param date
     * @return
     */
    public static Date getFirstDateOfSeason(Date date) {
        return getFirstDateOfMonth(getSeasonDate(date)[0]);
    }

    /**
     * 取得季度最后一天
     *
     * @param date
     * @return
     */
    public static Date getLastDateOfSeason(Date date) {
        return getLastDateOfMonth(getSeasonDate(date)[2]);
    }

    /**
     * 取得季度天数
     *
     * @param date
     * @return
     */
    public static int getDayOfSeason(Date date) {
        int day = 0;
        Date[] seasonDates = getSeasonDate(date);
        for (Date date2 : seasonDates) {
            day += getDayOfMonth(date2);
        }
        return day;
    }

    /**
     * 取得季度剩余天数
     *
     * @param date
     * @return
     */
    public static int getRemainDayOfSeason(Date date) {
        return getDayOfSeason(date) - getPassDayOfSeason(date);
    }

    /**
     * 取得季度已过天数
     *
     * @param date
     * @return
     */
    public static int getPassDayOfSeason(Date date) {
        int day = 0;

        Date[] seasonDates = getSeasonDate(date);

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int month = c.get(Calendar.MONTH);

        if (month == Calendar.JANUARY || month == Calendar.APRIL
                || month == Calendar.JULY || month == Calendar.OCTOBER) {// 季度第一个月
            day = getPassDayOfMonth(seasonDates[0]);
        } else if (month == Calendar.FEBRUARY || month == Calendar.MAY
                || month == Calendar.AUGUST || month == Calendar.NOVEMBER) {// 季度第二个月
            day = getDayOfMonth(seasonDates[0])
                    + getPassDayOfMonth(seasonDates[1]);
        } else if (month == Calendar.MARCH || month == Calendar.JUNE
                || month == Calendar.SEPTEMBER || month == Calendar.DECEMBER) {// 季度第三个月
            day = getDayOfMonth(seasonDates[0]) + getDayOfMonth(seasonDates[1])
                    + getPassDayOfMonth(seasonDates[2]);
        }
        return day;
    }

    /**
     * 取得季度月
     *
     * @param date
     * @return
     */
    public static Date[] getSeasonDate(Date date) {
        Date[] season = new Date[3];

        Calendar c = Calendar.getInstance();
        c.setTime(date);

        int nSeason = getSeason(date);
        if (nSeason == 1) {// 第一季度
            c.set(Calendar.MONTH, Calendar.JANUARY);
            season[0] = c.getTime();
            c.set(Calendar.MONTH, Calendar.FEBRUARY);
            season[1] = c.getTime();
            c.set(Calendar.MONTH, Calendar.MARCH);
            season[2] = c.getTime();
        } else if (nSeason == 2) {// 第二季度
            c.set(Calendar.MONTH, Calendar.APRIL);
            season[0] = c.getTime();
            c.set(Calendar.MONTH, Calendar.MAY);
            season[1] = c.getTime();
            c.set(Calendar.MONTH, Calendar.JUNE);
            season[2] = c.getTime();
        } else if (nSeason == 3) {// 第三季度
            c.set(Calendar.MONTH, Calendar.JULY);
            season[0] = c.getTime();
            c.set(Calendar.MONTH, Calendar.AUGUST);
            season[1] = c.getTime();
            c.set(Calendar.MONTH, Calendar.SEPTEMBER);
            season[2] = c.getTime();
        } else if (nSeason == 4) {// 第四季度
            c.set(Calendar.MONTH, Calendar.OCTOBER);
            season[0] = c.getTime();
            c.set(Calendar.MONTH, Calendar.NOVEMBER);
            season[1] = c.getTime();
            c.set(Calendar.MONTH, Calendar.DECEMBER);
            season[2] = c.getTime();
        }
        return season;
    }

    /**
     * 1 第一季度 2 第二季度 3 第三季度 4 第四季度
     *
     * @param date
     * @return
     */
    public static int getSeason(Date date) {

        int season = 0;

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int month = c.get(Calendar.MONTH);
        switch (month) {
            case Calendar.JANUARY:
            case Calendar.FEBRUARY:
            case Calendar.MARCH:
                season = 1;
                break;
            case Calendar.APRIL:
            case Calendar.MAY:
            case Calendar.JUNE:
                season = 2;
                break;
            case Calendar.JULY:
            case Calendar.AUGUST:
            case Calendar.SEPTEMBER:
                season = 3;
                break;
            case Calendar.OCTOBER:
            case Calendar.NOVEMBER:
            case Calendar.DECEMBER:
                season = 4;
                break;
            default:
                break;
        }
        return season;
    }

    public static Integer getDifMonth(Date startDate, Date endDate) {
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        start.setTime(startDate);
        end.setTime(endDate);
        int result = end.get(Calendar.MONTH) - start.get(Calendar.MONTH);
        int month = (end.get(Calendar.YEAR) - start.get(Calendar.YEAR)) * 12;
        return Math.abs(month + result);
    }

    public static String getyyyyMMdd(Date date) {
        if (date == null) {
            return "";
        }
        return DATE_FORMAT.format(date).toString();
    }

    public static void main(String[] args) {
        String strDate = "2021-06-29";

        Date date = parseDate(strDate);

        System.out.println(strDate + " 今天是哪一年？" + getYear(date));
        System.out.println(strDate + " 今天是哪个月？" + getMonth(date));
        System.out.println(strDate + " 今天是几号？" + getDay(date));
        System.out.println(strDate + " 今天是周几？" + getWeekDay(date));
        System.out.println(strDate + " 是一年的第几周？" + getWeekOfYear(date));
        System.out.println(strDate + " 所在周起始结束日期？"
                + getWeekBeginAndEndDate(date, "yyyy年MM月dd日"));
        System.out.println(strDate + " 所在周周一是？"
                + formatDate(getMondayOfWeek(date)));
        System.out.println(strDate + " 所在周周日是？"
                + formatDate(getSundayOfWeek(date)));

        System.out.println(strDate + " 当月第一天日期？"
                + formatDate(getFirstDateOfMonth(date)));
        System.out.println(strDate + " 当月最后一天日期？"
                + formatDate(getLastDateOfMonth(date)));
        System.out.println(strDate + " 当月天数？" + getDayOfMonth(date));
        System.out.println(strDate + " 当月已过多少天？" + getPassDayOfMonth(date));
        System.out.println(strDate + " 当月剩余多少天？" + getRemainDayOfMonth(date));

        System.out.println(strDate + " 所在季度第一天日期？"
                + formatDate(getFirstDateOfSeason(date)));
        System.out.println(strDate + " 所在季度最后一天日期？"
                + formatDate(getLastDateOfSeason(date)));
        System.out.println(strDate + " 所在季度天数？" + getDayOfSeason(date));
        System.out.println(strDate + " 所在季度已过多少天？" + getPassDayOfSeason(date));
        System.out.println(strDate + " 所在季度剩余多少天？" + getRemainDayOfSeason(date));
        System.out.println(strDate + " 是第几季度？" + getSeason(date));
        System.out.println(strDate + " 所在季度月份？"
                + formatDate(getSeasonDate(date)[0], "yyyy年MM月") + "/"
                + formatDate(getSeasonDate(date)[1], "yyyy年MM月") + "/"
                + formatDate(getSeasonDate(date)[2], "yyyy年MM月"));

        System.out.println(formatDate(date, "yyyy-MM"));
    }

}
