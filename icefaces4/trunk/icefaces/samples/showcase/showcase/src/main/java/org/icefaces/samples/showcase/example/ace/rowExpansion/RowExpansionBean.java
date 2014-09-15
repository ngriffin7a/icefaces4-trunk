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

package org.icefaces.samples.showcase.example.ace.rowExpansion;

import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;

import java.util.*;
import org.icefaces.samples.showcase.dataGenerators.utilityClasses.DataTableData;
import org.icefaces.samples.showcase.example.ace.dataTable.Car;
import org.icefaces.samples.showcase.util.SimpleEntry;

@ComponentExample(
        title = "example.ace.rowExpansion.title",
        description = "example.ace.rowExpansion.description",
        example = "/resources/examples/ace/rowExpansion/rowExpansion.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="rowExpansion.xhtml",
                    resource = "/resources/examples/ace/rowExpansion/rowExpansion.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="RowExpansionBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                    "/example/ace/rowExpansion/RowExpansionBean.java")
        }
)
@Menu(
    title = "menu.ace.rowExpansion.subMenu.title", 
    menuLinks = {
        @MenuLink(title = "menu.ace.rowExpansion.subMenu.main", isDefault = true, exampleBeanName = RowExpansionBean.BEAN_NAME)
    }
)

@ManagedBean(name = RowExpansionBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class RowExpansionBean extends ComponentExampleImpl<RowExpansionBean> implements Serializable {
    public static final String BEAN_NAME = "rowExpansionBean";
    
    public RowExpansionBean() {
        super(RowExpansionBean.class);
        generateCarsData();
    }
    
    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
        setGroup(4);
    }

    ArrayList<Map.Entry<Car,List>> carsData = null;

    private void generateCarsData() {
        carsData = new ArrayList<Map.Entry<Car, List>>();
        for (Car c : DataTableData.getDefaultData()) {
            ArrayList<Map.Entry<Car, List>> detailData = new ArrayList<Map.Entry<Car, List>>();
            detailData.add(new SimpleEntry(new Car(c.getId()+1000, c.getName()+
                " Custom Spec", c.getChassis(), c.getColor(), c.getYear(),
                c.getWeight(), c.getAcceleration()*2, c.getMpg()/2,
                c.getCost()*3), null));
            carsData.add(new SimpleEntry(c, detailData));
        }
    }

    public ArrayList<Map.Entry<Car,List>> getCarsData() { return carsData;}
    public void setCarsData(ArrayList<Map.Entry<Car,List>> carsData) {this.carsData = carsData;}
}
