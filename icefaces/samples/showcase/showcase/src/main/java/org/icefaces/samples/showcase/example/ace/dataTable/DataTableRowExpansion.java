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

package org.icefaces.samples.showcase.example.ace.dataTable;

import org.icefaces.samples.showcase.example.compat.dataTable.Car;
import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;
import org.icefaces.samples.showcase.util.SimpleEntry;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;
import java.util.*;
import org.icefaces.samples.showcase.dataGenerators.utilityClasses.DataTableData;

@ComponentExample(
        parent = DataTableBean.BEAN_NAME,
        title = "example.ace.dataTable.rowexpansion.title",
        description = "example.ace.dataTable.rowexpansion.description",
        example = "/resources/examples/ace/dataTable/dataTableRowExpansion.xhtml"
)
@ExampleResources(
        resources ={
                // xhtml
                @ExampleResource(type = ResourceType.xhtml,
                        title="dataTableRowExpansion.xhtml",
                        resource = "/resources/examples/ace/dataTable/dataTableRowExpansion.xhtml"),
                // Java Source
                @ExampleResource(type = ResourceType.java,
                        title="DataTableRowExpansion.java",
                        resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                                "/example/ace/dataTable/DataTableRowExpansion.java")
        }
)
@ManagedBean(name= DataTableRowExpansion.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class DataTableRowExpansion extends ComponentExampleImpl<DataTableRowExpansion> implements Serializable {
    public static final String BEAN_NAME = "dataTableRowExpansion";

    ArrayList<Map.Entry<Car,List>> carsData = null;
    /////////////---- CONSTRUCTOR BEGIN
    public DataTableRowExpansion() {
        super(DataTableRowExpansion.class);
        generateCarsData();
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    /////////////---- PRIVATE METHODS BEGIN
    private void generateCarsData() {
        carsData = new ArrayList<Map.Entry<Car, List>>();
        for (Car c : DataTableData.getDefaultData()) {
            ArrayList<Map.Entry<Car, List>> detailData = new ArrayList<Map.Entry<Car, List>>();
            detailData.add(new SimpleEntry(new Car(c.getId()+1000, c.getName()+" Custom Spec", c.getChassis(), c.getWeight(), c.getAcceleration()*2, c.getMpg()/2, c.getCost()*3), null));
            carsData.add(new SimpleEntry(c, detailData));
        }
    }
    /////////////---- GETTERS & SETTERS BEGIN
    public ArrayList<Map.Entry<Car,List>> getCarsData() { return carsData;}
    public void setCarsData(ArrayList<Map.Entry<Car,List>> carsData) {this.carsData = carsData;}
}
