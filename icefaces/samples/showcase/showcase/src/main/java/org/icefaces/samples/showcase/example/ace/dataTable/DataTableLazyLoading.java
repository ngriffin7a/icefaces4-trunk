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

package org.icefaces.samples.showcase.example.ace.dataTable;

import java.util.*;

import org.icefaces.ace.model.filter.ContainsFilterConstraint;
import org.icefaces.ace.model.table.SortCriteria;
import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;

import org.icefaces.ace.model.table.LazyDataModel;
import org.icefaces.samples.showcase.dataGenerators.VehicleGenerator;
import org.icefaces.samples.showcase.example.ace.dataTable.Car;

@ComponentExample(
        parent = DataTableBean.BEAN_NAME,
        title = "example.ace.dataTable.lazyLoading.title",
        description = "example.ace.dataTable.lazyLoading.description",
        example = "/resources/examples/ace/dataTable/dataTableLazyLoading.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="dataTableLazyLoading.xhtml",
                    resource = "/resources/examples/ace/dataTable/dataTableLazyLoading.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="DataTableLazyLoading.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                    "/example/ace/dataTable/DataTableLazyLoading.java")
        }
)
@ManagedBean(name= DataTableLazyLoading.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class DataTableLazyLoading extends ComponentExampleImpl<DataTableLazyLoading> implements Serializable 
{
    public static final String BEAN_NAME = "dataTableLazyLoading";

    private static final ContainsFilterConstraint CONTAINS = new ContainsFilterConstraint();
    private static final int COUNT = 300000;
    private List<Car> randomCars = new VehicleGenerator().getRandomCars(COUNT);
    private LazyDataModel<Car> carsData;

    public DataTableLazyLoading() {
        super(DataTableLazyLoading.class);
        carsData = new LazyDataModel<Car>() {
            @Override
            public List<Car> load(int first, int pageSize,
                    final SortCriteria[] criteria, final Map<String, String> filters)  {
                List<Car> list = new ArrayList<Car>(randomCars);
                if (filters != null) {
                    for (int i = list.size() - 1; i >= 0; i--) {
                        Car c = list.get(i);
                        if (shouldFilterRemove(c.getAcceleration(), filters.get("acceleration")) ||
                            shouldFilterRemove(c.getChassis(), filters.get("chassis")) ||
                            shouldFilterRemove(c.getColor(), filters.get("color")) ||
                            shouldFilterRemove(c.getCost(), filters.get("cost")) ||
                            shouldFilterRemove(c.getId(), filters.get("id")) ||
                            shouldFilterRemove(c.getMpg(), filters.get("mpg")) ||
                            shouldFilterRemove(c.getName(), filters.get("name")) ||
                            shouldFilterRemove(c.getWeight(), filters.get("weight")) ||
                            shouldFilterRemove(c.getYear(), filters.get("year"))) {
                            list.remove(i);
                        }
                    }
                }
                if (criteria != null && criteria.length > 0) {
                    Comparator<Car> comparator = new Comparator<Car>() {
                        public int compare(Car c1, Car c2) {
                            for (SortCriteria sc : criteria) {
                                int result = compareSortCriteria(sc, c1, c2);
                                if (result != 0) return result;
                            }
                            return 0;
                        }

                        int compareSortCriteria(SortCriteria sc, Car c1, Car c2) {
                            int result = 0;
                            if (sc.getPropertyName().equals("acceleration"))
                                result = compareValues(c1.getAcceleration(), c2.getAcceleration());
                            else if (sc.getPropertyName().equals("chassis"))
                                result = compareValues(c1.getChassis(), c2.getChassis());
                            else if (sc.getPropertyName().equals("color"))
                                result = compareValues(c1.getColor(), c2.getColor());
                            else if (sc.getPropertyName().equals("cost"))
                                result = compareValues(c1.getCost(), c2.getCost());
                            else if (sc.getPropertyName().equals("id"))
                                result = compareValues(c1.getId(), c2.getId());
                            else if (sc.getPropertyName().equals("mpg"))
                                result = compareValues(c1.getMpg(), c2.getMpg());
                            else if (sc.getPropertyName().equals("name"))
                                result = compareValues(c1.getName(), c2.getName());
                            else if (sc.getPropertyName().equals("weight"))
                                result = compareValues(c1.getWeight(), c2.getWeight());
                            else if (sc.getPropertyName().equals("year"))
                                result = compareValues(c1.getYear(), c2.getYear());
                            return sc.isAscending() ? result : -1 * result;
                        }

                        int compareValues(Object value1, Object value2) {
                            int result;
                            if (value1 == null) {
                                if (value2 == null) result = 0;
                                else result = 1;
                            }
                            else if (value2 == null)
                                result = -1;
                            else
                                result = ((Comparable) value1).compareTo(value2);
			                return result;
                        }
                    };
                    Collections.sort(list, comparator);
                }
                if (first >= list.size()) {
                    return Collections.emptyList();
                }
                return list.subList(first, Math.min(first+pageSize, list.size()-1));
            }
        };
        
        carsData.setRowCount(COUNT);
    }

    private static boolean shouldFilterRemove(Object value, String filterValue) {
        if (filterValue != null && filterValue.length() > 0) {
            return value == null || !CONTAINS.applies(value.toString(), filterValue);
        }
        return false;
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    public LazyDataModel<Car> getCarsData() { return carsData; }
    public void setCarsData(LazyDataModel<Car> carsData) { this.carsData = carsData; }
}