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

import org.icefaces.samples.showcase.view.navigation.NavigationController;
import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

@ComponentExample(
        parent = MapBean.BEAN_NAME,
        title = "example.compat.map.load.title",
        description = "example.compat.map.load.description",
        example = "/resources/examples/compat/map/mapLoad.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="mapLoad.xhtml",
                    resource = "/resources/examples/compat/"+
                               "map/mapLoad.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="MapLoad.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/map/MapLoad.java")
        }
)
@ManagedBean(name= MapLoad.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class MapLoad extends ComponentExampleImpl<MapLoad> implements Serializable {
	
	public static final String BEAN_NAME = "mapLoad";
	private static final String WORLD_ZOOM_LEVEL = "1";
	private static final String CUSTOM_SELECT = "CUSTOM_SELECT";
	
	private SelectItem[] availableKMLs = new SelectItem[] {
	    new SelectItem("http://coinatlantic.ca/kmlfiles/EnvironmentCanadaAtlantic/ECHydrometricNL.kml", "Atlantic Canada Hydrometric"),
	    new SelectItem("http://www.rivercitynetworks.com/weather_radar_usa.kml", "USA Weather"),
	    new SelectItem("http://www.austindarts.org/darts.kml", "Darts in Austin, Texas"),
	    new SelectItem("http://www.maceratameteo.it/GMStrikes.kml", "Italy Lightning Strikes"),
	    new SelectItem("http://www.ourairports.com/countries/CA/NT/airports.kml", "Northwest Territories Airports"),
	    new SelectItem("http://www.scribblemaps.com/maps/kml/KatieMowat.kml", "Immigration Origin")
	};
	private String selectedKML = availableKMLs[0].getValue().toString();
	private String customKML = availableKMLs[0].getValue().toString();
	private String zoomLevel = WORLD_ZOOM_LEVEL;

	public MapLoad() {
		super(MapLoad.class);
	}
	
    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

	public String getCustomSelect() { return CUSTOM_SELECT; }
	public boolean getIsCustom() {
	    return CUSTOM_SELECT.equals(selectedKML);
	}
	public String getCurrentKML() {
	    return getIsCustom() ? customKML : selectedKML;
	}
	
	public SelectItem[] getAvailableKMLs() { return availableKMLs; }
	public String getSelectedKML() { return selectedKML; }
	public String getCustomKML() { return customKML; }
	public String getZoomLevel() { return zoomLevel; }
	
	public void setAvailableKMLs(SelectItem[] availableKMLs) { this.availableKMLs = availableKMLs; }
	public void setSelectedKML(String selectedKML) { this.selectedKML = selectedKML; }
	public void setCustomKML(String customKML) { this.customKML = customKML; }
	public void setZoomLevel(String zoomLevel) { this.zoomLevel = zoomLevel; }
	
	public void applyChanges(ActionEvent event) {
	    // Zoom out so the user can see any loaded points wherever they are on the globe
	    setZoomLevel(WORLD_ZOOM_LEVEL);
	}
}
