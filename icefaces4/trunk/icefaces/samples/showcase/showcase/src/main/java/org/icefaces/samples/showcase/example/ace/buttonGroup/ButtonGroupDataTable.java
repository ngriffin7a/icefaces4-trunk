/*
 * Copyright 2004-2016 ICEsoft Technologies Canada Corp.
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

package org.icefaces.samples.showcase.example.ace.buttonGroup;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ValueChangeEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ManagedBean(name= ButtonGroupDataTable.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class ButtonGroupDataTable implements Serializable {
	private static final long serialVersionUID = 1L;
    public static final String BEAN_NAME = "buttonGroupDataTable";
    public static final String GROUP1_NAME="frm2:group1";
    public static final String GROUP2_NAME="frm2:group2";
    public static final String GROUP3_NAME="frm2:group3";
    public static final String GROUP4_NAME="frm2:group4";

    public String getBeanName() { return BEAN_NAME; }
	public int numberOfRows = 10;
	private List<ButtonGroupObject> buttonGroups = new ArrayList<ButtonGroupObject>() ;
    private String radio1Group, radio2Group, check1Group, check2Group, group;


	private String checkboxDescription;
	private String radioDescription;
	public ButtonGroupDataTable(){
        this.numberOfRows=10;
        this.setData();
        /*initial state is each column has own group*/
        radio1Group=GROUP1_NAME;
        radio2Group=GROUP2_NAME;
        check1Group=GROUP3_NAME;
        check2Group=GROUP4_NAME;
        group = "eachOwn";
	}


    public void groupChange(ValueChangeEvent event){
        this.buttonGroups.clear();
        this.setData();
        String valueObj = String.valueOf(event.getNewValue());
        if (valueObj.equals("byButtonType")){
            radio1Group = GROUP1_NAME;
            radio2Group= GROUP1_NAME;
            check1Group = GROUP2_NAME;
            check2Group = GROUP2_NAME;
        } else  {
            radio1Group = GROUP1_NAME;
            radio2Group= GROUP2_NAME;
            check1Group = GROUP3_NAME;
            check2Group = GROUP4_NAME;
        }
    }
	public List<ButtonGroupObject> getButtonGroups() {
		return buttonGroups;
	}

	public void setButtonGroups(List<ButtonGroupObject> buttonGroups) {
		this.buttonGroups = buttonGroups;
	}

	public int getNumberOfRows() {
		return numberOfRows;
	}

	public void setNumberOfRows(int numberOfRows) {
		this.numberOfRows = numberOfRows;
	}

    public String getRadio1Group() {
        return radio1Group;
    }

    public void setRadio1Group(String radio1Group) {
        this.radio1Group = radio1Group;
    }

    public String getRadio2Group() {
        return radio2Group;
    }

    public void setRadio2Group(String radio2Group) {
        this.radio2Group = radio2Group;
    }

    public String getCheck1Group() {
        return check1Group;
    }

    public void setCheck1Group(String check1Group) {
        this.check1Group = check1Group;
    }

    public String getCheck2Group() {
        return check2Group;
    }

    public void setCheck2Group(String check2Group) {
        this.check2Group = check2Group;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public void setData(){
       for (int i=0; i<numberOfRows; i++){
     		buttonGroups.add(new ButtonGroupObject(i));
        }
    }
    public void updateRows(ValueChangeEvent event){
        try{
            this.numberOfRows = Integer.parseInt(String.valueOf(event.getNewValue()));
        }   catch (NumberFormatException e){
            e.printStackTrace();
        }
        this.buttonGroups.clear();
        setData();
    }
}
