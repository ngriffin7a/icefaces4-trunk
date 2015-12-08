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
 
package org.icefaces.samples.showcase.example.ace.clientValidateRequired;

import java.io.Serializable;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import java.util.ArrayList;
import java.util.List;
import javax.faces.model.SelectItem;

@ManagedBean(name= ClientValidateRequiredBean.BEAN_NAME)
@CustomScoped(value = "#{window}")

public class ClientValidateRequiredBean implements Serializable {
	
	public static final String BEAN_NAME = "clientValidateRequiredBean";
	public String getBeanName() { return BEAN_NAME; }
		
	
	private String name;
	private String city;
	
	private SelectItem[] cities = new SelectItem[] {
			new SelectItem(""),
	        new SelectItem("Victoria"),
	        new SelectItem("Vancouver"),
	        new SelectItem("Edmonton"),
	        new SelectItem("Calgary"),
	        new SelectItem("Saskatoon"),
	        new SelectItem("Regina"),
	        new SelectItem("Winnipeg"),
	     };
	
	 private List<String> selected = new ArrayList<String>();	 

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public SelectItem[] getCities() {
		return cities;
	}

	public void setCities(SelectItem[] cities) {
		this.cities = cities;
	}

	public List<String> getSelected() {
		return selected;
	}

	public void setSelected(List<String> selected) {
		this.selected = selected;
	}
	
	

}
