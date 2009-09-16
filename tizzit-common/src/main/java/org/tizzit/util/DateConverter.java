/**
 * Copyright (c) 2009 Juwi MacMillan Group GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.tizzit.util;

import java.sql.Date;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * 
 * @author <a href="sascha.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id$
 */
public final class DateConverter {
	private DateConverter() {
	}

	public static java.util.Date getString2Sql(String date) {
		if (date == null || date.equals("")) return null;

		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		java.util.Date d = sdf.parse(date, new ParsePosition(0));
		return new java.util.Date(d.getTime());
	}

	public static java.util.Calendar getString2Calendar(String dateString) {
		if (dateString == null || dateString.equals("")) return null;

		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		java.util.Date date = sdf.parse(dateString, new ParsePosition(0));
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		return cal;
	}

	public static boolean isDateToday(java.sql.Timestamp date) {
		if (date == null) return false;

		SimpleDateFormat sdf = new SimpleDateFormat();
		sdf.applyPattern("dd.MM.yyyy");
		String lDate = sdf.format(date);

		Date today = new Date(System.currentTimeMillis());
		String lToday = sdf.format(today);

		if (lDate.equals(lToday)) return true;

		return false;
	}

	public static boolean isDateToday(Long date) {
		if (date == null) return false;
		return isDateToday(new java.sql.Timestamp(date.longValue()));
	}

	public static boolean isDateToday(java.util.Date date) {
		if (date == null) return false;
		return isDateToday(new java.sql.Timestamp(date.getTime()));
	}

	public static boolean isDateToday(java.util.Calendar date) {
		if (date == null) return false;
		return isDateToday(new java.sql.Timestamp(date.getTime().getTime()));
	}

	public static String getSql2String(java.util.Date date) {
		if (date == null) return new String();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		//        java.util.Date d = sdf.parse(date.toString(), new ParsePosition(0));
		sdf.applyPattern("dd.MM.yyyy");
		return sdf.format(date);
	}

	public static Calendar getDate2Calendar(java.util.Date date) {
		Calendar gc = new GregorianCalendar();
		if (date == null) return gc;

		gc.setTime(date);
		return gc;
	}

	public static String getSql2String(java.util.Calendar date) {
		if (date == null) return new String();
		return getSql2String(date.getTime());
	}

	public static String getSql2String(Long date) {
		if (date == null) return new String();
		return getSql2String(new java.util.Date(date.longValue()));
	}

	public static String getSql2String(java.sql.Timestamp date) {
		if (date == null) return new String();
		return getSql2String(new Long(date.getTime()));
	}

	public static String getSql2TimeString(java.sql.Timestamp date) {
		if (date == null) return new String();
		return getSql2TimeString(new Long(date.getTime()));
	}

	public static String getSql2TimeString(java.util.Date date) {
		if (date == null) return new String();
		return getSql2TimeString(new Long(date.getTime()));
	}

	public static String getSql2TimeString(java.util.Calendar date) {
		if (date == null) return new String();
		return getSql2TimeString(date.getTime());
	}

	public static String getSql2TimeString(java.lang.Long date) {
		if (date == null) return new String();

		SimpleDateFormat sdf = new SimpleDateFormat();
		sdf.applyPattern("HH:mm");
		return sdf.format(new Date(date.longValue()));
	}	
}