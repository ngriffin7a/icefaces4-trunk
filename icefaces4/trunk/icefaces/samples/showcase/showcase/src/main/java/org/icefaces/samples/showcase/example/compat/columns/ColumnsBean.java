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

package org.icefaces.samples.showcase.example.compat.columns;

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
import org.icefaces.samples.showcase.metadata.annotation.Menu;
import org.icefaces.samples.showcase.metadata.annotation.MenuLink;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

@ComponentExample(
        title = "example.compat.columns.title",
        description = "example.compat.columns.description",
        example = "/resources/examples/compat/columns/columns.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="columns.xhtml",
                    resource = "/resources/examples/compat/"+
                               "columns/columns.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="ColumnsBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/columns/ColumnsBean.java")
        }
)
@Menu(
	title = "menu.compat.columns.subMenu.title",
	menuLinks = {
            @MenuLink(title = "menu.compat.columns.subMenu.main",
                    isDefault = true,
                    exampleBeanName = ColumnsBean.BEAN_NAME),
            @MenuLink(title = "menu.compat.columns.subMenu.checker",
                    exampleBeanName = ColumnsChecker.BEAN_NAME)
})
@ManagedBean(name= ColumnsBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class ColumnsBean extends ComponentExampleImpl<ColumnsBean> implements Serializable {
	
	public static final String BEAN_NAME = "columns";
	
	private int columnNum = 3;
	private int rowNum = 10;
	private List<String> rowData;
	private List<String> columnData;
	
	public ColumnsBean() {
                    super(ColumnsBean.class);
                    init();
	}
	
    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

	private void init() {
	    generateRows();
	    generateColumns();
	}
	
	public int getRowNum() { return rowNum; }
	public int getColumnNum() { return columnNum; }
	public List<String> getRowData() { return rowData; }
	public List<String> getColumnData() { return columnData; }
	
	public void setRowNum(int rowNum) { this.rowNum = rowNum; }
	public void setColumnNum(int columnNum) { this.columnNum = columnNum; }
	public void setRowData(List<String> rowData) { this.rowData = rowData; }
	public void setColumnData(List<String> columnData) { this.columnData = columnData; }
	
	private void generateRows() {
	    rowData = generateData(getRowNum());
	}
	
	private void generateColumns() {
	    columnData = generateData(getColumnNum());
	}
	
	private List<String> generateData(int count) {
	    List<String> toReturn = new ArrayList<String>(count);
	    
	    for (int i = 0; i < count; i++) {
	        toReturn.add(String.valueOf(i+1));
	    }
	    
	    return toReturn;
	}
	
	public void applyChanges(ActionEvent event) {
	    generateRows();
	    generateColumns();
	}
}
