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

package org.icefaces.samples.showcase.example.ace.rowExpansion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;

import org.icefaces.samples.showcase.dataGenerators.utilityClasses.DataTableData;
import org.icefaces.samples.showcase.example.ace.dataTable.Car;
import org.icefaces.samples.showcase.util.SimpleEntry;

@ManagedBean(name = RowExpansionBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class RowExpansionBean implements Serializable {
    public static final String BEAN_NAME = "rowExpansionBean";
	public String getBeanName() { return BEAN_NAME; }
    
    public RowExpansionBean() {
        generateCarsData();
    }
    
    ArrayList<Map.Entry<Car,List>> carsData = null;

    private void generateCarsData() {
        carsData = new ArrayList<Map.Entry<Car, List>>();
        for (Car c : DataTableData.getDefaultData()) {
            ArrayList<Map.Entry<Car, List>> detailData = new ArrayList<Map.Entry<Car, List>>();
            detailData.add(new SimpleEntry(new Car(c.getId()+1000, c.getName()+
                " Custom Spec", c.getChassis(), c.getColor(), c.getYear(),
                c.getWeight(), c.getAcceleration()*2, c.getMpg()/2,
                c.getCost()*3), null));
            carsData.add(new SimpleEntry(c, detailData));
        }
    }

    public ArrayList<Map.Entry<Car,List>> getCarsData() { return carsData;}
    public void setCarsData(ArrayList<Map.Entry<Car,List>> carsData) {this.carsData = carsData;}
}
