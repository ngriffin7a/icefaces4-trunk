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

import org.icefaces.ace.model.SimpleEntry;

import javax.faces.model.DataModel;
import java.util.Iterator;
import java.util.Map;

public abstract class NodeDataModel<K> extends DataModel<K> {
    static final char SEPARATOR_CHAR = ':';
    NodeKey key = NodeKey.ROOT_KEY;
    K data;

    public K getData() {
        return data;
    }

    protected void setData(K data) {
        this.data = data;
    }

    public NodeKey getKey() {
        return key;
    }

    // Alias to navToKey.
    public void setKey(NodeKey key) {
        navToKey(key);
    }

    public Map.Entry<NodeKey, K> getEntry() {
        return new SimpleEntry<NodeKey, K>(key, data);
    }

    protected boolean atNullRoot() {
        return key != null && key.getKeys() == null;
    }

    // NodeDataModel Contract
    // Absolute navigation
    public abstract K navToKey(NodeKey key);
    // 1-step relative navigation
    public abstract K navToParent();
    public abstract K navToChild(Object keySegment);
    // For full tree traversals
    public abstract Iterator<Map.Entry<NodeKey, K>> children();

    public abstract KeySegmentConverter getConverter();
    public abstract void setConverter(KeySegmentConverter converter);
    public abstract boolean isNodeAvailable();
    public abstract boolean isLeaf();
    public abstract boolean isMutable();
    // If not mutable, no need to override

    /**
     * Insert a node as a child of the current node.
     * @param node the node to be inserted
     * @param index the position index of the node among siblings
     */
    public void insert(K node, int index) {
        throw new UnsupportedOperationException();
    }

    /**
     * Remove a node from the children of the current node.
     * @param segOrNode the node to be removed or identifying key segment
     * @param isSegment identify if the first argument is a node or segment
     */
    public void remove(Object segOrNode, boolean isSegment) {
        throw new UnsupportedOperationException();
    }

    // Data Model Impl.
    @Override
    public boolean isRowAvailable() {
        return isNodeAvailable();
    }

    @Override
    public int getRowCount() {
        throw new UnsupportedOperationException();
    }

    @Override
    public K getRowData() {
        return getData();
    }

    @Override
    public int getRowIndex() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setRowIndex(int rowIndex) {
        throw new UnsupportedOperationException();
    }
}
