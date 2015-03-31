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

package org.icefaces.demo.auction.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

@ManagedBean(name=ListData.BEAN_NAME)
@ApplicationScoped
public class ListData implements Serializable {
	public static final String BEAN_NAME = "listData";
	
	public static final String DEFAULT_TAB_ORIENTATION = "top";
	public static final String[] TAB_ORIENTATIONS = new String[] {
		"bottom", DEFAULT_TAB_ORIENTATION, "left", "right"
	};
	public static final List<ColorRGBA> COLORS = generateColors();
	
	private static List<ColorRGBA> generateColors() {
		List<ColorRGBA> toReturn = new ArrayList<ColorRGBA>();
		toReturn.add(new ColorRGBA("White", 255,255,255));
		toReturn.add(new ColorRGBA("Black", 0,0,0));
		toReturn.add(new ColorRGBA("Red", 255,0,0));
		toReturn.add(new ColorRGBA("Green", 0,255,0));
		toReturn.add(new ColorRGBA("Blue", 0,0,255));
		toReturn.add(new ColorRGBA("Yellow", 255,255,0));
		toReturn.add(new ColorRGBA("Purple", 128,0,128));
		toReturn.add(new ColorRGBA("Brown", 165,42,42));
		toReturn.add(new ColorRGBA("Cyan", 0,139,139));
		toReturn.add(new ColorRGBA("Orange", 255,140,0));
		toReturn.add(new ColorRGBA("Salmon", 233,150,122));
		toReturn.add(new ColorRGBA("Sky Blue", 0,191,255));
		toReturn.add(new ColorRGBA("Forest Green", 34,139,34));
		toReturn.add(new ColorRGBA("Gold", 255,215,0));
		toReturn.add(new ColorRGBA("Steel Blue", 176,196,222));
		toReturn.add(new ColorRGBA("Violet Red", 219,112,147));
		toReturn.add(new ColorRGBA("Seashell", 255,245,238));
		return toReturn;
	}
	
	public String[] getTabOrientations() {
		return TAB_ORIENTATIONS;
	}
	
	public List<ColorRGBA> getColors() {
		return COLORS;
	}
}
