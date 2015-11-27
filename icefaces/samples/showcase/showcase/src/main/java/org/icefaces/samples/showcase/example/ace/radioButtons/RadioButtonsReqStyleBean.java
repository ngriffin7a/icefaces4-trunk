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

package org.icefaces.samples.showcase.example.ace.radioButtons;

import java.io.Serializable;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;

@ManagedBean(name= RadioButtonsReqStyleBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class RadioButtonsReqStyleBean implements Serializable
{
    public static final String BEAN_NAME = "radioButtonsReqStyleBean";
	public String getBeanName() { return BEAN_NAME; }
    
	private String selected1 = "";
	public String getSelected1() { return selected1; }
	public void setSelected1(String selected1) { this.selected1 = selected1; }

	private String selected2 = "";
	public String getSelected2() { return selected2; }
	public void setSelected2(String selected2) { this.selected2 = selected2; }

	public String getSelected1String() {
		String result = "";
		if (selected1 != null) {
			result += selected1.toString();
			if ("".equals(result)) {
				result = "None";
			}
		} else {
			result = "None";
		}
		return result;
	}

	public String getSelected2String() {
		String result = "";
		if (selected2 != null) {
			result += selected2.toString();
			result = result.replace("[", "");
			result = result.replace("]", "");
			if ("".equals(result)) {
				result = "None";
			}
		} else {
			result = "None";
		}
		return result;
	}

    private String reqColor = "redRS";
    private String optColor = "greenRS";
   
    public String getReqColor() {
        return reqColor;
    }
    
    public String getOptColor() {
        return optColor;
    }
        
    public void setReqColor(String reqColor) {
        this.reqColor = reqColor;
    }
    
    public void setOptColor(String optColor) {
        this.optColor = optColor;
    }

	private boolean useTheme = false;

    public boolean getUseTheme() {
        return useTheme;
    }

    public void setUseTheme(boolean useTheme) {
        this.useTheme = useTheme;
    }

	public void clearValues() {
		selected1 = "";
		selected2 = "";
	}
}
