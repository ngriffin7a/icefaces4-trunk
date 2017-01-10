/*
 * Original Code Copyright Prime Technology.
 * Subsequent Code Modifications Copyright 2011-2014 ICEsoft Technologies Canada Corp. (c)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * NOTE THIS CODE HAS BEEN MODIFIED FROM ORIGINAL FORM
 *
 * Subsequent Code Modifications have been made and contributed by ICEsoft Technologies Canada Corp. (c).
 *
 * Code Modification 1: Integrated with ICEfaces Advanced Component Environment.
 * Contributors: ICEsoft Technologies Canada Corp. (c)
 *
 * Code Modification 2: [ADD BRIEF DESCRIPTION HERE]
 * Contributors: ______________________
 * Contributors: ______________________
 */
package org.icefaces.ace.component.schedule;

import org.icefaces.ace.model.schedule.ScheduleEvent;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

public class ScheduleUtils {

	/**
	 * source Date object, assumed to represent time in the given time zone
     */
	public static Date toUTCFromTimeZone(Date date, TimeZone timeZone) {

		// get offset of the specified time zone for the given date
		int timeZoneOffset = timeZone.getOffset(date.getTime());

		// subtract offset from given date to get UTC date
		long milliseconds = date.getTime() - timeZoneOffset;
		return new Date(milliseconds);
	}

	/**
	 * source Date object, assumed to represent a UTC time
     */
	public static Date toTimeZoneFromUTC(Date date, TimeZone timeZone) {

		// get offset of the specified time zone for the given date
		int timeZoneOffset = timeZone.getOffset(date.getTime() + timeZone.getRawOffset());

		// add offset to new date
		long milliseconds = date.getTime() + timeZoneOffset;
		return new Date(milliseconds);
	}

	public static Date toUTCFromString(String date, String time, String offsetInMinutes) {
		Calendar cal = Calendar.getInstance();
		String yearString = date.substring(0, 4);
		String monthString = date.substring(5, 7);
		String dayString = date.substring(8, 10);
		String hourString = time.substring(0, time.indexOf(":"));
		String minuteString = time.substring(time.indexOf(":")+1);
		try {
			int year, month, day, hour, minute, offset;
			year = Integer.valueOf(yearString);
			month = Integer.valueOf(monthString) - 1;
			day = Integer.valueOf(dayString);
			hour = Integer.valueOf(hourString);
			minute = Integer.valueOf(minuteString);
			cal.set(year, month, day, hour, minute);

			offset = Integer.valueOf(offsetInMinutes);
			return new Date(cal.getTime().getTime() - (offset * 60000));
		} catch (Exception e) {
			/* TO_DO: log warning */
			return null;
		}
	}

	/**
     * Assumes month is 1-relative
     */
	public static Date convertDateTimeToServerFormat(String date, String time) {
		Calendar cal = Calendar.getInstance();
		String yearString = date.substring(0, 4);
		String monthString = date.substring(5, 7);
		String dayString = date.substring(8, 10);
		String hourString = time.substring(0, time.indexOf(":"));
		String minuteString = time.substring(time.indexOf(":")+1);
		try {
			int year, month, day, hour, minute;
			year = Integer.valueOf(yearString);
			month = Integer.valueOf(monthString) - 1;
			day = Integer.valueOf(dayString);
			hour = Integer.valueOf(hourString);
			minute = Integer.valueOf(minuteString);
			cal.set(year, month, day, hour, minute);
			cal.set(Calendar.SECOND, 0);
		} catch (Exception e) {
			/* TO_DO: log warning */
			return null;
		}
		return cal.getTime();
	}

	public static ScheduleEvent buildScheduleEventFromRequest(Schedule schedule, 
			Map<String, String> params, String clientId) {
		String startDate = params.get(clientId + "_date");
		String startTime = params.get(clientId + "_time");
		String endDate = params.get(clientId + "_endDate");
		String endTime = params.get(clientId + "_endTime");
		String title = params.get(clientId + "_title");
		String location = params.get(clientId + "_location");
		String notes = params.get(clientId + "_notes");
		String styleClass = params.get(clientId + "_styleClass");
		String id = params.get(clientId + "_id");
		TimeZone timeZone = schedule.calculateTimeZone();

		// if no start date specified, use current date
		if (startDate == null || "".equals(startDate)) {
			Calendar cal = Calendar.getInstance(timeZone);
			startDate = cal.get(Calendar.YEAR) + "-"
				+ ((cal.get(Calendar.MONTH)+1) < 10 ? "0" : "") + (cal.get(Calendar.MONTH)+1) + "-"
				+ (cal.get(Calendar.DATE) < 10 ? "0" : "") + cal.get(Calendar.DATE);
		}

		// if no start time specified, use next hour
		if (startTime == null || "".equals(startTime)) {
			Calendar cal = Calendar.getInstance(timeZone);
			cal.add(Calendar.HOUR_OF_DAY, 1);
			startTime = (cal.get(Calendar.HOUR_OF_DAY) < 10 ? "0" : "") + cal.get(Calendar.HOUR_OF_DAY) + ":00";
			if (cal.get(Calendar.HOUR_OF_DAY) == 0) { // next day
				startDate = cal.get(Calendar.YEAR) + "-"
					+ ((cal.get(Calendar.MONTH)+1) < 10 ? "0" : "") + (cal.get(Calendar.MONTH)+1) + "-"
					+ (cal.get(Calendar.DATE) < 10 ? "0" : "") + cal.get(Calendar.DATE);
			}
		}

		// if no end date specified, use start date
		// if no end time specified, use default duration
		if ((endDate == null || "".equals(endDate))
			|| (endTime == null || "".equals(endTime))) {
			String[] endDateTime = getDefaultEndDateTime(schedule, clientId, startDate, startTime, endDate);
			endDate = endDateTime[0];
			endTime = endDateTime[1];
		}

		ScheduleEvent scheduleEvent = new ScheduleEvent();
		Date convertedDate = convertDateTimeToServerFormat(startDate, startTime);
		if (convertedDate == null) return null;
		Date convertedEndDate = convertDateTimeToServerFormat(endDate, endTime);
		if (convertedEndDate == null) convertedEndDate = new Date(convertedDate.getTime());
		scheduleEvent.setStartDate(toUTCFromTimeZone(convertedDate, timeZone));
		scheduleEvent.setEndDate(toUTCFromTimeZone(convertedEndDate, timeZone));
		scheduleEvent.setTitle(title);
		scheduleEvent.setLocation(location);
		scheduleEvent.setNotes(notes);
		if (styleClass != null) scheduleEvent.setStyleClass(styleClass);
		if (id != null) scheduleEvent.setId(id);

		return scheduleEvent;
	}

	public static ScheduleEvent buildOldScheduleEventFromRequest(Schedule schedule, 
			Map<String, String> params, String clientId) {
		String startDate = params.get(clientId + "_old_startDate");
		String startTime = params.get(clientId + "_old_startTime");
		String endDate = params.get(clientId + "_old_endDate");
		String endTime = params.get(clientId + "_old_endTime");
		String title = params.get(clientId + "_old_title");
		String location = params.get(clientId + "_old_location");
		String notes = params.get(clientId + "_old_notes");
		String styleClass = params.get(clientId + "_styleClass");
		String id = params.get(clientId + "_id");
		TimeZone timeZone = schedule.calculateTimeZone();

		ScheduleEvent scheduleEvent = new ScheduleEvent();
		Date convertedDate = convertDateTimeToServerFormat(startDate, startTime);
		if (convertedDate == null) return null;
		Date convertedEndDate = convertDateTimeToServerFormat(endDate, endTime);
		if (convertedEndDate == null) convertedEndDate = new Date(convertedDate.getTime());
		scheduleEvent.setStartDate(toUTCFromTimeZone(convertedDate, timeZone));
		scheduleEvent.setEndDate(toUTCFromTimeZone(convertedEndDate, timeZone));
		scheduleEvent.setTitle(title);
		scheduleEvent.setLocation(location);
		scheduleEvent.setNotes(notes);
		if (styleClass != null) scheduleEvent.setStyleClass(styleClass);
		if (id != null) scheduleEvent.setId(id);

		return scheduleEvent;
	}

	/**
	 * Returns the integer values of the last day of the week, starting with the current day.
     * Months are 0-relative.
     */
	public static int[] determineLastDayOfWeek(int currentYear, int currentMonth, int currentDay) {
		int endYear = currentYear;
		int endMonth = currentMonth;
		int endDay = currentDay;
		boolean is31DaysMonth = currentMonth == 0 || currentMonth == 2 || currentMonth == 4 || currentMonth == 6
			|| currentMonth == 7 || currentMonth == 9 || currentMonth == 11;
		boolean isLeapYear = ((currentYear % 4 == 0) && (currentYear % 100 != 0)) || (currentYear % 400 == 0);
		if (is31DaysMonth) {
			if (currentMonth == 11) {
				if (currentDay >= 26) {
					endYear = currentYear + 1;
					endMonth = 0;
					endDay = currentDay + 6 - 31;
				} else {
					endDay = currentDay + 6;
				}
			} else if (currentDay >= 26) {
				endMonth = currentMonth + 1;
				endDay = currentDay + 6 - 31;
			} else {
				endDay = currentDay + 6;
			}
		} else {
			if (currentMonth == 1) {
				if (isLeapYear) {
					if (currentDay >= 24) {
						endMonth = 2;
						endDay = currentDay + 6 - 29;
					} else {
						endDay = currentDay + 6;
					}
				} else {
					if (currentDay >= 23) {
						endMonth = 2;
						endDay = currentDay + 6 - 28;
					} else {
						endDay = currentDay + 6;
					}
				}
			} else if (currentDay >= 25) {
				endMonth = currentMonth + 1;
				endDay = currentDay + 6 - 30;
			} else {
				endDay = currentDay + 6;
			}
		}

		int[] values = new int[3];
		values[0] = endYear;
		values[1] = endMonth;
		values[2] = endDay;
		return values;
	};

	/**
	 * Returns the integer day number of the last day of the given month. Months are 0-relative.
     */
	public static int determineLastDayOfMonth(int currentYear, int currentMonth) {
		boolean is31DaysMonth = currentMonth == 0 || currentMonth == 2 || currentMonth == 4 || currentMonth == 6
			|| currentMonth == 7 || currentMonth == 9 || currentMonth == 11;
		boolean isLeapYear = ((currentYear % 4 == 0) && (currentYear % 100 != 0)) || (currentYear % 400 == 0);
		if (currentMonth == 1) { // February
			if (isLeapYear) return 29;
			else return 28;
		} else if (is31DaysMonth) {
			return 31;
		} else {
			return 30;
		}
	}

	/**
	 * Returns the integer year, month, day, hour, minute, and second values of a Date object. Months are 0-relative
     * The hour is assumed to be based on a 24-hour clock.
     */
	public static DateIntegerValues getDateIntegerValues(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DATE);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);
		int second = cal.get(Calendar.SECOND);
		return new DateIntegerValues(year, month, day, hour, minute, second);
	}

	/**
	 * Returns the Date object from the DateIntegerValues object provided. Months are 0-relative.
     * The hour is assumed to be based on a 24-hour clock.
     */
	public static Date getDateFromIntegerValues(DateIntegerValues values) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, values.getYear());
		cal.set(Calendar.MONTH, values.getMonth());
		cal.set(Calendar.DATE, values.getDay());
		cal.set(Calendar.HOUR_OF_DAY, values.getHour());
		cal.set(Calendar.MINUTE, values.getMinute());
		cal.set(Calendar.SECOND, values.getSecond());
		return cal.getTime();
	}

	public static class DateIntegerValues {
		int year;
		int month;
		int day;
		int hour;
		int minute;
		int second;

		public DateIntegerValues(int year, int month, int day, int hour, int minute, int second) {
			this.year = year;
			this.month = month;
			this.day = day;
			this.hour = hour;
			this.minute = minute;
			this.second = second;
		}

		public int getYear() { return this.year; }
		public int getMonth() { return this.month; }
		public int getDay() { return this.day; }
		public int getHour() { return this.hour; }
		public int getMinute() { return this.minute; }
		public int getSecond() { return this.second; }
	}

	public static String[] getDefaultEndDateTime(Schedule schedule, String clientId, 
		String startDate, String startTime, String endDate) {
		TimeZone timeZone = schedule.calculateTimeZone();
		String[] endDateTimeValues = new String[2];

		if (startDate.equals(endDate)
			|| (endDate == null || "".equals(endDate))) {
			int defaultDuration = schedule.getDefaultDuration();
			try {
				Calendar cal = Calendar.getInstance();
				int year = Integer.valueOf(startDate.substring(0, 4));
				int month = Integer.valueOf(startDate.substring(5, 7));
				int day = Integer.valueOf(startDate.substring(8, 10));
				int hour = Integer.valueOf(startTime.substring(0, startTime.indexOf(":")));
				int minute = Integer.valueOf(startTime.substring(startTime.indexOf(":")+1));
				cal.set(year, month-1, day, hour, minute);
				cal.add(Calendar.MINUTE, defaultDuration);
				endDateTimeValues[0] = cal.get(Calendar.YEAR) + "-"
					+ ((cal.get(Calendar.MONTH)+1) < 10 ? "0" : "") + (cal.get(Calendar.MONTH)+1) + "-"
					+ (cal.get(Calendar.DATE) < 10 ? "0" : "") + cal.get(Calendar.DATE);
				endDateTimeValues[1] = (cal.get(Calendar.HOUR_OF_DAY) < 10 ? "0" : "") + cal.get(Calendar.HOUR_OF_DAY) + ":"
					+ (cal.get(Calendar.MINUTE) < 10 ? "0" : "") + cal.get(Calendar.MINUTE);
			} catch (Exception e) {
				endDateTimeValues[0] = startDate;
				endDateTimeValues[1] = startTime;
			}
		} else {
			endDateTimeValues[0] = endDate;
			endDateTimeValues[1] = startTime;
		}

		return endDateTimeValues;
	}

	public static class ScheduleEventDecorator extends ScheduleEvent implements Comparable<ScheduleEventDecorator> {

		private int index;

		public ScheduleEventDecorator(ScheduleEvent original, int index) {
			this.setId(original.getId());
			this.setTitle(original.getTitle());
			this.setStartDate(original.getStartDate());
			this.setEndDate(original.getEndDate());
			this.setLocation(original.getLocation());
			this.setStyleClass(original.getStyleClass());
			this.setNotes(original.getNotes());
			this.index = index;
		}

		public int getIndex() { return index; }

		public int compareTo(ScheduleEventDecorator other) {
			if (this.getStartDate().after(other.getStartDate())) return 1;
			else if (this.getStartDate().before(other.getStartDate())) return -1;
			else return 0;
		}
	}
}