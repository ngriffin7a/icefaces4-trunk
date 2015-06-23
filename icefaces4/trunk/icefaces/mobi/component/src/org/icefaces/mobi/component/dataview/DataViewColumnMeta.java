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

package org.icefaces.mobi.component.dataview;

import org.icefaces.ace.meta.annotation.*;
import org.icefaces.ace.meta.baseMeta.UIComponentBaseMeta;

import javax.faces.convert.Converter;

@Component(
        tagName = "dataViewColumn",
        componentClass = "org.icefaces.mobi.component.dataview.DataViewColumn",
        generatedClass = "org.icefaces.mobi.component.dataview.DataViewColumnBase",
        componentType = "org.icefaces.DataViewColumn",
        extendsClass = "javax.faces.component.UIComponentBase",
        componentFamily = "org.icefaces.DataViewColumn",
        tlddoc = "DataViewColumns defines a column in the table region of the DataView component."
)
public class DataViewColumnMeta extends UIComponentBaseMeta {
    @Property(tlddoc = "Define the text to render in the header of this column.")
    String headerText;

    @Property(tlddoc = "Define the text to render in the footer of this column.")
    String footerText;

    @Property(tlddoc = "Define if the column is sortable.", defaultValue = "true")
    boolean sortable;

    @Property(defaultValue = "org.icefaces.mobi.component.dataview.ColumnType.text",
              defaultValueType = DefaultValueType.EXPRESSION,
              tlddoc = "Define the ColumnType of this column, determining how the " +
                      "'value' attribute of this component will be rendered. Valid options " +
                      "include : text, bool, date, image.\n" +
                      "Text will render the String representation of the value object.\n" +
                      "Bool assumes the value object is a boolean type and renders a checkbox icon.\n"+
                      "Date assumes the value object is a Date object and renders according to the required f:convertDateTime child component.\n"+
                      "Image assume the value is a representation of an image URL.")
    ColumnType type;

    @Property(expression = Expression.VALUE_EXPRESSION,
              tlddoc = "Define a ValueExpression whose resulting Object will be iteratively rendered in the table " +
                      "region of the DataView according to the 'type' of this column.")
    Object value;

    @Property(tlddoc = "Sets the CSS class to apply to this component.")
    String styleClass;

    @Property(tlddoc = "Flag indicating that characters that are sensitive in HTML and XML markup must be escaped.", defaultValue = "true")
    boolean escape;

    @Field // MyFaces -  converter must be state saved, cannot cache at component instance level
    Converter converter;
}
