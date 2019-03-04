package com.chatTest.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时期时间类
 * @author jpgong
 *
 */
public class DateUtil {
	
	/**
	 * 返回日期类的字符串
	 * @return
	 */
	public static String getDateStr() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date());
	}
	
	// 更改时间模式
	public static String getDateFormat(Timestamp timestamp) {
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = sDateFormat.format(timestamp);
		return date;
	}

}
