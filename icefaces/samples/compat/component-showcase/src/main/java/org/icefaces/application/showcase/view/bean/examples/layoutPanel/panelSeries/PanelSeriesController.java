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

package org.icefaces.application.showcase.view.bean.examples.layoutPanel.panelSeries;

import org.icefaces.application.showcase.view.bean.examples.component.dataTable.DataTableBase;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import java.io.Serializable;

/**
 * <p>The PanelSeriesController is a simple class that maintains an list of
 * employees. The class also has two methods which either add or removed
 * employees from the list. </p>
 * <p>The mock employe service layer is used to retreive employee records.</p>
 *
 * @see org.icefaces.application.showcase.view.bean.examples.component.dataTable.DataTableBase
 * @since 1.7
 */
@ManagedBean(name = "panelSeriesController")
@ViewScoped
public class PanelSeriesController extends DataTableBase implements Serializable {

    // start off with 3 employees.
    private int employeeCount = 3;

    /**
     * Adds a new employee to the list of employees.
     * @param event jsf action event
     */
    public void addEmployee(ActionEvent event){
        employeeCount++;
        employees = employeeService.getEmployees(employeeCount);
    }

    /**
     * Removes a new employee to the list of employees.
     * @param event jsf action event
     */
    public void removeEmployee(ActionEvent event){
        employeeCount--;
        employees = employeeService.getEmployees(employeeCount);
    }

    public int getEmployeeCount(){
        return employeeCount;
    }

    protected void init() {
        employees = employeeService.getEmployees(employeeCount); 
    }

}
