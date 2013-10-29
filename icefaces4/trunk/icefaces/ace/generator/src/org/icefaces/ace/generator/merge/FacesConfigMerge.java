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

package org.icefaces.ace.generator.merge;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Combine two or more faces-config.xml files.
 * <ul>
 *     <li>Create XPath expressions for important nodes.</li>
 *     <li>Loads host file as a DOM.</li>
 *     <li>Create references for important nodes in host.</li>
 *     <li>User xPath to fetch nodes from merge DOM, adding the nodes to appropriate locations in the host DOM.</li>
 *     <li>If locations for nodes in host are null, copy entire subtree to host.</li>
 *     <li>Write composite faces-config file to output path.</li>
 * </ul>
 */
public class FacesConfigMerge {
    private static DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
    private static DocumentBuilder builder;

    private static Document hostFile;
    private static Node hostRenderKit;
    private static Node hostRoot;
    private static Node hostApp;
    private static Node hostFac;
    private static Node hostLif;

    private static XPathExpression ROOT;
    private static XPathExpression COMPONENTS;
    private static XPathExpression BEHAVIORS;
    private static XPathExpression RENDER_KIT;
    private static XPathExpression RENDERERS;
    private static XPathExpression APP;
    private static XPathExpression APP_CHILDREN;
    private static XPathExpression FAC;
    private static XPathExpression FAC_CHILDREN;
    private static XPathExpression LIF;
    private static XPathExpression LIF_CHILDREN;


    private static class facesConfigNamespaceContext implements NamespaceContext {
        public String getNamespaceURI(String prefix) {
            if (prefix == null) throw new NullPointerException("Null prefix");
            else if ("f".equals(prefix)) return "http://java.sun.com/xml/ns/javaee";
            return XMLConstants.NULL_NS_URI;
        }
        public String getPrefix(String s) { throw new UnsupportedOperationException(); }
        public Iterator getPrefixes(String s) { throw new UnsupportedOperationException(); }
    }

    public static void main(String[] args) {
        if (args.length < 3 || args[0].equals("?") || args[0].contains("help"))
            usage();

        String hostFilePath = args[0];
        String outputFilePath = args[args.length - 1];
        ArrayList<String> mergeFilePaths = new ArrayList<String>();
        int mergeFilePathIndex;
        for (mergeFilePathIndex = 1; mergeFilePathIndex < (args.length - 1); mergeFilePathIndex++)
            mergeFilePaths.add(args[mergeFilePathIndex]);

        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xPath = xPathFactory.newXPath();
        xPath.setNamespaceContext(new facesConfigNamespaceContext());
        try {
            RENDER_KIT = xPath.compile("/f:faces-config/f:render-kit");
            RENDERERS = xPath.compile("/f:faces-config/f:render-kit/f:renderer");
            ROOT = xPath.compile("/f:faces-config");
            COMPONENTS = xPath.compile("/f:faces-config/f:component");
            BEHAVIORS = xPath.compile("/f:faces-config/f:behavior");
            APP = xPath.compile("/f:faces-config/f:application");
            APP_CHILDREN = xPath.compile("/f:faces-config/f:application/*");
            FAC = xPath.compile("/f:faces-config/f:factory");
            FAC_CHILDREN = xPath.compile("/f:faces-config/f:factory/*");
            LIF = xPath.compile("/f:faces-config/f:lifecycle");
            LIF_CHILDREN = xPath.compile("/f:faces-config/f:lifecycle/*");

            builderFactory.setNamespaceAware(true);
            builder = builderFactory.newDocumentBuilder();

            FacesConfigMerge.parseHostFile(args[0]);
            for (String s : mergeFilePaths) FacesConfigMerge.merge(s);
        }
        catch (IOException e) {
            System.out.println("File couldn't be opened.");
            e.printStackTrace(); }
        catch (SAXException e) {
            System.out.println("XML parser exception.");
            e.printStackTrace(); }
        catch (XPathExpressionException e) { e.printStackTrace(); }
        catch (ParserConfigurationException e) { e.printStackTrace(); }

        FacesConfigMerge.writeMerged(args[args.length - 1]);
    }

    public static void usage() {
        System.out.println("java FacesConfigMerge \"faces-config File A\" \"faces-config File B\" \"Output File\" Merges tags in File B into the taglib File A and writes results into outputFile. There can be multiple file Bs.");
    }

    public static void parseHostFile(String hostPath) throws IOException, SAXException, XPathExpressionException {
        hostFile = builder.parse(new File(hostPath));
        hostRenderKit = ((Node)(RENDER_KIT.evaluate(hostFile, XPathConstants.NODE)));
        hostRoot = ((Node)(ROOT.evaluate(hostFile, XPathConstants.NODE)));
        hostApp = ((Node)(APP.evaluate(hostFile, XPathConstants.NODE)));
        hostFac = ((Node)(FAC.evaluate(hostFile, XPathConstants.NODE)));
        hostLif = ((Node)(LIF.evaluate(hostFile, XPathConstants.NODE)));
    }

    // Merges this faces-config.xml into the one currently in memory.
    public static void merge(String mergePath) throws IOException, SAXException, XPathExpressionException {
        Document mergeFile = builder.parse(new File(mergePath));
        int nodeIndex;
        Node importedNode;

        NodeList mergeRenderers = ((NodeList)RENDERERS.evaluate(mergeFile, XPathConstants.NODESET));
        for (nodeIndex = 0; nodeIndex < mergeRenderers.getLength(); nodeIndex++) {
            importedNode = hostFile.importNode(mergeRenderers.item(nodeIndex), true);
            hostRenderKit.appendChild(importedNode);
        }

        NodeList mergeBehaviors = ((NodeList)BEHAVIORS.evaluate(mergeFile, XPathConstants.NODESET));
        for (nodeIndex = 0; nodeIndex < mergeBehaviors.getLength(); nodeIndex++) {
            importedNode = hostFile.importNode(mergeBehaviors.item(nodeIndex), true);
            hostRoot.appendChild(importedNode);
        }

        NodeList mergeComponents = ((NodeList)COMPONENTS.evaluate(mergeFile, XPathConstants.NODESET));
        for (nodeIndex = 0; nodeIndex < mergeComponents.getLength(); nodeIndex++) {
            importedNode = hostFile.importNode(mergeComponents.item(nodeIndex), true);
            hostRoot.appendChild(importedNode);
        }

        if (hostFac == null) {
            Node mergeFac = ((Node)(FAC.evaluate(mergeFile, XPathConstants.NODE)));
            if (mergeFac != null) {
                importedNode = hostFile.importNode(mergeFac, true);
                hostFac = importedNode;
                hostRoot.insertBefore(importedNode, hostRoot.getFirstChild());
            }
        } else {
            NodeList mergeFacChildren = ((NodeList)FAC_CHILDREN.evaluate(mergeFile, XPathConstants.NODESET));
            for (nodeIndex = 0; nodeIndex < mergeFacChildren.getLength(); nodeIndex++) {
                importedNode = hostFile.importNode(mergeFacChildren.item(nodeIndex), true);
                hostFac.appendChild(importedNode);
            }
        }

        if (hostLif == null) {
            Node mergeLif = ((Node)(LIF.evaluate(mergeFile, XPathConstants.NODE)));
            if (mergeLif != null) {
                importedNode = hostFile.importNode(mergeLif, true);
                hostLif = importedNode;
                hostRoot.insertBefore(importedNode, hostRoot.getFirstChild());
            }
        } else {
            NodeList mergeLifChildren = ((NodeList)LIF_CHILDREN.evaluate(mergeFile, XPathConstants.NODESET));
            for (nodeIndex = 0; nodeIndex < mergeLifChildren.getLength(); nodeIndex++) {
                importedNode = hostFile.importNode(mergeLifChildren.item(nodeIndex), true);
                hostLif.appendChild(importedNode);
            }
        }

        if (hostApp == null) {
            Node mergeApp = ((Node)(APP.evaluate(mergeFile, XPathConstants.NODE)));
            if (mergeApp != null) {
                importedNode = hostFile.importNode(mergeApp, true);
                hostApp = importedNode;
                hostRoot.insertBefore(importedNode, hostRoot.getFirstChild());
            }
        } else {
            NodeList mergeAppChildren = ((NodeList)APP_CHILDREN.evaluate(mergeFile, XPathConstants.NODESET));
            for (nodeIndex = 0; nodeIndex < mergeAppChildren.getLength(); nodeIndex++) {
                importedNode = hostFile.importNode(mergeAppChildren.item(nodeIndex), true);
                hostApp.appendChild(importedNode);
            }
        }
    }

    private static void writeMerged(String outputFilePath) {
        try {
            FileWriter fw = new FileWriter(new File(outputFilePath));
            OutputFormat format = new OutputFormat(hostFile);

            format.setIndenting(true);
            format.setIndent(2);

            XMLSerializer serializer = new XMLSerializer(fw, format);
            serializer.serialize(hostFile);
            fw.close();
        } catch (Exception ex) {
            throw new RuntimeException("Error converting to String", ex);
        }
    }
}
