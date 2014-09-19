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

package org.icefaces.samples.showcase.example.core.idleMonitor;

import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.annotation.Menu;
import org.icefaces.samples.showcase.metadata.annotation.MenuLink;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import java.io.Serializable;

@ComponentExample(
        title = "Idle Monitor Component",
        description = "The <b>icecore:idleMonitor</b> renders the enclosed markup when the user has not interacted with the page for a defined period of time. The rendered markup is hidden when user resumes the activity. In this example you need to stop using the page for 10 seconds in order to see the warning.",
        example = "/resources/examples/core/idle-monitor.xhtml"
)

@ExampleResources(
        resources = {
                @ExampleResource(type = ResourceType.xhtml,
                        title="idle-monitor.xhtml",
                        resource = "/resources/examples/core/idle-monitor.xhtml"),
                @ExampleResource(type = ResourceType.java,
                        title="IdleMonitorBean.java",
                        resource = "/WEB-INF/classes/org/icefaces/samples/showcase/example/core/IdleMonitorBean.java")
        }
)
@Menu(
    title = "menu.core.idleMonitorBean.subMenu.title", 
    menuLinks = {
        @MenuLink(title = "menu.core.idleMonitorBean.subMenu.main", isDefault = true, exampleBeanName = IdleMonitorBean.BEAN_NAME)
    }
)
@ManagedBean(name = IdleMonitorBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class IdleMonitorBean extends ComponentExampleImpl<IdleMonitorBean> implements Serializable {
    public static final String BEAN_NAME = "idleMonitorBean";
    private String actionDescription;

    public IdleMonitorBean() {
        super(IdleMonitorBean.class);
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }
}
