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

package org.icefaces.samples.showcase.example.ace.maskedEntry;

import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;

@ComponentExample(
        parent = MaskedEntryBean.BEAN_NAME,
        title = "example.ace.maskedIndicator.title",
        description = "example.ace.maskedIndicator.description",
        example = "/resources/examples/ace/maskedEntry/maskedIndicator.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="maskedIndicator.xhtml",
                    resource = "/resources/examples/ace/maskedEntry/maskedIndicator.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="MaskedIndicatorBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                    "/example/ace/maskedEntry/MaskedIndicatorBean.java")
        }
)
@ManagedBean(name= MaskedIndicatorBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class MaskedIndicatorBean extends ComponentExampleImpl<MaskedIndicatorBean> implements Serializable
{
    public static final String BEAN_NAME = "maskedIndicatorBean";
    
    private boolean required = true;
    private String requiredText = "This field is required.";
    private String optionalText = "Not mandatory.";
    private String position = "right";
    private String dob;
    private String workPhone;
    private String mobilePhone;
    private String sin;


    public MaskedIndicatorBean() {
        super(MaskedIndicatorBean.class);
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

    public String getSin() {
        return sin;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public String getWorkPhone() {
        return workPhone;
    }

    public String getDob() {
        return dob;
    }

    public boolean isRequired() {
        return required;
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

    public void setDob(String dob) {
        this.dob = dob;
    }

    public void setWorkPhone(String workPhone) {
        this.workPhone = workPhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public void setSin(String sin) {
        this.sin = sin;
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }
}
