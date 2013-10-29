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

package org.icefaces.ace.component.expansiontoggler;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Expression;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.annotation.Field;
import org.icefaces.ace.meta.baseMeta.UIComponentBaseMeta;

import javax.el.MethodExpression;

@Component(
    tagName = "expansionToggler",
    componentClass = "org.icefaces.ace.component.expansiontoggler.ExpansionToggler",
    generatedClass = "org.icefaces.ace.component.expansiontoggler.ExpansionTogglerBase",
    rendererClass = "org.icefaces.ace.component.expansiontoggler.ExpansionTogglerRenderer",
    extendsClass = "javax.faces.component.UIComponentBase",
    componentType = "org.icefaces.ace.component.ExpansionToggler",
    rendererType = "org.icefaces.ace.component.ExpansionTogglerRenderer",
    componentFamily = "org.icefaces.ace.ExpansionToggler",
    tlddoc = "<p>Renders a control to toggle the expanded state of this row. " +
            "If this table only contains either PanelExpansion or " +
            "RowExpansion, that type of expansion will occur. If both are " +
            "children of the table, the 'expansion type' for each row is " +
            "configurable via the RowState.</p>" +
            "<p>For more information, see the <a href=\"http://wiki.icefaces.org/display/ICE/ExpansionToggler\">Expansion Toggler Wiki Documentation</a>.</p>"
)
public class ExpansionTogglerMeta extends UIComponentBaseMeta {
    @Property(expression = Expression.METHOD_EXPRESSION,
            methodExpressionArgument = "org.icefaces.ace.event.ExpansionChangeEvent",
            tlddoc = "MethodExpression reference called whenever a row " +
                    "element is expanded. The method receives a single " +
                    "argument, ExpansionChangeEvent.")
    private MethodExpression changeListener;

    @Field(defaultValue = "false", defaultValueIsStringLiteral = false)
    private Boolean toggled;
}
