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

import org.icefaces.mobi.model.dataview.DataViewColumnModel;
import org.icefaces.mobi.model.dataview.DataViewColumnsModel;
import org.icefaces.mobi.model.dataview.IndexedIterator;

import javax.faces.component.UIComponent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DataViewComponentColumnsModel implements DataViewColumnsModel {
    List<DataViewColumnModel> columns = new ArrayList<DataViewColumnModel>();

    public DataViewComponentColumnsModel() {}

    public DataViewComponentColumnsModel(DataViewColumns uiColumns) {
        for (UIComponent child : uiColumns.getChildren())
            if (child instanceof DataViewColumn)
                columns.add(((DataViewColumn)child).getModel());
    }

    public IndexedIterator<DataViewColumnModel> iterator() {
        return new IndexedIterator(columns.iterator());
    }

    public boolean hasHeaders() {
        for (DataViewColumnModel column : columns)
            if (column.getHeaderText() != null) return true;

        return false;
    }

    public boolean hasFooters() {
        for (DataViewColumnModel column : columns)
            if (column.getFooterText() != null) return true;

        return false;
    }

    public int size() {
        return columns.size();
    }
}
