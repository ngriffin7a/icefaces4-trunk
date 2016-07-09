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
	@ICEResourceDependency(name = "schedule/underscore.js"),
	@ICEResourceDependency(name = "schedule/moment.js"),
	@ICEResourceDependency(name = "schedule/clndr.js"),
    @ICEResourceDependency(name = "schedule/schedule.css"),
    @ICEResourceDependency(name = "schedule/schedule.js")
})
public class ScheduleMeta extends UIComponentBaseMeta {

	@Property(tlddoc = "Temporary atribute. The value of this attribute is a JSON object with the configuration that the underlying library, CLNDR.js , takes to display an event calendar in the client. Eventually, this attribute will be removed, and more specific attributes will be added. The job of the Renderer class will be to translate the values of this component's attributes into a correct JSON object with the configuration that CLNDR.js will understand to build the event calendar on the client. There will also be a mechanism to avoid having to rebuild the entire component in the client whenever there's a slight configuration change.")
	private String configuration;
}

