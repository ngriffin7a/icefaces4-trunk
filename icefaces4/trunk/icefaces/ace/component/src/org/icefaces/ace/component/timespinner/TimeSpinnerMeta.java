/*
 * Copyright 2004-2013 ICEsoft Technologies Canada Corp.
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

package org.icefaces.ace.component.timespinner;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.baseMeta.UIInputMeta;
import org.icefaces.ace.meta.annotation.ClientBehaviorHolder;
import org.icefaces.ace.meta.annotation.ClientEvent;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;


@Component(
        tagName = "timeSpinner",
        componentClass = "org.icefaces.ace.component.timespinner.TimeSpinner",
        generatedClass = "org.icefaces.ace.component.timespinner.TimeSpinnerBase",
        extendsClass = "javax.faces.component.UIInput",
        rendererClass = "org.icefaces.ace.component.timespinner.TimeSpinnerRenderer",
        componentFamily = "org.icefaces.component.TimeSpinner",
        componentType = "org.icefaces.component.TimeSpinner",
        rendererType = "org.icefaces.component.TimeSpinnerRenderer",
        tlddoc = "TimeSpinner is an input component that provides a time input.")


@ResourceDependencies({
		@ResourceDependency(library = "org.icefaces.component.datespinner", name = "datespinner.css"),
		@ResourceDependency(library = "org.icefaces.component.util", name = "component.js"),
		@ResourceDependency(library = "org.icefaces.component.datespinner", name = "timespinner.js")
})
@ClientBehaviorHolder(events = {
	@ClientEvent(name="change", javadoc="Fires when a change is detected in the time spinner.",
            tlddoc="Fires when a change is detected in the time spinner.",
            defaultRender="@this", defaultExecute="@all")
}, defaultEvent="change")
public class TimeSpinnerMeta extends UIInputMeta {

    @Property(defaultValue = "hh:mm a", tlddoc = "The TimeFormat pattern used for localization.")
    private String pattern;

    @Property(defaultValue = "15", tlddoc="The width, in characters, of the input text field where the value of the date resides.")
    private String size;

    @Property(tlddoc = " The locale to be used for labels and conversion.")
    private Object locale;

    @Property(tlddoc = "A String or a java.util.TimeZone instance that specify the timezone used for date " +
            "conversion. Defaults to TimeZone.getDefault().")
    private Object timeZone;

     @Property(tlddoc = "Sets the CSS style definition to be applied to this component.")
     private String style;

     @Property(tlddoc = "Sets the CSS class to apply to this component.")
     private String styleClass;

    @Property(defaultValue = "false",
            tlddoc = "Disables this component, so it does not receive focus or get submitted.")
    private boolean disabled;

     @Property(tlddoc = "Sets this component to read only, so value cannot be changed.")
     private boolean readonly;

     @Property(tlddoc = "When singleSubmit is \"true\", triggering an action on " +
    		"this component will submit and execute only this component only (equivalent to" +
    		" <f:ajax execute='@this' render='@all'> ). When singleSubmit is \"false\", triggering an " +
    		"action on this component will submit and execute the full form that this component " +
    		"is contained within.")
     private boolean singleSubmit;

    @Property(defaultValue = "false", tlddoc = "Deterines if native time picker should be used when available.  Currently, native support is available for iOS5, iOS6, and BlackBerry devices.")
    private boolean useNative;

}
