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

package org.icefaces.ace.component.datatable;

import org.icefaces.ace.component.column.Column;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.model.SelectItem;
import java.util.Collection;
import java.util.List;

public class DataTableRendererUtil {
    protected static boolean isNextColumnRowSpanEqual(Column column, Column nextCol) {
        return (nextCol.getRowspan() == column.getRowspan());
    }

    protected static boolean areBothSingleColumnSpan(Column column, Column nextCol) {
        return (nextCol.getColspan() == 1) && (column.getColspan() == 1);
    }

    protected static boolean isCurrColumnStacked(List comps, Column currCol) {
        // The first column can not be stacked, only subsequent ones can be
        // stacked under it
        int index = comps.indexOf(currCol);
        if (index == 0) {
            return false;
        }
        return currCol.isStacked();
    }

    protected static Column getNextColumn(Column column, List columnSiblings) {
        int index = columnSiblings.indexOf(column);
        if (index >= 0) {
            if ((index + 1) < columnSiblings.size()) {
                UIComponent next = (UIComponent) columnSiblings.get(index + 1);
                if (next instanceof Column) {
                    return (Column) next;
                }
            }
        }
        return null;
    }
}
