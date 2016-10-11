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

public class ScheduleTemplates {

	static String fullMonth =
		"<div class=\"clndr-controls ui-state-active\">"
			+"<div class=\"clndr-previous-button\">&lt;</div>"
			+"<div class=\"clndr-next-button\">&gt;</div>"
			+"<div class=\"current-month\"><%= month %> <%= year %></div>"
		+"</div>"

		+"<div class=\"schedule-content\">"

			+"<div class=\"clndr-grid ui-widget-content\">"
				+"<table><thead class=\"days-of-the-week ui-state-default\"><tr>"
					+"<% _.each(daysOfTheWeek, function(day) { %>"
						+"<th class=\"header-day\"><%= day %></th>"
					+"<% }); %>"
				+"</tr></thead>"
				+"<tbody class=\"days\">"
					+"<% _.each(days, function(day, index) { %>"
						+"<% if (index % 7 == 0) %><tr><% ; %>"
							+"<td class=\"<%= day.classes %> ui-widget-content\" id=\"<%= day.id %>\">"
								+"<% if (day.classes.indexOf('today') > -1) %><div class=\"ui-state-highlight\"><% ; %>"
									+"<div class=\"day-number\"><%= day.day %></div>"
									+"<% var count = 0; %>"
									+"<% _.each(day.events, function(event) { if (day.classes.indexOf('adjacent-month') == -1) %>"
										+"<div class=\"ui-state-hover ui-corner-all schedule-event event-<%= count %>\">"
										+"<%= event.time %> <%= event.title %></div><% ;count++; }); %>"
								+"<% if (day.classes.indexOf('today') > -1) %></div><% ; %>"
							+"</td>"
						+"<% if (index % 7 == 6) %></tr><% ;}); %>"
				+"</tbody></table>"
			+"</div>"

			+"<div class=\"schedule-sidebar ui-widget-content\">"

				+"<div class=\"event-listing\">"
					+"<div class=\"event-listing-title ui-state-default\">Events this Month</div>"
					+"<div class=\"event-listing-body\">"
						+"<% _.each(eventsThisMonth, function(event) { %>"
							+"<div class=\"event-item\">"
								+"<div class=\"event-item-name\"><%= event.title %></div>"
								+"<div class=\"event-item-location\"><%= event.location %></div>"
							+"</div>"
						+"<% }); %>"
					+"</div>"
				+"</div>"

				+"<div class=\"event-details\">"
					+"<div class=\"event-details-title ui-state-default\">Event Details</div>"
					+"<div class=\"event-details-body\"></div>"
				+"</div>"

			+"</div>"

		+"</div>";

	static String fullWeek =
		"<div class=\"clndr-controls ui-state-active\">"
			+"<div class=\"clndr-previous-button\">&lt;</div>"
			+"<div class=\"clndr-next-button\">&gt;</div>"
			+"<div class=\"current-month\"><%= month %> <%= year %></div>"
		+"</div>"

		+"<div class=\"schedule-content\">"

			/* time grid */
			+"<div class=\"clndr-grid ui-widget-content\">"
				+"<table><thead class=\"days-of-the-week ui-state-default\"><tr>"
					+"<th class=\"header-day\">Time</th>"
					+"<% _.each(daysOfTheWeek, function(day, index) { %>"
						+"<th class=\"header-day dow-<%= index %>\"><%= day %></th>"
					+"<% }); %>"
				+"</tr></thead>"
				+"<tbody class=\"days\">"
					+"<% var i; for (i = 0; i < 24; i++) {var iString = i < 10 ? '0' + i : i;%>"
						+"<tr>"
							+"<td class=\"ui-widget-content\"><%= i %>:00</td>"
							+"<td class=\"ui-widget-content dow-0 time-<%= iString %>00\"></td>"
							+"<td class=\"ui-widget-content dow-1 time-<%= iString %>00\"></td>"
							+"<td class=\"ui-widget-content dow-2 time-<%= iString %>00\"></td>"
							+"<td class=\"ui-widget-content dow-3 time-<%= iString %>00\"></td>"
							+"<td class=\"ui-widget-content dow-4 time-<%= iString %>00\"></td>"
							+"<td class=\"ui-widget-content dow-5 time-<%= iString %>00\"></td>"
							+"<td class=\"ui-widget-content dow-6 time-<%= iString %>00\"></td>"
						+"</tr><tr>"
							+"<td class=\"ui-widget-content\"><%= i %>:30</td>"
							+"<td class=\"ui-widget-content dow-0 time-<%= iString %>30\"></td>"
							+"<td class=\"ui-widget-content dow-1 time-<%= iString %>30\"></td>"
							+"<td class=\"ui-widget-content dow-2 time-<%= iString %>30\"></td>"
							+"<td class=\"ui-widget-content dow-3 time-<%= iString %>30\"></td>"
							+"<td class=\"ui-widget-content dow-4 time-<%= iString %>30\"></td>"
							+"<td class=\"ui-widget-content dow-5 time-<%= iString %>30\"></td>"
							+"<td class=\"ui-widget-content dow-6 time-<%= iString %>30\"></td>"
						+"</tr><% ;}; %>"
				+"</tbody></table>"
			+"</div>"

			/* events */
			+"<% setTimeout(function(){_.each(days, function(day, index) {"
				+"var count = 0;"
				+"var dow = index % 7;"
				+"_.each(day.events, function(event) {"
				+"var hour = event.time.substring(0,2);"
				+"var selector = '.dow-'+dow+'.time-'+hour+'00';"
				+"var offset = ice.ace.jq(selector).offset();"
				+"ice.ace.jq('<div class=\"ui-state-hover ui-corner-all schedule-event event-'+count+'\">'+event.title+'</div')"
				+".css({position:'absolute', top:offset.top, left:offset.left}).appendTo(document.body);"
				+"count++;"
			+"})});}, 1000); %>"

			+"<div class=\"schedule-sidebar ui-widget-content\">"

				+"<div class=\"event-listing\">"
					+"<div class=\"event-listing-title ui-state-default\">Events this Month</div>"
					+"<div class=\"event-listing-body\">"
						+"<% _.each(eventsThisMonth, function(event) { %>"
							+"<div class=\"event-item\">"
								+"<div class=\"event-item-name\"><%= event.title %></div>"
								+"<div class=\"event-item-location\"><%= event.location %></div>"
							+"</div>"
						+"<% }); %>"
					+"</div>"
				+"</div>"

				+"<div class=\"event-details\">"
					+"<div class=\"event-details-title ui-state-default\">Event Details</div>"
					+"<div class=\"event-details-body\"></div>"
				+"</div>"

			+"</div>"

		+"</div>";

		static String fullDay = "";
		static String mini = "";
}
