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

package com.icesoft.faces.component.selectinputtext;

import javax.faces.event.ValueChangeEvent;
import javax.faces.event.FacesListener;
import javax.faces.component.UIComponent;

/**
 * TextChangeEvent is broadcast in the APPLY_REQUEST_VALUES phase via the
 * SelectInputText's textChangeListener MethodBinding, containing the
 * SelectInputText's submittedValue as its new value.
 * 
 * It's purpose is to notify the application that the user has typed in a
 * text fragment into the SelectInputText's text input field, allowing for
 * the application to refine its selection list which will popup.
 * 
 * In the case of converted and validated values, which require a complete
 * input of text, like with a Date, the textChangeListener may call
 * FacesContext.getCurrentInstance().renderResponse() to forstall the
 * doomed validation.
 * 
 * @author Mark Collette
 * @since ICEfaces 1.7
 */
public class TextChangeEvent extends ValueChangeEvent {
    public TextChangeEvent(UIComponent comp, Object oldValue, Object newValue) {
        super(comp, oldValue, newValue);
    }

    public boolean isAppropriateListener(FacesListener facesListener) {
        return false;
    }

    public void processListener(FacesListener facesListener) {
    }
}
