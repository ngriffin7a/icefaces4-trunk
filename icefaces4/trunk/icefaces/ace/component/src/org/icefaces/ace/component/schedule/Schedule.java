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

import org.icefaces.ace.event.ScheduleClickEvent;
import org.icefaces.ace.event.ScheduleModifyEvent;
import org.icefaces.ace.event.ScheduleNavigationEvent;
import org.icefaces.ace.model.schedule.LazyScheduleEventList;
import org.icefaces.ace.model.schedule.ScheduleEvent;

import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.FacesEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class Schedule extends ScheduleBase {

	private TimeZone appropriateTimeZone;

    @Override
    public void queueEvent(FacesEvent event) {
        if (event == null) {
            throw new NullPointerException();
        }

        if (event instanceof AjaxBehaviorEvent) {
            FacesContext context = FacesContext.getCurrentInstance();
            Map<String, String> params = context.getExternalContext().getRequestParameterMap();
            String eventName = params.get("javax.faces.behavior.event");
			String clientId = getClientId(context);

            if (eventName.equals("eventClick")) {
				ScheduleEvent scheduleEvent = ScheduleUtils.buildScheduleEventFromRequest(this, params, clientId);
                event = new ScheduleClickEvent((AjaxBehaviorEvent) event, "eventClick", "", "", scheduleEvent);
            } else if (eventName.equals("dayClick")) {
				String day = params.get(clientId + "_dayClick");
                event = new ScheduleClickEvent((AjaxBehaviorEvent) event, "dayClick", day, "", null);
            } else if (eventName.equals("timeClick")) {
				String dayTime = params.get(clientId + "_timeClick");
				String day = dayTime.substring(0, dayTime.indexOf(" "));
				String time = dayTime.substring(dayTime.indexOf(" ") + 1);
                event = new ScheduleClickEvent((AjaxBehaviorEvent) event, "timeClick", day, time, null);
            } else if (eventName.equals("addEvent")) {
				ScheduleEvent scheduleEvent = ScheduleUtils.buildScheduleEventFromRequest(this, params, clientId);
                event = new ScheduleModifyEvent((AjaxBehaviorEvent) event, "addEvent", scheduleEvent, null);
            } else if (eventName.equals("editEvent")) {
				ScheduleEvent scheduleEvent = ScheduleUtils.buildScheduleEventFromRequest(this, params, clientId);
				ScheduleEvent oldScheduleEvent = ScheduleUtils.buildOldScheduleEventFromRequest(this, params, clientId);
                event = new ScheduleModifyEvent((AjaxBehaviorEvent) event, "editEvent", scheduleEvent, oldScheduleEvent);
            } else if (eventName.equals("deleteEvent")) {
				ScheduleEvent scheduleEvent = ScheduleUtils.buildScheduleEventFromRequest(this, params, clientId);
                event = new ScheduleModifyEvent((AjaxBehaviorEvent) event, "deleteEvent", scheduleEvent, null);
            } else if (eventName.equals("next")) {
				String startDate = params.get(clientId + "_startDate");
				String endDate = params.get(clientId + "_endDate");
                event = new ScheduleNavigationEvent((AjaxBehaviorEvent) event, "next", startDate, endDate);
            } else if (eventName.equals("previous")) {
				String startDate = params.get(clientId + "_startDate");
				String endDate = params.get(clientId + "_endDate");
                event = new ScheduleNavigationEvent((AjaxBehaviorEvent) event, "previous", startDate, endDate);
            }
        }

		super.queueEvent(event);
	}


	public boolean isLazy() {
		return getValue() instanceof LazyScheduleEventList;
	}

	protected void resetDataModel() {
		setDataModel(null);
		Object value = getValue();
		if (value instanceof LazyScheduleEventList) {
			LazyScheduleEventList lazyScheduleEventList = (LazyScheduleEventList) value;
			int[] lazyYearMonthValues = getLazyDateValues();
			List<ScheduleEvent> list = lazyScheduleEventList.load(lazyYearMonthValues[0], lazyYearMonthValues[1]);
			lazyScheduleEventList.setWrapped(list);
		}
		getDataModel();
	}

	public int[] getLazyDateValues() {
		int lazyYear = getLazyYear();
		int lazyMonth = getLazyMonth();
		int lazyDay = getLazyDay();
		String viewMode = getViewMode().toLowerCase();
		if (lazyYear == -1 || lazyMonth == -1 || lazyDay == -1) { // get current date values
			int[] values = new int[3];
			Calendar cal = Calendar.getInstance();
			if ("month".equals(viewMode)) {
				values[0] = cal.get(Calendar.YEAR);
				values[1] = cal.get(Calendar.MONTH);
				values[2] = 1;
			} else if ("week".equals(viewMode)) {
				// determine previous Sunday
				while (cal.get(Calendar.DAY_OF_WEEK ) != Calendar.SUNDAY) {
					cal.add(Calendar.DAY_OF_WEEK, -1);
				}
				values[0] = cal.get(Calendar.YEAR);
				values[1] = cal.get(Calendar.MONTH);
				values[2] = cal.get(Calendar.DATE);
			} else {
				values[0] = cal.get(Calendar.YEAR);
				values[1] = cal.get(Calendar.MONTH);
				values[2] = cal.get(Calendar.DATE);
			}
			return values;
		} else {
			int[] values = {lazyYear, lazyMonth, lazyDay};
			return values;
		}
	}

	public int[] getCurrentDateValues() { // TO_DO: read startWith attribute, if set return those values
		String viewMode = getViewMode().toLowerCase();
		int[] values = new int[3];
		Calendar cal = Calendar.getInstance();
		if ("month".equals(viewMode)) {
			values[0] = cal.get(Calendar.YEAR);
			values[1] = cal.get(Calendar.MONTH);
			values[2] = 1;
		} else if ("week".equals(viewMode)) {
			// determine previous Sunday
			while (cal.get(Calendar.DAY_OF_WEEK ) != Calendar.SUNDAY) {
				cal.add(Calendar.DAY_OF_WEEK, -1);
			}
			values[0] = cal.get(Calendar.YEAR);
			values[1] = cal.get(Calendar.MONTH);
			values[2] = cal.get(Calendar.DATE);
		} else {
			values[0] = cal.get(Calendar.YEAR);
			values[1] = cal.get(Calendar.MONTH);
			values[2] = cal.get(Calendar.DATE);
		}
		return values;
	}

	public void addEvent(ScheduleEvent scheduleEvent) {
		if (scheduleEvent == null) return;
		Object value = getValue();
		if (value instanceof List) {
			((List) value).add(scheduleEvent);
		} else if (Object[].class.isAssignableFrom(value.getClass())) {
			ScheduleEvent[] oldArray = (ScheduleEvent[]) value;
			ScheduleEvent[] newArray = new ScheduleEvent[oldArray.length+1];
			for (int i = 0; i < newArray.length; i++) newArray[i] = oldArray[i];
			newArray[newArray.length-1] = scheduleEvent;
			setValue(newArray);
		} else if (value instanceof Collection) {
			((Collection) value).add(scheduleEvent);
		}
	}

	public void editEvent(int index, ScheduleEvent scheduleEvent) {
		if (scheduleEvent == null) return;
		Object value = getValue();
		if (value instanceof List) {
			((List) value).set(index, scheduleEvent);
		} else if (Object[].class.isAssignableFrom(value.getClass())) {
			((ScheduleEvent[]) value)[index] = scheduleEvent;
		}
	}

	public void deleteEvent(int index) {
		Object value = getValue();
		if (value instanceof List) {
			((List) value).remove(index);
		} else if (Object[].class.isAssignableFrom(value.getClass())) {
			ScheduleEvent[] oldArray = (ScheduleEvent[]) value;
			ScheduleEvent[] newArray = new ScheduleEvent[oldArray.length-1];
			for (int i = 0; i < oldArray.length; i++) {
				if (i == index) continue;
				newArray[i > index ? i-1 : i] = oldArray[i];
			}
			setValue(newArray);
		}		
	}

	public void deleteEvent(ScheduleEvent scheduleEvent) {
		if (scheduleEvent == null) return;
		Object value = getValue();
		if (value instanceof Collection) {
			((Collection) value).remove(scheduleEvent);
		}
	}

    public TimeZone calculateTimeZone() {
        if (appropriateTimeZone == null) {
            Object usertimeZone = getTimeZone();
            if (usertimeZone != null) {
                if (usertimeZone instanceof String)
                    appropriateTimeZone = TimeZone.getTimeZone((String) usertimeZone);
                else if (usertimeZone instanceof TimeZone)
                    appropriateTimeZone = (TimeZone) usertimeZone;
                else
                    throw new IllegalArgumentException("The value for the 'timeZone' attribute can only be either of type String or of type java.util.TimeZone.");
            } else {
                appropriateTimeZone = TimeZone.getDefault();
            }
        }

        return appropriateTimeZone;
    }
}