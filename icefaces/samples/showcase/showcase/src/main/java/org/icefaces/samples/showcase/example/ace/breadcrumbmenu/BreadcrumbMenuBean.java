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

package org.icefaces.samples.showcase.example.ace.breadcrumbmenu;

import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;
import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;

@ComponentExample(
        title = "example.ace.breadcrumbmenu.title",
        description = "example.ace.breadcrumbmenu.description",
        example = "/resources/examples/ace/breadcrumbmenu/breadcrumbMenu.xhtml"
)
@ExampleResources(
        resources = {
                // xhtml
                @ExampleResource(type = ResourceType.xhtml,
                        title = "breadcrumbMenu.xhtml",
                        resource = "/resources/examples/ace/breadcrumbmenu/breadcrumbMenu.xhtml"),
                // Java Source
                @ExampleResource(type = ResourceType.java,
                        title = "BreadcrumbMenuBean.java",
                        resource = "/WEB-INF/classes/org/icefaces/samples/showcase" +
                                "/example/ace/breadcrumbmenu/BreadcrumbMenuBean.java"),
                @ExampleResource(type = ResourceType.java,
                        title = "BreadcrumbMenuViewScopedBean.java",
                        resource = "/WEB-INF/classes/org/icefaces/samples/showcase" +
                                "/example/ace/breadcrumbmenu/BreadcrumbMenuViewScopedBean.java")
        }
)
@Menu(
        title = "menu.ace.breadcrumbmenu.submenu.title",
        menuLinks = {
                @MenuLink(title = "menu.ace.breadcrumbmenu.submenu.main", isDefault = true, exampleBeanName = BreadcrumbMenuBean.BEAN_NAME)
        }
)
@ManagedBean(name = BreadcrumbMenuBean.BEAN_NAME)
@CustomScoped(value="#{window}")
public class BreadcrumbMenuBean extends ComponentExampleImpl<BreadcrumbMenuBean> implements Serializable {
    public static final String BEAN_NAME = "breadcrumbMenuBean";

    private static final long serialVersionUID = -6406396460390676389L;

    public BreadcrumbMenuBean() {
        super(BreadcrumbMenuBean.class);
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }
	
	// The demo logic itself is in BreadcrumbMenuViewScopedBean.java
}
