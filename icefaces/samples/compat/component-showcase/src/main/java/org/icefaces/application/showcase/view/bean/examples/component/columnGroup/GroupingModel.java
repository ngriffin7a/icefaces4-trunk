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

package org.icefaces.application.showcase.view.bean.examples.component.columnGroup;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.icefaces.application.showcase.view.bean.examples.component.dataTable.DataTableBase;

/**
 * <p>The GroupingModel provides data for JSPX page that contains row and column
 * data grouping tags.</p>
 *
 * @since 1.7
 */
@ManagedBean(name="groupBean")
@ViewScoped
public class GroupingModel extends DataTableBase {

    protected void init(){
        // build employee list form employee service.
        employees = employeeService.getEmployees(25);
    }

}
