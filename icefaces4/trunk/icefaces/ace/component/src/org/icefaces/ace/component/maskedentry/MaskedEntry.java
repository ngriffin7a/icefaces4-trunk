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

package org.icefaces.ace.component.maskedentry;

import org.icefaces.ace.event.KeyPressEvent;
import org.icefaces.component.Focusable;

import javax.faces.context.FacesContext;
import javax.faces.component.UINamingContainer;
import javax.faces.component.html.HtmlInputText;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.FacesEvent;
import java.util.Map;

public class MaskedEntry extends MaskedEntryBase implements Focusable {

	private static final String OPTIMIZED_PACKAGE = "org.icefaces.ace.component.";

    public final static String THEME_INPUT_CLASS = "ui-inputfield ui-inputmask ui-widget ui-state-default ui-corner-all";
    public final static String PLAIN_INPUT_CLASS = "ui-inputmask";

	protected FacesContext getFacesContext() {
		return FacesContext.getCurrentInstance();
	}

    public void queueEvent(FacesEvent event) {
        Map<String, String> paramMap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String clientId = getClientId();
        String eventSource = paramMap.get("javax.faces.source");
        String eventType = paramMap.get("javax.faces.behavior.event");
        String theChar = paramMap.get("char");
        String value = paramMap.get(clientId + "_field");
        if (clientId.equals(eventSource) && "keypress".equals(eventType) && event instanceof AjaxBehaviorEvent) {
            KeyPressEvent keyPressEvent = new KeyPressEvent(this, ((AjaxBehaviorEvent) event).getBehavior(), theChar, value);
            super.queueEvent(keyPressEvent);
            return;
        }
        super.queueEvent(event);
	}

    public String getFocusedElementId() {
        return getClientId(FacesContext.getCurrentInstance()) + "_field";
    }
}
