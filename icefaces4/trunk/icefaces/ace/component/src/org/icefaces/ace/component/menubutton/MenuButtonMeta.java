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

package org.icefaces.ace.component.menubutton;

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
        tagName = "menuButton",
        componentClass = "org.icefaces.ace.component.menubutton.MenuButton",
        rendererClass = "org.icefaces.ace.component.menubutton.MenuButtonRenderer",
        generatedClass = "org.icefaces.ace.component.menubutton.MenuButtonBase",
        extendsClass = "org.icefaces.ace.component.menu.AbstractMenu",
        componentFamily = "org.icefaces.ace.component.Menu",
        componentType = "org.icefaces.ace.component.MenuButton",
        rendererType = "org.icefaces.ace.component.MenuButtonRenderer",
        tlddoc = "MenuButton displays different commands in a popup menu." +
                 "<p>For more information, see the " +
                 "<a href=\"http://wiki.icefaces.org/display/ICE/MenuButton\">MenuButton Wiki Documentation</a>."
)
@ICEResourceLibrary(ACEResourceNames.ACE_LIBRARY)
@ICEResourceDependencies({
    @ICEResourceDependency(name = "util/ace-menu.js")
})
public class MenuButtonMeta extends UIComponentBaseMeta {
    @Property(tlddoc = "Javascript variable name of the wrapped widget.")
    private String widgetVar;

    @Property(tlddoc = "MenuModel instance to create menus programmatically. " +
            "For the menuitem and submenu components, use explicit ids, and " +
            "avoid long processing in the getter method for this property, " +
            "as it will be called multiple times, in every lifecycle. If using MyFaces, see wiki page for some known issues.")
    private MenuModel model;

    @Property(tlddoc = "Label of the button.")
    private String value;

    @Property(tlddoc = "Style of the main container element.")
    private String style;

    @Property(tlddoc = "Style class of the main container element.")
    private String styleClass;

    @Property(tlddoc = "Disables or enables the button.")
    private boolean disabled;

    @Property(tlddoc = "Sets the effect for the menu display, default value is FADE. Possible values are" +
            " \"FADE\", \"SLIDE\", \"NONE\". Use \"NONE\" to disable animation at all.", defaultValue = "fade")
    private String effect;

    @Property(tlddoc = "Sets the effect duration in milliseconds.", defaultValue = "400")
    private int effectDuration;

    @Property(tlddoc = "zindex property to control overlapping with other elements.", defaultValue = "1")
    private int zindex;

    private AbstractMenu am; // need this to resolve dependence on AbstractMenu when compiling Base class
}
