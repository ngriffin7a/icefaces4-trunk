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

package org.icefaces.samples.showcase.example.ace.contextMenu;

import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import javax.faces.event.FacesEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.component.UIComponent;
import javax.annotation.PostConstruct;
import java.io.Serializable;
import javax.faces.model.SelectItem;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Date;

@ComponentExample(
        parent = ContextMenuBean.BEAN_NAME,
        title = "example.ace.contextMenu.effect.title",
        description = "example.ace.contextMenu.effect.description",
        example = "/resources/examples/ace/contextMenu/contextMenuEffect.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="contextMenuEffect.xhtml",
                    resource = "/resources/examples/ace/contextMenu/contextMenuEffect.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="ContextMenuEffect.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                    "/example/ace/contextMenu/ContextMenuEffect.java")
        }
)
@ManagedBean(name= ContextMenuEffect.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class ContextMenuEffect extends ComponentExampleImpl<ContextMenuEffect> implements Serializable {
    public static final String BEAN_NAME = "contextMenuEffect";
    
    private SelectItem[] availableEffects = { new SelectItem("bounce", "Bounce"),
                                              new SelectItem("clip", "Clip"),
                                              new SelectItem("fade", "Fade"),
                                              new SelectItem("puff", "Puff"),
                                              new SelectItem("slide", "Slide") };
    
    private String effectName = availableEffects[0].getValue().toString();
    private int effectDuration = 300;
    
    public ContextMenuEffect() {
        super(ContextMenuEffect.class);
    }
    
    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    public SelectItem[] getAvailableEffects() { return availableEffects; }
    public String getEffectName() { return effectName; }
    public int getEffectDuration() { return effectDuration; }
    
    public void setEffectName(String effectName) { this.effectName = effectName; }
    public void setEffectDuration(int effectDuration) { this.effectDuration = effectDuration; }
}
