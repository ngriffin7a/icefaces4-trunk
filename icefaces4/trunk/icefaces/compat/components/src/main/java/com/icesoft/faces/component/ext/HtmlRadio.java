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

package com.icesoft.faces.component.ext;

import org.icefaces.resources.BrowserType;
import org.icefaces.resources.ICEResourceDependencies;
import org.icefaces.resources.ICEResourceDependency;

import javax.faces.component.UIComponentBase;
import javax.faces.el.ValueBinding;
import javax.faces.context.FacesContext;

@ICEResourceDependencies({
	@ICEResourceDependency(name="icefaces-compat.js", library="ice.compat",target="head", browser=BrowserType.ALL, browserOverride={}),
	@ICEResourceDependency(name="compat.js", library="ice.compat",target="head", browser=BrowserType.ALL, browserOverride={})
})
public class HtmlRadio extends UIComponentBase {
    private String _for = null;
    private Integer _index = null;

    public HtmlRadio() {
        super();
        setRendererType(HtmlSelectOneRadio.RENDERER_TYPE);
    }

    public String getFamily() {
        return "com.icesoft.faces.HtmlRadio";
    }

    public void setFor(String forValue) {
        _for = forValue;
    }

    public String getFor() {
        if (_for != null) return _for;
        ValueBinding vb = getValueBinding("for");
        if (vb == null) return null;
        Object value = vb.getValue(getFacesContext());
        if (value == null) return null;
        return value.toString();
    }

    public void setIndex(int index) {
        _index = new Integer(index);
    }

    public int getIndex() {
        if (_index != null) return _index.intValue();
        ValueBinding vb = getValueBinding("index");
        Number v = vb != null ? (Number) vb.getValue(getFacesContext()) : null;
        return v != null ? v.intValue() : Integer.MIN_VALUE;
    }

    public Object saveState(FacesContext context) {
        Object values[] = new Object[3];
        values[0] = super.saveState(context);
        values[1] = _for;
        values[2] = _index;
        return values;
    }

    public void restoreState(FacesContext context, Object state) {
        Object values[] = (Object[]) state;
        super.restoreState(context, values[0]);
        _for = (String) values[1];
        _index = (Integer) values[2];
    }
}
