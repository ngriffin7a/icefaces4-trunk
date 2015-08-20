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

package org.icefaces.samples.showcase.example.ace.panelStack;

import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;
import java.lang.String;
import java.lang.System;


@ComponentExample(
		parent = PanelStackBean.BEAN_NAME,
        title = "example.ace.panelStack.client.title",
        description = "example.ace.panelStack.client.description",
        example = "/resources/examples/ace/panelStack/panelStackClient.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="panelStackClient.xhtml",
                    resource = "/resources/examples/ace/panelStack/panelStackClient.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="PanelStackClient.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                    "/example/ace/panelStack/PanelStackClient.java")
        }
)

@ManagedBean(name= PanelStackClient.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class PanelStackClient extends ComponentExampleImpl<PanelStackClient> implements  Serializable {
    public static final String BEAN_NAME = "panelStackClient";
	public String getBeanName() { return BEAN_NAME; }
    
    private String currentId="stackPane1"; // the id of the currently selected stackPane   

    public PanelStackClient() {
        super(PanelStackClient.class);
    }
    
    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    public String getCurrentId() {
        return currentId;
    }

    public void setCurrentId(String currentId) {
        this.currentId = currentId;
    }

   
}