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

package org.icefaces.demo.push;

import org.icefaces.application.PushRenderer;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;

@ManagedBean(name = "Window")
@CustomScoped(value = "#{window}")
public class Window {
    private static int subCounter = 0;
    private String id;

    public Window() {
        id = generateID();
    }

    @PostConstruct
    public void init() {
        PushRenderer.addCurrentView("test");
    }

    public String getId() {
        return id;
    }

    private synchronized String generateID() {
        return Integer.toString((++subCounter) + (hashCode() / 10000), 36).toUpperCase();
    }
}
