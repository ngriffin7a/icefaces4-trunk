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

package org.springframework.webflow.samples.booking;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * This class represents...
 *
 * @author Ben Simpson <ben.simpson@icesoft.com>
 *         Date: 3/4/11
 *         Time: 11:08 PM
 */
public class HotelModelBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private String sortColumn = "name";
    private boolean ascending = true;
    private Hotel selected = null;


    public List<Hotel> getHotels(SearchCriteria searchCriteria, BookingService bookingService) {
        List<Hotel> hotels = bookingService.findHotels(searchCriteria,0,sortColumn,true);
        return hotels;
    }

    public String getSortColumn() {
        return sortColumn;
    }

    public void setSortColumn(String sortColumn) {
        this.sortColumn = sortColumn;
    }

    public boolean isAscending() {
        return ascending;
    }

    public void setAscending(boolean ascending) {
        this.ascending = ascending;
    }


    public String selectHotelAction(Hotel hotel) {
        this.selected = hotel;
        return "select";
    }

    public Hotel getSelected() {
        return selected;
    }

    public void setSelected(Hotel selected) {
        this.selected = selected;
    }
}
