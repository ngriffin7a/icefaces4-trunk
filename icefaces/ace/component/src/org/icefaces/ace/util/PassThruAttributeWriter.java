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

package org.icefaces.ace.util;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.*;

/**
 * This class is copied over from compat and modified for mobility components
 * pass thru attributes. More jsf-2 oriented, it still does not include
 * client behavior holder support as of alpha release.
 */
public class PassThruAttributeWriter {

    public static final String[] EMPTY_STRING_ARRAY = {};

    private static List passThruAttributeNames = new ArrayList();
    private static List booleanPassThruAttributeNames = new ArrayList();

    static {
        passThruAttributeNames.add("accept");
        passThruAttributeNames.add("accesskey");
        passThruAttributeNames.add("alt");
        passThruAttributeNames.add("bgcolor");
        passThruAttributeNames.add("border");
        passThruAttributeNames.add("cellpadding");
        passThruAttributeNames.add("cellspacing");
        passThruAttributeNames.add("charset");
        passThruAttributeNames.add("cols");
        passThruAttributeNames.add("coords");
        passThruAttributeNames.add("dir");
        passThruAttributeNames.add("enctype");
        passThruAttributeNames.add("frame");
        passThruAttributeNames.add("height");
        passThruAttributeNames.add("hreflang");
        passThruAttributeNames.add("lang");
        passThruAttributeNames.add("longdesc");
        passThruAttributeNames.add("maxlength");
        passThruAttributeNames.add("onblur");
        passThruAttributeNames.add("onchange");
        passThruAttributeNames.add("onclick");
        passThruAttributeNames.add("ondblclick");
        passThruAttributeNames.add("onfocus");
        passThruAttributeNames.add("onkeydown");
        passThruAttributeNames.add("onkeypress");
        passThruAttributeNames.add("onkeyup");
        passThruAttributeNames.add("onload");
        passThruAttributeNames.add("onmousedown");
        passThruAttributeNames.add("onmousemove");
        passThruAttributeNames.add("onmouseout");
        passThruAttributeNames.add("onmouseover");
        passThruAttributeNames.add("onmouseup");
        passThruAttributeNames.add("onreset");
        passThruAttributeNames.add("onselect");
        passThruAttributeNames.add("onsubmit");
        passThruAttributeNames.add("onunload");
        passThruAttributeNames.add("rel");
        passThruAttributeNames.add("rev");
        passThruAttributeNames.add("rows");
        passThruAttributeNames.add("rules");
        passThruAttributeNames.add("shape");
        passThruAttributeNames.add("size");
        passThruAttributeNames.add("style");
        passThruAttributeNames.add("summary");
        passThruAttributeNames.add("tabindex");
        passThruAttributeNames.add("target");
        passThruAttributeNames.add("title");
        passThruAttributeNames.add("usemap");
        passThruAttributeNames.add("width");
        passThruAttributeNames.add("autocomplete");
//why isn't rendered in this next list?
        booleanPassThruAttributeNames.add("disabled");
        booleanPassThruAttributeNames.add("readonly");
        booleanPassThruAttributeNames.add("ismap");
    }


    /**
     * ToDo: Render the icefaces onfocus handler to the root element. This
     * should be restricted to input type elements and commandlinks.
     *
     * @param writer
     * @throws IOException
     */
    public static void renderOnFocus(ResponseWriter writer)
            throws IOException {
        writer.writeAttribute("onfocus", "ice.setFocus(this.id);", "onfocus");
    }

    /**
     * ToDo: Render the icefaces onblur handler to the root element. This should
     * be restricted to input type elements and commandlinks.
     *
     * @param writer
     * @throws IOException
     */
    public static void renderOnBlur(ResponseWriter writer)
            throws IOException {
        writer.writeAttribute("onfocus", "ice.setFocus('');", "onfocus");
    }

    public static void renderBooleanAttributes(
            ResponseWriter writer, UIComponent uiComponent,
            Attribute[] includedAttributes)
            throws IOException {


        if (writer == null) {
            throw new FacesException("Null pointer exception for writer");
        }
        if (uiComponent == null) {
            throw new FacesException("Null pointer exception for uiComponent");
        }

        List includedAttributesList = null;
        if (includedAttributes != null && includedAttributes.length > 0)
            includedAttributesList = Arrays.asList(includedAttributes);

        Object nextPassThruAttributeName;
        Object nextPassThruAttributeValue = null;
        Iterator passThruNameIterator =
                booleanPassThruAttributeNames.iterator();
        boolean primitiveAttributeValue;

        while (passThruNameIterator.hasNext()) {
            nextPassThruAttributeName = (passThruNameIterator.next());
            if (includedAttributesList != null) {
                if (includedAttributesList.contains(nextPassThruAttributeName))
                    continue;
            }
            nextPassThruAttributeValue = uiComponent.getAttributes().get(
                    nextPassThruAttributeName);
            if (nextPassThruAttributeValue != null) {
                if (nextPassThruAttributeValue instanceof Boolean) {
                    primitiveAttributeValue = ((Boolean)
                            nextPassThruAttributeValue).booleanValue();
                } else {
                    if (!(nextPassThruAttributeValue instanceof String)) {
                        nextPassThruAttributeValue =
                                nextPassThruAttributeValue.toString();
                    }
                    primitiveAttributeValue = (new Boolean((String)
                            nextPassThruAttributeValue)).booleanValue();
                }
                if (primitiveAttributeValue) {
                    writer.writeAttribute(nextPassThruAttributeName.toString(),
                            nextPassThruAttributeValue,
                            nextPassThruAttributeName.toString());
                }

            }
        }
    }

    public static void renderNonBooleanAttributes(
            ResponseWriter writer, UIComponent uiComponent,
            Attribute[] includedAttributes)
            throws IOException {
        if (writer == null) {
            throw new FacesException("Null pointer exception for writer");
        }

        if (uiComponent == null) {
            throw new FacesException("Component instance is null for uiComponent");
        }

        ///get list of behaviors but only render if component is not disabled
        Map<String, List<ClientBehavior>> behaviors = null;

        if (uiComponent instanceof ClientBehaviorHolder) {
            behaviors = ((ClientBehaviorHolder) uiComponent).getClientBehaviors();
        }
        //how to test for disabled component??? not doing anything with behaviours until figured out

        List includedAttributesList = null;
        if (includedAttributes != null && includedAttributes.length > 0)
            includedAttributesList = Arrays.asList(includedAttributes);

        Object nextPassThruAttributeName = null;
        Object nextPassThruAttributeValue = null;
        //why go through the whole list? compat version of this goes through entire list
        //       Iterator passThruNameIterator = passThruAttributeNames.iterator();
        Iterator passThruNameIterator = includedAttributesList.iterator();
        while (passThruNameIterator.hasNext()) {
            nextPassThruAttributeName = ((Attribute) (passThruNameIterator.next())).getName();
            //then don't need to exclude anything  check with Mark on logic
//            if (includedAttributesList != null) {
//                if (includedAttributesList.contains(nextPassThruAttributeName))
//                    continue;
//            }
//            System.out.println("nextPassThruAttributeName="+nextPassThruAttributeName);
            nextPassThruAttributeValue =
                    uiComponent.getAttributes().get(nextPassThruAttributeName);
            // Only render non-null attributes.
            // Some components have attribute values
            // set to the Wrapper classes' minimum value - don't render 
            // an attribute with this sentinel value.
            if (nextPassThruAttributeValue != null &&
                    !attributeValueIsSentinel(nextPassThruAttributeValue)) {
                writer.writeAttribute(
                        nextPassThruAttributeName.toString(),
                        nextPassThruAttributeValue,
                        nextPassThruAttributeValue.toString());
            }
        }
    }

    /**
     * Determine whether any of the attributes defined for the UIComponent
     * instance are pass thru attributes.
     *
     * @param uiComponent
     * @return true if the UIComponent parameter has one or more attributes
     *         defined that are pass thru attributes
     */
    public static boolean passThruAttributeExists(UIComponent uiComponent) {
        if (uiComponent == null) {
            return false;
        }
        Map componentAttributes = uiComponent.getAttributes();
        if (componentAttributes.size() <= 0) {
            return false;
        }
        if (componentAttributesIncludePassThruAttribute(componentAttributes,
                passThruAttributeNames)) {
            return true;
        }
        if (componentAttributesIncludePassThruAttribute(componentAttributes,
                booleanPassThruAttributeNames)) {
            return true;
        }
        return false;
    }

    private static boolean attributeValueIsSentinel(Object value) {
        if (value == null) {
            return false;
        }
        if (value instanceof Boolean) {
            if (((Boolean) value).booleanValue() == false) {
                return true;
            }
            return false;
        }
        if (value instanceof Number) {
            if (value instanceof Integer) {
                if (((Integer) value).intValue() == Integer.MIN_VALUE) {
                    return true;
                }
                return false;
            }
            if (value instanceof Long) {
                if (((Long) value).longValue() == Long.MIN_VALUE) {
                    return true;
                }
                return false;
            }
            if (value instanceof Short) {
                if (((Short) value).shortValue() == Short.MIN_VALUE) {
                    return true;
                }
                return false;
            }
            if (value instanceof Float) {
                if (((Float) value).floatValue() == Float.MIN_VALUE) {
                    return true;
                }
                return false;
            }
            if (value instanceof Double) {
                if (((Double) value).doubleValue() == Double.MIN_VALUE) {
                    return true;
                }
                return false;
            }
            if (value instanceof Byte) {
                if (((Byte) value).byteValue() == Byte.MIN_VALUE) {
                    return true;
                }
                return false;
            }
        }
        if (value instanceof Character) {
            if (((Character) value).charValue() == Character.MIN_VALUE) {
                return true;
            }
            return false;
        }
        return false;
    }

    private static boolean componentAttributesIncludePassThruAttribute(
            Map componentAttributes, List passThru) {
        Object componentAttributeKey;
        Object componentAttributeValue;
        Iterator attributeKeys = componentAttributes.keySet().iterator();
        while (attributeKeys.hasNext()) {
            componentAttributeKey = attributeKeys.next();
            if (passThru.contains(componentAttributeKey)) {
                componentAttributeValue =
                        componentAttributes.get(componentAttributeKey);
                if ((componentAttributeValue != null) &&
                        (componentAttributeValue != "")) {
                    return true;
                }
            }
        }
        return false;
    }

    static final List getpassThruAttributeNames() {
        return passThruAttributeNames;
    }

    /**
     * Write pass thru attributes associated with the UIComponent parameter.
     *
     * @param writer
     * @param uiComponent
     * @throws IOException
     */
    public static void renderHtmlAttributes(ResponseWriter writer,
                                            UIComponent uiComponent,
                                            String[] htmlAttributes)
            throws IOException {
        if (writer == null) {
            throw new FacesException("Null pointer exception");
        }

        if (uiComponent == null) {
            throw new FacesException("Component instance is null");
        }

        // Mircea will probably have to look at this to see if it is needed
        //for now assume that we have javax.faces.component.UIComponentBase
//        boolean stockAttribTracking =
//            ImplementationUtil.isStockAttributeTracking();
//        boolean attribTracking =
//            stockAttribTracking &&
//            uiComponent.getClass().getName().startsWith("javax.faces.component.");
//        List attributesThatAreSet = (!attribTracking) ? null :
//            (List) uiComponent.getAttributes().get(
//                "javax.faces.component.UIComponentBase.attributesThatAreSet");
        List attributesThatAreSet = (List) uiComponent.getAttributes().get(
                "javax.faces.component.UIComponentBase.attrbiutesThatAreSet");

        if (attributesThatAreSet.size() > 0) {

            String nextPassThruAttributeName = null;
            Object nextPassThruAttributeValue = null;

            for (int i = 0; i < htmlAttributes.length; i++) {
                nextPassThruAttributeName = htmlAttributes[i];
                if (!attributesThatAreSet.contains(nextPassThruAttributeName)) {
                    continue;
                }
                nextPassThruAttributeValue =
                        uiComponent.getAttributes().get(nextPassThruAttributeName);
                // Only render non-null attributes.
                // Some components have integer attribute values
                // set to the Wrapper classes' minimum value - don't render
                // an attribute with this sentinel value.
                if (nextPassThruAttributeValue != null &&
                        !valueIsIntegerSentinelValue(nextPassThruAttributeValue)) {
                    writer.writeAttribute(
                            nextPassThruAttributeName,
                            nextPassThruAttributeValue,
                            nextPassThruAttributeValue.toString());
                }
            }
        }

    }

    private static boolean valueIsIntegerSentinelValue(Object value) {

        if (value instanceof String) {
            return false;
        } else if (value instanceof Number) {
            if (value instanceof Integer) {
                if (((Integer) value).intValue() == Integer.MIN_VALUE) {
                    return true;
                }
                return false;
            }
        }
        return false;
    }

}
