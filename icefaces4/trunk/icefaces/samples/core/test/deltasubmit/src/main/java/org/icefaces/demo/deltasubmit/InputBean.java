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

package org.icefaces.demo.deltasubmit;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;
import java.util.Arrays;

@ManagedBean(name = "InputBean")
@SessionScoped
public class InputBean {
    private SingleValueEntry[] items = new SingleValueEntry[4];
    private MultiValueEntry[] mitems = new MultiValueEntry[4];

    public InputBean() {
        for (int i = 0; i < items.length; i++) {
            items[i] = new SingleValueEntry();
        }
        for (int i = 0; i < mitems.length; i++) {
            mitems[i] = new MultiValueEntry();
        }
    }

    public SingleValueEntry[] getItems() {
        return items;
    }

    public MultiValueEntry[] getMultiItems() {
        return mitems;
    }

    public static class SingleValueEntry {
        private String value = "A";

        public void setValue(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public static class MultiValueEntry {
        private String[] value = new String[0];

        public void setValue(String[] value) {
            this.value = value;
        }

        public String[] getValue() {
            return value;
        }

        public String getText() {
            StringBuffer sb = new StringBuffer();
            for (int i=0; i < value.length; i++) {
                if (i != 0) sb.append(", ");
                  sb.append(value[i]);
              }
              return sb.toString();
        }
    }
}
