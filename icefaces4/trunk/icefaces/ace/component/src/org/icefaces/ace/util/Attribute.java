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
package org.icefaces.ace.util;

/**
 * classed used for pass through attribute support on mobility components.
 *
 * @author jguglielmin
 */
public class Attribute implements Comparable<Attribute> {
    private final String name;

    private final String event;

    public Attribute(String name, String event) {
        this.name = name;
        this.event = event;
    }

    public int compareTo(Attribute o) {
        // TODO Auto-generated method stub
        return this.getName().compareTo(o.getName());
    }

    public String getName() {
        return name;
    }

    public String getEvent() {
        return event;
    }

}
