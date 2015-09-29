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

package org.icefaces.samples.showcase.example.ace.gMapMarker;

import java.io.Serializable;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;

@ManagedBean(name= GMapMarkerBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class GMapMarkerBean implements Serializable {
	public static final String BEAN_NAME = "gMapMarkerBean";
	public String getBeanName() { return BEAN_NAME; }
    private Double[] latList = {0.0,7.5,-10.0};
    private Double[] longList = {0.0,7.5,-10.0};
    private String[] optionsList = {"title:'Hover mouse over this marker to see title'","raiseOnDrag:false,draggable:true","draggable:true"};

    public Double[] getLatList() {
        return latList;
    }

    public void setLatList(Double[] latList) {
        this.latList = latList;
    }

    public Double[] getLongList() {
        return longList;
    }

    public void setLongList(Double[] longList) {
        this.longList = longList;
    }

    public String[] getOptionsList() {
        return optionsList;
    }

    public void setOptionsList(String[] optionsList) {
        this.optionsList = optionsList;
    }
}
