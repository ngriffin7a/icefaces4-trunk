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

package org.icefaces.samples.showcase.example.ace.panel;

import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;

@ComponentExample(
        parent = PanelBean.BEAN_NAME,
        title = "example.ace.panel.close.title",
        description = "example.ace.panel.close.description",
        example = "/resources/examples/ace/panel/panelClose.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="panelClose.xhtml",
                    resource = "/resources/examples/ace/panel/panelClose.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="PanelClose.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                    "/example/ace/panel/PanelClose.java")
        }
)
@ManagedBean(name= PanelClose.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class PanelClose extends ComponentExampleImpl<PanelClose> implements Serializable {

    public static final String BEAN_NAME = "panelClose";
    
    private boolean closable = true;
    private int speed = 700;
    
    public PanelClose() {
        super(PanelClose.class);
    }
    
    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    public boolean getClosable() { return closable; }
    public int getSpeed() { return speed; }
    
    public void setClosable(boolean closable) { this.closable = closable; }
    public void setSpeed(int speed) { this.speed = speed; }
}
