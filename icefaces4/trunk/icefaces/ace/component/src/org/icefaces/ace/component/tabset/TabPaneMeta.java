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

package org.icefaces.ace.component.tabset;

import javax.faces.component.UIComponent;

import org.icefaces.ace.meta.baseMeta.UIComponentBaseMeta;
import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.DefaultValueType;
import org.icefaces.ace.meta.annotation.Facet;
import org.icefaces.ace.meta.annotation.Facets;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.api.IceClientBehaviorHolder;

@Component(tagName = "tabPane",
        componentClass  = "org.icefaces.ace.component.tabset.TabPane",
        generatedClass  = "org.icefaces.ace.component.tabset.TabPaneBase",
        extendsClass    = "javax.faces.component.UIComponentBase", 
        componentType   = "org.icefaces.ace.component.TabPane",
        componentFamily = "org.icefaces.ace.TabPane",
        tlddoc = "<p>The TabPane component belongs inside of a TabSet " +
            "component, and encapsulates both the clickable label, and the " +
            "content pane that is shown when the TabPane is selected. The " +
            "clickable label part may be specified by the label property, " +
            "or by the label facet, allowing for any components to " +
            "comprise the label. <p>For more information, see the " +
            "<a href=\"http://wiki.icefaces.org/display/ICE/TabPane\">TabPane Wiki Documentation</a>.")
public class TabPaneMeta extends UIComponentBaseMeta {
    @Property(tlddoc="The text label in the clickable tab for the TabPane. " +
        "Where these are rendered is determined by TabSet's orientation " +
        "property. Alternatively, the label facet may be used to specify " +
        "components to represent the label.")
    private String label;
    
    @Property (tlddoc="If true then this tab will be disabled and can not be selected.")
    private boolean disabled;

    @Property(tlddoc="When clientSide=true on the tabSet, all tabPane " +
        "components are always cached on the client, so this only applies " +
        "when the tabSet has clientSide=false. When this property value is " +
        "\"none\" then no caching occurs in the browser and the tab " +
        "contents are completely rendered and updated in the browser when " +
        "the tab is active, and become unrendered when the tab is no " +
        "longer active. The other values involve the tab contents being " +
        "cached in different ways. The contents are lazily loaded when the " +
        "tab first becomes active, and then remain in the browser. When the " +
        "value is \"dynamic\", the lazily loaded tab contents will continue " +
        "to be rendered whether the tab is still active or not, and any " +
        "changes will be detected and granularly updated. When the value is " +
        "\"static\", the tab will not be rendered or updated after the " +
        "first time. This is an optimisation to save CPU rendering time. It " +
        "is facilitated by \"staticAuto\", which is usually like static " +
        "mode, except when a component within the tab initiates a lifecycle " +
        "causing a full render, then it will automatically change to being " +
        "dynamic for just that lifecycle, so that the tab contents may be " +
        "rendered and updated in the browser. As well, there is " +
        "\"dynamicRevertStaticAuto\", which allows for the application to " +
        "indicate that it wants to temporarily use dynamic caching for the " +
        "current lifecycle only, and have the tabPane automatically revert " +
        "the mode back to static auto mode afterwards. This is intended to " +
        "be set in action/actionListener methods that knowingly update an " +
        "otherwise statically cached tabPane.",
        defaultValue=TabPaneCache.DEFAULT,
        defaultValueType = DefaultValueType.STRING_LITERAL)
    private String cache;

    
    @Facets
    class FacetsMeta{
        @Facet(tlddoc = "Allows rendering of nested components as tab label.")
        UIComponent label;          
    }    
}
