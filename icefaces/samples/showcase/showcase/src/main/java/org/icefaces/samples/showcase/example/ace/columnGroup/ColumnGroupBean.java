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

package org.icefaces.samples.showcase.example.ace.columnGroup;

import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;
import org.icefaces.samples.showcase.dataGenerators.utilityClasses.DataTableData;
import org.icefaces.samples.showcase.example.ace.dataTable.Car;

@ComponentExample(
        title = "example.ace.columnGroup.title",
        description = "example.ace.columnGroup.description",
        example = "/resources/examples/ace/columnGroup/columnGroup.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="columnGroup.xhtml",
                    resource = "/resources/examples/ace/columnGroup/columnGroup.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="ColumnGroupBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                    "/example/ace/columnGroup/ColumnGroupBean.java")
        }
)
@Menu(
    title = "menu.ace.columnGroup.subMenu.title", 
    menuLinks = {
        @MenuLink(title = "menu.ace.columnGroup.subMenu.main", isDefault = true, exampleBeanName = ColumnGroupBean.BEAN_NAME)
    }
)

@ManagedBean(name = ColumnGroupBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class ColumnGroupBean extends ComponentExampleImpl<ColumnGroupBean> implements Serializable {
    public static final String BEAN_NAME = "columnGroupBean";

    public ColumnGroupBean() {
        super(ColumnGroupBean.class);
        this.cars = new ArrayList<Car>(DataTableData.getDefaultData());
    }
    
    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    private List<Car> cars;

    public List<Car> getCars() { return cars; }
    public void setCars(List<Car> cars) { this.cars = cars; }

}
