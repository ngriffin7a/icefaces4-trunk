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

package org.icefaces.mobi.component.flipswitch;

import org.icefaces.ace.meta.annotation.*;
import org.icefaces.ace.meta.annotation.ClientBehaviorHolder;
import org.icefaces.ace.meta.annotation.ClientEvent;

import org.icefaces.ace.meta.baseMeta.UISelectBooleanMeta;

import javax.el.MethodExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

@Component(
        tagName = "flipswitch",
        componentClass = "org.icefaces.mobi.component.flipswitch.FlipSwitch",
        rendererClass = "org.icefaces.mobi.component.flipswitch.FlipSwitchRenderer",
        generatedClass = "org.icefaces.mobi.component.flipswitch.FlipSwitchBase",
        extendsClass = "javax.faces.component.UISelectBoolean",
        componentType = "org.icesoft.faces.FlipSwitch",
        rendererType = "org.icesoft.faces.FlipSwitchRenderer",
        componentFamily = "org.icefaces.component.FlipSwitch",
        tlddoc = "The flipswitch provides a control that toggles between on and off states."                
)


@ResourceDependencies({
        @ResourceDependency(library = "org.icefaces.component.util", name = "component.js"),
		@ResourceDependency(library = "icefaces.ace", name = "util/ace-jquery.js"),
		@ResourceDependency(library = "org.icefaces.component.flipswitch", name = "flipswitch.js"),
		@ResourceDependency(library = "org.icefaces.component.flipswitch", name = "flip-switch.css")
})
@ClientBehaviorHolder(events = {
	@ClientEvent(name="activate", 
	        javadoc="Fired when flip switch has realized an onclick event.",
	        tlddoc="Fired when the the flip switch has realized an onclick event",
	        defaultRender="@all", defaultExecute="@this")
}, defaultEvent="activate")
public class FlipSwitchMeta extends UISelectBooleanMeta {

    @Property(tlddoc = "Sets the CSS style definition to be applied to this component.")
    private String style;

    @Property(defaultValue = "Integer.MIN_VALUE", tlddoc = "The tabindex of this component.")
    private int tabindex;

    @Property(tlddoc = "Disables this component, so it does not receive focus or get submitted.")
    private boolean disabled;

    @Property(tlddoc="Sets this component to read only, so value cannot be changed.")
    private boolean readonly;

    @Property(defaultValue="ON", tlddoc = "The label for the switch when \"true\". ")
    private String labelOn;
    
    @Property(defaultValue="OFF", tlddoc = "The label for the switch when \"false\". ")
    private String labelOff;    

	@Property(tlddoc ="The current value of the simple component. The value to be rendered.")
	private Boolean value;
}
