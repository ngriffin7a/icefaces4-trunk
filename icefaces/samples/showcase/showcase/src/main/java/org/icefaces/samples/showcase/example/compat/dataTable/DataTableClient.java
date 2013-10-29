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

package org.icefaces.samples.showcase.example.compat.dataTable;

import org.icefaces.samples.showcase.dataGenerators.utilityClasses.DataTableData;
import java.io.Serializable;

import java.util.ArrayList;
import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;

import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

@ComponentExample(
        parent = DataTableBean.BEAN_NAME,
        title = "example.compat.dataTable.client.title",
        description = "example.compat.dataTable.client.description",
        example = "/resources/examples/compat/dataTable/dataTableClient.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="dataTableClient.xhtml",
                    resource = "/resources/examples/compat/"+
                               "dataTable/dataTableClient.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="DataTableClient.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/dataTable/DataTableClient.java")
        }
)
@ManagedBean(name= DataTableClient.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class DataTableClient extends ComponentExampleImpl<DataTableClient> implements Serializable {
	
    public static final String BEAN_NAME = "dataTableClient";

    private boolean enable = true;
    private int defaultRows;
    private ArrayList<Car> cars;


    public DataTableClient() {
        super(DataTableClient.class);
        defaultRows = DataTableData.DEFAULT_ROWS;
        cars = new ArrayList<Car>(DataTableData.getDefaultData());
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    public boolean getEnable() { return enable; }
    public void setEnable(boolean enable) { this.enable = enable; }
    public ArrayList<Car> getCars() { return cars; }
    public void setCars(ArrayList<Car> cars) { this.cars = cars; }
    public int getDefaultRows() { return defaultRows; }
    public void setDefaultRows(int defaultRows) { this.defaultRows = defaultRows; }
}
