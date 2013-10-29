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

package org.icefaces.ace.component.datespinner;

import org.icefaces.ace.meta.annotation.ClientBehaviorHolder;
import org.icefaces.ace.meta.annotation.ClientEvent;
import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.baseMeta.UIInputMeta;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

@Component(
        tagName = "dateSpinner",
        componentClass = "org.icefaces.ace.component.datespinner.DateSpinner",
        generatedClass = "org.icefaces.ace.component.datespinner.DateSpinnerBase",
        extendsClass = "javax.faces.component.UIInput",
        rendererClass = "org.icefaces.ace.component.datespinner.DateSpinnerRenderer",
        componentFamily = "org.icefaces.component.DateSpinner",
        componentType = "org.icefaces.component.DateSpinner",
        rendererType = "org.icefaces.component.DateSpinnerRenderer",
        tlddoc = "DateSpinner is an input component used for choosing a date.")

@ResourceDependencies({
		@ResourceDependency(library = "org.icefaces.component.datespinner", name = "datespinner.css"),
		@ResourceDependency(library = "org.icefaces.component.util", name = "component.js"),
		@ResourceDependency(library = "org.icefaces.component.datespinner", name = "datespinner.js")
})
@ClientBehaviorHolder(events = {
        @ClientEvent(name = "change", javadoc = "Fired when a change is detected from date selection.",
                tlddoc = "Fired when a change is detected in date selection.",
                defaultRender = "@this", defaultExecute = "@all")
}, defaultEvent = "change")
public class DateSpinnerMeta extends UIInputMeta {

    @Property(defaultValue = "yyyy-MM-dd", tlddoc = "The DateFormat pattern.")
    private String pattern;

    @Property(defaultValue = "1980", tlddoc = "The first year to appear in the dateScroller.")
    private int yearStart;

    @Property(defaultValue = "2020", tlddoc = "The last year to appear in the dateScroller.")
    private int yearEnd;

    @Property(defaultValue = "15", tlddoc = "Width, in characters, of the input text field string containing the value of the selected date.")
    private String size;

    @Property(tlddoc = "The locale to be used for labels and conversion.")
    private Object locale;

    @Property(tlddoc = "A String or a java.util.TimeZone instance specify the timezone used for time conversion." +
            " Defaults to TimeZone.getDefault().")
    private Object timeZone;

    @Property(tlddoc = "Sets the CSS style definition to be applied to this component.")
    private String style;

    @Property(tlddoc = "Sets the CSS class to apply to this component.")
    private String styleClass;

    @Property(defaultValue = "false",tlddoc = "Disables this component, so it does not receive focus or get submitted.")
    private boolean disabled;

    @Property(tlddoc = "Sets this component to read only, so value cannot be changed.")
    private boolean readonly;

    @Property(tlddoc = "The tabindex of this component.")
    private boolean singleSubmit;

    @Property(defaultValue = "false", tlddoc = "Determines if native date picker should be used when available.  Currently, native support is available for iOS5, iOS6, and BlackBerry devices.")
    private boolean useNative;

}
