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

package org.icefaces.ace.component.gmap;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.baseMeta.UIPanelMeta;

import org.icefaces.ace.resources.ACEResourceNames;
import org.icefaces.resources.ICEResourceDependencies;
import org.icefaces.resources.ICEResourceDependency;
import org.icefaces.resources.ICEResourceLibrary;

@Component(
        tagName = "gMapControl",
        componentClass = "org.icefaces.ace.component.gmap.GMapControl",
        rendererClass = "org.icefaces.ace.component.gmap.GMapControlRenderer",
        generatedClass = "org.icefaces.ace.component.gmap.GMapControlBase",
        extendsClass = "javax.faces.component.UIPanel",
        componentType = "org.icefaces.ace.component.GMapControl",
        rendererType = "org.icefaces.ace.component.GMapControlRenderer",
        componentFamily = "org.icefaces.ace.component",
        tlddoc = "The ace:gMapControl component serves as an easy interface to modify the position, style and " +
                "rendered state of the controls that appear on the ace:gMap that this tag is nested within." +
                " For more information, see the <a href=\"http://wiki.icefaces.org/display/ICE/GMap\">gMap</a> Wiki Documentation."
)

@ICEResourceLibrary(ACEResourceNames.ACE_LIBRARY)
@ICEResourceDependencies({
    @ICEResourceDependency(name = ACEResourceNames.COMPONENTS_JS)
})
public class GMapControlMeta extends UIPanelMeta {
    @Property(tlddoc = "The name of the control to affect. Valid entries are: 'all', 'type', 'overview', 'pan', 'rotate', 'scale', 'streetView' and 'zoom'.")
    private String name;

    @Property(tlddoc = "The location for the chosen control to display. Valid entries are 'bottomCenter', 'bottomLeft', 'bottomRight', 'leftBottom', 'leftCenter', 'leftTop', 'rightBottom', 'rightCenter', 'rightTop', 'topCenter', 'topLeft' and 'topRight'.", defaultValue = "none")
    private String position;

    @Property(tlddoc = "The style that will be used on the control. Will only affect Zoom and Type controls. Type style options are: 'default', 'dropdown' and 'bar'. Zoom style options are 'default', 'large' and 'small'. ", defaultValue = "none")
    private String controlStyle;

    @Property(tlddoc = "Choose whether or not to display the chosen map control.", defaultValue = "false")
    private boolean disabled;
}
