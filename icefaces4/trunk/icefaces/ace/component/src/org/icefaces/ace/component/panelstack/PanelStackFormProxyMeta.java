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
package org.icefaces.ace.component.panelstack;
import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.baseMeta.UIComponentBaseMeta;


@Component(
        tagName = "panelStackFormProxy",
        componentClass = "org.icefaces.ace.component.panelstack.PanelStackFormProxy",
        rendererClass = "org.icefaces.ace.component.panelstack.PanelStackFormProxyRenderer",
        generatedClass = "org.icefaces.ace.component.panelstack.PanelStackFormProxyBase",
        componentType = "org.icefaces.PanelStackFormProxy",
        rendererType = "org.icefaces.PanelStackFormProxyRenderer",
        extendsClass = "javax.faces.component.UIComponentBase",
        componentFamily = "org.icefaces.PanelStackFormProxy",
        tlddoc = "panelStackFormProxy allows stackPanes to nest forms and still maintain server-side current pane state")
public class PanelStackFormProxyMeta extends UIComponentBaseMeta {

    @Property(tlddoc="clientId of the panelStack component, only required if the panelStackFormProxy is not nested inside a panelStack", name="for")
    private String For;
     
}
