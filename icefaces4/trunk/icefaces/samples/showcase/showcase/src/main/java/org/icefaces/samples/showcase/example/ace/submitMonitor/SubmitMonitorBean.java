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

package org.icefaces.samples.showcase.example.ace.submitMonitor;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ValueChangeEvent;

import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.Menu;
import org.icefaces.samples.showcase.metadata.annotation.MenuLink;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

@ComponentExample(
        title = "example.ace.submitMonitor.title",
        description = "example.ace.submitMonitor.description",
        example = "/resources/examples/ace/submitMonitor/submitMonitor.xhtml"
)
@ExampleResources(
        resources ={
            @ExampleResource(type = ResourceType.xhtml,
                    title="submitMonitor.xhtml",
                    resource = "/resources/examples/ace/submitMonitor/submitMonitor.xhtml"),
            @ExampleResource(type = ResourceType.java,
                    title="SubmitMonitorBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase/example/ace/submitMonitor/SubmitMonitorBean.java")
        }
)
@Menu(
		title = "menu.ace.submitMonitor.subMenu.title",
	menuLinks = {
		@MenuLink(title = "menu.ace.submitMonitor.subMenu.main", isDefault = true, exampleBeanName = SubmitMonitorBean.BEAN_NAME),
        @MenuLink(title = "menu.ace.submitMonitor.subMenu.configuration", exampleBeanName = SubmitMonitorConfiguration.BEAN_NAME),
        @MenuLink(title = "menu.ace.submitMonitor.subMenu.style", exampleBeanName = SubmitMonitorStyle.BEAN_NAME),
	    @MenuLink(title = "menu.ace.submitMonitor.subMenu.nested", exampleBeanName = SubmitMonitorNested.BEAN_NAME),
	    @MenuLink(title = "menu.ace.submitMonitor.subMenu.facet", exampleBeanName = SubmitMonitorFacet.BEAN_NAME)
})
@ManagedBean(name= SubmitMonitorBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class SubmitMonitorBean extends ComponentExampleImpl<SubmitMonitorBean> implements Serializable {
    public static final String BEAN_NAME = "submitMonitor";
	public String getBeanName() { return BEAN_NAME; }
	
	private static final long SLEEP = 2000;

    public SubmitMonitorBean() {
        super(SubmitMonitorBean.class);
    }
    
    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }
    
	public void sleep(long time) {
        try {
            Thread.sleep(time);
        }catch(InterruptedException ignored) { }
    }
	
	public void sleep() {
		sleep(SLEEP);
	}
	
	public void sleepLong() {
		sleep(SLEEP*2l);
	}
	
	public void sleepListener(ValueChangeEvent event) {
		sleep();
	}
}
