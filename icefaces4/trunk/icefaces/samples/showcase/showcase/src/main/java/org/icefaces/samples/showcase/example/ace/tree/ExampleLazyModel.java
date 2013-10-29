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

package org.icefaces.samples.showcase.example.ace.tree;

import org.icefaces.ace.model.tree.LazyNodeDataModel;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ExampleLazyModel extends LazyNodeDataModel<LocationNodeImpl> {
    private List<LocationNodeImpl> treeRoots = TreeDataFactory.getTreeRoots();

    @Override
    public List<LocationNodeImpl> loadChildrenForNode(LocationNodeImpl node) {
        if (node == null)
            return treeRoots;

        if (treeRoots.get(0) == node)
            return treeRoots.get(0).children;

        for (LocationNodeImpl child : treeRoots.get(0).children)
            if (child == node) return child.children;

        return Collections.emptyList();
    }
}
