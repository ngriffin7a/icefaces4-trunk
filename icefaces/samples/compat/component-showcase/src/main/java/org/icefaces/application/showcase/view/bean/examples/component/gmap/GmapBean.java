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

package org.icefaces.application.showcase.view.bean.examples.component.gmap;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import com.icesoft.faces.component.gmap.GMapLatLng;

/**
 * <p>The GmapBean is responsible for setting up default map markers,
 * as well as the selecting of cities, searching for addresses, and toggling
 * the visibility of the map ui controls.</p>
 *
 * @since 1.7
 */
@ManagedBean(name = "gmap")
@ViewScoped
public class GmapBean  implements Serializable {
    // address to search for
	private String geoCoderAddress = "";
    // city location selected from a preset list
	private String standardAddress = "";
    // whether we should search for an address or not
	private boolean locateAddress = false;
	private boolean showControls = true;
    // value bound to the gmap component
	private String address = "";
	
	public GmapBean() {
        // Generate a set of default map marker locations
	}
	
	public String getStandardAddress() {
		return standardAddress;
	}

	public void setStandardAddress(String standardAddress) {	
		this.standardAddress = standardAddress;
		this.address = standardAddress;
	}

    public String getGeoCoderAddress() {
        return geoCoderAddress;
    }

	public void setGeoCoderAddress(String geoCoderAddress) {
		this.geoCoderAddress = geoCoderAddress;
	}
	
    public boolean isShowControls() {
        return showControls;
    }
    
    public void setShowControls(boolean showControls) {
        this.showControls = showControls;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    /**
     * Method called when the actionListener is fired on the locate address field
     *
     *@param event of the key press
     */
	public void enterKeyPressed(ActionEvent event) {
	  address = geoCoderAddress;
      locateAddress = true;
	}
	
    /**
     * Method to determine if we should use a preset address or search for an
     *  address
     *
     *@return true to locate an address, false otherwise
     */
	public boolean isLocateAddress() {
		if (locateAddress) {
			locateAddress = false;
			return true;
		}
		return false;
	}
	
    /**
     * Method called when we should search the map for an address
     * This happens when the preset location list is modified
     *
     *@param event of the change
     */
	public void findAddress(ValueChangeEvent event) {
		locateAddress = true;
	}
}
