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

package org.icefaces.samples.showcase.dataGenerators.utilityClasses;

import org.icefaces.samples.showcase.dataGenerators.VehicleGenerator;
import org.icefaces.samples.showcase.example.compat.dataTable.Car;
import org.icefaces.samples.showcase.example.compat.selector.SelectableCar;

import java.io.Serializable;
import java.util.List;

public class DataTableData implements Serializable {
    
        public static final String BEAN_NAME = "tableData";

        public static final int DEFAULT_ROWS;
        public static final int DEFAULT_LIST_SIZE;
        public static final String[] CHASSIS_ALL;
        //static data initialization
        static {
            DEFAULT_ROWS = 8;
            DEFAULT_LIST_SIZE = 30;
            CHASSIS_ALL = new String[] {"Motorcycle", "Subcompact", "Mid-Size", "Luxury",
                                                        "Station Wagon", "Pickup", "Van", "Bus", "Semi-Truck"};
        }

        public DataTableData() {   }

        public static List<Car> getDefaultData() {
            VehicleGenerator generator = new VehicleGenerator();
            return generator.getRandomCars(DEFAULT_LIST_SIZE);
        }
        public static List<SelectableCar> getDefaultSelectableData() {
            VehicleGenerator generator = new VehicleGenerator();
            return generator.getRandomSelectableCars(DEFAULT_LIST_SIZE); 
        }
        public String[] getChassisAll() { return CHASSIS_ALL; }
}
