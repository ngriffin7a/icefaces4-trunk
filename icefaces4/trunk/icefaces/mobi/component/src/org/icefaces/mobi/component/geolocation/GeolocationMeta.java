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

package org.icefaces.mobi.component.geolocation;


import org.icefaces.ace.meta.annotation.ClientBehaviorHolder;
import org.icefaces.ace.meta.annotation.ClientEvent;
import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.baseMeta.UIComponentBaseMeta;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

@Component(
    tagName = "geolocation",
    componentClass = "org.icefaces.mobi.component.geolocation.Geolocation",
    rendererClass = "org.icefaces.mobi.component.geolocation.GeolocationRenderer",
    generatedClass = "org.icefaces.mobi.component.geolocation.GeolocationBase",
    componentType = "org.icefaces.Geolocation",
    rendererType = "org.icefaces.GeolocationRenderer",
    extendsClass = "javax.faces.component.UIComponentBase",
    componentFamily = "org.icefaces.Geolocation",
    tlddoc = "geolocation captures a geolocation object" +
        " of longitude and latitude, heading, speed and altitude via" +
        " html5 navigator API."
)
@ResourceDependencies({
                          @ResourceDependency(library = "org.icefaces.component.util", name = "component.js")
                      })
/*@ClientBehaviorHolder(events = {
    @ClientEvent(name = "activate", javadoc = "...", tlddoc = "...", defaultRender = "@this", defaultExecute = "@all")
}, defaultEvent = "activate") */
public class GeolocationMeta extends UIComponentBaseMeta {

    @Property(tlddoc = "Latitude of mobile device in decimal degrees.")
    private Double latitude;

    @Property(tlddoc = "Longitude of mobile device in decimal degrees.")
    private Double longitude;

    @Property(tlddoc = "Altitude of mobile device in meters.")
    private Double altitude;

    @Property(tlddoc = "Direction of mobile device in degrees from North.")
    private Double direction;

    @Property(defaultValue = "false",
            tlddoc = org.icefaces.mobi.util.TLDConstants.DISABLED)
    private boolean disabled;

    @Property(tlddoc = org.icefaces.mobi.util.TLDConstants.TABINDEX)
    private int tabindex;

     @Property(tlddoc = org.icefaces.mobi.util.TLDConstants.STYLE)
     private String style;

     @Property(tlddoc = org.icefaces.mobi.util.TLDConstants.STYLECLASS)
     private String styleClass;

    @Property(defaultValue = "false", tlddoc = org.icefaces.mobi.util.TLDConstants.SINGLESUBMIT + 
	      " When singleSubmit is false, no submit occurs. The value is simply stored in the hidden field.")
    private boolean singleSubmit;

	      @Property(tlddoc = "Determines if the component will continuously update the position. If true, a listener is used to retrieve position from navigator.geolocation.watchPosition. If false, the component will fetch a one time update via navigator.gelocation.getCurrentPosition",
              defaultValue = "true")
    private boolean continuousUpdates;

	      @Property(tlddoc = "Determines if high precision location is retrieved using GPS.  Has values of true, false, and asNeeded; where true means use GPS, false means don't use GPS, and asNeeded means use GPS only if needed to get a position.", defaultValue = "asNeeded")
    private String enableHighPrecision;

    @Property(tlddoc = "Indicates maximum age in seconds, of a cached position that is acceptable for use.",
              defaultValue = "0")
    private int maximumAge;

    @Property(tlddoc = "The maximum time in seconds to wait for success or error response. A value of 0 indicates no timeout.", defaultValue = "0")
    private int timeout;

}
