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

package org.icefaces.ace.component.menubar;

import org.icefaces.ace.component.menu.AbstractMenu;
import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.baseMeta.UIComponentBaseMeta;
import org.icefaces.ace.model.MenuModel;

import org.icefaces.ace.resources.ACEResourceNames;
import org.icefaces.resources.ICEResourceDependencies;
import org.icefaces.resources.ICEResourceDependency;
import org.icefaces.resources.ICEResourceLibrary;

@Component(
        tagName = "menuBar",
        componentClass = "org.icefaces.ace.component.menubar.MenuBar",
        rendererClass = "org.icefaces.ace.component.menubar.MenuBarRenderer",
        generatedClass = "org.icefaces.ace.component.menubar.MenuBarBase",
        extendsClass = "org.icefaces.ace.component.menu.AbstractMenu",
        componentFamily = "org.icefaces.ace.component.Menu",
        componentType = "org.icefaces.ace.component.MenuBar",
        rendererType = "org.icefaces.ace.component.MenuBarRenderer",
        tlddoc = "Menubar is a horizontal navigation component." +
                 "<p>For more information, see the " +
                 "<a href=\"http://wiki.icefaces.org/display/ICE/Menu+and+Menubar\">MenuBar Wiki Documentation</a>."
)
@ICEResourceLibrary(ACEResourceNames.ACE_LIBRARY)
@ICEResourceDependencies({
    @ICEResourceDependency(name = "util/ace-menu.js")
})
public class MenuBarMeta extends UIComponentBaseMeta {
    @Property(tlddoc = "Javascript variable name of the wrapped widget.")
    private String widgetVar;

    @Property(tlddoc = "MenuModel instance to create menus programmatically. " +
            "For the menuitem and submenu components, use explicit ids, and " +
            "avoid long processing in the getter method for this property, " +
            "as it will be called multiple times, in every lifecycle. If using MyFaces, see wiki page for some known issues.")
    private MenuModel model;

    @Property(tlddoc = "When set to true, submenus are displayed on mouseover of a menuitem.")
    private boolean autoSubmenuDisplay;

    @Property(tlddoc = "Sets the effect for the menu display, default value is FADE. Possible values are" +
            " \"FADE\", \"SLIDE\", \"NONE\". Use \"NONE\" to disable animation at all.", defaultValue = "fade")
    private String effect;

    @Property(tlddoc = "Sets the effect duration in milliseconds.", defaultValue = "400")
    private int effectDuration;

    @Property(tlddoc = "Style of the main container element.")
    private String style;

    @Property(tlddoc = "Style class of the main container element.")
    private String styleClass;

    @Property(tlddoc="Forces the direction in which the submenus will be displayed, regardless of the position of the menu in the viewport. Possible values are combinations of \"up\", \"down\", and \"middle\" with \"left\", \"right\", and \"center\", separated by a space. The value \"auto\" can also be used in place of any of those values (e.g. \"up\", \"up right\", \"left\", \"left auto\", \"center\"). Note that the \"center\" value only applies to first-level submenus, while the \"middle\" value only applies to deeper-level submenus (i.e. all but first-level submenus).", defaultValue = "auto")
    private String direction;
	
    private AbstractMenu am; // need this for resolving dependence on AbstractMenu when compiling Base class
}
