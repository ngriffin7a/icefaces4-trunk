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

package org.icefaces.samples.showcase.example.ace.dataTable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;

import org.icefaces.samples.showcase.dataGenerators.utilityClasses.DataTableData;

@ManagedBean(name= DataTableScrolling.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class DataTableScrolling implements Serializable {
    public static final String BEAN_NAME = "dataTableScrolling";
	public String getBeanName() { return BEAN_NAME; }
    
    private boolean scrolling = true;
    private int height = 200;
    private List<Car> carsData;
    /////////////---- CONSTRUCTOR BEGIN
    public DataTableScrolling() {
        carsData = new ArrayList<Car>(DataTableData.getDefaultData());
    }

    /////////////---- GETTERS & SETTERS BEGIN
    public List<Car> getCarsData() { return carsData; }
    public void setCarsData(List<Car> carsData) { this.carsData = carsData; }
    public boolean getScrolling() { return scrolling; }
    public int getHeight() { return height; }
    public void setScrolling(boolean scrolling) { this.scrolling = scrolling; }
    public void setHeight(int height) { this.height = height; }
}
