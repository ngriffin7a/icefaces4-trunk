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

package org.icefaces.mobi.component.onlinestatus;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

@Component(
        tagName = "onlineStatus",
        componentClass = "org.icefaces.mobi.component.onlinestatus.OnlineStatus",
        rendererClass = "org.icefaces.mobi.component.onlinestatus.OnlineStatusRenderer",
        generatedClass = "org.icefaces.mobi.component.onlinestatus.OnlineStatusBase",
        componentType = "org.icefaces.OnlineStatus",
        rendererType = "org.icefaces.OnlineStatusRenderer",
        extendsClass = "javax.faces.component.UIComponentBase",
        componentFamily = "org.icefaces.OnlineStatus",
        tlddoc = "Renders an element that has its CSS class changed depending on what the online state of the browser. Also it provides attributes " +
                "that can take Javascript code that is executed when browser goes online or offline."
)

@ResourceDependencies({
    @ResourceDependency(library = "org.icefaces.component.onlinestatus", name = "onlinestatus.js")
})
public class OnlineStatusMeta {
    @Property(defaultValue = "", tlddoc = "The CSS class applied to the component's root element when browser is online.")
    private String onlineStyleClass;

    @Property(defaultValue = "", tlddoc = "The CSS class applied to the component's root element when browser is offline.")
    private String offlineStyleClass;

    @Property(defaultValue = "", tlddoc = "JavaScript to be evaluated when the browser goes online. The 'element' argument passed to the callback code is the root element of the component. ")
    private String onOnline;

    @Property(defaultValue = "", tlddoc = "JavaScript to be evaluated when the browser goes offline. The 'element' argument passed to the callback code is the root element of the component. ")
    private String onOffline;

}
