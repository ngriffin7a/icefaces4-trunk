/*
 * Copyright 2004-2014 ICEsoft Technologies Canada Corp.
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
import org.icefaces.ace.util.SelectItemsIterator;
import org.icefaces.ace.util.SelectUtils;

import javax.faces.model.SelectItem;
import javax.faces.component.UISelectItem;
import javax.faces.component.UISelectItems;
import javax.faces.component.UISelectOne;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.FacesEvent;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
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
		SelectItemsIterator selectItemsIterator = new SelectItemsIterator(context, uiComponent);
		while (selectItemsIterator.hasNext()) {
			selectItems.add(cloneSelectItem(selectItemsIterator.next()));
		}
        return selectItems;
    }

    public String getFocusedElementId() {
        return getClientId() + "_input";
    }
	
	@Override
	protected void validateValue(FacesContext facesContext, Object submittedValue) {

		// custom validators
        if (!isEmpty(submittedValue)) {
            if (getValidators() != null) {
                Validator[] validators = getValidators();
                for (Validator validator : validators) {
                    try {
                        validator.validate(facesContext, this, submittedValue);
                    }
                    catch (ValidatorException ve) {
                        // If the validator throws an exception, we're
                        // invalid, and we need to add a message
                        setValid(false);
                        FacesMessage message;
                        String validatorMessageString = getValidatorMessage();

                        if (null != validatorMessageString) {
                            message =
                                  new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                   validatorMessageString,
                                                   validatorMessageString);
                            message.setSeverity(FacesMessage.SEVERITY_ERROR);
                        } else {
                            Collection<FacesMessage> messages = ve.getFacesMessages();
                            if (null != messages) {
                                message = null;
                                String cid = getClientId(facesContext);
                                for (FacesMessage m : messages) {
                                    facesContext.addMessage(cid, m);
                                }
                            } else {
                                message = ve.getFacesMessage();
                            }
                        }
                        if (message != null) {
                            facesContext.addMessage(getClientId(facesContext), message);
                        }
                    }
                }
            }
        }

        boolean found = SelectUtils.matchValue(facesContext,
                                               this,
                                               submittedValue,
                                               new SelectItemsIterator(facesContext, this),
                                               getConverter());

        boolean isNoSelection = SelectUtils.valueIsNoSelectionOption(facesContext,
                                               this,
                                               submittedValue,
                                               new SelectItemsIterator(facesContext, this),
                                               getConverter());

		boolean valid = true;
		if (!isRequired() && isNoSelection) {
			valid = true;
		} else {
			if (!found) valid = false;
			else if (isRequired() && isNoSelection) valid = false;
		}

        if (!valid) { // flag as invalid and add error message
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
		} else {
			setValid(true);
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

	private static SelectItem cloneSelectItem(SelectItem o) {
		SelectItem copy = new SelectItem();
		String description = o.getDescription();
		if (description != null) copy.setDescription(new String(description));
		copy.setDisabled(o.isDisabled());
		copy.setEscape(o.isEscape());
		String label = o.getLabel();
		if (label != null) copy.setLabel(new String(label));
		copy.setNoSelectionOption(o.isNoSelectionOption());
		copy.setValue(o.getValue());
		return copy;
	}
}
