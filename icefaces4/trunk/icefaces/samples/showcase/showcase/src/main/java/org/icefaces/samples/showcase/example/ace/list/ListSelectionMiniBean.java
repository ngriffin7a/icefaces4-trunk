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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.model.SelectItem;

import org.icefaces.samples.showcase.dataGenerators.utilityClasses.DataTableData;
import org.icefaces.samples.showcase.example.ace.dataTable.Car;

@ManagedBean(name= ListSelectionMiniBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class ListSelectionMiniBean implements Serializable {
    public static final String BEAN_NAME = "listSelectionMiniBean";
	public String getBeanName() { return BEAN_NAME; }

    private List<SelectItem> selections = new ArrayList<SelectItem>();
    private Car selectItemObject = DataTableData.getDefaultData().subList(11,12).get(0);
    private Map<String, Car> selectItemMap = new HashMap<String, Car>() {{
        for (Car c : DataTableData.getDefaultData().subList(0,10))
            put(c.getName(), c);
    }};

    public Map<String, Car> getSelectItemMap() {
        return selectItemMap;
    }

    public void setSelectItemMap(Map<String, Car> selectItemMap) {
        this.selectItemMap = selectItemMap;
    }

    public List<SelectItem> getSelections() {
        return selections;
    }

    public void setSelections(List<SelectItem> selections) {
        this.selections = selections;
    }

    public Car getSelectItemObject() {
        return selectItemObject;
    }

    public void setSelectItemObject(Car selectItemObject) {
        this.selectItemObject = selectItemObject;
    }
}
