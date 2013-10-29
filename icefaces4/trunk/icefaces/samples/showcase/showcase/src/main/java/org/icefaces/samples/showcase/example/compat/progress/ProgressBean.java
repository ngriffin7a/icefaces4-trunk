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

package org.icefaces.samples.showcase.example.compat.progress;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;

import org.icefaces.samples.showcase.util.FacesUtils;
import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.Menu;
import org.icefaces.samples.showcase.metadata.annotation.MenuLink;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

@ComponentExample(
        title = "example.compat.progress.title",
        description = "example.compat.progress.description",
        example = "/resources/examples/compat/progress/progress.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="progress.xhtml",
                    resource = "/resources/examples/compat/"+
                               "progress/progress.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="ProgressBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/progress/ProgressBean.java")
        }
)
@Menu(
	title = "menu.compat.progress.subMenu.title",
	menuLinks = {
            @MenuLink(title = "menu.compat.progress.subMenu.main",
                    isDefault = true,
                    exampleBeanName = ProgressBean.BEAN_NAME),
            @MenuLink(title = "menu.compat.progress.subMenu.indeterminate",
                    exampleBeanName = ProgressIndeterminate.BEAN_NAME),
            @MenuLink(title = "menu.compat.progress.subMenu.label",
                    exampleBeanName = ProgressLabel.BEAN_NAME),
            @MenuLink(title = "menu.compat.progress.subMenu.multiple",
                    exampleBeanName = ProgressMultiple.BEAN_NAME),
            @MenuLink(title = "menu.compat.progress.subMenu.style",
                    exampleBeanName = ProgressStyle.BEAN_NAME)
})
@ManagedBean(name= ProgressBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class ProgressBean extends ComponentExampleImpl<ProgressBean> implements Serializable 
{
    public static final String BEAN_NAME = "progress";

    public ProgressBean() 
    {
            super(ProgressBean.class);
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    public void startTask(ActionEvent event)
    {
        LongTaskManager threadBean = (LongTaskManager)FacesUtils.getManagedBean(LongTaskManager.BEAN_NAME);
        threadBean.startThread(10, 10, 650, 0);
    }
}
