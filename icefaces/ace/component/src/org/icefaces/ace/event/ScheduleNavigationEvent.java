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

import javax.faces.event.AjaxBehaviorEvent;

public class ScheduleNavigationEvent extends AjaxBehaviorEvent {

	private boolean next = false;
	private boolean previous = false;

	private String startDate = "";
	private String endDate = "";

    public ScheduleNavigationEvent(AjaxBehaviorEvent event, String type, String startDate, String endDate) {
        super(event.getComponent(), event.getBehavior());
		if (type != null) {
			if ("next".equals(type)) this.next = true;
			else if ("previous".equals(type)) this.previous = true;
		}
		this.startDate = startDate;
		this.endDate = endDate;
    }

	/**
     * Returns the date of the first day actively in view, in the format YYYY-MM-DD
     *
     * @return			the date of the first day in view, in the format YYYY-MM-DD
     */
    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

	/**
     * Returns the date of the last day actively in view, in the format YYYY-MM-DD
     *
     * @return			the date of the last day in view, in the format YYYY-MM-DD
     */
    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

	public boolean isNext() { return next; }
	public boolean isPrevious() { return previous; }
}
