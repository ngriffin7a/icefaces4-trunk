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

package org.icefaces.mobi.component.viewmanager;


import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.baseMeta.UIComponentBaseMeta;
import org.icefaces.mobi.util.TLDConstants;


@Component(
        tagName = "view",
        componentClass = "org.icefaces.mobi.component.viewmanager.View",
        rendererClass = "org.icefaces.mobi.component.viewmanager.ViewRenderer",
        generatedClass = "org.icefaces.mobi.component.viewmanager.ViewBase",
        componentType = "org.icefaces.View",
        rendererType = "org.icefaces.ViewRenderer",
        extendsClass = "javax.faces.component.UIComponentBase",
        componentFamily = "org.icefaces.View",
        tlddoc = "")


@ResourceDependencies({
        @ResourceDependency(library = "org.icefaces.component.util", name = "component.js")
})
public class ViewMeta extends UIComponentBaseMeta {

    @Property(tlddoc = TLDConstants.STYLE)
    private String style;

    @Property(tlddoc = TLDConstants.STYLECLASS)
    private String styleClass;
    
    @Property(tlddoc = "The menu icon type")
    private String menuIcon;
    
    @Property(tlddoc = "The page title")
    private String title;
    
    @Property(tlddoc = "Include the view in the main menu", defaultValue = "true")
    private boolean includeInMenu;
    
    @Property(tlddoc = "The nav bar group that this view will be rendered for")
    private String navBarGroup;
    
    @Property(tlddoc = "The group divider to render before the menu item for this view")
    private String divider;
    
    @Property(tlddoc = "Show as the splash screen")
    private boolean splash;

    
}
