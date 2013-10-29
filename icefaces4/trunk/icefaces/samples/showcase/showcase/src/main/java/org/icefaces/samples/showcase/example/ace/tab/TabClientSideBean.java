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

package org.icefaces.samples.showcase.example.ace.tab;


import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;

@ComponentExample(
        parent = TabSetBean.BEAN_NAME,
        title = "example.ace.tabSet.clientSide.title",
        description = "example.ace.tabSet.clientSide.description",
        example = "/resources/examples/ace/tab/tabset-client_side.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="tabset-client_side.xhtml",
                    resource = "/resources/examples/ace/tab/tabset-client_side.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="TabSetBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase/example/ace/tab/TabSetBean.java"),
            @ExampleResource(type = ResourceType.java,
                        title = "ImageSet.java",
                        resource = "/WEB-INF/classes/org/icefaces/samples/showcase/dataGenerators/ImageSet.java")
        }
)
@ManagedBean(name = TabClientSideBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class TabClientSideBean extends ComponentExampleImpl<TabClientSideBean> implements Serializable {

    public static final String BEAN_NAME = "tabClientSide";

    public TabClientSideBean() {
        super(TabClientSideBean.class);
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }
}
