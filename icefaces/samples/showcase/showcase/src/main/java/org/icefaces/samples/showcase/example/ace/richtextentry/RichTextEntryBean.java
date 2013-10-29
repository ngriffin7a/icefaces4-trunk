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

package org.icefaces.samples.showcase.example.ace.richtextentry;

import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;
import java.util.List;
import org.icefaces.samples.showcase.dataGenerators.ImageSet;
import org.icefaces.samples.showcase.dataGenerators.VehicleGenerator;
import org.icefaces.samples.showcase.example.compat.dataTable.Car;

@ComponentExample(
        title = "example.ace.richtextentry.title",
        description = "example.ace.richtextentry.description",
        example = "/resources/examples/ace/richtextentry/richTextEntryOverview.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="richTextEntryOverview.xhtml",
                    resource = "/resources/examples/ace/richtextentry/richTextEntryOverview.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="RichTextEntryBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                    "/example/ace/richtextentry/RichTextEntryBean.java")
        }
)
@Menu(
            title = "menu.ace.richtextentry.subMenu.title",
            menuLinks = {
                @MenuLink(title = "menu.ace.richtextentry.subMenu.main", isDefault = true, exampleBeanName = RichTextEntryBean.BEAN_NAME)
            }
)
@ManagedBean(name= RichTextEntryBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class RichTextEntryBean extends ComponentExampleImpl< RichTextEntryBean > implements Serializable {
    public static final String BEAN_NAME = "richTextEntryBean";
    private String text = "";
    
    public RichTextEntryBean() 
    {
        super(RichTextEntryBean.class);
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}