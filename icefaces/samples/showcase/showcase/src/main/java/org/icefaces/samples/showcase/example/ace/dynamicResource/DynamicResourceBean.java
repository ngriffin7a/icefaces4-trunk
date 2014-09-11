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

package org.icefaces.samples.showcase.example.ace.dynamicResource;

import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;

@ComponentExample(
        title = "example.ace.dynamicResource.title",
        description = "example.ace.dynamicResource.description",
        example = "/resources/examples/ace/dynamicResource/dynamicResource.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="dynamicResource.xhtml",
                    resource = "/resources/examples/ace/dynamicResource/dynamicResource.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="DynamicResourceBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                    "/example/ace/dynamicResource/DynamicResourceBean.java")
        }
)
@Menu(
	title = "menu.ace.dynamicResource.subMenu.main",
	menuLinks = {
	        @MenuLink(title = "menu.ace.dynamicResource.subMenu.main",
	                isDefault = true,
                    exampleBeanName = DynamicResourceBean.BEAN_NAME),
	        @MenuLink(title = "menu.ace.dynamicResource.subMenu.advanced",
                    exampleBeanName = DynamicResourceAdvancedBean.BEAN_NAME)
    }
)
@ManagedBean(name= DynamicResourceBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class DynamicResourceBean extends ComponentExampleImpl<DynamicResourceBean> implements Serializable 
{
    public static final String BEAN_NAME = "dynamicResourceBean";
    
    public DynamicResourceBean() {
        super(DynamicResourceBean.class);
		setGroup(8);
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
