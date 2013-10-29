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

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import org.icefaces.samples.showcase.dataGenerators.VehicleGenerator;
import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

@ComponentExample(
        parent = DataTableBean.BEAN_NAME,
        title = "example.compat.dataTable.group.title",
        description = "example.compat.dataTable.group.description",
        example = "/resources/examples/compat/dataTable/dataTableGroup.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="dataTableGroup.xhtml",
                    resource = "/resources/examples/compat/"+
                               "dataTable/dataTableGroup.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="DataTableGroup.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/dataTable/DataTableGroup.java")
        }
)
@ManagedBean(name= DataTableGroup.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class DataTableGroup extends ComponentExampleImpl<DataTableGroup> implements Serializable {

    public static final String BEAN_NAME = "dataTableGroup";

    private List<Car> carsData;

    public DataTableGroup() 
    {
        super(DataTableGroup.class);
        VehicleGenerator generator = new VehicleGenerator();
        carsData = generator.getRandomCars(20);
        DataTableSort.sort(DataTableSort.SORT_COLUMN_CHASSIS, carsData);
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    public List<Car> getCarsData() { return carsData; }
    public void setCarsData(List<Car> carsData) { this.carsData = carsData; }
}
