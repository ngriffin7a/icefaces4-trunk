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

package org.icefaces.ace.component.list;

import org.icefaces.ace.event.ListSelectEvent;

import javax.el.ELContext;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.application.NavigationHandler;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItem;
import javax.faces.component.UISelectItems;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.faces.model.*;
import javax.xml.transform.Result;
import java.sql.ResultSet;
import java.util.*;

public class ACEList extends ListBase {
    private static Class SQL_RESULT = null;
    static {
        try {
            SQL_RESULT = Class.forName("javax.servlet.jsp.jstl.sql.Result");
        } catch (Throwable t)  {
            //ignore if sql.result not available
        }
    }

    DataModel model;
    boolean selectItemModel = false;

    @Override
    protected DataModel getDataModel() {
        // Return any previously cached DataModel instance
        if (this.model != null) {
            return (model);
        }

        // Synthesize a DataModel around our current value if possible
        Object current = getValue();
        List<SelectItem> selectItems = getSelectItems();

        if (selectItems != null) {
            setSelectItemModel(true);
            setDataModel(new ListDataModel(selectItems));
            return model;
        } else if (current == null) {
            setDataModel(new ListDataModel(Collections.EMPTY_LIST));
        } else if (current instanceof DataModel) {
            setDataModel((DataModel) current);
        } else if (current instanceof List) {
            setDataModel(new ListDataModel((List) current));
        } else if (Object[].class.isAssignableFrom(current.getClass())) {
            setDataModel(new ArrayDataModel((Object[]) current));
        } else if (current instanceof ResultSet) {
            setDataModel(new ResultSetDataModel((ResultSet) current));
        } else if ((null != SQL_RESULT) && SQL_RESULT.isInstance(current)) {
            DataModel dataModel = new ResultDataModel();
            dataModel.setWrappedData(current);
            setDataModel(dataModel);
        } else {
            setDataModel(new ScalarDataModel(current));
        }

        setSelectItemModel(false);

        return (model);
    }

    @Override
    public void setRowIndex(int i) {
        if (i < 0) model = null;
        super.setRowIndex(i);
    }

    @Override
    protected void setDataModel(DataModel newModel) {
        model = newModel;
    }

    private List<SelectItem> getSelectItems() {
        List<SelectItem> selectItems = new ArrayList<SelectItem>();
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();

        for (UIComponent c : getChildren())
            if (c instanceof UISelectItems)
                selectItems.addAll(processUISelectItems(elContext, (UISelectItems) c));
            else if (c instanceof UISelectItem) {
                SelectItem item = processUISelectItem(elContext, (UISelectItem) c);
                if (item != null) selectItems.add(item);
            }

        return selectItems.size() == 0 ? null : selectItems;
    }

    private SelectItem processUISelectItem(ELContext elContext, UISelectItem itemComponent) {
        ValueExpression itemExpression = null;
        SelectItem selectItem = null;

        if (itemComponent.getValue() instanceof SelectItem)
            selectItem = (SelectItem) itemComponent.getValue();
        else if ((itemExpression = itemComponent.getValueExpression("value")) != null &&
                itemExpression.getType(elContext).equals(SelectItem.class)) {
            selectItem = (SelectItem)itemExpression.getValue(elContext);
        } else if (itemComponent.getItemLabel() != null) {
            selectItem = new SelectItem(
                    itemComponent.getItemValue(),
                    itemComponent.getItemLabel(),
                    itemComponent.getItemDescription(),
                    itemComponent.isItemDisabled(),
                    itemComponent.isItemEscaped(),
                    itemComponent.isNoSelectionOption()
            );
        }

        return selectItem;
    }

    private List<SelectItem> processUISelectItems(ELContext elContext, UISelectItems c) {
        List<SelectItem> selectItems = new ArrayList<SelectItem>();
        UISelectItems itemsComponent = c;
        Object value = itemsComponent.getValue();

        try  {
            if (value == null)
                value = itemsComponent.getValueExpression("value").getValue(elContext);
        } catch (NullPointerException npe) {
            // If ValueExpression is null return empty list
            return selectItems;
        }

        if (value instanceof SelectItem)
            selectItems.add((SelectItem) value);
        else if (value instanceof Collection) {
            Collection values = (Collection) value;

            for (Iterator<Object> itemIter = values.iterator(); itemIter.hasNext();)
                selectItems.add((SelectItem) itemIter.next());
        } else if (value instanceof SelectItem[]) {
            SelectItem[] values = (SelectItem[]) value;

            for (SelectItem item : values)
                selectItems.add(item);
        } else if (value instanceof Map) {
            Map itemMap = (Map)value;

            for (Iterator<Object> keyIter = itemMap.keySet().iterator(); keyIter.hasNext();) {
                Object key = keyIter.next();

                selectItems.add(new SelectItem(
                        itemMap.get(key),
                        key.toString()
                ));
            }
        }

        return selectItems;
    }

    @Override
    public Boolean isDragging() {
        if (isSelectItemModel()) return false;
        else return super.isDragging();
    }

    @Override
    public Boolean isControlsEnabled() {
        if (isSelectItemModel()) return false;
        else return super.isControlsEnabled();
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }

    @Override
    public Set<Object> getSelections() {
        Set<Object> set = super.getSelections();
        if (set == null) {
            set = new HashSet<Object>();
            setSelections(set);
        }
        return set;
    }

    @Override
    public void broadcast(FacesEvent event) throws AbortProcessingException {
        super.broadcast(event);

        FacesContext context = FacesContext.getCurrentInstance();
        String outcome = null;
        MethodExpression me = null;

        if (event instanceof ListSelectEvent) me = getSelectionListener();

        if (me != null) {
            if (!context.isValidationFailed()) {
                outcome = (String) me.invoke(context.getELContext(), new Object[] {event});
            }

            if (outcome != null) {
                NavigationHandler navHandler = context.getApplication().getNavigationHandler();
                navHandler.handleNavigation(context, null, outcome);
                context.renderResponse();
            }
        }
    }

    // References to immigrant objects are gathered immediately
    List<ImmigrationRecord> immigrants;

    public List<ImmigrationRecord> getImmigrants() {
        return immigrants;
    }

    public void setImmigrants(List<ImmigrationRecord> immigrants) {
        this.immigrants = immigrants;
    }

    public boolean isSelectItemModel() {
        return selectItemModel;
    }

    public void setSelectItemModel(boolean isSelect) {
        selectItemModel = isSelect;
    }
}
