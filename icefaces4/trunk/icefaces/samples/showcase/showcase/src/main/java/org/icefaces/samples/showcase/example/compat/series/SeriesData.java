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

package org.icefaces.samples.showcase.example.compat.series;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;

@ManagedBean(name= SeriesData.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class SeriesData implements Serializable {
    public static final String BEAN_NAME = "seriesData";

    private static Random randomizer = new Random(System.nanoTime());
    public static List<Employee> EMPLOYEES = generateEmployees();

    public List<Employee> getEmployees() { return EMPLOYEES; }

    private static List<Employee> generateEmployees() {
        List<Employee> toReturn = new ArrayList<Employee>(10);
        int baseId = 99;

        toReturn.add(makeEmployee(++baseId, "Dorothy", "Wentworth", "3517 Lighthouse Drive"));
        toReturn.add(makeEmployee(++baseId, "Leland", "Carreon", "3939 Smith Street"));
                toReturn.add(makeEmployee(++baseId, "Stevie", "Parodi", "1164 Harper Street"));
                toReturn.add(makeEmployee(++baseId, "Jennie", "Emory", "2489 James Street"));
        return toReturn;
    }

    private static Employee makeEmployee(int id, String fName, String lName, String address) {
        return new Employee(id,
                            fName, lName, address,
                            10000+randomizer.nextInt(80000));
    }

    public static boolean empty() {
        return ((EMPLOYEES == null) || (EMPLOYEES.size() == 0));
    }

    public static void defaultEmployees() {
        EMPLOYEES = generateEmployees();
    }

    public static void addEmployee(Employee employee) {
        EMPLOYEES.add(employee);
    }

    public static Employee removeEmployee(int index) {
    return EMPLOYEES.remove(index);
    }
}
