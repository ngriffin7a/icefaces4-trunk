package org.icefaces.impl.component;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;
import javax.faces.event.PostValidateEvent;
import javax.faces.event.PreValidateEvent;
import javax.faces.model.DataModel;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Repeat extends UIData {

    public Repeat() {
        setRowStatePreserved(false);
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
            VisitResult result = context.invokeVisitCallback(this, callback);
            if (result == VisitResult.COMPLETE)
                return true;

            if (result == VisitResult.ACCEPT) {
                iterateOverChildren(facesContext, new Runnable() {
                    public void run() {
                        List<UIComponent> children = Repeat.this.getChildren();
                        for (UIComponent child : children) {
                            child.visitTree(context, callback);
                        }
                    }
                });
            }
        } finally {
            // Pop ourselves off the EL stack
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
            Object item = model.getRowData();
            requestMap.put(var, item);
            if (varStatus != null) {
                requestMap.put(varStatus, new VarStatus(first, last, index));
            }
            runnable.run();
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
}
