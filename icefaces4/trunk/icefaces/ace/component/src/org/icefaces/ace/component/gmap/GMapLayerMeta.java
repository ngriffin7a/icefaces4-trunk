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
        tagName = "gMapLayer",
        componentClass = "org.icefaces.ace.component.gmap.GMapLayer",
        rendererClass = "org.icefaces.ace.component.gmap.GMapLayerRenderer",
        generatedClass = "org.icefaces.ace.component.gmap.GMapLayerBase",
        extendsClass = "javax.faces.component.UIPanel",
        componentType = "org.icefaces.ace.component.GMapLayer",
        rendererType = "org.icefaces.ace.component.GMapLayerRenderer",
        componentFamily = "org.icefaces.ace.component",
        tlddoc = "The ace:gMapLayer component creates one of a variety of layers on top of the parent ace:gMap, to enhance or display new information. " +
                "Google provides Bicycling, Traffic and Transit layers, which display bike paths, traffic flow and public transportations routes respectively. " +
                "Alternatively, a user defined kml file can also be used." +
                " For more information, see the <a href=\"http://wiki.icefaces.org/display/ICE/GMap\">gMap</a> Wiki Documentation."
)

@ICEResourceLibrary(ACEResourceNames.ACE_LIBRARY)
@ICEResourceDependencies({
    @ICEResourceDependency(name = ACEResourceNames.COMPONENTS_JS)
})
public class GMapLayerMeta extends UIPanelMeta {

    @Property(tlddoc = "The type of layer that you wish to create. Options are 'Bicycling', 'Traffic', 'Transit', 'Fusion' and 'Kml'. (Case insensitive)")
    private String layerType;

    @Property(tlddoc = "The URL from which to draw kml data.")
    private String url;

    @Property(tlddoc = "Additional options to be sent to the layer. Check google maps API for more specifics at https://developers.google.com/maps/documentation/javascript/reference#KmlLayerOptions. Form is attribute:'value'.", defaultValue = "Skip")
    private String options;

    @Property(tlddoc = "Whether to make the chosen layer visible or not.")
    private boolean visible;

}
