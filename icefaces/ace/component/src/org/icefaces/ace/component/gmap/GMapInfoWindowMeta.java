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
        tagName = "gMapInfoWindow",
        componentClass = "org.icefaces.ace.component.gmap.GMapInfoWindow",
        rendererClass = "org.icefaces.ace.component.gmap.GMapInfoWindowRenderer",
        generatedClass = "org.icefaces.ace.component.gmap.GMapInfoWindowBase",
        extendsClass = "javax.faces.component.UIPanel",
        componentType = "org.icefaces.ace.component.GMapInfoWindow",
        rendererType = "org.icefaces.ace.component.GMapInfoWindowRenderer",
        componentFamily = "org.icefaces.ace.component",
        tlddoc = "The ace:gMapInfoWindow component creates a pop-up window within the parent ace:gMap, which can be used to display either text via the 'content' attribute or " +
                "nested html or jsf tags. If placed within an ace:gMapMarker, the window will 'bind' to the marker, and move along with it." +
                " For more information, see the <a href=\"http://wiki.icefaces.org/display/ICE/GMap\">gMap</a> Wiki Documentation."
)

@ICEResourceLibrary(ACEResourceNames.ACE_LIBRARY)
@ICEResourceDependencies({
    @ICEResourceDependency(name = ACEResourceNames.COMPONENTS_JS)
})
public class GMapInfoWindowMeta extends UIPanelMeta {
    @Property(tlddoc = "The longitude for the window, will be overridden if the component is the child of a marker.")
    private String longitude;

    @Property(tlddoc = "The latitude for the window, will be overridden if the component is the child of a marker.")
    private String latitude;

    @Property(tlddoc = "The text content to be displayed within the info window. " +
            "If this component has child tags(either html or another jsf component), this attribute will not be used, placing the children within the window instead.")
    private String content;

    @Property(tlddoc = "Additional options to be sent to the window. Check google maps API for more specifics at " +
            "https://developers.google.com/maps/documentation/javascript/reference#InfoWindowOptions. Form is attribute:'value'.", defaultValue = "none")
    private String options;

    @Property(tlddoc = "Boolean value that determines whether or not the info window appears upon a click of the marker it is bound to. This attribute will not have any effect if the info window is not nested in a marker.", defaultValue = "true")
    private boolean showOnClick;

    @Property(tlddoc = "Boolean value that determines whether or not the info window will open upon loading the map, or whether it will remain hidden until the parent marker is clicked. Will only have an effect if window is nested inside a marker, and showOnClick is true.", defaultValue = "true")
    private boolean startOpen;

    @Property(tlddoc = "Set to true to remove the window from the map", defaultValue = "false")
    private Boolean disabled;
}
