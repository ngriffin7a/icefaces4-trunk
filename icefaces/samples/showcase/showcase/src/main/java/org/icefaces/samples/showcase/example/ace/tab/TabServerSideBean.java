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
import org.icefaces.samples.showcase.util.FacesUtils;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;

@ComponentExample(
        parent = TabSetBean.BEAN_NAME,
        title = "example.ace.tabSet.serverSide.title",
        description = "example.ace.tabSet.serverSide.description",
        example = "/resources/examples/ace/tab/tabset-server_side.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="tabset-server_side.xhtml",
                    resource = "/resources/examples/ace/tab/tabset-server_side.xhtml"),
            @ExampleResource(type = ResourceType.java,
                    title="TabServerSideBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase/example/ace/tab/TabServerSideBean.java")
        }
)
@ManagedBean(name = TabServerSideBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class TabServerSideBean extends ComponentExampleImpl<TabServerSideBean>
        implements Serializable {

    public static final String BEAN_NAME = "tabServerSide";

    private boolean fastTabs = true; // Add delay (large image, backend wait etc.) to tab loading make tabset difference clear

    public String getSlowDownTab() {
        try { Thread.sleep(2000); }
        catch (Exception e) {
            FacesUtils.addErrorMessage("Server-side tab waiting could not finish.");
        }
        return "";
    }

    public void setSlowDownTab(String slowDownTab) {}

    public boolean isFastTabs() {
        return fastTabs;
    }

    public void setFastTabs(boolean fastTabs) {
        this.fastTabs = fastTabs;
    }


    public TabServerSideBean() {
        super(TabServerSideBean.class);
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }
}
