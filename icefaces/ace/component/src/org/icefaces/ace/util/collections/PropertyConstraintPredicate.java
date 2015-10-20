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

package org.icefaces.ace.util.collections;

import org.icefaces.ace.component.column.Column;
import org.icefaces.ace.component.column.ColumnType;
import org.icefaces.ace.model.filter.FilterConstraint;

import javax.el.ELResolver;
import javax.el.ValueExpression;
import javax.faces.context.FacesContext;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;

public class PropertyConstraintPredicate implements Predicate {
    ValueExpression filterBy;
    String filterValue;
    FilterConstraint filterConstraint;
    FacesContext facesContext;
	Column column;

    public PropertyConstraintPredicate(FacesContext context, ValueExpression filterBy, String filterValue, FilterConstraint constraint, Column column) {
        this.filterValue = filterValue;
        this.filterConstraint = constraint;
        this.facesContext = context;
        this.filterBy = filterBy;
		this.column = column;
    }

    public boolean evaluate(Object object) {
        Object value = filterBy.getValue(facesContext.getELContext());

		if (this.column.getColumnType() == ColumnType.TEXT) {
			if (value instanceof Date) {
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				value = dateFormat.format(value);
			}

			if (value != null)
				return filterConstraint.applies(value.toString(), filterValue);
			else if (filterValue != null && filterValue.length() > 0)
				return false;
			else
				return true;
		} else if (this.column.getColumnType() == ColumnType.BOOLEAN) {
			if (value != null) {
				if ("true".equalsIgnoreCase(filterValue)) {
					return (Boolean) value == true;
				} else {
					return (Boolean) value == false;
				}
			} else if (filterValue != null)
				return false;
			else
				return true;			
		} else if (this.column.getColumnType() == ColumnType.DATE) {
			Date filterDate = null;
			DateFormat dateFormat = new SimpleDateFormat(this.column.getFilterDatePattern(), 
				this.column.calculateLocale(this.facesContext));
			try {
				if (filterValue != null && !"".equals(filterValue)) filterDate = dateFormat.parse(filterValue);
			} catch (ParseException e) {
				filterDate = null;
			}

			if (value instanceof Date) {
				if (value != null && filterDate != null) {
					String valueString = dateFormat.format(value);
					String filterString = dateFormat.format(filterDate);
					return valueString.equals(filterString);
				} else if (filterDate != null)
					return false;
				else
					return true;
			} else {
				return false;
			}
		} else if (this.column.getColumnType() == ColumnType.BYTE) {
			Byte convertedValue = null;
			Byte convertedFilterValue = null;
			try {
				if (value != null && !"".equals(value)) convertedValue = 
					new Byte(Byte.parseByte(value.toString()));
				if (filterValue != null && !"".equals(filterValue)) convertedFilterValue = 
					new Byte(Byte.parseByte(filterValue));
			} catch (NumberFormatException e) {
				convertedValue = null;
				convertedFilterValue = null;
			}

			if (convertedValue != null && convertedFilterValue != null) {
				return convertedValue.byteValue() == convertedFilterValue.byteValue();
			} else if (convertedFilterValue != null)
				return false;
			else
				return true;
		} else if (this.column.getColumnType() == ColumnType.SHORT) {
			Short convertedValue = null;
			Short convertedFilterValue = null;
			try {
				if (value != null && !"".equals(value)) convertedValue = 
					new Short(Short.parseShort(value.toString()));
				if (filterValue != null && !"".equals(filterValue)) convertedFilterValue = 
					new Short(Short.parseShort(filterValue));
			} catch (NumberFormatException e) {
				convertedValue = null;
				convertedFilterValue = null;
			}

			if (convertedValue != null && convertedFilterValue != null) {
				return convertedValue.shortValue() == convertedFilterValue.shortValue();
			} else if (convertedFilterValue != null)
				return false;
			else
				return true;
		} else if (this.column.getColumnType() == ColumnType.INT) {
			Integer convertedValue = null;
			Integer convertedFilterValue = null;
			try {
				if (value != null && !"".equals(value)) convertedValue = 
					new Integer(Integer.parseInt(value.toString()));
				if (filterValue != null && !"".equals(filterValue)) convertedFilterValue = 
					new Integer(Integer.parseInt(filterValue));
			} catch (NumberFormatException e) {
				convertedValue = null;
				convertedFilterValue = null;
			}

			if (convertedValue != null && convertedFilterValue != null) {
				return convertedValue.intValue() == convertedFilterValue.intValue();
			} else if (convertedFilterValue != null)
				return false;
			else
				return true;
		} else if (this.column.getColumnType() == ColumnType.LONG) {
			Long convertedValue = null;
			Long convertedFilterValue = null;
			try {
				if (value != null && !"".equals(value)) convertedValue = 
					new Long(Long.parseLong(value.toString()));
				if (filterValue != null && !"".equals(filterValue)) convertedFilterValue = 
					new Long(Long.parseLong(filterValue));
			} catch (NumberFormatException e) {
				convertedValue = null;
				convertedFilterValue = null;
			}

			if (convertedValue != null && convertedFilterValue != null) {
				return convertedValue.longValue() == convertedFilterValue.longValue();
			} else if (convertedFilterValue != null)
				return false;
			else
				return true;
		} else if (this.column.getColumnType() == ColumnType.FLOAT) {
			Float convertedValue = null;
			Float convertedFilterValue = null;
			try {
				if (value != null && !"".equals(value)) convertedValue = 
					new Float(Float.parseFloat(value.toString()));
				if (filterValue != null && !"".equals(filterValue)) convertedFilterValue = 
					new Float(Float.parseFloat(filterValue));
			} catch (NumberFormatException e) {
				convertedValue = null;
				convertedFilterValue = null;
			}

			if (convertedValue != null && convertedFilterValue != null) {
				return convertedValue.floatValue() == convertedFilterValue.floatValue();
			} else if (convertedFilterValue != null)
				return false;
			else
				return true;
		} else if (this.column.getColumnType() == ColumnType.DOUBLE) {
			Double convertedValue = null;
			Double convertedFilterValue = null;
			try {
				if (value != null && !"".equals(value)) convertedValue = 
					new Double(Double.parseDouble(value.toString()));
				if (filterValue != null && !"".equals(filterValue)) convertedFilterValue = 
					new Double(Double.parseDouble(filterValue));
			} catch (NumberFormatException e) {
				convertedValue = null;
				convertedFilterValue = null;
			}

			if (convertedValue != null && convertedFilterValue != null) {
				return convertedValue.doubleValue() == convertedFilterValue.doubleValue();
			} else if (convertedFilterValue != null)
				return false;
			else
				return true;
		}

		return true;
    }
}
