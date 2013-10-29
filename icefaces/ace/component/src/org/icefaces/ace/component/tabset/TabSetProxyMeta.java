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

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;

import org.icefaces.ace.meta.annotation.Required;
import org.icefaces.ace.meta.baseMeta.UIPanelMeta;


@Component(tagName ="tabSetProxy",
        componentClass  = "org.icefaces.ace.component.tabset.TabSetProxy",
        generatedClass  = "org.icefaces.ace.component.tabset.TabSetProxyBase",
        extendsClass    = "javax.faces.component.UIPanel", 
        componentType   = "org.icefaces.ace.component.TabSetProxy",
        componentFamily = "org.icefaces.ace.component",
        tlddoc = "<p>The TabSetProxy component is used in conjunction with a " +
            "server-side TabSet component that is not inside of a form. " +
            "The TabSetProxy will then instead be placed inside of a form, " +
            "to handle the server communication on behalf of the TabSet. <p>" +
            "For more information, see the <a href=\"http://wiki.icefaces.org/display/ICE/TabSetProxy\">TabSetProxy Wiki Documentation</a>.")
public class TabSetProxyMeta extends UIPanelMeta {
    @Property(tlddoc="clientId of the tabSet component", name="for", required=Required.yes)
    private String For;
}
