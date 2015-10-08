/*
 * Copyright 2004-2014 ICEsoft Technologies Canada Corp.
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

package org.icefaces.samples.showcase.example.ace.panelStack;

import org.icefaces.samples.showcase.dataGenerators.utilityClasses.DataTableData;
import org.icefaces.samples.showcase.example.ace.dataTable.Car;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ValueChangeEvent;

@ManagedBean(name= PanelStackRepeat.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class PanelStackRepeat implements  Serializable {
    public static final String BEAN_NAME = "panelStackRepeat";
	public String getBeanName() { return BEAN_NAME; }
    
    private String currentId; // the id of the currently selected stackPane
    private List<InputDefinition> queryList1 = new ArrayList<InputDefinition>();
    private List<InputDefinition> queryList2 = new ArrayList<InputDefinition>();
    private List<InputDefinition> queryList3 = new ArrayList<InputDefinition>();
    private List<InputDefinition> queryList4 = new ArrayList<InputDefinition>();

    private List<InputDefinition> selectedQuery;
    private String selectedValue = "1";
	private Date selectedDate = new Date(System.currentTimeMillis());
    private boolean facelet = false;

    public PanelStackRepeat() {
        //populate queryLists to simulate types of dataLists from db
        this.queryList1.add(new InputDefinition("TEXTENTRY", "Street Address", "108 Aspen Drive"));
	    this.queryList1.add(new InputDefinition("TEXTENTRY", "City", "Calgary"));
	    this.queryList1.add(new InputDefinition("CHECKBOX", "Current", true));

	    this.queryList2.add(new InputDefinition("TEXTENTRY", "First Name", "Jane"));
	    this.queryList2.add(new InputDefinition("TEXTENTRY", "Last Name", "Brown"));
	    this.queryList2.add(new InputDefinition("CHECKBOX", "Admin", true));
	    this.queryList2.add(new InputDefinition("DATE", "Date Hired", ""));

	      
        this.queryList3.add(new InputDefinition("TEXTENTRY", "First Name", "John"));
	    this.queryList3.add(new InputDefinition("TEXTENTRY", "Last Name", "Taylor"));
	    this.queryList3.add(new InputDefinition("TEXTENTRY", "Title", "Developer"));
	    this.queryList3.add(new InputDefinition("CHECKBOX", "Active", true));
	    this.queryList3.add(new InputDefinition("TEXTAREAENTRY", "Notes","Memo from last meeting.."));

        List<Car> carsData = new ArrayList<Car>(DataTableData.getDefaultData());
        this.queryList4.add(new InputDefinition("TEXTENTRY", "Department Report", "Human Resources"));
        this.queryList4.add(new InputDefinition("DATATABLE", "Fleet Information", carsData));
	    this.selectedQuery = queryList1;
	}
    
    public String getCurrentId() {
        return currentId;
    }

    public void setCurrentId(String currentId) {
        this.currentId = currentId;
    }

    public List<InputDefinition> getSelectedQuery() {
        return selectedQuery;
    }

    public void setSelectedQuery(List<InputDefinition> selectedList) {
        this.selectedQuery = selectedList;
    }
   
	public String getSelectedValue() {
        return selectedValue;
    }

    public void setSelectedValue(String selectedValue) {
        this.selectedValue = selectedValue;
    }
	
	public Date getSelectedDate() {
		return selectedDate;
	}

	public void setSelectedDate(Date selectedDate) {
		this.selectedDate = selectedDate;
	}

    public void changeList(ValueChangeEvent event){
        String newVal = event.getNewValue().toString();
        this.selectedValue = newVal;
        if (newVal.equals("2")){
            this.selectedQuery = queryList2;
        }else if (newVal.equals("3")){
            this.selectedQuery = queryList3;
        }else if (newVal.equals("4")){
            this.selectedQuery = queryList4;
        } else {
            this.selectedQuery = queryList1;
        }

    }


    public boolean isFacelet() {
        return facelet;
    }

    public void setFacelet(boolean facelet) {
        this.facelet = facelet;
    }
}