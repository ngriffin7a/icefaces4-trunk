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

package org.icefaces.samples.showcase.example.compat.tab;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;

import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

@ComponentExample(
        parent = TabBean.BEAN_NAME,
        title = "example.compat.tab.visibility.title",
        description = "example.compat.tab.visibility.description",
        example = "/resources/examples/compat/tab/tabVisibility.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="tabVisibility.xhtml",
                    resource = "/resources/examples/compat/"+
                               "tab/tabVisibility.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="TabVisibility.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/tab/TabVisibility.java")
        }
)
@ManagedBean(name= TabVisibility.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class TabVisibility extends ComponentExampleImpl<TabVisibility> implements Serializable {
	
    public static final String BEAN_NAME = "tabVisibility";

    private boolean renderAccount = true;
    private boolean renderSupport = false;
    private boolean renderForum = true;

    public TabVisibility() {
            super(TabVisibility.class);
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    public boolean getRenderAccount() { return renderAccount; }
    public boolean getRenderSupport() { return renderSupport; }
    public boolean getRenderForum() { return renderForum; }

    public void setRenderAccount(boolean renderAccount) { this.renderAccount = renderAccount; }
    public void setRenderSupport(boolean renderSupport) { this.renderSupport = renderSupport; }
    public void setRenderForum(boolean renderForum) { this.renderForum = renderForum; }
}
