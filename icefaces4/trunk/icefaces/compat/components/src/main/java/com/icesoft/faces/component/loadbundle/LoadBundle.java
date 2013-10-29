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

package com.icesoft.faces.component.loadbundle;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;

import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

import com.icesoft.faces.utils.MessageUtils;
import org.icefaces.resources.BrowserType;
import org.icefaces.resources.ICEResourceDependencies;
import org.icefaces.resources.ICEResourceDependency;

@ICEResourceDependencies({
        @ICEResourceDependency(name="icefaces-compat.js", library="ice.compat",target="head", browser= BrowserType.ALL, browserOverride={}),
        @ICEResourceDependency(name="compat.js", library="ice.compat",target="head", browser=BrowserType.ALL, browserOverride={})
})
public class LoadBundle extends UIOutput implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String COMPONENT_TYPE = "com.icesoft.faces.LoadBundle";
    public static final String COMPONENT_FAMILY = "com.icesoft.faces.LoadBundle";
    private String basename;
    private String var;
    transient private Locale oldLocale;
    transient private String oldBaseName = "";
    transient private ResourceBundle bundle;
    private Map map = new SerializableMap();

    public LoadBundle() {
        setRendererType(null);
    }

    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public String getComponentType() {
        return COMPONENT_TYPE;
    }

    public void decode(FacesContext context) {
        Map requestMap = context.getExternalContext().getRequestMap();
        requestMap.put(var, map);
    }

    public void encodeBegin(FacesContext context) throws IOException {
        setRendererType(null);
        super.encodeBegin(context);
        updateBundle();
    }

    private ResourceBundle getBundle() {
        if (bundle == null) {
            updateBundle();
        }

        return bundle;
    }

    public void setBasename(String basename) {
        this.basename = basename;
    }

    public String getBasename() {
        if (basename != null) {
            return basename;
        }
        ValueBinding vb = getValueBinding("basename");
        return vb != null ? (String) vb.getValue(getFacesContext()) : null;
    }

    public String getVar() {
        return var;
    }

    public void setVar(String var) {
        this.var = var;
    }

    private transient Object values[];

    /**
     * <p>Gets the state of the instance as a <code>Serializable</code>
     * Object.</p>
     */
    public Object saveState(FacesContext context) {
        if (values == null) {
            values = new Object[4];
        }
        values[0] = super.saveState(context);
        values[1] = basename;
        values[2] = var;
        values[3] = map;
        return ((Object) (values));
    }

    /**
     * <p>Perform any processing required to restore the state from the entries
     * in the state Object.</p>
     */
    public void restoreState(FacesContext context, Object state) {
        Object values[] = (Object[]) state;
        super.restoreState(context, values[0]);
        basename = (String) values[1];
        var = (String) values[2];
        map = (Map) values[3];
    }

    private void updateBundle() {
        String newBaseName = getBasename();
        FacesContext context = getFacesContext();
        Locale currentLocale = context.getViewRoot().getLocale();
        boolean reloadRequired =
                oldLocale == null || !oldLocale.getLanguage().equals(currentLocale.getLanguage()) ||
                        !oldBaseName.equals(newBaseName);
        if (reloadRequired) {
            bundle = ResourceBundle.getBundle(newBaseName.trim(), currentLocale, MessageUtils.getClassLoader(this));
            map = new SerializableMap();
            context.getExternalContext().getRequestMap().put(getVar(), map);
            oldBaseName = newBaseName;
            oldLocale = currentLocale;
        }
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
            Object currentValue = null;
            Enumeration keys = getBundle().getKeys();
            while (keys.hasMoreElements()) {
                currentValue = getBundle().getObject((String) keys.nextElement());
                if ((value == currentValue) ||
                        ((null != currentValue) && currentValue.equals(value))) {
                    found = true;
                    break;
                }
            }
            return found;
        }

        public Set entrySet() {
            HashMap entries = new HashMap();
            Enumeration keys = getBundle().getKeys();
            while (keys.hasMoreElements()) {
                Object key = keys.nextElement();
                Object value = getBundle().getObject((String) key);
                entries.put(key, value);
            }
            return entries.entrySet();
        }

        public Object get(Object key) {
            if (null == key) return null;
            Object result = null;
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
            Set keySet = new HashSet();
            Enumeration keys = getBundle().getKeys();
            while (keys.hasMoreElements()) {
                keySet.add(keys.nextElement());
            }
            return keySet;
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
            Enumeration keys = getBundle().getKeys();
            while (keys.hasMoreElements()) {
                values.add(getBundle().getObject((String) keys.nextElement()));
            }
            return values;
        }

        public int hashCode() {
            return getBundle().hashCode();
        }
    }
}
