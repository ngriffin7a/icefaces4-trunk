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

import org.icefaces.ace.json.JSONArray;
import org.icefaces.ace.json.JSONException;
import org.icefaces.ace.model.tree.NodeKey;
import org.icefaces.ace.model.tree.NodeState;

import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
import java.util.Map;

public class TreeDecoder<N> {
    private FacesContext context;
    private Tree<N> tree;
    private Map<String, String> paramMap;
    private String seperator;
    private String clientId;

    private static final String EXPANSION_SUFFIX = "_expand";
    private static final String CONTRACTION_SUFFIX = "_contract";
    private static final String SELECTION_SUFFIX = "_select";
    private static final String DESELECTION_SUFFIX = "_deselect";
    private static final String REORDER_SUFFIX = "_reorder";

    public TreeDecoder(FacesContext context, Tree<N> tree) {
        this.context = context;
        this.tree = tree;
        this.seperator = Character.toString(UINamingContainer.getSeparatorChar(context));
        this.clientId = tree.getClientId(context);
    }

    public void decode() {
         if (requestHasParam(clientId + EXPANSION_SUFFIX))
            decodeExpansion();

        if (requestHasParam(clientId + CONTRACTION_SUFFIX))
            decodeContraction();

        if (requestHasParam(clientId + SELECTION_SUFFIX)) {
            if (!tree.isSelectMultiple())
                tree.getStateMap().setAllSelected(false);

            decodeSelection();
        }

        if (requestHasParam(clientId + DESELECTION_SUFFIX))
            decodeDeselection();

        if (requestHasParam(clientId + REORDER_SUFFIX))
            decodeReordering();
    }

    private void decodeReordering() {
        String reorderString = getRequestParam(clientId + REORDER_SUFFIX);
        String[] reorderParts = reorderString.split(">");
        String[] destParts = reorderParts[1].split("@");
        NodeKey sourceKey = tree.getKeyConverter().parseSegments(reorderParts[0].split(seperator));
        NodeKey destKey = clientId.equals(destParts[0])
                ? NodeKey.ROOT_KEY
                : tree.getKeyConverter().parseSegments(destParts[0].split(seperator));
        int index = Integer.parseInt(destParts[1]);

        tree.setKey(sourceKey);

        N source = tree.getData();

        // Insert before remove to persist indexes
        tree.setKey(destKey);
        // Insert removes from old parent
        tree.insertNode(source, index);

        tree.setKey(NodeKey.ROOT_KEY);
    }

    private void decodeDeselection() {
        String deselectedJSON = getRequestParam(clientId + DESELECTION_SUFFIX);
        try {
            JSONArray array = new JSONArray(deselectedJSON);

            for (int i = 0; i < array.length(); i++) {
                tree.setKey(tree.getKeyConverter().parseSegments(array.getString(i).split(seperator)));
                tree.getStateMap()
                        .get(tree.getData())
                        .setSelected(false);
            }
            tree.setKey(NodeKey.ROOT_KEY);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void decodeContraction() {
        String contractedJSON= getRequestParam(clientId + CONTRACTION_SUFFIX);
        try {
            JSONArray array = new JSONArray(contractedJSON);

            for (int i = 0; i < array.length(); i++) {
                tree.setKey(tree.getKeyConverter().parseSegments(array.getString(i).split(seperator)));
                tree.getStateMap()
                        .get(tree.getData())
                        .setExpanded(false);
            }
            tree.setKey(NodeKey.ROOT_KEY);
        } catch (JSONException e) {
            e.printStackTrace();
        }
}

    private void decodeExpansion() {
        String expandedJSON = getRequestParam(clientId + EXPANSION_SUFFIX);
        try {
            JSONArray array = new JSONArray(expandedJSON);

            for (int i = 0; i < array.length(); i++) {
                tree.setKey(tree.getKeyConverter().parseSegments(array.getString(i).split(seperator)));
                tree.getStateMap()
                        .get(tree.getData())
                        .setExpanded(true);
            }
            tree.setKey(NodeKey.ROOT_KEY);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void decodeSelection() {
        String selectedJSON = getRequestParam(clientId + SELECTION_SUFFIX);
        try {
            JSONArray array = new JSONArray(selectedJSON);

            for (int i = 0; i < array.length(); i++) {
                tree.setKey(tree.getKeyConverter().parseSegments(array.getString(i).split(seperator)));
                tree.getStateMap()
                        .get(tree.getData())
                        .setSelected(true);
            }
            tree.setKey(NodeKey.ROOT_KEY);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private boolean requestHasParam(String s) {
        paramMapCheck();
        String o = paramMap.get(s);
        return o != null && o.length() > 0 && !o.equals("[]");
    }

    private String getRequestParam(String s) {
        paramMapCheck();
        return paramMap.get(s);
    }

    private void paramMapCheck() {
        if (paramMap == null)
            paramMap = context.getExternalContext().getRequestParameterMap();
    }
}
