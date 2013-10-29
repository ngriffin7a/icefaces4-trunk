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
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;

import com.icesoft.faces.component.commandsortheader.CommandSortHeader;
import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

@ComponentExample(
        parent = DataTableBean.BEAN_NAME,
        title = "example.compat.dataTable.sort.title",
        description = "example.compat.dataTable.sort.description",
        example = "/resources/examples/compat/dataTable/dataTableSort.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="dataTableSort.xhtml",
                    resource = "/resources/examples/compat/"+
                               "dataTable/dataTableSort.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="DataTableSort.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/dataTable/DataTableSort.java")
        }
)
@ManagedBean(name= DataTableSort.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class DataTableSort extends ComponentExampleImpl<DataTableSort> implements Serializable {
	
     public static final String BEAN_NAME = "dataTableSort";
	
    public static final String SORT_COLUMN_ID = "id";
    public static final String SORT_COLUMN_NAME = "name";
    public static final String SORT_COLUMN_CHASSIS = "chassis";
    public static final String SORT_COLUMN_WEIGHT = "weight";
    public static final String SORT_COLUMN_ACCELERATION = "acceleration";
    public static final String SORT_COLUMN_MPG = "mpg";
    public static final String SORT_COLUMN_COST = "cost";
    
    private List<Car> carsData;
    private int defaultRows;
    private String columnName = SORT_COLUMN_ID;
    private boolean ascending = true;
	
    public DataTableSort() {
        super(DataTableSort.class);
        carsData = new ArrayList<Car>(DataTableData.getDefaultData());
        defaultRows = DataTableData.DEFAULT_ROWS;
    }
	
    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    public void columnSort(ActionEvent event)
    {
        // Ensure we have valid items, a valid column to sort on, and a valid event to work with
        if ((carsData != null) && (carsData.size() > 0) &&
            (columnName != null) &&
            (event != null) && (event.getComponent() != null)) {
            // Get the column name that was clicked to perform the sort
            String newColumnName = ((CommandSortHeader)event.getComponent()).getColumnName();
            boolean newAscending = true;
            
            // If the new column was the same as the old column, just toggle the ascending
            if (newColumnName.equals(columnName)) {
                newAscending = !ascending;
            }
            
            // Perform the actual sort
            sort(newColumnName, newAscending, carsData);
        }
    }
    
    public static void sort(String sortColumn, List<Car> data) {
        sort(sortColumn, true, data);
    }
    
    public static void sort(final String sortColumn, final boolean sortAscending, final List<Car> data) {
        // Build a comparator that uses compareTo of the proper column
        Comparator<Car> sortComparator = new Comparator<Car>() {
            public int compare(Car item1, Car item2) {
                if (SORT_COLUMN_ID.equals(sortColumn)) {
                    return sortAscending ?
                            new Integer(item1.getId()).compareTo(new Integer(item2.getId())) :
                            new Integer(item2.getId()).compareTo(new Integer(item1.getId()));
                } else if (SORT_COLUMN_NAME.equals(sortColumn)) {
                    return sortAscending ?
                            item1.getName().compareTo(item2.getName()) :
                            item2.getName().compareTo(item1.getName());
                } else if (SORT_COLUMN_CHASSIS.equals(sortColumn)) {
                    return sortAscending ?
                            item1.getChassis().compareTo(item2.getChassis()) :
                            item2.getChassis().compareTo(item1.getChassis());
                } else if (SORT_COLUMN_WEIGHT.equals(sortColumn)) {
                    return sortAscending ?
                            new Integer(item1.getWeight()).compareTo(new Integer(item2.getWeight())) :
                            new Integer(item2.getWeight()).compareTo(new Integer(item1.getWeight()));
                } else if (SORT_COLUMN_ACCELERATION.equals(sortColumn)) {
                    return sortAscending ?
                            new Integer(item1.getAcceleration()).compareTo(new Integer(item2.getAcceleration())) :
                            new Integer(item2.getAcceleration()).compareTo(new Integer(item1.getAcceleration()));
                } else if (SORT_COLUMN_MPG.equals(sortColumn)) {
                    return sortAscending ?
                            new Double(item1.getMpg()).compareTo(new Double(item2.getMpg())) :
                            new Double(item2.getMpg()).compareTo(new Double(item1.getMpg()));
                } else if (SORT_COLUMN_COST.equals(sortColumn)) {
                    return sortAscending ?
                            new Double(item1.getCost()).compareTo(new Double(item2.getCost())) :
                            new Double(item2.getCost()).compareTo(new Double(item1.getCost()));
                } else return 0;
            }
        };
    
        Collections.sort(data, sortComparator);
    }
    
    public String getSortColumnId() { return SORT_COLUMN_ID; }
    public String getSortColumnName() { return SORT_COLUMN_NAME; }
    public String getSortColumnChassis() { return SORT_COLUMN_CHASSIS; }
    public String getSortColumnWeight() { return SORT_COLUMN_WEIGHT; }
    public String getSortColumnAcceleration() { return SORT_COLUMN_ACCELERATION; }
    public String getSortColumnMpg() { return SORT_COLUMN_MPG; }
    public String getSortColumnCost() { return SORT_COLUMN_COST; }
    public String getColumnName() { return columnName;}
    public List<Car> getCarsData() { return carsData;}
    public boolean getAscending() {return ascending;}
    public int getDefaultRows() {return defaultRows;}
        
    public void setCarsData(List<Car> carsData) {this.carsData = carsData;}
    public void setColumnName(String columnName) {this.columnName = columnName;}
    public void setAscending(boolean ascending) { this.ascending = ascending; }
}
