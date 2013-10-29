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

package org.icefaces.samples.showcase.example.ace.contextMenu;

import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;

@ComponentExample(
        title = "example.ace.contextMenu.title",
        description = "example.ace.contextMenu.description",
        example = "/resources/examples/ace/contextMenu/contextMenu.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="contextMenu.xhtml",
                    resource = "/resources/examples/ace/contextMenu/contextMenu.xhtml")
        }
)
@Menu(
	title = "menu.ace.contextMenu.subMenu.title",
	menuLinks = {
	        @MenuLink(title = "menu.ace.contextMenu.subMenu.main",
	                isDefault = true,
                    exampleBeanName = ContextMenuBean.BEAN_NAME),
	        @MenuLink(title = "menu.ace.contextMenu.subMenu.component",
                    exampleBeanName = ContextMenuComponent.BEAN_NAME),
	        @MenuLink(title = "menu.ace.contextMenu.subMenu.table",
                    exampleBeanName = ContextMenuTable.BEAN_NAME),
	        @MenuLink(title = "menu.ace.contextMenu.subMenu.effect",
                    exampleBeanName = ContextMenuEffect.BEAN_NAME),
	        @MenuLink(title = "menu.ace.contextMenu.subMenu.multicolumn",
                    exampleBeanName = ContextMenuMultiColumn.BEAN_NAME),
	        @MenuLink(title = "menu.ace.contextMenu.subMenu.delegate",
                    exampleBeanName = ContextMenuDelegate.BEAN_NAME)
    }
)
@ManagedBean(name= ContextMenuBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class ContextMenuBean extends ComponentExampleImpl<ContextMenuBean> implements Serializable {
    public static final String BEAN_NAME = "contextMenuBean";

    public ContextMenuBean() {
        super(ContextMenuBean.class);
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }
}