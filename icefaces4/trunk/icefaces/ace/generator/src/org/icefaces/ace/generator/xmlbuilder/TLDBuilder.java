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

package org.icefaces.ace.generator.xmlbuilder;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

import org.w3c.dom.CDATASection;
import org.w3c.dom.Element;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.icefaces.ace.generator.context.GeneratorContext;
import org.icefaces.ace.generator.utils.PropertyValues;
import org.icefaces.ace.generator.utils.Utility;
import org.icefaces.ace.meta.annotation.*;


public class TLDBuilder extends XMLBuilder{
    private Element tag;
    
    public TLDBuilder() {
        super("components.tld");
        Element root = getDocument().createElement("taglib");
        root.setAttribute("xmlns",              "http://java.sun.com/xml/ns/javaee");
        root.setAttribute("xmlns:xsi",          "http://www.w3.org/2001/XMLSchema-instance");
        root.setAttribute("xsi:schemaLocation", "http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-jsptaglibrary_2_1.xsd");
        root.setAttribute("version",            "2.1");
        getDocument().appendChild(root);
        addNode(root, "display-name", GeneratorContext.getDisplayName());
        addNode(root, "tlib-version", GeneratorContext.getVersion());
        //addNode(root, "jsp-version", "1.2");
        addNode(root, "short-name", GeneratorContext.shortName);
        addNode(root, "uri", GeneratorContext.namespace);
    }

    public void includeFileContents(String mergeInPath) {
        if (mergeInPath == null) {
            return;
        }
        System.out.println("TLD MERGE '"+mergeInPath+"' into '"+getFolder()+"/"+getFileName()+"'");
        File file = new File(mergeInPath);
        if (!file.exists()) {
            return;
        }
        try {
            Element root = getDocument().getDocumentElement();
            Document mergeInDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(mergeInPath));

            NodeList children = mergeInDocument.getDocumentElement().getChildNodes();
            for (int childIndex = 0; childIndex < children.getLength(); childIndex++) {
                Node node = children.item(childIndex);
                String nodeName = node.getNodeName();
                if (nodeName.equals("description") ||
                    nodeName.equals("display-name") ||
                    nodeName.equals("tlib-version") ||
                    nodeName.equals("jsp-version") ||
                    nodeName.equals("short-name") ||
                    nodeName.equals("uri")) {
                    continue;
                }
                node = getDocument().importNode(node, true);
                root.appendChild(node);
            }
        } catch(IOException e) {
            System.out.println("Problem merging in TLD from file '" + mergeInPath + "' : " + e);
            e.printStackTrace();
            System.exit(1);
        } catch(ParserConfigurationException e) {
            System.out.println("Problem configuring parser for merging in TLD from file '" + mergeInPath + "' : " + e);
            e.printStackTrace();
            System.exit(1);
        } catch(SAXException e) {
            System.out.println("Problem parsing when merging in TLD from file '" + mergeInPath + "' : " + e);
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void addTagInfo(Class clazz, JSP jsp) {
        Element root = (Element)getDocument().getDocumentElement();
        tag = getDocument().createElement("tag");
        root.appendChild(tag);
        Element description = getDocument().createElement("description");
        CDATASection descriptionCDATA = getDocument().createCDATASection( jsp.tlddoc() );
        description.appendChild(descriptionCDATA);
        tag.appendChild(description);
        addNode(tag, "name", Utility.getTagName(clazz, jsp));
        addNode(tag, "tag-class", Utility.getTagClassName(clazz, jsp));
        addNode(tag, "body-content", Utility.getBodyContentString(clazz, jsp));
    }

    public void addTagInfo(Class clazz, Component component) {
        Element root = (Element)getDocument().getDocumentElement();
        tag = getDocument().createElement("tag");
        root.appendChild(tag);
        Element description = getDocument().createElement("description");
        CDATASection descriptionCDATA = getDocument().createCDATASection( component.tlddoc() + getFacetsTlddoc(clazz) + getClientEventsTlddoc(clazz));
        description.appendChild(descriptionCDATA);
        tag.appendChild(description);
        addNode(tag, "name", component.tagName());
        addNode(tag, "tag-class", Utility.getTagClassName(component));
        addNode(tag, "body-content", "JSP");
    }
	
    public void addTagInfo(Class clazz, TagHandler tagHandler) {
        Element root = (Element)getDocument().getDocumentElement();
        tag = getDocument().createElement("tag");
        root.appendChild(tag);
        Element description = getDocument().createElement("description");
        CDATASection descriptionCDATA = getDocument().createCDATASection( tagHandler.tlddoc());
        description.appendChild(descriptionCDATA);
        tag.appendChild(description);
        addNode(tag, "name", tagHandler.tagName());
        //addNode(tag, "tag-class", Utility.getTagClassName(component));
		addNode(tag, "tag-class", "");
        addNode(tag, "body-content", "JSP");
    }
    
    public void addAttributeInfo(PropertyValues propertyValues) {
        Element attribute = getDocument().createElement("attribute");
        tag.appendChild(attribute);

        Element description = getDocument().createElement("description");
        String des = propertyValues.tlddoc;
        if (des == null || "null".equals(des) || "".equals(des)) {
        	des = "&nbsp;";
        }
        CDATASection descriptionCDATA = getDocument().createCDATASection(des);
        description.appendChild(descriptionCDATA);
        attribute.appendChild(description);

        String propertyName = propertyValues.resolvePropertyName();
        addNode(attribute, "name", propertyName);
        addNode(attribute, "required", String.valueOf(propertyValues.required));
        addNode(attribute, "rtexprvalue", "true");
        addNode(attribute, "type", propertyValues.getArrayAwareType());
    }       
	
	private String getClientEventsTlddoc(Class clazz) {
	
        if (clazz.isAnnotationPresent(ClientBehaviorHolder.class)) {
            ClientBehaviorHolder clientBehaviorHolder = (ClientBehaviorHolder) clazz.getAnnotation(ClientBehaviorHolder.class);
			ClientEvent[] events = clientBehaviorHolder.events();
			if (events.length > 0) {
				StringBuilder builder = new StringBuilder();
				builder.append("<hr><table border='1' cellpadding='3' cellspacing='0' width='100%'>");
				builder.append("<tr bgcolor='#CCCCFF' class='TableHeadingColor'><td colspan='3'><font size='+2'><b>Client Events</b></font></td></tr><tr><td><b>Name</b></td><td><b>Description</b></td><td><b>Supported classes for argument</b></td><tr>");

				for (int i = 0; i < events.length; i++) {
					builder.append("<tr><td>");
					ClientEvent event = events[i];
					builder.append(event.name());
					builder.append("</td><td>");
					builder.append(event.tlddoc());
					builder.append("</td><td>");
					builder.append("".equals(event.argumentClass()) ? "javax.faces.event.AjaxBehaviorEvent" : event.argumentClass());
					builder.append("</td></tr>");
				}
				builder.append("</table><i>Client events can be used with Client Behaviors and the ace:ajax tag.</i><br>");

				return builder.toString();
			}
		}
		return "";
	}
	
	private String getFacetsTlddoc(Class clazz) {

		Class[] classes = clazz.getDeclaredClasses();
		if (classes.length > 0) {
			boolean hasFacets = false;
			for (int i = 0; i < classes.length; i++) {
				Class childClass = classes[i];
				if (childClass.isAnnotationPresent(Facets.class)) {
					hasFacets = true;
					break;
				}
			}
			
			if (hasFacets) {			
				StringBuilder builder = new StringBuilder();
				builder.append("<hr><table border='1' cellpadding='3' cellspacing='0' width='100%'>");
				builder.append("<tr bgcolor='#CCCCFF' class='TableHeadingColor'><td colspan='2'><font size='+2'><b>Facets</b></font></td></tr>");
				for (int i = 0; i < classes.length; i++) {
					Class childClass = classes[i];
					if (childClass.isAnnotationPresent(Facets.class)) {
						Field[] fields = childClass.getDeclaredFields();
						for (int j = 0; j < fields.length; j++) {
							Field field = fields[j];
							if (field.isAnnotationPresent(Facet.class)) {
								Facet facet = (Facet) field.getAnnotation(Facet.class);
								String name = facet.name().trim();
								if ("".equals(name)) name = field.getName();
								builder.append("<tr><td>");
								builder.append(name);
								builder.append("</td><td>");
								builder.append(facet.tlddoc());
								builder.append("</td></tr>");
							}
						}
					}
				}
				builder.append("</table><br>");

				return builder.toString();
			}
		}
		return "";
	}
}
