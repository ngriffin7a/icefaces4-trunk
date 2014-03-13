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

package org.icefaces.mobi.model.dataview;

import java.util.List;

public abstract class DataViewLazyDataModel extends DataViewDataModel {

    /* objects retrieved for current page of data*/
    List result;

    /* get data object at index */
    abstract public Object getDataByIndex(int index);

    /* get length of entire data set*/
    abstract public int length();

    public IndexedIterator<Object> iterator() {
        if (result != null) {
            return new IndexedIterator<Object>(
                    result.iterator(),
                    getPageStartIndex()
                );
        }
        else return new IndexedIterator<Object>();
    }
}
