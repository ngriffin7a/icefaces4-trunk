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

package org.icefaces.application;


/**
 * PortableRenderer can trigger asynchronous rendering outside of a JSF lifecycle.
 */
public interface PortableRenderer {

    /**
     * Render the specified group of sessions by performing the JavaServer Faces
     * execute and render lifecycle phases.  If a FacesContext is in the
     * scope of the current thread scope, the current view will not be
     * asynchronously rendered
     * (it is already rendered as a result of the user event being
     * processed).
     */
    void render(String group);

    /**
     * Render message to the specified group of sessions but only to the clients
     * that have their blocking connection paused.
     */
    void render(String group, PushOptions options);
}
