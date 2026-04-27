package com.leadnews.utils.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
    public static String DATE_FORMAT = "yyyy-MM-dd";

    public static String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final String DATE_TIME_STAMP_FORMATE = "yyyyMMddHHmmss";

    //2019骞?7鏈?9鏃?
    public static String DATE_FORMAT_CHINESE = "yyyy骞碝鏈坉鏃?;
    //2019骞?7鏈?9鏃?14:00:32
    public static String DATE_TIME_FORMAT_CHINESE = "yyyy骞碝鏈坉鏃?HH:mm:ss";


    /**
     * 鑾峰彇褰撳墠鏃ユ湡
     *
     * @return
     */
    public static String getCurrentDate() {
        String datestr = null;
        SimpleDateFormat df = new SimpleDateFormat(DateUtils.DATE_FORMAT);
        datestr = df.format(new Date());
        return datestr;
    }

    /**
     * 鑾峰彇褰撳墠鏃ユ湡鏃堕棿
     *
     * @return
     */
    public static String getCurrentDateTime() {
        String datestr = null;
        SimpleDateFormat df = new SimpleDateFormat(DateUtils.DATE_TIME_FORMAT);
        datestr = df.format(new Date());
        return datestr;
    }

    /**
     * 鑾峰彇褰撳墠鏃ユ湡鏃堕棿
     *
     * @return
     */
    public static String getCurrentDateTime(String Dateformat) {
        String datestr = null;
        SimpleDateFormat df = new SimpleDateFormat(Dateformat);
        datestr = df.format(new Date());
        return datestr;
    }

    public static String dateToDateTime(Date date) {
        String datestr = null;
        SimpleDateFormat df = new SimpleDateFormat(DateUtils.DATE_TIME_FORMAT);
        datestr = df.format(date);
        return datestr;
    }

    /**
     * 灏嗗瓧绗︿覆鏃ユ湡杞崲涓烘棩鏈熸牸寮?
     *
     * @param datestr
     * @return
     */
    public static Date stringToDate(String datestr) {

        if (datestr == null || datestr.equals("")) {
            return null;
        }
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat(DateUtils.DATE_FORMAT);
        try {
            date = df.parse(datestr);
        } catch (ParseException e) {
            date = DateUtils.stringToDate(datestr, "yyyyMMdd");
        }
        return date;
    }

    /**
     * 灏嗗瓧绗︿覆鏃ユ湡杞崲涓烘棩鏈熸牸寮?
     * 鑷畾缇╂牸寮?
     *
     * @param datestr
     * @return
     */
    public static Date stringToDate(String datestr, String dateformat) {
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat(dateformat);
        try {
            date = df.parse(datestr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }


    /**
     * 灏嗘棩鏈熸牸寮忔棩鏈熻浆鎹负瀛楃涓叉牸寮?
     *
     * @param date
     * @return
     */
    public static String dateToString(Date date) {
        String datestr = null;
        SimpleDateFormat df = new SimpleDateFormat(DateUtils.DATE_FORMAT);
        datestr = df.format(date);
        return datestr;
    }

    /**
     * 灏嗘棩鏈熸牸寮忔棩鏈熻浆鎹负瀛楃涓叉牸寮?鑷畾缇╂牸寮?
     *
     * @param date
     * @param dateformat
     * @return
     */
    public static String dateToString(Date date, String dateformat) {
        String datestr = null;
        SimpleDateFormat df = new SimpleDateFormat(dateformat);
        datestr = df.format(date);
        return datestr;
    }

    /**
     * 鑾峰彇鏃ユ湡鐨凞AY鍊?
     *
     * @param date 杈撳叆鏃ユ湡
     * @return
     */
    public static int getDayOfDate(Date date) {
        int d = 0;
        Calendar cd = Calendar.getInstance();
        cd.setTime(date);
        d = cd.get(Calendar.DAY_OF_MONTH);
        return d;
    }

    /**
     * 鑾峰彇鏃ユ湡鐨凪ONTH鍊?
     *
     * @param date 杈撳叆鏃ユ湡
     * @return
     */
    public static int getMonthOfDate(Date date) {
        int m = 0;
        Calendar cd = Calendar.getInstance();
        cd.setTime(date);
        m = cd.get(Calendar.MONTH) + 1;
        return m;
    }

    /**
     * 鑾峰彇鏃ユ湡鐨刌EAR鍊?
     *
     * @param date 杈撳叆鏃ユ湡
     * @return
     */
    public static int getYearOfDate(Date date) {
        int y = 0;
        Calendar cd = Calendar.getInstance();
        cd.setTime(date);
        y = cd.get(Calendar.YEAR);
        return y;
    }

    /**
     * 鑾峰彇鏄熸湡鍑?
     *
     * @param date 杈撳叆鏃ユ湡
     * @return
     */
    public static int getWeekOfDate(Date date) {
        int wd = 0;
        Calendar cd = Calendar.getInstance();
        cd.setTime(date);
        wd = cd.get(Calendar.DAY_OF_WEEK) - 1;
        return wd;
    }

    /**
     * 鑾峰彇杈撳叆鏃ユ湡鐨勫綋鏈堢涓€澶?
     *
     * @param date 杈撳叆鏃ユ湡
     * @return
     */
    public static Date getFirstDayOfMonth(Date date) {
        Calendar cd = Calendar.getInstance();
        cd.setTime(date);
        cd.set(Calendar.DAY_OF_MONTH, 1);
        return cd.getTime();
    }

    /**
     * 鑾峰緱杈撳叆鏃ユ湡鐨勫綋鏈堟渶鍚庝竴澶?
     *
     * @param date
     */
    public static Date getLastDayOfMonth(Date date) {
        return DateUtils.addDay(DateUtils.getFirstDayOfMonth(DateUtils.addMonth(date, 1)), -1);
    }

    /**
     * 鍒ゆ柇鏄惁鏄棸骞?
     *
     * @param date 杈撳叆鏃ユ湡
     * @return 鏄痶rue 鍚alse
     */
    public static boolean isLeapYEAR(Date date) {

        Calendar cd = Calendar.getInstance();
        cd.setTime(date);
        int year = cd.get(Calendar.YEAR);

        if (year % 4 == 0 && year % 100 != 0 | year % 400 == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 鏍规嵁鏁村瀷鏁拌〃绀虹殑骞存湀鏃ワ紝鐢熸垚鏃ユ湡绫诲瀷鏍煎紡
     *
     * @param year  骞?
     * @param month 鏈?
     * @param day   鏃?
     * @return
     */
    public static Date getDateByYMD(int year, int month, int day) {
        Calendar cd = Calendar.getInstance();
        cd.set(year, month - 1, day);
        return cd.getTime();
    }

    /**
     * 鑾峰彇骞村懆鏈熷搴旀棩
     *
     * @param date  杈撳叆鏃ユ湡
     * @param iyear 骞存暟  璨犳暩琛ㄧず涔嬪墠
     * @return
     */
    public static Date getYearCycleOfDate(Date date, int iyear) {
        Calendar cd = Calendar.getInstance();
        cd.setTime(date);

        cd.add(Calendar.YEAR, iyear);

        return cd.getTime();
    }

    /**
     * 鑾峰彇鏈堝懆鏈熷搴旀棩
     *
     * @param date 杈撳叆鏃ユ湡
     * @param i
     * @return
     */
    public static Date getMonthCycleOfDate(Date date, int i) {
        Calendar cd = Calendar.getInstance();
        cd.setTime(date);

        cd.add(Calendar.MONTH, i);

        return cd.getTime();
    }

    /**
     * 璁＄畻 fromDate 鍒?toDate 鐩稿樊澶氬皯骞?
     *
     * @param fromDate
     * @param toDate
     * @return 骞存暟
     */
    public static int getYearByMinusDate(Date fromDate, Date toDate) {
        Calendar df = Calendar.getInstance();
        df.setTime(fromDate);

        Calendar dt = Calendar.getInstance();
        dt.setTime(toDate);

        return dt.get(Calendar.YEAR) - df.get(Calendar.YEAR);
    }

    /**
     * 璁＄畻 fromDate 鍒?toDate 鐩稿樊澶氬皯涓湀
     *
     * @param fromDate
     * @param toDate
     * @return 鏈堟暟
     */
    public static int getMonthByMinusDate(Date fromDate, Date toDate) {
        Calendar df = Calendar.getInstance();
        df.setTime(fromDate);

        Calendar dt = Calendar.getInstance();
        dt.setTime(toDate);

        return dt.get(Calendar.YEAR) * 12 + dt.get(Calendar.MONTH) -
                (df.get(Calendar.YEAR) * 12 + df.get(Calendar.MONTH));
    }

    /**
     * 璁＄畻 fromDate 鍒?toDate 鐩稿樊澶氬皯澶?
     *
     * @param fromDate
     * @param toDate
     * @return 澶╂暟
     */
    public static long getDayByMinusDate(Object fromDate, Object toDate) {

        Date f = DateUtils.chgObject(fromDate);

        Date t = DateUtils.chgObject(toDate);

        long fd = f.getTime();
        long td = t.getTime();

        return (td - fd) / (24L * 60L * 60L * 1000L);
    }

    /**
     * 璁＄畻骞撮緞
     *
     * @param birthday 鐢熸棩鏃ユ湡
     * @param calcDate 瑕佽绠楃殑鏃ユ湡鐐?
     * @return
     */
    public static int calcAge(Date birthday, Date calcDate) {

        int cYear = DateUtils.getYearOfDate(calcDate);
        int cMonth = DateUtils.getMonthOfDate(calcDate);
        int cDay = DateUtils.getDayOfDate(calcDate);
        int bYear = DateUtils.getYearOfDate(birthday);
        int bMonth = DateUtils.getMonthOfDate(birthday);
        int bDay = DateUtils.getDayOfDate(birthday);

        if (cMonth > bMonth || (cMonth == bMonth && cDay > bDay)) {
            return cYear - bYear;
        } else {
            return cYear - 1 - bYear;
        }
    }

    /**
     * 浠庤韩浠借瘉涓幏鍙栧嚭鐢熸棩鏈?
     *
     * @param idno 韬唤璇佸彿鐮?
     * @return
     */
    public static String getBirthDayFromIDCard(String idno) {
        Calendar cd = Calendar.getInstance();
        if (idno.length() == 15) {
            cd.set(Calendar.YEAR, Integer.valueOf("19" + idno.substring(6, 8))
                    .intValue());
            cd.set(Calendar.MONTH, Integer.valueOf(idno.substring(8, 10))
                    .intValue() - 1);
            cd.set(Calendar.DAY_OF_MONTH,
                    Integer.valueOf(idno.substring(10, 12)).intValue());
        } else if (idno.length() == 18) {
            cd.set(Calendar.YEAR, Integer.valueOf(idno.substring(6, 10))
                    .intValue());
            cd.set(Calendar.MONTH, Integer.valueOf(idno.substring(10, 12))
                    .intValue() - 1);
            cd.set(Calendar.DAY_OF_MONTH,
                    Integer.valueOf(idno.substring(12, 14)).intValue());
        }
        return DateUtils.dateToString(cd.getTime());
    }

    /**
     * 鍦ㄨ緭鍏ユ棩鏈熶笂澧炲姞锛?锛夋垨鍑忓幓锛?锛夊ぉ鏁?
     *
     * @param date   杈撳叆鏃ユ湡
     * @param iday 瑕佸鍔犳垨鍑忓皯鐨勫ぉ鏁?
     */
    public static Date addDay(Date date, int iday) {
        Calendar cd = Calendar.getInstance();

        cd.setTime(date);

        cd.add(Calendar.DAY_OF_MONTH, iday);

        return cd.getTime();
    }

    /**
     * 鍦ㄨ緭鍏ユ棩鏈熶笂澧炲姞锛?锛夋垨鍑忓幓锛?锛夋湀浠?
     *
     * @param date   杈撳叆鏃ユ湡
     * @param imonth 瑕佸鍔犳垨鍑忓皯鐨勬湀鍒嗘暟
     */
    public static Date addMonth(Date date, int imonth) {
        Calendar cd = Calendar.getInstance();

        cd.setTime(date);

        cd.add(Calendar.MONTH, imonth);

        return cd.getTime();
    }

    /**
     * 鍦ㄨ緭鍏ユ棩鏈熶笂澧炲姞锛?锛夋垨鍑忓幓锛?锛夊勾浠?
     *
     * @param date   杈撳叆鏃ユ湡
     * @param iyear 瑕佸鍔犳垨鍑忓皯鐨勫勾鏁?
     */
    public static Date addYear(Date date, int iyear) {
        Calendar cd = Calendar.getInstance();

        cd.setTime(date);

        cd.add(Calendar.YEAR, iyear);

        return cd.getTime();
    }

    /**
     * 灏嘜BJECT椤炲瀷杞夋彌鐐篋ate
     *
     * @param date
     * @return
     */
    public static Date chgObject(Object date) {

        if (date != null && date instanceof Date) {
            return (Date) date;
        }

        if (date != null && date instanceof String) {
            return DateUtils.stringToDate((String) date);
        }

        return null;

    }

    public static long getAgeByBirthday(String date) {

        Date birthday = stringToDate(date, "yyyy-MM-dd");
        long sec = new Date().getTime() - birthday.getTime();

        long age = sec / (1000 * 60 * 60 * 24) / 365;

        return age;
    }


    /**
     * @param args
     */
    public static void main(String[] args) {
        //String temp = DateUtil.dateToString(getLastDayOfMonth(new Date()),
        ///   DateUtil.DATE_FORMAT_CHINESE);
        //String s=DateUtil.dateToString(DateUtil.addDay(DateUtil.addYear(new Date(),1),-1));


        long s = DateUtils.getDayByMinusDate("2012-01-01", "2012-12-31");
        System.err.println(s);


    }
}

