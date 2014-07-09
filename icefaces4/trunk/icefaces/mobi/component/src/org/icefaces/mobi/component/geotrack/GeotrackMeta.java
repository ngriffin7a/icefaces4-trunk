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
    tagName = "geotrack",
    componentClass = "org.icefaces.mobi.component.geotrack.Geotrack",
    rendererClass = "org.icefaces.mobi.component.geotrack.GeotrackRenderer",
    generatedClass = "org.icefaces.mobi.component.geotrack.GeotrackBase",
    componentType = "org.icefaces.Geotrack",
    rendererType = "org.icefaces.GeotrackRenderer",
    extendsClass = "javax.faces.component.UIComponentBase",
    componentFamily = "org.icefaces.Geotrack",
    tlddoc = "The geotrack component renders a button that allows geotracking to be initiated through the bridgeit.js api."
)
@ResourceDependencies({
        @ResourceDependency(library = "icefaces.mobi", name = "core/bridgeit.js"),
        @ResourceDependency(library = "org.icefaces.component.util", name = "component.js")
})
public class GeotrackMeta extends UIComponentBaseMeta {

    @Property(tlddoc = "Three strategies are currently supported: 'continuous' where the location of the device will be uploaded as frequently as it changes (intended for testing only due to high power consumption), 'significant' where the location is uploaded when it changes significantly, and 'stop' to cease location tracking.", defaultValue="stop")
    private String strategy;

    @Property(tlddoc = "The duration in hours.", defaultValue="1")
    private int duration;

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

	@Property(tlddoc="The application-scoped bean property where the geoJSON data will be published to.")
	private Object publish;

    @Facets
    class FacetsMeta{
        @Facet(tlddoc = "Allows rendering of nested components that are displayed if the BridgeIt app cannot be used, either due to running on an unsupported platform (such as desktop OS), or the BridgeIt app not being installed on the device.")
        UIComponent fallback;
    }
}
