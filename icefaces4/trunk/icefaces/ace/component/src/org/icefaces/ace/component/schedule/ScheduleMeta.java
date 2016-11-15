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
	@ICEResourceDependency(name = "schedule/moment.js"),
	@ICEResourceDependency(name = "schedule/clndr.js"),
    @ICEResourceDependency(name = "schedule/schedule.js")
})
@ClientBehaviorHolder(events = {
	@ClientEvent( name="eventClick",
		javadoc="",
		tlddoc="",
		defaultRender="@this", defaultExecute="@this" ),
	@ClientEvent( name="dayClick",
		javadoc="",
		tlddoc="",
		defaultRender="@this", defaultExecute="@this" ),
	@ClientEvent( name="timeClick",
		javadoc="",
		tlddoc="",
		defaultRender="@this", defaultExecute="@this" ),
	@ClientEvent( name="addEvent",
		javadoc="",
		tlddoc="",
		defaultRender="@this", defaultExecute="@this" ),
	@ClientEvent( name="editEvent",
		javadoc="",
		tlddoc="",
		defaultRender="@this", defaultExecute="@this" ),
	@ClientEvent( name="deleteEvent",
		javadoc="",
		tlddoc="",
		defaultRender="@this", defaultExecute="@this" ),
	@ClientEvent( name="next",
		javadoc="",
		tlddoc="",
		defaultRender="@this", defaultExecute="@this" ),
	@ClientEvent( name="previous",
		javadoc="",
		tlddoc="",
		defaultRender="@this", defaultExecute="@this" )},
	defaultEvent="eventClick" )
public class ScheduleMeta extends UIDataMeta {

	@Property(tlddoc = "The value should be a List, Array, DataModel or a type that can be adapted into a DataModel (java.sql.ResultSet, javax.servlet.jsp.jstl.sql.Result, and java.util.Collection). It must contain the  org.icefaces.ace.model.schedule.ScheduleEvent objects to be displayed on the schedule. Alternatively, the value can be an implementation of org.icefaces.ace.model.schedule.LazyScheduleEventList to work in a lazy-loading mode, month per month.")
	private Object value;

	@Property(tlddoc = "A Date object to specify the month and year with which this component will be loaded in the client. The day and time values are irrelevant for this attribute. If this attribute is not specified, the component will be loaded with the current month, according to the server time.")
	private Date startWithMonth;

	@Property(tlddoc = "Specifies the location of the sidebar or whether it should be hidden. Possible values are 'right', 'left', and 'hidden'.", defaultValue="right")
	private String sideBar;

	@Property(tlddoc = "Specifies where to display the event details after clicking on an event tag on the calendar. Possible values are 'sidebar', 'popup', 'tooltip', and 'disabled'.", defaultValue="sidebar")
	private String displayEventDetails;

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
    private boolean scrollable;;

	@Property(tlddoc = "The inline style of the component, rendered on the root div of the component.")
	private String style;

	@Property(tlddoc = "The CSS style class of the component, rendered on the root div of the component.")
	private String styleClass;

    @Property(tlddoc = "A time zone ID String (matching an element of java.util.TimeZone.getAvailableIDs()) or a java.util.TimeZone instance to specify the time zone used for date conversion to and from UTC time. If not specified, the default value is TimeZone.getDefault().")
    private Object timeZone;

	@Field(defaultValue="-1")
	private Integer lazyYear;

	@Field(defaultValue="-1")
	private Integer lazyMonth;

	@Field(defaultValue="-1")
	private Integer lazyDay;
}
