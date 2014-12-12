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

package org.icefaces.ace.component.column;

import org.icefaces.ace.component.datatable.DataTable;

import javax.el.ValueExpression;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.Comparator;

/**
 * This allows key properties to be set on the corresponding header column,
 * and used as if it was set on the body column.
 */
public class ProxiedBodyColumn implements IProxiableColumn{
    private Column headerColumn;
    private Column bodyColumn;

    public ProxiedBodyColumn(Column headerColumn, Column bodyColumn) {
        this.headerColumn = headerColumn;
        this.bodyColumn = bodyColumn;
    }

    //////// First, the completely non-proxied properties ////////

    public String getId() {
        return bodyColumn.getId();
    }

    public void encodeAll(FacesContext context) throws IOException {
        bodyColumn.encodeAll(context);
    }

    public DataTable findParentTable() {
        return bodyColumn.findParentTable();
    }

    public boolean isOddGroup() {
        return bodyColumn.isOddGroup();
    }

    public void setOddGroup(boolean oddGroup) {
        bodyColumn.setOddGroup(oddGroup);
    }

    public boolean isSortWhenGrouping() {
        return bodyColumn.isSortWhenGrouping();
    }

    public void setSortWhenGrouping(boolean sortWhenGrouping) {
        bodyColumn.setSortWhenGrouping(sortWhenGrouping);
    }

    public boolean isHideSortControls() {
        return bodyColumn.isHideSortControls();
    }

    public void setHideSortControls(boolean hideSortControls) {
        bodyColumn.setHideSortControls(hideSortControls);
    }

    public int getCurrGroupLength() {
        return bodyColumn.getCurrGroupLength();
    }

    public void setCurrGroupLength(int currGroupLength) {
        bodyColumn.setCurrGroupLength(currGroupLength);
    }

    public boolean hasCellEditor() {
        return bodyColumn.hasCellEditor();
    }

    public void setColspan(int colspan) {
        bodyColumn.setColspan(colspan); //isPropertySpecified(bodyColumn, "colspan") ? bodyColumn : headerColumn);.setColspan(colspan);
    }

    public int getColspan() {
        return bodyColumn.getColspan(); //(isPropertySpecified(bodyColumn, "colspan") ? bodyColumn : headerColumn).getColspan();
    }

    public void setRowspan(int rowspan) {
        bodyColumn.setRowspan(rowspan); //isPropertySpecified(bodyColumn, "rowspan") ? bodyColumn : headerColumn);.setRowspan(rowspan);
    }

    public int getRowspan() {
        return bodyColumn.getRowspan(); //(isPropertySpecified(bodyColumn, "rowspan") ? bodyColumn : headerColumn).getRowspan();
    }

    //TODO if stacked changes, possibly it should apply to both
    public void setStacked(boolean stacked) {
        bodyColumn.setStacked(stacked);
    }

    public boolean isStacked() {
        return bodyColumn.isStacked();
    }

    public void setStyle(String style) {
        bodyColumn.setStyle(style);
    }

    public String getStyle() {
        return bodyColumn.getStyle();
    }

    public void setStyleClass(String styleClass) {
        bodyColumn.setStyleClass(styleClass);
    }

    public String getStyleClass() {
        return bodyColumn.getStyleClass();
    }


    //////// Next, the proxied properties ////////

    public boolean isRendered() {
        return (bodyColumn.isLikelySpecifiedRendered() ? bodyColumn : headerColumn).isRendered();
    }

    public ValueExpression getValueExpression(String name) {
        ValueExpression ve = bodyColumn.getValueExpression(name);
        if (ve == null) {
            ve = headerColumn.getValueExpression(name);
        }
        return ve;
    }

    public void setConfigurable(boolean configurable) {
        (isPropertySpecified(bodyColumn, "configurable") ? bodyColumn : headerColumn).setConfigurable(configurable);
    }

    public boolean isConfigurable() {
        return (isPropertySpecified(bodyColumn, "configurable") ? bodyColumn : headerColumn).isConfigurable();
    }

    public void setFilterBy(Object filterBy) {
        (isPropertySpecified(bodyColumn, "filterBy") ? bodyColumn : headerColumn).setFilterBy(filterBy);
    }

    public Object getFilterBy() {
        return (isPropertySpecified(bodyColumn, "filterBy") ? bodyColumn : headerColumn).getFilterBy();
    }

    public void setFilterMatchMode(String filterMatchMode) {
        (isPropertySpecified(bodyColumn, "filterMatchMode") ? bodyColumn : headerColumn).setFilterMatchMode(filterMatchMode);
    }

    public String getFilterMatchMode() {
        return (isPropertySpecified(bodyColumn, "filterMatchMode") ? bodyColumn : headerColumn).getFilterMatchMode();
    }

    public void setFilterOptions(Object filterOptions) {
        (isPropertySpecified(bodyColumn, "filterOptions") ? bodyColumn : headerColumn).setFilterOptions(filterOptions);
    }

    public Object getFilterOptions() {
        return (isPropertySpecified(bodyColumn, "filterOptions") ? bodyColumn : headerColumn).getFilterOptions();
    }

    public void setFilterStyle(String filterStyle) {
        (isPropertySpecified(bodyColumn, "filterStyle") ? bodyColumn : headerColumn).setFilterStyle(filterStyle);
    }

    public String getFilterStyle() {
        return (isPropertySpecified(bodyColumn, "filterStyle") ? bodyColumn : headerColumn).getFilterStyle();
    }

    public void setFilterStyleClass(String filterStyleClass) {
        (isPropertySpecified(bodyColumn, "filterStyleClass") ? bodyColumn : headerColumn).setFilterStyleClass(filterStyleClass);
    }

    public String getFilterStyleClass() {
        return (isPropertySpecified(bodyColumn, "filterStyleClass") ? bodyColumn : headerColumn).getFilterStyleClass();
    }

    public void setFilterValue(String filterValue) {
        (isPropertySpecified(bodyColumn, "filterValue") ? bodyColumn : headerColumn).setFilterValue(filterValue);
    }

    public String getFilterValue() {
        return (isPropertySpecified(bodyColumn, "filterValue") ? bodyColumn : headerColumn).getFilterValue();
    }

    public void setFooterText(String footerText) {
        (isPropertySpecified(bodyColumn, "footerText") ? bodyColumn : headerColumn).setFooterText(footerText);
    }

    public String getFooterText() {
        return (isPropertySpecified(bodyColumn, "footerText") ? bodyColumn : headerColumn).getFooterText();
    }

    public void setGroupBy(Object groupBy) {
        (isPropertySpecified(bodyColumn, "groupBy") ? bodyColumn : headerColumn).setGroupBy(groupBy);
    }

    public Object getGroupBy() {
        return (isPropertySpecified(bodyColumn, "groupBy") ? bodyColumn : headerColumn).getGroupBy();
    }

    public void setHeaderText(String headerText) {
        (isPropertySpecified(bodyColumn, "headerText") ? bodyColumn : headerColumn).setHeaderText(headerText);
    }

    public String getHeaderText() {
        return (isPropertySpecified(bodyColumn, "headerText") ? bodyColumn : headerColumn).getHeaderText();
    }

    public void setPinningOrder(Integer pinningOrder) {
        (isPropertySpecified(bodyColumn, "pinningOrder") ? bodyColumn : headerColumn).setPinningOrder(pinningOrder);
    }

    public Integer getPinningOrder() {
        return (isPropertySpecified(bodyColumn, "pinningOrder") ? bodyColumn : headerColumn).getPinningOrder();
    }

    public void setReorderable(boolean reorderable) {
        (isPropertySpecified(bodyColumn, "reorderable") ? bodyColumn : headerColumn).setReorderable(reorderable);
    }

    public boolean isReorderable() {
        return (isPropertySpecified(bodyColumn, "reorderable") ? bodyColumn : headerColumn).isReorderable();
    }

    public void setSortAscending(Boolean sortAscending) {
        (isPropertySpecified(bodyColumn, "sortAscending") ? bodyColumn : headerColumn).setSortAscending(sortAscending);
    }

    public Boolean isSortAscending() {
        return (isPropertySpecified(bodyColumn, "sortAscending") ? bodyColumn : headerColumn).isSortAscending();
    }

    public void setSortBy(Object sortBy) {
        (isPropertySpecified(bodyColumn, "sortBy") ? bodyColumn : headerColumn).setSortBy(sortBy);
    }

    public Object getSortBy() {
        return (isPropertySpecified(bodyColumn, "sortBy") ? bodyColumn : headerColumn).getSortBy();
    }

    public void setSortFunction(Comparator sortFunction) {
        (isPropertySpecified(bodyColumn, "sortFunction") ? bodyColumn : headerColumn).setSortFunction(sortFunction);
    }

    public Comparator getSortFunction() {
        return (isPropertySpecified(bodyColumn, "sortFunction") ? bodyColumn : headerColumn).getSortFunction();
    }

    public void setSortPriority(Integer sortPriority) {
        (isPropertySpecified(bodyColumn, "sortPriority") ? bodyColumn : headerColumn).setSortPriority(sortPriority);
    }

    public Integer getSortPriority() {
        return (isPropertySpecified(bodyColumn, "sortPriority") ? bodyColumn : headerColumn).getSortPriority();
    }

    private static boolean isPropertySpecified(Column column, String propertyName) {
        return column.isPropertySet(propertyName);
    }

    public void setLazyColumnKey(String lazyColumnKey) {
        (isPropertySpecified(bodyColumn, "lazyColumnKey") ? bodyColumn : headerColumn).setLazyColumnKey(lazyColumnKey);
    }

    public String getLazyColumnKey() {
        return (isPropertySpecified(bodyColumn, "lazyColumnKey") ? bodyColumn : headerColumn).getLazyColumnKey();
    }
}
