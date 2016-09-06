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
import javax.faces.render.Renderer;

import java.io.IOException;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class ScheduleRenderer extends Renderer {

	public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
			
		Schedule schedule = (Schedule) component;
		ResponseWriter writer = context.getResponseWriter();
		String clientId = component.getClientId();

		writer.startElement("div", null);
		writer.writeAttribute("id", clientId, null);

		writer.startElement("div", null);
        writer.write("<script type=\"text/template\" id=\"" + clientId + "_template\">"
            +"<div class=\"clndr-controls ui-state-active\">"
              +"<div class=\"clndr-previous-button\">&lt;</div>"
              +"<div class=\"clndr-next-button\">&gt;</div>"
              +"<div class=\"current-month\"><%= month %> <%= year %></div>"

            +"</div>"
            +"<div class=\"clndr-grid ui-widget-content\">"
              +"<div class=\"days-of-the-week clearfix ui-state-default\">"
                +"<% _.each(daysOfTheWeek, function(day) { %>"
                  +"<div class=\"header-day\"><%= day %></div>"
                +"<% }); %>"
              +"</div>"
              +"<div class=\"days\">"
                +"<% _.each(days, function(day) { %>"
                  +"<div class=\"<%= day.classes %> ui-widget-content\" id=\"<%= day.id %>\">"
                  +"<% if (day.classes.indexOf('today') > -1) %><div class=\"ui-state-highlight\"><% ; %>"
                  +"<div class=\"day-number\"><%= day.day %></div>"
                  +"<% _.each(day.events, function(event) { if (day.classes.indexOf('adjacent-month') == -1) %><div class=\"ui-state-hover ui-corner-all\">"
                  +"<%= event.title %></div><% }); %>"
                  +"<% if (day.classes.indexOf('today') > -1) %></div><% ; %>"
                +"</div><% }); %>"
              +"</div>"
            +"</div>"
            +"<div class=\"event-listing ui-widget-content\">"
              +"<div class=\"event-listing-title ui-state-default\">EVENTS THIS MONTH</div>"
              +"<% _.each(eventsThisMonth, function(event) { %>"
                  +"<div class=\"event-item\">"
                    +"<div class=\"event-item-name\"><%= event.title %></div>"
                    +"<div class=\"event-item-location\"><%= event.location %></div>"
                  +"</div>"
                +"<% }); %>"
            +"</div>"
          +"</script>");
		writer.endElement("div");

		// get events
		Object value = schedule.getValue();
		List<ScheduleEvent> eventList = null;
		if (value == null) eventList = Collections.EMPTY_LIST;
		else if (value instanceof List) eventList = (List<ScheduleEvent>) value;
		else if (Object[].class.isAssignableFrom(value.getClass())) {
			ScheduleEvent[] eventArray = (ScheduleEvent[]) value;
		}

		// render event data
		JSONBuilder events = JSONBuilder.create();
		events.beginArray();

		Iterator<ScheduleEvent> iterator = eventList.iterator();
		while (iterator.hasNext()) {
			ScheduleEvent scheduleEvent = iterator.next();
			events.beginMap();
			events.entry("date", convertDateToClientFormat(scheduleEvent.getDate()));
			events.entry("title", scheduleEvent.getTitle());
			events.entry("location", scheduleEvent.getLocation());
			events.endMap();
		}

		events.endArray();

		writer.startElement("div", null);
		writer.writeAttribute("class", "ice-ace-schedule ui-widget", null);
		writer.endElement("div");

		writer.startElement("script", null);
		writer.writeAttribute("type", "text/javascript", null);
		writer.write("ice.ace.schedule.renderSchedule('"
			+ clientId + "', " + events.toString() + ");");
		writer.endElement("script");

		writer.endElement("div");
	}

	private String convertDateToClientFormat(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DATE);
		return (year + "-" + month + "-" + day);
	}
}