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

package org.icefaces.ace.event;

import javax.faces.component.UIComponent;
import javax.faces.component.behavior.Behavior;
import javax.faces.event.AjaxBehaviorEvent;

public class TreeEvent extends AjaxBehaviorEvent {
    private Object object;
	private boolean expand = false;
	private boolean contract = false;
	private boolean select = false;
	private boolean deselect = false;
	private boolean reorder = false;

    public TreeEvent(UIComponent component, Behavior behavior, Object object, String type) {
        super(component, behavior);
        this.object = object;
		if (type != null) {
			if ("expand".equals(type)) this.expand = true;
			else if ("contract".equals(type)) this.contract = true;
			else if ("select".equals(type)) this.select = true;
			else if ("deselect".equals(type)) this.deselect = true;
			else if ("reorder".equals(type)) this.reorder = true;
		}
    }

    /**
     * Return the data object of the node involved in the event
     * @return Object data object of the node involved in the event
     */
    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

	public boolean isExpandEvent() { return expand; }
	public boolean isContractEvent() { return contract; }
	public boolean isSelectEvent() { return select; }
	public boolean isDeselectEvent() { return deselect; }
	public boolean isReorderEvent() { return reorder; }
}
