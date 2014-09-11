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

package org.icefaces.samples.showcase.example.ace.tableConfigPanel;

import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;

@ComponentExample(
        title = "example.ace.tableConfigPanel.title",
        description = "example.ace.tableConfigPanel.description",
        example = "/resources/examples/ace/tableConfigPanel/tableConfigPanel.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="tableConfigPanel.xhtml",
                    resource = "/resources/examples/ace/tableConfigPanel/tableConfigPanel.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="TableConfigPanelBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                    "/example/ace/tableConfigPanel/TableConfigPanelBean.java")
        }
)
@Menu(
	title = "menu.ace.tableConfigPanel.subMenu.main",
	menuLinks = {
	        @MenuLink(title = "menu.ace.tableConfigPanel.subMenu.main",
	                isDefault = true,
                    exampleBeanName = TableConfigPanelBean.BEAN_NAME),
	        @MenuLink(title = "menu.ace.tableConfigPanel.subMenu.advanced",
                    exampleBeanName = TableConfigPanelAdvancedBean.BEAN_NAME)
    }
)
@ManagedBean(name= TableConfigPanelBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class TableConfigPanelBean extends ComponentExampleImpl<TableConfigPanelBean> implements Serializable 
{
    public static final String BEAN_NAME = "tableConfigPanelBean";
    
    public TableConfigPanelBean() {
        super(TableConfigPanelBean.class);
		setGroup(1);
    }
    
    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    private String firstName;
    private String lastName;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
}
