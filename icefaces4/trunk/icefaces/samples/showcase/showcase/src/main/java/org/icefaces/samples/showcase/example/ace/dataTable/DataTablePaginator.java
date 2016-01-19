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

package org.icefaces.samples.showcase.example.ace.dataTable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.model.SelectItem;

import org.icefaces.samples.showcase.dataGenerators.utilityClasses.DataTableData;

@ManagedBean(name= DataTablePaginator.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class DataTablePaginator implements Serializable {
    public static final String BEAN_NAME = "dataTablePaginator";
	public String getBeanName() { return BEAN_NAME; }
    
    private static final SelectItem[] POSITION_AVAILABLE = { new SelectItem("bottom", "Bottom"),
                                                           new SelectItem("top", "Top"),
                                                           new SelectItem("both", "Both") };
    
    private boolean paginator = true;
    private boolean fastControls = false;
    private String position = POSITION_AVAILABLE[0].getValue().toString();
    private List<Car> carsData;
    private int rows = 10;
    private int pagesToSkip = 3;
    private int startPage = 1;
    
    /////////////---- CONSTRUCTOR BEGIN
    public DataTablePaginator() {
        carsData = new ArrayList<Car>(DataTableData.getDefaultData());
    }

    /////////////---- GETTERS & SETTERS BEGIN
    public boolean getPaginator() { return paginator; }
    public boolean getFastControls() { return fastControls; }
    public String getPosition() { return position; }
    public int getRows() { return rows; }
    public int getPagesToSkip() { return pagesToSkip; }
    public int getStartPage() { return startPage; }
    public SelectItem[] getPositionAvailable() { return POSITION_AVAILABLE; }
    public List<Car> getCarsData() { return carsData; }
    public int getStartPageMaximum() {
        return rows == 0 ? 1 : (int)Math.ceil(carsData.size()/(double)rows);
    }
    
    public void setPaginator(boolean paginator) { this.paginator = paginator; }
    public void setFastControls(boolean fastControls) { this.fastControls = fastControls; }
    public void setPosition(String position) { this.position = position; }
    public void setRows(int rows) {
        this.rows = rows;
        setStartPage(getStartPage());
    }
    public void setPagesToSkip(int pagesToSkip) { this.pagesToSkip = pagesToSkip; }
    public void setStartPage(int startPage) {
        this.startPage = startPage;
        int maxPages = getStartPageMaximum();
        if( this.startPage < 1 ){
            this.startPage = 1;
        } else if( startPage > maxPages ){
            this.startPage = maxPages;
        }
    }
    public void setCarsData(List<Car> carsData) { this.carsData = carsData; }
}
