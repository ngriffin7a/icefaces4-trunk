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

package org.icefaces.ace.component.resizable;

import org.icefaces.ace.resources.ACEResourceNames;
import org.icefaces.resources.ICEResourceDependencies;
import org.icefaces.resources.ICEResourceDependency;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.annotation.Expression;
import org.icefaces.ace.meta.baseMeta.UIComponentBaseMeta;

import javax.el.MethodExpression;

import org.icefaces.ace.event.ResizeEvent;
import org.icefaces.ace.meta.annotation.ClientBehaviorHolder;
import org.icefaces.ace.meta.annotation.ClientEvent;
import org.icefaces.ace.api.IceClientBehaviorHolder;
import org.icefaces.resources.ICEResourceLibrary;

@Component(
        tagName         = "resizable",
        componentClass  = "org.icefaces.ace.component.resizable.Resizable",
        rendererClass   = "org.icefaces.ace.component.resizable.ResizableRenderer",
        generatedClass  = "org.icefaces.ace.component.resizable.ResizableBase",
        extendsClass    = "javax.faces.component.UIComponentBase",
        componentType   = "org.icefaces.ace.component.Resizable",
        rendererType    = "org.icefaces.ace.component.ResizableRenderer",
		componentFamily = "org.icefaces.ace.Resizable",
		tlddoc = "The Resizable is a component that makes another component resizable." +
                "<p>For more information, see the <a href=\"http://wiki.icefaces.org/display/ICE/Resizable\">Resizable Wiki Documentation</a>."
        )
@ICEResourceLibrary(ACEResourceNames.ACE_LIBRARY)
@ICEResourceDependencies({
    @ICEResourceDependency(name = ACEResourceNames.COMPONENTS_JS)
})
@ClientBehaviorHolder(events = {
	@ClientEvent(name="resize", javadoc="Fired at the end of a resize operation (default event).", tlddoc="Fired at the end of a resize operation (default event).", defaultRender="@all", defaultExecute="@this")
}, defaultEvent="resize")

public class ResizableMeta extends UIComponentBaseMeta {

	@Property(tlddoc="Name of the widget to access client-side API.")
	private String widgetVar;
	
	@Property(name="for", tlddoc="Specifies the id of the component to make resizable.")
	private String forValue;
	
	@Property(tlddoc="Boolean value that specifies whether the aspect ratio of the component should be maintained when resizing.", defaultValue="false")
	private boolean aspectRatio;
	
	@Property(tlddoc="If set to true, the CSS class \"ui-resizable-proxy\" will be added to the helper element used to outline the resize.", defaultValue="false")
	private boolean proxy;
	
	@Property(tlddoc="Specifies the handles to use. Possible values are 'n', 'e', 's', 'w', 'ne', 'se', 'sw', and 'sw' (without the quotes and separated by commas). Shortcut \"all\" enables all handles.")
	private String handles;
	
	@Property(tlddoc="If set to true, a semi-transparent helper element is shown for resizing.", defaultValue="false")
	private boolean ghost;
	
	@Property(tlddoc="Boolean value that specifies whether the resizing should be animated.", defaultValue="false")
	private boolean animate;
	
	@Property(tlddoc="Easing effect for animating. \"linear\" or \"swing\". Easing plugins or functions can add more effects.", defaultValue="swing")
	private String effect;
	
	@Property(tlddoc="Duration time for animating, in milliseconds. Other possible values: \"slow\", \"normal\", \"fast\".", defaultValue="normal")
	private String effectDuration;
	
	@Property(tlddoc="Maximum width of the resizable in pixels.", defaultValue="Integer.MAX_VALUE")
	private int maxWidth;
	
	@Property(tlddoc="Maximum height of the resizable in pixels.", defaultValue="Integer.MAX_VALUE")
	private int maxHeight;
	
	@Property(tlddoc="Minimum width of the resizable in pixels.", defaultValue="Integer.MIN_VALUE")
	private int minWidth;
	
	@Property(tlddoc="Minimum height of the resizable in pixels.", defaultValue="Integer.MIN_VALUE")
	private int minHeight;
	
	@Property(tlddoc="Boolean value that specifies whether the resizable should be restricted to its parent's boundaries.", defaultValue="false")
	private boolean containment;
	
	@Property(tlddoc="Size in pixels of the increments in which the resizable should increase/decrease its size", defaultValue="1")
	private int grid;
	
	@Property(expression= Expression.METHOD_EXPRESSION, methodExpressionArgument="org.icefaces.ace.event.ResizeEvent", tlddoc="Listener for the end of resize event.")
	private MethodExpression resizeListener;
    
}
