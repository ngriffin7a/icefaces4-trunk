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

package org.icefaces.impl.event;

import org.icefaces.impl.context.DOMResponseWriter;
import org.icefaces.util.EnvUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.portlet.PortletRequest;
import javax.portlet.filter.PortletRequestWrapper;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.*;
import java.util.logging.Logger;

public class DeltaSubmitPhaseListener implements PhaseListener {
    public static final String PreviousParameters = "previous-parameters";
    private static final String[] StringArray = new String[0];
    private static final Logger log = Logger.getLogger("org.icefaces.event");
    private static final String Append = "patch+";
    private static final String Subtract = "patch-";

    public DeltaSubmitPhaseListener() {
    }

    public void beforePhase(PhaseEvent event) {
        FacesContext facesContext = event.getFacesContext();
        if (EnvUtils.isDeltaSubmit(facesContext)) {
            reconstructParametersFromDeltaSubmit(facesContext);
        }
    }

    public void afterPhase(PhaseEvent event) {
    }

    public PhaseId getPhaseId() {
        return PhaseId.APPLY_REQUEST_VALUES;
    }

    private void reconstructParametersFromDeltaSubmit(FacesContext facesContext) {
        ExternalContext externalContext = facesContext.getExternalContext();
        Map submittedParameters = externalContext.getRequestParameterValuesMap();

        String[] deltaSubmitFormValues = (String[]) submittedParameters.get("ice.deltasubmit.form");
        if (deltaSubmitFormValues == null) {
            //this is not a delta form submission
            return;
        }

        //todo: use a public constant to lookup old DOM document
        //construct previous parameters from the rendered document
        Document oldDOM = DOMResponseWriter.getOldDocument(facesContext);
        Map previousParameters = calculateParametersFromDOM(externalContext, oldDOM);

        final Map parameterValuesMap = new HashMap(previousParameters);
        final ArrayList directParameters = new ArrayList();

        Iterator i = submittedParameters.entrySet().iterator();
        while (i.hasNext()) {
            Map.Entry entry = (Map.Entry) i.next();
            String patchKey = (String) entry.getKey();
            String[] values = (String[]) entry.getValue();

            if (patchKey.startsWith(Append)) {
                String key = patchKey.substring(6);
                String[] previousValues = (String[]) parameterValuesMap.get(key);
                if (previousValues == null) {
                    parameterValuesMap.put(key, values);
                } else {
                    ArrayList allValues = new ArrayList();
                    allValues.addAll(Arrays.asList(previousValues));
                    allValues.addAll(Arrays.asList(values));
                    parameterValuesMap.put(key, allValues.toArray(StringArray));
                }
            } else if (patchKey.startsWith(Subtract)) {
                String key = patchKey.substring(6);
                String[] previousValues = (String[]) parameterValuesMap.get(key);
                if (previousValues == null) {
                    log.fine("Missing previous parameters: " + key);
                } else {
                    if ("".equals(previousValues[0])) {
                        //assuming this is a parameter that corresponds to a hidden input element not cleared by the JS code that controls it
                        previousValues[0] = values[0];
                    }
                    ArrayList allValues = new ArrayList();
                    allValues.addAll(Arrays.asList(previousValues));
                    allValues.removeAll(Arrays.asList(values));
                    if (allValues.isEmpty()) {
                        parameterValuesMap.remove(key);
                    } else {
                        parameterValuesMap.put(key, allValues.toArray(StringArray));
                    }
                }
            } else {
                //add direct parameter only when does not participate in the parameter diffing
                if (!submittedParameters.containsKey(Subtract + patchKey) && !submittedParameters.containsKey(Append + patchKey)) {
                    parameterValuesMap.put(patchKey, values);
                }
                directParameters.add(patchKey);
            }
        }

        orderMultiValueParameters(parameterValuesMap, externalContext, DOMResponseWriter.getOldDocument(facesContext));
        //remove parameters that don't participate in the parameter diffing
        Map newPreviousParameters = new HashMap(parameterValuesMap);
        Iterator directParameterIterator = directParameters.iterator();
        while (directParameterIterator.hasNext()) {
            String parameterName = (String) directParameterIterator.next();
            //don't remove parameter when it also participates in the parameter diffing
            if (!submittedParameters.containsKey(Append + parameterName)) {
                newPreviousParameters.remove(parameterName);
            }
        }

        Object request = externalContext.getRequest();
        if (EnvUtils.instanceofPortletRequest(request)) {
            externalContext.setRequest(new DeltaPortletRequest((PortletRequest) request, parameterValuesMap));
        } else {
            externalContext.setRequest(new DeltaHttpServletRequest((HttpServletRequest) request, parameterValuesMap));
        }
    }

    private void orderMultiValueParameters(Map parameterValuesMap, ExternalContext externalContext, Document doc) {
        Map orderedParameters = calculateMultiValueParameterOrder(externalContext, doc);
        Iterator i = parameterValuesMap.entrySet().iterator();
        while (i.hasNext()) {
            Map.Entry entry = (Map.Entry) i.next();
            String name = (String) entry.getKey();
            String[] unorderedValues = (String[]) entry.getValue();
            String[] orderedValues = (String[]) orderedParameters.get(name);
            if (unorderedValues.length > 1 && orderedValues != null) {
                //sort array in place (the reference remains the same)
                Arrays.sort(unorderedValues, new ParameterOrderComparator(Arrays.asList(orderedValues)));
            }
        }
    }

    private Map calculateMultiValueParameterOrder(ExternalContext externalContext, Document doc) {
        Map multiParameters = new HashMap();
        Map parameters = externalContext.getRequestParameterMap();

        NodeList forms = doc.getElementsByTagName("form");
        for (int i = 0; i < forms.getLength(); i++) {
            Element form = (Element) forms.item(i);
            String formID = form.getAttribute("id");
            if (parameters.containsKey(formID)) {
                NodeList inputs = form.getElementsByTagName("input");
                for (int j = 0; j < inputs.getLength(); j++) {
                    Element input = (Element) inputs.item(j);
                    //submitting type elements are present in the form only if they triggered the submission
                    //ignore radio/checkbox buttons
                    String type = input.getAttribute("type");
                    if ("checkbox".equalsIgnoreCase(type)) {
                        String name = input.getAttribute("name");
                        String value = input.getAttribute("value");
                        String[] values = (String[]) multiParameters.get(name);
                        if (values == null) {
                            multiParameters.put(name, new String[]{value});
                        } else {
                            ArrayList list = new ArrayList(Arrays.asList(values));
                            list.add(value);
                            multiParameters.put(name, list.toArray(StringArray));
                        }
                    }
                }
                NodeList selects = form.getElementsByTagName("select");
                for (int j = 0; j < selects.getLength(); j++) {
                    Element select = (Element) selects.item(j);
                    String name = select.getAttribute("name");
                    NodeList options = select.getElementsByTagName("option");
                    ArrayList values = new ArrayList();
                    for (int k = 0; k < options.getLength(); k++) {
                        Element option = (Element) options.item(k);
                        values.add(option.getAttribute("value"));
                    }
                    if (!values.isEmpty()) {
                        multiParameters.put(name, values.toArray(StringArray));
                    }
                }
                break;
            }
        }

        return multiParameters;
    }

    private Map calculateParametersFromDOM(ExternalContext externalContext, Document doc) {
        Map multiParameters = new HashMap();
        Map parameters = externalContext.getRequestParameterMap();

        NodeList forms = doc.getElementsByTagName("form");
        for (int i = 0; i < forms.getLength(); i++) {
            Element form = (Element) forms.item(i);
            String formID = form.getAttribute("id");
            if (parameters.containsKey(formID)) {
                NodeList inputs = form.getElementsByTagName("input");
                for (int j = 0; j < inputs.getLength(); j++) {
                    Element input = (Element) inputs.item(j);
                    //submitting type elements are present in the form only if they triggered the submission
                    //ignore radio/checkbox buttons
                    String type = input.getAttribute("type");
                    if ("image".equalsIgnoreCase(type) || "button".equalsIgnoreCase(type) || "submit".equalsIgnoreCase(type) || "reset".equalsIgnoreCase(type)) {
                        continue;
                    }

                    String name = input.getAttribute("name");
                    if ("checkbox".equalsIgnoreCase(type) || "radio".equalsIgnoreCase(type)) {
                        if (input.hasAttribute("checked")) {
                            String value = input.getAttribute("value");
                            value = "".equals(value) ? "on" : value;
                            String[] values = (String[]) multiParameters.get(name);
                            if (values == null) {
                                multiParameters.put(name, new String[]{value});
                            } else {
                                ArrayList list = new ArrayList(Arrays.asList(values));
                                list.add(value);
                                multiParameters.put(name, list.toArray(StringArray));
                            }
                        }
                    } else {
                        String value = input.getAttribute("value");
                        String[] values = (String[]) multiParameters.get(name);
                        if (values == null) {
                            multiParameters.put(name, new String[]{value});
                        } else {
                            ArrayList list = new ArrayList(Arrays.asList(values));
                            list.add(value);
                            multiParameters.put(name, list.toArray(StringArray));
                        }
                    }
                }
                NodeList textareas = form.getElementsByTagName("textarea");
                for (int j = 0; j < textareas.getLength(); j++) {
                    Element txtarea = (Element) textareas.item(j);
                    String name = txtarea.getAttribute("name");
                    if (!parameters.containsKey(name)) {
                        Node child = txtarea.getFirstChild();
                        String value = child == null ? "" : child.getNodeValue();
                        multiParameters.put(name, new String[]{value});
                    }
                }
                NodeList selects = form.getElementsByTagName("select");
                for (int j = 0; j < selects.getLength(); j++) {
                    Element select = (Element) selects.item(j);
                    String name = select.getAttribute("name");
                    if (!parameters.containsKey(name)) {
                        NodeList options = select.getElementsByTagName("option");
                        ArrayList selectedOptions = new ArrayList();
                        for (int k = 0; k < options.getLength(); k++) {
                            Element option = (Element) options.item(k);
                            String selectedAttribute = option.getAttribute("selected");
                            if ("selected".equalsIgnoreCase(selectedAttribute) || "true".equalsIgnoreCase(selectedAttribute)) {
                                selectedOptions.add(option.getAttribute("value"));
                            }
                        }
                        if (selectedOptions.isEmpty()) {
                            if (options.getLength() > 0) {
                                //select the first item if no option is marked as selected
                                Element option = (Element) options.item(0);
                                multiParameters.put(name, new String[] {option.getAttribute("value")});
                            }
                        } else {
                            multiParameters.put(name, selectedOptions.toArray(StringArray));
                        }
                    }
                }
                break;
            }
        }

        return multiParameters;
    }

    private static class DeltaHttpServletRequest extends HttpServletRequestWrapper {
        private final Map parameterValuesMap;

        public DeltaHttpServletRequest(HttpServletRequest originalRequest, Map parameterValuesMap) {
            super(originalRequest);
            this.parameterValuesMap = parameterValuesMap;
        }

        public String getParameter(String s) {
            String[] values = (String[]) parameterValuesMap.get(s);
            return values == null ? null : values[0];
        }

        public String[] getParameterValues(String s) {
            return (String[]) parameterValuesMap.get(s);
        }

        public Enumeration<String> getParameterNames() {
            return Collections.enumeration(parameterValuesMap.keySet());
        }

        public Map getParameterMap() {
            return Collections.unmodifiableMap(parameterValuesMap);
        }
    }

    private static class DeltaPortletRequest extends PortletRequestWrapper {
        private final Map parameterValuesMap;

        public DeltaPortletRequest(PortletRequest originalRequest, Map parameterValuesMap) {
            super(originalRequest);
            this.parameterValuesMap = parameterValuesMap;
        }

        public String getParameter(String s) {
            String[] values = (String[]) parameterValuesMap.get(s);
            return values == null ? null : values[0];
        }

        public String[] getParameterValues(String s) {
            return (String[]) parameterValuesMap.get(s);
        }

        public Enumeration<String> getParameterNames() {
            return Collections.enumeration(parameterValuesMap.keySet());
        }

        public Map getParameterMap() {
            return Collections.unmodifiableMap(parameterValuesMap);
        }
    }

    private static class ParameterOrderComparator implements Comparator<String> {
        private final List orderedValueList;

        public ParameterOrderComparator(List orderedValueList) {
            this.orderedValueList = orderedValueList;
        }

        public int compare(String a, String b) {
            return orderedValueList.indexOf(a) - orderedValueList.indexOf(b);
        }
    }
}
