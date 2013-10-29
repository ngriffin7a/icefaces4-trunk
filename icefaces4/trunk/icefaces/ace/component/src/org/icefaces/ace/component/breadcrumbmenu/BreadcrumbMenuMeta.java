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
package org.icefaces.ace.component.breadcrumbmenu;

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
        tagName = "breadcrumbMenu",
        componentClass = "org.icefaces.ace.component.breadcrumbmenu.BreadcrumbMenu",
        rendererClass = "org.icefaces.ace.component.breadcrumbmenu.BreadcrumbMenuRenderer",
        generatedClass = "org.icefaces.ace.component.breadcrumbmenu.BreadcrumbMenuBase",
        extendsClass = "org.icefaces.ace.component.menu.AbstractMenu",
        componentFamily = "org.icefaces.ace.component.Menu",
        componentType = "org.icefaces.ace.component.BreadcrumbMenu",
        rendererType = "org.icefaces.ace.component.BreadcrumbMenuRenderer",
        tlddoc = "BreadcrumbMenu is a horizontal bar of breadcrumb menu items. The crumbs are encoded by child menuItem tags. " +
                "The menu items can be encoded inline or dynamically using a menu model (org.icefaces.ace.model.MenuModel). The icon attribute is not used. " +
                "The icon will be forced to a home page icon for the first item and a right arrow icon for the rest. " +
                "The last (or only) item will also be forced to be disabled."
)
@ICEResourceLibrary(ACEResourceNames.ACE_LIBRARY)
@ICEResourceDependencies({
        @ICEResourceDependency(name = "util/ace-menu.js")
})
public class BreadcrumbMenuMeta extends UIComponentBaseMeta {

    @Property(tlddoc = "MenuModel instance to create menus programmatically. " +
            "For the menuitem components, use explicit ids, and " +
            "avoid long processing in the getter method for this property, " +
            "as it will be called multiple times, in every lifecycle.")
    private MenuModel model;

    @Property(tlddoc = "Style of the main container element.")
    private String style;

    @Property(tlddoc = "Style class of the main container element.")
    private String styleClass;

    private AbstractMenu am; // need this for resolving dependence on AbstractMenu when compiling Base class
}
