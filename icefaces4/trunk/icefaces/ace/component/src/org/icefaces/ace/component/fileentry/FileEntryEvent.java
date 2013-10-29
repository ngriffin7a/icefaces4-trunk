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

package org.icefaces.ace.component.fileentry;

import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;
import javax.faces.component.UIComponent;

public class FileEntryEvent extends FacesEvent {
    private boolean invoke;

    public FileEntryEvent(UIComponent src, boolean invoke) {
        super(src);
        this.invoke = invoke;
    }

    /**
     * FileEntry depends on this event being broadcast every lifecycle that
     * it executes, whereas the fileEntryListener should only be invoked when
     * files have been uploaded. So, this invoked flag tracks when to invoke
     * the fileEntryListener.
     *
     * @return Whether to invoke the fileEntry component's fileEntryListener
     */
    public boolean isInvoke() {
        return invoke;
    }

    public void processListener(FacesListener facesListener) {
    }

    public boolean isAppropriateListener(FacesListener facesListener) {
        return false;
    }
}
