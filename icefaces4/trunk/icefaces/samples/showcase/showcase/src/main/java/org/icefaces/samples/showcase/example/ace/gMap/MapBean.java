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

package org.icefaces.samples.showcase.example.ace.gMap;


import javax.faces.bean.ManagedBean;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.validator.ValidatorException;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.event.ActionEvent;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import java.util.Dictionary;
import java.util.Enumeration;
import javax.faces.bean.CustomScoped;
import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;


@ComponentExample(
    title = "example.ace.gMap.overview.title",
    description = "example.ace.gMap.overview.description",
    example = "/resources/examples/ace/gMap/gMapBasic.xhtml"
)
@ExampleResources(
    resources ={
        // xhtml
        @ExampleResource(type = ResourceType.xhtml,
                title="gMapBasic.xhtml",
                resource = "/resources/examples/ace/gMap/gMapBasic.xhtml"),
        // Java Source
        @ExampleResource(type = ResourceType.java,
                title="MapBean.java",
                resource = "/WEB-INF/classes/org/icefaces/samples/showcase/example/ace/gMap/MapBean.java")
    }
)
@Menu(
    title = "menu.ace.gMap.subMenu.title",
    menuLinks = {
        @MenuLink(title = "menu.ace.gMap.subMenu.overview",
                isDefault = true, exampleBeanName = MapBean.BEAN_NAME),
        @MenuLink(title = "menu.ace.gMap.subMenu.autocomplete",
                exampleBeanName = MapAutocompleteBean.BEAN_NAME),
		@MenuLink(title = "menu.ace.gMap.subMenu.control",
                exampleBeanName = MapControlBean.BEAN_NAME),
		@MenuLink(title = "menu.ace.gMap.subMenu.direction",
                exampleBeanName = MapDirectionBean.BEAN_NAME),
		@MenuLink(title = "menu.ace.gMap.subMenu.event",
                exampleBeanName = MapEventBean.BEAN_NAME),
		@MenuLink(title = "menu.ace.gMap.subMenu.infowindow",
                exampleBeanName = MapInfoWindowBean.BEAN_NAME),
        @MenuLink(title = "menu.ace.gMap.subMenu.layer",
                exampleBeanName = MapLayerBean.BEAN_NAME),
        @MenuLink(title = "menu.ace.gMap.subMenu.marker",
                exampleBeanName = MapMarkerBean.BEAN_NAME),
        @MenuLink(title = "menu.ace.gMap.subMenu.options",
                exampleBeanName = MapOptionsBean.BEAN_NAME),
        @MenuLink(title = "menu.ace.gMap.subMenu.overlay",
                exampleBeanName = MapOverlayBean.BEAN_NAME)
    }
)

@ManagedBean (name=MapBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class MapBean extends ComponentExampleImpl<MapBean> implements Serializable{
	public static final String BEAN_NAME = "mapBean";
    
	private double lat = 51.0453246;
	private double lon = -114.05810120000001;
    private int zoom = 4;
    private String type = "map";
    private boolean locateAddress = false;
    private String address ="Calgary Alberta";

	public MapBean() {
        super(MapBean.class);
    }
    
	public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getZoom() {
        return zoom;
    }

    public void setZoom(int zoom) {
        this.zoom = zoom;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public boolean getLocateAddress() {
        if (locateAddress) {
            locateAddress = false;

            return true;
        }

        return locateAddress;
    }
    public void setLocateAddress(boolean locateAddress) {
        this.locateAddress = locateAddress;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void lookup(ActionEvent event) {
        locateAddress = true;
		zoom = 12;
    }

    public void ajaxLookup(AjaxBehaviorEvent event){
        locateAddress = true;
		zoom = 12;
    }	

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }
	
}
