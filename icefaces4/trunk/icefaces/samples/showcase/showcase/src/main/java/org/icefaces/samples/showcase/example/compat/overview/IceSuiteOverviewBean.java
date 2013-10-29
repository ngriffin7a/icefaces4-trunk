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

package org.icefaces.samples.showcase.example.compat.overview;

import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;

@ComponentExample(
        title = "example.compat.iceSuiteOverview.title",
        description = "example.compat.iceSuiteOverview.description",
        example = "/resources/examples/compat/iceSuiteOverview/iceSuiteOverview.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="iceSuiteOverview.xhtml",
                    resource = "/resources/examples/compat/iceSuiteOverview/iceSuiteOverview.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="IceSuiteOverview.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                    "/example/compat/overview/IceSuiteOverviewBean.java")
        }
)
@Menu(
	title = "menu.compat.iceSuiteOverview.subMenu.title",
	menuLinks = {
                    @MenuLink(title = "menu.compat.iceSuiteOverview.subMenu.main", isDefault = true, exampleBeanName = IceSuiteOverviewBean.BEAN_NAME)
                }
)

@ManagedBean(name= IceSuiteOverviewBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class IceSuiteOverviewBean extends ComponentExampleImpl<IceSuiteOverviewBean> implements Serializable 
{
    public static final String BEAN_NAME = "iceSuiteOverview";
    
    public IceSuiteOverviewBean() 
    {
        super(IceSuiteOverviewBean.class);
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }
}
