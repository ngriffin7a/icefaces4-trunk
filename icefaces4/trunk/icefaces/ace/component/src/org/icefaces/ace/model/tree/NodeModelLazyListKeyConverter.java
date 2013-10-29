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

import java.util.List;
import java.util.Map;

public class NodeModelLazyListKeyConverter<K> implements KeySegmentConverter<K> {
    LazyNodeDataModel<K> model;
    public NodeModelLazyListKeyConverter(LazyNodeDataModel<K> ks) {
        model = ks;
    }

    public Object getSegment(K node) {
        K parent = model.parentMap.get(node);
        Map<K, List<K>> childMap = model.childMap;
        List siblings;

        // If parent returns null, root children are returned
        siblings = childMap.get(parent);

        return siblings.indexOf(node);
    }

    public NodeKey parseSegments(String[] segments) {
        Integer[] indexes = new Integer[segments.length];

        for (int i = 0; i < segments.length; i++) {
            indexes[i] = Integer.parseInt(segments[i]);
        }
        return new NodeKey(indexes);
    }
}
