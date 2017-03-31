package com.xinma.base.util;

import java.text.ParseException;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Assert;
import org.junit.Test;

public class DateHelperTest {

	@Test
	public void getAvailableTimeZoneIDsTest() {
		String[] timeZoneIds = TimeZone.getAvailableIDs();
		for (String timeZoneId : timeZoneIds) {
			System.out.println(timeZoneId);
		}
	}

	@Test
	public void formatCurrentTimeTest() {
		System.out.println(DateUtil.currentTime().toString());
	}

	@Test
	public void getDefaultTimeZoneTest() {
		TimeZone defaultTimeZone = TimeZone.getDefault();
		Assert.assertEquals(defaultTimeZone.getID(), "Asia/Shanghai");
		System.out.println(defaultTimeZone.getID());
	}

	@Test
	public void convertDateToStringTest() {
		TimeZone shanghaiTimeZone = TimeZone.getTimeZone("Asia/Shanghai");
		Date now = DateUtil.currentTime();
		String defaultFormatString = DateUtil.formatDate(now);
		String shanghaiTimeZoneFormatString = DateUtil.formatDate(now, DateUtil.DEFAULT_PATTERN, shanghaiTimeZone);
		System.out.println(defaultFormatString);
		System.out.println(shanghaiTimeZoneFormatString);
		Assert.assertEquals(defaultFormatString, shanghaiTimeZoneFormatString);

		TimeZone utcTimeZone = TimeZone.getTimeZone("UTC");
		String utcTimeZoneFormatString = DateUtil.formatDate(now, DateUtil.DEFAULT_PATTERN, utcTimeZone);
		System.out.println(utcTimeZoneFormatString);
		Assert.assertNotEquals(defaultFormatString, utcTimeZoneFormatString);
	}

	@Test
	public void convertStringToDateTest() throws ParseException {
		String formatTimeString = "2015-10-09 13:51:57";
		Date defaultDate = DateUtil.parseDate(formatTimeString);
		Date defaultPatternDate = DateUtil.parseDate(formatTimeString, DateUtil.DEFAULT_PATTERN);
		Assert.assertTrue(defaultDate.equals(defaultPatternDate));

		TimeZone timeZone = TimeZone.getTimeZone("UTC");
		Date utcDate = DateUtil.parseDate(formatTimeString, DateUtil.DEFAULT_PATTERN, timeZone);
		Assert.assertEquals(utcDate, DateUtils.addHours(defaultDate, 8));
	}
}
