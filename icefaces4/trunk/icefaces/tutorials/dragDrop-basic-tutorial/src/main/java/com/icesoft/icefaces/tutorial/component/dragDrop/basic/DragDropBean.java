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

package com.icesoft.icefaces.tutorial.component.dragDrop.basic;

import com.icesoft.faces.component.dragdrop.DragEvent;
import java.io.Serializable;

/**
 * <p>The DragDropBean handles DragEvent listeners for the
 * Drag and Drop tutorial.</p>
 */
public class DragDropBean implements Serializable {

	private String dragMessage = "";
		
	public void dragListener(DragEvent dragEvent){
	        dragMessage += DragEvent.getEventName(dragEvent.getEventType()) + ", ";
	}

	public String getDragMessage () {
	        return dragMessage;
	}
}
