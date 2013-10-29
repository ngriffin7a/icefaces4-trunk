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
        tagName = "gMapMarker",
        componentClass = "org.icefaces.ace.component.gmap.GMapMarker",
        rendererClass = "org.icefaces.ace.component.gmap.GMapMarkerRenderer",
        generatedClass = "org.icefaces.ace.component.gmap.GMapMarkerBase",
        extendsClass = "javax.faces.component.UIPanel",
        componentType = "org.icefaces.ace.component.GMapMarker",
        rendererType = "org.icefaces.ace.component.GMapMarkerRenderer",
        componentFamily = "org.icefaces.ace.component",
        tlddoc = "The ace:gMapMarker component places one of Google's markers upon the given latitude/longitude coordinates of the parent ace:gMap." +
                " For more information, see the <a href=\"http://wiki.icefaces.org/display/ICE/GMap\">gMap</a> Wiki Documentation."
)

@ICEResourceLibrary(ACEResourceNames.ACE_LIBRARY)
@ICEResourceDependencies({
    @ICEResourceDependency(name = ACEResourceNames.COMPONENTS_JS)
})

public class GMapMarkerMeta extends UIPanelMeta {
    @Property(tlddoc = "The longitude for the marker.")
    private String longitude;

    @Property(tlddoc = "The latitude for the marker.")
    private String latitude;

    @Property(tlddoc = "The animation that the marker should use. Valid values are 'bounce', 'drop', or 'none'.")
    private String animation;

    @Property(tlddoc = "Additional options to be sent to the marker. Check google maps API for more specifics at https://developers.google.com/maps/documentation/javascript/reference#MarkerOptions. Form is attribute:'value'.", defaultValue = " ")
    private String options;

    @Property(tlddoc = "Set to true to remove the marker from the map", defaultValue = "false")
    private Boolean disabled;
}
