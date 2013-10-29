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

package org.icefaces.samples.showcase.example.compat.positioned;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import javax.faces.bean.ViewScoped;
import javax.faces.bean.ManagedBean;

@ManagedBean(name= PositionedData.BEAN_NAME)
@ViewScoped
public class PositionedData implements Serializable {
	public static final String BEAN_NAME = "positionedData";
	
	public static List<String> FOODS = generateFoods();
	
	public List<String> getAvailableFoods() { return FOODS; }
	
	private static List<String> generateFoods() {
        List<String> toReturn = new ArrayList<String>(6);
        
        toReturn.add("Cake");
        toReturn.add("Popcorn");
	    toReturn.add("Steak");
	    toReturn.add("Stirfry");
	    toReturn.add("Rice");
	    toReturn.add("Chocolate");
	    
	    return toReturn;
	}
	
	public static boolean empty() {
	    return ((FOODS == null) || (FOODS.size() == 0));
	}
	
	public static void defaultFoods() {
	    FOODS = generateFoods();
	}
	
	public static String getFood(int index) {
	    return FOODS.get(index);
	}
	
	public static void addFood(String food) {
	    FOODS.add(food);
	}
	
	public static String removeFood(int index) {
        return FOODS.remove(index);
	}
	
	public static String getTopFood() {
	    return getFood(0);
	}
	
	public static String getBottomFood() {
	    return getFood(FOODS.size()-1);
	}
	
	public static void sortAscending() {
	    Collections.sort(FOODS);
	}
	
	public static void sortDescending() {
	    Collections.sort(FOODS);
	    Collections.reverse(FOODS);
	}
	
	public static void sortRandom() {
	    Collections.shuffle(FOODS);
	}
}
