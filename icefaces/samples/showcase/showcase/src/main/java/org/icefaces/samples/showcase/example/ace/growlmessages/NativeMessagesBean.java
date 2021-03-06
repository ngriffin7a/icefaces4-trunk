/*
 * Copyright 2004-2014 ICEsoft Technologies Canada Corp.
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

import javax.faces.application.FacesMessage;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import java.io.Serializable;
import java.util.Iterator;

@ManagedBean(name = NativeMessagesBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class NativeMessagesBean implements Serializable {
    public static final String BEAN_NAME = "nativeMessagesBean";
	public String getBeanName() { return BEAN_NAME; }

	private String header = "";

	private boolean info;
	private boolean warn;
	private boolean error;
	private boolean fatal;

 	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
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
