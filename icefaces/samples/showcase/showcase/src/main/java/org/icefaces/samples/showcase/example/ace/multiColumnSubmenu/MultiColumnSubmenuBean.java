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

package org.icefaces.samples.showcase.example.ace.multiColumnSubmenu;

import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;

@ComponentExample(
        title = "example.ace.multiColumnSubmenu.title",
        description = "example.ace.multiColumnSubmenu.description",
        example = "/resources/examples/ace/multiColumnSubmenu/multiColumnSubmenu.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="multiColumnSubmenu.xhtml",
                    resource = "/resources/examples/ace/multiColumnSubmenu/multiColumnSubmenu.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="MultiColumnSubmenuBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                    "/example/ace/multiColumnSubmenu/MultiColumnSubmenuBean.java")
        }
)
@Menu(
    title = "menu.ace.multiColumnSubmenu.subMenu.title", 
    menuLinks = {
        @MenuLink(title = "menu.ace.multiColumnSubmenu.subMenu.main", isDefault = true, exampleBeanName = MultiColumnSubmenuBean.BEAN_NAME)
    }
)

@ManagedBean(name = MultiColumnSubmenuBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class MultiColumnSubmenuBean extends ComponentExampleImpl<MultiColumnSubmenuBean> implements Serializable {
    public static final String BEAN_NAME = "multiColumnSubmenuBean";
	public String getBeanName() { return BEAN_NAME; }
    
    public MultiColumnSubmenuBean() {
        super(MultiColumnSubmenuBean.class);
    }
    
    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }
}
