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
}