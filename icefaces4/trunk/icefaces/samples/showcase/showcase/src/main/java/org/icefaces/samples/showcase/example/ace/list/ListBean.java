/*
 * Copyright 2004-2013 ICEsoft Technologies Canada Corp.
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

import org.icefaces.samples.showcase.dataGenerators.utilityClasses.DataTableData;
import org.icefaces.samples.showcase.example.compat.dataTable.Car;
import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.model.SelectItem;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@ComponentExample(
    title = "example.ace.list.title",
    description = "example.ace.list.description",
    example = "/resources/examples/ace/list/list.xhtml"
)
@ExampleResources(
    resources ={
        // xhtml
        @ExampleResource(type = ResourceType.xhtml,
                title="List.xhtml",
                resource = "/resources/examples/ace/"+
                        "list/list.xhtml"),
        // Java Source
        @ExampleResource(type = ResourceType.java,
                title="ListBean.java",
                resource = "/WEB-INF/classes/org/icefaces/samples/"+
                        "showcase/example/ace/list/ListBean.java")
    }
)
@Menu(
    title = "menu.ace.list.subMenu.title",
    menuLinks = {
            @MenuLink(title = "menu.ace.list.subMenu.main",
                    isDefault = true, exampleBeanName = ListBean.BEAN_NAME),
            @MenuLink(title = "menu.ace.list.subMenu.selection",
                    exampleBeanName = ListSelectionBean.BEAN_NAME),
            @MenuLink(title = "menu.ace.list.subMenu.selectionAjax",
                    exampleBeanName = ListSelectionAjaxBean.BEAN_NAME),
            @MenuLink(title = "menu.ace.list.subMenu.selectionMini",
                      exampleBeanName = ListSelectionMiniBean.BEAN_NAME),
            @MenuLink(title = "menu.ace.list.subMenu.reordering",
                    exampleBeanName = ListReorderBean.BEAN_NAME),
            @MenuLink(title = "menu.ace.list.subMenu.reorderingAjax",
                    exampleBeanName = ListReorderAjaxBean.BEAN_NAME),
            @MenuLink(title = "menu.ace.list.subMenu.drag",
                    exampleBeanName = ListDragBean.BEAN_NAME),
            @MenuLink(title = "menu.ace.list.subMenu.dual",
                    exampleBeanName = ListDualBean.BEAN_NAME),
            @MenuLink(title = "menu.ace.list.subMenu.multi",
                    exampleBeanName = ListMultiBean.BEAN_NAME),
            @MenuLink(title = "menu.ace.list.subMenu.block",
                    exampleBeanName = ListBlockBean.BEAN_NAME),
            @MenuLink(title = "menu.ace.list.subMenu.blockComplex",
                    exampleBeanName = ListBlockComplexBean.BEAN_NAME)
    })


@ManagedBean(name= ListBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class ListBean extends ComponentExampleImpl<ListBean> implements Serializable {
    public static final String BEAN_NAME = "listBean";

    public ListBean() {
        super(ListBean.class);

        // Move some of cars to other lists at init
        List<Car> removals = carList.subList(6,10);
        fstDestCarList = new ArrayList<Car>(removals.subList(0, 2));
        sndDestCarList = new ArrayList<Car>(removals.subList(2, 4));
        carList = carList.subList(0,6);
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
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
