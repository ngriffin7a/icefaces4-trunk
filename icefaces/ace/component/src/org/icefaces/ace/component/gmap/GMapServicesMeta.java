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
        tagName = "gMapServices",
        componentClass = "org.icefaces.ace.component.gmap.GMapServices",
        rendererClass = "org.icefaces.ace.component.gmap.GMapServicesRenderer",
        generatedClass = "org.icefaces.ace.component.gmap.GMapServicesBase",
        extendsClass = "javax.faces.component.UIPanel",
        componentType = "org.icefaces.ace.component.GMapServices",
        rendererType = "org.icefaces.ace.component.GMapServicesRenderer",
        componentFamily = "org.icefaces.ace.component",
        tlddoc = "The ace:gMapServices tag allows access to the various 'services' that Google provides through its maps API. Elevation gives the height of any point above or below sea level. Max zoom gives the highest level of satellite zoom available. " +
                "Distance gives the distance between two points along a set mode of transit and directions show the way between given locations." +
                " For more information, see the <a href=\"http://wiki.icefaces.org/display/ICE/GMap\">gMap</a> Wiki Documentation."
)

@ICEResourceLibrary(ACEResourceNames.ACE_LIBRARY)
@ICEResourceDependencies({
    @ICEResourceDependency(name = ACEResourceNames.COMPONENTS_JS)
})
public class GMapServicesMeta extends UIPanelMeta {

    @Property(tlddoc = "The name of the service type you want to create. Valid entries are: 'Directions', 'Elevation', 'MaxZoom', 'Distance' (Case insensitive).")
    private String name;

    @Property(tlddoc = "The points that the service is applied to. Format is (lat,long) or, for Directions and Distance only, a standard address. Separate points with ':'.")
    private String points;

    @Property(tlddoc = "Additional options to be sent to the service. Check google maps API for more specifics at https://developers.google.com/maps/documentation/javascript/reference. Form is attribute:'value'.", defaultValue = "travelMode:'DRIVING'")
    private String options;

    @Property(tlddoc = "Id of the div you with to set text directions in. null or none to clear.", defaultValue = "none")
    private String div;

}
