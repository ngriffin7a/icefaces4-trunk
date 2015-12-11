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
 
package org.icefaces.samples.showcase.example.ace.clientValidateLength;

import java.io.Serializable;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import java.util.ArrayList;
import java.util.List;
import javax.faces.model.SelectItem;

@ManagedBean(name= ClientValidateLengthBean.BEAN_NAME)
@CustomScoped(value = "#{window}")

public class ClientValidateLengthBean implements Serializable {
	
	public static final String BEAN_NAME = "clientValidateLength";
	public String getBeanName() { return BEAN_NAME; }
	
	private static final long serialVersionUID = 1L;

	private String password = "";	
	private String userName = "";
	private String userName2 = "";
	private String comment = "";
	
    private List<String> selected = new ArrayList<String>();	
	private List<String> selected2 = new ArrayList<String>();	

	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getUserName2() {
		return userName2;
	}

	public void setUserName2(String userName2) {
		this.userName2 = userName2;
	}
	
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	

	public List<String> getSelected() {
		return selected;
	}

	public void setSelected(List<String> selected) {
		this.selected = selected;
	}
	
	public List<String> getSelected2() {
		return selected2;
	}

	public void setSelected2(List<String> selected2) {
		this.selected2 = selected2;
	}
	
	
}
