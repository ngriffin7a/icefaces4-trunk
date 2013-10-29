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

package org.icefaces.application.showcase.model.service;

import java.util.ArrayList;

/**
 * <p>Simple Employee Service inteface.  This mock service layer is intended to 
 * make our dataTable and other record based examples behave like real world
 * applications.<p>
 *
 * @since 1.7
 */
public interface EmployeeService {

    /**
     * Gets a list of Employee records that matches the supplied criteria.
     *
     * @param listSize number of records that will be retreived from service layer
     * @param isDescending true indicates a descending order; otherwise, descending
     * @param sortColumn column name set will be sorted on.
     * @return list of Employee objects. 
     */
    public ArrayList getEmployees(int listSize, boolean isDescending,
                                            final String sortColumn);

    /**
     * Gets a list of Employee records that matches the supplied criteria.
     *
     * @param listSize number of records that will be retreived from service layer
     * @return list of Employee objects.
     */
    public ArrayList getEmployees(int listSize);
}
