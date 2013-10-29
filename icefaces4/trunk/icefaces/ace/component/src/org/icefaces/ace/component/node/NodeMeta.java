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

package org.icefaces.ace.component.node;

import org.icefaces.ace.meta.annotation.*;
import org.icefaces.ace.meta.baseMeta.UIComponentBaseMeta;

import org.icefaces.ace.resources.ACEResourceNames;
import org.icefaces.resources.ICEResourceDependencies;
import org.icefaces.resources.ICEResourceDependency;

@Component(
        tagName = "node",
        componentClass = "org.icefaces.ace.component.node.Node",
        generatedClass = "org.icefaces.ace.component.node.NodeBase",
        extendsClass   = "javax.faces.component.UIComponentBase",
        componentType  = "org.icefaces.ace.component.Node",
        componentFamily = "org.icefaces.ace.Node",
        tlddoc = "Defines the contents of a node of the parent tree. Multiple nodes of different types " +
                "can be rendered by the same tree by matching the ace:tree 'type' property." +
                "<p>For more information, see the <a href=\"http://wiki.icefaces.org/display/ICE/Tree\">Node Wiki Documentation</a>.</p>")

@ICEResourceDependency(library = ACEResourceNames.ACE_LIBRARY,
                       name = ACEResourceNames.JQUERY_JS)
public class NodeMeta extends UIComponentBaseMeta {
    @Property(tlddoc = "The String value that if equal to the value of the 'type' attribute of the tree, " +
            "indicates this ace:node and its contents will be the one rendered for the given node data object. " +
            "If no match is found, the ace:node without a type is used as a default node template. ")
    String type;
}
