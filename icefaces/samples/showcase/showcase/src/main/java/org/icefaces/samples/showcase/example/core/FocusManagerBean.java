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

package org.icefaces.samples.showcase.example.core;

import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import java.io.Serializable;

@ComponentExample(
        title = "Focus Manager Component",
        description = "The <b>&lt;icecore:focusManager&gt;</b> component manages where focus will be applied on page load.",
        example = "/resources/examples/core/focus-manager.xhtml"
)

@ExampleResources(
        resources = {
                @ExampleResource(type = ResourceType.xhtml,
                        title="autoCompleteEntry.xhtml",
                        resource = "/resources/examples/core/focus-manager.xhtml"),
                @ExampleResource(type = ResourceType.java,
                        title="AutoCompleteEntryBean.java",
                        resource = "/WEB-INF/classes/org/icefaces/samples/showcase/example/core/FocusManagerBean.java"),
        }
)

@ManagedBean
@CustomScoped(value = "#{window}")
public class FocusManagerBean extends ComponentExampleImpl<FocusManagerBean> implements Serializable {
    private String focusedComponent = "";
    private boolean renderInputs = true;

    public FocusManagerBean() {
        super(FocusManagerBean.class);
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    public String getFocusedComponent() {
        return focusedComponent;
    }

    public void setFocusedComponent(String focusedComponent) {
        this.focusedComponent = focusedComponent;
    }
}
