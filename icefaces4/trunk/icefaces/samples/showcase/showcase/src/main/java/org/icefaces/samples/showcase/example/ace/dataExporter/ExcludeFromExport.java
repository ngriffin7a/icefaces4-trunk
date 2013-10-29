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
import java.util.ArrayList;
import org.icefaces.samples.showcase.dataGenerators.utilityClasses.DataTableData;
import org.icefaces.samples.showcase.example.compat.dataTable.Car;

@ComponentExample(
        parent = DataExporterBean.BEAN_NAME,
        title = "example.ace.dataExporter.excludeFromExport.title",
        description = "example.ace.dataExporter.excludeFromExport.description",
        example = "/resources/examples/ace/dataExporter/dataExporterExcludeFromExport.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="dataExporterExcludeFromExport.xhtml",
                    resource = "/resources/examples/ace/dataExporter/dataExporterExcludeFromExport.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="ExcludeFromExport.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                    "/example/ace/dataExporter/ExcludeFromExport.java")
        }
)
@ManagedBean(name= ExcludeFromExport.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class ExcludeFromExport extends ComponentExampleImpl<ExcludeFromExport> implements Serializable {
    public static final String BEAN_NAME = "excludeFromExport";
    
    private ArrayList<Car> cars;
    private String type;
    
    /////////////---- CONSTRUCTOR BEGIN
    public ExcludeFromExport() 
    {
        super(ExcludeFromExport.class);
        initializeLocalVariables();
    }
    
    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    /////////////---- PRIVATE METHODS BEGIN
    private void initializeLocalVariables() {
        this.cars = new ArrayList<Car>(DataTableData.getDefaultData());
        this.type = "csv";
        
    }
    /////////////---- GETTERS & SETTERS BEGIN
    public ArrayList<Car> getCars() { return cars; }
    public void setCars(ArrayList<Car> cars) { this.cars = cars; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}
