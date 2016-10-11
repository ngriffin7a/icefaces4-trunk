/*
 * Copyright 2004-2014 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package org.icefaces.ace.component.schedule;

import org.icefaces.ace.util.JSONBuilder;
import org.icefaces.ace.model.schedule.ScheduleEvent;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.model.DataModel;
import javax.faces.render.Renderer;

import java.io.IOException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ScheduleRenderer extends Renderer {

	@Override
	public void decode(FacesContext context, UIComponent component) {

		Schedule schedule = (Schedule) component;
		Map<String, String> params = context.getExternalContext().getRequestParameterMap();
		schedule.resetDataModel();

		String clientId = schedule.getClientId(context);
		if (params.containsKey(clientId + "_add")) decodeAdd(context, schedule, params);
		else if (params.containsKey(clientId + "_edit")) decodeEdit(context, schedule, params);
		else if (params.containsKey(clientId + "_delete")) decodeDelete(context, schedule, params);
	}

	public void decodeAdd(FacesContext context, Schedule schedule, Map<String, String> params) {
		String clientId = schedule.getClientId(context);

		schedule.addEvent(buildScheduleEventFromRequest(params, clientId));
	}

	public void decodeEdit(FacesContext context, Schedule schedule, Map<String, String> params) {
		String clientId = schedule.getClientId(context);

		String indexParam = params.get(clientId + "_index");

		int index;
		try {
			index = Integer.valueOf(indexParam);
		} catch(Exception e) {
			/* TO_DO: log warning */
			return;
		}

		schedule.editEvent(index, buildScheduleEventFromRequest(params, clientId));
	}

	public void decodeDelete(FacesContext context, Schedule schedule, Map<String, String> params) {
		String clientId = schedule.getClientId(context);
		Object value = schedule.getValue();
		if (value instanceof List || Object[].class.isAssignableFrom(value.getClass())) {
			String indexParam = params.get(clientId + "_index");

			int index;
			try {
				index = Integer.valueOf(indexParam);
			} catch(Exception e) {
				/* TO_DO: log warning */
				return;
			}
			schedule.deleteEvent(index);
		} else if (value instanceof Collection) {
			schedule.deleteEvent(buildScheduleEventFromRequest(params, clientId));
		}
	}

	@Override
	public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
			
		Schedule schedule = (Schedule) component;
		ResponseWriter writer = context.getResponseWriter();
		String clientId = component.getClientId();

		// if in lazy mode, update current year and month values
		boolean isLazy = schedule.isLazy();
		if (isLazy) {
			Map<String, String> params = context.getExternalContext().getRequestParameterMap();
			String lazyYear = params.get(clientId + "_lazyYear");
			String lazyMonth = params.get(clientId + "_lazyMonth");
			String lazyDay = params.get(clientId + "_lazyDay");
			if (lazyYear != null && lazyMonth != null) {
				schedule.setLazyYear(new Integer(lazyYear));
				schedule.setLazyMonth(new Integer(lazyMonth));
				schedule.setLazyDay(new Integer(lazyDay));
			}
		}
		schedule.resetDataModel();

		String template = "";
		String templateName = schedule.getTemplate();
		if ("full".equalsIgnoreCase(templateName)) {
			templateName = "full";
		} else if ("mini".equalsIgnoreCase(templateName)) {
			templateName = "mini";
		} else {
			templateName = "custom";
		}
		String viewMode = schedule.getViewMode();
		if ("week".equalsIgnoreCase(viewMode)) {
			viewMode = "week";
		} else if ("day".equalsIgnoreCase(viewMode)) {
			viewMode = "day";
		} else {
			viewMode = "month";
		}
		String sideBar = schedule.getSideBar();
		String sideBarClass = "sidebar-right";
		if (sideBar != null) {
			if ("left".equalsIgnoreCase(sideBar)) sideBarClass = "sidebar-left";
			else if ("hidden".equalsIgnoreCase(sideBar)) sideBarClass = "sidebar-hidden";
		}
		String displayEventDetails = schedule.getDisplayEventDetails();
		String displayEventDetailsClass = "details-sidebar";
		if (displayEventDetails != null) {
			if ("popup".equalsIgnoreCase(displayEventDetails)) {
				displayEventDetailsClass = "details-popup";
				displayEventDetails = "popup";
			} else if ("tooltip".equalsIgnoreCase(displayEventDetails)) {
				displayEventDetailsClass = "details-tooltip";
				displayEventDetails = "tooltip";
			} else if ("disabled".equalsIgnoreCase(displayEventDetails)) {
				displayEventDetailsClass = "details-disabled";
				displayEventDetails = "disabled";
			} else {
				displayEventDetails = "sidebar";
			}
		} else {
			displayEventDetails = "sidebar";
		}

		writer.startElement("div", null);
		writer.writeAttribute("id", clientId, null);
		writer.writeAttribute("class", "ice-ace-schedule", null);

		writer.startElement("div", null);
		writer.writeAttribute("class", "event-details-popup-body", null);
		writer.writeAttribute("title", "Event Details", null);
		writer.endElement("div");

		writer.startElement("div", null);
		writer.writeAttribute("class", "event-details-tooltip-body", null);
		writer.endElement("div");

		writer.startElement("div", null);
		writer.writeAttribute("class", "events-container", null);
		writer.endElement("div");

		// render configuration and event data
		JSONBuilder jb = JSONBuilder.create();
		jb.beginFunction("ice.ace.create")
			.item("Schedule")
			.beginArray()
				.item(clientId)
				.beginMap()
					.entry("viewMode", viewMode)
					.entry("displayEventDetails", displayEventDetails)
					.entry("isEventAddition", "disabled".equalsIgnoreCase(schedule.getAdditionControls()) ? false : true)
					.entry("isEventEditing", "disabled".equalsIgnoreCase(schedule.getEditingControls()) ? false : true)
					.entry("isEventDeletion", "disabled".equalsIgnoreCase(schedule.getDeletionControls()) ? false : true);

					if (isLazy) {
						int[] lazyDateValues = schedule.getLazyDateValues();
						jb.entry("isLazy", true)
						.entry("lazyYear", lazyDateValues[0])
						.entry("lazyMonth", lazyDateValues[1])
						.entry("lazyDay", lazyDateValues[2]);
					} else {
						int[] currentDateValues = schedule.getCurrentDateValues();
						jb.entry("currentYear", currentDateValues[0])
						.entry("currentMonth", currentDateValues[1])
						.entry("currentDay", currentDateValues[2]);
					}

					jb.beginArray("events");

					int rowCount = schedule.getRowCount();
					for (int i = 0; i < rowCount; i++) {
						schedule.setRowIndex(i);
						ScheduleEvent scheduleEvent = (ScheduleEvent) schedule.getRowData();
						jb.beginMap();
						jb.entry("index", i);
						jb.entry("date", convertDateToClientFormat(scheduleEvent.getDate()));
						jb.entry("time", convertTimeToClientFormat(scheduleEvent.getDate()));
						jb.entry("title", scheduleEvent.getTitle());
						jb.entry("location", scheduleEvent.getLocation());
						jb.entry("notes", scheduleEvent.getNotes());
						jb.endMap();
					}

					jb.endArray()
				.endMap()
			.endArray()
          .endFunction();

		writer.startElement("div", null);
		writer.writeAttribute("class", "ice-ace-schedule-body ui-widget " + templateName 
			+ " " + viewMode + " " + sideBarClass + " " + displayEventDetailsClass, null);
		writer.endElement("div");

		writer.startElement("script", null);
		writer.writeAttribute("type", "text/javascript", null);
		writer.write(jb.toString());
		writer.endElement("script");

		writer.endElement("div");
	}

	private String convertDateToClientFormat(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DATE);
		return (year + "-" + addLeadingZero(month) + month + "-" + addLeadingZero(day) + day);
	}

	private String convertTimeToClientFormat(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);
		return (addLeadingZero(hour) + hour + ":" + addLeadingZero(minute) + minute);
	}

	private Date convertDateTimeToServerFormat(String date, String time) {
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

	private ScheduleEvent buildScheduleEventFromRequest(Map<String, String> params, String clientId){
		String date = params.get(clientId + "_date");
		String time = params.get(clientId + "_time");
		String title = params.get(clientId + "_title");
		String location = params.get(clientId + "_location");
		String notes = params.get(clientId + "_notes");

		ScheduleEvent scheduleEvent = new ScheduleEvent();
		Date convertedDate = convertDateTimeToServerFormat(date, time);
		if (convertedDate == null) return null;
		scheduleEvent.setDate(convertedDate);
		scheduleEvent.setTitle(title);
		scheduleEvent.setLocation(location);
		scheduleEvent.setNotes(notes);

		return scheduleEvent;
	}

	private String addLeadingZero(int value) {
		if (value < 10) return "0";
		return "";
	}
}