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

package org.icefaces.samples.showcase.example.mobi.outputlist;


import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@ManagedBean(name = ListBean.BEAN_NAME)
@SessionScoped
public class ListBean /*extends ExampleImpl<ListBean>*/ implements
        Serializable {

    public static final String BEAN_NAME = "mobiListBean";
    private List<Car> cars = new ArrayList<Car>();

    public ListBean() {
        //super(ListBean.class);
        cars.add(new Car("Porsche 924", 45000));
        cars.add(new Car("Audi A8", 90000));
        cars.add(new Car("BMW M3", 500000));
        cars.add(new Car("Bugatti Veyron", 2000000));
    }
    
    public List<Car> getCars() {
        return cars;
    }

    public class Car implements Serializable{
        private String title;
        private int cost;

        Car(String title, int cost) {
            this.title = title;
            this.cost = cost;
        }

        public String getTitle() {
            return title;
        }

        public int getCost() {
            return cost;
        }
    }

}