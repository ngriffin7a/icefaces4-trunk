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

package org.icefaces.impl.util;

import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Entity;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import javax.faces.component.UIComponent;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class DOMUtils {

    private static Logger log = Logger.getLogger("org.icefaces.util.DOMUtil");

    private static HashSet<String> TAGS_THAT_CAN_CLOSE_SHORT = new HashSet<String>(
            Arrays.asList("img", "input", "br", "hr", "meta", "base", "link", "frame", "col", "area"));

    private static HashSet<String> TAGS_THAT_ALLOW_NEWLINE = new HashSet<String>(
            Arrays.asList("img", "input", "td"));

    private static Pattern CDATA_END = Pattern.compile("]]>");

    //TODO: look at replacing with a lighter, more targetted DOM implementation
    private static DocumentBuilder DOCUMENT_BUILDER;
    private static boolean isDOMChecking = true;

    public static String DIFF_SUPPRESS = "data-ice-diffsuppress";
    public static String DIFF_INSDEL = "data-ice-insdel";
    public static String DIFF_TRUE = "true";

    public static class EditOperation {
        public String id = null;
        //will be Element once pruning is integrated
        public Node element = null;
        public Map<String, String> attributes = null;
        
        public String toString()  {
            return this.getClass().getName() + ":" + id + ":" + element;
        }
    }
    
    public static class InsertOperation extends EditOperation {
        public InsertOperation(String id, Node element)  {
            this.id = id;
            this.element = element;
        }
    }

    public static class DeleteOperation extends EditOperation {
        public DeleteOperation(String id)  {
            this.id = id;
            this.element = null;
        }
    }

    public static class ReplaceOperation extends EditOperation {
        public ReplaceOperation(String id, Node element)  {
            this.id = id;
            this.element = element;
        }
        public ReplaceOperation(Node element)  {
            this(null, element);
        }
    }

    public static class AttributeOperation extends EditOperation {
        public AttributeOperation(String id, Node element, 
                Map<String, String> attributes)  {
            this.id = id;
            this.element = element;
            this.attributes = attributes;
        }
    }

    public static class CursorList {
        public int cursor;
        public List<EditOperation> list;
        
        public CursorList()  {
            list = new ArrayList<EditOperation>();
            cursor = 0;
        }
        
        boolean add(EditOperation op)  {
            assert (null != getNodeId(op.element));
            if (list.size() == cursor)  {
                cursor++;
                return list.add(op);
            }
            list.set(cursor++, op);
            return true;
        }

        boolean addAll(List<EditOperation> ops)  {
            if (list.size() == cursor)  {
                cursor += ops.size();
                return list.addAll(ops);
            }
            //we are in the middle so iterate and add
            for (EditOperation op : ops)  {
                add(op);
            }
            return true;
        }

        public List asList()  {
            return list.subList(0, cursor);
        }
    }

    public static class DiffConfig  {

        public int maxDiffs = Integer.MAX_VALUE;
        public boolean isInsDel = false;
        public boolean isDebug = false;
        public boolean isDebugAB = false;
        public boolean isAtt = false;
        private static String MAXDIFFS = "maxDiffs";
        private static String INSDEL = "insDel";
        private static String DEBUG = "debug";
        private static String DEBUGAB = "debugAB";
        private static String ATT = "att";
        private static Pattern SPACE_PATTERN = Pattern.compile(" ");

        public DiffConfig(String config)  {
            try {
                HashMap<String, String> params = new HashMap();

                String[] paramPairs = SPACE_PATTERN.split(config);
                for (String pair: paramPairs)  {
                    int center = pair.indexOf("=");
                    if (-1 != center)  {
                        String key = pair.substring(0,center);
                        String value = pair.substring(center + 1);
                        params.put(key, value);
                    } else {
                        //singleton values act as flags
                        params.put(pair, "true");
                    }
                }
                
                String maxDiffsParam = (String) params.get(MAXDIFFS);
                if (null != maxDiffsParam)  {
                    maxDiffs = Integer.parseInt(maxDiffsParam);
                }
                
                String insDelParam = (String) params.get(INSDEL);
                if (null != insDelParam)  {
                    isInsDel = true;
                }

                String debugParam = (String) params.get(DEBUG);
                if (null != debugParam)  {
                    isDebug = true;
                }

                String debugABParam = (String) params.get(DEBUGAB);
                if (null != debugABParam)  {
                    isDebugAB = true;
                }

                String attParam = (String) params.get(ATT);
                if (null != attParam)  {
                    isAtt = true;
                }

            } catch (Exception e)  {
                log.log(Level.SEVERE, "Malformed DiffConfig " + config, e);
            }
        }

        public String toString()  {
            String values = 
                    MAXDIFFS + ": " + String.valueOf(maxDiffs) + " " +
                    DEBUG + ": " + String.valueOf(isDebug) + " " +
                    DEBUGAB + ": " + String.valueOf(isDebugAB) + " " +
                    ATT + ": " + String.valueOf(isAtt) + " " +
                    INSDEL + ": " + String.valueOf(isInsDel);
            return values;
        }
    }

    static {
        try {
            DOCUMENT_BUILDER = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            log.log(Level.SEVERE, "unable to acquire a DocumentBuilder", e);
        }
    }

    public static Document getNewDocument() {
        Document doc = DOCUMENT_BUILDER.newDocument();
        applyDocumentSettings(doc);
        return doc;
    }

    private static void applyDocumentSettings(Document doc) {
        if (!isDOMChecking) {
            Method setErrorCheckingMethod = null;
            try {
                setErrorCheckingMethod = doc.getClass().getMethod("setErrorChecking",
                        new Class[]{boolean.class});
                setErrorCheckingMethod.invoke(doc, new Object[]{Boolean.FALSE});
            } catch (Exception e) {
                if (log.isLoggable(Level.FINE)) {
                    log.log(Level.FINE, "DOM error checking not disabled ", e);
                }
            }
        }
    }

    public static String DocumentTypetoString(String publicID, String systemID,
                                              String root) {
        return "<!DOCTYPE " + root + " PUBLIC \"" + publicID + "\" \"" +
                systemID + "\">";
    }

    public static String nodeToString(Node node) throws IOException {
        StringWriter writer = new StringWriter();
        try {
            printNode(node, writer);
        } finally {
            writer.flush();
            return writer.toString();
        }
    }

    public static String childrenToString(Node node) throws IOException {
        StringWriter writer = new StringWriter();
        try {
            printChildNodes(node, writer);
        } finally {
            writer.flush();
            return writer.toString();
        }
    }

    public static void printChildNodes(Node node, Writer writer) throws IOException {
        NodeList children = node.getChildNodes();
        int l = children.getLength();
        for (int i = 0; i < l; i++) {
            printNode(children.item(i), writer);
        }
    }

    public static void printNodeCDATA(Node node, Writer writer) throws IOException {
        printNode(node, writer, 0, true, false, true);
    }

    public static void printNode(Node node, Writer writer) throws IOException {
        printNode(node, writer, 0, true, false, false);
    }

    private static void printNode(
            Node node, Writer writer,
            int depth, boolean allowAddingWhitespace,
            boolean addTrailingNewline, boolean isInCdata) throws IOException {

        switch (node.getNodeType()) {

            case Node.DOCUMENT_NODE:
                //writer.write("<xml version=\"1.0\">\n");
                // recurse on each child
                NodeList nodes = node.getChildNodes();
                if (nodes != null) {
                    for (int i = 0; i < nodes.getLength(); i++) {
                        printNode(nodes.item(i), writer, depth + 1,
                                allowAddingWhitespace, false, isInCdata);
                    }
                }
                break;

            case Node.ELEMENT_NODE:
                String name = node.getNodeName();
                //#2393 removed limited test for <br>

                writer.write("<");
                writer.write(name);
                NamedNodeMap attributes = node.getAttributes();
                for (int i = 0; i < attributes.getLength(); i++) {
                    Node current = attributes.item(i);
                    writer.write(" ");
                    writer.write(current.getNodeName());
                    writer.write("=\"");
                    writer.write(escapeAttribute(current.getNodeValue()));
                    writer.write("\"");
                }


                // #2393 allow short closing of certain tags
                if (!node.hasChildNodes() && xmlShortClosingAllowed(node)) {
                    writer.write(" />");
                    break;
                }

                writer.write(">");
                // recurse on each child
                NodeList children = node.getChildNodes();

                if (children != null) {
                    int childrenLength = children.getLength();
                    for (int i = 0; i < childrenLength; i++) {
                        boolean childAddTrailingNewline = false;
                        if (allowAddingWhitespace) {
                            if ((i + 1) < childrenLength) {
                                Node nextChild = children.item(i + 1);
                                // We don't add the newline if the next tag is a TD,
                                // because when rendering our tabbedPane, if there's
                                // any whitespace between the adjacent TDs, then
                                // Internet Explorer will add vertical spacing
                                // Also same for some other tags to avoid extra space (JIRA ICE-1351)
                                childAddTrailingNewline =
                                        !isWhitespaceText(nextChild) && isNewlineAllowedTag(nextChild);
                            }
                        }
                        printNode(children.item(i), writer, depth + 1,
                                allowAddingWhitespace,
                                childAddTrailingNewline, isInCdata);
                    }
                }

                writer.write("</");
                writer.write(name);
                writer.write(">");
                //ICE-5625: Adding a newline causes issues with some Mojarra tests but can also
                //          affect general layout and value display.
                // if (allowAddingWhitespace && addTrailingNewline) {
                //     writer.write("\n");
                // }
                break;

            case Node.TEXT_NODE:
                if (!isInCdata) {
                    writer.write(node.getNodeValue());
                } else {
                    String value = node.getNodeValue();
                    String escaped = CDATA_END.matcher(value)
                            .replaceAll("]]>]]&gt;<![CDATA[");
                    writer.write(escaped);
                }
                break;

            case Node.CDATA_SECTION_NODE:
                writer.write("<![CDATA[");
                writer.write(node.getNodeValue());
                writer.write("]]>");
                break;
        }
    }

    private static boolean isWhitespaceText(Node node) {
        if (node.getNodeType() == Node.TEXT_NODE) {
            String val = node.getNodeValue();

            // Treat an empty string or null value like whitespace
            if (val == null){
                return true;
            }

            for (int i = val.length() - 1; i >= 0; i--) {
                if (!Character.isWhitespace(val.charAt(i))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private static boolean isNewlineAllowedTag(Node node) {
        short nodeType = node.getNodeType();
        String nodeName = node.getNodeName().toLowerCase();
        return !(nodeType == Node.ELEMENT_NODE &&
                TAGS_THAT_ALLOW_NEWLINE.contains(nodeName));
    }

    /**
     * Check if short closing form is allowed. Short closing is of the form
     * <code> <xxx /></code>
     *
     * @param node Node
     * @return true if allowed
     */
    private static boolean xmlShortClosingAllowed(Node node) {
        short nodeType = node.getNodeType();
        String nodeName = node.getNodeName().toLowerCase();
        return (nodeType == Node.ELEMENT_NODE &&
                TAGS_THAT_CAN_CLOSE_SHORT.contains(nodeName));
    }

    /* Return the first child of the given nodeName under the given node.
     * @param node node to search under
     * @param name nodeName to search for
     */
    public static Node getChildByNodeName(Node node, String name) {
        NodeList children = node.getChildNodes();
        Node child;
        int l = children.getLength();
        for (int i = 0; i < l; i++) {
            child = children.item(i);
            if (child.getNodeName().equalsIgnoreCase(name)) {
                return child;
            }
        }
        return null;
    }

    /**
     * Determine the set of top-level nodes in newDOM that are different from
     * the corresponding nodes in oldDOM. If the DOMs are identical, this
     * method will return an empty array. This method should not be called if one
     * of the DOMs is null.
     *
     * @param config optional diff configuration
     * @param oldDOM original dom Document
     * @param newDOM changed dom Document
     * @return array of top-level nodes in newDOM that differ from oldDOM, an
     *         empty array if no nodes are different
     */
    public static List<EditOperation> domDiff(DiffConfig config,
            Document oldDOM, Document newDOM) {
        return nodeDiff(config, oldDOM.getDocumentElement(), newDOM.getDocumentElement());
    }

    /**
     * Variant of domDiff with default (null) DiffConfig
     */
    public static List<EditOperation> domDiff(Document oldDOM, Document newDOM) {
        return domDiff(null, oldDOM, newDOM);
    }

    /**
     * Determine the set of top-level nodes in newNode subtree that are different from
     * the corresponding nodes in oldNode. If the subtrees are identical, this
     * method will return an empty array. This method should not be called if one
     * of the subtrees is null.
     *
     * @param config optional diff configuration
     * @param oldNode original DOM subtree
     * @param newNode changed DOM subtree
     * @return array of top-level nodes in newNode subtree that differ from
     *         oldNode subtree, an empty array if no nodes are different
     */
    public static List<EditOperation> nodeDiff(DiffConfig config, 
            Node oldNode, Node newNode) {
        CursorList nodeDiffs = new CursorList();
        try {
            if (isDebug(config))  {
                log.log(Level.INFO, "nodeDiff debug " + config);
            }
            if (isDebugAB(config))  {
                dumpDebugAB(oldNode, newNode);
            }
            boolean success;
            success = compareNodes(config, nodeDiffs, oldNode, newNode);
            if (!success)  {
                log.severe("Diff propagated to root but no ID set " + newNode);
                if (isDebugAB(config)) {
                    dumpDebugAB(oldNode, newNode);
                }
            }
            assert checkPrunes(nodeDiffs.asList());
        } catch (Throwable t)  {
            //assert will not normally require a special try/catch
            //but Throwable handling above this is not sufficient
            log.log(Level.SEVERE, "Pruning failure", t);
        }
        return nodeDiffs.asList();
    }

    /**
     * Variant of nodeDiff with default (null) DiffConfig
     */

    public static List<EditOperation> nodeDiff(Node oldNode, Node newNode) {
        return nodeDiff(null, oldNode, newNode);
    }

    /**
     * Nodes are equivalent if they have the same names, attributes, and
     * children
     *
     * @param nodeDiffs
     * @param oldNode
     * @param newNode
     * @return true if diff was handled fully
     */
    private static boolean compareNodes(DiffConfig config, CursorList nodeDiffs, 
            Node oldNode, Node newNode) {

        int startCursor = nodeDiffs.cursor;

        if (!oldNode.getNodeName().equals(newNode.getNodeName())) {
            debugNameDifference(config, newNode, oldNode, newNode);
            return false;
        }
        if (!compareIDs(oldNode, newNode)) {
            debugIdDifference(config, newNode, oldNode, newNode, "A");
            return false;
        }
        if (isSuppressed(newNode))  {
            return true;
        }
        if (isAtt(config))  {
            String id = getNodeId(newNode);
            AttributeOperation op = detectAttributes(id, oldNode, newNode);
            if (null != op)  {
                if (null == id)  {
                    //attributes differ, but no id to apply change
                    debugAttributesDifference(config, newNode, oldNode, newNode, "A");
                    return false;
                }
                if (op.attributes.containsKey("value"))  {
                    //value update will require a special case in jsf.js
                    nodeDiffs.add(new ReplaceOperation(newNode));
                    debugAttributeValueDifference(config, newNode, oldNode, newNode, "A replace");
                    return true;
                }
                nodeDiffs.add(op);
                debugAttributesDifference(config, newNode, oldNode, newNode, "B attribute");
            }
        } else {
            if (!compareAttributes(oldNode, newNode)) {
                String id = getNodeId(newNode);
                if (null == id)  {
                    debugAttributesDifference(config, newNode, oldNode, newNode, "C");
                    return false;
                }
                nodeDiffs.add(new ReplaceOperation(newNode));
                debugAttributesDifference(config, newNode, oldNode, newNode, "D replace");
                return true;
            }
        }
        if (!compareStrings(oldNode.getNodeValue(),
                newNode.getNodeValue())) {
            String id = getNodeId(newNode);
            if (null == id)  {
                debugTextValueDifference(config, newNode, oldNode, newNode, "A");
                return false;
            }
            nodeDiffs.add(new ReplaceOperation(newNode));
            debugTextValueDifference(config, newNode, oldNode, newNode, "B replace");
            return true;
        }

        //if insert/delete is not enabled perform simple DOM diff below
        if (!isInsDel(config, newNode))  {
            NodeList oldChildNodes = oldNode.getChildNodes();
            NodeList newChildNodes = newNode.getChildNodes();

            int oldChildLength = oldChildNodes.getLength();
            int newChildLength = newChildNodes.getLength();

            if (oldChildLength != newChildLength) {
                String id = getNodeId(newNode);
                if (null == id)  {
                    debugChildCountDifference(config, newNode, oldNode, newNode, oldChildLength, newChildLength, "A");
                    return false;
                }
                nodeDiffs.add(new ReplaceOperation(newNode));
                debugChildCountDifference(config, newNode, oldNode, newNode, oldChildLength, newChildLength, "B replace");
                return true;
            }

            for (int i = 0; i < newChildLength; i++) {
                if (!compareNodes(config, nodeDiffs, oldChildNodes.item(i),
                        newChildNodes.item(i))) {
                    String id = getNodeId(newNode);
                    if (null != id)  {
                        //subtree was unable to process the diff
                        nodeDiffs.cursor = startCursor;
                        nodeDiffs.add(new ReplaceOperation(newNode));
                        diffDebug(config, "replace: diff from below ", newNode);
                        return true;
                    }
                    return false;
                }
            }
            if (null != config)  {
                int numDiffs = nodeDiffs.cursor - startCursor;
                if (numDiffs > config.maxDiffs)  {
                    String id = getNodeId(newNode);
                    if (null != id)  {
                        if (log.isLoggable(Level.FINE)) {
                            log.fine("DOM diff coalescing " + 
                                (nodeDiffs.cursor - startCursor) + 
                                " diffs for " + id);
                        }
                        //subtree generated a diff that was too large
                        nodeDiffs.cursor = startCursor;
                        nodeDiffs.add(new ReplaceOperation(newNode));
                        diffDebug(config, "replace: exceeded maxDiffs ", newNode);
                        return true;
                    }
                }
            }
            return true;
        }

        //searching for insert/delete is enabled
        if (!findChildOps(config, nodeDiffs, oldNode, newNode))  {
            String id = getNodeId(newNode);
            if (null != id)  {
                //subtree was unable to process the diff
                nodeDiffs.cursor = startCursor;
                nodeDiffs.add(new ReplaceOperation(newNode));
                diffDebug(config, "replace: diff below ", newNode);
                return true;
            }
            return false;
        }
        
        return true;
   }

    private static String PAD = "not_an_id_of_any_element";

    /**
     * Nodes are equivalent if they have the same names, attributes, and
     * children
     *
     * @param nodeDiffs
     * @param oldNode
     * @param newNode
     * @return true if diff was handled fully
     */
    private static boolean findChildOps(DiffConfig config, CursorList nodeDiffs, 
            Node oldNode, Node newNode) {

        NodeList oldChildNodes = oldNode.getChildNodes();
        NodeList newChildNodes = newNode.getChildNodes();

        int oldChildCount = oldChildNodes.getLength();
        int newChildCount = newChildNodes.getLength();

        if ((0 == oldChildCount) && (0 == newChildCount) )  {
            //the nodes themselves have already been compared
            //so if they both have no children, they match
            return true;
        }
        if ((0 == oldChildCount) || (0 == newChildCount) )  {
            //the node is either newly populated or cleared
            //simple replace is the most efficient
            if (null == getNodeId(newNode))  {
                debugChildCountDifference(config, newNode, oldNode, newNode, oldChildCount, newChildCount, "C");
                return false;
            }
            nodeDiffs.add(new ReplaceOperation(newNode));
            debugChildCountDifference(config, newNode, oldNode, newNode, oldChildCount, newChildCount, "D replace: cleared");
            return true;
        }

        List<String> oldList = getListOfIds(oldNode.getChildNodes());
        List<String> newList = getListOfIds(newNode.getChildNodes());
        List<EditOperation> ops = new ArrayList<EditOperation>();

        List<Node> oldDirectCompare = new ArrayList<Node>();
        List<Node> newDirectCompare = new ArrayList<Node>();

        boolean keepRunning = true;
        int oldListLen = oldList.size();
        int newListLen = newList.size();
        int oldIndex = 0;
        int newIndex = 0;

        while (keepRunning)  {
            //mainly operate on IDs to detect insert and delete
            String currentOld = paddedGet(oldList, oldIndex);
            String currentNew = paddedGet(newList, newIndex);
            if (!currentOld.equals(currentNew))  {
                boolean newInOld = oldList.contains(currentNew);
                boolean oldInNew = newList.contains(currentOld);
                EditOperation operation = null;
                String insertAnchor = null;
                if (newInOld && oldInNew)  {
                    //swap operation is not supported by jsf bridge
                    keepRunning = false;
                    //cancel all operations and replace oldNode with newNode
                    if (null == getNodeId(newNode))  {
                        debugNodeDifference(config, newNode, "Node swapped with other", "A");
                        return false;
                    }
                    nodeDiffs.add(new ReplaceOperation(newNode));
                    debugNodeDifference(config, newNode, "Node swapped with other", "B replace");
                    ops = null;
                    break;
                }
                if (newInOld && !oldInNew)  {
                    operation = new DeleteOperation(currentOld);
                    debugNodeDifference(config, oldNode, "Node deleted " + currentOld, "A");
                    oldIndex++;
                }
                if (!newInOld && oldInNew)  {
                    if (newIndex > 0)  {
                        insertAnchor = paddedGet(newList, newIndex - 1);
                    } else {
                        //TODO Implement InsertBefore
                        //this case is properly handled by an insert "before"
                        if (null == getNodeId(newNode))  {
                            debugNodeDifference(config, newNode, "Node inserted before other", "A");
                            return false;
                        }
                        nodeDiffs.add(new ReplaceOperation(newNode));
                        debugNodeDifference(config, newNode, "Node inserted before other", "B replace");
                        ops = null;
                        break;
                    }
                    if (insertAnchor.startsWith("?"))  {
                        //anchor is not valid so we must replace parent
                        if (null == getNodeId(newNode))  {
                            debugNodeDifference(config, newNode, "Invalid state", "A");
                            return false;
                        }
                        nodeDiffs.add(new ReplaceOperation(newNode));
                        debugNodeDifference(config, newNode, "No insert ID", "B replace");
                        ops = null;
                        break;
                    } else {
                        operation = new InsertOperation(insertAnchor, 
                                newChildNodes.item(newIndex) );
                        debugNodeDifference(config, newNode, "Inserted node", "A");
                        diffDebug(config, "insert: !new old ", 
                                newChildNodes.item(newIndex));
                    }
                    newIndex++;
                }
                if (!newInOld && !oldInNew)  {
                    if (PAD == currentNew)  {
                        operation = new DeleteOperation(currentOld);
                        diffDebug(config, "delete: ins/del " + currentOld, null);
                    } else if (PAD == currentOld)  {
                        if (newIndex > 0)  {
                            insertAnchor = paddedGet(newList, newIndex - 1);
                        } else {
                            //a new child added to an empty parent
                            //can be handled by a parent replace
                            //this should be covered by the length test on entry
                            if (null == getNodeId(newNode))  {
                                debugNodeDifference(config, newNode, "Child added to previously empty parent", "A");
                                return false;
                            }
                            nodeDiffs.add(new ReplaceOperation(newNode));
                            debugNodeDifference(config, newNode, "Child added to previously empty parent", "B replace");
                            ops = null;
                            break;
                        }
                        if (insertAnchor.startsWith("?"))  {
                            //anchor is not valid so we must replace parent
                            if (null == getNodeId(newNode))  {
                                debugNodeDifference(config, newNode, "Invalid state", "C");
                                return false;
                            }
                            nodeDiffs.add(new ReplaceOperation(newNode));
                            debugNodeDifference(config, newNode, "No insert ID", "D replace");
                            ops = null;
                            break;
                        } else {
                            operation = new InsertOperation(insertAnchor, 
                                    newChildNodes.item(newIndex) );
                            debugNodeDifference(config, newNode, "Inserted node", "B");
                            diffDebug(config, "insert2 ", 
                                    newChildNodes.item(newIndex));
                        }
                    } else {
                        //two completely different IDs at this location
                        //cancel and let parent handle
                        if (null == getNodeId(newNode))  {
                            debugIdDifference(config, newNode, oldNode, newNode, "B");
                            return false;
                        }
                        nodeDiffs.add(new ReplaceOperation(newNode));
                        debugIdDifference(config, newNode, oldNode, newNode, "C replace");
                        ops = null;
                        break;
                    }
                    oldIndex++;
                    newIndex++;
                }

                ops.add(operation);

            } else {
                //keep going on match
                oldDirectCompare.add(oldChildNodes.item(oldIndex));
                newDirectCompare.add(newChildNodes.item(newIndex));
                oldIndex++;
                newIndex++;
            }

            if ((oldIndex >= oldListLen) && (newIndex >= newListLen) ) {
                keepRunning = false;
            }
        }

        //examine the subtrees below the identified insert/delete locations
        if (null != ops)  {
            nodeDiffs.addAll(ops);

            int newChildLength = newDirectCompare.size();
            boolean handledBelow = true;
            for (int i = 0; i < newChildLength; i++) {
                if (!compareNodes(config, nodeDiffs, oldDirectCompare.get(i),
                        newDirectCompare.get(i))) {
                    handledBelow = false;
                }
            }
    
            return handledBelow;
        }

        return true;
    }

    private static String paddedGet(List<String> theList, int theIndex)  {
        if (theIndex >= theList.size())  {
            return PAD;
        }
        return theList.get(theIndex);
    }
    
    private static boolean paddedContains(List<String> theList, String theValue)  {
        if (PAD == theValue)  {
            return true;
        }
        return theList.contains(theValue);
    }

    private static List<Node> getListOfNodes(NodeList nodeList)  {
        int length = nodeList.getLength();
        List<Node> listOfNode = new ArrayList<Node>(length);
        for (int i = 0; i < length; i++) {
            listOfNode.add(nodeList.item(i));
        }
        return listOfNode;
    }

    private static List<String> getListOfIds(NodeList nodeList)  {
        int length = nodeList.getLength();
        List<String> listOfIds = new ArrayList<String>(length);
        for (int i = 0; i < length; i++) {
            Node node = nodeList.item(i);
            String id = "?" + i;
            if (node instanceof Element) {
                String tempId = ((Element) node).getAttribute("id");
                if ((null != tempId) && (!"".equals(tempId))) {
                    id = tempId;
                }
            }
            listOfIds.add(id);
        }
        return listOfIds;
    }

    private static boolean compareStrings(String oldString, String newString) {
        if ((null == oldString) && (null == newString)) {
            return true;
        }
        try {
            return (oldString.equals(newString));
        } catch (NullPointerException e) {
        }
        return false;
    }

    /**
     * @param oldNode
     * @param newNode
     * @return true if Nodes have the same IDs
     */
    public static boolean compareIDs(Node oldNode, Node newNode) {
        if (!(oldNode instanceof Element) &&
                !(newNode instanceof Element)) {
            //both do not have an ID
            return true;
        }
        try {
            return ((Element) oldNode).getAttribute("id").equals(
                    ((Element) newNode).getAttribute("id"));
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * @param oldNode
     * @param newNode
     * @return true if Nodes have the same attributes
     */
    public static boolean compareAttributes(Node oldNode, Node newNode) {
        boolean oldHasAttributes = oldNode.hasAttributes();
        boolean newHasAttributes = newNode.hasAttributes();

        if (!oldHasAttributes && !newHasAttributes) {
            return true;
        }
        if (oldHasAttributes != newHasAttributes) {
            return false;
        }

        NamedNodeMap oldMap = oldNode.getAttributes();
        NamedNodeMap newMap = newNode.getAttributes();

        int oldLength = oldMap.getLength();
        int newLength = newMap.getLength();

        if (oldLength != newLength) {
            return false;
        }

        Node newAttribute = null;
        Node oldAttribute = null;
        for (int i = 0; i < newLength; i++) {
            newAttribute = newMap.item(i);
            oldAttribute = oldMap.getNamedItem(newAttribute.getNodeName());
            if (null == oldAttribute) {
                return false;
            }
            if (!(String.valueOf(oldAttribute.getNodeValue()).equals(
                    String.valueOf(newAttribute.getNodeValue())))) {
                return false;
            }
        }

        return true;

    }

    /** The attribute operation sets attributes on the target node, so
     *  deleted attributes are simply cleared.
     * @param oldNode
     * @param newNode
     * @return op if Nodes have different attributes
     */
    public static AttributeOperation detectAttributes(String id, 
            Node oldNode, Node newNode) {
        boolean oldHasAttributes = oldNode.hasAttributes();
        boolean newHasAttributes = newNode.hasAttributes();

        if (!oldHasAttributes && !newHasAttributes) {
            return null;
        }

        Map<String, String> output = new HashMap();

        NamedNodeMap oldMap = oldNode.getAttributes();
        NamedNodeMap newMap = newNode.getAttributes();

        int oldLength = oldMap.getLength();
        int newLength = newMap.getLength();

        Node newAttribute = null;
        Node oldAttribute = null;
        for (int i = 0; i < newLength; i++) {
            newAttribute = newMap.item(i);
            String newName = newAttribute.getNodeName();
            String newValue = newAttribute.getNodeValue();
            oldAttribute = oldMap.getNamedItem(newName);
            if (null == oldAttribute) {
                output.put(newName, newValue);
            } else {
                if (!newValue.equals(oldAttribute.getNodeValue()))  {
                    output.put(newName, newValue);
                }
            }
        }

        for (int i = 0; i < oldLength; i++) {
            oldAttribute = oldMap.item(i);
            String oldName = oldAttribute.getNodeName();
            newAttribute = newMap.getNamedItem(oldName);
            if (null == newAttribute) {
                //clear values of old attributes no longer present
                output.put(oldName, "");
            } 
        }

        if (output.size() > 0)  {
            return new AttributeOperation(id, newNode, output);
        }
        return null;

    }

    public static boolean isDOMDiffFeature(String feature, Node newNode)  {
        if (!newNode.hasAttributes())  {
            return false;
        }
        NamedNodeMap newMap = newNode.getAttributes();
        Node featureMarker = newMap.getNamedItem(feature);
        if (null != featureMarker)  {
            String featureValue = featureMarker.getNodeValue();
            if (DIFF_TRUE.equals(featureValue))  {
                if (log.isLoggable(Level.FINE)) {
                    log.fine("DOM diff " + feature + " on " + newNode);
                }
                return true;
            }
        }
        return false;
    }

    public static boolean isSuppressed(Node newNode)  {
        return isDOMDiffFeature(DIFF_SUPPRESS, newNode);
    }

    public static boolean isInsDel(DiffConfig config, Node newNode)  {
        if ((null != config) && config.isInsDel)  {
            return true;
        }
        return isDOMDiffFeature(DIFF_INSDEL, newNode);
    }

    public static boolean isAtt(DiffConfig config)  {
        if ((null != config) && config.isAtt)  {
            return true;
        }
        return false;
    }

    static void diffDebug(DiffConfig config, String message, Node node)  {
        if (!isDebug(config))  {
            return;
        }
        String nodePath = "";
        Node parent = node;
        while (null != parent)  {
            nodePath = toDebugString(parent) + " " + nodePath;
            parent = parent.getParentNode();
        }
        log.info(message + nodePath);
    }

    static boolean isDebug(DiffConfig config)  {
        return ((null != config) && config.isDebug);
    }

    static boolean isDebugAB(DiffConfig config)  {
        return ((null != config) && config.isDebugAB);
    }

    static void dumpDebugAB(Node oldNode, Node newNode)  {
        String oldDebug = "null document";
        String newDebug = "null document";
        if (null != oldNode)  {
            oldDebug = toDebugStringDeep(oldNode);
        }  
        if (null != newNode)  {
            newDebug = toDebugStringDeep(newNode);
        }  
        log.log(Level.INFO, "nodeDiff--------------debugA\n");
        log.log(Level.INFO, "nodeDiff oldNode \n" + oldDebug);
        log.log(Level.INFO, "nodeDiff--------------debugB\n");
        log.log(Level.INFO, "nodeDiff newNode \n" + newDebug);
        log.log(Level.INFO, "nodeDiff--------------------\n");
    }

    public static String getNodeId(Node node)  {
        if (node instanceof Element) {
            String id = ((Element) node).getAttribute("id");
            if ((null != id) && (!"".equals(id))) {
                return id;
            }
        }
        return null;
    }

    private static boolean pruneCheckWarned = false;
    private static boolean checkPrunes(List<EditOperation> nodeDiffs)  {
        if (!pruneCheckWarned)  {
            log.severe("nodeDiff assertion checking active, disable to improve performance");
            pruneCheckWarned = true;
        }

        List<Node> justNodes = new ArrayList<Node>();
        for (EditOperation op : nodeDiffs)  {
            if (op instanceof ReplaceOperation)  {
                Node startNode = op.element;
                Node ascendNode = ascendToNodeWithID(op.element);
                if (!startNode.equals(ascendNode))  {
                    log.warning("ID missing " + startNode + " " + ascendNode);
                    return false;
                }
                op.element = ascendNode;
                justNodes.add(op.element);
            }
        }

        Node[] prunedDiff = null;
        try {
            prunedDiff = pruneAncestors(justNodes);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (null == prunedDiff)  {
            if (0 == nodeDiffs.size()) {
                return true;
            }
        }
        
        if (nodeDiffs.size() == prunedDiff.length)  {
            return true;
        }

        log.warning("pruning occured " + nodeDiffs.size() + " " + prunedDiff.length);
        return false;
    }

    public static Element ascendToNodeWithID(final Node start) {
        Node node = start;
        while (null != node) {
            if (node instanceof Element) {
                String id = ((Element) node).getAttribute("id");
                if ((null != id) && (!"".equals(id))) {
                    return (Element) node;
                }
            }
            node = node.getParentNode();
        }
        //stop DOM diffing at the root
        return start.getOwnerDocument().getDocumentElement();
    }

    /**
     * Escaping is required unless the escape attribute is present and is
     * "false"
     *
     * @param uiComponent
     * @return
     */
    public static boolean escapeIsRequired(UIComponent uiComponent) {
        Object escapeAttribute = uiComponent.getAttributes().get("escape");
        if (escapeAttribute != null) {
            if (escapeAttribute instanceof String) {
                return Boolean.valueOf((String) escapeAttribute).booleanValue();
            } else if (escapeAttribute instanceof Boolean) {
                return ((Boolean) escapeAttribute).booleanValue();
            }
        }
        return true; //default
    }

    /**
     * Escape Java String and discard illegal characters as suitable for a
     * double quoted "" XML attribute value
     *
     * @param text
     * @return escaped XML attribute value
     */
    public static String escapeAttribute(String text) {
        if (null == text) {
            return "";
        }
        char[] chars = text.toCharArray();
        StringBuilder buffer = new StringBuilder(chars.length);
        for (int index = 0; index < chars.length; index++) {
            char ch = chars[index];
            if (ch <= 31) {
                if (ch == '\t' || ch == '\n' || ch == '\r') {
                    buffer.append(ch);
                }
                //skip any other control character
            } else if (ch == '<') {
                buffer.append("&lt;");
            } else if (ch == '>') {
                buffer.append("&gt;");
            } else if (ch == '&') {
                buffer.append("&amp;");
            } else if (ch == '"') {
                buffer.append("&quot;");
            } else {
                buffer.append(ch);
            }
        }

        return buffer.toString();
    }

    public static String escapeAnsi(String text) {
        if (null == text) {
            return "";
        }
        char[] chars = text.toCharArray();
        StringBuffer buffer = new StringBuffer(chars.length);
        for (int index = 0; index < chars.length; index++) {
            char ch = chars[index];
            //see: http://www.w3schools.com/tags/ref_ascii.asp
            if (ch <= 31) {
                if (ch == '\t' || ch == '\n' || ch == '\r') {
                    buffer.append(ch);
                }
                //skip any other control character
            } else if (ch == 127) {
                //skip 'delete' character
            } else if (ch == '>') {
                buffer.append("&gt;");
            } else if (ch == '<') {
                buffer.append("&lt;");
            } else if (ch == '&') {
                buffer.append("&amp;");
            } else if (ch == '\'') {
                buffer.append("&#39;");
            } else if (ch == '"') {
                buffer.append("&#34;");
            } else if (ch >= 0xA0 && ch <= 0xff) {
                buffer.append("&#").append(Integer.toString(ch)).append(";");
            } else if (ch == 0x20AC) {//special case for euro symbol
                buffer.append("&#8364;");
            } else {
                buffer.append(ch);
            }
        }

        return buffer.toString();
    }

    /**
     * @param character
     * @return
     */
    private static String escapeAnsi(char character) {
        int indexOfEscapedCharacter = character - 0xA0;
        return ansiCharacters[indexOfEscapedCharacter];
    }

    /**
     * from http://www.w3.org/TR/REC-html40/sgml/entities.html
     * Portions Copyright International Organization for Standardization 1986
     * Permission to copy in any form is granted for use with
     * conforming SGML systems and applications as defined in
     * ISO 8879, provided this notice is included in all copies.
     */
    private static String[] ansiCharacters = new String[]{
            "nbsp"
            /* "&#160;" -- no-break space = non-breaking space, U+00A0 ISOnum -->*/,
            "iexcl"  /* "&#161;" -- inverted exclamation mark, U+00A1 ISOnum */,
            "cent"   /* "&#162;" -- cent sign, U+00A2 ISOnum */,
            "pound"  /* "&#163;" -- pound sign, U+00A3 ISOnum */,
            "curren" /* "&#164;" -- currency sign, U+00A4 ISOnum */,
            "yen"    /* "&#165;" -- yen sign = yuan sign, U+00A5 ISOnum */,
            "brvbar"
            /* "&#166;" -- broken bar = broken vertical bar, U+00A6 ISOnum */,
            "sect"   /* "&#167;" -- section sign, U+00A7 ISOnum */,
            "uml"
            /* "&#168;" -- diaeresis = spacing diaeresis, U+00A8 ISOdia */,
            "copy"   /* "&#169;" -- copyright sign, U+00A9 ISOnum */,
            "ordf"
            /* "&#170;" -- feminine ordinal indicator, U+00AA ISOnum */,
            "laquo"
            /* "&#171;" -- left-pointing double angle quotation mark = left pointing guillemet, U+00AB ISOnum */,
            "not"    /* "&#172;" -- not sign, U+00AC ISOnum */,
            "shy"
            /* "&#173;" -- soft hyphen = discretionary hyphen, U+00AD ISOnum */,
            "reg"
            /* "&#174;" -- registered sign = registered trade mark sign, U+00AE ISOnum */,
            "macr"
            /* "&#175;" -- macron = spacing macron = overline = APL overbar, U+00AF ISOdia */,
            "deg"    /* "&#176;" -- degree sign, U+00B0 ISOnum */,
            "plusmn"
            /* "&#177;" -- plus-minus sign = plus-or-minus sign, U+00B1 ISOnum */,
            "sup2"
            /* "&#178;" -- superscript two = superscript digit two = squared, U+00B2 ISOnum */,
            "sup3"
            /* "&#179;" -- superscript three = superscript digit three = cubed, U+00B3 ISOnum */,
            "acute"
            /* "&#180;" -- acute accent = spacing acute, U+00B4 ISOdia */,
            "micro"  /* "&#181;" -- micro sign, U+00B5 ISOnum */,
            "para"
            /* "&#182;" -- pilcrow sign = paragraph sign, U+00B6 ISOnum */,
            "middot"
            /* "&#183;" -- middle dot = Georgian comma = Greek middle dot, U+00B7 ISOnum */,
            "cedil"  /* "&#184;" -- cedilla = spacing cedilla, U+00B8 ISOdia */,
            "sup1"
            /* "&#185;" -- superscript one = superscript digit one, U+00B9 ISOnum */,
            "ordm"
            /* "&#186;" -- masculine ordinal indicator, U+00BA ISOnum */,
            "raquo"
            /* "&#187;" -- right-pointing double angle quotation mark = right pointing guillemet, U+00BB ISOnum */,
            "frac14"
            /* "&#188;" -- vulgar fraction one quarter = fraction one quarter, U+00BC ISOnum --> */,
            "frac12"
            /* "&#189;" -- vulgar fraction one half = fraction one half, U+00BD ISOnum */,
            "frac34"
            /* "&#190;" -- vulgar fraction three quarters = fraction three quarters, U+00BE ISOnum */,
            "iquest"
            /* "&#191;" -- inverted question mark = turned question mark, U+00BF ISOnum */,
            "Agrave"
            /* "&#192;" -- latin capital letter A with grave = latin capital letter A grave, U+00C0 ISOlat1 */,
            "Aacute"
            /* "&#193;" -- latin capital letter A with acute, U+00C1 ISOlat1 */,
            "Acirc"
            /* "&#194;" -- latin capital letter A with circumflex, U+00C2 ISOlat1 */,
            "Atilde"
            /* "&#195;" -- latin capital letter A with tilde, U+00C3 ISOlat1 */,
            "Auml"
            /* "&#196;" -- latin capital letter A with diaeresis, U+00C4 ISOlat1 */,
            "Aring"
            /* "&#197;" -- latin capital letter A with ring above = latin capital letter A ring, U+00C5 ISOlat1 --> */,
            "AElig"
            /* "&#198;" -- latin capital letter AE = latin capital ligature AE, U+00C6 ISOlat1 --> */,
            "Ccedil"
            /* "&#199;" -- latin capital letter C with cedilla, U+00C7 ISOlat1 */,
            "Egrave"
            /* "&#200;" -- latin capital letter E with grave, U+00C8 ISOlat1 */,
            "Eacute"
            /* "&#201;" -- latin capital letter E with acute, U+00C9 ISOlat1 */,
            "Ecirc"
            /* "&#202;" -- latin capital letter E with circumflex, U+00CA ISOlat1 */,
            "Euml"
            /* "&#203;" -- latin capital letter E with diaeresis, U+00CB ISOlat1 */,
            "Igrave"
            /* "&#204;" -- latin capital letter I with grave, U+00CC ISOlat1 */,
            "Iacute"
            /* "&#205;" -- latin capital letter I with acute, U+00CD ISOlat1 */,
            "Icirc"
            /* "&#206;" -- latin capital letter I with circumflex, U+00CE ISOlat1 */,
            "Iuml"
            /* "&#207;" -- latin capital letter I with diaeresis, U+00CF ISOlat1 */,
            "ETH"    /* "&#208;" -- latin capital letter ETH, U+00D0 ISOlat1 */,
            "Ntilde"
            /* "&#209;" -- latin capital letter N with tilde, U+00D1 ISOlat1 */,
            "Ograve"
            /* "&#210;" -- latin capital letter O with grave, U+00D2 ISOlat1 */,
            "Oacute"
            /* "&#211;" -- latin capital letter O with acute, U+00D3 ISOlat1 */,
            "Ocirc"
            /* "&#212;" -- latin capital letter O with circumflex, U+00D4 ISOlat1 */,
            "Otilde"
            /* "&#213;" -- latin capital letter O with tilde, U+00D5 ISOlat1 */,
            "Ouml"
            /* "&#214;" -- latin capital letter O with diaeresis, U+00D6 ISOlat1 */,
            "times"  /* "&#215;" -- multiplication sign, U+00D7 ISOnum */,
            "Oslash"
            /* "&#216;" -- latin capital letter O with stroke = latin capital letter O slash, U+00D8 ISOlat1 */,
            "Ugrave"
            /* "&#217;" -- latin capital letter U with grave, U+00D9 ISOlat1 */,
            "Uacute"
            /* "&#218;" -- latin capital letter U with acute, U+00DA ISOlat1 */,
            "Ucirc"
            /* "&#219;" -- latin capital letter U with circumflex, U+00DB ISOlat1 */,
            "Uuml"
            /* "&#220;" -- latin capital letter U with diaeresis, U+00DC ISOlat1 */,
            "Yacute"
            /* "&#221;" -- latin capital letter Y with acute, U+00DD ISOlat1 */,
            "THORN"
            /* "&#222;" -- latin capital letter THORN, U+00DE ISOlat1 */,
            "szlig"
            /* "&#223;" -- latin small letter sharp s = ess-zed, U+00DF ISOlat1 */,
            "agrave"
            /* "&#224;" -- latin small letter a with grave = latin small letter a grave, U+00E0 ISOlat1 */,
            "aacute"
            /* "&#225;" -- latin small letter a with acute, U+00E1 ISOlat1 */,
            "acirc"
            /* "&#226;" -- latin small letter a with circumflex, U+00E2 ISOlat1 */,
            "atilde"
            /* "&#227;" -- latin small letter a with tilde, U+00E3 ISOlat1 */,
            "auml"
            /* "&#228;" -- latin small letter a with diaeresis, U+00E4 ISOlat1 */,
            "aring"
            /* "&#229;" -- latin small letter a with ring above = latin small letter a ring, U+00E5 ISOlat1 */,
            "aelig"
            /* "&#230;" -- latin small letter ae = latin small ligature ae, U+00E6 ISOlat1 */,
            "ccedil"
            /* "&#231;" -- latin small letter c with cedilla, U+00E7 ISOlat1 */,
            "egrave"
            /* "&#232;" -- latin small letter e with grave, U+00E8 ISOlat1 */,
            "eacute"
            /* "&#233;" -- latin small letter e with acute, U+00E9 ISOlat1 */,
            "ecirc"
            /* "&#234;" -- latin small letter e with circumflex, U+00EA ISOlat1 */,
            "euml"
            /* "&#235;" -- latin small letter e with diaeresis, U+00EB ISOlat1 */,
            "igrave"
            /* "&#236;" -- latin small letter i with grave, U+00EC ISOlat1 */,
            "iacute"
            /* "&#237;" -- latin small letter i with acute, U+00ED ISOlat1 */,
            "icirc"
            /* "&#238;" -- latin small letter i with circumflex, U+00EE ISOlat1 */,
            "iuml"
            /* "&#239;" -- latin small letter i with diaeresis, U+00EF ISOlat1 */,
            "eth"    /* "&#240;" -- latin small letter eth, U+00F0 ISOlat1 */,
            "ntilde"
            /* "&#241;" -- latin small letter n with tilde, U+00F1 ISOlat1 */,
            "ograve"
            /* "&#242;" -- latin small letter o with grave, U+00F2 ISOlat1 */,
            "oacute"
            /* "&#243;" -- latin small letter o with acute, U+00F3 ISOlat1 */,
            "ocirc"
            /* "&#244;" -- latin small letter o with circumflex, U+00F4 ISOlat1 */,
            "otilde"
            /* "&#245;" -- latin small letter o with tilde, U+00F5 ISOlat1 */,
            "ouml"
            /* "&#246;" -- latin small letter o with diaeresis, U+00F6 ISOlat1 */,
            "divide" /* "&#247;" -- division sign, U+00F7 ISOnum */,
            "oslash"
            /* "&#248;" -- latin small letter o with stroke, = latin small letter o slash, U+00F8 ISOlat1 */,
            "ugrave"
            /* "&#249;" -- latin small letter u with grave, U+00F9 ISOlat1 */,
            "uacute"
            /* "&#250;" -- latin small letter u with acute, U+00FA ISOlat1 */,
            "ucirc"
            /* "&#251;" -- latin small letter u with circumflex, U+00FB ISOlat1 */,
            "uuml"
            /* "&#252;" -- latin small letter u with diaeresis, U+00FC ISOlat1 */,
            "yacute"
            /* "&#253;" -- latin small letter y with acute, U+00FD ISOlat1 */,
            "thorn"  /* "&#254;" -- latin small letter thorn,U+00FE ISOlat1 */,
            "yuml"
            /* "&#255;" -- latin small letter y with diaeresis, U+00FF ISOlat1 */,
    };

    private static Node[] pruneAncestors(List nodeList) {
        Node[] changed = (Node[]) nodeList.toArray(new Node[0]);
        HashMap depthMaps = new HashMap();
        for (int i = 0; i < changed.length; i++) {
            Element changeRoot =
                    DOMUtils.ascendToNodeWithID(changed[i]);
            changed[i] = changeRoot;
            Integer depth = new Integer(getDepth(changeRoot));
            HashSet peers = (HashSet) depthMaps.get(depth);
            if (null == peers) {
                peers = new HashSet();
                depthMaps.put(depth, peers);
            }
            //place the node in a collection of all nodes
            //at its same depth in the DOM
            peers.add(changeRoot);
        }
        Iterator allDepths = depthMaps.keySet().iterator();
        while (allDepths.hasNext()) {
            Integer baseDepth = (Integer) allDepths.next();
            Iterator checkDepths = depthMaps.keySet().iterator();
            while (checkDepths.hasNext()) {
                Integer checkDepth = (Integer) checkDepths.next();
                if (baseDepth.intValue() < checkDepth.intValue()) {
                    pruneAncestors(baseDepth, (HashSet) depthMaps.get(baseDepth),
                            checkDepth, (HashSet) depthMaps.get(checkDepth));
                }
            }
        }

        //Merge all remaining elements at different depths
        //Collection is a Set so duplicates will be discarded
        HashSet topElements = new HashSet();
        Iterator allDepthMaps = depthMaps.values().iterator();
        while (allDepthMaps.hasNext()) {
            topElements.addAll((HashSet) allDepthMaps.next());
        }

        Element[] elements = null;
        if (!topElements.isEmpty()) {
            boolean reload = false;
            int j = 0;
            elements = new Element[topElements.size()];
            HashSet dupCheck = new HashSet();
            //copy the succsessful changed elements and check for change
            //to head or body
            for (int i = 0; i < changed.length; i++) {
                Element element = (Element) changed[i];
                String tag = element.getTagName();
                //send reload command if 'html', 'body', or 'head' elements need to be updated (see: ICE-3063)
                //TODO: pass the reload flag back out of this function
                reload = reload || "html".equalsIgnoreCase(tag) || "head".equalsIgnoreCase(tag);
                if (topElements.contains(element)) {
                    if (!dupCheck.contains(element)) {
                        dupCheck.add(element);
                        elements[j++] = element;
                    }
                }
            }
        }

        return elements;
    }

    //prune the children by looking for ancestors in the parents collection 
    private static void pruneAncestors(Integer parentDepth, Collection parents,
                                       Integer childDepth, Collection children) {
        Iterator parentList = parents.iterator();
        while (parentList.hasNext()) {
            Node parent = (Node) parentList.next();
            Iterator childList = children.iterator();
            while (childList.hasNext()) {
                Node child = (Node) childList.next();
                if (isAncestor(parentDepth, parent, childDepth, child)) {
                    childList.remove();
                }
            }

        }
    }

    private static int getDepth(Node node) {
        int depth = 0;
        Node parent = node;
        while ((parent = parent.getParentNode()) != null) {
            depth++;
        }
        return depth;
    }

    private static boolean isAncestor(Integer parentDepth, Node parent,
                                      Integer childDepth, Node child) {
        if (!parent.hasChildNodes()) {
            return false;
        }
        Node testParent = child;
        int testDepth = childDepth.intValue();
        int stopDepth = parentDepth.intValue();
        while (((testParent = testParent.getParentNode()) != null) &&
                (testDepth > stopDepth)) {
            testDepth--;
            if (testParent.equals(parent)) {
                return true;
            }
        }
        return false;
    }


    public static String toDebugStringDeep(Node node) {
        return toDebugStringDeep(node, "");
    }

    static String toDebugStringDeep(Node node, String indent) {
        String result = toDebugString(node) + "\n";
        indent = indent + "  ";
        NodeList nodes = node.getChildNodes();
        if (nodes != null) {
            for (int i = 0; i < nodes.getLength(); i++) {
                result += indent + toDebugStringDeep(nodes.item(i), indent);
            }
        }
        return result;
    }

    public static String toDebugString(Node node) {
        short type = node.getNodeType();
        switch (type) {
            case Node.ATTRIBUTE_NODE: {
                Attr attr = (Attr) node;
                return "attribute[name: " + attr.getName() + "; value: " + attr.getValue() + "]";
            }
            case Node.ELEMENT_NODE: {
                Element element = (Element) node;
                StringBuffer buffer = new StringBuffer();
                buffer.append("element[tag: ");
                buffer.append(element.getTagName());
                buffer.append("; attributes: ");
                NamedNodeMap attributes = element.getAttributes();
                for (int i = 0; i < attributes.getLength(); i++) {
                    Attr attr = (Attr) attributes.item(i);
                    buffer.append(attr.getName());
                    buffer.append("=");
                    buffer.append(attr.getValue());
                    buffer.append(' ');
                }
                buffer.append(']');

                return buffer.toString();
            }
            case Node.CDATA_SECTION_NODE: {
                CDATASection cdataSection = (CDATASection) node;
                return "cdata[" + cdataSection.getData() + "]";
            }
            case Node.TEXT_NODE: {
                Text text = (Text) node;
                return "text[" + text.getData() + "]";
            }
            case Node.COMMENT_NODE: {
                Comment comment = (Comment) node;
                return "comment[" + comment.getData() + "]";
            }
            case Node.ENTITY_NODE: {
                Entity entity = (Entity) node;
                return "entity[public: " + entity.getPublicId() + "; system: " + entity.getSystemId() + "]";
            }
            default: {
                return node.getNodeName();
            }
        }
    }


    public static void debugIdDifference(DiffConfig config,
            Node differenceOrigin, Node oldNode, Node newNode, String variant) {
        if (!isDebug(config))  {
            return;
        }
        String differenceReason = "Id changed from '" + getNodeId(oldNode) +
            "' to '" + getNodeId(newNode) + "'. Examine attributes:\nOld: " +
            describeAttributes(oldNode) + "New: " + describeAttributes(newNode);
        log.info(getDifference(differenceOrigin, variant, differenceReason));
    }

    public static void debugNameDifference(DiffConfig config,
            Node differenceOrigin, Node oldNode, Node newNode) {
        if (!isDebug(config))  {
            return;
        }
        String differenceReason = "Name changed from '" + oldNode.getNodeName() +
            "' to '" + newNode.getNodeName() + "'. Examine attributes:\nOld: " +
            describeAttributes(oldNode) + "New: " + describeAttributes(newNode);
        log.info(getDifference(differenceOrigin, null, differenceReason));
    }

    public static void debugAttributesDifference(DiffConfig config,
            Node differenceOrigin, Node oldNode, Node newNode, String variant) {
        if (!isDebug(config))  {
            return;
        }
        String differenceReason = "Attributes changed\nOld: " +
            describeAttributes(oldNode) + "New: " + describeAttributes(newNode);
        log.info(getDifference(differenceOrigin, variant, differenceReason));
    }

    public static void debugAttributeValueDifference(DiffConfig config,
            Node differenceOrigin, Node oldNode, Node newNode, String variant) {
        if (!isDebug(config))  {
            return;
        }
        String differenceReason = "Attribute 'value' changed\nOld: " +
            describeAttributes(oldNode) + "New: " + describeAttributes(newNode);
        log.info(getDifference(differenceOrigin, variant, differenceReason));
    }

    public static void debugTextValueDifference(DiffConfig config,
            Node differenceOrigin, Node oldNode, Node newNode, String variant) {
        if (!isDebug(config))  {
            return;
        }
        String differenceReason = "Text value changed from '" +
            oldNode.getNodeValue() + "' to '" + newNode.getNodeValue() + "'";
        log.info(getDifference(differenceOrigin, variant, differenceReason));
    }

    public static void debugChildCountDifference(DiffConfig config,
            Node differenceOrigin, Node oldNode, Node newNode,
            int oldChildLength, int newChildLength, String variant) {
        if (!isDebug(config))  {
            return;
        }
        String differenceReason = "Number of children changed from " +
            oldChildLength + " to " + newChildLength + "\nOld: " +
            describeChildren(oldNode) + "New: " + describeChildren(newNode);
        log.info(getDifference(differenceOrigin, variant, differenceReason));
    }


    public static void debugNodeDifference(DiffConfig config, Node differenceOrigin, String variant, String differenceReason) {
        if (!isDebug(config))  {
            return;
        }
        log.info(getDifference(differenceOrigin, variant, differenceReason));
    }

    private static String getDifference(Node differenceOrigin, String variant, String differenceReason) {
        if (differenceOrigin == null) {
            return null;
        }
        int differenceReasonLength = (differenceReason == null) ? 0 : differenceReason.length();
        StringBuilder sb = new StringBuilder(256 + differenceReasonLength);
        Node n = differenceOrigin;
        while (true) {
            describeNodePrepended(n, sb);
            Node p = n.getParentNode();
            if (p == null) {
                break;
            }
            describeNodeIndexInParentPrepended(p, n, sb);
            n = p;
        }
        if (differenceReason != null) {
            sb.append(" :: ");
            if (variant != null) {
                sb.append("(").append(variant).append(") ");
            }
            sb.append(differenceReason);
        }
        return sb.toString();
    }

    private static void describeNodePrepended(Node n, StringBuilder sb) {
        sb.insert(0, ">");
        String id = getNodeId(n);
        if (id != null) {
            sb.insert(0, "\"");
            sb.insert(0, id);
            sb.insert(0, " id=\"");
        }
        sb.insert(0, n.getNodeName());
        sb.insert(0, "<");
    }

    private static void describeNodeIndexInParentPrepended(Node p, Node n, StringBuilder sb) {
        NodeList nl = p.getChildNodes();
        for (int i = nl.getLength()-1; i >= 0; i--) {
            Node c = nl.item(i);
            if (c == n) {
                sb.insert(0, "]");
                sb.insert(0, i);
                sb.insert(0, "[");
                return;
            }
        }
    }

    private static String describeChildren(Node n) {
        StringBuilder sb = new StringBuilder(256);
        NodeList nl = n.getChildNodes();
        sb.append("Children: ").append(nl.getLength()).append('\n');

        for (int i = 0; i < nl.getLength(); i++) {
            sb.append("  [").append(i).append("] ");
            Node c = nl.item(i);
            short type = c.getNodeType();
            switch (type) {
                case Node.CDATA_SECTION_NODE: {
                    CDATASection cdataSection = (CDATASection) c;
                    sb.append("cdata[").append(cdataSection.getData()).append("]\n");
                    break;
                }
                case Node.TEXT_NODE: {
                    Text text = (Text) c;
                    String str = text.getData();
                    sb.append("text");
                    if (str == null || str.length() == 0) {
                        sb.append(":EMPTY\n");
                    } else if (str.trim().length() == 0) {
                        sb.append(":WHITESPACE\n");
                    } else {
                        sb.append("[").append(str).append("]\n");
                    }
                    break;
                }
                case Node.COMMENT_NODE: {
                    Comment comment = (Comment) c;
                    sb.append("comment[").append(comment.getData()).append("]\n");
                    break;
                }
                case Node.ENTITY_NODE: {
                    Entity entity = (Entity) c;
                    sb.append("entity[public: ").append(entity.getPublicId()).append("; system: ").append(entity.getSystemId()).append("]\n");
                    break;
                }
                default:
                case Node.ELEMENT_NODE: {
                    sb.append("<").append(c.getNodeName());
                    String id = getNodeId(c);
                    if (id != null) {
                        sb.append(" id=\"").append(id).append("\"");
                    }
                    sb.append(">\n");
                    if (id == null) {
                        describeAttributes(c, sb, true);
                    }
                }
            }
        }

        return sb.toString();
    }

    private static String describeAttributes(Node n) {
        StringBuilder sb = new StringBuilder(256);
        describeAttributes(n, sb, false);
        return sb.toString();
    }

    private static void describeAttributes(Node n, StringBuilder sb, boolean doubleIndent) {
        NamedNodeMap nnm = n.getAttributes();
        int numAttribs = (nnm == null) ? 0 : nnm.getLength();
        if (doubleIndent) {
            sb.append("  ");
        }
        sb.append("Attributes: ").append(numAttribs).append('\n');

        for (int i = 0; i < numAttribs; i++) {
            Node c = nnm.item(i);
            if (doubleIndent) {
                sb.append("  ");
            }
            sb.append("  [").append(i).append("] ");
            sb.append(c.getNodeName());
            sb.append("=\"").append(c.getNodeValue()).append("\"\n");
        }
    }
}
