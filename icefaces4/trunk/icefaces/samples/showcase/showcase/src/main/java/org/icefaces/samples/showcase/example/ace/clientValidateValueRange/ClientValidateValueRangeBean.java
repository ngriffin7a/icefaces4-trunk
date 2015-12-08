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
 
package org.icefaces.samples.showcase.example.ace.clientValidateValueRange;

import java.io.Serializable;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;


@ManagedBean(name= ClientValidateValueRangeBean.BEAN_NAME)
@CustomScoped(value = "#{window}")

public class ClientValidateValueRangeBean implements Serializable {
	
	public static final String BEAN_NAME = "clientValidateValueRangeBean";
	public String getBeanName() { return BEAN_NAME; }
		
	private String value1;
	private String value2;
	private String value3;
	
	public String getValue1() {
		return value1;
	}

	public void setValue1(String value1) {
		this.value1 = value1;
	}
		
	public String getValue2() {
		return value2;
	}

	public void setValue2(String value2) {
		this.value2 = value2;
	}
	
	public String getValue3() {
		return value3;
	}

	public void setValue3(String value3) {
		this.value3 = value3;
	}
	

}
