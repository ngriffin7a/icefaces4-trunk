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
import org.icefaces.samples.showcase.util.PositionBean;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ValueChangeEvent;
import java.io.Serializable;

import java.util.*;

@ComponentExample(
        parent = CheckboxButtonsBean.BEAN_NAME,
        title = "example.ace.checkboxButtons.label.title",
        description = "example.ace.checkboxButtons.label.description",
        example = "/resources/examples/ace/checkboxButtons/checkboxButtonsLabel.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="checkboxButtonsLabel.xhtml",
                    resource = "/resources/examples/ace/checkboxButtons/checkboxButtonsLabel.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="CheckboxButtonsLabelBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                    "/example/ace/checkboxButtons/CheckboxButtonsLabelBean.java"),
            @ExampleResource(type = ResourceType.java,
                    title="CheckboxButtonsBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                    "/example/ace/checkboxButtons/CheckboxButtonsBean.java")
        }
)
@ManagedBean(name= CheckboxButtonsLabelBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class CheckboxButtonsLabelBean extends ComponentExampleImpl<CheckboxButtonsLabelBean> implements Serializable
{
    public static final String BEAN_NAME = "checkboxButtonsLabelBean";
	public String getBeanName() { return BEAN_NAME; }

    private String labelPosition = "left";

    public CheckboxButtonsLabelBean() 
    {
        super(CheckboxButtonsLabelBean.class);
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

	private List<String> selected = new ArrayList<String>();
	public List<String> getSelected() { return selected; }
	public void setSelected(List<String> selected) { this.selected = selected; }
    
    public String getLabelPosition() {
        return labelPosition;
    }
    
    public void setLabelPosition(String labelPosition) {
        this.labelPosition = labelPosition;
    }
}
