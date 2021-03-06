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

package org.icefaces.samples.showcase.example.ace.tooltip; 

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;

import org.icefaces.samples.showcase.dataGenerators.utilityClasses.DataTableData;
import org.icefaces.samples.showcase.example.ace.dataTable.Car;


@ManagedBean(name= DelegateTooltipBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class DelegateTooltipBean implements Serializable {

	private static final long serialVersionUID = 1L;
	public static final String BEAN_NAME = "delegateTooltipBean";
	public String getBeanName() { return BEAN_NAME; }
	
	private Object data;
	private List<Car> carList = new ArrayList<Car>();
	private boolean cancelListener = false;
	
	public DelegateTooltipBean() {
		carList = new ArrayList<Car>(DataTableData.getDefaultData());
	}
	
	public Object getData() {
		return data;
	}
	
	public void setData(Object data) {
		this.data = data;
	}
			
	public List<Car> getCarList() {
		return carList;
	}
	public void setCarList(List<Car> carList) {
		this.carList = carList;
	}

    public boolean isCancelListener() {
        return cancelListener;
    }

    public void setCancelListener(boolean cancelListener) {
        this.cancelListener = cancelListener;
    }
}
