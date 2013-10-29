package org.icefaces.ace.component.dataview;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;

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
 * Time: 10:50 AM
 */
public class DataViewDetails extends DataViewDetailsBase {
    DataView view;

    /* keep details unrendered if using server activation and no row is active */
    @Override
    public boolean isRendered() {
        FacesContext context = FacesContext.getCurrentInstance();
        if (!context.getCurrentPhaseId().equals(PhaseId.RENDER_RESPONSE)) {
            Integer index = getView().getActiveRowIndex();
            return super.isRendered() && index != null && index > -1;
        }
        return super.isRendered();
    }

    private DataView getView() {
        if (view == null) {
            UIComponent parent = getParent();
            while (parent != null && !(parent instanceof DataView)) {
                parent = parent.getParent();
            }
            if (parent != null) view = (DataView)parent;
        }

        return view;
    }


}
