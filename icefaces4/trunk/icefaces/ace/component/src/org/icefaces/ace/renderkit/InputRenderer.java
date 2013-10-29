/*
 * Original Code Copyright Prime Technology.
 * Subsequent Code Modifications Copyright 2011-2012 ICEsoft Technologies Canada Corp. (c)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * NOTE THIS CODE HAS BEEN MODIFIED FROM ORIGINAL FORM
 *
 * Subsequent Code Modifications have been made and contributed by ICEsoft Technologies Canada Corp. (c).
 *
 * Code Modification 1: Integrated with ICEfaces Advanced Component Environment.
 * Contributors: ICEsoft Technologies Canada Corp. (c)
 *
 * Code Modification 2: [ADD BRIEF DESCRIPTION HERE]
 * Contributors: ______________________
 * Contributors: ______________________
 */
package org.icefaces.ace.renderkit;

import java.util.*;
import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UISelectItem;
import javax.faces.component.UISelectItems;
import javax.faces.context.FacesContext;
import java.io.IOException;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;

import javax.el.ValueExpression;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

public class InputRenderer extends CoreRenderer {

    public static final String LABEL_STYLE_CLASS = "ui-input-label";
    public static final String IN_FIELD_LABEL_STYLE_CLASS = LABEL_STYLE_CLASS + "-infield";
    public static final Set<String> labelPositionSet = new HashSet<String>(Arrays.asList("left", "right", "top", "bottom", "inField", "none"));
    public static final Set<String> indicatorPositionSet = new HashSet<String>(Arrays.asList("left", "right", "top", "bottom", "labelLeft", "labelRight", "none"));

    protected List<SelectItem> getSelectItems(FacesContext context, UIInput component) {
        List<SelectItem> selectItems = new ArrayList<SelectItem>();

        for(UIComponent child : component.getChildren()) {
            if(child instanceof UISelectItem) {
                UISelectItem uiSelectItem = (UISelectItem) child;

				selectItems.add(new SelectItem(uiSelectItem.getItemValue(), uiSelectItem.getItemLabel()));
			}
            else if(child instanceof UISelectItems) {
                UISelectItems uiSelectItems = ((UISelectItems) child);
				Object value = uiSelectItems.getValue();

                if(value instanceof SelectItem[]) {
                    selectItems.addAll(Arrays.asList((SelectItem[]) value));
                }
                else if(value instanceof Map) {
                    Map map = (Map) value;

                    for(Iterator it = map.keySet().iterator(); it.hasNext();) {
                        Object key = it.next();

                        selectItems.add(new SelectItem(map.get(key), String.valueOf(key)));
                    }
                }
                else if(value instanceof Collection) {
                    Collection collection = (Collection) value;
                    String var = (String) uiSelectItems.getAttributes().get("var");

                    if(var != null) {
                        for(Iterator it = collection.iterator(); it.hasNext();) {
                            Object object = it.next();
                            context.getExternalContext().getRequestMap().put(var, object);
                            String itemLabel = (String) uiSelectItems.getAttributes().get("itemLabel");
                            Object itemValue = uiSelectItems.getAttributes().get("itemValue");

                            selectItems.add(new SelectItem(itemValue, itemLabel));
                        }
                    } else {
                        for(Iterator it = collection.iterator(); it.hasNext();) {
                            selectItems.add((SelectItem) it.next());
                        }
                    }                    
                }
			}
        }

        return selectItems;
	}

	protected String getOptionAsString(FacesContext context, UIInput component, Converter converter, Object value) {
		if(converter != null)
			return converter.getAsString(context, component, value);
		else if(value == null)
            return "";
        else
            return value.toString();
	}

    protected Converter getConverter(FacesContext context, UIInput component) {
        Converter converter = component.getConverter();

        if(converter != null) {
            return converter;
        } else {
            ValueExpression ve = component.getValueExpression("value");

            if(ve != null) {
                Class<?> valueType = ve.getType(context.getELContext());
                
                return context.getApplication().createConverter(valueType);
            }
        }

        return null;
    }
	
	@Override
	public Object getConvertedValue(FacesContext context, UIComponent component, Object submittedValue) throws ConverterException {
		UIInput uiInput = (UIInput) component;
		String value = (String) submittedValue;
		Converter converter = uiInput.getConverter();
		
		//first ask the converter
		if(converter != null) {
			return converter.getAsObject(context, uiInput, value);
		}
		//Try to guess
		else {
            ValueExpression ve = uiInput.getValueExpression("value");

            if(ve != null) {
                Class<?> valueType = ve.getType(context.getELContext());
                Converter converterForType = context.getApplication().createConverter(valueType);

                if(converterForType != null) {
                    return converterForType.getAsObject(context, uiInput, value);
                }
            }
		}
		
		return value;
	}

    protected void encodeLabelAndInput(UIComponent component, Map<String, Object> labelAttributes, Map<String, Object> domUpdateMap) throws IOException {
        writeLabelAndIndicatorBefore(labelAttributes);
        writeInputField(component, labelAttributes, domUpdateMap);
        writeLabelAndIndicatorAfter(labelAttributes);
    }

    protected void writeInputField(UIComponent component, Map<String, Object> labelAttributes, Map<String, Object> domUpdateMap) throws IOException {
    }

    private void writeHiddenLabel(ResponseWriter writer, String label) throws IOException {
        writer.startElement("span", null);
        writer.writeAttribute("class", LABEL_STYLE_CLASS + " hidden", null);
        writer.write(label);
        writer.endElement("span");
    }

    private void writeLabel(Map<String, Object> attributes) throws IOException {
        ResponseWriter writer = FacesContext.getCurrentInstance().getResponseWriter();
        boolean required = (Boolean) attributes.get("required"), hasIndicator = (Boolean) attributes.get("hasIndicator");
        String clientId = (String) attributes.get("clientId");
        String label = (String) attributes.get("label"), labelPosition = (String) attributes.get("labelPosition");
        String indicator = (String) attributes.get("indicator"), indicatorPosition = (String) attributes.get("indicatorPosition");
        if (hasIndicator) {
            if (indicatorPosition.equals("labelLeft") || indicatorPosition.equals("labelRight")) {
                writer.startElement("span", null);
                writer.writeAttribute("class", LABEL_STYLE_CLASS + " " + LABEL_STYLE_CLASS + "-" + labelPosition, null);
            }
/*
            if (indicatorPosition.equals("labelTop") || indicatorPosition.equals("labelBottom")) {
                writer.startElement("span", null);
                writer.writeAttribute("style", "float:left;", null);
            }
*/
            if (indicatorPosition.equals("labelLeft")/* || indicatorPosition.equals("labelTop")*/) {
                writeIndicator(writer, required, indicator, indicatorPosition);
            }
/*
            if (indicatorPosition.equals("labelTop")) {
                writer.startElement("br", null);
                writer.endElement("br");
            }
*/
        }
        writer.startElement("span", null);
        writer.writeAttribute("id", "label_" + clientId, null);
        if (!hasIndicator || (!indicatorPosition.equals("labelLeft") && !indicatorPosition.equals("labelRight"))) {
            writer.writeAttribute("class", LABEL_STYLE_CLASS + " " + LABEL_STYLE_CLASS + "-" + labelPosition, null);
        }
        writer.write(label);
        writer.endElement("span");
        if (hasIndicator) {
/*
            if (indicatorPosition.equals("labelBottom")) {
                writer.startElement("br", null);
                writer.endElement("br");
            }
*/
            if (indicatorPosition.equals("labelRight")/* || indicatorPosition.equals("labelBottom")*/) {
                writeIndicator(writer, required, indicator, indicatorPosition);
            }
/*
            if (indicatorPosition.equals("labelTop") || indicatorPosition.equals("labelBottom")) {
                writer.endElement("span");
            }
*/
            if (indicatorPosition.equals("labelLeft") || indicatorPosition.equals("labelRight")) {
                writer.endElement("span");
            }
        }
    }

    private void writeIndicator(ResponseWriter writer, boolean required, String indicator, String indicatorPosition) throws IOException {
        String class1 = "ui-" + (required ? "required" : "optional") + "-indicator";
        String class2;
        if (indicatorPosition.equals("labelLeft")) {
            class2 = class1 + "-label-left";
        } else if (indicatorPosition.equals("labelRight")) {
            class2 = class1 + "-label-right";
        } else {
            class2 = class1 + "-" + indicatorPosition;
        }
        writer.startElement("span", null);
        writer.writeAttribute("class", class1 + " " + class2, null);
        writer.write(indicator);
        writer.endElement("span");
    }

    protected String getStateStyleClasses(UIInput component) {
        String styleClases = "";
        if (component.isRequired()) {
            styleClases += " ui-state-required";
        } else {
            styleClases += " ui-state-optional";
        }
        if (!component.isValid()) {
            styleClases += " ui-state-error";
        }
        return styleClases;
    }

    protected Map<String, Object> getLabelAttributes(final UIComponent component) {
        return new HashMap<String, Object>() {{
            boolean required = (Boolean) component.getAttributes().get("required");

            String label = (String) component.getAttributes().get("label");
            String labelPosition = (String) component.getAttributes().get("labelPosition");
            if (!labelPositionSet.contains(labelPosition)) {
                labelPosition = "none";
            }
            boolean hasLabel = !isValueBlank(label) && !labelPosition.equals("none");

            String indicator = (String) (required ? component.getAttributes().get("requiredIndicator") : component.getAttributes().get("optionalIndicator"));
            String indicatorPosition = (String) component.getAttributes().get("indicatorPosition");
            if (!indicatorPositionSet.contains(indicatorPosition)) {
                indicatorPosition = labelPosition.equals("inField") ? "labelRight" : "right";
            }
            boolean hasIndicator = !isValueBlank(indicator) && !indicatorPosition.equals("none");

            String inFieldLabel = null;
            if (hasLabel && labelPosition.equals("inField")) {
                inFieldLabel = label;
                if (hasIndicator) {
                    if (indicatorPosition.equals("labelLeft")) {
                        inFieldLabel = indicator + inFieldLabel;
                    } else if (indicatorPosition.equals("labelRight")) {
                        inFieldLabel = inFieldLabel + indicator;
                    }
                }
            }

            put("clientId", component.getClientId());
            put("required", required);
            put("label", label);
            put("labelPosition", labelPosition);
            put("hasLabel", hasLabel);
            put("indicator", indicator);
            put("indicatorPosition", indicatorPosition);
            put("hasIndicator", hasIndicator);
            put("inFieldLabel", inFieldLabel);
            put("labelIsInField", false);
        }};
    }

    protected void writeLabelAndIndicatorBefore(Map<String, Object> attributes) throws IOException {
        ResponseWriter writer = FacesContext.getCurrentInstance().getResponseWriter();
        boolean required = (Boolean) attributes.get("required");
        boolean hasLabel = (Boolean) attributes.get("hasLabel"), hasIndicator = (Boolean) attributes.get("hasIndicator");
        String label = (String) attributes.get("label"), labelPosition = (String) attributes.get("labelPosition");
        String indicator = (String) attributes.get("indicator"), indicatorPosition = (String) attributes.get("indicatorPosition");
        if (hasLabel && labelPosition.equals("top")) {
            writeLabel(attributes);
            writer.startElement("br", null);
            writer.endElement("br");
        }
        if (hasIndicator && indicatorPosition.equals("top")) {
            if (hasLabel && labelPosition.equals("left")) {
                writeHiddenLabel(writer, label);
            }
            writeIndicator(writer, required, indicator, indicatorPosition);
            writer.startElement("br", null);
            writer.endElement("br");
        }
        if (hasLabel) {
            if (labelPosition.equals("left")/* || labelPosition.equals("top")*/) {
                writeLabel(attributes);
            }
            if (labelPosition.equals("top")) {
//                writer.startElement("br", null);
//                writer.endElement("br");
            }
        }
        if (hasIndicator) {
            if (indicatorPosition.equals("top") || indicatorPosition.equals("bottom")) {
//                writer.startElement("span", null);
            }
            if (indicatorPosition.equals("left")/* || indicatorPosition.equals("top")*/) {
                writeIndicator(writer, required, indicator, indicatorPosition);
            }
            if (indicatorPosition.equals("top")) {
//                writer.startElement("br", null);
//                writer.endElement("br");
            }
        }
    }

    protected void writeLabelAndIndicatorAfter(Map<String, Object> attributes) throws IOException {
        ResponseWriter writer = FacesContext.getCurrentInstance().getResponseWriter();
        boolean required = (Boolean) attributes.get("required");
        boolean hasLabel = (Boolean) attributes.get("hasLabel"), hasIndicator = (Boolean) attributes.get("hasIndicator");
        String label = (String) attributes.get("label"), labelPosition = (String) attributes.get("labelPosition");
        String indicator = (String) attributes.get("indicator"), indicatorPosition = (String) attributes.get("indicatorPosition");
        if (hasIndicator && indicatorPosition.equals("right")) {
            writeIndicator(writer, required, indicator, indicatorPosition);
        }
        if (hasLabel && labelPosition.equals("right")) {
            writeLabel(attributes);
        }
        if (hasIndicator) {
            if (indicatorPosition.equals("bottom")) {
                writer.startElement("br", null);
                writer.endElement("br");
            }
            if (/*indicatorPosition.equals("right") || */indicatorPosition.equals("bottom")) {
                if (hasLabel && labelPosition.equals("left")) {
                    writeHiddenLabel(writer, label);
                }
                writeIndicator(writer, required, indicator, indicatorPosition);
            }
            if (indicatorPosition.equals("top") || indicatorPosition.equals("bottom")) {
//                writer.endElement("span");
            }
        }
        if (hasLabel) {
            if (labelPosition.equals("bottom")) {
                writer.startElement("br", null);
                writer.endElement("br");
            }
            if (/*labelPosition.equals("right") || */labelPosition.equals("bottom")) {
                writeLabel(attributes);
            }
        }
    }

    protected void writeAriaAttributes(Map<String, Object> ariaAttributes, Map<String, Object> labelAttributes) throws IOException {
        ResponseWriter writer = FacesContext.getCurrentInstance().getResponseWriter();

        String autocomplete = (String) ariaAttributes.get("autocomplete");
        Boolean multiline = (Boolean) ariaAttributes.get("multiline");
        Boolean readonly = (Boolean) ariaAttributes.get("readonly");
        Boolean required = (Boolean) labelAttributes.get("required");
        Boolean disabled = (Boolean) ariaAttributes.get("disabled");
        Boolean invalid = (Boolean) ariaAttributes.get("invalid");

        String clientId = (String) labelAttributes.get("clientId");
        String label = (String) labelAttributes.get("label");
        String labelPosition = (String) labelAttributes.get("labelPosition");
        boolean hasLabel = (Boolean) labelAttributes.get("hasLabel");

        if (autocomplete != null && !autocomplete.equals("none")) {
            writer.writeAttribute("aria-autocomplete", autocomplete, null);
        }
        if (multiline != null && multiline) {
            writer.writeAttribute("aria-multiline", true, null);
        }
        if (readonly != null && readonly) {
            writer.writeAttribute("aria-readonly", true, "readonly");
        }
        if (required != null && required) {
            writer.writeAttribute("aria-required", true, "required");
        }
        if (disabled != null && disabled) {
            writer.writeAttribute("aria-disabled", true, "disabled");
        }
        if (invalid != null && invalid) {
            writer.writeAttribute("aria-invalid", true, "valid");
        }
        if (hasLabel) {
            if (labelPosition.equals("inField")) {
                writer.writeAttribute("aria-label", label, "label");
            } else {
                writer.writeAttribute("aria-labelledby", "label_" + clientId, null);
            }
        }
    }
}
