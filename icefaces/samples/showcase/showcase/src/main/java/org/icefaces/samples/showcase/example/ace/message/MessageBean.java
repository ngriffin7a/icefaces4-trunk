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
package org.icefaces.samples.showcase.example.ace.message;

import org.icefaces.ace.component.textentry.TextEntry;
import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import java.io.Serializable;
import java.util.HashMap;

@ComponentExample(
        title = "example.ace.message.title",
        description = "example.ace.message.description",
        example = "/resources/examples/ace/message/message.xhtml"
)
@ExampleResources(
        resources = {
                // xhtml
                @ExampleResource(type = ResourceType.xhtml,
                        title = "message.xhtml",
                        resource = "/resources/examples/ace/message/message.xhtml"),
                // Java Source
                @ExampleResource(type = ResourceType.java,
                        title = "MessageBean.java",
                        resource = "/WEB-INF/classes/org/icefaces/samples/showcase" +
                                "/example/ace/message/MessageBean.java")
        }
)
@Menu(
        title = "menu.ace.message.subMenu.main",
        menuLinks = {
                @MenuLink(title = "menu.ace.message.subMenu.main",
                        isDefault = true,
                        exampleBeanName = MessageBean.BEAN_NAME)
        }
)
@ManagedBean(name = MessageBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class MessageBean extends ComponentExampleImpl<MessageBean> implements Serializable {
    public static final String BEAN_NAME = "messageBean";

    private String firstName;
    private String lastName;
    private String city;
    private String province;
    private String country;

    private static HashMap<String, Integer> severityMap = new HashMap<String, Integer>() {{
            put("First Name", 0);
            put("Last Name", 1);
            put("City", 2);
            put("Country", 3);
        }
        private static final long serialVersionUID = 6584997908723158778L;
    };
    private static String[] severityNames = {"Info", "Warn", "Error", "Fatal"};

    public MessageBean() {
        super(MessageBean.class);
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void blurListener(AjaxBehaviorEvent event) {
        TextEntry textEntry = (TextEntry) event.getComponent();
        String value = textEntry.getValue().toString().trim();
        String label = textEntry.getLabel();
        if (value.equals("") || value.equalsIgnoreCase(label)) {
            int index = severityMap.get(label);
            String message;
            if (value.equals("")) {
                message = severityNames[index] + ": " + label + " missing.";
            } else {
                message = severityNames[index] + ": Value cannot be \"" + value + "\"";
            }
            FacesMessage facesMessage = new FacesMessage((FacesMessage.Severity) FacesMessage.VALUES.get(index), message, message);
            FacesContext.getCurrentInstance().addMessage(textEntry.getClientId(), facesMessage);
        }
    }
}
