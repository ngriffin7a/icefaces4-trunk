package org.icefaces.impl.component;

import javax.faces.component.*;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class Repeat extends UIData {

    public Repeat() {
        setRowStatePreserved(true);
    }

    /**
     * <p>Return the request-scope attribute under which the iteration status for the
     * current row will be exposed.  This property is
     * <strong>not</strong> enabled for value binding expressions.</p>
     */
    public String getVarStatus() {
        return (String) getStateHelper().get("varStatus");
    }


    /**
     * <p>Set the request-scope attribute under which the iteration status for the
     * current row wil be exposed.</p>
     *
     * @param var The new request-scope attribute name
     */
    public void setVarStatus(String var) {
        getStateHelper().put("varStatus", var);
    }

    public void processDecodes(final FacesContext context) {
        if (isRendered()) {
            pushComponentToEL(context, this);
            iterateOverChildren(context, new Runnable() {
                public void run() {
                    for (UIComponent child : getChildren()) {
                        if (child.isRendered()) {
                            child.processDecodes(context);
                        }
                    }
                }
            });
            decode(context);
            popComponentFromEL(context);
        }
    }

    public void processValidators(final FacesContext context) {
        if (isRendered()) {
            pushComponentToEL(context, this);
            iterateOverChildren(context, new Runnable() {
                public void run() {
                    for (UIComponent child : getChildren()) {
                        if (child.isRendered()) {
                            child.processValidators(context);
                        }
                    }
                }
            });
            popComponentFromEL(context);
        }
    }

    public void processUpdates(final FacesContext context) {
        if (isRendered()) {
            pushComponentToEL(context, this);
            iterateOverChildren(context, new Runnable() {
                public void run() {
                    for (UIComponent child : getChildren()) {
                        if (child.isRendered()) {
                            child.processUpdates(context);
                        }
                    }
                }
            });
            popComponentFromEL(context);
        }
    }

    public boolean visitTree(final VisitContext context, final VisitCallback callback) {
        if (!isVisitable(context)) {
            return false;
        }

        FacesContext facesContext = context.getFacesContext();
        pushComponentToEL(facesContext, this);

        try {
            iterateOverChildren(facesContext, new Runnable() {
                public void run() {
                    List<UIComponent> children = Repeat.this.getChildren();
                    for (UIComponent child : children) {
                        child.visitTree(context, callback);
                    }
                }
            });
        } finally {
            popComponentFromEL(facesContext);
        }

        return false;
    }

    public void encodeChildren(final FacesContext context) throws IOException {
        iterateOverChildren(context, new Runnable() {
            public void run() {
                try {
                    Repeat.super.encodeChildren(context);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void iterateOverChildren(FacesContext context, Runnable runnable) {
        final String var = getVar();
        final String varStatus = getVarStatus();
        final Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
        final DataModel model = getDataModel();
        int first = getFirst();
        int rows = getRows();
        int actualNoRows = getDataModel().getRowCount();
        rows = rows == 0 || rows + first > actualNoRows ? actualNoRows - first : rows;
        first = first > actualNoRows ? 0 : first;
        int last = first + rows - 1;
        int index = first < actualNoRows ? first : 0;

        while (index <= last) {
            setRowIndex(index);
            restoreDescendantState();
            Object item = model.getRowData();
            requestMap.put(var, item);
            if (varStatus != null) {
                requestMap.put(varStatus, new VarStatus(first, last, index));
            }

            runnable.run();

            saveDescendantState();
            resetClientIDs(this);
            index++;
        }
        requestMap.remove(var);
        requestMap.remove(varStatus);
    }

    private void resetClientIDs(UIComponent component) {
        for (UIComponent child : component.getChildren()) {
            child.setId(child.getId());
            resetClientIDs(child);
        }
    }

    public boolean getRendersChildren() {
        return true;
    }

    public String getRendererType() {
        return null;
    }

    enum PropertyKeys {
        saved
    }

    private void restoreDescendantState() {
        if (getChildCount() > 0) {
            for (UIComponent kid : getChildren()) {
                restoreDescendantState(kid);
            }
        }
    }

    private void restoreDescendantState(UIComponent component) {
        // Reset the client identifier for this component
        String id = component.getId();
        component.setId(id); // Forces client id to be reset
        StateHelper stateHelper = getStateHelper();
        Map<String, SavedState> saved = (Map<String, SavedState>) stateHelper.get(PropertyKeys.saved);
        // Restore state for this component (if it is a EditableValueHolder)
        if (component instanceof EditableValueHolder) {
            EditableValueHolder input = (EditableValueHolder) component;
            String clientId = component.getClientId();

            SavedState state = (saved == null ? null : saved.get(clientId));
            if (state == null) {
                input.resetValue();
            } else {
                input.setValue(state.getValue());
                input.setValid(state.isValid());
                input.setSubmittedValue(state.getSubmittedValue());
                // This *must* be set after the call to setValue(), since
                // calling setValue() always resets "localValueSet" to true.
                input.setLocalValueSet(state.isLocalValueSet());
            }
        } else if (component instanceof UIForm) {
            UIForm form = (UIForm) component;
            String clientId = component.getClientId();
            SavedState state = (saved == null ? null : saved.get(clientId));
            if (state == null) {
                // submitted is transient state
                form.setSubmitted(false);
            } else {
                form.setSubmitted(state.getSubmitted());
            }
        }

        // Restore state for children of this component
        if (component.getChildCount() > 0) {
            for (UIComponent kid : component.getChildren()) {
                restoreDescendantState(kid);
            }
        }

        // Restore state for facets of this component
        if (component.getFacetCount() > 0) {
            for (UIComponent facet : component.getFacets().values()) {
                restoreDescendantState(facet);
            }
        }
    }

    private void saveDescendantState() {
        if (getChildCount() > 0) {
            for (UIComponent kid : getChildren()) {
                saveDescendantState(kid);
            }
        }
    }

    private void saveDescendantState(UIComponent component) {
        // Save state for this component (if it is a EditableValueHolder)
        StateHelper stateHelper = getStateHelper();
        Map<String, SavedState> saved = (Map<String, SavedState>) stateHelper.get(PropertyKeys.saved);
        String clientId = component.getClientId();
        if (component instanceof EditableValueHolder) {
            EditableValueHolder input = (EditableValueHolder) component;
            SavedState state = extractSavedState(saved, clientId);
            state.setValue(input.getLocalValue());
            state.setValid(input.isValid());
            state.setSubmittedValue(input.getSubmittedValue());
            state.setLocalValueSet(input.isLocalValueSet());
            if (state.hasDeltaState()) {
                stateHelper.put(PropertyKeys.saved, clientId, state);
            } else if (saved != null) {
                stateHelper.remove(PropertyKeys.saved, clientId);
            }
        } else if (component instanceof UIForm) {
            UIForm form = (UIForm) component;
            SavedState state = extractSavedState(saved, clientId);
            state.setSubmitted(form.isSubmitted());
            if (state.hasDeltaState()) {
                stateHelper.put(PropertyKeys.saved, clientId, state);
            } else if (saved != null) {
                stateHelper.remove(PropertyKeys.saved, clientId);
            }
        }

        // Save state for children of this component
        if (component.getChildCount() > 0) {
            for (UIComponent uiComponent : component.getChildren()) {
                saveDescendantState(uiComponent);
            }
        }

        // Save state for facets of this component
        if (component.getFacetCount() > 0) {
            for (UIComponent facet : component.getFacets().values()) {
                saveDescendantState(facet);
            }
        }
    }

    private SavedState extractSavedState(Map<String, SavedState> saved, String clientId) {
        SavedState state = null;
        if (saved == null) {
            state = new SavedState();
        }
        if (state == null) {
            state = saved.get(clientId);
            if (state == null) {
                state = new SavedState();
            }
        }
        return state;
    }

    private class SavedState implements Serializable {
        private Object submittedValue;
        private boolean submitted;
        private boolean valid = true;
        private Object value;
        private boolean localValueSet;

        Object getSubmittedValue() {
            return (this.submittedValue);
        }

        void setSubmittedValue(Object submittedValue) {
            this.submittedValue = submittedValue;
        }

        boolean isValid() {
            return (this.valid);
        }

        void setValid(boolean valid) {
            this.valid = valid;
        }

        Object getValue() {
            return (this.value);
        }

        public void setValue(Object value) {
            this.value = value;
        }

        boolean isLocalValueSet() {
            return (this.localValueSet);
        }

        public void setLocalValueSet(boolean localValueSet) {
            this.localValueSet = localValueSet;
        }

        public boolean getSubmitted() {
            return this.submitted;
        }

        public void setSubmitted(boolean submitted) {
            this.submitted = submitted;
        }

        public boolean hasDeltaState() {
            return submittedValue != null || value != null || localValueSet
                    || !valid || submitted;
        }
    }
}
