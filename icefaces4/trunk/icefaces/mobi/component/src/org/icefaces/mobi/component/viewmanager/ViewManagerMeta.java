/*
 * Copyright 2004-2014 ICEsoft Technologies Canada Corp.
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


import java.util.Stack;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.baseMeta.UIComponentBaseMeta;
import org.icefaces.mobi.util.TLDConstants;


@Component(
        tagName = "viewManager",
        componentClass = "org.icefaces.mobi.component.viewmanager.ViewManager",
        rendererClass = "org.icefaces.mobi.component.viewmanager.ViewManagerRenderer",
        generatedClass = "org.icefaces.mobi.component.viewmanager.ViewManagerBase",
        componentType = "org.icefaces.ViewManager",
        rendererType = "org.icefaces.ViewManagerRenderer",
        extendsClass = "javax.faces.component.UIComponentBase",
        componentFamily = "org.icefaces.ViewManager",
        tlddoc = "")


@ResourceDependencies({
		@ResourceDependency(library = "org.icefaces.component.viewmanager", name = "view-manager.css"),
		@ResourceDependency(library = "org.icefaces.component.icons", name = "icons.css"),
        @ResourceDependency(library = "org.icefaces.component.util", name = "component.js")
})
public class ViewManagerMeta extends UIComponentBaseMeta {

    @Property(tlddoc = TLDConstants.STYLE)
    private String style;

    @Property(tlddoc = TLDConstants.STYLECLASS)
    private String styleClass;
    
    @Property(tlddoc = "The main header title")
    private String title;
    
    @Property(tlddoc = "The selected view id. It has to match the id of an existing child mobi:view component.", defaultValue = "mobiViewManagerMenu")
    private String selected;
    
    @Property(tlddoc = "The inline CSS style applied to the header.")
    private String headerStyle;
    
    @Property(tlddoc = "The type of view transitons to apply [horizontal|vertical|flip|fade].", 
    		defaultValue = "horizontal")
    private String transitionType;
    
    @Property(tlddoc = "The history stack.")
    private Stack<String> history;
    
    @Property(tlddoc = "If set, the back button label to display, if left to the default 'mobi-view' keyword, the back button label will"
            + " be set to the associated view title. ", defaultValue = "mobi-view")
    private String backButtonLabel;
    
    

    
}
