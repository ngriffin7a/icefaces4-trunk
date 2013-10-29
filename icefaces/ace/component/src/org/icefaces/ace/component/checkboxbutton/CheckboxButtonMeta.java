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

package org.icefaces.ace.component.checkboxbutton;


import org.icefaces.ace.resources.ACEResourceNames;
import org.icefaces.resources.ICEResourceDependencies;
import org.icefaces.resources.ICEResourceDependency;

import org.icefaces.ace.meta.baseMeta.UISelectBooleanMeta;
import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.annotation.ClientBehaviorHolder;
import org.icefaces.ace.meta.annotation.ClientEvent;
import org.icefaces.resources.ICEResourceLibrary;

@Component(
        tagName        = "checkboxButton",
        componentClass = "org.icefaces.ace.component.checkboxbutton.CheckboxButton",
        rendererClass  = "org.icefaces.ace.component.checkboxbutton.CheckboxButtonRenderer",
        generatedClass = "org.icefaces.ace.component.checkboxbutton.CheckboxButtonBase",
        extendsClass   = "javax.faces.component.UISelectBoolean",
        componentType  = "org.icefaces.ace.component.CheckboxButton",
        rendererType   = "org.icefaces.ace.component.CheckboxButtonRenderer",
		componentFamily= "org.icefaces.ace.CheckboxButton",
		tlddoc="The Checkbox Button is a component that allows entry of a button which "+
		       "supports browsers that see checkbox as true or false, "+
		       "yes or no, on or off. The Themeroller check icon will be displayed by default when the checkbox is checked. Override the .ui-icon-check class to display a different image." +
               "<p>For more information, see the <a href=\"http://wiki.icefaces.org/display/ICE/CheckboxButton\">CheckboxButton Wiki Documentation</a>."
        )
@ICEResourceLibrary(ACEResourceNames.ACE_LIBRARY)
@ICEResourceDependencies({
    @ICEResourceDependency(name=ACEResourceNames.COMPONENTS_JS)
})
@ClientBehaviorHolder(events = {
	@ClientEvent(name="activate",
            javadoc="Fired when the button is clicked or pressed by any other means (default event).",
            tlddoc="Fired when the button is clicked or pressed by any other means (default event).",
            defaultRender="@all", defaultExecute="@this")
}, defaultEvent="activate")
public class CheckboxButtonMeta extends UISelectBooleanMeta {

    @Property(tlddoc="Label to be displayed on the button.")
    private String label;

/*    @Property(defaultValue="left",
    		tlddoc="Default is left for rime theme. Other possibility is \"on\" " +
    				"for sam skin.")
    private String labelPosition; */

    @Property(tlddoc="The inline style of the component, rendered on the root div of the component.")
	private String style;

    @Property(tlddoc="The CSS style class of the component, rendered on the root div of the component.")
	private String styleClass;

    @Property (tlddoc="Tabindex of the component.")
    private Integer tabindex;

    @Property (defaultValue="false",
    		tlddoc="If true no input may be submitted via this component.")
    private boolean disabled;

    @Property(tlddoc = "ID of button group component if this button is outside of the button group but wants to logically belong to the group.")
    private String group;
}
