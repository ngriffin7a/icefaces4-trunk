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

package org.icefaces.samples.showcase.example.ace.overview;

import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;
import org.icefaces.samples.showcase.metadata.context.ResourceRootPath;

@ComponentExample(
        title = "example.ace.aceSuiteOverview.title",
        description = "example.ace.aceSuiteOverview.description",
        example = "/resources/examples/ace/aceSuiteOverview/aceSuiteOverview.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="aceSuiteOverview.xhtml",
                    resource = "/resources/examples/ace/aceSuiteOverview/aceSuiteOverview.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="AceSuiteOverview.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                    "/example/ace/overview/AceSuiteOverviewBean.java"),
            //WIKI documentation
            @ExampleResource(type = ResourceType.wiki,
                    title="ACE Components WIKI",
                    resource = ResourceRootPath.FOR_WIKI+"ACE+Components"),
            //TLD Documentation
            @ExampleResource(type = ResourceType.tld,
                    title="ACE Components TLD",
                    resource = ResourceRootPath.FOR_ACE_TLD + "tld-summary.html")
        }
)
@Menu(
	title = "menu.ace.aceSuiteOverview.subMenu.title",
	menuLinks = {
                    @MenuLink(title = "menu.ace.aceSuiteOverview.subMenu.main", isDefault = true, exampleBeanName = AceSuiteOverviewBean.BEAN_NAME)
                }
)
@ManagedBean(name= AceSuiteOverviewBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class AceSuiteOverviewBean extends ComponentExampleImpl<AceSuiteOverviewBean> implements Serializable 
{
    public static final String BEAN_NAME = "aceSuiteOverview";
    
    public AceSuiteOverviewBean() 
    {
        super(AceSuiteOverviewBean.class);
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }
}
