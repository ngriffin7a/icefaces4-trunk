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

package org.icefaces.ace.component.dnd;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Expression;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.baseMeta.UIComponentBaseMeta;

import javax.el.MethodExpression;
import java.lang.reflect.Method;
import org.icefaces.ace.meta.annotation.ClientBehaviorHolder;
import org.icefaces.ace.meta.annotation.ClientEvent;
import org.icefaces.ace.api.IceClientBehaviorHolder;

import org.icefaces.ace.resources.ACEResourceNames;
import org.icefaces.resources.ICEResourceDependencies;
import org.icefaces.resources.ICEResourceDependency;
import org.icefaces.resources.ICEResourceLibrary;

@Component(
    tagName = "droppable",
    componentClass = "org.icefaces.ace.component.dnd.Droppable",
    generatedClass = "org.icefaces.ace.component.dnd.DroppableBase",
    rendererClass = "org.icefaces.ace.component.dnd.DroppableRenderer",
    extendsClass = "javax.faces.component.UIComponentBase",
    componentType = "org.icefaces.ace.component.Droppable",
    rendererType = "org.icefaces.ace.component.DroppableRenderer",
    componentFamily = "org.icefaces.ace.Droppable",
    tlddoc = "Allows a specified component to act as an area where Draggable items can be moved onto and register the event." +
                 "<p>For more information, see the " +
                 "<a href=\"http://wiki.icefaces.org/display/ICE/Droppable\">Droppable Wiki Documentation</a>."
)
@ClientBehaviorHolder(events = {
	@ClientEvent(name="drop", javadoc="Fired when a draggable component is dropped on this droppable component (default event). By default, both the draggable and the droppable components are executed in this ajax request.", 
	tlddoc="Fired when a draggable component is dropped on this droppable component (default event). By default, both the draggable and the droppable components are executed in this ajax request.", defaultRender="@all", defaultExecute="@this")
}, defaultEvent="drop")
@ICEResourceLibrary(ACEResourceNames.ACE_LIBRARY)
@ICEResourceDependencies({
	@ICEResourceDependency(name=ACEResourceNames.COMPONENTS_JS)
})
public class DroppableMeta extends UIComponentBaseMeta {
    @Property(tlddoc = "The JavaScript component instance variable name.")
    String widgetVar;
    @Property(name = "for",
              tlddoc = "Id of the component to add the droppable behavior to.")
    String forValue;

    @Property(tlddoc = "Class to apply to the droppable component when an acceptable draggable is hovering.")
    String hoverStyleClass;
    @Property(tlddoc = "Class to apply to the droppable component when an acceptable draggable is dropped.")
    String activeStyleClass;

    @Property(tlddoc = "JQuery selector to define the set of acceptable draggables.")
    String accept;
    @Property(tlddoc = "This keyword allows sets of dragabbles and droppables with the same keyword to be associated with each other exclusively.")
    String scope;
    @Property(tlddoc = "This arbitrary keyword specifies the method of checking if a draggable is 'over' the droppable, possible values include: fit (drag must be smaller), intersect (drag must cover > 50%). pointer (cursor must be inside drop) and touch (any of the drop touches).")
    String tolerance;
    @Property(tlddoc = "Allows you to set the ID of an ACE UIData component and that will provide a Java object as input for the drop event. That object is defined by matching the sibling index of the droppable to an object at that index in the UIData component backing.")
    String datasource;

    @Property(tlddoc = "If enabled, no draggables will activate this droppable.")
    boolean disabled;

    @Property(expression = Expression.METHOD_EXPRESSION,
              methodExpressionArgument = "org.icefaces.ace.event.DragDropEvent",
              tlddoc = "MethodExpression reference to a method called whenever a draggable is moved into this droppable. The method receives a single argument, DragDropEvent.")
    MethodExpression dropListener;
}
