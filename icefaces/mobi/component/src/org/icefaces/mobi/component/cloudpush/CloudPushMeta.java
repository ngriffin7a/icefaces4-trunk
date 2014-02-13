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

package org.icefaces.mobi.component.cloudpush;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.baseMeta.UIComponentBaseMeta;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

@Component(
        tagName = "cloudPush",
        componentClass = "org.icefaces.mobi.component.cloudpush.CloudPush",
        rendererClass = "org.icefaces.mobi.component.cloudpush.CloudPushRenderer",
        generatedClass = "org.icefaces.mobi.component.cloudpush.CloudPushBase",
        componentType = "org.icefaces.CloudPush",
        rendererType = "org.icefaces.CameraRenderer",
        extendsClass = "javax.faces.component.UIComponentBase",
        componentFamily = "org.icefaces.CloudPush",
        tlddoc = "Renders a button to enable Cloud Push on the page."
)

@ResourceDependencies({
        @ResourceDependency(library = "icefaces.mobi", name = "core/bridgeit.js"),
        @ResourceDependency(library = "org.icefaces.component.util", name = "component.js")
})
public class CloudPushMeta extends UIComponentBaseMeta {

    @Property(defaultValue="Enable Cloud Push", tlddoc="The label to be displayed on the button.")
    private String buttonLabel;

}
