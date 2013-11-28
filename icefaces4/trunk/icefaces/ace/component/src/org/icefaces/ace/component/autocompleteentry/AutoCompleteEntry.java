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

package org.icefaces.ace.component.autocompleteentry;

import org.icefaces.ace.event.TextChangeEvent;
import org.icefaces.component.Focusable;

import javax.faces.component.NamingContainer;
import javax.faces.model.SelectItem;
import javax.faces.component.UINamingContainer;
import javax.faces.component.UISelectItem;
import javax.faces.component.UISelectItems;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.el.MethodExpression;
import java.util.*;

public class AutoCompleteEntry extends AutoCompleteEntryBase implements NamingContainer, Focusable {

    private transient int index = -1;
	
    public Iterator getItemListIterator() {
		List list = getItemList();
        if (list == null) {
            return Collections.EMPTY_LIST.iterator();
        }
        return list.iterator();
    }
	
    public void setIndex(int index) {
        this.index = index;
    }
	
    public String getClientId(FacesContext context) {
        if (context == null) {
            throw new NullPointerException();
        }
        String baseClientId = super.getClientId(context);
        if (index >= 0) {
            return (baseClientId + UINamingContainer.getSeparatorChar(context) + index++);
        } else {
            return (baseClientId);
        }
    }
	
    public void resetId(UIComponent component) {
        String id = component.getId();
        component.setId(id); // Forces client id to be reset
        if (component.getChildCount() == 0)return;
        Iterator kids = component.getChildren().iterator();
        while (kids.hasNext()) {
            UIComponent kid = (UIComponent) kids.next();
            resetId(kid);
        }

    }
	
    void populateItemList() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (getSelectFacet() != null) {
            //facet being used on jsf page, so get the list from the value binding
            setItemList(getListValue());
        } else {
            //selectItem or selectItems has been used on jsf page, so get the selectItems
            setItemList(getSelectItems(facesContext,this));
        }
    }
	
    public UIComponent getSelectFacet() {
        return (UIComponent) getFacet("row");
    }
	
    public String getOnkeypress() {
        if (isDisabled() || isReadonly()) return "";
        return super.getOnkeypress();
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
	
    public void broadcast(FacesEvent event) throws AbortProcessingException {
        super.broadcast(event);

        if (event != null && event instanceof TextChangeEvent) {
            
            MethodExpression method = getTextChangeListener();
            if (method != null) {
                method.invoke(getFacesContext().getELContext(), new Object[]{event});
            }
        }
    }

    public String getFocusedElementId() {
        return getClientId(FacesContext.getCurrentInstance()) + "_input";
    }
}
