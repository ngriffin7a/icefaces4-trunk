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

package org.icefaces.ace.event;

import org.icefaces.ace.model.schedule.ScheduleEvent;

import javax.faces.event.AjaxBehaviorEvent;

public class ScheduleClickEvent extends AjaxBehaviorEvent {

	private boolean eventClick = false;
	private boolean dayClick = false;
	private boolean timeClick = false;

	private String day; // YYYY-MM-DD
	private String time; // HH:mm (24-hour clock)
	private ScheduleEvent scheduleEvent;

    public ScheduleClickEvent(AjaxBehaviorEvent event, String type, String day, String time, ScheduleEvent scheduleEvent) {
        super(event.getComponent(), event.getBehavior());
		if (type != null) {
			if ("eventClick".equals(type)) this.eventClick = true;
			else if ("dayClick".equals(type)) this.dayClick = true;
			else if ("timeClick".equals(type)) this.timeClick = true;
		}
		this.day = day != null ? day : "";
		this.time = time != null ? time : "";
		this.scheduleEvent = scheduleEvent;
    }

	/**
     * Returns a String representing the day that was clicked, in the format YYYY-MM-DD.
     * This only applies to 'dayClick' and 'timeClick' events.
     * This method will return the empty string for 'eventClick' events.
     *
     * @return			the day that was clicked, in the format YYYY-MM-DD
     */
    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

	/**
     * Returns a String representing the time slot that was clicked, in the format HH:mm (24-hour clock).
     * This only applies to 'timeClick' events.
     * This method will return the empty string for 'eventClick' and 'dayClick' events.
     *
     * @return			the time that was clicked, in the format HH:mm (24-hour clock)
     */
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

	/**
     * Returns an instance of ScheduleEvent that corresponds to the event that was clicked on the client.
     * This only applies to 'eventClick' events.
     * This method will return null for 'dayClick' and 'timeClick' events.
     *
     * @return			the ScheduleEvent object that was clicked
     */
    public ScheduleEvent getScheduleEvent() {
        return scheduleEvent;
    }

    public void setScheduleEvent(ScheduleEvent scheduleEvent) {
        this.scheduleEvent = scheduleEvent;
    }

	public boolean isEventClick() { return eventClick; }
	public boolean isDayClick() { return dayClick; }
	public boolean isTimeClick() { return timeClick; }
}
