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

package org.icefaces.ace.component.animation;

import java.util.Collection;

import javax.faces.component.UIComponent;
import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.context.FacesContext;

public class ClientBehaviorContextImpl extends ClientBehaviorContext {
	private UIComponent uiComponent;
	private Collection<Parameter> parameters;
	private String eventName;
	private String sourceId;
	
	public ClientBehaviorContextImpl(UIComponent uiComponent, String eventName) {
		this.uiComponent = uiComponent;
		this.eventName = eventName;
	}
	
	public ClientBehaviorContextImpl(UIComponent uiComponent, String eventName, Collection<Parameter> parameters) {
		this(uiComponent, eventName);
		this.parameters = parameters;
	}
	
	public ClientBehaviorContextImpl(UIComponent uiComponent, String eventName, Collection<Parameter> parameters, String sourceId) {
		this(uiComponent, eventName, parameters);
		this.sourceId = sourceId;
	}
	
	@Override
	public UIComponent getComponent() {
		return uiComponent;
	}

	@Override
	public String getEventName() {
		return eventName;
	}

	@Override
	public FacesContext getFacesContext() {
		return FacesContext.getCurrentInstance();
	}

	@Override
	public Collection<Parameter> getParameters() {
		return parameters;
	}

	@Override
	public String getSourceId() {
		return sourceId;
	}

}
