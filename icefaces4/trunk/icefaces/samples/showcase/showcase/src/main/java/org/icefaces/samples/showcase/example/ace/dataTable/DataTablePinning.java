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

import org.icefaces.samples.showcase.dataGenerators.utilityClasses.DataTableData;
import org.icefaces.samples.showcase.example.compat.dataTable.Car;
import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ComponentExample(
    parent = DataTableBean.BEAN_NAME,
    title = "example.ace.dataTable.pinning.title",
    description = "example.ace.dataTable.pinning.description",
    example = "/resources/examples/ace/dataTable/dataTablePinning.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="dataTableScrolling.xhtml",
                    resource = "/resources/examples/ace/dataTable/dataTablePinning.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="DataTablePinning.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                            "/example/ace/dataTable/DataTablePinning.java")
        }
)
@ManagedBean(name= DataTablePinning.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class DataTablePinning extends ComponentExampleImpl<DataTablePinning> implements Serializable {
    public static final String BEAN_NAME = "dataTablePinning";

    private List<Car> carsData;
    private Integer[] pinOrder;

    public DataTablePinning() {
        super(DataTablePinning.class);
        carsData = new ArrayList<Car>(DataTableData.getDefaultData());
        pinOrder = new Integer[7];
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    public List<Car> getCarsData() {
        return carsData;
    }

    public void setCarsData(List<Car> carsData) {
        this.carsData = carsData;
    }

    public Integer[] getPinOrder() {
        return pinOrder;
    }

    public void setPinOrder(Integer[] pinOrder) {
        this.pinOrder = pinOrder;
    }
}