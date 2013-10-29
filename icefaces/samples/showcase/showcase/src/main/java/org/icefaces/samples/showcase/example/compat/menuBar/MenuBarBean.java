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

package org.icefaces.samples.showcase.example.compat.menuBar;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;

import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.Menu;
import org.icefaces.samples.showcase.metadata.annotation.MenuLink;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

@ComponentExample(
        title = "example.compat.menuBar.title",
        description = "example.compat.menuBar.description",
        example = "/resources/examples/compat/menuBar/menuBar.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="menuBar.xhtml",
                    resource = "/resources/examples/compat/"+
                               "menuBar/menuBar.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="MenuBarBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/menuBar/MenuBarBean.java")
        }
)
@Menu(
	title = "menu.compat.menuBar.subMenu.title",
	menuLinks = {
            @MenuLink(title = "menu.compat.menuBar.subMenu.main",
                    isDefault = true,
                    exampleBeanName = MenuBarBean.BEAN_NAME),
            @MenuLink(title = "menu.compat.menuBar.subMenu.events",
                    exampleBeanName = MenuBarEvents.BEAN_NAME),
            @MenuLink(title = "menu.compat.menuBar.subMenu.orientation",
                    exampleBeanName = MenuBarOrientation.BEAN_NAME),
            @MenuLink(title = "menu.compat.menuBar.subMenu.click",
                    exampleBeanName = MenuBarClick.BEAN_NAME),
            @MenuLink(title = "menu.compat.menuBar.subMenu.icons",
                    exampleBeanName = MenuBarIcons.BEAN_NAME),
            @MenuLink(title = "menu.compat.menuBar.subMenu.separator",
                    exampleBeanName = MenuBarSeparator.BEAN_NAME),
            @MenuLink(title = "menu.compat.menuBar.subMenu.dynamic",
                    exampleBeanName = MenuBarDynamic.BEAN_NAME)
})
@ManagedBean(name= MenuBarBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class MenuBarBean extends ComponentExampleImpl<MenuBarBean> implements Serializable {
	
	public static final String BEAN_NAME = "menuBar";
	
	public MenuBarBean() {
		super(MenuBarBean.class);
	}

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }
}
