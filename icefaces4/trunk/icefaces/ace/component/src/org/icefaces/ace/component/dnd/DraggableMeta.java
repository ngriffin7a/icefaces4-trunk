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

import org.icefaces.ace.meta.annotation.ClientBehaviorHolder;
import org.icefaces.ace.meta.annotation.ClientEvent;
import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.DefaultValueType;
import org.icefaces.ace.meta.annotation.Expression;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.baseMeta.UIComponentBaseMeta;
import javax.el.MethodExpression;

import org.icefaces.ace.resources.ACEResourceNames;
import org.icefaces.resources.ICEResourceDependencies;
import org.icefaces.resources.ICEResourceDependency;
import org.icefaces.resources.ICEResourceLibrary;

@Component(
    tagName = "draggable",
    componentClass = "org.icefaces.ace.component.dnd.Draggable",
    generatedClass = "org.icefaces.ace.component.dnd.DraggableBase",
    rendererClass = "org.icefaces.ace.component.dnd.DraggableRenderer",
    extendsClass = "javax.faces.component.UIComponentBase",
    componentType = "org.icefaces.ace.component.Draggable",
    rendererType = "org.icefaces.ace.component.DraggableRenderer",
    componentFamily = "org.icefaces.ace.Draggable",
    tlddoc = "Enables an Id-specified component to be moved via dragging. Draggable component can cause events on droppable ones." +
                 "<p>For more information, see the " +
                 "<a href=\"http://wiki.icefaces.org/display/ICE/Draggable\">Draggable Wiki Documentation</a>."
)
@ClientBehaviorHolder(events = {
	@ClientEvent(name="start", javadoc="Fired when the target component starts to be dragged (default event). By default, no components are rendered in this ajax request, because rendering this draggable component itself or the target component could cause a dynamic update that replaces the original nodes being dragged on the page, which could interrupt the dragging action for the user.", 
	tlddoc="Fired when the target component starts to be dragged (default event). By default, no components are rendered in this ajax request, because rendering this draggable component itself or the target component could cause a dynamic update that replaces the original nodes being dragged on the page, which could interrupt the dragging action for the user.", defaultRender="@none", defaultExecute="@this")
}, defaultEvent="start")
@ICEResourceLibrary(ACEResourceNames.ACE_LIBRARY)
@ICEResourceDependencies({
	@ICEResourceDependency(name=ACEResourceNames.COMPONENTS_JS)
})
public class DraggableMeta extends UIComponentBaseMeta {
    @Property(tlddoc = "The JavaScript component instance variable name.")
    String widgetVar;
    @Property(name = "for",
              tlddoc = "Id of the component to add draggable behavior to.")
    String forValue;

    @Property(tlddoc = "Restrict the drag movement of this item to an axis. Default is null, possible values include: \"x\" & \"y\".")
    String axis;
    @Property(tlddoc = "Id of the parent component whose boundaries should restrict the movement of this draggable.")
    String containment;
    @Property(tlddoc = "JQuery selector that defines the set of items this draggable shares its space with, ensures this item is brought to the front z-index when dragged.")
    String stack;
    @Property(tlddoc = "Defines the drag movement of this component as though snapping to a grid in defined intervals. Ex: '20x20', measured in pixels.")
    String grid;
    @Property(tlddoc = "This arbitrary keyword allows sets of dragabbles and droppables with the same keyword to be associated with each other exclusively.")
    String scope;
    @Property(tlddoc = "Cursor style to set when this component is being dragged. See <a href=\"http://www.quirksmode.org/css/cursor.html\">this page</a> for a reference of what cursor styles your browser supports.")
    String cursor;
/*
    @Property(tlddoc = "Id of the ACE Dashboard component to interact with.")
    String dashboard;
*/
    @Property(tlddoc = "Restrict the drag-event start to an element specified by this JQuery selector.")
    String handle;

    @Property(tlddoc = "Enable 'snap to droppable' behavior for this component.")
    boolean snap;
    @Property(tlddoc = "Specifies which edges on elements to snap to. Possible values include: outer, inner and both.")
    String snapMode;
    @Property(tlddoc = "Distance in pixels from the droppable edges at which snapping should occur.")
    int snapTolerance;

    @Property(tlddoc = "If set, component won't cause drop event.")
    boolean dragOnly;
    @Property(tlddoc = "If set, movement is disabled.")
    boolean disabled;
    @Property(tlddoc = "If set, the component will return to its original position when dragging stops.")
    boolean revert;

    // Intended to take function as well, should look at extending this.
    @Property(tlddoc = "Specifies which element to move during the dragging event. Possible values are 'original' or 'clone'.")
    String helper;
    @Property(tlddoc = "The z-index to set for the helper object.")
    int zindex;

    @Property(defaultValue = "1.0",
              defaultValueType = DefaultValueType.EXPRESSION,
              tlddoc = "The opacity for the helper during dragging. Values can range from 0.00 to 1.00.")
    Double opacity;
	
    @Property(tlddoc = "Allows you to set the ID of an UIData component that will provide a Java object as input for the drag start event. That object is defined by matching the index of the draggable to an object at the same index in the UIData component backing.")
    String datasource;
	
    @Property(expression = Expression.METHOD_EXPRESSION,
              methodExpressionArgument = "org.icefaces.ace.event.DragDropEvent",
              tlddoc = "MethodExpression reference to a method called whenever the target component starts to be dragged. (It is advised not to use this event for updating the component being dragged, in order to avoid having its markup replaced while still dragging it. See wiki for more details.)")
    MethodExpression dragStartListener;
}
