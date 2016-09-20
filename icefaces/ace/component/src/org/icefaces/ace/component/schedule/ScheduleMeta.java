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

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.baseMeta.UIComponentBaseMeta;
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
        extendsClass = "javax.faces.component.UIComponentBase",
        componentFamily = "org.icefaces.Schedule",
        tlddoc = ""
)
@ICEResourceLibrary(ACEResourceNames.ACE_LIBRARY)
@ICEResourceDependencies({
	@ICEResourceDependency(name = "util/ace-core.js"),
	@ICEResourceDependency(name = "jquery/jquery.js"),
	@ICEResourceDependency(name = "util/ace-jquery-ui.js"),
	@ICEResourceDependency(name = "schedule/underscore.js"),
	@ICEResourceDependency(name = "schedule/moment.js"),
	@ICEResourceDependency(name = "schedule/clndr.js"),
    @ICEResourceDependency(name = "schedule/schedule.js")
})
public class ScheduleMeta extends UIComponentBaseMeta {

	@Property(tlddoc = "The value should be a List or an Array of org.icefaces.ace.model.schedule.ScheduleEvent objects to be displayed on the schedule. Alternatively, the value can be an instance of org.icefaces.ace.model.schedule.LazyScheduleModel to work in a lazy-loading mode.")
	private Object value;

	@Property(tlddoc = "The name of the template to use to render the component in the client. The valid values are 'full', 'mini', and 'custom'. Use 'custom' to define your own template in the 'customTemplate' attribute.", defaultValue="full")
	private String template;

	@Property(tlddoc = "This advanced feature allows to specify a string defining a custom Underscore.js template to render the component in the client. Note that, when using this advanced feature, it is also necessary to define the CSS styling for the template on the page, either inline or in an external CSS resource.")
	private String customTemplate;

	@Property(tlddoc = "A Date object to specify the month and year with which this component will be loaded in the client. The day and time values are irrelevant for this attribute. If this attribute is not specified, the component will be loaded with the current month, according to the server time.")
	private Date startWithMonth;

	@Property(tlddoc = "Specifies the location of the sidebar or whether it should be hidden. Possible values are 'right', 'left', and 'hidden'.", defaultValue="right")
	private String sideBar;

	@Property(tlddoc = "Specifies where to display the event details after clicking on an event tag on the calendar. Possible values are 'sidebar', 'popup', 'tooltip', and 'disabled'.", defaultValue="sidebar")
	private String displayEventDetails;

	@Property(tlddoc = "The inline style of the component, rendered on the root div of the component.")
	private String style;

	@Property(tlddoc = "The CSS style class of the component, rendered on the root div of the component.")
	private String styleClass;
}
