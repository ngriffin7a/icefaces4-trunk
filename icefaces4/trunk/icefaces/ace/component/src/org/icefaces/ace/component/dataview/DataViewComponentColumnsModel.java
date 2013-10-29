package org.icefaces.ace.component.dataview;

import org.icefaces.ace.model.dataview.DataViewColumnModel;
import org.icefaces.ace.model.dataview.DataViewColumnsModel;
import org.icefaces.ace.model.dataview.IndexedIterator;

import javax.faces.component.UIComponent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Copyright 2010-2013 ICEsoft Technologies Canada Corp.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * <p/>
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * <p/>
 * User: Nils Lundquist
 * Date: 2013-04-01
 * Time: 2:12 PM
 */
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
