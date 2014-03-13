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

import java.util.Collections;
import java.util.Iterator;

public class IndexedIterator<K> implements Iterator<K> {
    boolean nextCalled = false;
    int curIndex = 0;
    Iterator<K> backing = Collections.EMPTY_LIST.iterator();

    public IndexedIterator() {}

    public IndexedIterator(Iterator<K> iterator, int startingIndex) {
        curIndex = startingIndex-1; /* Decremented to provide 0 based indexing */
        backing = iterator;
    }

    public IndexedIterator(Iterator<K> iterator) {
        curIndex = -1; /* Decremented to provide 0 based indexing */
        backing = iterator;
    }

    /**
     * Return the row index of the last object returned from next() or throw
     * an IllegalStateException if next() has not yet been called.
     *
     * @return object row index
     */
    public int getIndex() {
        if (!nextCalled) throw new IllegalStateException();
        return curIndex;
    }

    public boolean hasNext() {
        return backing.hasNext();
    }

    public K next() {
        nextCalled = true;
        curIndex++;
        return backing.next();
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }
}
