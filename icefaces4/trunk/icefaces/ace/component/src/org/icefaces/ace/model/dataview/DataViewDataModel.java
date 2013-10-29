package org.icefaces.ace.model.dataview;

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
 * Time: 2:32 PM
 */

/**
 * Implementors of this interface should return an iterator over the current
 * 'page' of Objects, starting at the firstIndex.
 */
public abstract class DataViewDataModel implements IndexedIterable<Object> {
    abstract public Object getDataByIndex(int index);;
    abstract public int length();

    int pageStartIndex;
    int pageSize = -1; // negative implies max size

    public void setPageStartIndex(int index) {
        pageStartIndex = index;
    }

    public int getPageStartIndex() {
        return pageStartIndex;
    }

    public void setPageSize(int size) {
        pageSize = size;
    }

    public int getPageSize() {
        return pageSize;
    }
}
