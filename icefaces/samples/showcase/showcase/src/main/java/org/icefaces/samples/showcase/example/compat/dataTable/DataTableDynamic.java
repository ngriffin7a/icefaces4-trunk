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
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import org.icefaces.samples.showcase.dataGenerators.VehicleGenerator;
import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

@ComponentExample(
        parent = DataTableBean.BEAN_NAME,
        title = "example.compat.dataTable.dynamic.title",
        description = "example.compat.dataTable.dynamic.description",
        example = "/resources/examples/compat/dataTable/dataTableDynamic.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="dataTableDynamic.xhtml",
                    resource = "/resources/examples/compat/"+
                               "dataTable/dataTableDynamic.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="DataTableDynamic.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/dataTable/DataTableDynamic.java")
        }
)
@ManagedBean(name= DataTableDynamic.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class DataTableDynamic extends ComponentExampleImpl<DataTableDynamic> implements Serializable {
	
    public static final String BEAN_NAME = "dataTableDynamic";

    private static final int BULK_ADD_NUMBER = 5;
    private int bulkNumber = BULK_ADD_NUMBER;
    private ArrayList<Car> carsData;
    private VehicleGenerator generator;
    private List<String> chasisOptions;
    private Car toModify;
    private int toRemove;
    private int toEdit;

    public DataTableDynamic() {
        super(DataTableDynamic.class);
        generator = new VehicleGenerator();
        toModify = new Car();
        carsData = getDefaultCars();
        chasisOptions = generator.getChassisPool();
    }
    
    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    ///////////////////////// ACTION LISTENERS //////////////////////////////////////////////////////////////////////////////
    public void bulkAdd(ActionEvent event) {
        /*
         * Action listener logic:
         * 1. Generate pseudo random list which will be BULK_ADD_NUMBER elements longer then a current one
         * 2. Extract sublist from generated pseudo random list based on:
         *   2.a begin index = size of our current list
         *   2.b end index = pseudo random list last element
         * 3. Find largest id number in our current list
         * 4. Assign id's to the extracted sublist based on formula NEWid = ++MAXid + sublist element index
         * 5. Add extracted sublist to the end of our current list
         */  
        
        //generate temporary list
        ArrayList<Car> list = generator.getRandomCars(carsData.size()+BULK_ADD_NUMBER);
        //grab elements from temporary list starting from the element index = carsData.size() till the end;
        list = new ArrayList<Car>(list.subList(carsData.size(), list.size()));
        //find max id in our current list
        int maxId = findLargestIdInList(carsData);
        //Reassign id's in list
        for (Car car : list) {
            car.setId(++maxId);
        }
        //add those elements to the carsData
        carsData.addAll(list);
    }

    public void submitItem(ActionEvent event) {
        // Edit use case if we already have an id
        if (toModify.getId() != -1) 
        {
            Car oldEdit = getCarById(toModify.getId());
            // Only apply the changes if we can
            // There is a chance a user edits a car, then deletes it, then tries to submit the edit
            // This logic will prevent that from causing an error
            if (oldEdit != null) {
                oldEdit.applyValues(toModify);

                // Reset the values to blank
                toModify = new Car();

                return;
            }
        }
        toModify.setId(carsData.size()+1);
        // Add our new car
        carsData.add(toModify);
        // Reset the values to blank
        toModify = new Car();
    }
    /////////////////////////////////////// ACTIONS /////////////////////////////////////////////////////////////////
    public String removeItem() {
        Car removeCar = getCarById(toRemove);

        if (removeCar != null) {
            carsData.remove(removeCar);
        }

        return null;
    }

    public String editItem() {
        toModify = getCarById(toEdit);

        return null;
    }

    public void restoreDefault() {
        carsData = getDefaultCars();
    }
    ////////////////////////////////////// PRIVATE METHODS //////////////////////////////////////////////////////
    private ArrayList<Car> getDefaultCars() {
            return generator.getRandomCars(DataTableData.DEFAULT_ROWS);
    }

    private Car getCarById(int id) {
        for (Car currentItem : carsData) {
            if (id == currentItem.getId()) {
                return currentItem;
            }
        }
        return null;
    }
    
    private int findLargestIdInList(ArrayList<Car> list) {
        int largest = 0;
        //go in reverse against list to speed up this search
        for (int i = list.size()-1; i>0; i--) 
        {
            if(largest < list.get(i).getId())
                largest = list.get(i).getId();
        }
        return largest;
    }
    ///////////////////////////////////////// GETTERS & SETTERS //////////////////////////////////////////////////////
    public int getBulkNumber() { return bulkNumber; }
    public List<String> getChasisOptions() { return chasisOptions; }
    public List<Car> getCarsData() { return carsData; }
    public Car getToModify() { return toModify; }
    public int getToRemove() { return toRemove; }
    public int getToEdit() { return toEdit; }

    public void setChasisOptions(List<String> chasisOptions) { this.chasisOptions = chasisOptions; }
    public void setBulkNumber(int bulkNumber) { this.bulkNumber = bulkNumber; }
    public void setCarsData(ArrayList<Car> carsData) { this.carsData = carsData; }
    public void setToModify(Car toModify) { this.toModify = toModify; }
    public void setToRemove(int toRemove) { this.toRemove = toRemove; }
    public void setToEdit(int toEdit) { this.toEdit = toEdit; }

}
