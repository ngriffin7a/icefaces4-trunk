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

package org.icefaces.samples.showcase.example.compat.selector;

import java.io.Serializable;
import org.icefaces.samples.showcase.example.compat.dataTable.Car;

public class SelectableCar extends Car implements Serializable{
    private boolean selected = false;
    
    public SelectableCar() {
        super();
    }
    
    public SelectableCar(int id,
               String name, String chassis,
               int weight, int acceleration, 
               double mpg, double cost) {
        super(id, name, chassis, weight, acceleration, mpg, cost);
    }
    
    public SelectableCar(Car base) {
        super(base.getId(),
              base.getName(),
              base.getChassis(),
              base.getWeight(),
              base.getAcceleration(),
              base.getMpg(),
              base.getCost());
    }
    
    public boolean isSelected() { return selected; }
    
    public void setSelected(boolean selected) { this.selected = selected; }
}
