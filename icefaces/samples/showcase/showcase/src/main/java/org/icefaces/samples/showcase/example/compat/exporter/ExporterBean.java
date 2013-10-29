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

package org.icefaces.samples.showcase.example.compat.exporter;

import java.io.Serializable;

import java.util.ArrayList;
import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;

import org.icefaces.samples.showcase.dataGenerators.utilityClasses.DataTableData;
import org.icefaces.samples.showcase.example.compat.dataTable.Car;
import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.Menu;
import org.icefaces.samples.showcase.metadata.annotation.MenuLink;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

@ComponentExample(
        title = "example.compat.exporter.title",
        description = "example.compat.exporter.description",
        example = "/resources/examples/compat/exporter/exporter.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="exporter.xhtml",
                    resource = "/resources/examples/compat/"+
                               "exporter/exporter.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="ExporterBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/exporter/ExporterBean.java"),
            @ExampleResource(type = ResourceType.java,
                    title="DataTableData.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/dataGenerators/utilityClasses/DataTableData.java")
        }
)
@Menu(
	title = "menu.compat.exporter.subMenu.title",
	menuLinks = {
            @MenuLink(title = "menu.compat.exporter.subMenu.main",
                    isDefault = true,
                    exampleBeanName = ExporterBean.BEAN_NAME),
            @MenuLink(title = "menu.compat.exporter.subMenu.type",
                    exampleBeanName = ExporterType.BEAN_NAME),
            @MenuLink(title = "menu.compat.exporter.subMenu.column",
                    exampleBeanName = ExporterColumn.BEAN_NAME),
            @MenuLink(title = "menu.compat.exporter.subMenu.pagination",
                    exampleBeanName = ExporterPagination.BEAN_NAME),
            @MenuLink(title = "menu.compat.exporter.subMenu.label",
                    exampleBeanName = ExporterLabel.BEAN_NAME)
})
@ManagedBean(name= ExporterBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class ExporterBean extends ComponentExampleImpl<ExporterBean> implements Serializable {
	
    public static final String BEAN_NAME = "exporter";
    private ArrayList<Car> cars;
    private int defaultRows;


    public ExporterBean() {
        super(ExporterBean.class);
        cars = new ArrayList<Car>(DataTableData.getDefaultData());
        defaultRows = DataTableData.DEFAULT_ROWS;
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    public ArrayList<Car> getCars() { return cars; }
    public int getDefaultRows() { return defaultRows; }
}
