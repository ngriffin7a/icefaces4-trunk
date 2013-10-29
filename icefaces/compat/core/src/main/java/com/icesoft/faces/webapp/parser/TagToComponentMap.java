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

package com.icesoft.faces.webapp.parser;

import com.icesoft.jasper.xmlparser.ParserUtils;
import org.apache.commons.digester.Digester;
import org.apache.commons.digester.Rule;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Attributes;

import java.io.*;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * This class provides a map from TLD tag names to the tag processing class
 * associated with the tag.  The map is used by the parser to establish a
 * ruleset for the digester to use when parsing a JSFX page.
 *
 * @author Steve Maryka
 */
public class TagToComponentMap implements Serializable {

    public static final String XHTML_COMPONENT_TYPE =
            "com.icesoft.faces.XhtmlComponent";
    public static final String XHTML_COMPONENT_CLASS =
            "com.icesoft.faces.component.UIXhtmlComponent";
    private static ClassLoader loader =
            TagToComponentMap.class.getClassLoader();
    private static final Log log = LogFactory.getLog(TagToComponentMap.class);

    private Hashtable tagToComponentMap = new Hashtable();
    private Writer faceletsTaglibXmlWriter;

    private void setFaceletsTaglibXmlWriter(Writer writer) {
        faceletsTaglibXmlWriter = writer;
    }

    /**
     * Build the map from a serialized source.
     *
     * @param fis Input stream for the serialized data.
     * @return The map
     * @throws IOException
     * @throws ClassNotFoundException
     */
    static TagToComponentMap loadFrom(InputStream fis)
            throws IOException, ClassNotFoundException {
        try {
            ObjectInputStream ois = new ObjectInputStream(fis);
            return (TagToComponentMap) ois.readObject();
        } catch (IOException e) {
            log.error("Error building map from TLD tag names", e);
            throw e;
        } catch (ClassNotFoundException e) {
            log.error("Error building map from TLD tag names", e);
            throw e;
        }
        catch (Exception e) {
            return new TagToComponentMap();
        }
    }

    /**
     * Getter for TagToComponentMap
     *
     * @return The tag to tag processing class map.
     */
    public Hashtable getTagToComponentMap() {
        return tagToComponentMap;
    }
    
    /**
     * Takes a TLD file, parses it and build up map from tag name to tag
     * processing class.
     *
     * @param tldInput The TLD to process
     * @throws IOException If digester barfs.
     */
//    public void addTags(InputStream tldInput) throws IOException {
//        /*
//          Use the digester to parse input file looking for <tag> entries, extract the <name>
//          and extract the <tag-class> and build hash table for looking up component
//          classes based on a tag name.
//        */
//        Digester digester = new Digester();
//        digester.setNamespaceAware(true);
//        digester.setValidating(false);
//        digester.setEntityResolver(ParserUtils.entityResolver);
//        digester.setUseContextClassLoader(false);
//
//        /* Need to set the class loader to work.  Not sure why.
//           May need to change when we move behind servlet container or Tomcat */
//        digester.setClassLoader(loader);
//
//        // This rule creates an element we can use to populate the map;
//        digester.addObjectCreate("*/tag",
//                "com.icesoft.faces.webapp.parser.TagToTagClassElement");
//        digester.addObjectCreate("*/uri", "java.lang.StringBuffer");
//
//        // This rule pushes everything into the hash table;
//        NameRule nRule =
//                new NameRule(tagToComponentMap, faceletsTaglibXmlWriter);
//        digester.addRule("*/tag/tag-class", nRule);
//        digester.addRule("*/uri", nRule);
//
//        // These rules scoop the values from <name> and <tag-class> elements;
//        digester.addCallMethod("*/tag/name", "setTagName", 0);
//        digester.addCallMethod("*/tag/tag-class", "setTagClass", 0);
//        digester.addCallMethod("*/uri", "append", 0);
//
//        try {
//            digester.parse(tldInput);
//        } catch (Throwable e) {
//            IOException ioe = new IOException("Can't parse tld " + tldInput.toString());
//            ioe.initCause(e);
//            throw ioe;
//        } finally {
//            tldInput.close();
//        }
//    }   
    
    /**
     * Same as addTags but this one has more info such as attributes/descriptions
     *
     * @param tldInput The TLD to process
     * @throws IOException If digester barfs.
     */
    public void addTagAttrib(InputStream tldInput) throws IOException {

        Digester digester = new Digester();
        digester.setNamespaceAware(true);
        digester.setValidating(false);
        digester.setEntityResolver(ParserUtils.entityResolver);
        digester.setUseContextClassLoader(false);

        /* Need to set the class loader to work.  Not sure why.
           May need to change when we move behind servlet container or Tomcat */
        digester.setClassLoader(loader);

        // This rule creates an element we can use to populate the map;
        digester.addObjectCreate("taglib/tag", "com.icesoft.faces.webapp.parser.TagToTagClassElement");
        digester.addObjectCreate("taglib/uri", "java.lang.StringBuffer");

        // This rule pushes everything into the hash table;
        NameRule nRule =  new NameRule(tagToComponentMap, faceletsTaglibXmlWriter);
        digester.addRule("taglib/tag", nRule);
        digester.addRule("taglib/uri", nRule);

        // These rules scoop the values from <name> and <tag-class> elements;
        digester.addCallMethod("taglib/tag/name", "setTagName", 0);
        digester.addCallMethod("taglib/tag/tag-class", "setTagClass", 0);
        digester.addCallMethod("taglib/tag/description", "setDescription", 0);
        digester.addCallMethod("taglib/uri", "append", 0);

        digester.addObjectCreate("taglib/tag/attribute", "com.icesoft.faces.webapp.parser.AttributeElement");
      
        digester.addCallMethod("taglib/tag/attribute/name", "setName", 0);
        digester.addCallMethod("taglib/tag/attribute/required", "setRequired", 0);
        digester.addCallMethod("taglib/tag/attribute/description", "setDescription", 0);
        digester.addSetNext("taglib/tag/attribute", "addAttribute");
        
        try {
            digester.parse(tldInput);
        } catch (Throwable e) {
            IOException ioe = new IOException("Can't parse tld " + tldInput.toString());
            ioe.initCause(e);
            throw ioe;
        } finally {
            tldInput.close();
        }
    }    


    /**
     * Main method for when this class is run to build the serialized data from
     * a set of TLDS.
     *
     * @param args The runtime arguements.
     */
    public static void main(String args[]) {

        /* arg[0] is "new" to create serialzed data or 'old' to read serialized data
           arg[1] is filename for serialized data;
           arg[2...] are tld's to process */

        FileInputStream tldFile = null;

        TagToComponentMap map = new TagToComponentMap();

        if (args[0].equals("new")) {
            // Build new component map from tlds and serialize it;

            for (int i = 2; i < args.length; i++) {
                try {
                    tldFile = new FileInputStream(args[i]);
                    map.addTagAttrib((InputStream) tldFile);
                }
                catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            }

            try {
                FileOutputStream fos = new FileOutputStream(args[1]);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(map);
                oos.flush();
                oos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (args[0].equals("old")) {
            // Build component from serialized data;
            try {
                FileInputStream fis = new FileInputStream(args[1]);
                ObjectInputStream ois = new ObjectInputStream(fis);
                map = (TagToComponentMap) ois.readObject();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (args[0].equals("facelets")) {
            // Build new component map from tld, and use that to
            //  generate a Facelets taglib.xml
            // args[0] is command
            // args[1] is output taglib.xml
            // args[2] is input tld

            try {
                FileWriter faceletsTaglibXmlWriter = new FileWriter(args[1]);
                String preamble =
                        "<?xml version=\"1.0\"?>\n" +
                        "<facelet-taglib xmlns=\"http://java.sun.com/xml/ns/javaee\"\n" + 
                        "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                        "xsi:schemaLocation=\"http://java.sun.com/xml/ns/javaee " +
                        "http://java.sun.com/xml/ns/javaee/web-facelettaglibrary_2_0.xsd\"\n" +
                        "version=\"2.0\">\n";

                String trailer =
                        "</facelet-taglib>\n";
                faceletsTaglibXmlWriter.write(preamble);

                map.setFaceletsTaglibXmlWriter(faceletsTaglibXmlWriter);
                tldFile = new FileInputStream(args[2]);
               	map.addTagAttrib((InputStream) tldFile);

                faceletsTaglibXmlWriter.write(trailer);
                faceletsTaglibXmlWriter.flush();
                faceletsTaglibXmlWriter.close();
            }
            catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }
}


/**
 * A digest rule for reading tags and creating map elements.
 */
//final class NameRule extends Rule {
//    // A class for adding tag name to current element;
//    private Hashtable componentMap;
//    private Writer faceletsTaglibXmlWriter;
//    private String currentNamespace;
//    private static final Log log = LogFactory.getLog(NameRule.class);
//
//    /**
//     * Constructor.
//     *
//     * @param map    The map being created.
//     * @param writer
//     */
//    public NameRule(Hashtable map, Writer writer) {
//        super();
//        componentMap = map;
//        faceletsTaglibXmlWriter = writer;
//        currentNamespace = null;
//    }
//
//    /**
//     * Do nothing in begin.
//     *
//     * @param attributes The tag attributes
//     * @throws Exception No exception thrown.
//     * @deprecated
//     */
//    public void begin(Attributes attributes) throws Exception {
//    }
//
//    /**
//     * Puts the element into the map.
//     *
//     * @param namespace Not used
//     * @param name      Not used
//     */
//    public void end(String namespace, String name) {
//        if (name.equals("uri")) {
//            if (faceletsTaglibXmlWriter != null) {
//                try {
//                    String ns = digester.peek().toString();
//                    boolean namespaceChanged =
//                            (ns != null && ns.length() > 0) &&
//                                    (currentNamespace == null ||
//                                            !currentNamespace.equals(ns));
//                    if (namespaceChanged) {
//                        currentNamespace = ns;
//                        String nsOutput =
//                                "	<namespace>" + currentNamespace +
//                                        "</namespace>\n";
//                        faceletsTaglibXmlWriter.write(nsOutput);
//                        System.out.print(nsOutput);
//                    }
//                }
//                catch (Exception e) {
//                    System.out.println(
//                            "Problem writing namespace to Facelets taglib.xml.  Exception: " +
//                                    e);
//                }
//            }
//            return;
//        }
//
//        TagToTagClassElement elem = (TagToTagClassElement) digester.peek();
//
//        /* Don't want to duplicate tag entries.  Need JSF tags to be first though */
//        if (componentMap.get(elem.getTagName()) != null) {
//            if (log.isDebugEnabled()) {
//                log.debug("Duplicate Tag " + elem.getTagName() +
//                        " not processed");
//            }
//            return;
//        }
//
//        componentMap.put(elem.getTagName(), elem.getTagClass());
//        if (log.isDebugEnabled()) {
//            log.debug(
//                    "Adding " + elem.getTagName() + ": " + elem.getTagClass());
//        }
//
//        if (faceletsTaglibXmlWriter != null) {
//            try {
//                String tagName = elem.getTagName();
//                String tagClassStr = elem.getTagClass();
//                if (tagName != null && tagClassStr != null &&
//                        tagClassStr.indexOf("com.icesoft") >= 0) {
//                    // We have to have special cases for any tags that
//                    //  are not UIComponents, but are instead simply tags
//                    // Map from JSP tag TabChangeListenerTag to
//                    //  Facelets TabChangeListenerHandler
//                    if (tagName.equals("tabChangeListener")) {
//                        StringBuffer sb = new StringBuffer(256);
//                        sb.append("\t<tag>\n\t\t<tag-name>");
//                        sb.append(tagName);
//                        sb.append("</tag-name>\n\t\t<handler-class>");
//                        sb.append(
//                                "com.icesoft.faces.facelets.TabChangeListenerHandler");
//                        sb.append("</handler-class>\n\t</tag>\n");
//                        faceletsTaglibXmlWriter.write(sb.toString());
//                        System.out.print(sb.toString());
//                    } else {
//                        Class tagClass = Class.forName(tagClassStr);
//                        Object tagObj = tagClass.newInstance();
//                        java.lang.reflect.Method getComponentTypeMeth =
//                                tagClass.getMethod("getComponentType",
//                                        new Class[]{});
//                        String componentType =
//                                (String) getComponentTypeMeth.invoke(
//                                        tagObj, new Object[]{});
//                        java.lang.reflect.Method getRendererTypeMeth =
//                                tagClass.getMethod("getRendererType",
//                                        new Class[]{});
//                        String rendererType =
//                                (String) getRendererTypeMeth.invoke(
//                                        tagObj, new Object[]{});
//
//                        StringBuffer sb = new StringBuffer(256);
//                        sb.append("\t<tag>\n\t\t<tag-name>");
//                        sb.append(tagName);
//                        sb.append(
//                                "</tag-name>\n\t\t<component>\n\t\t\t<component-type>");
//                        sb.append(componentType);
//                        sb.append("</component-type>\n");
//                        if (rendererType != null) {
//                            sb.append("\t\t\t<renderer-type>");
//                            sb.append(rendererType);
//                            sb.append("</renderer-type>\n");
//                        }
//                        //TODO: is this handler necessary?  Yes..required for method binding of custom comps
//                        sb.append("\t\t\t<handler-class>org.icefaces.facelets.tag.icefaces.core.IceComponentHandler</handler-class>\n");
//                        sb.append("\t\t</component>\n\t</tag>\n");
//                        faceletsTaglibXmlWriter.write(sb.toString());
//                        System.out.print(sb.toString());
//                    }
//                }
//            }
//            catch (Exception e) {
//                System.out.println(
//                        "Problem writing tag to Facelets taglib.xml.  Tag name: " +
//                                elem.getTagName() +
//                                ", Tag class: " + elem.getTagClass() +
//                                ", Exception: " + e);
//            }
//        }
//    }
//}

/**
 * Same as NameRule but with more info required by ide
 */
final class NameRule extends Rule {
    // A class for adding tag name to current element;
    private Hashtable componentMap;
    private Writer faceletsTaglibXmlWriter;
    private String currentNamespace;
    private static final Log log = LogFactory.getLog(NameRule.class);

    /**
     * Constructor.
     *
     * @param map    The map being created.
     * @param writer
     */
    public NameRule(Hashtable map, Writer writer) {
        super();
        componentMap = map;
        faceletsTaglibXmlWriter = writer;
        currentNamespace = null;
    }

    /**
     * Do nothing in begin.
     *
     * @param attributes The tag attributes
     * @throws Exception No exception thrown.
     * @deprecated
     */
    public void begin(Attributes attributes) throws Exception {
    }

    /**
     * Puts the element into the map.
     *
     * @param namespace Not used
     * @param name      Not used
     */
    public void end(String namespace, String name) {
        if (name.equals("uri")) {
            if (faceletsTaglibXmlWriter != null) {
                try {
                    String ns = digester.peek().toString();
                    boolean namespaceChanged =
                            (ns != null && ns.length() > 0) &&
                                    (currentNamespace == null ||
                                            !currentNamespace.equals(ns));
                    if (namespaceChanged) {
                        currentNamespace = ns;
                        String nsOutput ="	<namespace>" + currentNamespace + "</namespace>\n";
                        faceletsTaglibXmlWriter.write(nsOutput);
                    }
                }
                catch (Exception e) {
                	log.error("Problem writing ns to eclipse taglib.xml", e);
                }
            }
            return;
        }

        TagToTagClassElement elem = (TagToTagClassElement) digester.peek();

        /* Don't want to duplicate tag entries.  Need JSF tags to be first though */
        if (componentMap.get(elem.getTagName()) != null) {
            if (log.isDebugEnabled()) {
                log.debug("Duplicate Tag " + elem.getTagName() + " not processed");
            }
            return;
        }

        componentMap.put(elem.getTagName(), elem.getTagClass());
        if (log.isDebugEnabled()) {
            log.debug( "Adding " + elem.getTagName() + ": " + elem.getTagClass());
        }

        if (faceletsTaglibXmlWriter != null) {
            try {
                String tagName = elem.getTagName();
                String tagClassStr = elem.getTagClass();
                if (tagName != null && tagClassStr != null &&
                        tagClassStr.indexOf("com.icesoft") >= 0) {
                    // We have to have special cases for any tags that
                    //  are not UIComponents, but are instead simply tags
                    // Map from JSP tag TabChangeListenerTag to
                    //  Facelets TabChangeListenerHandler
                    if (tagName.equals("tabChangeListener")) {
                        StringBuffer sb = new StringBuffer(256);
                        sb.append("\t<tag>\n\t\t<tag-name>");
                        sb.append(tagName);
                        sb.append("</tag-name>\n\t\t<handler-class>");
                        sb.append("com.icesoft.faces.facelets.TabChangeListenerHandler");
                        sb.append("</handler-class>\n\t</tag>\n");
                        faceletsTaglibXmlWriter.write(sb.toString());
                    } else {
                        Class tagClass = Class.forName(tagClassStr);
                        Object tagObj = tagClass.newInstance();
                        java.lang.reflect.Method getComponentTypeMeth =
                                tagClass.getMethod("getComponentType",new Class[]{});
                        String componentType =
                        	(String) getComponentTypeMeth.invoke( tagObj, new Object[]{});
                        java.lang.reflect.Method getRendererTypeMeth =
                                tagClass.getMethod("getRendererType",new Class[]{});
                        String rendererType =
                                (String) getRendererTypeMeth.invoke(tagObj, new Object[]{});

                        StringBuffer sb = new StringBuffer(256);
                        sb.append("\t<tag>\n\t\t<description><![CDATA[");
                        sb.append(elem.getDescription());
                        sb.append("]]></description>\n");
                        sb.append("\t\t<tag-name>");
                        sb.append(tagName);
                        sb.append("</tag-name>\n\t\t<component>\n\t\t\t<component-type>");
                        sb.append(componentType);
                        sb.append("</component-type>\n");
                        if (rendererType != null) {
                            sb.append("\t\t\t<renderer-type>");
                            sb.append(rendererType);
                            sb.append("</renderer-type>\n");
                        }
                        //TODO: is this handler necessary?  Yes..required for method binding of custom comps
                        sb.append("\t\t\t<handler-class>org.icefaces.facelets.tag.icefaces.core.IceComponentHandler</handler-class>\n");
                        sb.append("\t\t</component>\n"); 
                        ArrayList<AttributeElement> list = elem.getAttributes();
                       
                        for (AttributeElement attributeElement : list) {
							sb.append("\t\t<attribute>\n");
							sb.append("\t\t\t<description><![CDATA[");
							sb.append(attributeElement.getDescription());
							sb.append("]]></description>\n");
							
							sb.append("\t\t\t<name>");
							sb.append(attributeElement.getName());
							sb.append("</name>\n");
							
							sb.append("\t\t\t<required>");
							sb.append(attributeElement.getRequired());
							sb.append("</required>\n");
							sb.append("\t\t</attribute>\n");
						}
                        sb.append("\t</tag>\n");
                        faceletsTaglibXmlWriter.write(sb.toString());
                    }
                }
            }
            catch (Exception e) {
                log.error("Problem writing tag to eclipse taglib.xml. Tag name: " +
                    elem.getTagName() + ", Tag class: " + elem.getTagClass() , e);
                        
            }
        }
    }
}
