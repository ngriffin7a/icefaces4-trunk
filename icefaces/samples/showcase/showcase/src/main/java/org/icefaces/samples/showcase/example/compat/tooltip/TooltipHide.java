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
        title = "example.compat.tooltip.hide.title",
        description = "example.compat.tooltip.hide.description",
        example = "/resources/examples/compat/tooltip/tooltipHide.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="tooltipHide.xhtml",
                    resource = "/resources/examples/compat/"+
                               "tooltip/tooltipHide.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="TooltipHide.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/tooltip/TooltipHide.java")
        }
)
@ManagedBean(name= TooltipHide.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class TooltipHide extends ComponentExampleImpl<TooltipHide> implements Serializable {
	
    public static final String BEAN_NAME = "tooltipHide";

    public static final String TYPE_MOUSEOUT = "mouseout";
    public static final String TYPE_MOUSEDOWN = "mousedown";
    public static final String TYPE_NONE = "none";

    private SelectItem[] availableTypes = new SelectItem[] {
        new SelectItem(TYPE_MOUSEOUT, "Mouse Out"),
        new SelectItem(TYPE_MOUSEDOWN, "Mouse Down"),
        new SelectItem(TYPE_NONE, "None")
    };
    private String type = availableTypes[0].getValue().toString();	

    public TooltipHide() {
            super(TooltipHide.class);
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    public String getTypeMouseOut() { return TYPE_MOUSEOUT; }
    public String getTypeMouseDown() { return TYPE_MOUSEDOWN; }
    public String getTypeNone() { return TYPE_NONE; }
    public SelectItem[] getAvailableTypes() { return availableTypes; }

    public String getType() { return type; }

    public void setType(String type) { this.type = type; }
}
