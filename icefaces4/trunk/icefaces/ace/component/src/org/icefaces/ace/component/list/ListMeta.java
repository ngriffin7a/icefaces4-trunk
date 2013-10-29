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

package org.icefaces.ace.component.list;

import org.icefaces.ace.meta.annotation.*;
import org.icefaces.ace.meta.baseMeta.UIDataMeta;

import javax.el.MethodExpression;

import org.icefaces.ace.resources.ACEResourceNames;
import org.icefaces.resources.ICEResourceDependencies;
import org.icefaces.resources.ICEResourceDependency;
import org.icefaces.resources.ICEResourceLibrary;

import java.util.Set;

@Component(
    tagName = "list",
    componentClass = "org.icefaces.ace.component.list.ACEList",
    generatedClass = "org.icefaces.ace.component.list.ListBase",
    extendsClass = "org.icefaces.impl.component.UISeriesBase",
    componentType = "org.icefaces.ace.component.List",
    rendererType  = "org.icefaces.ace.component.ListRenderer",
    rendererClass = "org.icefaces.ace.component.list.ListRenderer",
    componentFamily = "org.icefaces.ace.List",
    tlddoc = "Renders an Array or List of objects as a styled HTML UL element. Supports dual & multi" +
            "list configurations, reordering, item drag/drop, selection & block style layout.  The component " +
            "strives to be exceptionally easy to style and adjust layout.The ace:list value attribute" +
            "may take a List of SelectItem objects to use their String label representations " +
            "as list item contents, can take SelectItems as a model via f:selectItem(s) children, or " +
            "value defines a List of arbitrary objects to be represented by iterative renderings of our child components." +
            "The components are associated with the iterative object via bindings of the 'var' property." +
            "<p>For more information, see the <a href=\"http://wiki.icefaces.org/display/ICE/List\">List Wiki Documentation</a>.</p>"
)
@ICEResourceLibrary(ACEResourceNames.ACE_LIBRARY)
@ICEResourceDependencies({
     @ICEResourceDependency(name = ACEResourceNames.COMPONENTS_JS)
})
@ClientBehaviorHolder(events = {
    @ClientEvent(name="select", defaultRender="@all", defaultExecute="@this",
            javadoc="Fired when an item is clicked & selected in the List.",
            tlddoc ="Fired when an item is clicked & selected in the List."),
    @ClientEvent(name="deselect", defaultRender="@all", defaultExecute="@this",
            javadoc="Fired when an item is clicked & deselected in the List.",
            tlddoc ="Fired when an item is clicked & deselected in the List."),
    @ClientEvent(name="move", defaultRender="@all", defaultExecute="@this",
            javadoc="Fired when an item is moved within the List.",
            tlddoc ="Fired when an item is moved within the List."),
    @ClientEvent(name="migrate", defaultRender="@all", defaultExecute="@this",
            javadoc="Fired when an item is migrated to this List.",
            tlddoc ="Fired when an item is migrated to this List.")
    },
    defaultEvent = "select"
)
public class ListMeta extends UIDataMeta {
    // Properties
    @Property(tlddoc="Style class to apply to the iterative LI element.")
    private String itemClass;

    @Property(tlddoc="Style class to apply to the container UL element.")
    private String bodyClass;

    @Property(tlddoc="Style class to apply to the container DIV element.")
    private String styleClass;

    @Property(tlddoc="Style class to apply to the header DIV element.")
    private String headerClass;

    @Property(tlddoc="Style class to apply to the footer DIV element.")
    private String footerClass;

    @Property(tlddoc="Style class to apply to the optional dragging placeholder LI element.")
    private String placeholderClass;


    @Property(tlddoc = "Defines a CSS height value to set in the UL element style. eg. '200px', '10%', etc.")
    private String height;


    @Property(tlddoc = "Style rules to apply to the container DIV element")
    private String style;

    @Property(tlddoc="Style rules to apply to the iterative LI element.")
    private String itemStyle;

    @Property(tlddoc="Style rules to apply to the container UL element.")
    private String bodyStyle;

    @Property(tlddoc="Style rules to apply to the header DIV element.")
    private String headerStyle;

    @Property(tlddoc="Style rules to apply to the footer DIV element.")
    private String footerStyle;



    @Property(tlddoc = "Enable adding a style to the whitespace that is cleared for a" +
            " list item being dragged / dropped.", defaultValue = "true",
            defaultValueType = DefaultValueType.EXPRESSION)
    private Boolean placeholder;

    @Property(tlddoc = "Enable the dragging of list items in this list. Note this attribute has no effect when an f:selectItem(s) model is used.",
            defaultValue = "true", defaultValueType = DefaultValueType.EXPRESSION)
    private Boolean dragging;

    @Property(tlddoc = "Enables inter-list dragging and dropping; an identifier" +
            " used to link this region and others for bi-directional dropping. Note this attribute has no effect when an f:selectItem(s) model is used.")
    private String dropGroup;



    @Property(tlddoc = "Enables item selection via clicking when defined as \"multiple\" or \"single\". " +
            "The default value of the property is null; this and any value other than \"multiple\" or " +
            "\"single\" disables selection for the list. Note that each click begins a " +
            "new selection. If \"multiple\" is set, CTRL (or Command) + Click adds to (or removes from) " +
            "the selection, and Shift + Click will add to the selection all unselected items between the previous selection " +
            "(or deselection) and the clicked item (inclusive of the clicked item itself).")
    private String selectionMode;

    @Property(tlddoc = "Enable single item migration to the next list in the first matching " +
            "ListControl via item double clicks; with reverse migation via shift double clicks. " +
            "If used in a dual list configuration the reverse migration is implied for the second " +
            "list, and holding the shift key isn't required. Note this attribute has no effect when an f:selectItem(s) model is used.",
            defaultValue = "true", defaultValueType = DefaultValueType.EXPRESSION)
    private Boolean doubleClickMigration;

    @Property(tlddoc = "Defines the set of Objects from the source List that are selected. " +
            "Note that when f:selectItem(s) children are used as the model to this ace:list " +
            "that selections are instead added to the Collection bound to the value attribute, " +
            "to more closely emulate the h:selectManyListbox component.")
    private Set<Object> selections;

    @Property(expression = Expression.METHOD_EXPRESSION,
            methodExpressionArgument = "org.icefaces.ace.event.ListSelectEvent",
            tlddoc = "Define a method listener that is passed a ListSelectEvent wrapping the array" +
                    "of items newly selected in a single request.")
    private MethodExpression selectionListener;



    @Property(tlddoc = "Enable a set of buttons to control reordering of items within this list." +
            "Requires selection of determine items to adjust via buttons.",
            defaultValue = "false", defaultValueType = DefaultValueType.EXPRESSION)
    private Boolean controlsEnabled;

    @Property(tlddoc = "Defines the order in which the reordering buttons render. " +
            "Specify a space delimited list of values such as \"top\", \"up\", \"dwn\", \"btm\".",
            defaultValue = "top up dwn btm",
            defaultValueType = DefaultValueType.STRING_LITERAL)
    private String controlsFormat;

    @Property(tlddoc = "Style class to apply to the DIV surrounding the reordering controls.", defaultValue = "",
            defaultValueType = DefaultValueType.STRING_LITERAL)
    private String controlsContainerClass;

    @Property(tlddoc = "Style class to apply to the div surrounding the individual reordering icons.",
            defaultValue = "ui-state-default ui-corner-all",
            defaultValueType = DefaultValueType.STRING_LITERAL)
    private String controlsItemClass;

    @Property(tlddoc="Style class to apply to the spacer container around each button element.")
    private String controlsSpacerClass;

    @Property(tlddoc = "Style class to apply to the span defining the top button icon.",
            defaultValue = "ui-icon ui-icon-arrowstop-1-n",
            defaultValueType = DefaultValueType.STRING_LITERAL)
    private String topButtonClass;

    @Property(tlddoc = "Style class to apply to the span defining the up button icon.",
            defaultValue = "ui-icon ui-icon-arrow-1-n",
            defaultValueType = DefaultValueType.STRING_LITERAL)
    private String upButtonClass;

    @Property(tlddoc = "Style class to apply to the span defining the down button icon.",
            defaultValue = "ui-icon ui-icon-arrow-1-s",
            defaultValueType = DefaultValueType.STRING_LITERAL)
    private String downButtonClass;

    @Property(tlddoc = "Style class to apply to the span defining the bottom button icon.",
            defaultValue = "ui-icon ui-icon-arrowstop-1-s",
            defaultValueType = DefaultValueType.STRING_LITERAL)
    private String bottomButtonClass;

    @Property(tlddoc = "Enables an alternate style on the table that uses greatly " +
            "reduced padding and a 8 point default font.",
            defaultValue = "false",
            defaultValueType = DefaultValueType.EXPRESSION)
    private Boolean compact;
	
	@Property(tlddoc = "Specifies the jQuery selector(s) of the elements inside the item container (<li> element) that will be used as the drag handle(s). This is intended to be used only when the list contains nested components and/or elements. If this attribute isn't specified, any point of the item container can initiate the dragging action. The selectors are relative to the item's root element. When using multiple selectors, separate them by commas.")
	String dragHandle;
}