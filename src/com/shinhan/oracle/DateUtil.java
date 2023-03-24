package com.shinhan.oracle;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateUtil {

	public static Date convertToDate(String hireDateStr) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		Date sqlDate = null;
		try {
			java.util.Date date = sdf.parse(hireDateStr);
			sqlDate = new Date(date.getTime());

		} catch (ParseException e) {
			e.printStackTrace();
		}

		return sqlDate;
	}

}
