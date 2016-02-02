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

package org.icefaces.ace.component.pushbutton;

import org.icefaces.ace.resources.ACEResourceNames;
import org.icefaces.resources.ICEResourceDependencies;
import org.icefaces.resources.ICEResourceDependency;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.baseMeta.UICommandMeta;
import org.icefaces.ace.meta.annotation.Property;

import org.icefaces.ace.meta.annotation.ClientBehaviorHolder;
import org.icefaces.ace.meta.annotation.ClientEvent;
import org.icefaces.ace.api.IceClientBehaviorHolder;
import org.icefaces.resources.ICEResourceLibrary;

  @Component(
        tagName         = "pushButton",
        componentClass  = "org.icefaces.ace.component.pushbutton.PushButton",
        rendererClass   = "org.icefaces.ace.component.pushbutton.PushButtonRenderer",
        generatedClass  = "org.icefaces.ace.component.pushbutton.PushButtonBase",
        extendsClass    = "javax.faces.component.UICommand",
        componentType   = "org.icefaces.ace.component.PushButton",
        rendererType    = "org.icefaces.ace.component.PushButtonRenderer",
		componentFamily = "org.icefaces.ace.PushButton",
	    tlddoc = "The Push Button is a component that allows entry of a complete form or just itself. " +
	         "It has the same functionality of a regular jsf command button " +
	         "but without having to add extra attributes." +
             "<p>For more information, see the <a href=\"http://wiki.icefaces.org/display/ICE/PushButton\">PushButton Wiki Documentation</a>."
        )
@ICEResourceLibrary(ACEResourceNames.ACE_LIBRARY)
@ICEResourceDependencies({
	@ICEResourceDependency(name = "util/ace-core.js"),
	@ICEResourceDependency(name = "jquery/jquery.js"),
	@ICEResourceDependency(name = "pushbutton/pushbutton.js")
})
@ClientBehaviorHolder(events = {
	@ClientEvent(name="action", javadoc="Fired when the button is clicked or pressed by any other means (default event).", tlddoc="Fired when the button is clicked or pressed by any other means (default event).", defaultRender="@all", defaultExecute="@this")
}, defaultEvent="action")
public class PushButtonMeta extends UICommandMeta {
    
    @Property(tlddoc="A localized user presentable name for this component.")
    private String label;

    @Property (defaultValue="false", tlddoc="If true, no input may be submitted via this component.")
    private boolean disabled;

    @Property (defaultValue="false", tlddoc="If true, no input may be submitted via this component when browser is offline.")
    private boolean offlineDisabled;

    @Property (tlddoc="Tabindex of the component.")
    private Integer tabindex;
  
    @Property(tlddoc="The CSS style class of the component, rendered on the root div of the component.")
    private String styleClass;  

    @Property(tlddoc="The inline style of the component, rendered on the root div of the component.")
    private String style;

    @Property (defaultValue="button", tlddoc="Specifies the button type. The possible values are 'button', 'submit', 'clear' and 'reset'. The 'button' type is the original and standard mode, where the button submits the form when activated. The 'submit' type lets the browser handle the request natively, without ICEfaces intervention (Note that if the component has a listener or an ajax event attached to it an additional request will be made, handled by ICEfaces). If the type is 'clear', the component acts in a special way: the button will act in a client-side mode, clearing all fields and components in the form it is contained in. If the type is 'reset', the component will reset all the input elements and components to their original values at the time they were loaded. The button will not submit the form in the 'clear' and 'reset' modes, unless the component has a listener or an ajax event attached to it.")
    private String type;
}
