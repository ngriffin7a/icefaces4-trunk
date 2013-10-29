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

package org.icefaces.samples.showcase.example.compat.map;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.icefaces.samples.showcase.util.FacesUtils;
import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

@ComponentExample(
        parent = MapBean.BEAN_NAME,
        title = "example.compat.map.latlong.title",
        description = "example.compat.map.latlong.description",
        example = "/resources/examples/compat/map/mapLatLong.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="mapLatLong.xhtml",
                    resource = "/resources/examples/compat/"+
                               "map/mapLatLong.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="MapLatLong.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/map/MapLatLong.java")
        }
)
@ManagedBean(name= MapLatLong.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class MapLatLong extends ComponentExampleImpl<MapLatLong> implements Serializable {
	
	public static final String BEAN_NAME = "mapLatLong";
	private static final String CUSTOM_SELECT = "CUSTOM_COORD";
	
	private SelectItem[] availableCoordinates = new SelectItem[] {
	    new SelectItem("52.0,4.0", "Amsterdam, Netherlands"),
	    new SelectItem("37.0,23.0", "Athens, Greece"),
	    new SelectItem("39.0,116.0", "Beijing, China"),
	    new SelectItem("54.0,-5.0", "Belfast, Northern Ireland"),
	    new SelectItem("52.0,13.0", "Berlin, Germany"),
	    new SelectItem("19.0,72.0", "Bombay, India"),
	    new SelectItem("51.0,-114.0", "Calgary, Canada"),
	    new SelectItem("30.0,31.0", "Cairo, Egypt"),
	    new SelectItem("60.0,25.0", "Helsinki, Finland"),
	    new SelectItem("51.0,0.0", "London, England"),
	    new SelectItem("19.0,-99.0", "Mexico City, Mexico"),
	    new SelectItem("55.0,37.0", "Moscow, Russia"),
	    new SelectItem("48.0,2.0", "Paris, France"),
	    new SelectItem("-22.0,-43.0", "Rio de Janeiro, Brazil"),
	    new SelectItem("41.0,12.0", "Rome, Italy"),
	    new SelectItem("-34.0,151.0", "Sydney, Australia"),
	    new SelectItem("35.0,139.0", "Tokyo, Japan"),
	    new SelectItem("38.0,-77.0", "Washington, USA")
	};
	private double latitude;
	private double longitude;
	private String selectedCoordinates = availableCoordinates[0].getValue().toString();

	public MapLatLong() {
		super(MapLatLong.class);
	}
	
    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

	public String getCustomSelect() { return CUSTOM_SELECT; }
	public SelectItem[] getAvailableCoordinates() { return availableCoordinates; }
	public double getLatitude() { return latitude; }
	public double getLongitude() { return longitude; }
	public String getSelectedCoordinates() { return selectedCoordinates; }
	
	public void setAvailableCoordinates(SelectItem[] availableCoordinates) { this.availableCoordinates = availableCoordinates; }
	public void setLatitude(double latitude) { this.latitude = latitude; }
	public void setLongitude(double longitude) { this.longitude = longitude; }
	public void setSelectedCoordinates(String selectedCoordinates) { this.selectedCoordinates = selectedCoordinates; }
	
	public void applyChanges(ActionEvent event) {
	    if ((!FacesUtils.isBlank(selectedCoordinates)) &&
	        (!CUSTOM_SELECT.equals(selectedCoordinates))) {
	        String[] splitCoordinates = selectedCoordinates.split(",");
	        
	        if ((splitCoordinates != null) && (splitCoordinates.length == 2)) {
                latitude = Double.parseDouble(splitCoordinates[0]);
                longitude = Double.parseDouble(splitCoordinates[1]);
            }
	    }
	}
}
