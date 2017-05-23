package com.iconectiv.irsf.util;

import org.apache.commons.lang.time.DateUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Months;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTimeHelper {
	static DateTimeZone tz = DateTimeZone.getDefault();
	public static Date nowInUTC() {
		return DateTime.now().withZone(DateTimeZone.UTC).toDate();
	}
	
	public static Date toUTC(Date date) {
		return new Date(tz.convertLocalToUTC(date.getTime(), false));
	}
	
	public static DateTime getTodayAtMidnight() {
		return getDateAtMidnight(1);
	}

	public static DateTime getDateBefore(int days) {
		DateTime myDay = new DateTime().withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0);
		myDay = myDay.minusDays(days);
		return myDay;
	}

	public static DateTime getDateAtMidnight(int days) {
		DateTime myDay = new DateTime().withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0);
		myDay = myDay.plusDays(days);
		return myDay;
	}

	public static Date getDateAtMidnightInMonth(int months) {
		DateTime myDay = new DateTime().withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0);
		myDay = myDay.plusDays(1);
		myDay = myDay.plusMonths(months);
		return myDay.toDate();
	}

	public static Date getDateAtMidnightInMonth(Date startDate, int months) {
		DateTime myDay = new DateTime(startDate.getTime()).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0);
		//myDay = myDay.plusDays(0);
		myDay = myDay.plusMonths(months);
		return myDay.toDate();
	}
	
	
	public static String formatDate(Date myDate, SimpleDateFormat format) {
		if (myDate == null) {
			return "";
		}
		return format.format(myDate);
	}

	public static String formatDate(Date myDate, String formatStr) {
		SimpleDateFormat format = new SimpleDateFormat(formatStr);
		if (myDate == null) {
			return "";
		}
		return format.format(myDate);
	}

	public static Date formatDate(String myDateStr, String formatStr) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat(formatStr);
		if (myDateStr == null || "".equals(myDateStr)) {
			return null;
		}
		return format.parse(myDateStr);
	}

	public static DateTime getDateBefore(Date expirationdate, int days) {
		DateTime myDay = new DateTime(expirationdate);
		myDay = myDay.minusDays(days);
		return myDay;
	}


	public static int getDaysBetween(Date startDate, Date endDate) {
		DateTime startDateTime = new DateTime(startDate.getTime());
		DateTime endDateTime = new DateTime(endDate.getTime());
		
		return Days.daysBetween(startDateTime, endDateTime).getDays();
	}
	
	public static int getMonthsBetween(Date startDate, Date endDate) {
		DateTime startDateTime = new DateTime(startDate.getTime());
		DateTime endDateTime = new DateTime(endDate.getTime());
		
		return Months.monthsBetween(startDateTime, endDateTime).getMonths();
	}
	
	public static int getMonthsBetween(DateTime startDateTime, DateTime endDateTime) {
		
		return Months.monthsBetween(startDateTime, endDateTime).getMonths();
	}
	
	public static DateTime getDateTillEndOfMonth(Date startDate, int days) {
		Calendar baseDay = Calendar.getInstance();
		baseDay.setTime(startDate);
		baseDay.set(Calendar.DAY_OF_MONTH, baseDay.getActualMaximum(Calendar.DAY_OF_MONTH)- days +1);
		DateTime myDay = new DateTime(baseDay.getTime()).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0);
		
		return myDay;
	}
	
	
/*	public static int getDaysTillEndOfMonth() {
		Calendar baseDay = Calendar.getInstance();
		
		return baseDay.getActualMaximum(Calendar.DAY_OF_MONTH) - baseDay.get(Calendar.DAY_OF_MONTH);
	}
	
*/	
	public static int getDaysTillStartOfNextMonth() {
		Calendar baseDay = Calendar.getInstance();
		
		return baseDay.getActualMaximum(Calendar.DAY_OF_MONTH) - baseDay.get(Calendar.DAY_OF_MONTH) + 1;
	}
	
	
	public static int getDaysTillStartOfNextMonth(Date baseDate) {
		Calendar baseDay = Calendar.getInstance();
		baseDay.setTime(baseDate);
		
		return baseDay.getActualMaximum(Calendar.DAY_OF_MONTH) - baseDay.get(Calendar.DAY_OF_MONTH) + 1;
	}
	
	
	public static DateTime getLastDayOfMonth(Date myDate) {
		Calendar baseDay = Calendar.getInstance();
		baseDay.setTime(myDate);
		baseDay.set(Calendar.DAY_OF_MONTH, baseDay.getActualMaximum(Calendar.DAY_OF_MONTH));
		DateTime myDay = new DateTime(baseDay.getTime()).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0);
		
		return myDay;
	}


	public static DateTime getFirstDayOfMonth(Date myDate) {
		Calendar baseDay = Calendar.getInstance();
		baseDay.setTime(myDate);
		baseDay.set(Calendar.DAY_OF_MONTH, 1);
		DateTime myDay = new DateTime(baseDay.getTime()).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0);
		
		return myDay;
	}


	public static DateTime getLastDayOfCurrentMonth() {
		Calendar baseDay = Calendar.getInstance();
		baseDay.set(Calendar.DAY_OF_MONTH, baseDay.getActualMaximum(Calendar.DAY_OF_MONTH));
		DateTime myDay = new DateTime(baseDay.getTime()).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0);
		
		return myDay;
	}

	public static DateTime getLastDayOfNextMonth() {
		Date nextMonth = DateUtils.addMonths(new Date(), 1);
		Calendar baseDay = Calendar.getInstance();
		baseDay.setTime(nextMonth);
		baseDay.set(Calendar.DAY_OF_MONTH, baseDay.getActualMaximum(Calendar.DAY_OF_MONTH));
		DateTime myDay = new DateTime(baseDay.getTime()).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0);
		
		return myDay;
	}

	public static DateTime getLastDayOfPreviousMonth(Date myDate) {
		Date previousMonth = DateUtils.addMonths(myDate, -1);
		Calendar baseDay = Calendar.getInstance();
		baseDay.setTime(previousMonth);
		baseDay.set(Calendar.DAY_OF_MONTH, baseDay.getActualMaximum(Calendar.DAY_OF_MONTH));
		DateTime myDay = new DateTime(baseDay.getTime()).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0);
		
		return myDay;
	}


	public static DateTime getFirstDayOfNextMonth() {
		return getFirstDayOfMonth(1);
	}

	public static DateTime getFirstDayOfMonth(int n) {
		Date nextMonth = DateUtils.addMonths(new Date(), n);
		Calendar baseDay = Calendar.getInstance();
		baseDay.setTime(nextMonth);
		baseDay.set(Calendar.DAY_OF_MONTH, 1);
		DateTime myDay = new DateTime(baseDay.getTime()).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0);
		
		return myDay;
	}

	public static int getDayOfMonth(Date date) {
		Calendar cal = Calendar.getInstance();
	    cal.setTime(date);
	    //int year = cal.get(Calendar.YEAR);
	    //int month = cal.get(Calendar.MONTH);
	    int day = cal.get(Calendar.DAY_OF_MONTH);	
	    
	    return day;
	}


	public static DateTime getDayInMonth(int dayOfMonth, int months) {
		Date inMonths = DateUtils.addMonths(new Date(), months);
		Calendar baseDay = Calendar.getInstance();
		baseDay.setTime(inMonths);
		
		int maxDay = baseDay.getActualMaximum(Calendar.DAY_OF_MONTH);
		
		if (dayOfMonth >= maxDay) {
			baseDay.set(Calendar.DAY_OF_MONTH, maxDay);
		} else {		
			baseDay.set(Calendar.DAY_OF_MONTH, dayOfMonth);
		}
		return new DateTime(baseDay.getTime()).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0);
	}
	
	public static int getMonthInBetween(DateTime startDate, DateTime endDate) {
		return Months.monthsBetween(startDate.withDayOfMonth(1), endDate.withDayOfMonth(28)).getMonths();
	}

	public static int getMonthInBetween(Date startDate, Date endDate) {
		return getMonthInBetween( new DateTime(startDate.getTime()), new DateTime(endDate.getTime()));
	}
	
	public static Date getDateNDaysBeforeStartOfNextMonth(int days)
	{

		Calendar baseDay = Calendar.getInstance();
		baseDay.set(Calendar.DAY_OF_MONTH, baseDay.getActualMaximum(Calendar.DAY_OF_MONTH));
		DateTime myDay = new DateTime(baseDay.getTime()).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0);

		myDay = myDay.minusDays(days - 1);
		
		return myDay.toDate();

	}

	public static Date getDate(String month, String year) throws Exception {
		int iYear = Integer.parseInt(year);
		int iMonth = Integer.parseInt(month);

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, iYear);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		
		if(iMonth == 0)
			cal.set(Calendar.MONTH, Calendar.JANUARY);
		if(iMonth == 1)
			cal.set(Calendar.MONTH, Calendar.FEBRUARY);
		if(iMonth == 2)
			cal.set(Calendar.MONTH, Calendar.MARCH);
		if(iMonth == 3)
			cal.set(Calendar.MONTH, Calendar.APRIL);
		if(iMonth == 4)
			cal.set(Calendar.MONTH, Calendar.MAY);
		if(iMonth == 5)
			cal.set(Calendar.MONTH, Calendar.JUNE);
		if(iMonth == 6)
			cal.set(Calendar.MONTH, Calendar.JULY);
		if(iMonth == 7)
			cal.set(Calendar.MONTH, Calendar.AUGUST);
		if(iMonth == 8)
			cal.set(Calendar.MONTH, Calendar.SEPTEMBER);
		if(iMonth == 9)
			cal.set(Calendar.MONTH, Calendar.OCTOBER);
		if(iMonth == 10)
			cal.set(Calendar.MONTH, Calendar.NOVEMBER);
		if(iMonth == 11)
			cal.set(Calendar.MONTH, Calendar.DECEMBER);

		return cal.getTime();
	}
}
