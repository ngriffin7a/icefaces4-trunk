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

package org.icefaces.samples.showcase.example.ace.dataTable;

import java.io.Serializable;
import java.util.*;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.model.SelectItem;

@ManagedBean(name= DataTableFilteringDates.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class DataTableFilteringDates implements Serializable {
    public static final String BEAN_NAME = "dataTableFilteringDates";
	public String getBeanName() { return BEAN_NAME; }

	private List<Event> events;
	private String[] eventTypes = {"Wedding", "Birthday", "Anniversary", "Reunion", "Other"};
	private List<SelectItem> eventOptions = new ArrayList<SelectItem>() {{
		add(new SelectItem(""));
		add(new SelectItem("Wedding"));
		add(new SelectItem("Birthday"));
		add(new SelectItem("Anniversary"));
		add(new SelectItem("Reunion"));
	}};

    public DataTableFilteringDates() {
		events = new ArrayList<Event>(100);
		Random random = new Random();
		for (int i = 0; i < 100; i++) {
			Calendar calendar = Calendar.getInstance();
			calendar.setLenient(true);
			calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + i);
			Date date = calendar.getTime();
			String event = eventTypes[random.nextInt(4)];
			events.add(new Event(i, date, event));
		}
	}

	public List<Event> getEvents() { return events; }
	public void setEvents(List<Event> events) { this.events = events; }

    public List<SelectItem> getEventOptions() { return eventOptions; }
    public void setEventOptions(List<SelectItem> eventOptions) { this.eventOptions = eventOptions; }

	private String type;
	public String getType() { return type; }
	public void setType(String type) { this.type = type; }

	private Object minDate;
	public Object getMinDate() { return minDate; }
	public void setMinDate(Object minDate) { this.minDate = minDate; }

	private Object maxDate;
	public Object getMaxDate() { return maxDate; }
	public void setMaxDate(Object maxDate) { this.maxDate = maxDate; }
}
