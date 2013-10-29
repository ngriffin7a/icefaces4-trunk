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
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ComponentExample(
        parent = DataTableBean.BEAN_NAME,
        title = "example.ace.dataTable.dynamicColumns.title",
        description = "example.ace.dataTable.dynamicColumns.description",
        example = "/resources/examples/ace/dataTable/dataTableDynamicColumns.xhtml"
)
@ExampleResources(
        resources = {
                // xhtml
                @ExampleResource(type = ResourceType.xhtml,
                        title = "dataTableDynamicColumns.xhtml",
                        resource = "/resources/examples/ace/dataTable/dataTableDynamicColumns.xhtml"),
                // Java Source
                @ExampleResource(type = ResourceType.java,
                        title = "DataTableDynamicColumns.java",
                        resource = "/WEB-INF/classes/org/icefaces/samples/showcase" +
                                "/example/ace/dataTable/DataTableDynamicColumns.java")
        }
)
@ManagedBean(name = DataTableDynamicColumns.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class DataTableDynamicColumns extends ComponentExampleImpl<DataTableDynamicColumns> implements Serializable {
    public static final String BEAN_NAME = "dataTableDynamicColumns";
    private List<Car> cars;
    private List<Integer> ordering = new ArrayList<Integer>();
    private List<ColumnModel> columns = new ArrayList<ColumnModel>() {{
        add(new ColumnModel("id", "ID"));
        add(new ColumnModel("name", "Name"));
        add(new ColumnModel("chassis", "Chassis"));
        add(new ColumnModel("weight", "Weight"));
        add(new ColumnModel("acceleration", "Accel"));
        add(new ColumnModel("mpg", "MPG"));
        add(new ColumnModel("cost", "Cost"));
    }};

    private List<SelectItem> checkboxes = new ArrayList<SelectItem>() {{
        add(new SelectItem("id", "ID"));
        add(new SelectItem("name", "Name"));
        add(new SelectItem("chassis", "Chassis"));
        add(new SelectItem("weight", "Weight"));
        add(new SelectItem("acceleration", "Accel"));
        add(new SelectItem("mpg", "MPG"));
        add(new SelectItem("cost", "Cost"));
    }};

    private List<String> selectedCheckboxes = new ArrayList<String>() {{
        add("id");
        add("name");
        add("chassis");
        add("weight");
        add("acceleration");
        add("mpg");
        add("cost");
    }};

    public DataTableDynamicColumns() {
        super(DataTableDynamicColumns.class);
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

    public List<ColumnModel> getColumns() {
        return columns;
    }

    public void setColumns(List<ColumnModel> columns) {
        this.columns = columns;
    }

    public List<SelectItem> getCheckboxes() {
        return checkboxes;
    }

    public void setCheckboxes(List<SelectItem> checkboxes) {
        this.checkboxes = checkboxes;
    }

    public List<String> getSelectedCheckboxes() {
        return selectedCheckboxes;
    }

    public void setSelectedCheckboxes(List<String> selectedCheckboxes) {
        this.selectedCheckboxes = selectedCheckboxes;
    }

    public void removeColumn(String name) {
        for (int i = 0; i < columns.size(); i++)
            if (columns.get(i).getValue().equals(name))
                columns.remove(i);
    }

    public void addColumn(String name) {
        columns.add(
                new ColumnModel(
                        name,
                        name.substring(0, 1).toUpperCase() + name.substring(1)
                )
        );
    }

    public void checkboxChange(ValueChangeEvent event) {
        List<String> oldVal = (List<String>) event.getNewValue();
        List<String> newVal = (List<String>) event.getOldValue();

        List<String> added = new ArrayList<String>(oldVal);
        added.removeAll(newVal);
        for (String s : added) {
            addColumn(s.toLowerCase());
        }

        List<String> removed = new ArrayList<String>(newVal);
        removed.removeAll(oldVal);
        for (String s : removed) {
            removeColumn(s.toLowerCase());
        }
    }
}