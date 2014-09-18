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

package org.icefaces.samples.showcase.example.ace.buttonGroup;

import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;
import org.icefaces.samples.showcase.metadata.annotation.Menu;
import org.icefaces.samples.showcase.metadata.annotation.MenuLink;

@ComponentExample(
    title = "example.ace.buttonGroup.title",
    description = "example.ace.buttonGroup.description",
    example = "/resources/examples/ace/buttonGroup/buttongroup.xhtml"
)
@ExampleResources(
    resources ={
        // xhtml
        @ExampleResource(type = ResourceType.xhtml,
                         title="buttongroup.xhtml",
                         resource = "/resources/examples/ace/buttonGroup/buttongroup.xhtml"),
        // Java Source
        @ExampleResource(type = ResourceType.java,
                         title="ButtonGroupBean.java",
                         resource = "/WEB-INF/classes/org/icefaces/samples/showcase/example/ace/buttonGroup/ButtonGroupBean.java")
    }
)

@Menu(
    title = "menu.ace.buttonGroup.subMenu.title",
    menuLinks = {
        @MenuLink(title = "menu.ace.buttonGroup.subMenu.main", isDefault = true,
                  exampleBeanName = ButtonGroupBean.BEAN_NAME)
    }
)
@ManagedBean(name= ButtonGroupBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class ButtonGroupBean extends ComponentExampleImpl<ButtonGroupBean> implements Serializable {

    public static final String BEAN_NAME = "buttonGroup";
    private boolean cb1 = false;
	private boolean cb2 = false;
	private boolean cb3 = false;
	private boolean cb4 = false;
    private boolean r1 = false;
	private boolean r2 = false;
	private boolean r3 = false;
	private boolean r4 = false;
	private String checkboxDescription;
	private String radioDescription;

    public ButtonGroupBean() {
        super(ButtonGroupBean.class);
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    public boolean isCb1() {
        return cb1;
    }

    public void setCb1(boolean cb1) {
        this.cb1 = cb1;
    }
	
    public boolean isCb2() {
        return cb2;
    }

    public void setCb2(boolean cb2) {
        this.cb2 = cb2;
    }
	
    public boolean isCb3() {
        return cb3;
    }

    public void setCb3(boolean cb3) {
        this.cb3 = cb3;
    }
	
    public boolean isCb4() {
        return cb4;
    }

    public void setCb4(boolean cb4) {
        this.cb4 = cb4;
    }
	
    public boolean isR1() {
        return r1;
    }

    public void setR1(boolean r1) {
        this.r1 = r1;
    }
	
    public boolean isR2() {
        return r2;
    }

    public void setR2(boolean r2) {
        this.r2 = r2;
    }
	
    public boolean isR3() {
        return r3;
    }

    public void setR3(boolean r3) {
        this.r3 = r3;
    }
	
    public boolean isR4() {
        return r4;
    }

    public void setR4(boolean r4) {
        this.r4 = r4;
    }
    
    public String getCheckboxDescription() {
		if (cb1) checkboxDescription = "Checkbox 1";
		else if (cb2) checkboxDescription = "Checkbox 2";
		else if (cb3) checkboxDescription = "Checkbox 3";
		else if (cb4) checkboxDescription = "Checkbox 4";
		else checkboxDescription = "None";
		return checkboxDescription;
    }
	
    public String getRadioDescription() {
		if (r1) radioDescription = "Radio 1";
		else if (r2) radioDescription = "Radio 2";
		else if (r3) radioDescription = "Radio 3";
		else if (r4) radioDescription = "Radio 4";
		else radioDescription = "None";
		return radioDescription;
    }
}
