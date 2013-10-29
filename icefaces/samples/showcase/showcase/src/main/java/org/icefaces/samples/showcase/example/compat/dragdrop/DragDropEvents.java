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

package org.icefaces.samples.showcase.example.compat.dragdrop;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;

import com.icesoft.faces.component.dragdrop.DndEvent;
import com.icesoft.faces.component.dragdrop.DragEvent;
import com.icesoft.faces.component.dragdrop.DropEvent;

import org.icefaces.samples.showcase.util.FacesUtils;
import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

@ComponentExample(
        parent = DragDropBean.BEAN_NAME,
        title = "example.compat.dragdrop.events.title",
        description = "example.compat.dragdrop.events.description",
        example = "/resources/examples/compat/dragdrop/dragdropEvents.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="dragdropEvents.xhtml",
                    resource = "/resources/examples/compat/"+
                               "dragdrop/dragdropEvents.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="DragDropEvents.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/dragdrop/DragDropEvents.java")
        }
)
@ManagedBean(name= DragDropEvents.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class DragDropEvents extends ComponentExampleImpl<DragDropEvents> implements Serializable {
	
	public static final String BEAN_NAME = "dragdropEvents";
	private static final int ROW_SIZE = 10;
	private String[] availableMasks = new String[] {
	    "dragging",
	    "drag_cancel",
	    "dropped",
	    "hover_start",
	    "hover_end"
	};
	private List<String> eventLog = new ArrayList<String>(ROW_SIZE);
	private String[] dragMaskFrom = new String[] {
	    "dragging",
	    "drag_cancel",
	    "dropped",
	    "hover_start",
	    "hover_end"
	};
	private String[] dropMaskFrom = new String[] {
	    "dragging",
	    "drag_cancel",
	    "dropped",
	    "hover_start",
	    "hover_end"
	};
	private String[] dragMaskTo = new String[] {
	    "dragging",
	    "drag_cancel",
	    "hover_end"
	};
	private String[] dropMaskTo = new String[] {
	    "dragging",
	    "drag_cancel",
	    "hover_end"
	};
	
	public DragDropEvents() {
                        super(DragDropEvents.class);
	}
	
    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

	private void addEvent(DndEvent event, String from)
                {
	    StringBuilder sb = new StringBuilder(40);
	    sb.append("Fired event \"");
	    sb.append(DndEvent.getEventName(event.getEventType()));
	    sb.append("\" from ");
	    sb.append(from);
	    sb.append(" Listener with a drag value of [");
	    sb.append(event.getTargetDragValue());
	    sb.append("].");
	    eventLog.add(0, sb.toString());
	    
	    // Cap the list at the displayed row size
	    if (eventLog.size() > ROW_SIZE) {
	        eventLog = eventLog.subList(0, ROW_SIZE);
	    }
	}
	
	public void dragListener(DragEvent event) {
	    addEvent(event, "Drag");
	}
	
	public void dropListener(DropEvent event) {
	    addEvent(event, "Drop");
	    
	    // Propogate the event to the bean, since we may need to act upon the event
                    DragDropBean bean = (DragDropBean)FacesUtils.getManagedBean(DragDropBean.BEAN_NAME);
                    bean.dropListener(event);
	}
        
                public int getRowSize() { return ROW_SIZE; }
	public String[] getAvailableMasks() { return availableMasks; }
	public List<String> getEventLog() { return eventLog; }
	public String[] getDragMaskFrom() { return dragMaskFrom; }
	public String[] getDropMaskFrom() { return dropMaskFrom; }
	public String[] getDragMaskTo() { return dragMaskTo; }
	public String[] getDropMaskTo() { return dropMaskTo; }
	public String getDragMaskFromString() { return FacesUtils.join(dragMaskFrom); }
	public String getDropMaskFromString() { return FacesUtils.join(dropMaskFrom); }
	public String getDragMaskToString() { return FacesUtils.join(dragMaskTo); }
	public String getDropMaskToString() { return FacesUtils.join(dropMaskTo); }
	
	public void setEventLog(List<String> eventLog) { this.eventLog = eventLog; }
	public void setDragMaskFrom(String[] dragMaskFrom) { this.dragMaskFrom = dragMaskFrom; }
	public void setDropMaskFrom(String[] dropMaskFrom) { this.dropMaskFrom = dropMaskFrom; }
	public void setDragMaskTo(String[] dragMaskTo) { this.dragMaskTo = dragMaskTo; }
	public void setDropMaskTo(String[] dropMaskTo) { this.dropMaskTo = dragMaskTo; }
}
