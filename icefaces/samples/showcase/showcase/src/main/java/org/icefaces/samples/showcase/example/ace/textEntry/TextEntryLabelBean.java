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

package org.icefaces.samples.showcase.example.ace.textEntry;

import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;

@ComponentExample(
        parent = TextEntryBean.BEAN_NAME,
        title = "example.ace.textEntry.label.title",
        description = "example.ace.textEntry.label.description",
        example = "/resources/examples/ace/textEntry/textEntryLabel.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="textEntryLabel.xhtml",
                    resource = "/resources/examples/ace/textEntry/textEntryLabel.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="TextEntryLabelBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                    "/example/ace/textEntry/TextEntryLabelBean.java")
        }
)
@ManagedBean(name= TextEntryLabelBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class TextEntryLabelBean extends ComponentExampleImpl<TextEntryLabelBean> implements Serializable
{
    public static final String BEAN_NAME = "textEntryLabelBean";
    
    private String firstLabelText = "First Name";
    private String lastLabelText = "Last Name";
    private String labelPosition = "inField";
    private String firstName;
    private String lastName;
    private String address1;
    private String address2;
    private String city;
    private String province;
    private String country;
    
    public TextEntryLabelBean() {
        super(TextEntryLabelBean.class);
    }
    
    public String getFirstLabelText() {
        return firstLabelText;
    }
    
    public String getLastLabelText() {
        return lastLabelText;
    }
    
    public String getLabelPosition() {
        return labelPosition;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAddress1() {
        return address1;
    }

    public String getAddress2() {
        return address2;
    }

    public String getCity() {
        return city;
    }

    public String getProvince() {
        return province;
    }

    public String getCountry() {
        return country;
    }

    public void setFirstLabelText(String firstLabelText) {
        this.firstLabelText = firstLabelText;
    }
    
    public void setLastLabelText(String lastLabelText) {
        this.lastLabelText = lastLabelText;
    }
    
    public void setLabelPosition(String labelPosition) {
        this.labelPosition = labelPosition;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }
}
