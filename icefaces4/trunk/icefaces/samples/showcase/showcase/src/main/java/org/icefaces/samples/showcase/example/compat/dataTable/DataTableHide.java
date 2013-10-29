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

package org.icefaces.samples.showcase.example.compat.dataTable;

import org.icefaces.samples.showcase.dataGenerators.utilityClasses.DataTableData;
import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;

import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

@ComponentExample(
        parent = DataTableBean.BEAN_NAME,
        title = "example.compat.dataTable.hide.title",
        description = "example.compat.dataTable.hide.description",
        example = "/resources/examples/compat/dataTable/dataTableHide.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="dataTableHide.xhtml",
                    resource = "/resources/examples/compat/"+
                               "dataTable/dataTableHide.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="DataTableHide.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/dataTable/DataTableHide.java")
        }
)
@ManagedBean(name= DataTableHide.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class DataTableHide extends ComponentExampleImpl<DataTableHide> implements Serializable {
	
    public static final String BEAN_NAME = "dataTableHide";

    private boolean renderId;
    private boolean renderName;
    private boolean renderChassis;
    private boolean renderWeight;
    private boolean renderAcceleration;
    private boolean renderMpg;
    private boolean renderCost;
    private int rows;
    private List<Car> cars;

    public DataTableHide() {
        super(DataTableHide.class);
        initializeDefaults();
    }
    
    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    public void showAll(ActionEvent event)
    {
        applyAll(true);
    }

    public void hideAll(ActionEvent event) 
    {
        applyAll(false);
    }
    
    public String toggleColumn() {
        return null;
    }
    
    private void initializeDefaults()
    {
        renderId = false;
        renderName = true;
        renderChassis = true;
        renderWeight = true;
        renderAcceleration = false;
        renderMpg = false;
        renderCost = true;
        rows = DataTableData.DEFAULT_ROWS;
        cars = new ArrayList<Car>(DataTableData.getDefaultData());
    }

    private void applyAll(boolean set) {
        renderId = set;
        renderName = set;
        renderChassis = set;
        renderWeight = set;
        renderAcceleration = set;
        renderMpg = set;
        renderCost = set;
    }

    public boolean isRenderId() {return renderId;}
    public boolean isRenderName() {return renderName;}
    public boolean isRenderChassis() {return renderChassis;}
    public boolean isRenderWeight() {return renderWeight;}
    public boolean isRenderAcceleration() {return renderAcceleration;}
    public boolean isRenderMpg() {return renderMpg;}
    public boolean isRenderCost() {return renderCost;}
    public List<Car> getCars() {return cars;}
    public int getRows() {return rows;}
    
    public void setRenderId(boolean renderId) {this.renderId = renderId;}
    public void setRenderName(boolean renderName) {this.renderName = renderName;}
    public void setRenderChassis(boolean renderChassis) {this.renderChassis = renderChassis;}
    public void setRenderWeight(boolean renderWeight) {this.renderWeight = renderWeight;}
    public void setRenderAcceleration(boolean renderAcceleration) {this.renderAcceleration = renderAcceleration;}
    public void setRenderMpg(boolean renderMpg) {this.renderMpg = renderMpg;}
    public void setRenderCost(boolean renderCost) {this.renderCost = renderCost;}
    public void setCars(List<Car> cars) {this.cars = cars;}
    public void setRows(int rows) {this.rows = rows;}
}
