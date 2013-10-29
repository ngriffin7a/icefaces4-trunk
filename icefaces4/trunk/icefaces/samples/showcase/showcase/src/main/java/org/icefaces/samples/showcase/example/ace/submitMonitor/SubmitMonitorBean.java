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

package org.icefaces.samples.showcase.example.ace.submitMonitor;

import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;

@ComponentExample(
        title = "example.ace.submitMonitor.title",
        description = "example.ace.submitMonitor.description",
        example = "/resources/examples/ace/submitMonitor/submitMonitor.xhtml"
)
@ExampleResources(
        resources ={
            @ExampleResource(type = ResourceType.xhtml,
                    title="submitMonitor.xhtml",
                    resource = "/resources/examples/ace/submitMonitor/submitMonitor.xhtml"),
            @ExampleResource(type = ResourceType.java,
                    title="SubmitMonitorBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase/example/ace/submitMonitor/SubmitMonitorBean.java")
        }
)
@Menu(
	title = "menu.ace.submitMonitor.subMenu.title",
	menuLinks = {
                    @MenuLink(title = "menu.ace.submitMonitor.subMenu.main", isDefault = true, exampleBeanName = SubmitMonitorBean.BEAN_NAME)
})
@ManagedBean(name= SubmitMonitorBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class SubmitMonitorBean extends ComponentExampleImpl<SubmitMonitorBean>
        implements Serializable {

    public static final String BEAN_NAME = "submitMonitor";
    private String forSelection;
    private String blockSelection;
    private String idleLabel;
    private String activeLabel;
    private String serverErrorLabel;
    private String networkErrorLabel;
    private String sessionExpiredLabel;


    public SubmitMonitorBean() {
        super(SubmitMonitorBean.class);
        initialiseInstanceVariables();
    }
    
    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    private void initialiseInstanceVariables() {
        this.forSelection = ":demoForm:demoGroup";
        this.blockSelection = "@none";
        this.idleLabel = "Idle";
        this.activeLabel = "Submitting...";
        this.serverErrorLabel = "Server error";
        this.networkErrorLabel = "Network error";
        this.sessionExpiredLabel = "Session expired";
    }

    public String getForSelection() {
        return forSelection;
    }
    public void setForSelection(String forSelection) {
        this.forSelection = forSelection;
    }

    public String getBlockSelection() {
        return blockSelection;
    }
    public void setBlockSelection(String blockSelection) {
        this.blockSelection = blockSelection;
    }

    public String getIdleLabel() {
        return idleLabel;
    }
    public void setIdleLabel(String idleLabel) {
        this.idleLabel = idleLabel;
    }

    public String getActiveLabel() {
        return activeLabel;
    }
    public void setActiveLabel(String activeLabel) {
        this.activeLabel = activeLabel;
    }

    public String getServerErrorLabel() {
        return serverErrorLabel;
    }
    public void setServerErrorLabel(String serverErrorLabel) {
        this.serverErrorLabel = serverErrorLabel;
    }

    public String getNetworkErrorLabel() {
        return networkErrorLabel;
    }
    public void setNetworkErrorLabel(String networkErrorLabel) {
        this.networkErrorLabel = networkErrorLabel;
    }

    public String getSessionExpiredLabel() {
        return sessionExpiredLabel;
    }
    public void setSessionExpiredLabel(String sessionExpiredLabel) {
        this.sessionExpiredLabel = sessionExpiredLabel;
    }

    public void sleep() {
        try {
            Thread.currentThread().sleep(2500);
        } catch(Exception e) {}
    }
}
