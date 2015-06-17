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
package org.icefaces.samples.showcase.example.ace.panelStack;

import java.io.Serializable;
import java.lang.String;

public class InputDefinition implements Serializable{
	private static final long serialVersionUID = 1L;
	private String type;
    private String title;
    private Object value;
    private boolean booleanVal = false;

    public boolean isFacelet() {
        return facelet;
    }

    public void setFacelet(boolean facelet) {
        this.facelet = facelet;
    }

    private boolean facelet = true;

    public InputDefinition(String type, String title,  Object value){
    	this.type = type;
        this.title = title;
    	this.value = value;
    }

    public InputDefinition(String type, String title, boolean bool){
        this.type = type;
        this.title = title;
        this.booleanVal = bool;
    }

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

    public boolean isBooleanVal() {
        return booleanVal;
    }

    public void setBooleanVal(boolean booleanVal) {
        this.booleanVal = booleanVal;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
