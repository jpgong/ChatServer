package com.chatTest.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ʱ��ʱ����
 * @author jpgong
 *
 */
public class DateUtil {
	
	/**
	 * ������������ַ���
	 * @return
	 */
	public static String getDateStr() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date());
	}
	
	// ����ʱ��ģʽ
	public static String getDateFormat(Timestamp timestamp) {
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = sDateFormat.format(timestamp);
		return date;
	}

}
