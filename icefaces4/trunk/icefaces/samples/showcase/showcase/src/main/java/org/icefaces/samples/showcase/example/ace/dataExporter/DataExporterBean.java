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

package org.icefaces.samples.showcase.example.ace.dataExporter;

import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;

@ComponentExample(
        title = "example.ace.dataExporter.title",
        description = "example.ace.dataExporter.description",
        example = "/resources/examples/ace/dataExporter/dataExporter.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="dataExporter.xhtml",
                    resource = "/resources/examples/ace/dataExporter/dataExporter.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="DataExporterBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                    "/example/ace/dataExporter/DataExporterBean.java"),
            @ExampleResource(type = ResourceType.java,
                    title="DataTableSort.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                    "/example/compat/dataTable/DataTableSort.java")
        }
)
@Menu(
	title = "menu.ace.dataExporter.subMenu.title",
	menuLinks = {
	        @MenuLink(title = "menu.ace.dataExporter.subMenu.main", isDefault = true, exampleBeanName = DataExporterBean.BEAN_NAME),
	        @MenuLink(title = "menu.ace.dataExporter.subMenu.columns", exampleBeanName = DataExporterColumns.BEAN_NAME),
                        @MenuLink(title = "menu.ace.dataExporter.subMenu.rows", exampleBeanName = DataExporterRows.BEAN_NAME),
                        @MenuLink(title = "menu.ace.dataExporter.subMenu.excludeFromExport", exampleBeanName = ExcludeFromExport.BEAN_NAME),
			@MenuLink(title = "menu.ace.dataExporter.subMenu.custom", exampleBeanName = DataExporterCustom.BEAN_NAME)
    }
)
@ManagedBean(name= DataExporterBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class DataExporterBean extends ComponentExampleImpl<DataExporterBean> implements Serializable {
    public static final String BEAN_NAME = "dataExporterBean";
    private String type;

    public DataExporterBean() {
        super(DataExporterBean.class);
        this.type = "csv";
    }
    
    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}