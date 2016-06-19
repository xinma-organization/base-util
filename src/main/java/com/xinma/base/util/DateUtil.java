package com.xinma.base.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Date工具类
 * 
 * @author Alauda
 *
 * @date 2016年6月19日
 *
 */
public class DateUtil {

	public static final String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";

	private static final TimeZone defaultTimeZone = TimeZone.getDefault();

	/**
	 * 获取当前时间
	 * 
	 * @return new Date()
	 */
	public static Date currentTime() {
		return new Date();
	}

	/**
	 * 格式化当前系统时间
	 * 
	 * @return 当前时间默认格式化字符串
	 */
	public static String currentTimeFormat() {
		return formatDate(currentTime(), DEFAULT_PATTERN);
	}

	/**
	 * 格式化Date对象
	 * 
	 * @param date
	 *            date对象
	 * @return 格式化后的时间字符串
	 */
	public static String formatDate(Date date) {
		return formatDate(date, DEFAULT_PATTERN);
	}

	/**
	 * 格式化Date对象
	 * 
	 * @param date
	 *            date对象
	 * @param pattern
	 *            格式化字符串
	 * @return 格式化后的时间字符串
	 */
	public static String formatDate(Date date, String pattern) {
		return formatDate(date, pattern, defaultTimeZone);
	}

	/**
	 * 格式化Date对象
	 * 
	 * @param date
	 *            date对象
	 * @param pattern
	 *            格式化字符串
	 * @param timeZone
	 *            时间区域
	 * @return 格式化后的时间字符串
	 */
	public static String formatDate(Date date, String pattern, TimeZone timeZone) {
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		format.setTimeZone(timeZone);
		return format.format(date);
	}

	/**
	 * 将时间格式化字符串解析成Date对象
	 * 
	 * @param dateStr
	 *            时间格式化字符串
	 * @return Date对象
	 * @throws ParseException
	 */
	public static Date parseDate(String dateStr) throws ParseException {
		return parseDate(dateStr, DEFAULT_PATTERN);
	}

	/**
	 * 将时间格式化字符串解析成Date对象
	 * 
	 * @param dateStr
	 *            时间格式化字符串
	 * @param pattern
	 *            格式化字符串
	 * @return Date对象
	 * @throws ParseException
	 */
	public static Date parseDate(String dateStr, String pattern) throws ParseException {
		return parseDate(dateStr, pattern, defaultTimeZone);
	}

	/**
	 * 将时间格式化字符串解析成Date对象
	 * 
	 * @param dateStr
	 *            时间格式化字符串
	 * @param pattern
	 *            格式化字符串
	 * @param timeZone
	 *            时间区域
	 * @return Date对象
	 * @throws ParseException
	 */
	public static Date parseDate(String dateStr, String pattern, TimeZone timeZone) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		format.setTimeZone(timeZone);
		return format.parse(dateStr);
	}

}
