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

package org.icefaces.samples.showcase.example.compat.tooltip;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.model.SelectItem;

import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

@ComponentExample(
        parent = TooltipBean.BEAN_NAME,
        title = "example.compat.tooltip.display.title",
        description = "example.compat.tooltip.display.description",
        example = "/resources/examples/compat/tooltip/tooltipDisplay.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="tooltipDisplay.xhtml",
                    resource = "/resources/examples/compat/"+
                               "tooltip/tooltipDisplay.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="TooltipDisplay.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/tooltip/TooltipDisplay.java")
        }
)
@ManagedBean(name= TooltipDisplay.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class TooltipDisplay extends ComponentExampleImpl<TooltipDisplay> implements Serializable {
	
    public static final String BEAN_NAME = "tooltipDisplay";

    public static final String TYPE_HOVER = "hover";
    public static final String TYPE_CLICK = "click";
    public static final String TYPE_ALTCLICK = "altclick";
    public static final String TYPE_DBLCLICK = "dblclick";

    private SelectItem[] availableTypes = new SelectItem[] {
        new SelectItem(TYPE_HOVER, "Hover"),
        new SelectItem(TYPE_CLICK, "Left Click"),
        new SelectItem(TYPE_ALTCLICK, "Right Click"),
        new SelectItem(TYPE_DBLCLICK, "Double Click")
    };
    private String type = availableTypes[0].getValue().toString();

    public TooltipDisplay() {
            super(TooltipDisplay.class);
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    public String getTypeHover() { return TYPE_HOVER; }
    public String getTypeClick() { return TYPE_CLICK; }
    public String getTypeAltClick() { return TYPE_ALTCLICK; }
    public String getTypeDblClick() { return TYPE_DBLCLICK; }
    public SelectItem[] getAvailableTypes() { return availableTypes; }

    public String getType() { return type; }

    public void setType(String type) { this.type = type; }
}
