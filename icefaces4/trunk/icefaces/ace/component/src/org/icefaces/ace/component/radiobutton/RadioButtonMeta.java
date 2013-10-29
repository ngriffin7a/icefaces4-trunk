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

package org.icefaces.ace.component.radiobutton;


import org.icefaces.ace.meta.annotation.ClientBehaviorHolder;
import org.icefaces.ace.meta.annotation.ClientEvent;
import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.baseMeta.UISelectBooleanMeta;
import org.icefaces.ace.resources.ACEResourceNames;
import org.icefaces.resources.ICEResourceDependencies;
import org.icefaces.resources.ICEResourceDependency;
import org.icefaces.resources.ICEResourceLibrary;

@Component(
        tagName        = "radioButton",
        componentClass = "org.icefaces.ace.component.radiobutton.RadioButton",
        rendererClass  = "org.icefaces.ace.component.radiobutton.RadioButtonRenderer",
        generatedClass = "org.icefaces.ace.component.radiobutton.RadioButtonBase",
        extendsClass   = "javax.faces.component.UISelectBoolean",
        componentType  = "org.icefaces.ace.component.RadioButton",
        rendererType   = "org.icefaces.ace.component.RadioButtonRenderer",
		componentFamily= "org.icefaces.ace.RadioButton",
		tlddoc="The Radio Button is a component that allows entry of a button which "+
		       "supports browsers that see radio as true or false, "+
		       "yes or no, on or off. Once a radio button is selected, it can only be deselected by selecting a different radio button within the same button group. The Themeroller check icon will be displayed by default when the radio is checked. Override the .ui-icon-check class to display a different image." +
               "<p>For more information, see the <a href=\"http://wiki.icefaces.org/display/ICE/RadioButton\">RadioButton Wiki Documentation</a>."
        )
@ICEResourceLibrary(ACEResourceNames.ACE_LIBRARY)
@ICEResourceDependencies({
    @ICEResourceDependency(name=ACEResourceNames.COMBINED_CSS),
    @ICEResourceDependency(name=ACEResourceNames.JQUERY_JS),
    @ICEResourceDependency(name=ACEResourceNames.COMPONENTS_JS)
})
@ClientBehaviorHolder(events = {
	@ClientEvent(name="activate",
            javadoc="Fired when the button is clicked or pressed by any other means and changes to selected state (default event).",
            tlddoc="Fired when the button is clicked or pressed by any other means and changes to selected state (default event).",
            defaultRender="@all", defaultExecute="@all"), // defaultExecute is @all for updating other radio buttons
	@ClientEvent(name="deactivate",
            javadoc="Fired when the button is clicked or pressed by any other means and changes to not selected state.",
            tlddoc="Fired when the button is clicked or pressed by any other means and changes to not selected state.",
            defaultRender="@all", defaultExecute="@this")
}, defaultEvent="activate")
public class RadioButtonMeta extends UISelectBooleanMeta {

    @Property(tlddoc="Label to be displayed for the button.")
    private String label;
	
    @Property(tlddoc = "Position of label relative to the radio button. Supported values are \"left/right/top/bottom/none\". Default is \"none\".")
    private String labelPosition;

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
