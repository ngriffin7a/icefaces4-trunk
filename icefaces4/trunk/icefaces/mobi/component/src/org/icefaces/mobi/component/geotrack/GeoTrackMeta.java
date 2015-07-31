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

package org.icefaces.mobi.component.geotrack;


import org.icefaces.ace.meta.annotation.ClientBehaviorHolder;
import org.icefaces.ace.meta.annotation.ClientEvent;
import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.annotation.Facet;
import org.icefaces.ace.meta.annotation.Facets;
import org.icefaces.ace.meta.baseMeta.UIComponentBaseMeta;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;

@Component(
    tagName = "geoTrack",
    componentClass = "org.icefaces.mobi.component.geotrack.GeoTrack",
    rendererClass = "org.icefaces.mobi.component.geotrack.GeoTrackRenderer",
    generatedClass = "org.icefaces.mobi.component.geotrack.GeoTrackBase",
    componentType = "org.icefaces.GeoTrack",
    rendererType = "org.icefaces.GeoTrackRenderer",
    extendsClass = "javax.faces.component.UIComponentBase",
    componentFamily = "org.icefaces.GeoTrack",
    tlddoc = "The geoTrack component renders a button that allows geotracking to be initiated through the bridgeit.js API. After Bridgeit geotracking is initiated, Bridget will send geoJSON data to the server as well as any custom parameters defined in the component tag. This data will be published to the application-scoped bean property specified by the attribute 'publish'. Because of the way the Bridgeit geotracking feature works, a user can be tracked for hours without necessarily having an active session. Because of this and because of the fact that the data is sent in a non-JSF request, only application-scoped beans are supported. The data sent by Bridgeit will be in JSON format. The custom parameters will be under the 'properties' object."
)
@ResourceDependencies({
        @ResourceDependency(library = "icefaces.mobi", name = "core/bridgeit.js"),
        @ResourceDependency(library = "org.icefaces.component.util", name = "component.js")
})
public class GeoTrackMeta extends UIComponentBaseMeta {

    @Property(tlddoc = "Three strategies are currently supported: 'continuous' where the location of the device will be uploaded as frequently as it changes (intended for testing only due to high power consumption), 'significant' where the location is uploaded when it changes significantly, and 'stop' to cease location tracking.", defaultValue="continuous")
    private String strategy;

    @Property(tlddoc = "The duration in hours.", defaultValue="1.0")
    private double duration;

    @Property( tlddoc = org.icefaces.mobi.util.TLDConstants.TABINDEX)
    private int tabindex;

    @Property(tlddoc = org.icefaces.mobi.util.TLDConstants.STYLE)
    private String style;

    @Property(tlddoc = org.icefaces.mobi.util.TLDConstants.STYLECLASS)
    private String styleClass;

    @Property(defaultValue = "false",
            tlddoc = org.icefaces.mobi.util.TLDConstants.DISABLED)
    private boolean disabled;

    @Property(defaultValue="Geotrack", tlddoc="The label to be displayed on the button.")
    private String buttonLabel;

	@Property(tlddoc="The application-scoped bean property where the geoJSON data and other parameters will be published to.")
	private String publish;

    @Property(tlddoc="Custom parameters that will be echoed by Bridgeit whenever it sends geotracking data to the server. These parameters can be used to identify individual users, pages, etc. The parameters must be in correct JSON format (without the opening/closing brackets), and the names should always start with the underscore character (e.g. \"_viewId: '/geotracking.jsf', _userId: '001'\").", defaultValue="")
    private String parameters;

    @Facets
    class FacetsMeta{
        @Facet(tlddoc = "Allows rendering of nested components that are displayed if the BridgeIt app cannot be used, either due to running on an unsupported platform (such as a desktop OS), or the BridgeIt app not being installed on the device.")
        UIComponent fallback;
    }
}
