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

package org.icefaces.samples.showcase.example.ace.animation;

import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;

@ComponentExample(
        title = "example.ace.animation.title",
        description = "example.ace.animation.description",
        example = "/resources/examples/ace/animation/animation.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="animation.xhtml",
                    resource = "/resources/examples/ace/animation/animation.xhtml")
        }
)
@Menu(
            title = "menu.ace.animation.subMenu.title",
            menuLinks = {
                @MenuLink(title = "menu.ace.animation.subMenu.main", isDefault = true, exampleBeanName = AnimationBean.BEAN_NAME)
            }
)
@ManagedBean(name= AnimationBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class AnimationBean extends ComponentExampleImpl<AnimationBean> implements Serializable {
    public static final String BEAN_NAME = "animationBean";
    
    public AnimationBean() 
    {
        super(AnimationBean.class);
    }
    
    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }
    }
