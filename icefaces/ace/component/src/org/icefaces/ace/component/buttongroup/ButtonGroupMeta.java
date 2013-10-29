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
package org.icefaces.ace.component.buttongroup;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.baseMeta.UIComponentBaseMeta;
import org.icefaces.ace.resources.ACEResourceNames;
import org.icefaces.resources.ICEResourceDependencies;
import org.icefaces.resources.ICEResourceDependency;
import org.icefaces.resources.ICEResourceLibrary;

@Component(
        tagName = "buttonGroup",
        componentClass = "org.icefaces.ace.component.buttongroup.ButtonGroup",
        rendererClass = "org.icefaces.ace.component.buttongroup.ButtonGroupRenderer",
        generatedClass = "org.icefaces.ace.component.buttongroup.ButtonGroupBase",
        extendsClass = "javax.faces.component.UIComponentBase",
        componentType = "org.icefaces.ace.component.ButtonGroup",
        rendererType = "org.icefaces.ace.component.ButtonGroupRenderer",
        componentFamily = "org.icefaces.ace.component.ButtonGroup",
        tlddoc = "")
@ICEResourceLibrary(ACEResourceNames.ACE_LIBRARY)
@ICEResourceDependencies({
        @ICEResourceDependency(name = ACEResourceNames.COMPONENTS_JS)
})
public class ButtonGroupMeta extends UIComponentBaseMeta {
    @Property(tlddoc = "If true, only one checkboxButton in the group can be selected at one time, selecting a new checkboxButton deselects the previously selected one.", defaultValue = "false")
    private boolean mutuallyExclusive;

    @Property(tlddoc = "Header text for the button group.")
    private String header;

    @Property(tlddoc = "Style of the container element.")
    private String style;

    @Property(tlddoc = "Style class of the container element.")
    private String styleClass;
}
