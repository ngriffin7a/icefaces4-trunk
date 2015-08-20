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

package org.icefaces.samples.showcase.example.ace.checkboxButtons;

import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;
import java.util.*;

@ComponentExample(
        parent = CheckboxButtonsBean.BEAN_NAME,
        title = "example.ace.checkboxButtons.reqStyle.title",
        description = "example.ace.checkboxButtons.reqStyle.description",
        example = "/resources/examples/ace/checkboxButtons/checkboxButtonsReqStyle.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="checkboxButtonsReqStyle.xhtml",
                    resource = "/resources/examples/ace/checkboxButtons/checkboxButtonsReqStyle.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="CheckboxButtonsReqStyleBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                    "/example/ace/checkboxButtons/CheckboxButtonsReqStyleBean.java")
        }
)
@ManagedBean(name= CheckboxButtonsReqStyleBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class CheckboxButtonsReqStyleBean extends ComponentExampleImpl<CheckboxButtonsReqStyleBean> implements Serializable
{
    public static final String BEAN_NAME = "checkboxButtonsReqStyleBean";
	public String getBeanName() { return BEAN_NAME; }
    
    public CheckboxButtonsReqStyleBean() 
    {
        super(CheckboxButtonsReqStyleBean.class);
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

	private List<String> selected1 = new ArrayList<String>();
	public List<String> getSelected1() { return selected1; }
	public void setSelected1(List<String> selected1) { this.selected1 = selected1; }

	private List<String> selected2 = new ArrayList<String>();
	public List<String> getSelected2() { return selected2; }
	public void setSelected2(List<String> selected2) { this.selected2 = selected2; }

	public String getSelected1String() {
		String result = "";
		if (selected1 != null) {
			result += selected1.toString();
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
		selected1 = new ArrayList<String>();
		selected2 = new ArrayList<String>();
	}
}
