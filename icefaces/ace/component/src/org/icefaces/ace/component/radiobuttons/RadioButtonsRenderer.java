/*
 * Original Code Copyright Prime Technology.
 * Subsequent Code Modifications Copyright 2011-2014 ICEsoft Technologies Canada Corp. (c)
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
 * Code Modification 2: (ICE-6978) Used JSONBuilder to add the functionality of escaping JS output.
 * Contributors: ICEsoft Technologies Canada Corp. (c)
 */
package org.icefaces.ace.component.radiobuttons;


import org.icefaces.render.MandatoryResourceComponent;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.io.IOException;
import java.lang.String;
import java.util.*;
import org.icefaces.ace.util.*;
import javax.faces.component.UIParameter;
import javax.faces.convert.ConverterException;
import org.icefaces.util.EnvUtils;
import org.icefaces.ace.renderkit.InputRenderer;
import javax.faces.model.SelectItem;
import javax.faces.convert.Converter;
import javax.el.ExpressionFactory;
import javax.el.ELException;
import java.lang.reflect.Array;
import javax.el.ValueExpression;
import javax.faces.component.UISelectMany;
import javax.faces.FacesException;
import javax.faces.application.Application;

@MandatoryResourceComponent(tagName = "radioButtons", value = "org.icefaces.ace.component.radiobuttons.RadioButtons")
public class RadioButtonsRenderer extends InputRenderer {
    private enum EventType {
        HOVER, FOCUS
    }

    @Override
    public void decode(FacesContext context, UIComponent component) {

//		componentIsDisabledOrReadonly ?

        RadioButtons radioButtons = (RadioButtons) component;
		String clientId = radioButtons.getClientId(context);

		Map<String, String[]> requestParameterValuesMap =
			  context.getExternalContext().getRequestParameterValuesMap();
		if (requestParameterValuesMap.containsKey(clientId)) {
			String newValues[] = requestParameterValuesMap.get(clientId);
			radioButtons.setSubmittedValue(newValues);
		} else {
			// Use the empty array, not null, to distinguish
			// between a deselected UISelectMany and a disabled one
			radioButtons.setSubmittedValue(new String[0]);
		}
		decodeBehaviors(context, radioButtons);
    }

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        super.encodeBegin(context, component);
        ResponseWriter writer = context.getResponseWriter();
        RadioButtons radioButtons = (RadioButtons) component;
        String style = (style = radioButtons.getStyle()) == null ? "" : style.trim();
        String styleClass = (styleClass = radioButtons.getStyleClass()) == null ? "" : styleClass.trim();
        styleClass += (styleClass.length() > 0 ? " " : "") + "ice-ace-radiobuttons ui-widget ui-widget-content ui-corner-all " +getStateStyleClasses(radioButtons);

        writer.startElement("div", component);
        writer.writeAttribute("id", component.getClientId(context), "id");
        if (style.length() > 0) {
            writer.writeAttribute("style", style, "style");
        }
        writer.writeAttribute("class", styleClass, "styleClass");

        UIComponent headerFacet = component.getFacet("header");
        String headerText = (headerText = radioButtons.getHeader()) == null ? "" : headerText.trim();

        if (headerFacet != null || headerText.length() > 0) {
            writer.startElement("div", null);
            writer.writeAttribute("class", "ui-widget-header ui-corner-top", null);
            if (headerFacet != null) {
                renderChild(context, headerFacet);
            } else if (headerText.length() > 0) {
                writer.write(headerText);
            }
            writer.endElement("div");
        }

		boolean required = radioButtons.isRequired();
		String indicatorPosition = radioButtons.getIndicatorPosition();
		String indicator = required ? radioButtons.getRequiredIndicator() : radioButtons.getOptionalIndicator();
		if ("left".equalsIgnoreCase(indicatorPosition)
			|| "top".equalsIgnoreCase(indicatorPosition)) {
				if (indicator != null) {
					writer.startElement("span", null);
					writer.writeAttribute("class", "ice-indicator", null);
					writer.write(indicator);
					writer.endElement("span");
				}
		}
		if ("top".equalsIgnoreCase(indicatorPosition)) {
			writer.startElement("br", null);
			writer.endElement("br");
		}

		// render buttons
		Object currentSelections = getCurrentSelectedValues(radioButtons);
        Converter converter = radioButtons.getConverter();
		SelectItemsIterator selectItemsIterator = new SelectItemsIterator(context, radioButtons);
		int i = 0;
		if (!radioButtons.isValid()) {
			while (selectItemsIterator.hasNext()) {
				encodeButton(context, radioButtons, i++, selectItemsIterator.next(), converter, radioButtons.getSubmittedValue());
			}
		} else {
			while (selectItemsIterator.hasNext()) {
				encodeButton(context, radioButtons, i++, selectItemsIterator.next(), converter, currentSelections);
			}
		}

		if ("bottom".equalsIgnoreCase(indicatorPosition)) {
			writer.startElement("br", null);
			writer.endElement("br");
		}
		if ("right".equalsIgnoreCase(indicatorPosition)
			|| "bottom".equalsIgnoreCase(indicatorPosition)) {
				if (indicator != null) {
					writer.startElement("span", null);
					writer.writeAttribute("class", "ice-indicator", null);
					writer.write(indicator);
					writer.endElement("span");
				}
		}

		writer.endElement("div");
    }

	private void encodeButton(FacesContext facesContext, RadioButtons radioButtons, int index, SelectItem item, Converter converter, Object currentSelections) throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();

        String clientId = radioButtons.getClientId(facesContext) + ":" + index;
		String labelPosition = radioButtons.getLabelPosition();

		String label = item.getLabel();
		Object value = getConvertedValueForClient(facesContext, radioButtons, item.getValue());
		boolean selected = isSelected(facesContext, radioButtons, value, currentSelections, converter);

        String firstWrapperClass = "ice-ace-radiobutton-main";
        boolean ariaEnabled = EnvUtils.isAriaEnabled(facesContext);

        // Root Container
        writer.startElement(HTML.DIV_ELEM, radioButtons);
        writer.writeAttribute(HTML.ID_ATTR, clientId, null);
//        ComponentUtils.enableOnElementUpdateNotify(writer, clientId);

        writer.writeAttribute(HTML.CLASS_ATTR, "ice-ace-radiobutton", null);
        encodeScript(facesContext, writer, radioButtons, clientId, EventType.HOVER, item.isDisabled());

		if (label != null) {
			if ("left".equalsIgnoreCase(labelPosition)
					|| "top".equalsIgnoreCase(labelPosition)) {
				writer.startElement("label", null);
				writer.writeAttribute("id", "label_" + clientId, null);
				writer.writeAttribute("for", clientId, null);
				writer.write(label);
				writer.endElement("label");
			}
			if ("top".equalsIgnoreCase(labelPosition)) {
				writer.startElement("br", null);
				writer.endElement("br");
			}
		}

        // First Wrapper
        writer.startElement(HTML.SPAN_ELEM, null);
        writer.writeAttribute(HTML.CLASS_ATTR, firstWrapperClass, null);

        // Second Wrapper
        writer.startElement(HTML.SPAN_ELEM, null);

        if (ariaEnabled) {
			writer.writeAttribute(HTML.ROLE_ATTR, "radio", null);
			writer.writeAttribute(HTML.ARIA_DISABLED_ATTR, item.isDisabled(), null);
		}

        // Button Element
        writer.startElement(HTML.BUTTON_ELEM, null);
        writer.writeAttribute(HTML.TYPE_ATTR, "button", null);
        String buttonId = clientId + "_button";
        writer.writeAttribute(HTML.ID_ATTR, buttonId, null);
        writer.writeAttribute(HTML.NAME_ATTR, buttonId, null);
		String selectedClass = "";
		selectedClass = (selected ? "ice-ace-radiobutton-selected" : "");
		writer.writeAttribute(HTML.CLASS_ATTR, "ui-corner-all " + selectedClass, null);

		if (ariaEnabled) writer.writeAttribute(HTML.TABINDEX_ATTR, "0", null);
        encodeButtonStyle(writer, item.isDisabled());
        encodeScript(facesContext, writer, radioButtons, clientId, EventType.FOCUS, item.isDisabled());

        if (label != null && "inField".equalsIgnoreCase(radioButtons.getLabelPosition())) {
            writer.startElement(HTML.SPAN_ELEM, null);
            writer.writeAttribute(HTML.CLASS_ATTR, "ui-label", null);
            writer.write(label);
            writer.endElement(HTML.SPAN_ELEM);
        } else {
            writer.startElement(HTML.SPAN_ELEM, null);
            encodeIconStyle(writer, selected);
            writer.endElement(HTML.SPAN_ELEM);
        }

        writer.endElement(HTML.BUTTON_ELEM);
        writer.endElement(HTML.SPAN_ELEM);
        writer.endElement(HTML.SPAN_ELEM);

		if (label != null) {
			if ("bottom".equalsIgnoreCase(labelPosition)) {
				writer.startElement("br", null);
				writer.endElement("br");
			}
			if ("right".equalsIgnoreCase(labelPosition)
					|| "bottom".equalsIgnoreCase(labelPosition)) {
				writer.startElement("label", null);
				writer.writeAttribute("id", "label_" + clientId, null);
				writer.writeAttribute("for", clientId, null);
				writer.write(label);
				writer.endElement("label");
			}
		}

        writer.startElement("input", radioButtons);
        writer.writeAttribute("type", "hidden", null);
        writer.writeAttribute("autocomplete", "off", null);
        if (selected) writer.writeAttribute("name", radioButtons.getClientId(facesContext), null);
        writer.writeAttribute("value", value, null);
        writer.endElement("input");

		// register radio button with group
        writer.startElement("script", null);
        writer.writeAttribute("type", "text/javascript", null);
        writer.writeText("ice.ace.radiobutton.register('"+clientId+"','"+radioButtons.getClientId(facesContext)+"');", null);
		writer.endElement("script");

        writer.endElement(HTML.DIV_ELEM);
		
		Utils.registerLazyComponent(facesContext, clientId, getScript(facesContext, writer, radioButtons, clientId, item.isDisabled()));
	}

    private void encodeScript(FacesContext facesContext, ResponseWriter writer,
                              RadioButtons radioButtons, String clientId, EventType type, boolean disabled) throws IOException {

        String eventType = "";
        if (EventType.HOVER.equals(type))
            eventType = HTML.ONMOUSEOVER_ATTR;
        else if (EventType.FOCUS.equals(type))
            eventType = HTML.ONFOCUS_ATTR;

        writer.writeAttribute(eventType, "if (!document.getElementById('" + clientId + "').widget) "+ getScript(facesContext, writer, radioButtons, clientId, disabled), null);
    }

    private String getScript(FacesContext facesContext, ResponseWriter writer,
                              RadioButtons radioButtons, String clientId, boolean disabled) throws IOException {
        String groupId = radioButtons.getClientId(facesContext);

        boolean ariaEnabled = EnvUtils.isAriaEnabled(facesContext);
        JSONBuilder jb = JSONBuilder.create();
        List<UIParameter> uiParamChildren = Utils.captureParameters(radioButtons);

        jb.beginFunction("ice.ace.lazy")
          .item("radiobutton")
          .beginArray()
          .item(clientId)
          .beginMap()
          .entry("groupId", groupId)
          .entry("ariaEnabled", ariaEnabled)
          .entry("radioButtons", radioButtons.getClientId(facesContext))
          .entry("mutuallyExclusive", radioButtons.isMutuallyExclusive());

		if (disabled) jb.entry("disabled", true);

        if (uiParamChildren != null) {
            jb.beginMap("uiParams");
            for (UIParameter p : uiParamChildren)
                jb.entry(p.getName(), (String)p.getValue());
            jb.endMap();
        }

        encodeClientBehaviors(facesContext, radioButtons, jb);

        jb.endMap().endArray().endFunction();
		
		return jb.toString();
	}

    private void encodeButtonStyle(ResponseWriter writer, boolean disabled) throws IOException {
        String buttonClasses = "";
        String disabledClass = "ui-state-disabled";

        if (disabled) {
            buttonClasses += disabledClass + " ";
        }

        if (!buttonClasses.equals("")) {
            writer.writeAttribute(HTML.CLASS_ATTR, buttonClasses.trim(), null);
        }
    }

    private void encodeIconStyle(ResponseWriter writer, boolean value) throws IOException {
        String iconClass = "fa";
        String selectedStyle = "fa-dot-circle-o";
        String unselectedStyle = "fa-circle-o";
		String largeStyle = "fa-lg";

        if (value) {
            iconClass += " " + selectedStyle + " " + largeStyle;
        } else {
            iconClass += " " + unselectedStyle + " " + largeStyle;
        };

        writer.writeAttribute(HTML.CLASS_ATTR, iconClass, null);
    }

    protected boolean isSelected(FacesContext context,
                                 UIComponent component,
                                 Object itemValue,
                                 Object valueArray,
                                 Converter converter) {

        if (itemValue == null && valueArray == null) {
            return true;
        }
        if (null != valueArray) {
            if (!valueArray.getClass().isArray()) {
                return valueArray.equals(itemValue);
            }
            int len = Array.getLength(valueArray);
            for (int i = 0; i < len; i++) {
                Object value = Array.get(valueArray, i);
                if (value == null && itemValue == null) {
                    return true;
                } else {
                    if ((value == null) ^ (itemValue == null)) {
                        continue;
                    }
                    Object compareValue;
                    if (converter == null) {
                        compareValue = coerceToModelType(context,
                                                        itemValue,
                                                        value.getClass());
                    } else {
                        compareValue = itemValue;
                        if (compareValue instanceof String && !(value instanceof String)) {
                            // type mismatch between the time and the value we're
                            // comparing.  Invoke the Converter.
                            compareValue = converter.getAsObject(context,
                                                                component,
                                                                (String) compareValue);
                        }
                    }

                    if (value.equals(compareValue)) {
                        return (true);
                    }
                }
            }
        }
        return false;

    }

    protected Object coerceToModelType(FacesContext ctx,
                                       Object value,
                                       Class itemValueType) {

        Object newValue;
        try {
            ExpressionFactory ef = ctx.getApplication().getExpressionFactory();
            newValue = ef.coerceToType(value, itemValueType);
        } catch (ELException ele) {
            newValue = value;
        } catch (IllegalArgumentException iae) {
            // If coerceToType fails, per the docs it should throw
            // an ELException, however, GF 9.0 and 9.0u1 will throw
            // an IllegalArgumentException instead (see GF issue 1527).
            newValue = value;
        }

        return newValue;

    }

    protected Object getCurrentSelectedValues(RadioButtons radioButtons) {

            Object value = radioButtons.getValue();
            if (value == null) {
                return null;
            } else if (value instanceof Collection) {
                return ((Collection) value).toArray();
            } else if (value.getClass().isArray()) {
                if (Array.getLength(value) == 0) {
                    return null;
                }
            } else if (!value.getClass().isArray()) {
/*                logger.warning(
                    "The UISelectMany value should be an array or a collection type, the actual type is " +
                    value.getClass().getName());*/
            }

            return value;
    }

    @Override
    public Object getConvertedValue(FacesContext context, UIComponent component, Object submittedValue) throws ConverterException {
		UISelectMany uiSelectMany = (UISelectMany) component;
		String[] newValues = (String[]) submittedValue;

		// if we have no local value, try to get the valueExpression.
		ValueExpression valueExpression =
			  uiSelectMany.getValueExpression("value");

		Object result = newValues; // default case, set local value
		boolean throwException = false;

		// If we have a ValueExpression
		if (null != valueExpression) {
			Class modelType = valueExpression.getType(context.getELContext());
			// Does the valueExpression resolve properly to something with
			// a type?
			if (modelType != null) {
				result = convertSelectManyValuesForModel(context,
														 uiSelectMany,
														 modelType,
														 newValues);
			}
			// If it could not be converted, as a fall back try the type of
			// the valueExpression's current value covering some edge cases such
			// as where the current value came from a Map.
			if(result == null) {
				Object value = valueExpression.getValue(context.getELContext());
				if(value != null) {
					result = convertSelectManyValuesForModel(context,
															 uiSelectMany,
															 value.getClass(),
															 newValues);
				}
			}
			if(result == null) {
				throwException = true;
			}
		} else {
			// No ValueExpression, just use Object array.
			result = convertSelectManyValues(context, uiSelectMany,
											 Object[].class,
											 newValues);
		}
		if (throwException) {
			StringBuffer values = new StringBuffer();
			if (null != newValues) {
				for (int i = 0; i < newValues.length; i++) {
					if (i == 0) {
						values.append(newValues[i]);
					} else {
						values.append(' ').append(newValues[i]);
					}
				}
			}
			Object[] params = {
				  values.toString(),
				  valueExpression.getExpressionString()
			};
			throw new ConverterException("Couldn't convert " + values.toString()
				+ " (" + valueExpression.getExpressionString() + ")");
		}

		return result;
    }

	protected Object convertSelectManyValuesForModel(FacesContext context,
													 UISelectMany uiSelectMany,
													 Class modelType,
													 String[] newValues) {

		if (modelType.isArray()) {
			return convertSelectManyValues(context,
										   uiSelectMany,
										   modelType,
										   newValues);
		} else if (Collection.class.isAssignableFrom(modelType)) {
			Object[] values = (Object[]) convertSelectManyValues(context,
																 uiSelectMany,
																 Object[].class,
																 newValues);

			Collection targetCollection = bestGuess(modelType, values.length);

			//noinspection ManualArrayToCollectionCopy
			for (Object v : values) {
				//noinspection unchecked
				targetCollection.add(v);
			}

			return targetCollection;
		} else if (Object.class.equals(modelType)) {
			return convertSelectManyValues(context,
										   uiSelectMany,
										   Object[].class,
										   newValues);
		} else {
			throw new FacesException("Target model Type is not a Collection or Array");
		}
	}

	protected Object convertSelectManyValues(FacesContext context,
											 UISelectMany uiSelectMany,
											 Class arrayClass,
											 String[] newValues)
		  throws ConverterException {

		Object result;
		Converter converter;
		int len = (null != newValues ? newValues.length : 0);

		Class elementType = arrayClass.getComponentType();

		// Optimization: If the elementType is String, we don't need
		// conversion.  Just return newValues.
		if (elementType.equals(String.class)) {
			return newValues;
		}

		try {
			result = Array.newInstance(elementType, len);
		} catch (Exception e) {
			throw new ConverterException(e);
		}

		// bail out now if we have no new values, returning our
		// oh-so-useful zero-length array.
		if (null == newValues) {
			return result;
		}

		// obtain a converter.

		// attached converter takes priority
		if (null == (converter = uiSelectMany.getConverter())) {
			// Otherwise, look for a by-type converter
			if (null == (converter = getConverterForClass(elementType,
															   context))) {
				// if that fails, and the attached values are of Object type,
				// we don't need conversion.
				if (elementType.equals(Object.class)) {
					return newValues;
				}
				StringBuffer valueStr = new StringBuffer();
				for (int i = 0; i < len; i++) {
					if (i == 0) {
						valueStr.append(newValues[i]);
					} else {
						valueStr.append(' ').append(newValues[i]);
					}
				}
				Object[] params = {
					  valueStr.toString(),
					  "null Converter"
				};

				throw new ConverterException("Couldn't convert " + valueStr.toString()
					+ " (null Converter)");
			}
		}

		assert(null != result);
		if (elementType.isPrimitive()) {
			for (int i = 0; i < len; i++) {
				if (elementType.equals(Boolean.TYPE)) {
					Array.setBoolean(result, i,
									 ((Boolean) converter.getAsObject(context,
																	  uiSelectMany,
																	  newValues[i])));
				} else if (elementType.equals(Byte.TYPE)) {
					Array.setByte(result, i,
								  ((Byte) converter.getAsObject(context,
																uiSelectMany,
																newValues[i])));
				} else if (elementType.equals(Double.TYPE)) {
					Array.setDouble(result, i,
									((Double) converter.getAsObject(context,
																	uiSelectMany,
																	newValues[i])));
				} else if (elementType.equals(Float.TYPE)) {
					Array.setFloat(result, i,
								   ((Float) converter.getAsObject(context,
																  uiSelectMany,
																  newValues[i])));
				} else if (elementType.equals(Integer.TYPE)) {
					Array.setInt(result, i,
								 ((Integer) converter.getAsObject(context,
																  uiSelectMany,
																  newValues[i])));
				} else if (elementType.equals(Character.TYPE)) {
					Array.setChar(result, i,
								  ((Character) converter.getAsObject(context,
																	 uiSelectMany,
																	 newValues[i])));
				} else if (elementType.equals(Short.TYPE)) {
					Array.setShort(result, i,
								   ((Short) converter.getAsObject(context,
																  uiSelectMany,
																  newValues[i])));
				} else if (elementType.equals(Long.TYPE)) {
					Array.setLong(result, i,
								  ((Long) converter.getAsObject(context,
																uiSelectMany,
																newValues[i])));
				}
			}
		} else {
			for (int i = 0; i < len; i++) {
				Array.set(result, i, converter.getAsObject(context,
														   uiSelectMany,
														   newValues[i]));
			}
		}
		return result;
	}

    protected Converter getConverterForClass(Class converterClass, FacesContext context) {
        if (converterClass == null) {
            return null;
        }
        try {            
            Application application = context.getApplication();
            return (application.createConverter(converterClass));
        } catch (Exception e) {
            return (null);
        }
    }

    protected Collection bestGuess(Class<? extends Collection> type, int initialSize) {

        if (SortedSet.class.isAssignableFrom(type)) {
            return new TreeSet();
        } else if (Queue.class.isAssignableFrom(type)) {
           return new LinkedList(); 
        } else if (Set.class.isAssignableFrom(type)) {
            return new HashSet(initialSize);
        } else {
            // this covers the where type is List or Collection
            return new ArrayList(initialSize);
        }

    }

	public String getConvertedValueForClient(FacesContext context, UIComponent component, Object value) throws ConverterException {
		RadioButtons radioButtons = (RadioButtons) component;
		Converter converter = radioButtons.getConverter();
		
		if(converter != null) {
			return converter.getAsString(context, radioButtons, value);
		} else {
			ValueExpression ve = radioButtons.getValueExpression("value");

			if(ve != null) {
				Class<?> valueType = ve.getType(context.getELContext());
				Converter converterForType = context.getApplication().createConverter(valueType);

				if(converterForType != null) {
					if (converterForType instanceof javax.faces.convert.EnumConverter && "".equals(value)) return converterForType.getAsString(context, radioButtons, null);
					return converterForType.getAsString(context, radioButtons, value);
				}
			}
		}
		
		return (value != null ? value.toString() : "");
	}
}
