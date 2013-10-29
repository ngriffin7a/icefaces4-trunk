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

package org.icefaces.tutorials.util;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import java.util.Collection;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * User: Nils
 * Date: 11-04-05
 * Time: 1:50 PM
 */
public class TutorialMessageUtils {
    private Map getLinkLabelMap = new LinkLabelMap();
    private Map getLinkUrlMap = new LinkUrlMap();
    private static ResourceBundle messages = ResourceBundle.getBundle("messages");

    /* Faux-maps to allow for parameter passing in EL 1.0 */

    /*
     * LinkLabelMap - Returns the title for the currently selected navigation.content.list string
     */
    private class LinkUrlMap implements Map {
        public Object get(Object o) {
            String s = (String)o;
            return messages.getString("navigation.link."+o+".url");
        }

        public int size() { return 0; }
        public boolean isEmpty() { return false; }
        public boolean containsKey(Object o) { return false; }
        public boolean containsValue(Object o) { return false; }
        public Object put(Object o, Object o1) { return null; }
        public Object remove(Object o) { return null; }
        public void putAll(Map map) {}
        public void clear() {}
        public Set keySet() { return null; }
        public Collection values() { return null; }
        public Set entrySet() { return null; }
    }
    private class LinkLabelMap implements Map {
        public Object get(Object o) {
            String s = (String)o;
            return messages.getString("navigation.link."+o+".label");
        }

        public int size() { return 0; }
        public boolean isEmpty() { return false; }
        public boolean containsKey(Object o) { return false; }
        public boolean containsValue(Object o) { return false; }
        public Object put(Object o, Object o1) { return null; }
        public Object remove(Object o) { return null; }
        public void putAll(Map map) {}
        public void clear() {}
        public Set keySet() { return null; }
        public Collection values() { return null; }
        public Set entrySet() { return null; }
    }
    public Map getGetLinkLabelMap() { return getLinkLabelMap; }
    public void setGetLinkLabelMap(Map getLinkLabelMap) { this.getLinkLabelMap = getLinkLabelMap; }
    public static String getMessage(String key) { return messages.getString(key); }
    public Map getGetLinkUrlMap() { return getLinkUrlMap; }
    public void setGetLinkUrlMap(Map getLinkUrlMap) { this.getLinkUrlMap = getLinkUrlMap; }
}
