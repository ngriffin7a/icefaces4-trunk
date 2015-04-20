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

package org.icefaces.samples.showcase.example.ace.panel;

import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;

import java.io.Serializable;

@ComponentExample(
        title = "example.ace.panel.title",
        description = "example.ace.panel.description",
        example = "/resources/examples/ace/panel/panel.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="panel.xhtml",
                    resource = "/resources/examples/ace/panel/panel.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="PanelBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                    "/example/ace/panel/PanelBean.java")
        }
)
@Menu(
	title = "menu.ace.panel.subMenu.title",
	menuLinks = {
	        @MenuLink(title = "menu.ace.panel.subMenu.main",
	                isDefault = true,
                    exampleBeanName = PanelBean.BEAN_NAME),
            @MenuLink(title = "menu.ace.panel.subMenu.header",
                exampleBeanName = PanelHeader.BEAN_NAME),
            @MenuLink(title = "menu.ace.panel.subMenu.toggle",
                exampleBeanName = PanelToggle.BEAN_NAME),
            @MenuLink(title = "menu.ace.panel.subMenu.close",
                exampleBeanName = PanelClose.BEAN_NAME),
            @MenuLink(title = "menu.ace.panel.subMenu.listener",
                exampleBeanName = PanelListener.BEAN_NAME),
            @MenuLink(title = "menu.ace.panel.subMenu.menu",
                exampleBeanName = PanelMenu.BEAN_NAME)
    }
)
@ManagedBean(name= PanelBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class PanelBean extends ComponentExampleImpl<PanelBean> implements Serializable {
    public static final String BEAN_NAME = "panelBean";
	public String getBeanName() { return BEAN_NAME; }
    private boolean reOpenButton = false;
    
    private boolean collapsed = false;

    public PanelBean() {
        super(PanelBean.class);
    }
    
    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    public boolean getCollapsed() { return collapsed; }
    public void setCollapsed(boolean collapsed) { this.collapsed = collapsed; }

    /**
     * use the close event of panels ace:ajax tag to make a show button visible
     * @param event
     */
    public void setButtonVisible(javax.faces.event.AjaxBehaviorEvent event){
       this.reOpenButton=true;
    }

    /**
     * use ace:ajax on the pushButton to reset it to not be rendered
     * @param event
     */
    public void buttonNotVisible(ActionEvent event){
        this.reOpenButton = false;
    }

    public boolean isReOpenButton() {
        return reOpenButton;
    }

    public void setReOpenButton(boolean reOpenButton) {
        this.reOpenButton = reOpenButton;
    }


}