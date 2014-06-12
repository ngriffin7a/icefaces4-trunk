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

package org.icefaces.impl.component;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

public class LoadBundle extends UIComponentBase {
    private String oldVar;
    transient private Locale oldLocale;
    transient private String oldBasename = "";
    transient private ResourceBundle bundle;
    private Map map = new SerializableMap();

    public String getFamily() {
        return "javax.faces.Output";
    }

    public String getVar() {
        return (String) getStateHelper().eval("var");
    }

    public void setVar(String var) {
        getStateHelper().put("var", var);
    }

    public String getBasename() {
        return (String) getStateHelper().eval("basename");
    }

    public void setBasename(String basename) {
        getStateHelper().put("basename", basename);
    }

    public void decode(FacesContext context) {
        updateBundle(context);
    }

    public void encodeBegin(FacesContext context) throws IOException {
        updateBundle(context);
    }

    private void updateBundle(FacesContext context) {
        String var = getVar();
        String basename = getBasename();
        Locale locale = context.getViewRoot().getLocale();
        boolean reloadRequired =
                oldLocale == null ||
                !oldLocale.getLanguage().equals(locale.getLanguage()) ||
                !basename.equals(oldBasename) ||
                !var.equals(oldVar);
        if (reloadRequired) {
            bundle = ResourceBundle.getBundle(basename.trim(), locale, getClassLoader(this));
            map = new SerializableMap();
            Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
            requestMap.remove(oldVar);
            requestMap.put(var, map);

            oldBasename = basename;
            oldLocale = locale;
            oldVar = var;
        }
    }

    private ResourceBundle getBundle() {
        if (bundle == null) {
            updateBundle(FacesContext.getCurrentInstance());
        }
        return bundle;
    }

    private static ClassLoader getClassLoader(Object fallback) {
        ClassLoader classLoader = Thread.currentThread()
                .getContextClassLoader();
        if (classLoader == null) {
            classLoader = fallback.getClass().getClassLoader();
        }
        return classLoader;
    }

    private class SerializableMap implements Map, Serializable {
        private static final long serialVersionUID = 1L;

        public void clear() {
            throw new UnsupportedOperationException();
        }

        public boolean containsKey(Object key) {
            return (null == key) ? false : (null != getBundle().getObject(key.toString()));
        }

        public boolean containsValue(Object value) {
            boolean found = false;
            ResourceBundle resourceBundle = getBundle();
            Enumeration keys = resourceBundle.getKeys();
            while (keys.hasMoreElements()) {
                Object currentValue = resourceBundle.getObject((String) keys.nextElement());
                if ((value == currentValue) || ((null != currentValue) && currentValue.equals(value))) {
                    found = true;
                    break;
                }
            }
            return found;
        }

        public Set entrySet() {
            HashMap entries = new HashMap();
            ResourceBundle resourceBundle = getBundle();
            Enumeration keys = resourceBundle.getKeys();
            while (keys.hasMoreElements()) {
                Object key = keys.nextElement();
                Object value = resourceBundle.getObject((String) key);
                entries.put(key, value);
            }

            return entries.entrySet();
        }

        public Object get(Object key) {
            if (null == key) {
                return null;
            }
            Object result;
            try {
                result = getBundle().getObject(key.toString());
            } catch (MissingResourceException mre) {
                result = "???" + key + "???";
            }
            return result;
        }

        public boolean isEmpty() {
            return !getBundle().getKeys().hasMoreElements();
        }

        public Set keySet() {
            return getBundle().keySet();
        }

        public Object put(Object key, Object value) {
            throw new UnsupportedOperationException();
        }

        public void putAll(Map t) {
            throw new UnsupportedOperationException();
        }

        public Object remove(Object key) {
            throw new UnsupportedOperationException();
        }

        public int size() {
            int size = 0;
            Enumeration keys = getBundle().getKeys();
            while (keys.hasMoreElements()) {
                keys.nextElement();
                size++;
            }
            return size;
        }

        public Collection values() {
            ArrayList values = new ArrayList();
            ResourceBundle resourceBundle = getBundle();
            Enumeration keys = resourceBundle.getKeys();
            while (keys.hasMoreElements()) {
                values.add(resourceBundle.getObject((String) keys.nextElement()));
            }
            return values;
        }

        public int hashCode() {
            return getBundle().hashCode();
        }
    }
}
