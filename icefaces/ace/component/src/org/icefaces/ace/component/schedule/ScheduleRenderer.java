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
import java.util.Collections;
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
			String lazyYear = params.get(schedule.getClientId(context) + "_lazyYear");
			String lazyMonth = params.get(schedule.getClientId(context) + "_lazyMonth");
			if (lazyYear != null && lazyMonth != null) {
				schedule.setLazyYear(new Integer(lazyYear));
				schedule.setLazyMonth(new Integer(lazyMonth));
			}
		}
		schedule.resetDataModel();

		String template = "";
		String templateName = schedule.getTemplate();
		if ("full".equalsIgnoreCase(templateName)) {
			template = ScheduleTemplates.full;
			templateName = "full";
		} else if ("mini".equalsIgnoreCase(templateName)) {
			template = ScheduleTemplates.mini;
			templateName = "mini";
		} else {
			templateName = "custom";
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

		writer.startElement("div", null);
        writer.write("<script type=\"text/template\" id=\"" + clientId + "_template\">"
			+ template + "</script>");
		writer.endElement("div");

		// render configuration and event data
		JSONBuilder jb = JSONBuilder.create();
		jb.beginFunction("ice.ace.create")
			.item("Schedule")
			.beginArray()
				.item(clientId)
				.beginMap()
					.entry("displayEventDetails", displayEventDetails);

					if (isLazy) {
						int[] lazyYearMonthValues = schedule.getLazyYearMonthValues();
						jb.entry("isLazy", true)
						.entry("lazyYear", lazyYearMonthValues[0])
						.entry("lazyMonth", lazyYearMonthValues[1]);
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
		writer.writeAttribute("class", "ice-ace-schedule ui-widget " + templateName 
			+ " " + sideBarClass + " " + displayEventDetailsClass, null);
		writer.endElement("div");

		writer.startElement("script", null);
		writer.writeAttribute("type", "text/javascript", null);
		writer.write(jb.toString());
		writer.endElement("script");

		writer.startElement("div", null);
		writer.writeAttribute("class", "event-details-popup", null);
		writer.writeAttribute("title", "Event Details", null);
		writer.endElement("div");

		writer.startElement("div", null);
		writer.writeAttribute("class", "event-details-tooltip", null);
		writer.endElement("div");

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
		String ampm = cal.get(Calendar.AM_PM) == Calendar.AM ? "am" : "pm";
		return (addLeadingZero(hour) + hour + ":" + addLeadingZero(minute) + minute + " " + ampm);
	}

	private String addLeadingZero(int value) {
		if (value < 10) return "0";
		return "";
	}
}