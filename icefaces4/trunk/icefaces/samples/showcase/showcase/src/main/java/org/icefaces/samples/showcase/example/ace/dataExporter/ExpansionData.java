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

public class ExpansionData implements Serializable {

	protected double number1;
	protected double number2;
	protected double number3;
	
	public ExpansionData() {
		number1 = Math.random();
		number2 = Math.random();
		number3 = Math.random();
	}
	
	public double getNumber1() {
		return number1;
	}
	
	public void setNumber1(double number1) {
		this.number1 = number1;
	}
	
	public double getNumber2() {
		return number2;
	}
	
	public void setNumber2(double number2) {
		this.number2 = number2;
	}
	
	public double getNumber3() {
		return number3;
	}
	
	public void setNumber3(double number3) {
		this.number3 = number3;
	}
}
