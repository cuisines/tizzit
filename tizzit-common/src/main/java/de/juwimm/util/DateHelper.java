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
package de.juwimm.util;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateHelper {

	/**
	 * set 00:00 for a date
	 * 
	 * @param date
	 * @return
	 */
	public static Date resetTime(Date date) {
		Calendar cal = Calendar.getInstance();
		
		return resetTime(date, cal);
	}
	
	public static Date resetTime(Date date, Calendar cal) {
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		
		return cal.getTime();
	}
	
	/**
	 * get actual date 00:00 plus dayOffset days
	 * 
	 * @param dayOffset
	 * @return
	 */
	public static Date getActDate(int dayOffset) {
		Calendar cal = Calendar.getInstance();
		
		cal.setTimeInMillis(System.currentTimeMillis());
		cal.add(Calendar.DAY_OF_YEAR, dayOffset);
		
		return DateHelper.resetTime(cal.getTime());		
	}
	
	public static Date getEndOfDay(Date date, TimeZone tz) {
		Calendar cal = Calendar.getInstance(tz);
		
		return getEndOfDay(date, cal);
	}
	
	public static Date getEndOfDay(Date date, Calendar cal) {
		cal.setTime(resetTime(date, cal));
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 999);
		
		return cal.getTime();
	}
	
	public static int getDay(Date date, Calendar cal) {
		cal.setTime(date);
		
		return cal.get(Calendar.DAY_OF_MONTH);
	}
	
	public static int getMonth(Date date, Calendar cal) {
		cal.setTime(date);
		
		return cal.get(Calendar.MONTH)+1;
	}
	
	public static int getYear(Date date, Calendar cal) {
		cal.setTime(date);
		
		return cal.get(Calendar.YEAR);
	}
}