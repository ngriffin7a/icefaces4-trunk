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

package org.icefaces.ace.component.row;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.DefaultValueType;
import org.icefaces.ace.meta.annotation.Expression;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.baseMeta.UIComponentBaseMeta;
import org.icefaces.ace.util.collections.Predicate;

import javax.el.ValueExpression;

@Component(
        tagName = "row",
        componentClass = "org.icefaces.ace.component.row.Row",
        generatedClass = "org.icefaces.ace.component.row.RowBase",
        extendsClass = "javax.faces.component.UIComponentBase",
        componentType = "org.icefaces.ace.component.Row",
        componentFamily = "org.icefaces.ace.Row",
        tlddoc = "<p>Renders a set of ace:column components as one row of the header or footer segment of an ace:dataTable via an ace:columnGroup.</p>" +
                 "<p>For more information, see the <a href=\"http://wiki.icefaces.org/display/ICE/Row\">Row Wiki Documentation</a>.</p>"
)
public class RowMeta extends UIComponentBaseMeta {
    @Property(tlddoc = "Defines a mode of operation that determines under what type of condition ths row will be " +
            "rendered. Options include 'interval', 'predicate' and 'group'.")
    private String condition;

    @Property(tlddoc = "Defines an integer that determines how regularly this row will render in interval mode.")
    private Integer interval;
    
    @Property(tlddoc = "Defines whether a conditional row is intended to render 'before' or 'after' a group change " +
            "in 'group' conditional mode. If in predicate or interval mode, this setting defines if the conditional " +
            "row renders before or after the data model position that caused the predicate to evaluate true.",
            defaultValue = "after", defaultValueType = DefaultValueType.STRING_LITERAL)
    private String pos;

    @Property(tlddoc = "When using 'predicate' conditional mode, defines a function taking the current data model index " +
            "as an argument, and returning a value determining whether or not to render the conditional row for this index.")
    private Predicate predicate;

    @Property(expression = Expression.VALUE_EXPRESSION, tlddoc="Defines an expression used to determine when a group " +
            "change is occurring in 'group' conditional mode.")
    private Object groupBy;
    
    @Property(tlddoc = "Define style classes for this row. Only applicable to conditional rows.")
    private String styleClass;

    @Property(tlddoc = "Define inline style rules for this row. Only applicable to conditional rows.")
    private String style;
}
