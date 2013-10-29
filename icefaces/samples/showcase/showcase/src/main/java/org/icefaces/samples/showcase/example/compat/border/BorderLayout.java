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

package org.icefaces.samples.showcase.example.compat.border;

import java.io.Serializable;

import java.util.LinkedHashMap;
import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;

import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

@ComponentExample(
        parent = BorderBean.BEAN_NAME,
        title = "example.compat.border.layout.title",
        description = "example.compat.border.layout.description",
        example = "/resources/examples/compat/border/borderLayout.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="borderLayout.xhtml",
                    resource = "/resources/examples/compat/"+
                               "border/borderLayout.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="BorderLayout.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/border/BorderLayout.java")
        }
)
@ManagedBean(name= BorderLayout.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class BorderLayout extends ComponentExampleImpl<BorderLayout> implements Serializable {
	
    public static final String BEAN_NAME = "borderLayout";
    private LinkedHashMap <String, String> availableLayouts;
    private String layout;

    public BorderLayout() {
                super(BorderLayout.class);
                initializeDefaultInstanceVariables();
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    private void initializeDefaultInstanceVariables() {
        this.availableLayouts = new LinkedHashMap <String, String>();
        
        availableLayouts.put("Default","default");
        availableLayouts.put("Center Only", "center only");
        availableLayouts.put("Horizontal Reverse","horizontal reverse");
        availableLayouts.put("Vertical Reverse", "vertical reverse");
        availableLayouts.put("Hide North", "hide north");
        availableLayouts.put("Hide South", "hide south");
        availableLayouts.put("Hide East", "hide east");
        availableLayouts.put("Hide West","hide west");
        layout = availableLayouts.get("Default");
    }

    public LinkedHashMap<String, String> getAvailableLayouts() {
        return availableLayouts;
    }

    public void setAvailableLayouts(LinkedHashMap<String, String> availableLayouts) {
        this.availableLayouts = availableLayouts;
    }

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }
}
