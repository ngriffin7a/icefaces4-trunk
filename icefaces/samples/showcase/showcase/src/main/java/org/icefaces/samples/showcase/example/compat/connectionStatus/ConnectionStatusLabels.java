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

package org.icefaces.samples.showcase.example.compat.connectionStatus;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

@ComponentExample(
        parent = ConnectionStatus.BEAN_NAME,
        title = "example.compat.connectionStatus.labels.title",
        description = "example.compat.connectionStatus.labels.description",
        example = "/resources/examples/compat/connectionStatus/connectionStatusLabels.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="connectionStatusLabels.xhtml",
                    resource = "/resources/examples/compat/"+
                               "connectionStatus/connectionStatusLabels.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="ConnectionStatusLabels.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/connectionStatus/ConnectionStatusLabels.java")
        }
)
@ManagedBean(name= ConnectionStatusLabels.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class ConnectionStatusLabels extends ComponentExampleImpl<ConnectionStatusLabels> implements Serializable {
	
	public static final String BEAN_NAME = "connectionStatusLabels";
	
	private String activeLabel = "Active";
	private String inactiveLabel = "Inactive";
	private String disconnectLabel = "Disconnected";
	
	public ConnectionStatusLabels() {
		super(ConnectionStatusLabels.class);
	}
        
    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

                /**
	 * Method to simulate a long running task
	 * We'll just hold the thread for a certain length of time
	 * The client browser will react as "active" the entire time, so
	 *  the user can see the connection status in action
	 */
	public void longEvent(ActionEvent event) {
	    try{
	        Thread.sleep(1500);
	    }catch (Exception ignored) { }
	}
	
	/**
	 * Method to force a disconnect for the client
	 * This is achieved by expiring their session
	 * The user can therefore see the disconnected state of the connection status
	 */
	public void disconnectEvent(ActionEvent event) {
	    FacesContext context = FacesContext.getCurrentInstance();
	    HttpSession session = (HttpSession)context.getExternalContext().getSession(false);
	    session.invalidate();
	}
	
	public String getActiveLabel() { return activeLabel; }
	public String getInactiveLabel() { return inactiveLabel; }
	public String getDisconnectLabel() { return disconnectLabel; }
	
	public void setActiveLabel(String activeLabel) { this.activeLabel = activeLabel; }
	public void setInactiveLabel(String inactiveLabel) { this.inactiveLabel = inactiveLabel; }
	public void setDisconnectLabel(String disconnectLabel) { this.disconnectLabel = disconnectLabel; }
}
