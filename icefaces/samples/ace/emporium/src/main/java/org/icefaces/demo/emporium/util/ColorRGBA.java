/*
 * Copyright 2004-2015 ICEsoft Technologies Canada Corp.
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

package org.icefaces.demo.emporium.util;


public class ColorRGBA {
	public static final double DEFAULT_OPACITY = 0.9;
	
	private String name;
	private String rgba;
	
	public ColorRGBA(String name, int red, int green, int blue) {
		this.name = name;
		this.rgba = "rgba(" + red + ", " + green + ", " + blue + ", " + DEFAULT_OPACITY + ")";
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRgba() {
		return rgba;
	}
	public void setRgba(String rgba) {
		this.rgba = rgba;
	}
}
