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

package org.icefaces.ace.model.tree;

import org.apache.commons.collections.IteratorUtils;
import org.icefaces.ace.model.SimpleEntry;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;

public class NodeEnumerationToNodeEntryIterator<V> implements Iterator<Map.Entry<NodeKey, V>> {
    Iterator<V> iter;
    KeySegmentConverter converter;
    NodeKey parentKey;

    public NodeEnumerationToNodeEntryIterator(KeySegmentConverter converter, NodeKey parentKey, Enumeration<V> children) {
        iter = (Iterator<V>)IteratorUtils.asIterator(children);
        this.converter = converter;
        this.parentKey = parentKey;
    }

    public boolean hasNext() {
        return iter.hasNext();
    }

    public Map.Entry<NodeKey, V> next() {
        V n = iter.next();
        return new SimpleEntry<NodeKey, V>(parentKey.append(converter.getSegment(n)), n);
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }
}
