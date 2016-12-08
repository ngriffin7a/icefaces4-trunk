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

package org.icefaces.samples.showcase.example.ace.schedule;

import org.icefaces.ace.model.schedule.ScheduleEvent;
import org.icefaces.ace.model.schedule.LazyScheduleEventList;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.model.SelectItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;

@ManagedBean(name= ScheduleBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class ScheduleBean implements Serializable {
    public static final String BEAN_NAME = "scheduleBean";
	public String getBeanName() { return BEAN_NAME; }
    
	private List<ScheduleEvent> eventList = new ArrayList<ScheduleEvent>();
	private Random randomEvents = new Random();
	private Random randomDays = new Random();
	private Random randomHours = new Random();
	private Random randomMinutes = new Random();
	private Random randomDurationHours = new Random();

	public ScheduleBean() {
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int nextYear = year + 1;
		for (year--; year <= nextYear; year++) {
			for (int i = 0; i < 12; i++) {
				eventList.addAll(generateRandomEventList(year, i));
			}
		}
	}

	private LazyScheduleEventList lazyScheduleEventList =
		new LazyScheduleEventList() {
			public List<ScheduleEvent> load(Date startDate, Date endDate) {
				List<ScheduleEvent> events = new ArrayList<ScheduleEvent>();
				long startTime = startDate.getTime();
				long endTime = endDate.getTime();
				int size = eventList.size();
				for (int i = 0; i < size; i++) {
					ScheduleEvent event = eventList.get(i);
					long time = event.getStartDate().getTime();
					if (time >= startTime && time <= endTime)
						events.add(event);
				}
				return events;
			}

			public boolean add(ScheduleEvent e) {
				return eventList.add(e);
			}

			public ScheduleEvent set(int index, ScheduleEvent element) {
				ScheduleEvent oldElement = get(index);
				oldElement.setStartDate(element.getStartDate());
				oldElement.setEndDate(element.getEndDate());
				oldElement.setTitle(element.getTitle());
				oldElement.setLocation(element.getLocation());
				oldElement.setNotes(element.getNotes());
				oldElement.setStyleClass(element.getStyleClass());
				oldElement.setId(element.getId());
				return element;
			}

			public ScheduleEvent remove(int index) {
				ScheduleEvent element = get(index);
				eventList.remove(element);
				return element;
			}
		};

	public LazyScheduleEventList getLazyScheduleEventList() {
		return lazyScheduleEventList;
	}

	public void setLazyScheduleEventList(LazyScheduleEventList lazyScheduleEventList) {
		this.lazyScheduleEventList = lazyScheduleEventList;
	}

	private List<ScheduleEvent> generateRandomEventList(int year, int month) {
		ArrayList<ScheduleEvent> list = new ArrayList<ScheduleEvent>();
		int eventsNumber = randomEvents.nextInt(21) + 20; // from 20 to 40 events
		for (int i = 0; i < eventsNumber; i++) {
			ScheduleEvent event = new ScheduleEvent();
			int day = randomDays.nextInt(27) + 1;
			int startHours = randomHours.nextInt(21);
			int startMinutes = randomMinutes.nextInt(2) * 30;
			int duration = randomDurationHours.nextInt(2) + 1;
			event.setStartDate(getDate(year, month, day, startHours, startMinutes));
			event.setEndDate(getDate(year, month, day, startHours + duration, startMinutes));
			event.setTitle("Random event " + i);
			event.setLocation("Some location");
			event.setNotes("Random notes...");
			list.add(event);
		}
		eventsNumber = randomEvents.nextInt(11) + 5; // from 5 to 15 events
		for (int i = 0; i < eventsNumber; i++) {
			ScheduleEvent event = new ScheduleEvent();
			int day = randomDays.nextInt(27) + 1;
			int startHours = randomHours.nextInt(21);
			int startMinutes = randomMinutes.nextInt(2) * 30;
			int duration = randomDurationHours.nextInt(2) + 1;
			event.setStartDate(getDate(year, month, day, startHours, startMinutes));
			event.setEndDate(getDate(year, month, day, startHours + duration, startMinutes));
			event.setTitle("Meeting " + i);
			event.setLocation("Some meeting room");
			event.setNotes("Meeting notes...");
			event.setStyleClass("meeting");
			list.add(event);
		}
		return list;
	}

	private Date getDate(int year, int month, int day, int hours, int minutes) {
		Calendar cal = Calendar.getInstance();
		cal.set(year, month, day, hours, minutes);
		cal.set(Calendar.SECOND, 0);
		return cal.getTime();
	}

	private String viewMode = "month";
	public String getViewMode() { return viewMode; }
	public void setViewMode(String viewMode) { this.viewMode = viewMode; }

	private boolean scrollable;
	public boolean isScrollable() { return scrollable; }
	public void setScrollable(boolean scrollable) { this.scrollable = scrollable; }

	private int scrollHeight = 600;
	public int getScrollHeight() { return scrollHeight; }
	public void setScrollHeight(int scrollHeight) { this.scrollHeight = scrollHeight; }

	private String timeZone;
	public String getTimeZone() { return timeZone; }
	public void setTimeZone(String timeZone) { this.timeZone = timeZone; }

    private List<SelectItem> timeZoneList = null;
    public List<SelectItem> getTimeZoneList() {
		if (timeZoneList == null) {
			timeZoneList = new ArrayList<SelectItem>();
			timeZoneList.add(new SelectItem(TimeZone.getDefault().getID(),
				"System Default (" + TimeZone.getDefault().getID() + ")"));
			timeZoneList.add(new SelectItem("America/Vancouver"));
			timeZoneList.add(new SelectItem("America/Edmonton"));
			timeZoneList.add(new SelectItem("America/Toronto"));
		}
		return timeZoneList;
	}
    public void setTimeZoneList(List<SelectItem> timeZoneList) { this.timeZoneList = timeZoneList; }

	private String sideBar = "right";
	public String getSideBar() { return sideBar; }
	public void setSideBar(String sideBar) { this.sideBar = sideBar; }

	private String eventDetails = "popup";
	public String getEventDetails() { return eventDetails; }
	public void setEventDetails(String eventDetails) { this.eventDetails = eventDetails; }
}