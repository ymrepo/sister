package com.mei.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtils {
	
	public static Date getSystemDate(){
		return new Date(System.currentTimeMillis());
	}
	
	public static String getSystemDateString(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault());
		return sdf.format(new Date(System.currentTimeMillis()));
	}
	
	public static String getSystemTimeString(){
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm",Locale.getDefault());
		return sdf.format(new Date(System.currentTimeMillis()));
	}
	
	public static String getDateString(long date){
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd",Locale.getDefault());
		return sdf.format(new Date(date));
	}
	
	public static String formatDate(String date){
		Date mDate;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
		try {
			mDate = sdf.parse(date);
			Calendar cal = Calendar.getInstance();
			cal.setTime(mDate);
			cal.add(Calendar.DAY_OF_YEAR, -1);
			mDate = cal.getTime();
			return sdf.format(mDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public static String getTimeString(long time){
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss",Locale.getDefault());
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		return sdf.format(new Date(time));
	}
}
