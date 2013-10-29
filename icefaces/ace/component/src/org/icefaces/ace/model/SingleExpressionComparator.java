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

package org.icefaces.ace.model;

import org.icefaces.ace.model.table.SortCriteria;

import java.util.Comparator;
import javax.el.ELContext;
import javax.faces.context.FacesContext;

public class SingleExpressionComparator implements Comparator {

    private SortCriteria criteria;
    private String rowVar;

	public SingleExpressionComparator(SortCriteria sortCriteria, String rowVar) {
		this.criteria = sortCriteria;
        this.rowVar = rowVar;
	}
	
	public int compare(Object obj1, Object obj2) {
		try {
			FacesContext context = FacesContext.getCurrentInstance();
            ELContext elContext = context.getELContext();
            boolean hasComparator = criteria.getComparator() != null;
            int result;

            context.getExternalContext().getRequestMap().put(rowVar, obj1);
            Object value1 = criteria.getExpression().getValue(elContext);

            context.getExternalContext().getRequestMap().put(rowVar, obj2);
            Object value2 = criteria.getExpression().getValue(elContext);

            context.getExternalContext().getRequestMap().remove(rowVar);

            if (hasComparator) {
                result = criteria.getComparator().compare(value1, value2);
            } else {
                if (value1 == null) {
                    if (value2 == null) result = 0;
                    else result = 1;
                }
                else if (value2 == null)
                    result = -1;
                else
                    result = ((Comparable) value1).compareTo(value2);
            }
			
			return criteria.isAscending() ? result : -1 * result;
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}