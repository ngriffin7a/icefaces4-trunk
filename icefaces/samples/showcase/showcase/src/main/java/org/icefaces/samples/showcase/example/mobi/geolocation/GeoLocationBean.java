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

package org.icefaces.samples.showcase.example.mobi.geolocation;


import org.icefaces.mobi.util.MobiJSFUtils;
//import org.icemobile.samples.mobileshowcase.view.metadata.annotation.*;
//import org.icemobile.samples.mobileshowcase.view.metadata.context.ExampleImpl;
import org.icefaces.ace.util.ClientDescriptor;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import java.io.Serializable;
import java.util.ArrayList;
/*
@Destination(
    title = "example.input.geolocation.destination.title.short",
    titleExt = "example.input.geolocation.destination.title.long",
    titleBack = "example.input.geolocation.destination.title.back"
)
@Example(
    descriptionPath = "/WEB-INF/includes/examples/input/geolocation-desc.xhtml",
    examplePath = "/WEB-INF/includes/examples/input/geolocation-example.xhtml",
    resourcesPath = "/WEB-INF/includes/examples/example-resources.xhtml"
)
@ExampleResources(
    resources = {
        // xhtml
        @ExampleResource(type = ResourceType.xhtml,
                         title = "geolocation-example.xhtml",
                         resource = "/WEB-INF/includes/examples/input/geolocation-example.xhtml"),
        // Java Source
        @ExampleResource(type = ResourceType.java,
                         title = "GeoLocationBean.java",
                         resource = "/WEB-INF/classes/org/icemobile/samples/mobileshowcase" +
                             "/view/examples/input/geolocation/GeoLocationBean.java")
    }
)
*/
@ManagedBean(name = GeoLocationBean.BEAN_NAME)
@SessionScoped
public class GeoLocationBean /*extends ExampleImpl<GeoLocationBean>*/ implements
    Serializable {

    public static final String BEAN_NAME = "geoLocationBean";

    private double latitude = 0.0;
    private double longitude = 0.0;
    private double altitude = 0.0;
    private double direction = 0.0;

    private int timeout = 30;
    private int maximumAge = 3600;
    private String enableHighPrecision = "asNeeded";
    private boolean continuousUpdates = true;

    public GeoLocationBean() {
        //super(GeoLocationBean.class);
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLongitudeRead() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitudeRead() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    // lat and long values are read only, no value writing takes place.
    public void setLongitudeRead(double longitude) {
    }

    public void setLatitudeRead(double latitude) {
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public double getDirection() {
        return direction;
    }

    public void setDirection(double direction) {
        this.direction = direction;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getMaximumAge() {
        return maximumAge;
    }

    public void setMaximumAge(int maximumAge) {
        this.maximumAge = maximumAge;
    }

    public String getEnableHighPrecision() {
        return enableHighPrecision;
    }

    public void setEnableHighPrecision(String enableHighPrecision) {
        this.enableHighPrecision = enableHighPrecision;
    }

    public boolean isContinuousUpdates() {
        return continuousUpdates;
    }

    public void setContinuousUpdates(boolean continuousUpdates) {
        this.continuousUpdates = continuousUpdates;
    }

    public void continuousUpdatesChange(ValueChangeEvent event) {
        continuousUpdates = ((Boolean) event.getNewValue()).booleanValue();
    }
    
    public void resetValues(ActionEvent evt){
        this.latitude = 0;
        this.longitude = 0;
        this.altitude = 0;
        this.direction = 0;
    }
}