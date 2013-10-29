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

package org.icefaces.ace.component.rowexpansion;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Expression;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.baseMeta.UIComponentBaseMeta;

import javax.el.MethodExpression;


@Component(
        tagName = "rowExpansion",
        componentClass = "org.icefaces.ace.component.rowexpansion.RowExpansion",
        generatedClass = "org.icefaces.ace.component.rowexpansion.RowExpansionBase",
        extendsClass = "javax.faces.component.UIComponentBase",
        componentType = "org.icefaces.ace.component.RowExpansion",
        componentFamily = "org.icefaces.ace.RowExpansion",
        tlddoc = "<p>Renders a set of rows located underneath the row where ace:expansionToggler was activated. These subrows are defined by a tree relationship modeled in the List bound to the 'value' attribute of the parent DataTable. The List must be of the type List<java.util.Map.Entry<Object, List>> where the entries are mappings from row data objects to lists of sub-entries.</p>" +
                 "<p>For more information, see the <a href=\"http://wiki.icefaces.org/display/ICE/RowExpansion\">Row Expander Wiki Documentation</a>.</p>"
)
public class RowExpansionMeta extends UIComponentBaseMeta {}
