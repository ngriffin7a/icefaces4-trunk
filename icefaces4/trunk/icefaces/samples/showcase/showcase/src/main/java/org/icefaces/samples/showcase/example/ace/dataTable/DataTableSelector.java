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

import org.icefaces.ace.model.table.RowStateMap;
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
        title = "example.ace.dataTable.selector.title",
        description = "example.ace.dataTable.selector.description",
        example = "/resources/examples/ace/dataTable/dataTableSelector.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="dataTableSelector.xhtml",
                    resource = "/resources/examples/ace/dataTable/dataTableSelector.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="DataTableSelector.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                    "/example/ace/dataTable/DataTableSelector.java")
        }
)
@ManagedBean(name= DataTableSelector.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class DataTableSelector extends ComponentExampleImpl<DataTableSelector> implements Serializable {
    public static final String BEAN_NAME = "dataTableSelector";

    private static final SelectItem[] AVAILABLE_MODES = { new SelectItem("single", "Single Row"),
                                                          new SelectItem("multiple", "Multiple Rows"),
                                                          new SelectItem("singlecell", "Single Cell"),
                                                          new SelectItem("multiplecell", "Multiple Cell") };

    private RowStateMap stateMap = new RowStateMap();
    private String selectionMode = AVAILABLE_MODES[0].getValue().toString();
    private boolean dblClick = false;
    private boolean instantUpdate = true;
    private List<Car> carsData;
    
    /////////////---- CONSTRUCTOR BEGIN
    public DataTableSelector() {
        super(DataTableSelector.class);
        carsData = new ArrayList<Car>(DataTableData.getDefaultData());
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    /////////////---- VALUE CHANGE LISTENERS BEGIN
    public void changedMode(ValueChangeEvent event) {
        stateMap.clear();
    }
    /////////////---- GETTERS & SETTERS BEGIN
    public Class getClazz() {
        return getClass();
    }
    public RowStateMap getStateMap() { return stateMap; }
    public ArrayList<Car> getMultiRow() { return (ArrayList<Car>) stateMap.getSelected(); }

    public List<RowObjectColumnSelections> getCellSelections() {
        ArrayList<RowObjectColumnSelections> cellSelections = new ArrayList<RowObjectColumnSelections>();
        for (Object o : stateMap.getRowsWithSelectedCells()) {
            Car c = (Car) o;
            List columnSelections = new ArrayList();
            for (String columnId : stateMap.get(o).getSelectedColumnIds()) {
                if ("id".equals(columnId)) columnSelections.add(c.getId());
                else if ("name".equals(columnId)) columnSelections.add(c.getName());
                else if ("chassis".equals(columnId)) columnSelections.add(c.getChassis());
                else if ("weight".equals(columnId)) columnSelections.add(c.getWeight());
                else if ("accel".equals(columnId)) columnSelections.add(c.getAcceleration());
                else if ("mpg".equals(columnId)) columnSelections.add(c.getMpg());
                else if ("cost".equals(columnId)) columnSelections.add(c.getCost());
            }
            cellSelections.add(new RowObjectColumnSelections(o, columnSelections));
        }
        return cellSelections;
    }

    public String getSelectionMode() { return selectionMode; }
    public boolean getDblClick() { return dblClick; }
    public boolean getInstantUpdate() { return instantUpdate; }
    public SelectItem[] getAvailableModes() { return AVAILABLE_MODES; }
    public List<Car> getCarsData() { return carsData; }


    public void setStateMap(RowStateMap stateMap) { this.stateMap = stateMap; }
    public void setMultiRow(ArrayList<Car> multiRow) { }
    public void setSelectionMode(String selectionMode) { this.selectionMode = selectionMode; }
    public void setDblClick(boolean dblClick) { this.dblClick = dblClick; }
    public void setInstantUpdate(boolean instantUpdate) { this.instantUpdate = instantUpdate; }
    public void setCarsData(List<Car> carsData) { this.carsData = carsData; }


    public static class RowObjectColumnSelections {
        private Object object;
        private List columnSelections;

        RowObjectColumnSelections(Object object, List columnSelections) {
            this.object = object;
            this.columnSelections = columnSelections;
        }

        public Object getObject() { return object; }
        public List getColumnSelections() { return columnSelections; }
    }
}
