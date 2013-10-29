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

package org.icefaces.ace.component.tree;

import org.icefaces.ace.model.tree.*;
import org.icefaces.ace.renderkit.CoreRenderer;
import org.icefaces.ace.util.ComponentUtils;
import org.icefaces.ace.util.HTML;
import org.icefaces.ace.util.JSONBuilder;
import org.icefaces.render.MandatoryResourceComponent;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.lang.String;
import java.util.Iterator;
import java.util.Map;

@MandatoryResourceComponent(tagName = "tree", value="org.icefaces.ace.component.tree.Tree")
public class TreeRenderer extends CoreRenderer {
    // Wraps everything
    public static final String TREE_CONTAINER_CLASS = "if-tree if-node-sub ui-widget-content ui-corner-all";
    // Cell for the node template
    public static final String NODE_CLASS = "if-node";
    // Wraps individual node template
    public static final String NODE_WRAPPER_CLASS = "if-node-wrp";
    // Wraps node & subnode container class
    public static final String NODE_CONTAINER_CLASS = "if-node-cnt";
    // Wraps child node container classes
    public static final String SUBNODE_CONTAINER_CLASS = "if-node-sub";
    // Wraps switch icon
    public static final String NODE_SWITCH_CLASS = "if-node-sw";
    public static final String NODE_SWITCH_DISABLED_CLASS = "noexp";
    public static final String NODE_SWITCH_UNRENDERED_CLASS = "ui-icon ui-icon-radio-on";
    public static final String NODE_SELECTION_DISABLED_CLASS = "noselect";
    public static final String NODE_SELECTED_CLASS = "ui-state-active";
    // Applied to span with JQuery UI icon class applied
    public static final String NODE_SWITCH_ICON_CLASS = "ui-icon";
    public static final String NODE_EXPANDED_ICON_CLASS = "ui-icon-minus";
    public static final String NODE_CONTRACTED_ICON_CLASS = "ui-icon-plus";
    public static final String NODE_CELL = "if-node-td";
    public static final String NODE_ROW = "if-node-tr";
    public static final String NODE_LEAF_LINE_STYLE = "width: 13px; left:-17px;";
    private static final String NODE_LINE_CONTAINER = "if-node-ln";

    @Override
    public void decode(final FacesContext context, final UIComponent component) {
         new TreeDecoder(context, (Tree) component) {{
             decode();
         }};

        decodeBehaviors(context, component);
    }

    @Override
    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {}

    @Override
    public boolean getRendersChildren() { return true; }

    @Override
    public void encodeEnd(FacesContext facesContext, UIComponent component) throws IOException {
        Tree tree = (Tree) component;
        TreeRendererContext renderContext = new TreeRendererContext(tree, resolveWidgetVar(tree));
        ResponseWriter writer = facesContext.getResponseWriter();

        openContainerElement(writer, facesContext, renderContext);
        encodeRoots(writer, facesContext, renderContext);
        encodeHiddenFields(writer, facesContext, renderContext);
        encodeScript(writer, facesContext, renderContext);
        closeContainerElement(writer, renderContext);
    }

    private void encodeHiddenFields(ResponseWriter writer, FacesContext facesContext, TreeRendererContext renderContext) throws IOException {
        String id;
        String clientId = renderContext.getTree().getClientId(facesContext);

        id = clientId + "_select";
        writer.startElement(HTML.INPUT_ELEM, null);
        writer.writeAttribute(HTML.TYPE_ATTR, "hidden", null);
        writer.writeAttribute(HTML.ID_ATTR, id, null);
        writer.writeAttribute(HTML.NAME_ATTR, id, null);
        writer.writeAttribute(HTML.AUTOCOMPLETE_ATTR, "off", null);
        writer.endElement(HTML.INPUT_ELEM);

        id = clientId + "_deselect";
        writer.startElement(HTML.INPUT_ELEM, null);
        writer.writeAttribute(HTML.TYPE_ATTR, "hidden", null);
        writer.writeAttribute(HTML.ID_ATTR, id, null);
        writer.writeAttribute(HTML.NAME_ATTR, id, null);
        writer.writeAttribute(HTML.AUTOCOMPLETE_ATTR, "off", null);
        writer.endElement(HTML.INPUT_ELEM);

        id = clientId + "_expand";
        writer.startElement(HTML.INPUT_ELEM, null);
        writer.writeAttribute(HTML.TYPE_ATTR, "hidden", null);
        writer.writeAttribute(HTML.ID_ATTR, id, null);
        writer.writeAttribute(HTML.NAME_ATTR, id, null);
        writer.writeAttribute(HTML.AUTOCOMPLETE_ATTR, "off", null);
        writer.endElement(HTML.INPUT_ELEM);

        id = clientId + "_contract";
        writer.startElement(HTML.INPUT_ELEM, null);
        writer.writeAttribute(HTML.TYPE_ATTR, "hidden", null);
        writer.writeAttribute(HTML.ID_ATTR, id, null);
        writer.writeAttribute(HTML.NAME_ATTR, id, null);
        writer.writeAttribute(HTML.AUTOCOMPLETE_ATTR, "off", null);
        writer.endElement(HTML.INPUT_ELEM);
    }

    private void encodeScript(ResponseWriter writer, FacesContext facesContext, TreeRendererContext renderContext) throws IOException {
        JSONBuilder confJson = new JSONBuilder();
        Tree tree = renderContext.getTree();
        KeySegmentConverter converter = tree.getKeyConverter();
        String clientId  = tree.getClientId(facesContext);
        String widgetVar = renderContext.getWidgetVar();
        boolean selection = renderContext.isSelection();
        boolean expansion = renderContext.isExpansion();
        boolean reordering = renderContext.isReordering();
        boolean multipleSelection = renderContext.isMultipleSelection();
        boolean indexIds = converter instanceof NodeModelLazyListKeyConverter ||
                converter instanceof NodeModelListSequenceKeyConverter;
		String handle = tree.getDragHandle();

        confJson.initialiseVar(widgetVar).beginFunction("ice.ace.create").item("Tree")
                .beginArray()
                .item(clientId)
                .beginMap()
                .entry("widgetVar", widgetVar)
                .entry("expansionMode", tree.getExpansionMode().name())
                .entry("selectionMode", tree.getSelectionMode().name())
                .entry("indexIds", indexIds);

        if (reordering)
            confJson.entry("reorder", true);

        if (expansion) {
            confJson.entry("expansion", true);
        }

        if (selection) {
            confJson.entry("selection", true);
            if (multipleSelection) confJson.entry("multiSelect", true);
        }
		
		if (handle != null) {
			confJson.entry("handle", handle);
		}

        encodeClientBehaviors(facesContext, tree, confJson);
        confJson.endMap().endArray().endFunction();

        writer.startElement(HTML.SCRIPT_ELEM, null);
        writer.writeAttribute(HTML.TYPE_ATTR, "text/javascript", null);
        writer.write(confJson.toString());
        writer.endElement(HTML.SCRIPT_ELEM);
    }


    private void encodeRoots(ResponseWriter writer, FacesContext facesContext, TreeRendererContext renderContext) throws IOException {
        Tree tree = renderContext.getTree();
        // Initalize cached NodeStateMap before clientId begins to change
        tree.getClientId();
        tree.getStateMap();

        // Encode each 'child' of the null keyed node, the root nodes.
        for (Iterator<Map.Entry<NodeKey,Object>> roots = tree.children();
             roots.hasNext();) {
            Map.Entry<NodeKey, Object> root = roots.next();

            tree.setNodeToChild(root.getKey().getKeys()[0]);
            encodeNode(writer, facesContext, renderContext);
            tree.setNodeToParent();
        }

        tree.setNodeToKey(NodeKey.ROOT_KEY);
    }

    private void encodeNode(ResponseWriter writer, FacesContext facesContext, TreeRendererContext renderContext) throws IOException {
        Tree tree = renderContext.getTree();
        NodeState state = tree.getNodeState();
        String nodeClass = NODE_CLASS;
        String nodeWrapperClass = NODE_WRAPPER_CLASS;
        String dotSource = renderContext.getDotURL();
        String widgetVar = renderContext.getWidgetVar();
        String id = tree.getClientId(facesContext);
        boolean expanded = state.isExpanded();
        boolean isClientExpansion = renderContext.getExpansionMode().isClient();

        if (!state.isSelectionEnabled())
            nodeClass+= " " + NODE_SELECTION_DISABLED_CLASS;

        if (state.isSelected() && renderContext.isSelection())
            nodeWrapperClass += " " + NODE_SELECTED_CLASS;

        // Encode 'table' container
        writer.startElement(HTML.TABLE_ELEM, null);
        writer.writeAttribute(HTML.ID_ATTR, id, null);
        writer.writeAttribute(HTML.CLASS_ATTR, NODE_CONTAINER_CLASS, null);
        writer.startElement(HTML.TBODY_ELEM, null);

        // First 'line' of node
        writer.startElement(HTML.TR_ELEM, null);
        writer.writeAttribute(HTML.CLASS_ATTR, NODE_ROW, null);

        encodeNodeSwitch(writer, renderContext, state);

        // Write Node Template
        writer.startElement(HTML.TD_ELEM, null);
        writer.writeAttribute(HTML.CLASS_ATTR, nodeClass, null);
        writer.startElement(HTML.DIV_ELEM, null);
        writer.writeAttribute(HTML.CLASS_ATTR, nodeWrapperClass, null);
        tree.getNodeForType().encodeAll(facesContext);
        writer.endElement(HTML.DIV_ELEM);
        writer.endElement(HTML.TD_ELEM);

        // End first line
        writer.endElement(HTML.TR_ELEM);

        // 2nd line of node
        writer.startElement(HTML.TR_ELEM, null);
        writer.writeAttribute(HTML.CLASS_ATTR, NODE_ROW, null);

        // Write filler cell
        writer.startElement(HTML.TD_ELEM, null);
        writer.writeAttribute(HTML.CLASS_ATTR, NODE_CELL, null);
        writer.startElement(HTML.DIV_ELEM, null);
        writer.writeAttribute(HTML.CLASS_ATTR, NODE_LINE_CONTAINER, null);
        writer.startElement(HTML.IMG_ELEM, null);
        writer.writeAttribute(HTML.SRC_ATTR, dotSource, null);
        writer.endElement(HTML.IMG_ELEM);
        writer.endElement(HTML.DIV_ELEM);
        writer.endElement(HTML.TD_ELEM);

        // Open SubComponent Container
        writer.startElement(HTML.TD_ELEM, null);
        writer.writeAttribute(HTML.CLASS_ATTR, SUBNODE_CONTAINER_CLASS, null);

        if (isClientExpansion && !expanded)
            writer.writeAttribute(HTML.STYLE_ATTR, "display:none;", null);

        if (expanded || isClientExpansion) {
            encodeSubnodes(writer, facesContext, renderContext);
        }

        // Close SubComponent Container
        writer.endElement(HTML.TD_ELEM);
        // End 2nd line of node
        writer.endElement(HTML.TR_ELEM);
        // End node
        writer.endElement(HTML.TBODY_ELEM);

        if (renderContext.isReordering()) {
            writer.startElement(HTML.SCRIPT_ELEM, null);
            writer.writeAttribute(HTML.TYPE_ATTR, "text/javascript", null);
            writer.write("if (window['"+widgetVar+"']) "+widgetVar+".rs('"+id+"');");
            writer.endElement(HTML.SCRIPT_ELEM);
        }

        writer.endElement(HTML.TABLE_ELEM);
    }

    private void encodeSubnodes(ResponseWriter writer, FacesContext facesContext, TreeRendererContext renderContext) throws IOException {
        Tree tree = renderContext.getTree();
        for (Iterator<Map.Entry<NodeKey,Object>> children = tree.children();
             children.hasNext();) {
            Map.Entry<NodeKey, Object> node = children.next();
            Object[] nextKeys = node.getKey().getKeys();

            tree.setNodeToChild(nextKeys[nextKeys.length - 1]);
            if (tree.isNodeAvailable())
                encodeNode(writer, facesContext, renderContext);
            tree.setNodeToParent();
        }
    }

    // Responsible for encoding node switch and heirarchy line.
    private void encodeNodeSwitch(ResponseWriter writer, TreeRendererContext renderContext, NodeState state) throws IOException {
        String switchClass = NODE_SWITCH_CLASS;
        String iconClass = NODE_SWITCH_ICON_CLASS;
        String expandedClass = NODE_EXPANDED_ICON_CLASS;
        String contractedClass = NODE_CONTRACTED_ICON_CLASS;
        String leafStyle = NODE_LEAF_LINE_STYLE;
        String dotSource = renderContext.getDotURL();
        boolean lazy = renderContext.isLazy();
        boolean leaf = lazy ? false : renderContext.getTree().isLeaf(); // prevent attempt at loading children in lazy mode

        if (renderContext.isExpansion())
            iconClass += " " + (state.isExpanded() ? expandedClass : contractedClass);
        else
            iconClass = NODE_SWITCH_UNRENDERED_CLASS;

        if (!state.isExpansionEnabled() || !renderContext.isExpansion())
            switchClass += " " + NODE_SWITCH_DISABLED_CLASS;       

        writer.startElement(HTML.TD_ELEM, null);
        writer.writeAttribute(HTML.CLASS_ATTR, switchClass, null);

        writer.startElement(HTML.DIV_ELEM, null);
        writer.writeAttribute(HTML.CLASS_ATTR, NODE_LINE_CONTAINER, null);

        writer.startElement(HTML.IMG_ELEM, null);
        writer.writeAttribute(HTML.SRC_ATTR, dotSource, null);
        if (leaf)
            writer.writeAttribute(HTML.STYLE_ATTR, leafStyle, null);
        writer.endElement(HTML.IMG_ELEM);

        if (!leaf || lazy) {
            writer.startElement(HTML.SPAN_ELEM, null);
            writer.writeAttribute(HTML.CLASS_ATTR, iconClass, null);
            writer.endElement(HTML.SPAN_ELEM);
        }

        writer.endElement(HTML.DIV_ELEM);
        writer.endElement(HTML.TD_ELEM);
    }

    private void closeContainerElement(ResponseWriter writer, TreeRendererContext renderContext) throws IOException {
        writer.endElement(HTML.SPAN_ELEM);
    }

    private void openContainerElement(ResponseWriter writer, FacesContext context, TreeRendererContext renderContext) throws IOException {
        writer.startElement(HTML.SPAN_ELEM, renderContext.getTree());

        String clientId = renderContext.getTree().getClientId(context);
        writer.writeAttribute(HTML.ID_ATTR, clientId, null);
        ComponentUtils.enableOnElementUpdateNotify(writer, clientId);

        writer.writeAttribute(HTML.CLASS_ATTR, TREE_CONTAINER_CLASS, null);
    }
}
