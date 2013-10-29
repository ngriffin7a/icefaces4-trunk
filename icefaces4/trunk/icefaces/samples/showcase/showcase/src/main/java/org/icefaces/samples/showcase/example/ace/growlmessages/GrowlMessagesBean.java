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
package org.icefaces.samples.showcase.example.ace.growlmessages;

import org.icefaces.ace.component.textentry.TextEntry;
import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ActionEvent;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;

@ComponentExample(
        title = "example.ace.growlmessages.title",
        description = "example.ace.growlmessages.description",
        example = "/resources/examples/ace/growlmessages/growlmessages.xhtml"
)
@ExampleResources(
        resources = {
                // xhtml
                @ExampleResource(type = ResourceType.xhtml,
                        title = "growlmessages.xhtml",
                        resource = "/resources/examples/ace/growlmessages/growlmessages.xhtml"),
                // Java Source
                @ExampleResource(type = ResourceType.java,
                        title = "GrowlMessagesBean.java",
                        resource = "/WEB-INF/classes/org/icefaces/samples/showcase" +
                                "/example/ace/growlmessages/GrowlMessagesBean.java")
        }
)
@Menu(
        title = "menu.ace.growlmessages.subMenu.main",
        menuLinks = {
                @MenuLink(title = "menu.ace.growlmessages.subMenu.main",
                        isDefault = true,
                        exampleBeanName = GrowlMessagesBean.BEAN_NAME)
        }
)
@ManagedBean(name = GrowlMessagesBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class GrowlMessagesBean extends ComponentExampleImpl<GrowlMessagesBean> implements Serializable {
    public static final String BEAN_NAME = "growlMessagesBean";

    private String autoHide = "true";
    private boolean closeAll = true;
    private int maxVisibleMessages = 0;
    private String position = "top-right";
	
	private boolean info;
	private boolean warn;
	private boolean error;
	private boolean fatal;

    public GrowlMessagesBean() {
        super(GrowlMessagesBean.class);
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    public String getAutoHide() {
        return autoHide;
    }

    public void setAutoHide(String autoHide) {
        this.autoHide = autoHide;
    }

    public boolean isCloseAll() {
        return closeAll;
    }

    public void setCloseAll(boolean closeAll) {
        this.closeAll = closeAll;
    }

    public int getMaxVisibleMessages() {
        return maxVisibleMessages;
    }

    public void setMaxVisibleMessages(int maxVisibleMessages) {
        this.maxVisibleMessages = maxVisibleMessages;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
	
	public boolean getInfo() {
		return info;
	}
	
	public void setInfo(boolean info) {
		this.info = info;
	}
	
	public boolean getWarn() {
		return warn;
	}
	
	public void setWarn(boolean warn) {
		this.warn = warn;
	}
	
	public boolean getError() {
		return error;
	}
	
	public void setError(boolean error) {
		this.error = error;
	}
	
	public boolean getFatal() {
		return fatal;
	}
	
	public void setFatal(boolean fatal) {
		this.fatal = fatal;
	}
	
	public void listener(ActionEvent event) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		
		// remove existing messages
		Iterator<FacesMessage> i = facesContext.getMessages();
		while (i.hasNext()) {
			i.next();
			i.remove();
		}
		
		// add messages
		UIComponent component = event.getComponent();
		if (info) {
			String message = "Info Sample";
			FacesMessage facesMessage = new FacesMessage((FacesMessage.Severity) FacesMessage.VALUES.get(0), message, message);
			facesContext.addMessage(component.getClientId(), facesMessage);
		}
		if (warn) {
			String message = "Warn Sample";
			FacesMessage facesMessage = new FacesMessage((FacesMessage.Severity) FacesMessage.VALUES.get(1), message, message);
			facesContext.addMessage(component.getClientId(), facesMessage);
		}
		if (error) {
			String message = "Error Sample";
			FacesMessage facesMessage = new FacesMessage((FacesMessage.Severity) FacesMessage.VALUES.get(2), message, message);
			facesContext.addMessage(component.getClientId(), facesMessage);
		}
		if (fatal) {
			String message = "Fatal Sample";
			FacesMessage facesMessage = new FacesMessage((FacesMessage.Severity) FacesMessage.VALUES.get(3), message, message);
			facesContext.addMessage(component.getClientId(), facesMessage);
		}
		if (!info && !warn && !error && !fatal) {
			String message = "No checkboxes checked";
			FacesMessage facesMessage = new FacesMessage((FacesMessage.Severity) FacesMessage.VALUES.get(0), message, message);
			facesContext.addMessage(component.getClientId(), facesMessage);		
		}
	}
}
