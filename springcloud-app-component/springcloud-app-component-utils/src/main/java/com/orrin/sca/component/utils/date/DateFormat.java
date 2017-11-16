package com.orrin.sca.component.utils.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author orrin.zhang on 2017/8/3.
 */
public class DateFormat {
	private static ThreadLocal<Map<String, SimpleDateFormat>> threadLocal = new ThreadLocal<>();

	private static SimpleDateFormat getSimpleDateFormat(DatePattern datePattern){
		if(threadLocal.get() == null){
			Map<String, SimpleDateFormat> map = new HashMap<>();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datePattern.getValue());
			map.put(datePattern.getValue(),simpleDateFormat);
			threadLocal.set(map);
		}else if(threadLocal.get()!= null && threadLocal.get().get(datePattern.getValue()) == null) {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datePattern.getValue());
			threadLocal.get().put(datePattern.getValue(),simpleDateFormat);
		}

		return threadLocal.get().get(datePattern.getValue());
	}

	private static SimpleDateFormat getSimpleDateFormat(String datePattern){
		if(threadLocal.get() == null){
			Map<String, SimpleDateFormat> map = new HashMap<>();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datePattern);
			map.put(datePattern,simpleDateFormat);
			threadLocal.set(map);
		}else if(threadLocal.get()!= null && threadLocal.get().get(datePattern) == null) {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datePattern);
			threadLocal.get().put(datePattern,simpleDateFormat);
		}

		return threadLocal.get().get(datePattern);
	}

	public static String defaultFormat(Instant instant){
		LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
		String format = localDateTime.format(DateTimeFormatter.ofPattern(DatePattern.YYYY_MM_DD_HH_MM_SS.getValue()));
		return format;
	}

	public static String defaultFormat(){
		LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault());
		String format = localDateTime.format(DateTimeFormatter.ofPattern(DatePattern.YYYY_MM_DD_HH_MM_SS.getValue()));
		return format;
	}

	public static String defaultPatternFormat(DatePattern datePattern){
		LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault());
		String format = localDateTime.format(DateTimeFormatter.ofPattern(datePattern.getValue()));
		return format;
	}

	public static String patternFormat(Date date, DatePattern datePattern){
		SimpleDateFormat simpleDateFormat = getSimpleDateFormat(datePattern);
		return simpleDateFormat.format(date);
	}

	public static Date defaultPatternFormat(String dataStr){
		SimpleDateFormat simpleDateFormat = getSimpleDateFormat(DatePattern.YYYY_MM_DD_HH_MM_SS);
		try {
			return simpleDateFormat.parse(dataStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Date patternFormat(String dataStr, DatePattern datePattern){
		SimpleDateFormat simpleDateFormat = getSimpleDateFormat(datePattern);
		try {
			return simpleDateFormat.parse(dataStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Date patternFormat(String dataStr, String datePattern){
		SimpleDateFormat simpleDateFormat = getSimpleDateFormat(datePattern);
		try {
			return simpleDateFormat.parse(dataStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
}
