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

package org.icefaces.samples.showcase.example.ace.gMapLayer;

import java.io.Serializable;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;

@ManagedBean(name= GMapLayerBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class GMapLayerBean implements Serializable{
    public static final String BEAN_NAME = "gMapLayerBean";
	public String getBeanName() { return BEAN_NAME; }
	private boolean bikeLayer = false;
    private boolean kmlLayer = false;
    private boolean trafficLayer = false;
    private boolean transitLayer = false;
    private String chosenLayer;
    private String kmlURL = "http://coinatlantic.ca/kmlfiles/EnvironmentCanadaAtlantic/ECHydrometricNL.kml";
    private double lat = 40.7142;
    private double lon = -74.0064;

    public boolean isBikeLayer() {
        return bikeLayer;
    }

    public void setBikeLayer(boolean bikeLayer) {
        this.bikeLayer = bikeLayer;
    }

    public boolean isKmlLayer() {
        return kmlLayer;
    }

    public void setKmlLayer(boolean kmlLayer) {
        this.kmlLayer = kmlLayer;
    }

    public boolean isTrafficLayer() {
        return trafficLayer;
    }

    public void setTrafficLayer(boolean trafficLayer) {
        this.trafficLayer = trafficLayer;
    }

    public boolean isTransitLayer() {
        return transitLayer;
    }

    public void setTransitLayer(boolean transitLayer) {
        this.transitLayer = transitLayer;
    }

    public String getChosenLayer() {
        return chosenLayer;
    }

    public void setChosenLayer(String chosenLayer) {
        this.chosenLayer = chosenLayer;
        if (chosenLayer.equalsIgnoreCase("Biking"))
        {
            bikeLayer=true;
            trafficLayer=false;
            transitLayer=false;
            kmlLayer=false;
            lat = 40.7142;
            lon = -74.0064;
        }
        else if (chosenLayer.equalsIgnoreCase("Transit"))
        {
            bikeLayer=false;
            trafficLayer=false;
            transitLayer=true;
            kmlLayer=false;
            lat = 40.7142;
            lon = -74.0064;
        }
        else if (chosenLayer.equalsIgnoreCase("Traffic"))
        {
            bikeLayer=false;
            trafficLayer=true;
            transitLayer=false;
            kmlLayer=false;
            lat = 40.7142;
            lon = -74.0064;
        }
        else if (chosenLayer.equalsIgnoreCase("kml"))
        {
            bikeLayer=false;
            trafficLayer=false;
            transitLayer=false;
            kmlLayer=true;
            lat = 40.7143;
            lon = -74.0063;
        }
    }

    public String getKmlURL() {
        return kmlURL;
    }

    public void setKmlURL(String kmlURL) {
        this.kmlURL = kmlURL;
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
}
