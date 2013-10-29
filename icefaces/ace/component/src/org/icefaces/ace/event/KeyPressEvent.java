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

package org.icefaces.ace.event;

import javax.faces.component.UIComponent;
import javax.faces.component.behavior.Behavior;
import javax.faces.event.AjaxBehaviorEvent;

public class KeyPressEvent extends AjaxBehaviorEvent {
    private final String theChar;
    private final String newValue;

    public KeyPressEvent(UIComponent component, Behavior behavior, String theChar, String newValue) {
        super(component, behavior);
        this.theChar = theChar;
        this.newValue = newValue;
    }

    public String getChar() {
        return theChar;
    }

    public String getNewValue() {
        return newValue;
    }
}
