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

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

import java.io.IOException;

public class ScheduleRenderer extends Renderer {

	public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
			
		Schedule schedule = (Schedule) component;
		ResponseWriter writer = context.getResponseWriter();
		String clientId = component.getClientId();

		writer.startElement("div", null);
		writer.writeAttribute("id", clientId, null);

		writer.startElement("div", null);
        writer.write("<script type=\"text/template\" id=\"full-clndr-template\">"
            +"<div class=\"clndr-controls\">"
              +"<div class=\"clndr-previous-button\">&lt;</div>"
              +"<div class=\"clndr-next-button\">&gt;</div>"
              +"<div class=\"current-month\"><%= month %> <%= year %></div>"

            +"</div>"
            +"<div class=\"clndr-grid\">"
              +"<div class=\"days-of-the-week clearfix\">"
                +"<% _.each(daysOfTheWeek, function(day) { %>"
                  +"<div class=\"header-day\"><%= day %></div>"
                +"<% }); %>"
              +"</div>"
              +"<div class=\"days\">"
                +"<% _.each(days, function(day) { %>"
                  +"<div class=\"<%= day.classes %>\" id=\"<%= day.id %>\"><span class=\"day-number\"><%= day.day %></span></div>"
                +"<% }); %>"
              +"</div>"
            +"</div>"
            +"<div class=\"event-listing\">"
              +"<div class=\"event-listing-title\">EVENTS THIS MONTH</div>"
              +"<% _.each(eventsThisMonth, function(event) { %>"
                  +"<div class=\"event-item\">"
                    +"<div class=\"event-item-name\"><%= event.title %></div>"
                    +"<div class=\"event-item-location\"><%= event.location %></div>"
                  +"</div>"
                +"<% }); %>"
            +"</div>"
          +"</script>");
		writer.endElement("div");

		writer.startElement("div", null);
		writer.writeAttribute("id", "full-clndr", null);
		writer.writeAttribute("class", "schedule", null);
		writer.endElement("div");

		writer.startElement("script", null);
		writer.writeAttribute("type", "text/javascript", null);
		writer.write("ice.ace.schedule.renderSchedule('"
			+ clientId + "', " + schedule.getConfiguration() + ");");
		writer.endElement("script");

		writer.endElement("div");
	}
}