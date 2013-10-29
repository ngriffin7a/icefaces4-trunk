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

package org.icefaces.ace.component.column;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.DefaultValueType;
import org.icefaces.ace.meta.annotation.Expression;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.baseMeta.UIColumnMeta;

import org.icefaces.resources.ICEResourceDependencies;
import java.util.Comparator;

@Component(
        tagName         = "column",
        componentClass  = "org.icefaces.ace.component.column.Column",
        generatedClass  = "org.icefaces.ace.component.column.ColumnBase",
        extendsClass    = "javax.faces.component.UIColumn",
        componentType   = "org.icefaces.ace.component.Column",
        rendererType    = "",
		componentFamily = "org.icefaces.ace.component",
		tlddoc = "<p>Component that represents a column in an ace:dataTable.</p>" +
                 "<p>For more information, see the <a href=\"http://wiki.icefaces.org/display/ICE/Column\">Column Wiki Documentation</a>.</p>")
public class ColumnMeta extends UIColumnMeta {

    @Property(tlddoc = "Custom inline CSS styles to use for this component. These styles are generally applied to the root DOM element of the component. This is intended for per-component basic style customizations. Note that due to browser CSS precedence rules, CSS rendered on a DOM element will take precedence over the external stylesheets used to provide the ThemeRoller theme on this component. If the CSS properties applied with this attribute do not affect the DOM element you want to style, you may need to create a custom theme styleClass for the theme CSS class that targets the particular DOM elements you wish to customize.")
	private String style;

    @Property(tlddoc = "Custom CSS style class(es) to use for this component. These style classes can be defined in your page or in a theme CSS file.")
	private String styleClass;

//    @Property(expression = Expression.VALUE_EXPRESSION,
//            tlddoc = "Defines a value expression representing the value of the column per row, " +
//            "optionally superseded by the more specific value definitions like sortBy, filterBy etc.")
//    private Object `value;

    @Property(expression = Expression.VALUE_EXPRESSION,
            tlddoc="Defines a value expression representing the value of this column per row when determining " +
                   "if the values of this column in sibling rows are equal. If they are equal, span the rows with" +
                   "a single column cell.")
    private Object groupBy;

	@Property(expression = Expression.VALUE_EXPRESSION,
            tlddoc="Defines a value expression representing the value of this column per row when sorting. " +
                   "Setting this attribute, or the \"value\" attribute for a column enables sorting.")
	private Object sortBy;
	
	@Property(expression = Expression.VALUE_EXPRESSION,
            tlddoc="Defines an alternate method of sorting. Sort this column using a " +
            "Comparator<Object> object that takes the sortBy values of this column as input.")
	private Comparator sortFunction;

	@Property(expression = Expression.VALUE_EXPRESSION,
            tlddoc="Defines a ValueExpression of the value of this row to use when filtering this column. " +
                   "Setting this attribute, or the \"value\" attribute for a column enables filtering.")
	private Object filterBy;

    @Property(tlddoc="Defines the string input filtering this column, coming from the client, or from " +
            "the application via a value binding.")
    private String filterValue;
    
	@Property(tlddoc="Defines additional CSS rules to be applied to the filter text input.")
	private String filterStyle;
	
	@Property(tlddoc="Defines supplementary CSS classes to add to those already " +
            "applied on this component.")
	private String filterStyleClass;
	
	@Property(tlddoc="Defines a collection of SelectItem objects for use as filter choices.")
	private Object filterOptions;
	
	@Property(tlddoc="Defines the method of filter comparison used, default is \"startsWith\". " +
            "Types available include: \"contains\", \"exact\", \"startsWith\" and \"endsWith\".", defaultValue="startsWith")
	private String filterMatchMode;
	
	@Property(tlddoc="Defines the number of rows the rendered cell spans. Only " +
            "significant to Column components within a column group.", defaultValue="1")
	private int rowspan;
	
	@Property(tlddoc="Defines the number of columns the rendered cell spans. Only " +
            "significant to Column components within a column group.", defaultValue="1")
	private int colspan;
	
	@Property(tlddoc="Defines a plain text header with less effort than adding a facet. Note that even when using a header facet this property should also be set as a short-form label for the column that will be used within the ace:tableConfigPanel.")
	private String headerText;
	
	@Property(tlddoc="Defines a  plain text footer with less effort than adding a facet.")
	private String footerText;

    @Property(tlddoc="When enabled, this column is rendered underneath the previous column.")
    private boolean stacked;

    @Property(tlddoc="When disabled, this column is excluded from the list of columns available" +
            " for configuration on a TableConfigPanel component.",
            defaultValue = "true", defaultValueType = DefaultValueType.EXPRESSION)
    private boolean configurable;

    @Property(tlddoc="Defines the priority of a column during sorting. The column " +
            "priorities incoming from the client during a sort request overwrite any " +
            "set by the application. Processing the sorted columns is done by the " +
            "component whenever a client edits a sort control or the application calls table.applySorting().")
    private Integer sortPriority;

    @Property(tlddoc="Defines the direction of column values during sorting. " +
            "The column directions incoming from the client during a sort request " +
            "overwrite any set by the application. Processing the sorted columns is " +
            "done by the component whenever a client edits a sort control or the " +
            "application calls table.applySorting().",
            defaultValue = "false", defaultValueType = DefaultValueType.EXPRESSION)
    private Boolean sortAscending;

    @Property(tlddoc="Enables per-column control of column ordering when the " +
            "attribute (\"reorderableColumns\") is true at the table level.",
            defaultValue = "true", defaultValueType = DefaultValueType.EXPRESSION)
    private boolean reorderable;

    @Property(tlddoc = "Define the position of this column in the pinned column " +
            "region when column pinning and scrolling are enabled at the table level. " +
            "Having a value in this field indicates a column is pinned. 1 indexed.")
    private Integer pinningOrder;

//    These per-feature configuration attributes will be used when the 'value' property is added to Column.
//    Until then they are redundant.
//    @Property(tlddoc="Enables per-column control of column sorting when either the " +
//            "attribute (\"sortBy\") or (\"value\") are set.",
//            defaultValue = "true", defaultValueType = DefaultValueType.EXPRESSION)
//    private boolean sortable;
//
//    @Property(tlddoc="Enables per-column control of column filtering when either the " +
//            "attribute (\"filterBy\") or (\"value\") are set.",
//            defaultValue = "true", defaultValueType = DefaultValueType.EXPRESSION)
//    private boolean filterable;
//
//    @Property(tlddoc="Enables per-column control of Cell selection when either the " +
//            "attribute (\"selectBy\") or (\"value\") are set.",
//            defaultValue = "true", defaultValueType = DefaultValueType.EXPRESSION)
//    private boolean selectable;
}
