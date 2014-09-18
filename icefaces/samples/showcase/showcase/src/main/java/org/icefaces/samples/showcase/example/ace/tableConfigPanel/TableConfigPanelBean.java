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

package org.icefaces.samples.showcase.example.ace.tableConfigPanel;

import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;
import org.icefaces.samples.showcase.dataGenerators.utilityClasses.DataTableData;
import org.icefaces.samples.showcase.example.ace.dataTable.Car;

import javax.faces.event.ActionEvent;

@ComponentExample(
        title = "example.ace.tableConfigPanel.title",
        description = "example.ace.tableConfigPanel.description",
        example = "/resources/examples/ace/tableConfigPanel/tableConfigPanel.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="tableConfigPanel.xhtml",
                    resource = "/resources/examples/ace/tableConfigPanel/tableConfigPanel.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="TableConfigPanelBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                    "/example/ace/tableConfigPanel/TableConfigPanelBean.java")
        }
)
@Menu(
	title = "menu.ace.tableConfigPanel.subMenu.main",
	menuLinks = {
	        @MenuLink(title = "menu.ace.tableConfigPanel.subMenu.main",
	                isDefault = true,
                    exampleBeanName = TableConfigPanelBean.BEAN_NAME),
	        @MenuLink(title = "menu.ace.tableConfigPanel.subMenu.advanced",
                    exampleBeanName = TableConfigPanelAdvancedBean.BEAN_NAME)
    }
)
@ManagedBean(name= TableConfigPanelBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class TableConfigPanelBean extends ComponentExampleImpl<TableConfigPanelBean> implements Serializable 
{
    public static final String BEAN_NAME = "tableConfigPanelBean";
    
    public TableConfigPanelBean() {
        super(TableConfigPanelBean.class);
        carsData = new ArrayList<Car>(DataTableData.getDefaultData());
    }
    
    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    private List<Car> carsData;

    public List<Car> getCarsData() { return carsData; }
    public void setCarsData(List<Car> carsData) { this.carsData = carsData; }
	
	private ColumnSettings[] savedColumns = getDefaultColumns();
	private ColumnSettings[] columns = getDefaultColumns();
	private List<Integer> savedColumnOrder = getDefaultColumnOrder();
	private List<Integer> columnOrder = getDefaultColumnOrder();
	private List<Integer> savedColumnHeaderOrder = getDefaultColumnHeaderOrder();
	private List<Integer> columnHeaderOrder = getDefaultColumnHeaderOrder();
	
	private ColumnSettings[] getDefaultColumns() {
		return new ColumnSettings[] { new ColumnSettings(true, "ID", null, false),
			new ColumnSettings(true, "Name", null, false), new ColumnSettings(true, "Chassis", null, false), new ColumnSettings(true, "Weight", null, false),
			new ColumnSettings(true, "Accel", null, false), new ColumnSettings(true, "MPG", null, false), new ColumnSettings(true, "Cost", null, false)};
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
		savedColumns = copyColumns(columns);
		savedColumnOrder = columnOrder;
		savedColumnHeaderOrder = columnHeaderOrder;
	}

	public void restoreSavedSettings(ActionEvent event) {
		columns = copyColumns(savedColumns);
		columnOrder = savedColumnOrder;
		columnHeaderOrder = savedColumnHeaderOrder;
	}

	public void restoreDefaultSettings(ActionEvent event) {
		columns = getDefaultColumns();
		columnOrder = getDefaultColumnOrder();
		columnHeaderOrder = getDefaultColumnHeaderOrder();
		carsData = new ArrayList<Car>(DataTableData.getDefaultData()); // for undoing sorting
	}

	private ColumnSettings[] copyColumns(ColumnSettings[] columns) {
		int length = columns.length;
		ColumnSettings[] copy = new ColumnSettings[length];
		for (int i = 0; i < length; i++) {
			ColumnSettings cs = columns[i];
			copy[i] = new ColumnSettings(cs.rendered, cs.name, cs.sortPriority, cs.sortAscending);
		}
		return copy;
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
