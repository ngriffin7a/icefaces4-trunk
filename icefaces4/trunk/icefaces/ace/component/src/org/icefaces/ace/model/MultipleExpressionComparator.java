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

public class MultipleExpressionComparator implements Comparator {

	private SingleExpressionComparator[] singlePropertyComparators;
    private String rowVar;

	public MultipleExpressionComparator(SortCriteria[] sortCriteria, String rowVar) {
        this.rowVar = rowVar;
		singlePropertyComparators = new SingleExpressionComparator[sortCriteria.length];
		for (int i = 0; i < sortCriteria.length; i++) {
			singlePropertyComparators[i] = new SingleExpressionComparator(sortCriteria[i], rowVar);
		}
	}
	
	public int compare(Object obj1, Object obj2) {
	
		try {
			for (int i = 0; i < singlePropertyComparators.length; i++) {
				int result = singlePropertyComparators[i].compare(obj1, obj2);
				if (result != 0) return result;
			}
			return 0;
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}