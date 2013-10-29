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

import java.io.Serializable;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import org.icefaces.samples.showcase.dataGenerators.utilityClasses.DataTableData;
import org.icefaces.samples.showcase.view.navigation.NavigationController;
import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

@ComponentExample(
        parent = DataTableBean.BEAN_NAME,
        title = "example.compat.dataTable.widths.title",
        description = "example.compat.dataTable.widths.description",
        example = "/resources/examples/compat/dataTable/dataTableWidths.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="dataTableWidths.xhtml",
                    resource = "/resources/examples/compat/"+
                               "dataTable/dataTableWidths.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="DataTableWidths.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/dataTable/DataTableWidths.java")
        }
)
@ManagedBean(name= DataTableWidths.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class DataTableWidths extends ComponentExampleImpl<DataTableWidths> implements Serializable {
	
	public static final String BEAN_NAME = "dataTableWidths";
	
	private static final int NUM_COLS;
	private static final int DEFAULT_WIDTH;
	private static final int WIDTH_INCREMENT;
	private static final int MIN_WIDTH;
	private static final int MAX_WIDTH;
                private static final int[] RANDOM_WIDTHS;
                private int pointer;
	private ArrayList<Car> cars;
                private int defaultRows;
	private int currentWidth = DEFAULT_WIDTH;
	private String widthString;
        
                static
                {
                    NUM_COLS = 8;
                    DEFAULT_WIDTH = 55;
                    WIDTH_INCREMENT = 10;
                    MIN_WIDTH = 25;
                    MAX_WIDTH = 300;
                    RANDOM_WIDTHS = new int [] {37, 68, 45, 81, 53, 77,  29, 76, 32, 71, 54};
                }
	
	public DataTableWidths() {
                    super(DataTableWidths.class);
                    cars = new ArrayList<Car>(DataTableData.getDefaultData());
                    defaultRows = DataTableData.DEFAULT_ROWS;
                    widthString = buildWidthString(currentWidth);
                    pointer = 0;
	}
	
    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

	private void rebuildWidthString() {
	    widthString = buildWidthString(currentWidth);
	    
	    NavigationController.refreshPage();
	}
	
	private String buildWidthString(int width) {
	    StringBuilder toReturn = new StringBuilder(NUM_COLS * 5);
	    
	    for (int i = 0; i < NUM_COLS; i++) {
	        toReturn.append(width);
	        toReturn.append("px");
	        
	        if ((i+1) < NUM_COLS) {
	            toReturn.append(",");
	        }
	    }
	    
	    return toReturn.toString();
	}
	
	public void increaseWidth(ActionEvent event) {
	    currentWidth += WIDTH_INCREMENT;
	    
	    if (currentWidth > MAX_WIDTH) {
	        currentWidth = MAX_WIDTH;
	    }
	    
	    rebuildWidthString();
	}
	
	public void decreaseWidth(ActionEvent event) {
	    currentWidth -= WIDTH_INCREMENT;
	    
	    if (currentWidth < MIN_WIDTH) {
	        currentWidth = MIN_WIDTH;
	    }
	    
	    rebuildWidthString();
	}
	
	public void randomWidth(ActionEvent event) {
	    currentWidth = getRandomWidth();
	    rebuildWidthString();
	}
	
	public void defaultWidth(ActionEvent event) {
	    currentWidth = DEFAULT_WIDTH;
	    
	    rebuildWidthString();
	}
        
                private int getRandomWidth() 
                {
                     if(pointer == RANDOM_WIDTHS.length)
                        pointer = 0;
                     
                    int value = RANDOM_WIDTHS[pointer];
                    pointer++; 
                    return value;
                }
        
        	public String getWidthString() { return widthString; }
                public ArrayList<Car> getCars() { return cars; }
                public int getDefaultRows() { return defaultRows; }
                
	public void setWidthString(String widthString) { this.widthString = widthString; }
}
