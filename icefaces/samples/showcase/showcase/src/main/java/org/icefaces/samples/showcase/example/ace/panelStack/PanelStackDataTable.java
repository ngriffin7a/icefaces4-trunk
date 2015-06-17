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

import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ValueChangeEvent;
import java.io.Serializable;
import java.lang.String;
import java.lang.System;
import java.util.ArrayList;
import java.util.List;


@ComponentExample(
        title = "example.ace.panelStack.datatable.title",
        description = "example.ace.panelStack.datatable.description",
        example = "/resources/examples/ace/panelStack/panelStackDataTable.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="panelStackDataTable.xhtml",
                    resource = "/resources/examples/ace/panelStack/panelStackDataTable.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="PanelStackDataTable.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                    "/example/ace/panelStack/PanelStackDataTable.java")
        }
)
@ManagedBean(name= PanelStackDataTable.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class PanelStackDataTable extends ComponentExampleImpl<PanelStackDataTable> implements  Serializable {
    public static final String BEAN_NAME = "panelStackDataTable";
    
    private String currentId; // the id of the currently selected stackPane
    private List<InputDefinition> queryList1 = new ArrayList<InputDefinition>();
    private List<InputDefinition> queryList2 = new ArrayList<InputDefinition>();
    private List<InputDefinition> queryList3 = new ArrayList<InputDefinition>();
    private List<InputDefinition> selectedQuery;
    private String selectedValue = "1";
    private boolean facelet = true;

    public PanelStackDataTable() {
        super(PanelStackDataTable.class);
        //populate queryLists to simulate types of dataLists from db
        this.queryList1.add(new InputDefinition("TEXTENTRY", "FirstName", "Thomas"));
        this.queryList1.add(new InputDefinition("TEXTENTRY", "LastName", "Smith"));
        this.queryList1.add(new InputDefinition("CHECKBOX", "Admin", false));

        this.queryList2.add(new InputDefinition("TEXTENTRY", "FirstName", "Jane"));
        this.queryList2.add(new InputDefinition("TEXTENTRY", "LastName", "Brown"));
        this.queryList2.add(new InputDefinition("CHECKBOX", "Admin", true));
        this.queryList2.add(new InputDefinition("RADIO", "Level", true));
        this.queryList2.add(new InputDefinition("CHECKBOX", "Active", false));

        this.queryList3.add(new InputDefinition("TEXTENTRY", "FirstName", "John"));
        this.queryList3.add(new InputDefinition("TEXTENTRY", "LastName", "Taylor"));
        this.queryList3.add(new InputDefinition("TEXTENTRY", "Middle", "A"));
        this.queryList3.add(new InputDefinition("TEXTAREAENTRY", "Notes","Memo from last meeting.."));
        this.selectedQuery = queryList1;
    }
    
    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
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

    public boolean isFacelet() {
        return facelet;
    }

    public void setFacelet(boolean facelet) {
        this.facelet = facelet;
    }

    public void changeList(ValueChangeEvent event){
        String newVal = event.getNewValue().toString();
        this.selectedValue=newVal;
        if (newVal.equals("2")){
            this.selectedQuery = queryList2;
        }else if (newVal.equals("3")){
            this.selectedQuery = queryList3;
        } else {
            this.selectedQuery = queryList1;
        }

    }

    public String getSelectedValue() {
        return selectedValue;
    }

    public void setSelectedValue(String selectedValue) {
        this.selectedValue = selectedValue;
    }
}