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

package org.icefaces.ace.component.simpleselectonemenu;

import org.icefaces.component.Focusable;

import javax.faces.model.SelectItem;
import javax.faces.component.UISelectItem;
import javax.faces.component.UISelectItems;
import javax.faces.component.UISelectOne;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.FacesEvent;
import java.util.*;

import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import java.util.Locale;
import java.util.ResourceBundle;

public class SimpleSelectOneMenu extends SimpleSelectOneMenuBase implements Focusable {
	
    public Iterator getItemListIterator() {
		List list = getItemList();
        if (list == null) {
            return Collections.EMPTY_LIST.iterator();
        }
        return list.iterator();
    }
	
    void populateItemList() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
		setItemList(getSelectItems(facesContext,this));
    }
	
    public static List getSelectItems(FacesContext context, UIComponent uiComponent) {
        List selectItems = new ArrayList();
        if (uiComponent.getChildCount() == 0) return selectItems;
        Iterator children = uiComponent.getChildren().iterator();
        while (children.hasNext()) {
            UIComponent nextSelectItemChild = (UIComponent) children.next();
            if (nextSelectItemChild instanceof UISelectItem) {
                Object selectItemValue =
                        ((UISelectItem) nextSelectItemChild).getValue();
                if (selectItemValue != null &&
                    selectItemValue instanceof SelectItem) {
                    selectItems.add(selectItemValue);
                } else {
                    selectItems.add(
                            new SelectItem(
                                    ((UISelectItem) nextSelectItemChild).getItemValue(),
                                    ((UISelectItem) nextSelectItemChild).getItemLabel(),
                                    ((UISelectItem) nextSelectItemChild).getItemDescription(),
                                    ((UISelectItem) nextSelectItemChild).isItemDisabled()));
                }
            } else if (nextSelectItemChild instanceof UISelectItems) {
                Object selectItemsValue =
                        ((UISelectItems) nextSelectItemChild).getValue();

                if (selectItemsValue != null) {
                    if (selectItemsValue instanceof SelectItem) {
                        selectItems.add(selectItemsValue);
                    } else if (selectItemsValue instanceof Collection) {
                        Iterator selectItemsIterator =
                                ((Collection) selectItemsValue).iterator();
                        while (selectItemsIterator.hasNext()) {
                            selectItems.add(selectItemsIterator.next());
                        }
                    } else if (selectItemsValue instanceof SelectItem[]) {
                        SelectItem selectItemArray[] =
                                (SelectItem[]) selectItemsValue;
                        for (int i = 0; i < selectItemArray.length; i++) {
                            selectItems.add(selectItemArray[i]);
                        }
                    } else if (selectItemsValue instanceof Map) {
                        Iterator selectItemIterator =
                                ((Map) selectItemsValue).keySet().iterator();
                        while (selectItemIterator.hasNext()) {
                            Object nextKey = selectItemIterator.next();
                            if (nextKey != null) {
                                Object nextValue =
                                        ((Map) selectItemsValue).get(nextKey);
                                if (nextValue != null) {
                                    selectItems.add(
                                            new SelectItem(
                                                    nextValue.toString(),
                                                    nextKey.toString()));
                                }
                            }
                        }
                    }
                }
            }
        }
        return selectItems;
    }

    public String getFocusedElementId() {
        return getClientId() + "_input";
    }
	
	@Override
	protected void validateValue(FacesContext facesContext, Object submittedValue) {
	
		boolean found = false;
		populateItemList();
		Iterator matches = getItemListIterator();
		SelectItem item = null;
		String convertedSubmittedValue = null;
		if (submittedValue != null) {
			try {
				convertedSubmittedValue = ((SimpleSelectOneMenuRenderer) getRenderer(facesContext)).getConvertedValueForClient(facesContext, this, submittedValue);
			} catch (Exception e) {
				convertedSubmittedValue = submittedValue.toString();
			}
		}
		while (matches.hasNext()) {
			item = (SelectItem) matches.next();
			Object value = item.getValue();
			String convertedValue = null;
			
			if (value != null) {
				try {
					convertedValue = ((SimpleSelectOneMenuRenderer) getRenderer(facesContext)).getConvertedValueForClient(facesContext, this, value);
				} catch (Exception e) {
					convertedValue = value.toString();
				}
			}
			
			if (!item.isDisabled() && convertedValue != null && convertedSubmittedValue != null 
				&& convertedValue.equals(convertedSubmittedValue.toString())) {
				found = true;
				break;
			}
		}
		
		if (found) {
			setValid(true);
		} else { // flag as invalid and add error message
			Locale locale = null;
			// facesContext.getViewRoot() may not have been initialized at this point.
			if (facesContext != null && facesContext.getViewRoot() != null) {
				locale = facesContext.getViewRoot().getLocale();
				if (locale == null) {
					locale = Locale.getDefault();
				}
			} else {
				locale = Locale.getDefault();
			}
			
			String summary = null;
			String detail = null;       
			ResourceBundle bundle;
			String bundleName;
			String messageId = UISelectOne.INVALID_MESSAGE_ID;
			Application app = facesContext.getApplication();
			Class appClass = app.getClass();
			
			// see if we have a user-provided bundle
			if (null != (bundleName = app.getMessageBundle())) {
				if (null != 
					(bundle = 
						ResourceBundle.getBundle(bundleName, locale,
						 getCurrentLoader(appClass)))) {
					// see if we have a hit
					try {
						summary = bundle.getString(messageId);
						detail = bundle.getString(messageId + "_detail");
					}
					catch (Exception e) {
						// ignore
					}
				}
			}

			// we couldn't find a summary in the user-provided bundle
			if (null == summary) {
				// see if we have a summary in the app provided bundle
				if (null != 
					(bundle = 
						ResourceBundle.getBundle(FacesMessage.FACES_MESSAGES, 
							locale, getCurrentLoader(appClass)))) {
					try {
						summary = bundle.getString(messageId);
						detail = bundle.getString(messageId + "_detail");
					} catch (Exception e) {
						// ignore
					}
				}
			}
			
			// use default message
			if (null == summary) {
				summary = "{0}: INVALID: Selected value didn't match one of the options.";
				detail = summary;
			}
			
			String _label = getLabel();
			if (_label != null && !"".equals(_label)) {
				summary = summary.replace("{0}", _label);
				if (detail != null) detail = detail.replace("{0}", _label);
			}

			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, detail);
            facesContext.addMessage(getClientId(facesContext), message);
            setValid(false);
		}
	}
	
    private static ClassLoader getCurrentLoader(Class fallbackClass) {
        ClassLoader loader =
            Thread.currentThread().getContextClassLoader();
        if (loader == null) {
            loader = fallbackClass.getClassLoader();
        }
        return loader;
    }
}
