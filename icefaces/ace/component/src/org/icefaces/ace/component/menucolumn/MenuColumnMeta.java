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

package org.icefaces.ace.component.menucolumn;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.baseMeta.UIComponentBaseMeta;

@Component(
        tagName = "menuColumn",
        componentClass = "org.icefaces.ace.component.menucolumn.MenuColumn",
        generatedClass = "org.icefaces.ace.component.menucolumn.MenuColumnBase",
        extendsClass = "javax.faces.component.UIComponentBase",
        componentFamily = "org.icefaces.ace.component.Menu",
        componentType = "org.icefaces.ace.component.MenuColumn",
        tlddoc = "MenuColumn is nested in a MultiColumnMenu component and represents a column of submenus and menu items." +
                 "<p>For more information, see the " +
                 "<a href=\"http://wiki.icefaces.org/display/ICE/MenuColumn\">MenuColumn Wiki Documentation</a>."
)
public class MenuColumnMeta extends UIComponentBaseMeta {

    @Property(tlddoc = "Width of the column in pixels.", defaultValue="200")
    private int width;

    @Property(tlddoc = "Maximum number of items in this column. If there are more items than the maximum, new columns will be created as necessary. When the value is 0, there is no limit.", defaultValue="0")
    private int autoflow;	
}
