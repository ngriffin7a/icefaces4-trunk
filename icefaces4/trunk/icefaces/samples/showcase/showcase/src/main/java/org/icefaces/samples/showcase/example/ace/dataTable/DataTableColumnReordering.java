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

import org.icefaces.samples.showcase.example.compat.dataTable.Car;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import org.icefaces.samples.showcase.dataGenerators.utilityClasses.DataTableData;

@ComponentExample(
        parent = DataTableBean.BEAN_NAME,
        title = "example.ace.dataTable.columnReordering.title",
        description = "example.ace.dataTable.columnReordering.description",
        example = "/resources/examples/ace/dataTable/dataTableColumnReordering.xhtml"
)
@ExampleResources(
        resources ={
                // xhtml
                @ExampleResource(type = ResourceType.xhtml,
                        title="dataTableColumnReordering.xhtml",
                        resource = "/resources/examples/ace/dataTable/dataTableColumnReordering.xhtml"),
                // Java Source
                @ExampleResource(type = ResourceType.java,
                        title="DataTableColumnReordering.java",
                        resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                                "/example/ace/dataTable/DataTableColumnReordering.java")
        }
)
@ManagedBean(name= DataTableColumnReordering.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class DataTableColumnReordering extends ComponentExampleImpl<DataTableColumnReordering> implements Serializable
{
    public static final String BEAN_NAME = "dataTableColumnReordering";
    private List<Car> cars;
    private List<Integer> ordering = new ArrayList<Integer>();

    public DataTableColumnReordering() 
    {
        super(DataTableColumnReordering.class);
        this.cars = new ArrayList<Car>(DataTableData.getDefaultData());
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    public List<Car> getCars() {
        return cars;
    }

    public void setCars(List<Car> cars) {
        this.cars = cars;
    }

    public List<Integer> getOrdering() {
        return ordering;
    }
    public void setOrdering(List<Integer> ordering) {
        this.ordering = ordering;
    }
}