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
import javax.faces.model.SelectItem;

import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

@ComponentExample(
        parent = RichTextBean.BEAN_NAME,
        title = "example.compat.richtext.skin.title",
        description = "example.compat.richtext.skin.description",
        example = "/resources/examples/compat/richtext/richtextSkin.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="richtextSkin.xhtml",
                    resource = "/resources/examples/compat/"+
                               "richtext/richtextSkin.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="RichTextSkin.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/richtext/RichTextSkin.java")
        }
)
@ManagedBean(name= RichTextSkin.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class RichTextSkin extends ComponentExampleImpl<RichTextSkin> implements Serializable {
	
    public static final String BEAN_NAME = "richtextSkin";

    private String text;
    private SelectItem[] availableSkins = new SelectItem[] {
        new SelectItem("default", "Tan"),
        //new SelectItem("silver", "Silver"),
        new SelectItem("office2003", "Office")
    };
    private String skin = availableSkins[0].getValue().toString();

    public RichTextSkin() {
            super(RichTextSkin.class);
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    public String getText() { return text; }
    public String getSkin() { return skin; }
    public SelectItem[] getAvailableSkins() { return availableSkins; }

    public void setText(String text) { this.text = text; }
    public void setSkin(String skin) { this.skin = skin; }
}
