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

package org.icefaces.samples.showcase.example.ace.menu;

import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;

import java.io.Serializable;
import java.util.LinkedHashMap;

@ComponentExample(
        parent = MenuBean.BEAN_NAME,
        title = "example.ace.menu.effect.title",
        description = "example.ace.menu.effect.description",
        example = "/resources/examples/ace/menu/menuEffect.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="menuEffect.xhtml",
                    resource = "/resources/examples/ace/menu/menuEffect.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="MenuEffect.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                    "/example/ace/menu/MenuEffect.java")
        }
)
@ManagedBean(name= MenuEffect.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class MenuEffect extends ComponentExampleImpl<MenuEffect> implements Serializable {
    public static final String BEAN_NAME = "menuEffect";
    public static final String DEFAULT_EFFECT = "Slide";
    
    private LinkedHashMap <String, String> availableEffects;
    private String effectName;
    private int effectDuration;
    
    public MenuEffect() {
        super(MenuEffect.class);
        
        availableEffects = populateAvailableEffects();
        effectName = availableEffects.get(DEFAULT_EFFECT);
        effectDuration = 400;
    }
    
    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    private LinkedHashMap<String, String> populateAvailableEffects()
    {
        LinkedHashMap <String, String> list = new LinkedHashMap <String, String>();
        list.put("Slide","slide");
        list.put("Fade", "fade");
        return list;
    }

    public LinkedHashMap<String, String> getAvailableEffects() {return availableEffects;}
    public String getEffectName() { return effectName; }
    public int getEffectDuration() { return effectDuration; }
    
    public void setEffectName(String effectName) { this.effectName = effectName; }
    public void setEffectDuration(int effectDuration) { this.effectDuration = effectDuration; }
}
