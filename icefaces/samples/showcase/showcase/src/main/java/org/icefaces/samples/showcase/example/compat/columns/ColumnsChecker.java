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
import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

@ComponentExample(
        parent = ColumnsBean.BEAN_NAME,
        title = "example.compat.columns.checker.title",
        description = "example.compat.columns.checker.description",
        example = "/resources/examples/compat/columns/columnsChecker.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="columnsChecker.xhtml",
                    resource = "/resources/examples/compat/"+
                               "columns/columnsChecker.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="ColumnsChecker.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/columns/ColumnsChecker.java")
        }
)
@ManagedBean(name= ColumnsChecker.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class ColumnsChecker extends ComponentExampleImpl<ColumnsChecker> implements Serializable {
	
	public static final String BEAN_NAME = "columnsChecker";
	
	private static final int BOARD_SIZE = 8;
	
	private List<Integer> rowData = generateData(BOARD_SIZE);
	private List<Integer> columnData = generateData(BOARD_SIZE);
	
	public ColumnsChecker() {
		super(ColumnsChecker.class);
	}
	
    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

	public List<Integer> getRowData() { return rowData; }
	public List<Integer> getColumnData() { return columnData; }
	
	public void setRowData(List<Integer> rowData) { this.rowData = rowData; }
	public void setColumnData(List<Integer> columnData) { this.columnData = columnData; }
	
	private List<Integer> generateData(int count) {
	    List<Integer> toReturn = new ArrayList<Integer>(count);
	    
	    for (int i = 0; i < count; i++) {
	        toReturn.add(i+1);
	    }
	    
	    return toReturn;
	}
}
