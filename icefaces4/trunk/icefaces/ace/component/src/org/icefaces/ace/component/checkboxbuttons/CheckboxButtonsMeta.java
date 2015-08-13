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

package org.icefaces.ace.component.checkboxbuttons;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.baseMeta.UISelectManyMeta;
import org.icefaces.ace.resources.ACEResourceNames;
import org.icefaces.resources.ICEResourceDependencies;
import org.icefaces.resources.ICEResourceDependency;
import org.icefaces.resources.ICEResourceLibrary;
import org.icefaces.ace.meta.annotation.ClientBehaviorHolder;
import org.icefaces.ace.meta.annotation.ClientEvent;

@Component(
        tagName = "checkboxButtons",
        componentClass = "org.icefaces.ace.component.checkboxbuttons.CheckboxButtons",
        rendererClass = "org.icefaces.ace.component.checkboxbuttons.CheckboxButtonsRenderer",
        generatedClass = "org.icefaces.ace.component.checkboxbuttons.CheckboxButtonsBase",
        extendsClass = "javax.faces.component.UISelectMany",
        componentType = "org.icefaces.ace.component.CheckboxButtons",
        rendererType = "org.icefaces.ace.component.CheckboxButtonsRenderer",
        componentFamily = "org.icefaces.ace.component.CheckboxButtons",
        tlddoc = "")
@ICEResourceLibrary(ACEResourceNames.ACE_LIBRARY)
@ICEResourceDependencies({
	@ICEResourceDependency(name = "fontawesome/font-awesome.css"),
	@ICEResourceDependency(name = "util/ace-core.js"),
	@ICEResourceDependency(name = "jquery/jquery.js"),
	@ICEResourceDependency(name = "checkboxbutton/checkboxbutton.js")
})
@ClientBehaviorHolder(events = {
	@ClientEvent(name="valueChange",
            javadoc="Fired when the value changes by checking/unchecking a checkbox.",
            tlddoc="Fired when the value changes by checking/unchecking a checkbox.",
            defaultRender="@all", defaultExecute="@this")
}, defaultEvent="valueChange")
public class CheckboxButtonsMeta extends UISelectManyMeta {
    @Property(tlddoc = "If true, only one button in the group can be selected at one time, selecting a new button deselects the previously selected one.", defaultValue = "false")
    private boolean mutuallyExclusive;

    @Property(tlddoc = "Header text for the button group.")
    private String header;

    @Property(tlddoc = "Style of the container element.")
    private String style;

    @Property(tlddoc = "Style class of the container element.")
    private String styleClass;

    @Property(tlddoc = "Message indicating that the user is required to provide a submitted value for this input component.")
    private String requiredIndicator;

    @Property(tlddoc = "Message indicating that the user is NOT required to provide a submitted value for this input component.")
    private String optionalIndicator;

    @Property(tlddoc = "Position of input-required or input-optional indicator relative to checkboxes. " +
            "Supported values are \"left/right/top/bottom/none\".", defaultValue="bottom")
    private String indicatorPosition;

    @Property(tlddoc = "Position of the individual checkbox labels relative to their input fields. Supported values are \"left/right/top/bottom/inField/none\".", defaultValue="none")
    private String labelPosition;

}
