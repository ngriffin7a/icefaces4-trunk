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

package org.icefaces.application.showcase.view.bean.examples.component.commandSortHeader;

import org.icefaces.application.showcase.view.bean.examples.component.dataTable.DataTableBase;
import org.icefaces.application.showcase.view.bean.BeanNames;
import org.icefaces.application.showcase.model.entity.Employee;
import org.icefaces.application.showcase.util.FacesUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.PhaseListener;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;

/**
 *
 */
@ManagedBean
@ViewScoped
public class SortHeaderModel extends DataTableBase {

    private static final Log logger =
            LogFactory.getLog(SortHeaderModel.class);

    private boolean descending = true;
    private String columnName = Employee.DEPARTMENT_NAME_COLUMN;


    protected void init() {
        // build employee list form employee service.
        employees = employeeService.getEmployees(50, descending,
                columnName);
    }

    public void sort() {
        init();
    }

    public boolean isDescending() {
        return descending;
    }

    public void setDescending(boolean descending) {
        this.descending = descending;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }
}
