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

package com.icesoft.faces.component.menubar;

import com.icesoft.faces.component.ext.taglib.Util;
import com.icesoft.faces.util.CoreUtils;
import org.icefaces.resources.BrowserType;
import org.icefaces.resources.ICEResourceDependencies;
import org.icefaces.resources.ICEResourceDependency;


/**
 *
 */
@ICEResourceDependencies({
        @ICEResourceDependency(name="icefaces-compat.js", library="ice.compat",target="head", browser= BrowserType.ALL, browserOverride={}),
        @ICEResourceDependency(name="compat.js", library="ice.compat",target="head", browser=BrowserType.ALL, browserOverride={})
})
public class MenuItemCheckbox extends MenuItem {


    protected static String DEFAULT_ICON_CHECKED =
            "/xmlhttp/css/xp/css-images/menu_checkbox_selected.gif";
    protected static String DEFAULT_ICON_UNCHECKED =
            "/xmlhttp/css/xp/css-images/menu_checkbox.gif";

    private boolean selected = false;


    /**
     * <p>Return the value of the <code>COMPONENT_FAMILY</code> of this
     * component.</p>
     */
    public String getFamily() {
        return "com.icesoft.faces.MenuNodeCheckbox";
    }

    /**
     * <p>Return the value of the <code>selected</code> property.</p>
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * <p>Set the value of the <code>selected</code> property.</p>
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    /**
     * <p>Return the value of the <code>unselectedIcon</code> property.</p>
     */
    public String getUnselectedIcon() {
        return CoreUtils.resolveResourceURL(getFacesContext(), DEFAULT_ICON_UNCHECKED);
    }

    /**
     * <p>Return the value of the <code>selectedIcon</code> property.</p>
     */
    public String getSelectedIcon() {
        return CoreUtils.resolveResourceURL(getFacesContext(), DEFAULT_ICON_CHECKED);
    }
}
