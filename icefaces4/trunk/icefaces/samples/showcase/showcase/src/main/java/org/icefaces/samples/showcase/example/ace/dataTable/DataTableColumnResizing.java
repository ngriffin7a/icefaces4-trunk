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

@ManagedBean(name= DataTableColumnResizing.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class DataTableColumnResizing implements Serializable {
    public static final String BEAN_NAME = "dataTableColumnResizing";
	public String getBeanName() { return BEAN_NAME; }
    
    private List<Car> cars;
    /////////////---- CONSTRUCTOR BEGIN
    public DataTableColumnResizing() 
    {
        this.cars = new ArrayList<Car>(DataTableData.getDefaultData());
    }

    /////////////---- GETTERS & SETTERS BEGIN
    public List<Car> getCars() { return cars; }
    public void setCars(List<Car> cars) { this.cars = cars; }
}
