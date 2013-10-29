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

package org.icefaces.samples.showcase.example.compat.positioned;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;

import com.icesoft.faces.component.panelpositioned.PanelPositionedEvent; 

import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

@ComponentExample(
        parent = PositionedBean.BEAN_NAME,
        title = "example.compat.positioned.listener.title",
        description = "example.compat.positioned.listener.description",
        example = "/resources/examples/compat/positioned/positionedListener.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="positionedListener.xhtml",
                    resource = "/resources/examples/compat/"+
                               "positioned/positionedListener.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="PositionedListener.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/positioned/PositionedListener.java")
        }
)
@ManagedBean(name= PositionedListener.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class PositionedListener extends ComponentExampleImpl<PositionedListener> implements Serializable {
	
	public static final String BEAN_NAME = "positionedListener";
	
	private String eventText = "No positions have been changed yet.";
	
	public PositionedListener() {
		super(PositionedListener.class);
	}
	
    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

	public String getEventText() { return eventText; }
	
	public void setEventText(String eventText) { this.eventText = eventText; }
	
	public void changeEvent(PanelPositionedEvent event) {
		if (event.getIndex() < 0) return;
	    StringBuilder sb = new StringBuilder(90);
	    
	    sb.append("Moved ");
	    sb.append(PositionedData.getFood(event.getIndex()));
	    sb.append(" from index ");
	    sb.append((event.getOldIndex()+1));
	    sb.append(" to ");
	    sb.append((event.getIndex()+1));
	    sb.append(".<br/>");
	    sb.append("The current top item is ");
	    sb.append(PositionedData.getTopFood());
	    sb.append(" and ");
	    sb.append("the current bottom item is ");
	    sb.append(PositionedData.getBottomFood());
	    sb.append(".");
	    
	    eventText = sb.toString();
	}
}
