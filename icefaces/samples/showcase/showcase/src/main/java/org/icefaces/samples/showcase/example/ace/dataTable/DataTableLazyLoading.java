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

import java.util.Map;
import org.icefaces.ace.model.table.SortCriteria;
import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;
import java.util.List;
import org.icefaces.ace.model.table.LazyDataModel;
import org.icefaces.samples.showcase.dataGenerators.VehicleGenerator;
import org.icefaces.samples.showcase.example.compat.dataTable.Car;

@ComponentExample(
        parent = DataTableBean.BEAN_NAME,
        title = "example.ace.dataTable.lazyLoading.title",
        description = "example.ace.dataTable.lazyLoading.description",
        example = "/resources/examples/ace/dataTable/dataTableLazyLoading.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="dataTableLazyLoading.xhtml",
                    resource = "/resources/examples/ace/dataTable/dataTableLazyLoading.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="DataTableLazyLoading.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                    "/example/ace/dataTable/DataTableLazyLoading.java")
        }
)
@ManagedBean(name= DataTableLazyLoading.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class DataTableLazyLoading extends ComponentExampleImpl<DataTableLazyLoading> implements Serializable 
{
    public static final String BEAN_NAME = "dataTableLazyLoading";
    
    private LazyDataModel<Car> carsData;

    public DataTableLazyLoading() 
    {
        super(DataTableLazyLoading.class);
        carsData = new LazyDataModel<Car>() 
        {
            @Override
            public List<Car> load(int first, int pageSize, SortCriteria[] criteria, Map<String, String> filters) 
            {
                List<Car> randomCars;
                VehicleGenerator generator = new VehicleGenerator();
                randomCars = generator.getCarsForLazyLoading(pageSize);
                return randomCars;
            }
        };
        
        carsData.setRowCount(3000000);
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    public LazyDataModel<Car> getCarsData() { return carsData; }
    public void setCarsData(LazyDataModel<Car> carsData) { this.carsData = carsData; }
}