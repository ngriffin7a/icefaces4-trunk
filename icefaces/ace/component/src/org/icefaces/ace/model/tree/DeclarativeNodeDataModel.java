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

import java.util.Iterator;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Nils
 * Date: 8/15/12
 * Time: 8:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class DeclarativeNodeDataModel<K> extends NodeDataModel<K> {
    TreeModelAdaptor adaptor;

    public DeclarativeNodeDataModel(TreeModelAdaptor adaptor) {
        this.adaptor = adaptor;
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public K navToKey(NodeKey key) {
        return null;
    }

    @Override
    public K navToParent() {
        return null;
    }

    @Override
    public K navToChild(Object keySegment) {
        return null;
    }

    @Override
    public boolean isNodeAvailable() {
        return false;
    }

    @Override
    public boolean isLeaf() {
        return false;
    }

    @Override
    public Iterator<Map.Entry<NodeKey, K>> children() {
        return null;
    }

    @Override
    public KeySegmentConverter getConverter() {
        return null;
    }

    @Override
    public void setConverter(KeySegmentConverter converter) {

    }

    @Override
    public Object getWrappedData() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setWrappedData(Object data) {
        if (!(data instanceof TreeModelAdaptor))
            throw new IllegalArgumentException(String.valueOf(data));
        else
            adaptor = (TreeModelAdaptor) data;
    }
}
