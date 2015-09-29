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

package org.icefaces.samples.showcase.example.ace.radioButton;

import java.io.Serializable;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;

@ManagedBean(name= RadioButtonBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class RadioButtonBean implements Serializable {

    public static final String BEAN_NAME = "radioButton";
	public String getBeanName() { return BEAN_NAME; }
    private boolean selected1 = true;
	private boolean selected2 = false;

    public boolean isSelected1() {
        return selected1;
    }

    public void setSelected1(boolean selected) {
        this.selected1 = selected;
    }
	
    public boolean isSelected2() {
        return selected2;
    }

    public void setSelected2(boolean selected) {
        this.selected2 = selected;
    }
    
    public String getBoxValueDescription1() {
        if(selected1)
            return "Selected";
        else
            return "Not Selected";
    }
	
    public String getBoxValueDescription2() {
        if(selected2)
            return "Selected";
        else
            return "Not Selected";
    }
}
