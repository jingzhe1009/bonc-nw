package com.ljz.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.ljz.constant.BoncConstant;

public class TimeUtil {

	public static java.sql.Date getTomorrow() {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, 1);
        return new java.sql.Date(calendar.getTime().getTime());
    }

	public static Date getTw() {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, 1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String format = sdf.format(calendar.getTime());
        return StringtoDate(format);
    }

	public static java.sql.Date getToday(){
		return new java.sql.Date(new Date().getTime());
	}

	public static String getDate(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(date);
	}
	
	public static String getTime(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}

	public static String getDateToString(Date date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		return sdf.format(date);
	}

	public static void main(String[] args) {
		String s = getDateToString(getToday());
		System.out.println(s);
	}

	public static Date getTy(){
		return new Date();
	}
	
	public static java.sql.Date getEdate() {
		return ExcelUtil.getInstance().StringToDate(BoncConstant.CON_E_DATE);
	}


	public static Date getE(){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date date = simpleDateFormat.parse(BoncConstant.CON_E_DATE);
			return date;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new Date();
	}

	public static Date StringtoDate(String str){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date date = simpleDateFormat.parse(str);
			return date;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new Date();
	}

}
