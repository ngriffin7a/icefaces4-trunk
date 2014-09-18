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
        title = "example.ace.maskedReqStyle.title",
        description = "example.ace.maskedReqStyle.description",
        example = "/resources/examples/ace/maskedEntry/maskedReqStyle.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="maskedReqStyle.xhtml",
                    resource = "/resources/examples/ace/maskedEntry/maskedReqStyle.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="MaskedReqStyle.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                    "/example/ace/maskedEntry/MaskedReqStyleBean.java")
        }
)
@ManagedBean(name= MaskedReqStyleBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class MaskedReqStyleBean extends ComponentExampleImpl<MaskedReqStyleBean> implements Serializable
{
    public static final String BEAN_NAME = "maskedReqStyleBean";
    
    private String reqColor = "redRS";
    private String optColor = "greenRS";
    private String dob;
    private String workPhone;
    private String mobilePhone;
    private String sin;


    public MaskedReqStyleBean() {
        super(MaskedReqStyleBean.class);
    }
    
    public String getReqColor() {
        return reqColor;
    }
    
    public String getOptColor() {
        return optColor;
    }
    
    public void setReqColor(String reqColor) {
        this.reqColor = reqColor;
    }
    
    public void setOptColor(String optColor) {
        this.optColor = optColor;
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
    
    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }
	
	private boolean useTheme = false;

    public boolean getUseTheme() {
        return useTheme;
    }

    public void setUseTheme(boolean useTheme) {
        this.useTheme = useTheme;
    }
}
