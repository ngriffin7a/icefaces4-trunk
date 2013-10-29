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

package org.icefaces.tutorials;

import org.icefaces.ace.component.tabset.TabPaneCache;

import javax.faces.event.ActionEvent;
import java.io.Serializable;

public class Caching implements Serializable {
    private static final long serialVersionUID = 178220630189443946L;
    private long counter = 0;
    private String conditionallyIncrement;
    private String staticAutoInput;
    private String dynamicRevertStaticAutoInput;
    private String cache = "staticAuto";

    public Caching() {
        super();
    }

    public String getAlwaysIncrement() {
        return getNewValue();
    }

    public String getConditionallyIncrement() {
        // Lazy init
        if (conditionallyIncrement == null) {
            conditionallyIncrement = getNewValue();
        }
        return conditionallyIncrement;
    }

    public void increment(ActionEvent event) {
        conditionallyIncrement = getNewValue();
    }

    protected String getNewValue() {
        return Long.toHexString(System.currentTimeMillis() + counter++);
    }

    public String getStaticAutoInput() {
        return staticAutoInput;
    }

    public void setStaticAutoInput(String staticAutoInput) {
        this.staticAutoInput = staticAutoInput;
    }

    public String getDynamicRevertStaticAutoInput() {
        return dynamicRevertStaticAutoInput;
    }

    public void setDynamicRevertStaticAutoInput(String dynamicRevertStaticAutoInput) {
        this.dynamicRevertStaticAutoInput = dynamicRevertStaticAutoInput;
    }

    public String getCache() {
        return cache;
    }

    public void setCache(String cache) {
        this.cache = cache;
    }

    public void dynamicRevertStaticAutoActionListener(ActionEvent event) {
        cache = TabPaneCache.DYNAMIC_REVERT_STATIC_AUTO.getNamed();
    }
}
