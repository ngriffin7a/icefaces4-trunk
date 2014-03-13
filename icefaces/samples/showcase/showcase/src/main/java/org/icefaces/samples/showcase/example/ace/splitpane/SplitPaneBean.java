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

package org.icefaces.samples.showcase.example.ace.splitpane;
import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;
import java.util.List;

@ComponentExample(
        title = "example.ace.splitpane.title",
        description = "example.ace.splitpane.description",
        example = "/resources/examples/ace/splitpane/splitPaneOverview.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="splitPaneOverview.xhtml",
                    resource = "/resources/examples/ace/splitpane/splitPaneOverview.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="SplitPaneBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                    "/example/ace/splitpane/SplitPaneBean.java")
        }
)
@Menu(
            title = "menu.ace.splitpane.subMenu.title",
            menuLinks = {
                @MenuLink(title = "menu.ace.splitpane.subMenu.main", isDefault = true, exampleBeanName = SplitPaneBean.BEAN_NAME)
            }
)
@ManagedBean(name= SplitPaneBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class SplitPaneBean extends ComponentExampleImpl< SplitPaneBean > implements Serializable {
    public static final String BEAN_NAME = "splitPaneBean";
    
    public SplitPaneBean() 
    {
        super(SplitPaneBean.class);
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }
}