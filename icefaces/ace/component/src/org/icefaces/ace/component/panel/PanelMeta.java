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

package org.icefaces.ace.component.panel;

import org.icefaces.ace.resources.ACEResourceNames;
import org.icefaces.resources.ICEResourceDependencies;
import org.icefaces.resources.ICEResourceDependency;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.annotation.Field;
import org.icefaces.ace.meta.baseMeta.UIPanelMeta;
import org.icefaces.ace.meta.annotation.ClientBehaviorHolder;
import org.icefaces.ace.meta.annotation.ClientEvent;
import org.icefaces.ace.api.IceClientBehaviorHolder;
import org.icefaces.resources.ICEResourceLibrary;

@Component(
        tagName         = "panel",
        componentClass  = "org.icefaces.ace.component.panel.Panel",
        rendererClass   = "org.icefaces.ace.component.panel.PanelRenderer",
        generatedClass  = "org.icefaces.ace.component.panel.PanelBase",
        extendsClass    = "javax.faces.component.UIPanel",
        componentType   = "org.icefaces.ace.component.Panel",
        rendererType    = "org.icefaces.ace.component.PanelRenderer",
		componentFamily = "org.icefaces.ace.Panel",
		tlddoc = "Panel is a generic grouping component that also supports toggling, closing and options menu. Both Close and Toggle events can be listened on server side with ajax listeners." +
                 "<p>For more information, see the " +
                 "<a href=\"http://wiki.icefaces.org/display/ICE/Panel\">Panel Wiki Documentation</a>."
        )
@ICEResourceLibrary(ACEResourceNames.ACE_LIBRARY)
@ICEResourceDependencies({
    @ICEResourceDependency(name = ACEResourceNames.COMPONENTS_JS)
})
@ClientBehaviorHolder(events = {
	@ClientEvent(name="toggle", javadoc="Fired when the toggle button is activated to expand or compress the panel (default event).",
	    tlddoc="Fired when the toggle button is activated to expand or collapse the panel (default event).",
        defaultRender="@all",
        defaultExecute="@this",
        argumentClass="org.icefaces.ace.event.ToggleEvent"),

    @ClientEvent(name="close",
        javadoc="Fired when the close button is activated to remove the panel from view.",
        tlddoc="Fired when the close button is activated to remove the panel from view.",
        defaultRender="@all",
        defaultExecute="@this",
        argumentClass="org.icefaces.ace.event.CloseEvent")
}, defaultEvent="toggle")

public class PanelMeta extends UIPanelMeta {

	@Property(tlddoc="Name of the widget variable to access the client side api.")
	private String widgetVar;
	
	@Property(tlddoc="Header text of the panel.")
	private String header;
	
	@Property(tlddoc="Footer text of the panel.")
	private String footer;
	
	@Property(tlddoc="Boolean value that specifies whether to display a button to toggle between the expanded and collapsed states of the panel.", defaultValue="false")
	private boolean toggleable;
	
	@Property(tlddoc="Integer value that specifies the duration of the transition between states.", defaultValue="1000")
	private int toggleSpeed;
	
	@Property(tlddoc="Style to apply to the container element.")
	private String style;
	
	@Property(tlddoc="Style class to apply to the container element.")
	private String styleClass;
	
	@Property(tlddoc="Boolean value that specifies whether the panel is collapsed.", defaultValue="false")
	private boolean collapsed;
	
	@Property(tlddoc="Boolean value that specifies whether the panel has a close button.", defaultValue="false")
	private boolean closable;
	
	@Property(tlddoc="Integer value that specifies the duration of the closing animation.", defaultValue="1000")
	private int closeSpeed;
	
	@Property(tlddoc="Boolean value that specifies whether the panel is visible.", defaultValue="true")
	private boolean visible;

	@Property(tlddoc="Boolean value that specifies whether all input elements inside this panel should be disabled or not. This doesn't affect the children components' internal state; this simply disables all input elements in the client side, in order to avoid submitting their values in requests to the server. (Note: some components might not look different to the user when disabled via this option, but their values will not be submitted to the server nonetheless.)", defaultValue="false")
	private boolean disableInputs;    
	
	@Field
	private Boolean previousDisableInputs;
}
