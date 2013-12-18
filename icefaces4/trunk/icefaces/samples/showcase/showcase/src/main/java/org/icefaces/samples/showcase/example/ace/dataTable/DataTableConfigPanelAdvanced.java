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

package org.icefaces.samples.showcase.example.ace.dataTable;

import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;
import java.util.ArrayList;
import java.util.List;
import org.icefaces.samples.showcase.dataGenerators.utilityClasses.DataTableData;
import org.icefaces.samples.showcase.example.ace.dataTable.Car;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import java.io.Serializable;

@ComponentExample(
        parent = DataTableBean.BEAN_NAME,
        title = "example.ace.dataTable.configpaneladvanced.title",
        description = "example.ace.dataTable.configpaneladvanced.description",
        example = "/resources/examples/ace/dataTable/dataTableConfigPanelAdvanced.xhtml"
)
@ExampleResources(
        resources ={
                // xhtml
                @ExampleResource(type = ResourceType.xhtml,
                        title="dataTableConfigPanelAdvanced.xhtml",
                        resource = "/resources/examples/ace/dataTable/dataTableConfigPanelAdvanced.xhtml"),
                // Java Source
                @ExampleResource(type = ResourceType.java,
                        title="DataTableConfigPanelAdvanced.java",
                        resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                                "/example/ace/dataTable/DataTableConfigPanelAdvanced.java")
        }
)
@ManagedBean(name= DataTableConfigPanelAdvanced.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class DataTableConfigPanelAdvanced extends ComponentExampleImpl<DataTableConfigPanelAdvanced> implements Serializable {
    public static final String BEAN_NAME = "dataTableConfigPanelAdvanced";
    private List<Car> carsData;
    /////////////---- CONSTRUCTOR BEGIN
    public DataTableConfigPanelAdvanced() {
        super(DataTableConfigPanelAdvanced.class);
        carsData = new ArrayList<Car>(DataTableData.getDefaultData());
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    /////////////---- GETTERS & SETTERS BEGIN
    public List<Car> getCarsData() { return carsData; }
    public void setCarsData(List<Car> carsData) { this.carsData = carsData; }
	
	private ColumnSettings[] savedColumns = getDefaultColumns();
	private ColumnSettings[] columns = getDefaultColumns();
	private ColumnSettings[] savedHeaderColumns = getDefaultHeaderColumns();
	private ColumnSettings[] headerColumns = getDefaultHeaderColumns();
	private List<Integer> savedColumnOrder = getDefaultColumnOrder();
	private List<Integer> columnOrder = getDefaultColumnOrder();
	private List<Integer> savedColumnHeaderOrder = getDefaultColumnHeaderOrder();
	private List<Integer> columnHeaderOrder = getDefaultColumnHeaderOrder();
	
	private ColumnSettings[] getDefaultColumns() {
		return new ColumnSettings[] { new ColumnSettings(true, "ID", null, false),
			new ColumnSettings(true, "Name", null, false), new ColumnSettings(true, "Chassis", null, false), new ColumnSettings(true, "Weight", null, false),
			new ColumnSettings(true, "Accel", null, false), new ColumnSettings(true, "MPG", null, false), new ColumnSettings(true, "Cost", null, false)};
	}

	private ColumnSettings[] getDefaultHeaderColumns() {
		return new ColumnSettings[] { new ColumnSettings(true, "Car Details", null, false),
			new ColumnSettings(true, "Model", null, false), new ColumnSettings(true, "Specifications", null, false), new ColumnSettings(true, "Cost", null, false)};
	}

	private List<Integer> getDefaultColumnOrder() {
		List<Integer> order = new ArrayList<Integer>();
		order.add(0);
		order.add(1);
		order.add(2);
		order.add(3);
		order.add(4);
		order.add(5);
		order.add(6);
		return order;
	}

	private List<Integer> getDefaultColumnHeaderOrder() {
		List<Integer> order = new ArrayList<Integer>();
		order.add(0);
		order.add(1);
		order.add(2);
		order.add(3);
		order.add(4);
		order.add(5);
		order.add(6);
		return order;
	}

	public ColumnSettings[] getColumns() {
		return columns;
	}

	public ColumnSettings[] getHeaderColumns() {
		return headerColumns;
	}

	public List<Integer> getColumnOrder() {
		return columnOrder;
	}

	public void setColumnOrder(List<Integer> columnOrder) {
		this.columnOrder = columnOrder;
	}

	public List<Integer> getColumnHeaderOrder() {
		return columnHeaderOrder;
	}

	public void setColumnHeaderOrder(List<Integer> columnHeaderOrder) {
		this.columnHeaderOrder = columnHeaderOrder;
	}

	public void saveSettings(ActionEvent event) {
		savedColumns = columns;
		savedHeaderColumns = headerColumns;
		savedColumnOrder = columnOrder;
		savedColumnHeaderOrder = columnHeaderOrder;
	}

	public void restoreSavedSettings(ActionEvent event) {
		columns = savedColumns;
		headerColumns = savedHeaderColumns;
		columnOrder = savedColumnOrder;
		columnHeaderOrder = savedColumnHeaderOrder;
	}

	public void restoreDefaultSettings(ActionEvent event) {
		columns = getDefaultColumns();
		headerColumns = getDefaultHeaderColumns();
		columnOrder = getDefaultColumnOrder();
		columnHeaderOrder = getDefaultColumnHeaderOrder();
		carsData = new ArrayList<Car>(DataTableData.getDefaultData()); // for undoing sorting
	}

	public static class ColumnSettings implements Serializable {
		private boolean rendered;
		private String name;
		private Integer sortPriority;
		private boolean sortAscending;

		public ColumnSettings(boolean rendered, String name, Integer sortPriority, boolean sortAscending) {
			this.rendered = rendered;
			this.name = name;
			this.sortPriority = sortPriority;
			this.sortAscending = sortAscending;
		}

		public boolean isRendered() {
			return rendered;
		}

		public void setRendered(boolean rendered) {
			this.rendered = rendered;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Integer getSortPriority() {
			return sortPriority;
		}

		public void setSortPriority(Integer sortPriority) {
			this.sortPriority = sortPriority;
		}

		public boolean isSortAscending() {
			return sortAscending;
		}

		public void setSortAscending(boolean sortAscending) {
			this.sortAscending = sortAscending;
		}
	}
}
