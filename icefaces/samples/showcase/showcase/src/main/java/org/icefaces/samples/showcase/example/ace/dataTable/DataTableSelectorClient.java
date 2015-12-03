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
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.icefaces.samples.showcase.dataGenerators.utilityClasses.DataTableData;

@ManagedBean(name= DataTableSelectorClient.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class DataTableSelectorClient implements Serializable {
    public static final String BEAN_NAME = "dataTableSelectorClient";
	public String getBeanName() { return BEAN_NAME; }

    private static final SelectItem[] AVAILABLE_MODES = { new SelectItem("multiple", "Multiple Rows"),
                                                          new SelectItem("multiplecell", "Multiple Cell") };

    private String selectionMode = AVAILABLE_MODES[0].getValue().toString();
    private List<Car> carsData;
    
    /////////////---- CONSTRUCTOR BEGIN
    public DataTableSelectorClient() {
        carsData = new ArrayList<Car>(DataTableData.getDefaultData());
    }

    /////////////---- VALUE CHANGE LISTENERS BEGIN
    public void changedMode(ValueChangeEvent event) {
    }
	
    /////////////---- GETTERS & SETTERS BEGIN
    public String getSelectionMode() { return selectionMode; }
    public SelectItem[] getAvailableModes() { return AVAILABLE_MODES; }
    public List<Car> getCarsData() { return carsData; }
    public void setSelectionMode(String selectionMode) { this.selectionMode = selectionMode; }
    public void setCarsData(List<Car> carsData) { this.carsData = carsData; }

}
