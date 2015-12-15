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

package org.icefaces.ace.component.radiobuttons;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.baseMeta.UISelectOneMeta;
import org.icefaces.ace.resources.ACEResourceNames;
import org.icefaces.resources.ICEResourceDependencies;
import org.icefaces.resources.ICEResourceDependency;
import org.icefaces.resources.ICEResourceLibrary;
import org.icefaces.ace.meta.annotation.ClientBehaviorHolder;
import org.icefaces.ace.meta.annotation.ClientEvent;

@Component(
        tagName = "radioButtons",
        componentClass = "org.icefaces.ace.component.radiobuttons.RadioButtons",
        rendererClass = "org.icefaces.ace.component.radiobuttons.RadioButtonsRenderer",
        generatedClass = "org.icefaces.ace.component.radiobuttons.RadioButtonsBase",
        extendsClass = "javax.faces.component.UISelectOne",
        componentType = "org.icefaces.ace.component.RadioButtons",
        rendererType = "org.icefaces.ace.component.RadioButtonsRenderer",
        componentFamily = "org.icefaces.ace.component.RadioButtons",
        tlddoc = "")
@ICEResourceLibrary(ACEResourceNames.ACE_LIBRARY)
@ICEResourceDependencies({
	@ICEResourceDependency(name = "fontawesome/font-awesome.css"),
	@ICEResourceDependency(name = "util/ace-core.js"),
	@ICEResourceDependency(name = "jquery/jquery.js"),
	@ICEResourceDependency(name = "radiobutton/radiobutton.js")
})
@ClientBehaviorHolder(events = {
	@ClientEvent(name="valueChange",
            javadoc="Fired when the value changes by selecting/deselecting a radio button.",
            tlddoc="Fired when the value changes by selecting/deselecting a radio button.",
            defaultRender="@all", defaultExecute="@this")
}, defaultEvent="valueChange")
public class RadioButtonsMeta extends UISelectOneMeta {

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

    @Property(tlddoc = "Position of input-required or input-optional indicator relative to the radio buttons. " +
            "Supported values are \"left/right/top/bottom/none\".", defaultValue="bottom")
    private String indicatorPosition;

    @Property(tlddoc = "Position of the individual radio button labels relative to their input fields. Supported values are \"left/right/top/bottom/none\".", defaultValue="right")
    private String labelPosition;

}
