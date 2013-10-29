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

import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

@ComponentExample(
        parent = TooltipBean.BEAN_NAME,
        title = "example.compat.tooltip.move.title",
        description = "example.compat.tooltip.move.description",
        example = "/resources/examples/compat/tooltip/tooltipMove.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="tooltipMove.xhtml",
                    resource = "/resources/examples/compat/"+
                               "tooltip/tooltipMove.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="TooltipMove.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/tooltip/TooltipMove.java")
        }
)
@ManagedBean(name= TooltipMove.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class TooltipMove extends ComponentExampleImpl<TooltipMove> implements Serializable {
	
    public static final String BEAN_NAME = "tooltipMove";

    private boolean move = true;

    public TooltipMove() {
            super(TooltipMove.class);
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    public boolean getMove() { return move; }

    public void setMove(boolean move) { this.move = move; }
}
