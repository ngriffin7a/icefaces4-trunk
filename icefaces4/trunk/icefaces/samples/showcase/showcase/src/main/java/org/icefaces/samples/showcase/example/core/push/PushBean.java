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

package org.icefaces.samples.showcase.example.core.push;

import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.annotation.Menu;
import org.icefaces.samples.showcase.metadata.annotation.MenuLink;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;
import org.icefaces.samples.showcase.util.FacesUtils;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.CustomScoped;
import java.io.Serializable;

@ComponentExample(
        title = "Push Component",
        description = "The <b>icecore:push</b> component allows the configuration of Ajax Push behavior on a per-view basis.",
        example = "/resources/examples/core/push.xhtml"
)

@ExampleResources(
        resources = {
                @ExampleResource(type = ResourceType.xhtml,
                        title="push.xhtml",
                        resource = "/resources/examples/core/push.xhtml"),
                @ExampleResource(type = ResourceType.java,
                        title="PushBean.java",
                        resource = "/WEB-INF/classes/org/icefaces/samples/showcase/example/core/push/PushBean.java"),
                @ExampleResource(type = ResourceType.java,
                        title="PushWindowScopeBean.java",
                        resource = "/WEB-INF/classes/org/icefaces/samples/showcase/example/core/push/PushWindowScopeBean.java")
        }
)
@Menu(
    title = "menu.core.pushBean.subMenu.title", 
    menuLinks = {
        @MenuLink(title = "menu.core.pushBean.subMenu.main", isDefault = true, exampleBeanName = PushBean.BEAN_NAME)
    }
)
@ManagedBean(name = PushBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class PushBean extends ComponentExampleImpl<PushBean> implements Serializable {
    public static final String BEAN_NAME = "pushBean";
	public String getBeanName() { return BEAN_NAME; }

    public PushBean() {
        super(PushBean.class);
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    public void selected() {
        PushWindowScopeBean managedBean = (PushWindowScopeBean) FacesUtils.getManagedBean(PushWindowScopeBean.BEAN_NAME);
        managedBean.selected();
    }

    public void deselected() {
        PushWindowScopeBean managedBean = (PushWindowScopeBean) FacesUtils.getManagedBean(PushWindowScopeBean.BEAN_NAME);
        managedBean.deselected();
    }
}
