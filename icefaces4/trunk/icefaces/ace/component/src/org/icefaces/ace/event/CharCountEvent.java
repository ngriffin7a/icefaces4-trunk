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

public class CharCountEvent extends AjaxBehaviorEvent {
    private long currentLength;
    private long charsRemaining;

    public CharCountEvent(UIComponent component, Behavior behavior, long currentLength, long charsRemaining) {
        super(component, behavior);
        this.currentLength = currentLength;
        this.charsRemaining = charsRemaining;
    }

    public long getCurrentLength() {
        return currentLength;
    }

    public long getCharsRemaining() {
        return charsRemaining;
    }
}
