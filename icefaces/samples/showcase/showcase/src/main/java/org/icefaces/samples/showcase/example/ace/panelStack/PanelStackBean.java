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

import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ValueChangeEvent;
import java.io.Serializable;
import java.lang.String;
import java.lang.System;
import java.util.ArrayList;
import java.util.List;


@ComponentExample(
        title = "example.ace.panelStack.title",
        description = "example.ace.panelStack.description",
        example = "/resources/examples/ace/panelStack/panelStack.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="panelStack.xhtml",
                    resource = "/resources/examples/ace/panelStack/panelStack.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="PanelStackBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                    "/example/ace/panelStack/PanelStackBean.java")
        }
)
@Menu(
	title = "menu.ace.panelStack.subMenu.title",
	menuLinks = {
	        @MenuLink(title = "menu.ace.panelStack.subMenu.main",
	                isDefault = true,
                    exampleBeanName = PanelStackBean.BEAN_NAME),
            @MenuLink(title = "menu.ace.panelStack.subMenu.dataTable",
                    exampleBeanName = PanelStackDataTable.BEAN_NAME)
    }
)
@ManagedBean(name= PanelStackBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class PanelStackBean extends ComponentExampleImpl<PanelStackBean> implements  Serializable {
    public static final String BEAN_NAME = "panelStackBean";
    
    private String currentId="TEXTONLY1"; // the id of the currently selected stackPane
    private boolean facelet = false;
    private String text1;
    private String text2;
    private String comboBox;
    private boolean chkbox1;

    public boolean isChkbox1() {
        return chkbox1;
    }

    public void setChkbox1(boolean chkbox1) {
        this.chkbox1 = chkbox1;
    }

    public String getComboBox() {
        return comboBox;
    }

    public void setComboBox(String comboBox) {
        this.comboBox = comboBox;
    }

    public String getText1() {
        return text1;
    }

    public void setText1(String text1) {
        this.text1 = text1;
    }

    public String getText2() {
        return text2;
    }

    public void setText2(String text2) {
        this.text2 = text2;
    }

    public PanelStackBean() {
        super(PanelStackBean.class);
    }
    
    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    public String getCurrentId() {
        return currentId;
    }

    public void setCurrentId(String currentId) {
        this.currentId = currentId;
    }

    public boolean isFacelet() {
        return facelet;
    }

    public void setFacelet(boolean facelet) {
        this.facelet = facelet;
    }

}