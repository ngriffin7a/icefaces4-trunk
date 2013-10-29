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

package org.icefaces.ace.component.printer;

import org.icefaces.ace.meta.annotation.ClientBehaviorHolder;
import org.icefaces.ace.meta.annotation.ClientEvent;
import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.annotation.Field;
import org.icefaces.ace.meta.baseMeta.UIComponentBaseMeta;
import org.icefaces.ace.resources.ACEResourceNames;
import org.icefaces.resources.ICEResourceDependencies;
import org.icefaces.resources.ICEResourceDependency;
import org.icefaces.resources.ICEResourceLibrary;

@Component(
        tagName         = "printer",
        componentClass  = "org.icefaces.ace.component.printer.Printer",
        rendererClass   = "org.icefaces.ace.component.printer.PrinterRenderer",
        generatedClass  = "org.icefaces.ace.component.printer.PrinterBase",
        extendsClass    = "javax.faces.component.UIComponentBase",
        componentType   = "org.icefaces.ace.component.Printer",
        rendererType    = "org.icefaces.ace.component.PrinterRenderer",
		componentFamily = "org.icefaces.ace.component",
		tlddoc = "Printer allows sending a specific JSF component to the printer, not the whole page. It needs to be nested inside an h:commandButton or h:outputLink component." +
                 "<p>For more information, see the " +
                 "<a href=\"http://wiki.icefaces.org/display/ICE/Printer\">Printer Wiki Documentation</a>."
        )
@ICEResourceLibrary(ACEResourceNames.ACE_LIBRARY)
@ICEResourceDependencies({
    @ICEResourceDependency(name = ACEResourceNames.COMPONENTS_JS)
})
@ClientBehaviorHolder(events = {
	@ClientEvent(name="activate", javadoc="Fired when the parent component is clicked or activated via the keyboard (default event).", tlddoc="Fired when the parent component is clicked or activated via the keyboard (default event).", defaultRender="@all", defaultExecute="@all")
}, defaultEvent="activate")
public class PrinterMeta extends UIComponentBaseMeta {

	@Property(name="for", tlddoc="Specifies the id of the component to print.")
	private String forValue;
	
	@Property(tlddoc="If an ajax behavior is attached to this component, the print interface will appear whether validation failed or not for the ajax request fi this attribute is set to true. Otherwise, the printing action will be halted.", defaultValue="true")
	private boolean ignoreValidation;
	
	@Field(defaultValue="false")
	private Boolean passedValidation;
}
