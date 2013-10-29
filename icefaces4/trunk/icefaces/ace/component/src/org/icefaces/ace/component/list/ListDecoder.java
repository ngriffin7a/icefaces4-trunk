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
import org.icefaces.ace.json.JSONArray;
import org.icefaces.ace.json.JSONException;

import javax.faces.component.UIComponent;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitHint;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import java.util.*;

public class ListDecoder {
    private ACEList list;
    private List<ImmigrationRecord> emigrants;

    public ListDecoder(ACEList list) {
        this.list = list;
    }

    public ListDecoder processSelections(String raw) throws JSONException {
        if (raw == null || raw.length() == 0) return this;

        final boolean selectItems = list.isSelectItemModel();
        final JSONArray array = new JSONArray(raw);
        final Collection<Object> selections = selectItems ? (Collection)list.getValue() : list.getSelections();
        final Collection<Object> newSelections = new ArrayList<Object>();

        for (int i = 0; i < array.length(); i++) {
            final int index = array.getInt(i);
            list.setRowIndex(index);

            if (selectItems)
                newSelections.add(((SelectItem)list.getRowData()).getValue());
            else
                newSelections.add(list.getRowData());
        }

        list.queueEvent(new ListSelectEvent(list, new HashSet<Object>(newSelections)));

        selections.addAll(newSelections);

        list.setRowIndex(-1);

        return this;
    }

    public ListDecoder processDeselections(String raw) throws JSONException {
        if (raw == null || raw.length() == 0) return this;

        final JSONArray array = new JSONArray(raw);
        final boolean selectItems = list.isSelectItemModel();
        final Collection<Object> selections = selectItems ? (Collection)list.getValue() : list.getSelections();

        for (int i = 0; i < array.length(); i++) {
            int index = array.getInt(i);
            list.setRowIndex(index);

            if (selectItems)
                selections.remove(((SelectItem)list.getRowData()).getValue());
            else
                selections.remove(list.getRowData());
        }

        list.setRowIndex(-1);

        return this;
    }

    public ListDecoder processReorderings(String raw) throws JSONException {
        if (list.isSelectItemModel() || raw == null || raw.length() == 0) return this;

        JSONArray array = new JSONArray(raw);
        Object value = list.getValue();
        List collection = null;

        if (value instanceof List) collection = (List) value;
        else if (value.getClass().isArray()) collection = Arrays.asList(value);

        for (int i = 0; i < array.length(); i++) {
            JSONArray record = array.getJSONArray(i);
            int from = record.getInt(0);
            int to = record.getInt(1);
            Collections.swap(collection, from, to);
        }

        return this;
    }

    public ListDecoder attachEmigrants(FacesContext context, String destListId) throws JSONException {
        if (list.isSelectItemModel() || destListId == null || destListId.length() == 0)
            return this;

        // Init list to be attached to dest list and carry our records for detaching later
        emigrants = new ArrayList<ImmigrationRecord>();

        context.getViewRoot().visitTree(
                VisitContext.createVisitContext(
                        context,
                        Arrays.asList(new String[] {destListId}),
                        EnumSet.of(VisitHint.SKIP_TRANSIENT, VisitHint.SKIP_UNRENDERED)),
                new EmigrantAttachingVisit(list, emigrants)
        );

        return this;
    }

    private class EmigrantAttachingVisit implements VisitCallback {
        ACEList sourceList;
        List<ImmigrationRecord> migrants;

        private EmigrantAttachingVisit(ACEList sourceList, List<ImmigrationRecord> migrants) {
            this.migrants = migrants;
            this.sourceList = sourceList;
        }

        public VisitResult visit(VisitContext visitContext, UIComponent targetComponent) {
            ACEList destList = (ACEList)targetComponent;

            // List has already had immigrants attached by itself,
            // and doesn't need us to attach immigrants.
            // We'll take this list of immigrants so we know who
            // to detach from ourselves later.
            if (destList.getImmigrants() != null) {
                migrants.addAll(destList.getImmigrants());
                return null;
            }

            // Get immigration list from-to index records to do our
            // removals, and push built record objects if needed
            FacesContext context = visitContext.getFacesContext();
            Map<String, String> params = context.getExternalContext().getRequestParameterMap();
            String raw = params.get(destList.getClientId() + "_immigration");

            try {
                JSONArray records = new JSONArray(raw).getJSONArray(1);
                Set<Object> selected = sourceList.getSelections();
                Object value = sourceList.getValue();
                List collection = null;

                if (value instanceof List) collection = (List) value;
                else if (value.getClass().isArray()) collection = Arrays.asList(value);

                if (records != null)
                for (int i = 0; i < records.length(); i++) {
                    JSONArray record = records.getJSONArray(i);
                    Object val = collection.get(((Integer)record.get(0)).intValue());
                    migrants.add(new ImmigrationRecord(val, (Integer) record.get(1), selected.contains(val)));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            destList.setImmigrants(migrants);

            return VisitResult.COMPLETE;
        }
    }

    public ListDecoder removeEmigrants(FacesContext context, String destListId) {
        if (list.isSelectItemModel() || destListId == null || destListId.length() == 0)
            return this;

        // We'll have either created or fetched the records of our moves in the
        // attach emigrants phase.

        Object value = list.getValue();
        List collection = null;
        Set<Object> selected = list.getSelections();
        if (value instanceof List) collection = (List) value;
        else if (value.getClass().isArray()) collection = Arrays.asList(value);

        for (ImmigrationRecord r : emigrants) {
            collection.remove(r.getValue());
            selected.remove(r.getValue());
        }

        return this;
    }

    public ListDecoder fetchImmigrants(FacesContext context, String raw) throws JSONException {
        if (list.isSelectItemModel() || raw == null || raw.length() == 0)
            return this;

        // List has already had immigrants attached by source,
        // and doesn't need to parse immigrants itself
        if (list.getImmigrants() != null) return this;

        JSONArray array = new JSONArray(raw);
        String sourceListId = array.getString(0);

        List<ImmigrationRecord> immigrants = new ArrayList<ImmigrationRecord>();
        context.getViewRoot().visitTree(
                VisitContext.createVisitContext(
                        context,
                        Arrays.asList(new String[] {sourceListId}),
                        EnumSet.of(VisitHint.SKIP_TRANSIENT, VisitHint.SKIP_UNRENDERED)),
                new ImmigrantFetchingVisit(immigrants, array.getJSONArray(1))
        );

        list.setImmigrants(immigrants);

        return this;
    }

    private class ImmigrantFetchingVisit implements VisitCallback {
        List<ImmigrationRecord> immigrants;
        JSONArray records;

        public ImmigrantFetchingVisit(List<ImmigrationRecord> immigrants, JSONArray records) {
            this.immigrants = immigrants;
            this.records = records;
        }

        public VisitResult visit(VisitContext visitContext, UIComponent uiComponent) {
            ACEList sourceList = (ACEList) uiComponent;
            String sourceListId = sourceList.getClientId();
            FacesContext context = visitContext.getFacesContext();
            Set<Object> sourceSelected = sourceList.getSelections();

            try {
                for (int i = 0; i < records.length(); i++) {
                    JSONArray record = records.getJSONArray(i);
                    int from = record.getInt(0);
                    int to = record.getInt(1);

                    sourceList.setRowIndex(from);
                    Object value = sourceList.getRowData();

                    // If selected, but not deselected this request
                    // decode that information for the dest list.
                    boolean selected = !indexDeselectedThisRequest(context, sourceListId, from);
                    if (selected)
                        selected = sourceSelected.contains(value) || indexSelectedThisRequest(context, sourceListId, from);

                    immigrants.add(new ImmigrationRecord(value, to, selected));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            sourceList.setRowIndex(-1);
            return VisitResult.COMPLETE;
        }

        JSONArray sourceSelections;
        private boolean indexSelectedThisRequest(FacesContext context, String listId, int from) throws JSONException {
            if (sourceSelections == null) {
                String raw = context.getExternalContext().getRequestParameterMap().get(listId+"_selections");
                if (raw == null || raw.length() == 0) return false;
                sourceSelections = new JSONArray(raw);
            }
            for (int i = 0; i < sourceSelections.length(); i++)
                if (sourceSelections.getInt(i) == from) return true;

            return false;
        }


        JSONArray sourceDeselections;
        private boolean indexDeselectedThisRequest(FacesContext context, String listId, int from) throws JSONException {
            if (sourceDeselections == null) {
                String raw = context.getExternalContext().getRequestParameterMap().get(listId+"_deselections");
                if (raw == null || raw.length() == 0) return false;
                sourceDeselections = new JSONArray(raw);
            }
            for (int i = 0; i < sourceDeselections.length(); i++)
                if (sourceDeselections.getInt(i) == from) return true;

            return false;
        }
    }

    public ListDecoder insertImmigrants() {
        if (list.isSelectItemModel()) return this;

        Object value = list.getValue();
        List collection = null;
        Set<Object> selected = list.getSelections();
        List<ImmigrationRecord> records = list.getImmigrants();

        if (value instanceof List) collection = (List) value;
        else if (value.getClass().isArray()) collection = Arrays.asList(value);

        if (records != null)
            for (ImmigrationRecord record : records) {
                collection.add(record.getDestination(),
                        record.getValue());
                if (record.isSelected())
                    selected.add(record.getValue());
            }

        return this;
    }
}
