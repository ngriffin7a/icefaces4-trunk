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

package org.icefaces.samples.showcase.example.ace.dataExporter;

import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.model.SelectItem;

import java.util.List;
import java.util.ArrayList;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import org.icefaces.ace.component.datatable.DataTable;
import org.icefaces.ace.component.panelexpansion.PanelExpansion;
import org.icefaces.ace.component.dataexporter.OuterTableCSVExporter;
import org.icefaces.ace.component.dataexporter.InnerTableCSVExporter;

@ComponentExample(
        parent = DataExporterBean.BEAN_NAME,
        title = "example.ace.dataExporter.custom.title",
        description = "example.ace.dataExporter.custom.description",
        example = "/resources/examples/ace/dataExporter/dataExporterCustom.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="dataExporterCustom.xhtml",
                    resource = "/resources/examples/ace/dataExporter/dataExporterCustom.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="DataExporterCustom.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                    "/example/ace/dataExporter/DataExporterCustom.java")
        }
)
@ManagedBean(name= DataExporterCustom.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class DataExporterCustom extends ComponentExampleImpl<DataExporterCustom> implements Serializable {
	public static final String BEAN_NAME = "dataExporterCustom";
	
	private static final String OUTER_TABLE_ID = "carTable";
	
    public DataExporterCustom() { 
		super(DataExporterCustom.class);
	}
	
    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }
	
	public static List<CustomCar> getRandomData() {
		CustomVehicleGenerator generator = new CustomVehicleGenerator();
		return generator.getRandomCars(30);
	}
	
    private List<CustomCar> carsData = getRandomData();
	public List<CustomCar> getCarsData() { return carsData; }
    public void setCarsData(List<CustomCar> carsData) { this.carsData = carsData; }

    private String selectedItem = null;
	public String getSelectedItem() { return selectedItem; }
    public void setSelectedItem(String selectedItem) { this.selectedItem = selectedItem; }	
	
	public List<SelectItem> getSelectItems() {
		List<SelectItem> selectItems = new ArrayList<SelectItem>();
		FacesContext facesContext = FacesContext.getCurrentInstance();
		UIComponent table = findComponentCustom(facesContext.getViewRoot(), OUTER_TABLE_ID);
		if (table != null) {
			DataTable outerTable = (DataTable) table;
			PanelExpansion pe = outerTable.getPanelExpansion();
			if (pe != null) {
				populateItems(pe, selectItems);
			}
		} else {
			throw new FacesException("Table with id '" + OUTER_TABLE_ID + "' not found in view.");
		}
		return selectItems;
	}
	
	private void populateItems(UIComponent component, List<SelectItem> selectItems) {
		for (UIComponent kid : component.getChildren()) {
			if (kid instanceof DataTable) {
				selectItems.add(new SelectItem(kid.getId(), kid.getId()));
			}
			if (kid.getChildren().size() > 0) {
				populateItems(kid, selectItems);
			}
		}		
	}
	
	public Object getCustomExporter() {
		return new OuterTableCSVExporter(selectedItem);
	}
	
	private UIComponent findComponentCustom(UIComponent base, String id) {
		if (base.getId() != null && base.getId().equals(id)) return base;
		List<UIComponent> children = base.getChildren();
		UIComponent result = null;
		for (UIComponent child : children) {
			result = findComponentCustom(child, id);
			if (result != null) break;
		}
		return result;
	}
}
