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

package org.icefaces.ace.model.table;

import org.icefaces.ace.component.column.Column;
import org.icefaces.ace.util.ComponentUtils;

import javax.el.ValueExpression;
import java.util.Comparator;

public class SortCriteria {
    private Comparator<Object> comparator;
    private ValueExpression expression;
	private String propertyName;
	private boolean ascending;

	
	public SortCriteria(ValueExpression expression, boolean ascending) {
		this.expression = expression;
		this.ascending = ascending;
        this.propertyName = ComponentUtils.resolveField(expression);
	}

    public SortCriteria(ValueExpression expression, boolean ascending, Comparator<Object> comparator) {
        this.expression = expression;
        this.ascending = ascending;
        this.propertyName = ComponentUtils.resolveField(expression);
        this.comparator = comparator;
    }

	public String getPropertyName() {
		return propertyName;
    }
	
	public boolean isAscending() {
		return this.ascending;
	}
	
	public void setAscending(boolean ascending) {
		this.ascending = ascending;
	}

    public ValueExpression getExpression() {
        return expression;
    }

    public void setExpression(ValueExpression expression) {
        this.expression = expression;
    }

    public Comparator<Object> getComparator() {
        return comparator;
    }

    public void setComparator(Comparator<Object> comparator) {
        this.comparator = comparator;
    }
}