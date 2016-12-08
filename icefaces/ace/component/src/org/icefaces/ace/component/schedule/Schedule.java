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

import java.io.Serializable;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class Schedule extends ScheduleBase implements Serializable {

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
            } else if (eventName.equals("dayDblclick")) {
				String day = params.get(clientId + "_dayDblclick");
                event = new ScheduleClickEvent((AjaxBehaviorEvent) event, "dayDblclick", day, "", null);
            } else if (eventName.equals("timeDblclick")) {
				String dayTime = params.get(clientId + "_timeDblclick");
				String day = dayTime.substring(0, dayTime.indexOf(" "));
				String time = dayTime.substring(dayTime.indexOf(" ") + 1);
                event = new ScheduleClickEvent((AjaxBehaviorEvent) event, "timeDblclick", day, time, null);
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
			Date [] lazyDateRange = getLazyDateRange();
			TimeZone timeZone = calculateTimeZone();
			List<ScheduleEvent> list = lazyScheduleEventList.load(
				ScheduleUtils.toUTCFromTimeZone(lazyDateRange[0], timeZone), 
				ScheduleUtils.toUTCFromTimeZone(lazyDateRange[1], timeZone));
			lazyScheduleEventList.setWrapped(list);
		}
		getDataModel();
	}

	public Date[] getLazyDateRange() {
		int currentYear = getCurrentYear();
		int currentMonth = getCurrentMonth();
		int currentDay = getCurrentDay();
		String viewMode = getViewMode();
		viewMode = viewMode != null ? viewMode.toLowerCase() : "month";

		// get start date values
		int[] startValues = new int[3];
		Calendar cal = Calendar.getInstance(calculateTimeZone()); // right now
		if (currentYear != -1 && currentMonth != -1 && currentDay != -1) {
			cal.set(Calendar.YEAR, currentYear);
			cal.set(Calendar.MONTH, currentMonth);
			cal.set(Calendar.DATE, currentDay);
		}
		if ("day".equals(viewMode)) {
			startValues[0] = cal.get(Calendar.YEAR);
			startValues[1] = cal.get(Calendar.MONTH);
			startValues[2] = cal.get(Calendar.DATE);
		} else if ("week".equals(viewMode)) {
			// determine previous Sunday
			while (cal.get(Calendar.DAY_OF_WEEK ) != Calendar.SUNDAY) {
				cal.add(Calendar.DAY_OF_WEEK, -1);
			}
			startValues[0] = cal.get(Calendar.YEAR);
			startValues[1] = cal.get(Calendar.MONTH);
			startValues[2] = cal.get(Calendar.DATE);
		} else {
			startValues[0] = cal.get(Calendar.YEAR);
			startValues[1] = cal.get(Calendar.MONTH);
			startValues[2] = 1;
		}

		// get end date values
		int[] endValues = new int[3];
		if ("day".equals(viewMode)) {
			endValues[0] = startValues[0];
			endValues[1] = startValues[1];
			endValues[2] = startValues[2];
		} else if ("week".equals(viewMode)) {
			int[] lastDayOfWeek = ScheduleUtils.determineLastDayOfWeek(startValues[0], startValues[1], startValues[2]);
			endValues[0] = lastDayOfWeek[0];
			endValues[1] = lastDayOfWeek[1];
			endValues[2] = lastDayOfWeek[2];
		} else {
			endValues[0] = startValues[0];
			endValues[1] = startValues[1];
			endValues[2] = ScheduleUtils.determineLastDayOfMonth(startValues[0], startValues[1]);
		}

		Date [] dateRange = new Date[2];
		dateRange[0] = ScheduleUtils.getDateFromIntegerValues(new ScheduleUtils.DateIntegerValues(startValues[0],
			startValues[1], startValues[2], 0, 0, 0));
		dateRange[1] = ScheduleUtils.getDateFromIntegerValues(new ScheduleUtils.DateIntegerValues(endValues[0],
			endValues[1], endValues[2], 23, 59, 59));
		return dateRange;
	}

	public int[] getCurrentDateValues() {
		int currentYear = getCurrentYear();
		int currentMonth = getCurrentMonth();
		int currentDay = getCurrentDay();
		String viewMode = getViewMode();
		viewMode = viewMode != null ? viewMode.toLowerCase() : "month";
		if (currentYear == -1 || currentMonth == -1 || currentDay == -1) { // get current date values
			int[] values = new int[3];
			Calendar cal = Calendar.getInstance(calculateTimeZone()); // right now
			if ("day".equals(viewMode)) {
				values[0] = cal.get(Calendar.YEAR);
				values[1] = cal.get(Calendar.MONTH);
				values[2] = cal.get(Calendar.DATE);
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
				values[2] = 1;
			}
			return values;
		} else {
			int[] values = new int[3];
			if ("day".equals(viewMode)) {
				values[0] = currentYear;
				values[1] = currentMonth;
				values[2] = currentDay;
			} else if ("week".equals(viewMode)) {
				// determine previous Sunday
				Calendar cal = Calendar.getInstance();
				cal.set(Calendar.YEAR, currentYear);
				cal.set(Calendar.MONTH, currentMonth);
				cal.set(Calendar.DATE, currentDay);
				cal.set(Calendar.HOUR_OF_DAY, 0);
				cal.set(Calendar.MINUTE, 0);
				cal.set(Calendar.SECOND, 0);
				while (cal.get(Calendar.DAY_OF_WEEK ) != Calendar.SUNDAY) {
					cal.add(Calendar.DAY_OF_WEEK, -1);
				}
				values[0] = cal.get(Calendar.YEAR);
				values[1] = cal.get(Calendar.MONTH);
				values[2] = cal.get(Calendar.DATE);
			} else {
				values[0] = currentYear;
				values[1] = currentMonth;
				values[2] = 1;
			}
			return values;
		}
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