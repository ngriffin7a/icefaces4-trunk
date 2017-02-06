package org.icefaces.impl.component;

import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import java.io.IOException;
import java.util.Map;

public class Repeat extends UIData {

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

    public void encodeChildren(FacesContext context) throws IOException {
        String var = getVar();
        String varStatus = getVarStatus();
        Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
        DataModel model = getDataModel();
        int first = getFirst();
        int rows = getRows();
        int actualNoRows = getDataModel().getRowCount();
        rows = rows == 0 || rows > actualNoRows ? actualNoRows : rows;
        int index = first < rows ? first : 0;

        while (model.isRowAvailable() && index < first + rows) {
            setRowIndex(index);
            Object item = model.getRowData();
            requestMap.put(var, item);
            if (varStatus != null) {
                requestMap.put(varStatus, new VarStatus(first, first + rows, index));
            }
            super.encodeChildren(context);
            resetClientIDs(this);
            index++;
        }
        requestMap.remove(var);
        requestMap.remove(varStatus);
    }

    private void resetClientIDs(UIComponent component) {
        for (UIComponent child: component.getChildren()) {
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
