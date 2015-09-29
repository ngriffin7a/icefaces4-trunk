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

package org.icefaces.samples.showcase.example.ace.list;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.model.SelectItem;

import org.icefaces.samples.showcase.dataGenerators.utilityClasses.DataTableData;
import org.icefaces.samples.showcase.example.ace.dataTable.Car;

@ManagedBean(name= ListBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class ListBean implements Serializable {
    public static final String BEAN_NAME = "listBean";
	public String getBeanName() { return BEAN_NAME; }

    public ListBean() {
        // Move some of cars to other lists at init
        List<Car> removals = carList.subList(6,10);
        fstDestCarList = new ArrayList<Car>(removals.subList(0, 2));
        sndDestCarList = new ArrayList<Car>(removals.subList(2, 4));
        carList = carList.subList(0,6);
    }

    List<SelectItem> stringList = new ArrayList<SelectItem>() {{
        for (String s : DataTableData.CHASSIS_ALL) {
            add(new SelectItem(s));
        }
        remove(DataTableData.CHASSIS_ALL.length-1);
    }};

    List<SelectItem> destStringList = new ArrayList<SelectItem>() {{
        add(new SelectItem(DataTableData.CHASSIS_ALL[DataTableData.CHASSIS_ALL.length-1]));
    }};

    List<Car> carList = DataTableData.getDefaultData().subList(0,10);
    List<Car> fstDestCarList;
    List<Car> sndDestCarList;




    public List<SelectItem> getStringList() {
        return stringList;
    }

    public void setStringList(List<SelectItem> stringList) {
        this.stringList = stringList;
    }

    public List<SelectItem> getDestStringList() {
        return destStringList;
    }

    public void setDestStringList(List<SelectItem> destStringList) {
        this.destStringList = destStringList;
    }

    public List<Car> getCarList() {
        return carList;
    }

    public void setCarList(List<Car> carList) {
        this.carList = carList;
    }

    public List<Car> getFstDestCarList() {
        return fstDestCarList;
    }

    public void setFstDestCarList(List<Car> fstDestCarList) {
        this.fstDestCarList = fstDestCarList;
    }

    public List<Car> getSndDestCarList() {
        return sndDestCarList;
    }

    public void setSndDestCarList(List<Car> sndDestCarList) {
        this.sndDestCarList = sndDestCarList;
    }
}
