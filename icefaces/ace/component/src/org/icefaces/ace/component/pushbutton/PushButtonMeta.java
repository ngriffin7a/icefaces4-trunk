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
    @ICEResourceDependency(name = ACEResourceNames.COMPONENTS_JS)
})
@ClientBehaviorHolder(events = {
	@ClientEvent(name="activate", javadoc="Fired when the button is clicked or pressed by any other means (default event).", tlddoc="Fired when the button is clicked or pressed by any other means (default event).", defaultRender="@all", defaultExecute="@all")
}, defaultEvent="activate")
public class PushButtonMeta extends UICommandMeta {
    
    @Property(tlddoc="A localized user presentable name for this component.")
    private String label;
	
    @Property (defaultValue="false", tlddoc="If true, no input may be submitted via this component.")
    private boolean disabled;
    
    @Property (tlddoc="Tabindex of the component.")
    private Integer tabindex;
  
    @Property(tlddoc="The CSS style class of the component, rendered on the root div of the component.")
    private String styleClass;  

    @Property(tlddoc="The inline style of the component, rendered on the root div of the component.")
    private String style;
}
