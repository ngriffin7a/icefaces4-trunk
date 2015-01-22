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

package org.icefaces.mobi.component.deviceresource;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.baseMeta.UIComponentBaseMeta;

@Component(
        tagName = "deviceResource",
        componentClass = "org.icefaces.mobi.component.deviceresource.DeviceResource",
        rendererClass = "org.icefaces.mobi.component.deviceresource.DeviceResourceRenderer",
        generatedClass = "org.icefaces.mobi.component.deviceresource.DeviceResourceBase",
        componentType = "org.icefaces.DeviceResource",
        rendererType = "org.icefaces.DeviceResourceRenderer",
        extendsClass = "javax.faces.component.UIComponentBase",
        componentFamily = "org.icefaces.DeviceResource",
        tlddoc = "Render the necessary HTML head resources for a detected " +
                "device, including the device-specific meta tags, the CSS " +
                "themes, and javascript. This component must be placed inside the head section of a page."
        )
@ResourceDependencies({
    @ResourceDependency(library = "org.icefaces.component.util", name = "component.js")
})

public class DeviceResourceMeta extends UIComponentBaseMeta {

    @Property(defaultValue="false", tlddoc = "Flag indicating whether to use " +
        "Smart App Banners on iOS. " +
        "If true (default), the deviceResource tag will cause IOS 6 to"
            +" render a Smart App Banner for the application, allowing the user to install ICEmobile SX,"
            +"  if it is not already installed, and open it, if the app is installed."
            +" If the user chooses to open the installed app, the session will be auto-registered.")   
    private boolean includeIOSSmartAppBanner = false;

    @Property(tlddoc = "Resource library name as also used by the " +   
            "h:outputStyleSheet component.")
    private String library;

    @Property(tlddoc = "View size name, values \"small\" or \"large\".")
    private String view;

}
