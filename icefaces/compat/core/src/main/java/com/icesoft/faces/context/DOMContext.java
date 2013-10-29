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

/*
 * $Id: DOMContext.java,v 1.0 2004/07/20 14:02:36 tedg Exp $
 */
package com.icesoft.faces.context;

import org.icefaces.impl.context.DOMPartialViewContext;
import org.icefaces.impl.context.DOMResponseWriter;
import org.icefaces.impl.util.DOMUtils;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * <p><strong>DOMContext</strong> provides a component specific interface to the
 * DOM renderer
 */
public class DOMContext implements java.io.Serializable {
    private static Logger log = Logger.getLogger(DOMContext.class.getName());
    private transient DOMResponseWriter writer;
    private Node cursor;
    private Document document;
    private Node rootNode;
    private Node parentElement;
    private boolean initialized;
    private static String DOM_CONTEXTS = "com.icesoft.faces.domcontext";

    protected DOMContext(DOMResponseWriter writer, Document document,
                         Node parentElement) {
        this.writer = writer;
        this.document = document;
        this.cursor = parentElement;
        this.parentElement = parentElement;
        this.initialized = false;
    }

    /**
     * <p>Determine whether this instance is initialized. An initialized
     * instance is guaranteed to have a root node.</p>
     *
     * @return boolean reflecting whether this instance is initialized.
     */
    public boolean isInitialized() {
        return initialized;
    }

    /**
     * <p>This method returns the DOMContext associated with the specified
     * component.</p>
     *
     * @param facesContext an instance of {@link FacesContext} associated with
     *                     the lifecycle
     * @param component    component associated with this {@link DOMContext}
     * @return the attached {@link DOMContext}
     */
    public static DOMContext attachDOMContext(FacesContext facesContext,
                                              UIComponent component) {
        ResponseWriter responseWriter = facesContext.getResponseWriter();
        DOMResponseWriter domWriter;
        if (responseWriter instanceof DOMResponseWriter) {
            domWriter = (DOMResponseWriter) responseWriter;
        } else {
//            domWriter = createTemporaryDOMResponseWriter(responseWriter, facesContext);
            log.severe("ICEfaces rendering required by icefaces-compat.jar " +
                      "components. Enable via <icecore:config render=\"true\" />.");
            throw new UnsupportedOperationException("ICEfaces rendering required.");
        }
        Node cursorParent = domWriter.getCursorParent();
        Document doc = domWriter.getDocument();
        Map domContexts = getDOMContexts(facesContext);

        DOMContext context = null;
        String clientId =
                component.getClientId(FacesContext.getCurrentInstance());
        if (clientId != null && domContexts.containsKey(clientId)) {
            context = (DOMContext) domContexts.get(clientId);
        }
        if (null == context) {
            context = new DOMContext(domWriter, doc, cursorParent);
            domContexts.put(clientId, context);
        }
        //context may have been severed from the tree at some point
        if (context.isInitialized()) {
            if (!(cursorParent instanceof Element)) {
                context.stepOver();
                return context;
            }
            //TODO: Remove as per ICE-4088
            //both MenuBarRenderer.encodeEnd and
            //CommandLinkRenderer.encodeBegin make use of the
            //attach method and should be fixed
            context.attach((Element) cursorParent);
        }
        context.stepOver();

        return context;
    }
    
    public static DOMContext reattachDOMContext(FacesContext facesContext,
                                                UIComponent component) {
        ResponseWriter responseWriter = facesContext.getResponseWriter();
        DOMResponseWriter domWriter;
        if (responseWriter instanceof DOMResponseWriter) {
            domWriter = (DOMResponseWriter) responseWriter;
        } else {
            domWriter = createTemporaryDOMResponseWriter(responseWriter, facesContext);
        }
        Document doc = domWriter.getDocument();
        Map domContexts = getDOMContexts(facesContext);
        DOMContext context = null;
        String clientId = component.getClientId(FacesContext.getCurrentInstance());
        if (domContexts.containsKey(clientId)) {
            context = (DOMContext) domContexts.get(clientId);
        }
        if (null == context) {
            Node cursorParent = domWriter.getCursorParent();
            context = new DOMContext(domWriter, doc, cursorParent);
            domContexts.put(clientId, context);
        }
        else {
            domWriter.setCursorParent(context.getRootNode());
        }
        return context;
    }

    private static DOMResponseWriter createTemporaryDOMResponseWriter(
            ResponseWriter responseWriter, FacesContext facesContext) {
/*
        try {
            //try to unwrap the DOMResponseWriter, if available
            Method delegateMethod = responseWriter.getClass()
                    .getDeclaredMethod("getWrapped", new Class[]{});
            delegateMethod.setAccessible(true);
            Object delegate = delegateMethod
                    .invoke(responseWriter, (Object[]) null);
            if (delegate instanceof DOMResponseWriter)  {
                return (DOMResponseWriter) delegate;
            }
        } catch (Exception e)  {
        }
        DOMResponseWriter domWriter;
        domWriter = new DOMResponseWriter(facesContext, null, new Configuration() {
            public String getName() {
                return "noop configuration";
            }

            public Configuration getChild(String child) throws ConfigurationException {
                throw new ConfigurationException("child not available");
            }

            public Configuration[] getChildren(String name) throws ConfigurationException {
                throw new ConfigurationException("children not available");
            }

            public String getAttribute(String paramName) throws ConfigurationException {
                throw new ConfigurationException("attribute not available");
            }

            public String getValue() throws ConfigurationException {
                throw new ConfigurationException("value not available");
            }
        }, Collections.EMPTY_LIST, Collections.EMPTY_LIST, null);
        Document doc = domWriter.getDocument();
        Element html = doc.createElement("html");
        doc.appendChild(html);
        Element body = doc.createElement("body");
        html.appendChild(body);
        domWriter.setCursorParent(body);
        return domWriter;
*/
        throw new UnsupportedOperationException("createTemporaryDOMResponseWriter not implemented");
    }

    public static Map getDOMContexts(FacesContext facesContext)  {
        Map requestMap = facesContext.getExternalContext().getRequestMap();
        Map domContexts = (Map) requestMap.get(DOM_CONTEXTS);
        if (null == domContexts)  {
            domContexts = new HashMap();
            requestMap.put(DOM_CONTEXTS, domContexts);
        }
        return domContexts;
    }

    /**
     * <p>Get the DOMContext associated with the component. Do not attach the
     * DOMContext instance to its parent element.</p>
     *
     * @param facesContext
     * @param component    the {@link UIComponent} instance whose DOMContext we
     *                     are retrieving
     * @return {@link DOMContext}
     */
    public static DOMContext getDOMContext(FacesContext facesContext,
                                           UIComponent component) {
        ResponseWriter responseWriter = facesContext.getResponseWriter();
        DOMResponseWriter domWriter;
        if (responseWriter instanceof DOMResponseWriter) {
            domWriter = (DOMResponseWriter) responseWriter;
        } else {
//            domWriter = createTemporaryDOMResponseWriter(responseWriter, facesContext);
            log.severe("ICEfaces rendering required by icefaces-compat.jar " +
                      "components. Enable via <icecore:config render=\"true\" />.");
            throw new UnsupportedOperationException("ICEfaces rendering required.");
        }
        Document doc = domWriter.getDocument();
        Map domContexts = getDOMContexts(facesContext);

        DOMContext context = null;
        String clientId =
                component.getClientId(FacesContext.getCurrentInstance());
        if (domContexts.containsKey(clientId)) {
            context = (DOMContext) domContexts.get(clientId);
        }
        if (null == context) {
            Node cursorParent = domWriter.getCursorParent();
            context = new DOMContext(domWriter, doc, cursorParent);
            domContexts.put(clientId, context);
        }
        return context;
    }

    private void attach(Element cursorParent) {
        if (null == rootNode) { //nothing to attach
            return;
        }
        if (rootNode.equals(cursorParent)) {
            return;
        }

        //TODO needs proper fix
        //Quick & temp fix for ICEfacesWebPresentation application
        //This exception only happens when "rootNode" is ancestor of "cursor"
        if (rootNode.getParentNode() != cursorParent) {
            try {
                //re-attaching on top of another node
                //replace them and assume they will re-attach later
                cursorParent.appendChild(rootNode);
            } catch (DOMException e) {
                //this happens in strea-write mode only.
            }
        }
    }


    /**
     * <p>Creates an element of the type specified. Note that the instance
     * returned implements the <code>Element</code> interface, so attributes can
     * be specified directly on the returned object. <br>In addition, if there
     * are known attributes with default values, <code>Attr</code> nodes
     * representing them are automatically created and attached to the
     * element.</p>
     *
     * @param name the specified Element type to create
     * @return the created element
     */
    public Element createElement(String name) {
        return document.createElement(name);
    }

    /**
     * <p/>
     * Creates a <code>Text</code> node given the specified string. </p>
     * The output is escaped.
     *
     * @param cData The data for the node.
     * @return The new <code>Text</code> object.
     */
    public Text createTextNode(String cData) {
        return document.createTextNode(DOMUtils.escapeAnsi(cData));
    }

    /**
     * <p/>
     * Creates a <code>Text</code> node given the specified string. 
     * The output is not escaped, so the calling code must be carefully
     * audited to ensure that application data is not passed in directly.</p>
     *
     * @param cData The data for the node.
     * @return The new <code>Text</code> object.
     */
    public Text createTextNodeUnescaped(String cData) {
        return document.createTextNode(cData);
    }

    /**
     * <p/>
     * Set the rootNode member variable to the parameter Node. </p>
     *
     * @param rootNode
     */
    public void setRootNode(Node rootNode) {
        this.rootNode = rootNode;
        parentElement.appendChild(rootNode);
        initialized = true;
    }

    /**
     * <p/>
     * Creates an element of the type specified. Note that the instance returned
     * implements the <code>Element</code> interface, so attributes can be
     * specified directly on the returned object. <br>In addition, if there are
     * known attributes with default values, <code>Attr</code> nodes
     * representing them are automatically created and attached to the element.
     * Set the rootNode member variable of this instance to the newly-created
     * Element. </p>
     *
     * @param name
     * @return Element
     */
    public Element createRootElement(String name) {
        Element rootElement = createElement(name);
        setRootNode(rootElement);
        return rootElement;
    }

    void setIsolatedRootNode(Node rootElement) {
        this.rootNode = rootElement;
        initialized = true;
    }

    /**
     * <p>Get the rootNode member variable.</p>
     *
     * @return rootNode the root node of this <code>DOMContext</code> instance
     */
    public Node getRootNode() {
        return rootNode;
    }

    /**
     * Set the position at which the next rendered node will be appended
     *
     * @param cursorParent
     */
    public void setCursorParent(Node cursorParent) {
        this.cursor = cursorParent;
        writer.setCursorParent(cursorParent);
    }

    /**
     * Get the position in the document where the next DOM node will be
     * rendererd.
     */
    public Node getCursorParent() {
        return cursor;
    }


    /**
     * Maintain the cursor and cursor position; step to the position where the
     * next sibling should be rendered.
     */
    public void stepOver() {
        if (null != rootNode && rootNode.getParentNode() != null) {
            setCursorParent(rootNode.getParentNode());
        }
    }

    /**
     * Maintain the cursor and cursor such that the next rendered component will
     * be rendered as a child of the parameter component.
     *
     * @param component
     */
    public void stepInto(UIComponent component) {
        if (rootNode != null) {
            // default behaviour;
            // just like calling setCursorParent at the end of encode begin
            setCursorParent(rootNode);
        }
    }


    /**
     * Retrieve the org.w3c.dom.Document instance associated with this
     * DOMContext
     *
     * @return Document
     */
    public Document getDocument() {
        return document;
    }

    /**
     * Remove all children from Node parent
     *
     * @param parent - the root node to remove
     */
    public static void removeChildren(Node parent) {
        while (parent.hasChildNodes()) {
            parent.removeChild(parent.getFirstChild());
        }
    }

    /**
     * Removes from the root element all children with node name equal to the
     * nodeName parameter
     *
     * @param rootElement
     * @param name
     */
    public static void removeChildrenByTagName(Element rootElement,
                                               String name) {

        Node nextChildToRemove = null;
        while (rootElement.hasChildNodes()
                && ((nextChildToRemove = findChildWithNodeName(rootElement,
                name)) != null)) {
            rootElement.removeChild(nextChildToRemove);
        }
    }

    /**
     * Find and return root's child Node with name nodeName or null if no such
     * child Node exists.
     */
    private static Node findChildWithNodeName(Element root, String nodeName) {
        NodeList children = root.getChildNodes();
        int length = children.getLength();
        for (int i = 0; i < length; i++) {
            Node nextChildNode = children.item(i);
            String name = nextChildNode.getNodeName();
            if (name.equalsIgnoreCase(nodeName)) {
                return nextChildNode;
            }
        }
        return null;
    }

    public static List findChildrenWithNodeName(Element root, String nodeName) {
        NodeList children = root.getChildNodes();
        int length = children.getLength();
        List foundItems = new ArrayList();
        for (int i = 0; i < length; i++) {
            Node nextChildNode = children.item(i);
            String name = nextChildNode.getNodeName();
            if (name.equalsIgnoreCase(nodeName)) {
                foundItems.add(nextChildNode);
            }
        }
        return foundItems;
    }

    public static void enableOnElementUpdateNotify(Element element, String id) {
        element.setAttribute(DOMPartialViewContext.DATA_ELEMENTUPDATE, id);
    }
}
