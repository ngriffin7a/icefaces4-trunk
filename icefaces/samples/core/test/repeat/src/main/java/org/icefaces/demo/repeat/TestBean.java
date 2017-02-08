/*
 * Copyright 2004-2016 ICEsoft Technologies Canada Corp.
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

package org.icefaces.demo.repeat;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;
import java.io.Serializable;
import java.util.ArrayList;

@ManagedBean(name = "test")
@SessionScoped
public class TestBean implements Serializable {
    private static final String[] list = { "a", "b", "c", "d", "e", "f", "g", "h", "j", "k" };
    private ArrayList<ClickCounter> counters = new ArrayList<ClickCounter>();

    public TestBean() {
        counters.add(new ClickCounter("A"));
        counters.add(new ClickCounter("B"));
        counters.add(new ClickCounter("C"));
        System.out.println(this);
    }

    public String[] getList() {
        return list;
    }

    public ArrayList<ClickCounter> getCounters() {
        return counters;
    }

    public static class ClickCounter implements Serializable {
        private String name;
        private int clicks;

        public ClickCounter(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public int getClicks() {
            return clicks;
        }

        public void click(ActionEvent e) {
            ++clicks;
        }
    }
}