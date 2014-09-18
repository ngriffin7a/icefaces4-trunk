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
import javax.faces.bean.ViewScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ValueChangeEvent;
import java.io.Serializable;

@ComponentExample(
        parent = TextEntryBean.BEAN_NAME,
        title = "example.ace.textEntry.autotab.title",
        description = "example.ace.textEntry.autotab.description",
        example = "/resources/examples/ace/textEntry/textEntryAutotab.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="textEntryAutotab.xhtml",
                    resource = "/resources/examples/ace/textEntry/textEntryAutotab.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="TextEntryAutotabBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                    "/example/ace/textEntry/TextEntryAutotabBean.java")
        }
)
@ManagedBean(name= TextEntryAutotabBean.BEAN_NAME)
@ViewScoped
public class TextEntryAutotabBean extends ComponentExampleImpl<TextEntryAutotabBean> implements Serializable
{
    public static final String BEAN_NAME = "textEntryAutotabBean";
    
    private int firstNameLength = 5;
    private int lastNameLength = 15;
    private int cityLength = 10;
    private int provinceLength = 2;
    
    private String firstName, lastName, city, province;
    
    public TextEntryAutotabBean() {
        super(TextEntryAutotabBean.class);
    }
    
    public int getFirstNameLength() {
        return firstNameLength;
    }
    
    public int getLastNameLength() {
        return lastNameLength;
    }
    
    public int getCityLength() {
        return cityLength;
    }
    
    public int getProvinceLength() {
        return provinceLength;
    }
    
    public void setFirstNameLength(int firstNameLength) {
        this.firstNameLength = firstNameLength;
    }
    
    public void setLastNameLength(int lastNameLength) {
        this.lastNameLength = lastNameLength;
    }
    
    public void setCityLength(int cityLength) {
        this.cityLength = cityLength;
    }
    
    public void setProvinceLength(int provinceLength) {
        this.provinceLength = provinceLength;
    }
    
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }
}
