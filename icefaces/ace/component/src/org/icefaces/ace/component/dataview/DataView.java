package org.icefaces.ace.component.dataview;

import org.icefaces.ace.util.Utils;
import org.icefaces.ace.model.dataview.*;
import org.icefaces.ace.util.ClientDescriptor;

import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Copyright 2010-2013 ICEsoft Technologies Canada Corp.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * <p/>
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * <p/>
 * User: Nils Lundquist
 * Date: 2013-04-01
 * Time: 10:47 AM
 */
public class DataView extends DataViewBase implements IDataView, NamingContainer {

    private static Logger logger = Logger.getLogger(DataViewRenderer.class.getName());

    public static final String DATAVIEW_CLASS = "mobi-dv ui-widget ui-widget-content";
    public static final String DATAVIEW_MASTER_CLASS = "mobi-dv-mst";
    public static final String DATAVIEW_DETAIL_CLASS = "mobi-dv-det ui-widget ui-widget-content";
    public static final String DATAVIEW_HEAD_CLASS = "mobi-dv-head";
    public static final String DATAVIEW_FOOT_CLASS = "mobi-dv-foot";
    public static final String DATAVIEW_BODY_CLASS = "mobi-dv-body";
    public static final String DATAVIEW_SORT_INDICATOR_CLASS = "mobi-dv-si";
    public static final String DATAVIEW_BOOL_COLUMN_CLASS = "mobi-dv-bool";
    public static final String DATAVIEW_COLUMN_CLASS = "mobi-dv-c";
    public static final String DATAVIEW_ROW_ACTIVE_CLASS = "ui-state-active";
    public static final String DATAVIEW_HEADER_ROW_CLASS = "ui-widget-header";

    protected static final DataViewDataModel EMPTY_DATA_MODEL = new DataViewListDataModel(Collections.EMPTY_LIST);

    private Map<String, Object> requestMap;
    private boolean decodedActive = false;

    @Override
    public boolean visitTree(VisitContext context,
                             VisitCallback callback) {

        // First check to see whether we are visitable.  If not
        // short-circuit out of this subtree, though allow the
        // visit to proceed through to other subtrees.
        if (!isVisitable(context))
            return false;

        // Push ourselves to EL before visiting
        FacesContext facesContext = context.getFacesContext();
        pushComponentToEL(facesContext, null);

        /* Decode new index early to alter server state to match sent client state*/
        if (!decodedActive && ActivationMode.client.equals(getActivationMode())
                && !context.getFacesContext().getCurrentPhaseId().equals(PhaseId.RESTORE_VIEW)) {
            decodeIndex(facesContext);
        }

        try {
            // Visit ourselves.  Note that we delegate to the
            // VisitContext to actually perform the visit.
            VisitResult result = context.invokeVisitCallback(this, callback);

            // If the visit is complete, short-circuit out and end the visit
            if (result == VisitResult.COMPLETE)
                return true;

            // Visit children if necessary
            if (result == VisitResult.ACCEPT) {
                Iterator<UIComponent> kids = this.getFacetsAndChildren();

                while(kids.hasNext()) {
                    UIComponent kid = kids.next();
                    boolean details = kid instanceof DataViewDetails;

                    if (details) initDetailContext(facesContext);
                    boolean done = kid.visitTree(context, callback);

                    if (details) clearDetailContext(facesContext);

                    // If any kid visit returns true, we are done.
                    if (done)
                        return true;
                }
            }
        }
        finally {
            // Pop ourselves off the EL stack
            popComponentFromEL(facesContext);
        }

        // Return false to allow the visit to continue
        return false;
    }

    @Override
    public void processUpdates(FacesContext context) {
        initDetailContext(context);
        super.processUpdates(context);
        clearDetailContext(context);

        if (!decodedActive) {
            decodeIndex(context);
        }
    }

    @Override
    public void processValidators(FacesContext context) {
        initDetailContext(context);
        super.processValidators(context);
        clearDetailContext(context);
    }

    @Override
    public void processDecodes(FacesContext context) {
        if (ActivationMode.client.equals(getActivationMode()) && !decodedActive) {
            decodeIndex(context);
        }

        initDetailContext(context);
        super.processDecodes(context);
        clearDetailContext(context);
    }

    @Override
    public Object processSaveState(FacesContext context) {
        initDetailContext(context);
        Object o = super.processSaveState(context);
        clearDetailContext(context);
        return o;
    }

    @Override
    public void processRestoreState(FacesContext context, Object state) {
        initDetailContext(context);
        super.processRestoreState(context, state);
        clearDetailContext(context);

        /* if not post-restore restoreState overwrites new active index */
        if (ActivationMode.client.equals(getActivationMode()) && !decodedActive) {
            decodeIndex(context);
        }
    }

    private void decodeIndex(FacesContext context) {
        String indexStr = context.getExternalContext().getRequestParameterMap().get(getClientId() + "_active");
        if (indexStr != null && indexStr.length() > 0) {
            int newIndex = Integer.parseInt(indexStr);
            if (newIndex >= 0) setActiveRowIndex(newIndex);
            else setActiveRowIndex(null);
        }

        decodedActive = true;
    }

    public void clearDetailContext(FacesContext context) {
        getRequestMap(context).remove(getVar());
    }

    public void initDetailContext(FacesContext context) {
        Integer index = getActiveRowIndex();

        if (index != null &&index >= 0)
            getRequestMap(context).put(getVar(), getDataModel().getDataByIndex(index));
    }

    private Map<String,Object> getRequestMap(FacesContext context) {
        if (requestMap == null) {
            requestMap = context.getExternalContext().getRequestMap();
        }
        return requestMap;
    }

    protected DataViewColumns getColumns() {
        for (UIComponent child : getChildren())
            if (child instanceof DataViewColumns)
                return (DataViewColumns)child;

        logger.log(Level.WARNING, "DataView: " + getClientId() + " - doesn't have the nessecary 'ace:dataViewColumns' child component.");

        return null;
    }

    protected DataViewDataModel getDataModel() {
        Object value = getValue();

        if (value != null) {
            if (value instanceof List)
                return new DataViewListDataModel((List) value);
            else if (value instanceof DataViewLazyDataModel)
                return (DataViewDataModel)value;
            else {
                logger.log(Level.WARNING, "DataView: " + getClientId() + " - Invalid type for 'value' attribute.");
                return EMPTY_DATA_MODEL;
            }
        }

        logger.log(Level.WARNING, "DataView: " + getClientId() + " - 'value' attribute is null.");
        return EMPTY_DATA_MODEL;
    }

    protected DataViewDetails getDetails() {
        for (UIComponent child : getChildren())
            if (child instanceof DataViewDetails)
                return (DataViewDetails)child;

        logger.log(Level.WARNING, "DataView: " + getClientId() + " - doesn't have the nessecary 'ace:dataViewDetails' child component.");

        return null;
    }

    public ClientDescriptor getClient() {
        return Utils.getClientDescriptor();
    }

    protected Integer[] getReactiveColumnPriorities() {
        DataViewColumnsModel model = getColumns().getModel();
        Integer[] priorities = new Integer[model.size()];
        Integer highest = null;
        List<Integer> unrenderedIntegers = new ArrayList<Integer>();

        //Explicit priorities
        for (IndexedIterator<DataViewColumnModel> columnIter = model.iterator(); columnIter.hasNext();) {
            DataViewColumnModel columnModel = columnIter.next();
            Integer pri = columnModel.getReactivePriority();
            int index = columnIter.getIndex();
            if (columnModel.isRendered()) {
                priorities[index] = pri;
                if (highest == null || (pri != null && pri > highest))
                    highest = pri;
            } else unrenderedIntegers.add(index);
        }

        //Second pass priorities
        if (highest == null) highest = -1;
        for (int i = 0; i < priorities.length; i++) {
            Integer pri = priorities[i];
            if (pri == null && !unrenderedIntegers.contains(i))
                priorities[i] = ++highest;
        }

        //Remove null gaps if any
        Integer[] finalPri = new Integer[model.size()];
        int finalIndex = 0;
        for (int i = 0; i < priorities.length; i++)
            if (priorities[i] != null)
                finalPri[finalIndex++] = priorities[i];

        //Truncate
        Integer[] trimPri = new Integer[finalIndex];
        for (int i = 0; i < finalPri.length; i++)
            if (finalPri[i] != null)
                trimPri[i] = finalPri[i];
            else break;

        return trimPri;
    }
}
