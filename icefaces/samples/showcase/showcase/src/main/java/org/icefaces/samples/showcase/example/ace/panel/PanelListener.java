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

package org.icefaces.samples.showcase.example.ace.panel;

import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;

import java.io.Serializable;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.icefaces.ace.event.CloseEvent;
import org.icefaces.ace.event.ToggleEvent;

@ComponentExample(
        parent = PanelBean.BEAN_NAME,
        title = "example.ace.panel.listener.title",
        description = "example.ace.panel.listener.description",
        example = "/resources/examples/ace/panel/panelListener.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="panelListener.xhtml",
                    resource = "/resources/examples/ace/panel/panelListener.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="PanelListener.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                    "/example/ace/panel/PanelListener.java")
        }
)
@ManagedBean(name= PanelListener.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class PanelListener extends ComponentExampleImpl<PanelListener> implements Serializable {

    public static final String BEAN_NAME = "panelListener";
	public String getBeanName() { return BEAN_NAME; }
    private Format formatter = new SimpleDateFormat("HH:mm:ss");
    private String statusMessage = "No status yet.";
    private boolean reOpenButton = false;

    
    public PanelListener() {
        super(PanelListener.class);
    }
    
    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    public String getStatusMessage() { return statusMessage; }
    
    public void setStatusMessage(String statusMessage) { this.statusMessage = statusMessage; }
    
    public void close(CloseEvent event) {
        statusMessage = "Close Event fired @ "+ formatter.format(new Date())+" server time";
        this.reOpenButton = true;
    }
    
    public void toggle(ToggleEvent event) {
        statusMessage = "Toggle Event fired @ "+ formatter.format(new Date())+ " server time";
    }

    /**
      * use ace:ajax on the pushButton to reset it to not be rendered
      * @param event
      */
     public void buttonNotVisible(ActionEvent event){
         this.reOpenButton = false;
     }

    public boolean isReOpenButton() {
        return reOpenButton;
    }

    public void setReOpenButton(boolean reOpenButton) {
        this.reOpenButton = reOpenButton;
    }

}
