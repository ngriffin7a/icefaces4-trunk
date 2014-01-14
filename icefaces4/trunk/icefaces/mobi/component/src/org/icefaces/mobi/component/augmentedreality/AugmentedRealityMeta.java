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
package org.icefaces.mobi.component.augmentedreality;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.annotation.Required;
import org.icefaces.ace.meta.baseMeta.UIComponentBaseMeta;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

@Component(
        tagName = "augmentedReality",
        componentClass = "org.icefaces.mobi.component.augmentedreality.AugmentedReality",
        rendererClass = "org.icefaces.mobi.component.augmentedreality.AugmentedRealityRenderer",
        generatedClass = "org.icefaces.mobi.component.augmentedreality.AugmentedRealityBase",
        componentType = "org.icefaces.AugmentedReality",
        rendererType = "org.icefaces.AugmentedRealityRenderer",
        extendsClass = "javax.faces.component.UIInput",
        componentFamily = "org.icefaces.AugmentedReality",
        tlddoc = "Renders a button that will launch an Augmented Reality view" +
                " displaying any child marker models or locations."
)

@ResourceDependencies({
        @ResourceDependency(library = "org.icefaces.component.util", name = "component.js")
})
public class AugmentedRealityMeta extends UIComponentBaseMeta  {
    @Property(tlddoc="Additional parameters for experimental features.", 
            required = Required.no)
    private String params;

    @Property(tlddoc="URL base used as a prefix for loading resources in AR view.",
            required = Required.no)
    private String urlBase;

     @Property(tlddoc = org.icefaces.mobi.util.TLDConstants.STYLE)
     private String style;

     @Property(tlddoc = org.icefaces.mobi.util.TLDConstants.STYLECLASS)
     private String styleClass;

    @Property(tlddoc = "The current value, typically the user-selected location or marker.", required = Required.no)
    private String value;
	
	@Property(tlddoc = "The label to display on the augmented reality button.", defaultValue="Reality", required = Required.no)
    private String buttonLabel;

    @Property(defaultValue="Reality Captured", tlddoc = "The button label displayed when Augmented Reality input has been captured.",
        required = Required.no)
    private String captureMessageLabel;

}
