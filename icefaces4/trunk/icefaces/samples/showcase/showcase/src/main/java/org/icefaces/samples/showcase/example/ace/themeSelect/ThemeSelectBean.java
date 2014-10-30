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

package org.icefaces.samples.showcase.example.ace.themeSelect;

import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.event.ActionEvent;

@ComponentExample(
        title = "example.ace.themeSelect.title",
        description = "example.ace.themeSelect.description",
        example = "/resources/examples/ace/themeSelect/themeSelect.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="themeSelect.xhtml",
                    resource = "/resources/examples/ace/themeSelect/themeSelect.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="ThemeSelectBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                    "/example/ace/themeSelect/ThemeSelectBean.java")
        }
)
@Menu(
    title = "menu.ace.themeSelect.subMenu.title", 
    menuLinks = {
        @MenuLink(title = "menu.ace.themeSelect.subMenu.main", isDefault = true, exampleBeanName = ThemeSelectBean.BEAN_NAME)
    }
)

@ManagedBean(name = ThemeSelectBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class ThemeSelectBean extends ComponentExampleImpl<ThemeSelectBean> implements Serializable {
    public static final String BEAN_NAME = "themeSelectBean";
	public String getBeanName() { return BEAN_NAME; }
    
    public ThemeSelectBean() {
        super(ThemeSelectBean.class);
    }
    
    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }
}
