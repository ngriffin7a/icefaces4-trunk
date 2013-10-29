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
        title = "example.compat.dataTable.resize.title",
        description = "example.compat.dataTable.resize.description",
        example = "/resources/examples/compat/dataTable/dataTableResize.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="dataTableResize.xhtml",
                    resource = "/resources/examples/compat/"+
                               "dataTable/dataTableResize.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="DataTableResize.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/dataTable/DataTableResize.java")
        }
)
@ManagedBean(name= DataTableResize.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class DataTableResize extends ComponentExampleImpl<DataTableResize> implements Serializable {
	
    public static final String BEAN_NAME = "dataTableResize";
    private boolean resizable = true;
    private int rows = DataTableData.DEFAULT_ROWS;
    private ArrayList<Car> cars;
	
    public DataTableResize() {
        super(DataTableResize.class);
        rows = DataTableData.DEFAULT_ROWS;
        cars = new ArrayList<Car>(DataTableData.getDefaultData());
    }
	
    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    public boolean getResizable() { return resizable; }
    public void setResizable(boolean resizable) { this.resizable = resizable; }
    public ArrayList<Car> getCars() { return cars; }
    public void setCars(ArrayList<Car> cars) { this.cars = cars;}
    public int getRows() {return rows;}
    public void setRows(int rows) {this.rows = rows;}
}
