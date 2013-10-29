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
        tagName = "gMapOverlay",
        componentClass = "org.icefaces.ace.component.gmap.GMapOverlay",
        rendererClass = "org.icefaces.ace.component.gmap.GMapOverlayRenderer",
        generatedClass = "org.icefaces.ace.component.gmap.GMapOverlayBase",
        extendsClass = "javax.faces.component.UIPanel",
        componentType = "org.icefaces.ace.component.GMapOverlay",
        rendererType = "org.icefaces.ace.component.GMapOverlayRenderer",
        componentFamily = "org.icefaces.ace.component",
        tlddoc = "The ace:gMapOverlay component allows for the creation of user defined shapes on the parent ace:gMap. Google provides rectangles and circles for ease of use, but polygon and polyline can be used for greater control." +
                " Colours and borders can be set through options." +
                " For more information, see the <a href=\"http://wiki.icefaces.org/display/ICE/GMap\">gMap</a> Wiki Documentation."
)

@ICEResourceLibrary(ACEResourceNames.ACE_LIBRARY)
@ICEResourceDependencies({
    @ICEResourceDependency(name = ACEResourceNames.COMPONENTS_JS)
})
public class GMapOverlayMeta extends UIPanelMeta {

    @Property(tlddoc = "The name of the shape overlay you want to create. Valid entries are: 'Line', 'Polygon', 'Rectangle', 'Circle'  (Case insensitive).")
    private String shape;

    @Property(tlddoc = "The points that the service is applied to. Format is (lat,long) or, for Directions and Distance only, a standard address. Separate points with ':'.")
    private String points;

    @Property(tlddoc = "Additional options to be sent to the service. Check google maps API for more specifics at https://developers.google.com/maps/documentation/javascript/reference#PolylineOptions. Form is attribute:'value'.")
    private String options;

}
