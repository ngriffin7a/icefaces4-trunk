package org.icefaces.impl.component;

import javax.faces.component.*;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.faces.model.DataModel;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class Repeat extends UIData {
    private enum PropertyKeys {
        saved
    }

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
            iterateOverChildren(new Runnable() {
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
            iterateOverChildren(new Runnable() {
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
            iterateOverChildren(new Runnable() {
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
            iterateOverChildren(new Runnable() {
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
        iterateOverChildren(new Runnable() {
            public void run() {
                try {
                    Repeat.super.encodeChildren(context);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public void broadcast(FacesEvent event) throws AbortProcessingException {
        int actualNoRows = getDataModel().getRowCount();
        populateVar(getVar(), getVarStatus(), getValidatedFirst(actualNoRows), getValidatedLast(actualNoRows), getRowIndex());
        super.broadcast(event);
        unpopulateVar();
    }

    private int getValidatedFirst(int actualNoRows) {
        int first = getFirst();
        return first > actualNoRows ? 0 : first;
    }

    private int getValidatedLast(int actualNoRows) {
        int first = getValidatedFirst(actualNoRows);
        int rows = getRows();
        if (rows == 0 || rows + first > actualNoRows) {
            rows = actualNoRows - first;
        }
        return first + rows - 1;
    }

    private void populateVar(String var, String varStatus, int first, int last, int index) {
        final Map<String, Object> requestMap = FacesContext.getCurrentInstance().getExternalContext().getRequestMap();
        final DataModel model = getDataModel();
        final Object item = model.getRowData();
        requestMap.put(var, item);
        if (varStatus != null) {
            requestMap.put(varStatus, new VarStatus(first, last, index));
        }
    }

    private void unpopulateVar() {
        final String var = getVar();
        final String varStatus = getVarStatus();
        final Map<String, Object> requestMap = FacesContext.getCurrentInstance().getExternalContext().getRequestMap();
        requestMap.remove(var);
        requestMap.remove(varStatus);
    }

    private void iterateOverChildren(Runnable runnable) {
        final String var = getVar();
        final String varStatus = getVarStatus();
        final int actualNoRows = getDataModel().getRowCount();
        final int first = getValidatedFirst(actualNoRows);
        final int last = getValidatedLast(actualNoRows);

        int index = first < actualNoRows ? first : 0;

        while (index <= last) {
            setRowIndex(index);
            restoreState();
            populateVar(var, varStatus, first, last, index);
            runnable.run();
            saveState();
            resetClientIDs(this);
            index++;
        }
        unpopulateVar();
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

    private void restoreState() {
        for (UIComponent kid : getChildren()) {
            restoreState(kid);
        }
    }

    private void restoreState(UIComponent component) {
        StateHelper stateHelper = getStateHelper();
        Map<String, SavedState> saved = (Map<String, SavedState>) stateHelper.get(PropertyKeys.saved);
        if (component instanceof EditableValueHolder) {
            EditableValueHolder input = (EditableValueHolder) component;
            String clientId = component.getClientId();

            SavedState state = (saved == null ? null : saved.get(clientId));
            if (state == null) {
                input.resetValue();
            } else {
                input.setValue(state.value);
                input.setValid(state.valid);
                input.setSubmittedValue(state.submittedValue);
                //set after the call to setValue() to avoid "localValueSet" reset
                input.setLocalValueSet(state.localValueSet);
            }
        } else if (component instanceof UIForm) {
            UIForm form = (UIForm) component;
            String clientId = component.getClientId();
            SavedState state = (saved == null ? null : saved.get(clientId));
            if (state == null) {
                form.setSubmitted(false);
            } else {
                form.setSubmitted(state.submitted);
            }
        }

        if (component.getChildCount() > 0) {
            for (UIComponent kid : component.getChildren()) {
                restoreState(kid);
            }
        }

        if (component.getFacetCount() > 0) {
            for (UIComponent facet : component.getFacets().values()) {
                restoreState(facet);
            }
        }
    }

    private void saveState() {
        for (UIComponent kid : getChildren()) {
            saveState(kid);
        }
    }

    private void saveState(UIComponent component) {
        StateHelper stateHelper = getStateHelper();
        Map<String, SavedState> saved = (Map<String, SavedState>) stateHelper.get(PropertyKeys.saved);
        String clientId = component.getClientId();
        if (component instanceof EditableValueHolder) {
            EditableValueHolder input = (EditableValueHolder) component;
            SavedState state = extractSavedState(saved, clientId);
            state.value = input.getLocalValue();
            state.valid = input.isValid();
            state.submittedValue = input.getSubmittedValue();
            state.localValueSet = input.isLocalValueSet();
            if (state.hasDeltaState()) {
                stateHelper.put(PropertyKeys.saved, clientId, state);
            } else if (saved != null) {
                stateHelper.remove(PropertyKeys.saved, clientId);
            }
        } else if (component instanceof UIForm) {
            UIForm form = (UIForm) component;
            SavedState state = extractSavedState(saved, clientId);
            state.submitted = form.isSubmitted();
            if (state.hasDeltaState()) {
                stateHelper.put(PropertyKeys.saved, clientId, state);
            } else if (saved != null) {
                stateHelper.remove(PropertyKeys.saved, clientId);
            }
        }

        if (component.getChildCount() > 0) {
            for (UIComponent uiComponent : component.getChildren()) {
                saveState(uiComponent);
            }
        }

        if (component.getFacetCount() > 0) {
            for (UIComponent facet : component.getFacets().values()) {
                saveState(facet);
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
        Object submittedValue;
        boolean submitted;
        boolean valid = true;
        Object value;
        boolean localValueSet;

        public boolean hasDeltaState() {
            return submittedValue != null || value != null || localValueSet || !valid || submitted;
        }
    }
}
