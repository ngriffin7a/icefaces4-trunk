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

import org.icefaces.ace.util.CollectionUtils;
import org.icefaces.ace.util.collections.EntrySetToKeyListTransformer;
import org.icefaces.ace.util.collections.Predicate;

import java.io.Serializable;
import java.util.*;

public class NodeStateMap implements Map<Object, NodeState>, Serializable {
    Map<Object, NodeState> map = new HashMap<Object, NodeState>();
    KeySegmentConverter keyConverter;
    Predicate selectedPredicate = new SelectedPredicate();
    Predicate expandedPredicate = new ExpandedPredicate();
    NodeStateCreationCallback initCallback;

    public NodeStateMap() {
    }

    public NodeStateMap(NodeStateCreationCallback callback) {
        setInitCallback(callback);
    }

    public KeySegmentConverter getKeyConverter() {
        return keyConverter;
    }

    public void setKeyConverter(KeySegmentConverter keyConverter) {
        this.keyConverter = keyConverter;
    }

    public int size() {
        return map.size();
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    public boolean containsKey(Object o) {
        return map.containsKey(o);
    }

    public boolean containsValue(Object o) {
        return map.containsValue(o);
    }

    public NodeState get(Object o) {
        NodeState state;

        state = map.get(o);
        if (state != null) return state;

        // If state is null, create a new state for the node.
        state = new NodeState();

        if (getInitCallback() != null)
            getInitCallback().initializeState(state, o);

        put(o, state);
        return state;
    }

    public NodeState put(Object o, NodeState nodeState) {
        if (keyConverter == null) return map.put(o, nodeState);
        else return map.put(keyConverter.getSegment(o), nodeState);
    }

    public NodeState remove(Object o) {
        if (keyConverter == null) return map.remove(o);
        else return map.remove(keyConverter.getSegment(o));
    }

    public void putAll(Map<? extends Object, ? extends NodeState> inputMap) {
        for (Object key : inputMap.keySet())
            put(key, inputMap.get(key));
    }

    public void clear() {
        map.clear();
    }

    public Set<Object> keySet() {
        return map.keySet();
    }

    public Collection<NodeState> values() {
        return map.values();
    }

    public Set<Entry<Object, NodeState>> entrySet() {
        return map.entrySet();
    }

    // Getters
    public List getSelected() {
        return EntrySetToKeyListTransformer.transform(CollectionUtils.select(map.entrySet(), this.selectedPredicate));
    }

    public List getExpanded() {
        return EntrySetToKeyListTransformer.transform(CollectionUtils.select(map.entrySet(), this.expandedPredicate));
    }

    public NodeStateCreationCallback getInitCallback() {
        return initCallback;
    }

    // Setters
    public void setAllSelected(boolean val) {
        for (NodeState s : map.values()) s.setSelected(val);
    }

    public void setAllExpanded(boolean val) {
        for (NodeState s : map.values()) s.setExpanded(val);
    }


    public void setInitCallback(NodeStateCreationCallback initCallback) {
        this.initCallback = initCallback;
    }

    // Predicates
    static class SelectedPredicate implements Predicate, Serializable {
        public boolean evaluate(Object o) {
            if (o instanceof Entry)
                if (((NodeState)((Entry)o).getValue()).isSelected()) return true;
            return false;
        }
    }

    static class ExpandedPredicate implements Predicate, Serializable {
        public boolean evaluate(Object o) {
            if (o instanceof Entry)
                if (((NodeState)((Entry)o).getValue()).isExpanded()) return true;
            return false;
        }
    }
}
