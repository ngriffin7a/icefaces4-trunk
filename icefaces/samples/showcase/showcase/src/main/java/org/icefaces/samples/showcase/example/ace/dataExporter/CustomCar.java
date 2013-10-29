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

package org.icefaces.samples.showcase.example.ace.dataExporter;

import java.io.Serializable;
import java.util.List;

public class CustomCar implements Serializable {
	protected int id = -1;
	protected String name;
	protected String chassis;
	protected int weight;
	protected int acceleration;
	protected double mpg;
	protected double cost;
	protected List<ExpansionData> expansionData1;
	protected List<ExpansionData> expansionData2;
	
	public CustomCar() {
	}
        
	public CustomCar(int id,
	           String name, String chassis,
	           int weight, int acceleration, 
	           double mpg, double cost, 
			   List<ExpansionData> expansionData1,
			   List<ExpansionData> expansionData2) {
		this.id = id;
		this.name = name;
		this.chassis = chassis;
		this.weight = weight;
		this.acceleration = acceleration;
		this.mpg = mpg;
		this.cost = cost;
		this.expansionData1 = expansionData1;
		this.expansionData2 = expansionData2;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getChassis() {
		return chassis;
	}
	public void setChassis(String chassis) {
		this.chassis = chassis;
	}
	public int getWeight() {
		return weight;
	}
	public void setWeight(int weight) {
		this.weight = weight;
	}
	public int getAcceleration() {
		return acceleration;
	}
	public void setAcceleration(int acceleration) {
		this.acceleration = acceleration;
	}
	public double getMpg() {
		return mpg;
	}
	public void setMpg(double mpg) {
		this.mpg = mpg;
	}
	public double getCost() {
	    return cost;
	}
	public void setCost(double cost) {
	    this.cost = cost;
	}
	public List<ExpansionData> getExpansionData1() {
	    return expansionData1;
	}
	public void setExpansionData1(List<ExpansionData> expansionData1) {
	    this.expansionData1 = expansionData1;
	}
	public List<ExpansionData> getExpansionData2() {
	    return expansionData2;
	}
	public void setExpansionData2(List<ExpansionData> expansionData2) {
	    this.expansionData2 = expansionData2;
	}
	
	public void applyValues(CustomCar parent) {
	    setId(parent.getId());
	    setName(parent.getName());
	    setChassis(parent.getChassis());
	    setWeight(parent.getWeight());
	    setAcceleration(parent.getAcceleration());
	    setMpg(parent.getMpg());
	    setCost(parent.getCost());
	}
	
	public String toString() {
	    return getName();
	}

    // Fix symptoms of session serialization issue when using TreeDataModel
    // by giving hashCode implementation to row objects.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CustomCar car = (CustomCar) o;

        if (id != car.id) return false;
        if (!name.equals(car.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + name.hashCode();
        return result;
    }
}
