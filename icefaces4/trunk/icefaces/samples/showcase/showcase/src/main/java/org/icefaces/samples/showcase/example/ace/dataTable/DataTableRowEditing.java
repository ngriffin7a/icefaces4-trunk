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

import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.icefaces.samples.showcase.dataGenerators.utilityClasses.DataTableData;
import org.icefaces.samples.showcase.example.compat.dataTable.Car;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;


@ComponentExample(
        parent = DataTableBean.BEAN_NAME,
        title = "example.ace.dataTable.rowEditing.title",
        description = "example.ace.dataTable.rowEditing.description",
        example = "/resources/examples/ace/dataTable/dataTableRowEditing.xhtml"
)
@ExampleResources(
        resources ={
                // xhtml
                @ExampleResource(type = ResourceType.xhtml,
                        title="dataTableRowEditing.xhtml",
                        resource = "/resources/examples/ace/dataTable/dataTableRowEditing.xhtml"),
                // Java Source
                @ExampleResource(type = ResourceType.java,
                        title="RowEditing.java",
                        resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                                "/example/ace/dataTable/DataTableRowEditing.java")
        }
)
@ManagedBean(name= DataTableRowEditing.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class DataTableRowEditing extends ComponentExampleImpl<DataTableRowEditing> implements Serializable
{
    public static final String BEAN_NAME = "dataTableRowEditing";
    private List<Car> cars;
    /////////////---- CONSTRUCTOR BEGIN
    public DataTableRowEditing() 
    {
        super(DataTableRowEditing.class);
        cars = new ArrayList<Car>(DataTableData.getDefaultData());
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    /////////////---- GETTERS & SETTERS BEGIN
    public List<Car> getCars() { return cars; }
    public void setCars(List<Car> cars) { this.cars = cars; }
}
