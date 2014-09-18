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
        title = "example.ace.maskedEntry.title",
        description = "example.ace.maskedEntry.description",
        example = "/resources/examples/ace/maskedEntry/maskedEntry.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="maskedEntry.xhtml",
                    resource = "/resources/examples/ace/maskedEntry/maskedEntry.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="MaskedEntry.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                    "/example/ace/maskedEntry/MaskedEntryBean.java")
        }
)
@Menu(
	title = "menu.ace.maskedEntry.subMenu.main",
	menuLinks = {
	        @MenuLink(title = "menu.ace.maskedEntry.subMenu.main",
	                isDefault = true,
                    exampleBeanName = MaskedEntryBean.BEAN_NAME),
	        @MenuLink(title = "menu.ace.maskedEntry.subMenu.label",
                    exampleBeanName = MaskedLabelBean.BEAN_NAME),
	        @MenuLink(title = "menu.ace.maskedEntry.subMenu.indicator",
                    exampleBeanName = MaskedIndicatorBean.BEAN_NAME),
	        @MenuLink(title = "menu.ace.maskedEntry.subMenu.reqStyle",
                    exampleBeanName = MaskedReqStyleBean.BEAN_NAME)
    }
)
@ManagedBean(name= MaskedEntryBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class MaskedEntryBean extends ComponentExampleImpl<MaskedEntryBean> implements Serializable
{
    public static final String BEAN_NAME = "maskedEntryBean";
    
    private String dob;
    private String workPhone;
    private String mobilePhone;
    private String sin;
    
    public MaskedEntryBean()
    {
        super(MaskedEntryBean.class);
    }
    
    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getSin() {
        return sin;
    }

    public void setSin(String sin) {
        this.sin = sin;
    }

    public String getWorkPhone() {
        return workPhone;
    }

    public void setWorkPhone(String workPhone) {
        this.workPhone = workPhone;
    }
}
