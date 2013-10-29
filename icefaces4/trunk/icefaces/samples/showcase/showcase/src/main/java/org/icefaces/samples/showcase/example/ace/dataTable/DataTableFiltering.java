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

import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.model.SelectItem;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.icefaces.samples.showcase.dataGenerators.utilityClasses.DataTableData;
import org.icefaces.samples.showcase.example.compat.dataTable.Car;

@ComponentExample(
        parent = DataTableBean.BEAN_NAME,
        title = "example.ace.dataTable.filtering.title",
        description = "example.ace.dataTable.filtering.description",
        example = "/resources/examples/ace/dataTable/dataTableFiltering.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="dataTableFiltering.xhtml",
                    resource = "/resources/examples/ace/dataTable/dataTableFiltering.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="DataTableFiltering.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                    "/example/ace/dataTable/DataTableFiltering.java")
        }
)
@ManagedBean(name= DataTableFiltering.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class DataTableFiltering extends ComponentExampleImpl<DataTableFiltering> implements Serializable {
    public static final String BEAN_NAME = "dataTableFiltering";
    private List<Car> carsData;
    private List<SelectItem> accelOptions = new ArrayList<SelectItem>() {{
        add(new SelectItem(""));
        add(new SelectItem(5));
        add(new SelectItem(10));
        add(new SelectItem(15));
    }};

    /////////////---- CONSTRUCTOR BEGIN
    public DataTableFiltering() {
        super(DataTableFiltering.class);
        carsData = new ArrayList<Car>(DataTableData.getDefaultData());
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    /////////////---- GETTERS & SETTERS BEGIN
    public List<Car> getCarsData() { return carsData; }
    public void setCarsData(List<Car> carsData) { this.carsData = carsData; }

    public List<SelectItem> getAccelOptions() {
        return accelOptions;
    }

    public void setAccelOptions(List<SelectItem> accelOptions) {
        this.accelOptions = accelOptions;
    }
}
