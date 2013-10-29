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

package org.icefaces.samples.showcase.example.ace.textAreaEntry;

import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;

@ComponentExample(
        title = "example.ace.textAreaEntry.title",
        description = "example.ace.textAreaEntry.description",
        example = "/resources/examples/ace/textAreaEntry/textAreaEntry.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="textAreaEntry.xhtml",
                    resource = "/resources/examples/ace/textAreaEntry/textAreaEntry.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="TextAreaEntry.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                    "/example/ace/textAreaEntry/TextAreaEntryBean.java")
        }
)
@Menu(
	title = "menu.ace.textAreaEntry.subMenu.main",
	menuLinks = {
	        @MenuLink(title = "menu.ace.textAreaEntry.subMenu.main",
	                isDefault = true,
                    exampleBeanName = TextAreaEntryBean.BEAN_NAME),
	        @MenuLink(title = "menu.ace.textAreaEntry.subMenu.label",
                    exampleBeanName = TextAreaEntryLabelBean.BEAN_NAME),
	        @MenuLink(title = "menu.ace.textAreaEntry.subMenu.indicator",
                    exampleBeanName = TextAreaEntryIndicatorBean.BEAN_NAME),
	        @MenuLink(title = "menu.ace.textAreaEntry.subMenu.reqStyle",
                    exampleBeanName = TextAreaEntryReqStyleBean.BEAN_NAME)
    }
)
@ManagedBean(name= TextAreaEntryBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class TextAreaEntryBean extends ComponentExampleImpl<TextAreaEntryBean> implements Serializable
{
    public static final String BEAN_NAME = "textAreaEntryBean";
    
    private String comment1;
    private String comment2;

    public TextAreaEntryBean()
    {
        super(TextAreaEntryBean.class);
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    public String getComment1() {
        return comment1;
    }

    public void setComment1(String comment) {
        this.comment1 = comment;
    }

    public String getComment2() {
        return comment2;
    }

    public void setComment2(String comment) {
        this.comment2 = comment;
    }
}
