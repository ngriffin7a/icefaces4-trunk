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

package com.icesoft.faces.component.paneltabset;

import javax.faces.component.UIComponent;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;
import javax.faces.event.PhaseId;

/**
 * TabChangeEvent is an extension of FacesEvent.
 */
public class TabChangeEvent
        extends FacesEvent {
    /**
     * The serialVersionUID unique identifier.
     */
    private static final long serialVersionUID = -7249763750612129099L;
    /**
     * The old selected tab index.
     */
    private int _oldTabIndex;
    /**
     * The new selected tab index.
     */
    private int _newTabIndex;

    /**
     * Creates an instance.
     *
     * @param component
     * @param oldTabIndex
     * @param newTabIndex
     */
    public TabChangeEvent(UIComponent component, int oldTabIndex,
                          int newTabIndex) {
        super(component);
        _oldTabIndex = oldTabIndex;
        _newTabIndex = newTabIndex;
        setPhaseId(PhaseId.INVOKE_APPLICATION);
    }

    /**
     * Returns the old selected tab index.
     *
     * @return _oldTabIndex
     */
    public int getOldTabIndex() {
        return _oldTabIndex;
    }

    /**
     * Returns the new selected tab index.
     *
     * @return _newTabIndex
     */
    public int getNewTabIndex() {
        return _newTabIndex;
    }

    /**
     * Returns true when the listener is an instance of TabChangeListener
     *
     * @param listener
     * @return true if listener is a TabChangeListener
     */
    public boolean isAppropriateListener(FacesListener listener) {
        return listener instanceof TabChangeListener;
    }

    /**
     * invokes the processTabChange method on the TabChangeListener.
     *
     * @param listener
     */
    public void processListener(FacesListener listener) {
        ((TabChangeListener) listener).processTabChange(this);
    }

}
