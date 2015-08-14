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

import java.util.Iterator;
import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ValueChangeEvent;
import java.io.Serializable;
import java.lang.String;
import java.lang.System;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;


@ComponentExample(
        parent = PanelStackBean.BEAN_NAME,
        title = "example.ace.panelStack.facelet.title",
        description = "example.ace.panelStack.facelet.description",
        example = "/resources/examples/ace/panelStack/panelStackFacelet.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="panelStackFacelet.xhtml",
                    resource = "/resources/examples/ace/panelStack/panelStackFacelet.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="PanelStackFacelet.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                    "/example/ace/panelStack/PanelStackFacelet.java")
        }
)

@ManagedBean(name = PanelStackFacelet.BEAN_NAME)
@CustomScoped(value = "#{window}")


public class PanelStackFacelet extends ComponentExampleImpl<PanelStackFacelet> implements Serializable {

	public static final String BEAN_NAME = "panelStackFacelet";

	private static final long serialVersionUID = 1L;
	private String currentId = "stackPane1";
	private String name, name2, address, phone, phone2, city, title1, note,
	 occasion, note2, servicesString;	
	private String time = "6pm";	

	private boolean facelet = false;
	private boolean r1 = true;
	private boolean r2 = false;	
	private boolean r3 = true;
	private boolean r4 = false;
	private boolean r5 = false;
	private boolean r6 = false;
	private boolean valet = false;
	private boolean cb1 = false;
	private boolean cb2 = false;
	private boolean cb3 = false;
	private boolean cb4 = false;
	
	private int sliderValue;
	private ArrayList<String> services;	
	private Date selectedDate = new Date(System.currentTimeMillis());
	
	public PanelStackFacelet() {
		  super(PanelStackFacelet.class);
	}
	
	@PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

	public String getCurrentId() {
		if(r1) currentId = "stackPane1";
		else currentId = "stackPane2";
		return currentId;
	}

	public void setCurrentId(String currentId) {
		this.currentId = currentId;
	}
	
	
	public Date getSelectedDate() {
		return selectedDate;
	}

	public void setSelectedDate(Date selectedDate) {
		this.selectedDate = selectedDate;
	}	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName2() {
		return name2;
	}

	public void setName2(String name2) {
		this.name2 = name2;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPhone2() {
		return phone2;
	}

	public void setPhone2(String phone2) {
		this.phone2 = phone2;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public boolean isFacelet() {	
		return facelet;
	}

	public void setFacelet(boolean facelet) {		
		this.facelet = facelet;
	}
	
	
	public boolean isR1() {
		return r1;
	}

	public void setR1(boolean r1) {
		this.r1 = r1;
	}

	public boolean isR2() {
		return r2;
	}

	public void setR2(boolean r2) {
		this.r2 = r2;
	}
	
	public String getTitle1() {
		if (r3) title1 = "Mr.";
		else if (r4) title1 = "Mrs.";
		else if (r5) title1 = "Ms.";
		else title1 = "Dr.";		
		return title1;
	}

	public void setTitle1(String title1) {
		this.title1 = title1;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	
	public boolean isR3() {				
		return r3;
	}

	public void setR3(boolean r3) {
		this.r3 = r3;
	}

	public boolean isR4() {
		return r4;
	}

	public void setR4(boolean r4) {
		this.r4 = r4;
	}

	public boolean isR5() {
		return r5;
	}

	public void setR5(boolean r5) {
		this.r5 = r5;
	}

	public boolean isR6() {
		return r6;
	}

	public void setR6(boolean r6) {
		this.r6 = r6;
	}

	public String getOccasion() {
		return occasion;
	}

	public void setOccasion(String occasion) {
		this.occasion = occasion;
	}	

	public String getNote2() {
		return note2;
	}

	public void setNote2(String note2) {
		this.note2 = note2;
	}

	
	public boolean isValet() {
		return valet;
	}

	public void setValet(boolean valet) {
		this.valet = valet;
	}
	
	public boolean isCb1() {
		return cb1;
	}

	public void setCb1(boolean cb1) {
		this.cb1 = cb1;
	}

	public boolean isCb2() {
		return cb2;
	}

	public void setCb2(boolean cb2) {
		this.cb2 = cb2;
	}

	public boolean isCb3() {
		return cb3;
	}

	public void setCb3(boolean cb3) {
		this.cb3 = cb3;
	}

	public boolean isCb4() {
		return cb4;
	}

	public void setCb4(boolean cb4) {
		this.cb4 = cb4;
	}
	
	public int getSliderValue() {
		return sliderValue;
	}

	public void setSliderValue(int sliderValue) {
		this.sliderValue = sliderValue;
	}
	
	public ArrayList<String> getServices() {
		
		return services;
	}

	public void setServices(ArrayList<String> services) {
	this.services = services;
	}

	public String getServicesString() {	
		services = new ArrayList<String>();	
		if (cb1) services.add("Private table ");
		if (cb2) services.add("Roses ");
		if (cb3) services.add("Champagne ");
		if (cb4) services.add("Wine Cellar ");
		
		StringBuilder sb = new StringBuilder();
		for(String s : services)
		{
			sb.append(s);			
		}
		
		servicesString = sb.toString();
		return servicesString;
	}
	
	
	public void setServicesString(String servicesString) {
		this.servicesString = servicesString;
	}

	
	
}
