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

package org.icefaces.samples.showcase.example.compat.richtext;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;

import org.icefaces.samples.showcase.view.navigation.NavigationController;
import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

@ComponentExample(
        parent = RichTextBean.BEAN_NAME,
        title = "example.compat.richtext.disable.title",
        description = "example.compat.richtext.disable.description",
        example = "/resources/examples/compat/richtext/richtextDisable.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="richtextDisable.xhtml",
                    resource = "/resources/examples/compat/"+
                               "richtext/richtextDisable.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="RichTextDisable.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/richtext/RichTextDisable.java")
        }
)
@ManagedBean(name= RichTextDisable.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class RichTextDisable extends ComponentExampleImpl<RichTextDisable> implements Serializable {
	
    public static final String BEAN_NAME = "richtextDisable";

    private String text;
    private boolean disabled = true;

    public RichTextDisable() {
            super(RichTextDisable.class);
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    public String getText() { return text; }
    public boolean getDisabled() { return disabled; }

    public void setText(String text) { this.text = text; }
    public void setDisabled(boolean disabled) { this.disabled = disabled; }

    public void apply(ActionEvent event) {
        NavigationController.refreshPage();
    }
}
