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

package org.icefaces.ace.model.schedule;

import java.util.Date;

public class ScheduleEvent {

	private String id;
    public String getId() { return id; }
	public void setId(String id) { this.id = id; }

	private Date date;
    public Date getDate() { return date; }
	public void setDate(Date date) { this.date = date; }

	private String title;
    public String getTitle() { return title; }
	public void setTitle(String title) { this.title = title; }

	private Date endDate;
    public Date getEndDate() { return endDate; }
	public void setEndDate(Date endDate) { this.date = endDate; }

	private String location;
    public String getLocation() { return location; }
	public void setLocation(String location) { this.location = location; }

	private String styleClass;
    public String getStyleClass() { return styleClass; }
	public void setStyleClass(String styleClass) { this.styleClass = styleClass; }

	private String notes;
    public String getNotes() { return notes; }
	public void setNotes(String notes) { this.notes = notes; }

	private String data; // arbitrary data do used with custom templates
    public String getData() { return data; }
	public void setData(String data) { this.data = data; }
}