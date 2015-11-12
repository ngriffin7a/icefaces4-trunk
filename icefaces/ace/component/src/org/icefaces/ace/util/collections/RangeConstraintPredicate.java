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

import org.icefaces.ace.component.column.ColumnType;

import javax.el.ELResolver;
import javax.el.ValueExpression;
import javax.faces.context.FacesContext;

import java.util.Date;

public class RangeConstraintPredicate implements Predicate {

    FacesContext facesContext;
    ValueExpression filterBy;
    Object filterValueMin;
    Object filterValueMax;
	ColumnType type;

    public RangeConstraintPredicate(FacesContext context, ValueExpression filterBy, 
			Object filterValueMin, Object filterValueMax, ColumnType type) {
        this.facesContext = context;
        this.filterBy = filterBy;
		this.filterValueMin = filterValueMin;
		this.filterValueMax = filterValueMax;
		this.type = type;
    }

    public boolean evaluate(Object object) {

		Object value = filterBy.getValue(facesContext.getELContext());
		if (value != null) {
			if (type == ColumnType.DATE && value instanceof Date) {
				Date minDate = null;
				Date maxDate = null;
				Date rowDate = null;

				try {
					if (filterValueMin != null) minDate = (Date) filterValueMin;
					if (filterValueMax != null) maxDate = (Date) filterValueMax;
					rowDate = (Date) value;
				} catch (Exception e) {
					return false;
				}

				if (rowDate.equals(minDate) || rowDate.equals(maxDate)) {
					return true;
				} else if (minDate != null && maxDate == null) {
					return rowDate.after(minDate);
				} else if (minDate == null && maxDate != null) {
					maxDate = new Date(maxDate.getTime() + 86399999); // include all milliseconds of a day
					return rowDate.before(maxDate);
				} else if (minDate != null && maxDate != null) {
					maxDate = new Date(maxDate.getTime() + 86399999); // include all milliseconds of a day
					return (rowDate.after(minDate) && rowDate.before(maxDate));
				} else {
					return true; // no filtering taking place
				}
			} else if ((type == ColumnType.BYTE && value instanceof Byte)) {
				Byte minValue = null;
				Byte maxValue = null;
				Byte rowValue = null;

				try {
					if (filterValueMin != null) minValue = (Byte) filterValueMin;
					if (filterValueMax != null) maxValue = (Byte) filterValueMax;
					rowValue = (Byte) value;
				} catch (Exception e) {
					return false;
				}

				if (rowValue != null) {
					if (minValue != null) {
						byte rowVal = rowValue.byteValue();
						byte minVal = minValue.byteValue();
						if (rowVal == minVal) return true;
					}
					if (maxValue != null) {
						byte rowVal = rowValue.byteValue();
						byte maxVal = maxValue.byteValue();
						if (rowVal == maxVal) return true;
					}
				}
				if (minValue != null && maxValue == null) {
					return rowValue > minValue;
				} else if (minValue == null && maxValue != null) {
					return rowValue < maxValue;
				} else if (minValue != null && maxValue != null) {
					return (rowValue > minValue && rowValue < maxValue);
				} else {
					return true; // no filtering taking place
				}
			} else if ((type == ColumnType.SHORT && value instanceof Short)) {
				Short minValue = null;
				Short maxValue = null;
				Short rowValue = null;

				try {
					if (filterValueMin != null) minValue = (Short) filterValueMin;
					if (filterValueMax != null) maxValue = (Short) filterValueMax;
					rowValue = (Short) value;
				} catch (Exception e) {
					return false;
				}

				if (rowValue != null) {
					if (minValue != null) {
						short rowVal = rowValue.shortValue();
						short minVal = minValue.shortValue();
						if (rowVal == minVal) return true;
					}
					if (maxValue != null) {
						short rowVal = rowValue.shortValue();
						short maxVal = maxValue.shortValue();
						if (rowVal == maxVal) return true;
					}
				}
				if (minValue != null && maxValue == null) {
					return rowValue > minValue;
				} else if (minValue == null && maxValue != null) {
					return rowValue < maxValue;
				} else if (minValue != null && maxValue != null) {
					return (rowValue > minValue && rowValue < maxValue);
				} else {
					return true; // no filtering taking place
				}
			} else if ((type == ColumnType.INT && value instanceof Integer)) {
				Integer minValue = null;
				Integer maxValue = null;
				Integer rowValue = null;

				try {
					if (filterValueMin != null) minValue = (Integer) filterValueMin;
					if (filterValueMax != null) maxValue = (Integer) filterValueMax;
					rowValue = (Integer) value;
				} catch (Exception e) {
					return false;
				}

				if (rowValue != null) {
					if (minValue != null) {
						int rowVal = rowValue.intValue();
						int minVal = minValue.intValue();
						if (rowVal == minVal) return true;
					}
					if (maxValue != null) {
						int rowVal = rowValue.intValue();
						int maxVal = maxValue.intValue();
						if (rowVal == maxVal) return true;
					}
				}
				if (minValue != null && maxValue == null) {
					return rowValue > minValue;
				} else if (minValue == null && maxValue != null) {
					return rowValue < maxValue;
				} else if (minValue != null && maxValue != null) {
					return (rowValue > minValue && rowValue < maxValue);
				} else {
					return true; // no filtering taking place
				}
			} else if ((type == ColumnType.LONG && value instanceof Long)) {
				Long minValue = null;
				Long maxValue = null;
				Long rowValue = null;

				try {
					if (filterValueMin != null) minValue = (Long) filterValueMin;
					if (filterValueMax != null) maxValue = (Long) filterValueMax;
					rowValue = (Long) value;
				} catch (Exception e) {
					return false;
				}

				if (rowValue != null) {
					if (minValue != null) {
						long rowVal = rowValue.longValue();
						long minVal = minValue.longValue();
						if (rowVal == minVal) return true;
					}
					if (maxValue != null) {
						long rowVal = rowValue.longValue();
						long maxVal = maxValue.longValue();
						if (rowVal == maxVal) return true;
					}
				}
				if (minValue != null && maxValue == null) {
					return rowValue > minValue;
				} else if (minValue == null && maxValue != null) {
					return rowValue < maxValue;
				} else if (minValue != null && maxValue != null) {
					return (rowValue > minValue && rowValue < maxValue);
				} else {
					return true; // no filtering taking place
				}
			} else if ((type == ColumnType.FLOAT && value instanceof Float)) {
				Float minValue = null;
				Float maxValue = null;
				Float rowValue = null;

				try {
					if (filterValueMin != null) minValue = (Float) filterValueMin;
					if (filterValueMax != null) maxValue = (Float) filterValueMax;
					rowValue = (Float) value;
				} catch (Exception e) {
					return false;
				}

				if (rowValue != null) {
					if (minValue != null) {
						float rowVal = rowValue.floatValue();
						float minVal = minValue.floatValue();
						if (rowVal == minVal) return true;
					}
					if (maxValue != null) {
						float rowVal = rowValue.floatValue();
						float maxVal = maxValue.floatValue();
						if (rowVal == maxVal) return true;
					}
				}
				if (minValue != null && maxValue == null) {
					return rowValue > minValue;
				} else if (minValue == null && maxValue != null) {
					return rowValue < maxValue;
				} else if (minValue != null && maxValue != null) {
					return (rowValue > minValue && rowValue < maxValue);
				} else {
					return true; // no filtering taking place
				}
			} else if ((type == ColumnType.DOUBLE && value instanceof Double)) {
				Double minValue = null;
				Double maxValue = null;
				Double rowValue = null;

				try {
					if (filterValueMin != null) minValue = (Double) filterValueMin;
					if (filterValueMax != null) maxValue = (Double) filterValueMax;
					rowValue = (Double) value;
				} catch (Exception e) {
					return false;
				}

				if (rowValue != null) {
					if (minValue != null) {
						double rowVal = rowValue.doubleValue();
						double minVal = minValue.doubleValue();
						if (rowVal == minVal) return true;
					}
					if (maxValue != null) {
						double rowVal = rowValue.doubleValue();
						double maxVal = maxValue.doubleValue();
						if (rowVal == maxVal) return true;
					}
				}
				if (minValue != null && maxValue == null) {
					return rowValue > minValue;
				} else if (minValue == null && maxValue != null) {
					return rowValue < maxValue;
				} else if (minValue != null && maxValue != null) {
					return (rowValue > minValue && rowValue < maxValue);
				} else {
					return true; // no filtering taking place
				}
			} else {
				return false;
			}
		} else if (filterValueMin != null || filterValueMax != null) {
			return false;
		} else {
			return true; // no filtering taking place
		}
    }
}
