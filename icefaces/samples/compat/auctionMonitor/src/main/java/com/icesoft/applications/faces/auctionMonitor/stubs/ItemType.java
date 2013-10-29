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

package com.icesoft.applications.faces.auctionMonitor.stubs;

import java.io.Serializable;
import java.net.URL;
import java.util.Calendar;

/**
 * Class that stores and manages all information for a single auction item
 * Example item information is title, startPrice, etc.
 */
public class ItemType implements Serializable {
    private URL picture;
    private String title;
    private String itemID;
    private String description;
    private String location;
    private String seller;
    private Calendar endTime;

    public ItemType() {
    }

    public ItemType(ItemType parent) {
        this.picture = parent.getPictureURL();
        this.title = parent.getTitle();
        this.itemID = parent.getItemID();
        this.description = parent.getDescription();
        this.location = parent.getLocation();
        this.seller = parent.getSeller();
        this.endTime = parent.getEndTimeCal();
    }

    public Calendar getEndTimeCal() {
        return endTime;
    }

    public String getSeller() {
        return seller;
    }

    public String getTitle() {
        return title;
    }

    public String getItemID() {
        return itemID;
    }

    public URL getPictureURL() {
        return picture;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public void setEndTimeCal(Calendar endTime) {
        this.endTime = endTime;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    public void setPictureURL(URL picture) {
        this.picture = picture;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
