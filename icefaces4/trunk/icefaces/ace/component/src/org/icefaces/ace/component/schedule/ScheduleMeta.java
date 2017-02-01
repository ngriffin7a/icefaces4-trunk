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

import org.icefaces.ace.meta.annotation.ClientBehaviorHolder;
import org.icefaces.ace.meta.annotation.ClientEvent;
import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Field;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.annotation.DefaultValueType;

import org.icefaces.ace.meta.baseMeta.UIDataMeta;
import org.icefaces.ace.resources.ACEResourceNames;

import org.icefaces.resources.ICEResourceDependencies;
import org.icefaces.resources.ICEResourceDependency;
import org.icefaces.resources.ICEResourceLibrary;

import java.util.Date;

@Component(
        tagName = "schedule",
        componentClass = "org.icefaces.ace.component.schedule.Schedule",
        rendererClass = "org.icefaces.ace.component.schedule.ScheduleRenderer",
        generatedClass = "org.icefaces.ace.component.schedule.ScheduleBase",
        componentType = "org.icefaces.Schedule",
        rendererType = "org.icefaces.ScheduleRenderer",
        extendsClass = "javax.faces.component.UIData",
        componentFamily = "org.icefaces.ace.Schedule",
        tlddoc = "A calendar with month, week, and day views that displays events and information about them. The dates must be stored internally in UTC time. They are converted to the time zone specified by the 'timeZone' attribute to display to the user. The class org.icefaces.ace.component.schedule.ScheduleUtils contains utility functions to help with conversion."
)
@ICEResourceLibrary(ACEResourceNames.ACE_LIBRARY)
@ICEResourceDependencies({
	@ICEResourceDependency(name = "fontawesome/font-awesome.css"),
	@ICEResourceDependency(name = "util/ace-core.js"),
	@ICEResourceDependency(name = "jquery/jquery.js"),
	@ICEResourceDependency(name = "util/ace-jquery-ui.js"),
    @ICEResourceDependency(name = "schedule/schedule.js")
})
@ClientBehaviorHolder(events = {
	@ClientEvent( name="eventClick",
		javadoc="Fired when the user clicks on an event in the schedule. The listener method for this event can take an event object of the type org.icefaces.ace.event.ScheduleClickEvent.",
		tlddoc="Fired when the user clicks on an event in the schedule. The listener method for this event can take an event object of the type org.icefaces.ace.event.ScheduleClickEvent.",
		defaultRender="@this", defaultExecute="@this" ),
	@ClientEvent( name="dayDblclick",
		javadoc="Fired when the user does double click on a blank area of a day in the month view, which would trigger a dialog to add a new event on that day. The listener method for this event can take an event object of the type org.icefaces.ace.event.ScheduleClickEvent.",
		tlddoc="Fired when the user does double click on a blank area of a day in the month view, which would trigger a dialog to add a new event on that day. The listener method for this event can take an event object of the type org.icefaces.ace.event.ScheduleClickEvent.",
		defaultRender="@this", defaultExecute="@this" ),
	@ClientEvent( name="timeDblclick",
		javadoc="Fired when the user does double click on a blank area of a time cell in the grid of the week and day views, which would trigger a dialog to add a new event starting at that time on that day. The listener method for this event can take an event object of the type org.icefaces.ace.event.ScheduleClickEvent.",
		tlddoc="Fired when the user does double click on a blank area of a time cell in the grid of the week and day views, which would trigger a dialog to add a new event starting at that time on that day. The listener method for this event can take an event object of the type org.icefaces.ace.event.ScheduleClickEvent.",
		defaultRender="@this", defaultExecute="@this" ),
	@ClientEvent( name="addEvent",
		javadoc="Fired when the user clicks on the 'Add' button in the Event Details dialog to add a new event to the schedule. The listener method for this event can take an event object of the type org.icefaces.ace.event.ScheduleModifyEvent.",
		tlddoc="Fired when the user clicks on the 'Add' button in the Event Details dialog to add a new event to the schedule. The listener method for this event can take an event object of the type org.icefaces.ace.event.ScheduleModifyEvent.",
		defaultRender="@this", defaultExecute="@this" ),
	@ClientEvent( name="editEvent",
		javadoc="Fired when the user clicks on the 'Save' button in the Event Details dialog to save the changes of an existing event in the schedule. The listener method for this event can take an event object of the type org.icefaces.ace.event.ScheduleModifyEvent.",
		tlddoc="Fired when the user clicks on the 'Save' button in the Event Details dialog to save the changes of an existing event in the schedule. The listener method for this event can take an event object of the type org.icefaces.ace.event.ScheduleModifyEvent.",
		defaultRender="@this", defaultExecute="@this" ),
	@ClientEvent( name="deleteEvent",
		javadoc="Fired when the user clicks on the 'Yes' button after having clicked on the 'Delete' button in the Event Details dialog to delete an existing event from the schedule The listener method for this event can take an event object of the type org.icefaces.ace.event.ScheduleModifyEvent.",
		tlddoc="Fired when the user clicks on the 'Yes' button after having clicked on the 'Delete' button in the Event Details dialog to delete an existing event from the schedule. The listener method for this event can take an event object of the type org.icefaces.ace.event.ScheduleModifyEvent.",
		defaultRender="@this", defaultExecute="@this" ),
	@ClientEvent( name="next",
		javadoc="Fired when the user clicks on the right arrow of the schedule viewer to navigate to the next month, week or day. The listener method for this event can take an event object of the type org.icefaces.ace.event.ScheduleNavigationEvent.",
		tlddoc="Fired when the user clicks on the right arrow of the schedule viewer to navigate to the next month, week or day. The listener method for this event can take an event object of the type org.icefaces.ace.event.ScheduleNavigationEvent.",
		defaultRender="@this", defaultExecute="@this" ),
	@ClientEvent( name="previous",
		javadoc="Fired when the user clicks on the left arrow of the schedule viewer to navigate to the previous month, week or day. The listener method for this event can take an event object of the type org.icefaces.ace.event.ScheduleNavigationEvent.",
		tlddoc="Fired when the user clicks on the left arrow of the schedule viewer to navigate to the previous month, week or day. The listener method for this event can take an event object of the type org.icefaces.ace.event.ScheduleNavigationEvent.",
		defaultRender="@this", defaultExecute="@this" )},
	defaultEvent="eventClick" )
public class ScheduleMeta extends UIDataMeta {

	@Property(tlddoc = "The value should be a List, Array, DataModel or a type that can be adapted into a DataModel (java.sql.ResultSet, javax.servlet.jsp.jstl.sql.Result, and java.util.Collection). It must contain the org.icefaces.ace.model.schedule.ScheduleEvent objects to be displayed on the schedule. Alternatively, the value can be an implementation of org.icefaces.ace.model.schedule.LazyScheduleEventList to work in a lazy-loading mode.")
	private Object value;

	@Property(tlddoc = "A Date object specifying the date that is currently displayed in the client. This attribute can be used to set an inital date to display or to programmatically set a date to display. This attribute will be automatically updated with the current date being displayed in the client as the user interacts with the component. If the current view mode is set to 'month', this Date object will be automatically changed to the first day of the given month. If the current view mode is set to 'week', this Date object will be automatically changed to the previous Sunday, when the week starts. If the current view mode is set to 'day', this Date object will not change. The time values are irrelevant for this attribute. If this attribute is not specified, the current date is going to be used. This Date object is assumed to be in the time zone specified by the 'timeZone' attribute.")
	private Date currentDate;

	@Property(tlddoc = "Specifies the location of the sidebar or whether it should be hidden. Possible values are 'right', 'left', and 'hidden'.", defaultValue="right")
	private String sideBar;

	@Property(tlddoc = "Specifies where to display the event details after clicking on an event tag on the calendar. Possible values are 'sidebar', 'popup', and 'disabled'.", defaultValue="popup")
	private String eventDetails;

	@Property(tlddoc = "Specifies whether to display a tooltip next to an event, containing the event's detailed information.", defaultValue="false")
	private boolean displayTooltip;

	@Property(tlddoc = "Enable or disable the built-in event addition controls. These controls appear in a popup when clicking on an empty area of a day square. Adding new events this way is only supported if the component value is an instance of any of the following four types: Array, List, Collection, and org.icefaces.ace.model.schedule.LazyScheduleEventList.", defaultValue="enabled")
	private String additionControls;

	@Property(tlddoc = "Enable or disable the built-in event editing controls. These controls only appear in the popup and sidebar event details view. Modifying events this way is only supported if the component value is an instance of any of the following three types: Array, List, and org.icefaces.ace.model.schedule.LazyScheduleEventList.", defaultValue="enabled")
	private String editingControls;

	@Property(tlddoc = "Enable or disable the built-in event deletion controls. These controls only appear in the popup and sidebar event details view. Deleting events this way is only supported if the component value is an instance of any of the following four types: Array, List, Collection, and org.icefaces.ace.model.schedule.LazyScheduleEventList.", defaultValue="enabled")
	private String deletionControls;

	@Property(tlddoc = "Specifies the range of days that should be displayed at a time in the calendar. Possible values are 'month', 'week', 'day'.", defaultValue="month")
	private String viewMode;

    @Property(tlddoc = "Defines a fixed height for the scrollable time grid in pixels.",
            defaultValue = "600", defaultValueType = DefaultValueType.EXPRESSION)
    private Integer scrollHeight;

    @Property(tlddoc = "Enabling renders the time grid of the week and day views in a container that overflows the fixed height and adds a scrollbar.")
    private boolean scrollable;

	@Property(tlddoc = "The inline style of the component, rendered on the root div of the component.")
	private String style;

	@Property(tlddoc = "The CSS style class of the component, rendered on the root div of the component.")
	private String styleClass;

    @Property(tlddoc = "A time zone ID String (matching an element of java.util.TimeZone.getAvailableIDs()) or a java.util.TimeZone instance to specify the time zone used for date conversion to and from UTC time. If not specified, the default value is TimeZone.getDefault(), which is the default time zone on the system.")
    private Object timeZone;

    @Property(tlddoc = "Defines the default duration (in minutes) of new events added by the user if an end date or time was not set.", defaultValue = "60", defaultValueType = DefaultValueType.EXPRESSION)
    private Integer defaultDuration;

    @Property(tlddoc = "Enabling renders the times in the 12-hour clock format.", defaultValue = "false")
    private boolean twelveHourClock;

	@Field(defaultValue="-1")
	private Integer currentYear;

	@Field(defaultValue="-1")
	private Integer currentMonth;

	@Field(defaultValue="-1")
	private Integer currentDay;
}
