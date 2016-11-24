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

public class ScheduleModifyEvent extends AjaxBehaviorEvent {

	private boolean addEvent = false;
	private boolean editEvent = false;
	private boolean deleteEvent = false;

	private ScheduleEvent scheduleEvent;
	private ScheduleEvent oldScheduleEvent;

    public ScheduleModifyEvent(AjaxBehaviorEvent event, String type, ScheduleEvent scheduleEvent, ScheduleEvent oldScheduleEvent) {
        super(event.getComponent(), event.getBehavior());
		if (type != null) {
			if ("addEvent".equals(type)) this.addEvent = true;
			else if ("editEvent".equals(type)) this.editEvent = true;
			else if ("deleteEvent".equals(type)) this.deleteEvent = true;
		}
		this.scheduleEvent = scheduleEvent;
		this.oldScheduleEvent = oldScheduleEvent;
    }

	/**
     * Returns the instance of ScheduleEvent that was added, edited or deleted.
     *
     * @return			the ScheduleEvent object that was added, edited, or deleted
     */
    public ScheduleEvent getScheduleEvent() {
        return scheduleEvent;
    }

    public void setScheduleEvent(ScheduleEvent scheduleEvent) {
        this.scheduleEvent = scheduleEvent;
    }

	/**
     * Returns an instance of ScheduleEvent that corresponds to the event before it was edited.
     * This only applies to 'editEvent' events.
     * This method will return null for 'addEvent' and 'editEvent' events.
     *
     * @return			the ScheduleEvent object that was edited, before any changes were applied to it
     */
    public ScheduleEvent getOldScheduleEvent() {
        return oldScheduleEvent;
    }

    public void setOldScheduleEvent(ScheduleEvent oldScheduleEvent) {
        this.oldScheduleEvent = oldScheduleEvent;
    }

	public boolean isAddEvent() { return addEvent; }
	public boolean isEditEvent() { return editEvent; }
	public boolean isDeleteEvent() { return deleteEvent; }
}
