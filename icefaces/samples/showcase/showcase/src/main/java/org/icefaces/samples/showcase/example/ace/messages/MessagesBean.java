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
package org.icefaces.samples.showcase.example.ace.messages;

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
        title = "example.ace.messages.title",
        description = "example.ace.messages.description",
        example = "/resources/examples/ace/messages/messages.xhtml"
)
@ExampleResources(
        resources = {
                // xhtml
                @ExampleResource(type = ResourceType.xhtml,
                        title = "messages.xhtml",
                        resource = "/resources/examples/ace/messages/messages.xhtml"),
                // Java Source
                @ExampleResource(type = ResourceType.java,
                        title = "MessagesBean.java",
                        resource = "/WEB-INF/classes/org/icefaces/samples/showcase" +
                                "/example/ace/messages/MessagesBean.java")
        }
)
@Menu(
        title = "menu.ace.messages.subMenu.main",
        menuLinks = {
                @MenuLink(title = "menu.ace.messages.subMenu.main",
                        isDefault = true,
                        exampleBeanName = MessagesBean.BEAN_NAME)
        }
)
@ManagedBean(name = MessagesBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class MessagesBean extends ComponentExampleImpl<MessagesBean> implements Serializable {
    public static final String BEAN_NAME = "messagesBean";

    private String fullName;
    private String dateOfBirth;
    private String major;
    private String workingSince;

    private static HashMap<String, Integer> severityMap = new HashMap<String, Integer>() {{
            put("Full Name", 3);
            put("Date of Birth", 2);
            put("Major", 1);
            put("Working Since", 0);
        }
        private static final long serialVersionUID = 6584997908723158778L;
    };
    private static String[] severityNames = {"Info", "Warn", "Error", "Fatal"};

    public MessagesBean() {
        super(MessagesBean.class);
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getWorkingSince() {
        return workingSince;
    }

    public void setWorkingSince(String workingSince) {
        this.workingSince = workingSince;
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
