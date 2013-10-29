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

package org.icefaces.ace.component.contextmenu;

import org.icefaces.ace.component.menu.AbstractMenu;
import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.annotation.Expression;
import org.icefaces.ace.meta.baseMeta.UIComponentBaseMeta;
import org.icefaces.ace.model.MenuModel;

import org.icefaces.ace.resources.ACEResourceNames;
import org.icefaces.resources.ICEResourceDependencies;
import org.icefaces.resources.ICEResourceDependency;
import org.icefaces.resources.ICEResourceLibrary;

import javax.el.ValueExpression;

@Component(
        tagName = "contextMenu",
        componentClass = "org.icefaces.ace.component.contextmenu.ContextMenu",
        rendererClass = "org.icefaces.ace.component.contextmenu.ContextMenuRenderer",
        generatedClass = "org.icefaces.ace.component.contextmenu.ContextMenuBase",
        extendsClass = "org.icefaces.ace.component.menu.AbstractMenu",
        componentFamily = "org.icefaces.ace.component.Menu",
        componentType = "org.icefaces.ace.component.ContextMenu",
        rendererType = "org.icefaces.ace.component.ContextMenuRenderer",
        tlddoc = "ContextMenu provides a popup menu that is displayed on mouse right-click event." +
                 "<p>For more information, see the " +
                 "<a href=\"http://wiki.icefaces.org/display/ICE/ContextMenu\">ContextMenu Wiki Documentation</a>."
)
@ICEResourceLibrary(ACEResourceNames.ACE_LIBRARY)
@ICEResourceDependencies({
        @ICEResourceDependency(name = "util/ace-menu.js")
})
public class ContextMenuMeta extends UIComponentBaseMeta {
    @Property(tlddoc = "Javascript variable name of the wrapped widget.")
    private String widgetVar;

    @Property(name = "for", tlddoc = "Server side id of the component to attach to.")
    private String forValue;
	
    @Property(tlddoc = "Server side id of the ace:delegate component to attach to.")
    private String forDelegate;

    @Property(tlddoc = "Style of the main container element.")
    private String style;

    @Property(tlddoc = "Style class of the main container element.")
    private String styleClass;

    @Property(tlddoc = "zindex property to control overlapping with other elements.", defaultValue = "1")
    private int zindex;

    @Property(tlddoc = "Sets the effect for the menu display. Possible values are 'blind', 'clip', 'drop', 'explode, 'fade', 'fold', 'puff', 'slide', 'scale', 'bounce', 'highlight', 'pulsate', and 'shake' (This feature is not supported in IE7 and IE8, see wiki page for more information).", defaultValue = "fade")
    private String effect;

    @Property(tlddoc = "Sets the effect duration in milliseconds.", defaultValue = "400")
    private int effectDuration;

    @Property(tlddoc = "MenuModel instance to create menus programmatically. " +
            "For the menuitem and submenu components, use explicit ids, and " +
            "avoid long processing in the getter method for this property, " +
            "as it will be called multiple times, in every lifecycle. If using MyFaces, see wiki page for some known issues.")
    private MenuModel model;
	
    @Property(tlddoc="Forces the direction in which the context menu will be displayed, regardless of the position of the mouse pointer in the viewport. Possible values are combinations of \"up\" and \"down\" with \"left\" and \"right\", separated by a space, and \"auto\" can also be used in place of any of those values (e.g. \"up\", \"up right\", \"left\", \"left auto\").", defaultValue = "auto")
    private String direction;
	
	@Property(tlddoc="")
	private Object store;
	
	@Property(tlddoc="", expression = Expression.VALUE_EXPRESSION)
	private ValueExpression fetch;

    private AbstractMenu am; // need this to resolve dependence on AbstractMenu when compiling Base class
}
