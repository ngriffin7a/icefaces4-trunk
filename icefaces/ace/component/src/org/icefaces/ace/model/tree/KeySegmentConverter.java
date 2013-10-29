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

/**
 * Created with IntelliJ IDEA.
 * User: Nils
 * Date: 8/16/12
 * Time: 2:41 PM
 * To change this template use File | Settings | File Templates.
 */
public interface KeySegmentConverter<K> {
    // Return key segment object based on node object
    // Key segment  equality is used to identify the source node object
    // The .toString() of the key segment object will be used to write DOM node IDs.
    public Object getSegment(K node);
    // Used to create complete NodeKey with segment objects parsed from their .toString()
    // representations.
    public NodeKey parseSegments(String[] segments);
}
