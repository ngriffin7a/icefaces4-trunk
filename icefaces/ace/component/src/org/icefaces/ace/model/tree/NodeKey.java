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

import org.icefaces.ace.util.ArrayUtils;

import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: Nils
 * Date: 8/15/12
 * Time: 9:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class NodeKey implements Serializable {
    public static final NodeKey ROOT_KEY = new NodeKey();

    private Object[] keys;

    public NodeKey() {}

    public NodeKey(Object... keys) {
        this.keys = keys;
    }

    public Object[] getKeys() {
        return keys;
    }

    public void setKeys(Object[] keys) {
        this.keys = keys;
    }

    public NodeKey append(Object[] segments) {
        return new NodeKey((Object[])ArrayUtils.concat(keys, segments));
    }

    public NodeKey append(final Object segment) {
        return append(new Object[] {segment});
    }

    public NodeKey getParent() {
        if (keys.length == 0) return null;

        Object[] parentKeys = new Object[keys.length - 1];
        System.arraycopy(keys, 0, parentKeys, 0, parentKeys.length);
        if (parentKeys.length == 0) parentKeys = null;
        return new NodeKey(parentKeys);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NodeKey nodeKey = (NodeKey) o;

        if (!Arrays.equals(keys, nodeKey.keys)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return keys != null ? Arrays.hashCode(keys) : 0;
    }

    @Override
    public String toString() {
        if (keys != null)
            return join(keys,
                    UINamingContainer.getSeparatorChar(
                            FacesContext.getCurrentInstance()));
        return "";
    }

    private String join(Object[] objects, char sep) {
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < objects.length; i++) {
            b.append(objects[i].toString());
            if (i != (objects.length - 1)) b.append(sep);
        }
        return b.toString();
    }
}
