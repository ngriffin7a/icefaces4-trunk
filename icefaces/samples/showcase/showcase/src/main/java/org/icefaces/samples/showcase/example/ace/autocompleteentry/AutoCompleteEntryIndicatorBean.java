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

package org.icefaces.samples.showcase.example.ace.autocompleteentry;

import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;

@ComponentExample(
        parent = AutoCompleteEntryBean.BEAN_NAME,
        title = "example.ace.autocompleteentry.indicator.title",
        description = "example.ace.autocompleteentry.indicator.description",
        example = "/resources/examples/ace/autocompleteentry/autoCompleteEntryIndicator.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="autoCompleteEntryIndicator.xhtml",
                    resource = "/resources/examples/ace/autocompleteentry/autoCompleteEntryIndicator.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="...IndicatorBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                    "/example/ace/autocompleteentry/AutoCompleteEntryIndicatorBean.java"),
            @ExampleResource(type = ResourceType.java,
                    title="AutoCompleteEntryBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                    "/example/ace/autocompleteentry/AutoCompleteEntryBean.java")
        }
)
@ManagedBean(name= AutoCompleteEntryIndicatorBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class AutoCompleteEntryIndicatorBean extends ComponentExampleImpl<AutoCompleteEntryIndicatorBean> implements Serializable
{
    public static final String BEAN_NAME = "autoCompleteEntryIndicatorBean";
    
    private boolean required = true;
    private String requiredText = "This field is required.";
    private String optionalText = "Not mandatory.";
    private String position = "right";

    public AutoCompleteEntryIndicatorBean() 
    {
        super(AutoCompleteEntryIndicatorBean.class);
    }
    
    public boolean getRequired() {
        return required;
    }
    
    public String getRequiredText() {
        return requiredText;
    }
    
    public String getOptionalText() {
        return optionalText;
    }
    
    public String getPosition() {
        return position;
    }
    
    public void setRequired(boolean required) {
        this.required = required;
    }
    
    public void setRequiredText(String requiredText) {
        this.requiredText = requiredText;
    }
    
    public void setOptionalText(String optionalText) {
        this.optionalText = optionalText;
    }
    
    public void setPosition(String position) {
        this.position = position;
    }
    
    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }
}
