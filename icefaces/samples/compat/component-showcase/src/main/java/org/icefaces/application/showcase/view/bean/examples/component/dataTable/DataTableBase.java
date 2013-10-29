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

package org.icefaces.application.showcase.view.bean.examples.component.dataTable;

import org.icefaces.application.showcase.view.bean.BaseBean;
import org.icefaces.application.showcase.model.service.EmployeeService;
import org.icefaces.application.showcase.model.entity.Employee;

import java.util.ArrayList;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.NoneScoped;

/**
 * <p>The class DataTableBase is the base implementation for all DataTable
 * related examples.  This class should be extended for any dataTable example
 * to insure that the example has easy access to common example data.</p>
 *
 * @since 1.7
 */
@ManagedBean(eager=true)
//@NoneScoped
public class DataTableBase extends BaseBean {

    // mock service that retreives employee data
	@ManagedProperty(value="#{employeeServiceImpl}")
    protected EmployeeService employeeService;

    // internal list of retreived records.
    protected ArrayList employees;

    protected void init() {
        // build employee list form employee service.
        employees = employeeService.getEmployees(50, true,
                Employee.DEPARTMENT_NAME_COLUMN);
    }

    /**
     * Gets list of employee records retrieved from service layer.
     *
     * @return list of employees from service layer
     */
    public ArrayList getEmployees() {
        return employees;
    }

    /**
     * Sets the EmployeService reference.  Once the service has been set the
     * init method is called on this class to get the initial data for the
     * example.
     *
     * @param employeeService set the employeeService reference at which time
     *                        the init method is called on the class.
     */
    public void setEmployeeService(EmployeeService employeeService) {
        this.employeeService = employeeService;
        init();
    }
}