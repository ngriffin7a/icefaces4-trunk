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

package com.icesoft.faces.component.inputfile;

import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;
import javax.faces.event.PhaseId;
import javax.faces.component.UIComponent;

/**
 * @author Mark Collette
 * @since 1.8
 */
public class InputFileSetFileEvent extends FacesEvent {
    public InputFileSetFileEvent(UIComponent component) {
        super(component);
        this.setPhaseId(PhaseId.UPDATE_MODEL_VALUES);
    }
    
    public boolean isAppropriateListener(FacesListener listener) {
        return false;
    }
    
    public void processListener(FacesListener listener) {
    }
}
