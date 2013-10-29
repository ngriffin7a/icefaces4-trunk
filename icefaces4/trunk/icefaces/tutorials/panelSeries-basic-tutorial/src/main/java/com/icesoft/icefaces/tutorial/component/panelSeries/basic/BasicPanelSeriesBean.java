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

package com.icesoft.icefaces.tutorial.component.panelSeries.basic;


import java.io.Serializable;

/**
 * <p>
 * A basic backing bean for a ice:panelSeries component.  The only instance variable
 * needed is an array of Strings which is bound to the icefaces tree
 * component in the jspx code.</p>
 */
public class BasicPanelSeriesBean implements Serializable {

    private static final long serialVersionUID = 5541832815571111343L;
    private String[] colorList = new String[]{
		"Black",
		"White",
		"Yellow",
		"Green",
		"Red"
	};


	public String[] getColorList(){
		return this.colorList;
	}

	public void setColorList(String[] colorList){
		this.colorList = colorList;
	}

}