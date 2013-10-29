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

package org.icefaces.impl.component;

import org.icefaces.application.PushRenderer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.el.ValueExpression;
import javax.faces.component.StateHelper;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;

public class Push
extends UIComponentBase {
    private static final Logger LOGGER = Logger.getLogger(Push.class.getName());

    private enum PropertyKeys {
        group;

        private String toString;

        private PropertyKeys() {
            // Do nothing.
        }

        private PropertyKeys(final String toString) {
            this.toString = toString;
        }

        public String toString() {
            return toString != null ? toString : super.toString();
        }
    }

    public void encodeBegin(final FacesContext facesContext)  {
        PushRenderer.addCurrentView(getGroup());
    }

    @Override
    public String getFamily() {
        return "org.icefaces.impl.component.Push";
    }

    public String getGroup() {
        String returnValue = "top";
        ValueExpression valueExpression = getValueExpression(PropertyKeys.group.name());
        if (valueExpression != null) {
                returnValue = (String)valueExpression.getValue(getFacesContext().getELContext());
        } else {
            StateHelper stateHelper = getStateHelper();
            String valuesKey = PropertyKeys.group.name() + "_rowValues";
            Map clientValues = (Map)stateHelper.get(valuesKey);
            boolean mapNoValue = false;
            if (clientValues != null) {
                String clientId = getClientId();
                if (clientValues.containsKey( clientId ) ) {
                    returnValue = (String)clientValues.get(clientId);
                } else {
                    mapNoValue = true;
                }
            }
            if (mapNoValue || clientValues == null ) {
                String defaultKey = PropertyKeys.group.name() + "_defaultValues";
                Map defaultValues = (Map)stateHelper.get(defaultKey);
                if (defaultValues != null) {
                    if (defaultValues.containsKey("defValue" )) {
                        returnValue = (String)defaultValues.get("defValue");
                    }
                }
            }
        }
        return returnValue;
    }

    public void setGroup(final String group) {
        ValueExpression valueExpression = getValueExpression(PropertyKeys.group.name());
        if (valueExpression != null) {
            valueExpression.setValue(getFacesContext().getELContext(), group);
        } else {
            PhaseId phaseID = getFacesContext().getCurrentPhaseId();
            StateHelper stateHelper = getStateHelper();
            if (phaseID.equals(PhaseId.RENDER_RESPONSE) || phaseID.equals(PhaseId.RESTORE_VIEW))  {
                String defaultKey = PropertyKeys.group.name() + "_defaultValues";
                Map clientDefaults = (Map)stateHelper.get(defaultKey);
                if (clientDefaults == null) {
                    clientDefaults = new HashMap();
                    clientDefaults.put("defValue", group);
                    stateHelper.put(defaultKey, clientDefaults);
                }
            } else {
                String clientId = getClientId();
                String valuesKey = PropertyKeys.group.name() + "_rowValues";
                Map clientValues = (Map)stateHelper.get(valuesKey);
                if (clientValues == null) {
                    clientValues = new HashMap();
                }
                clientValues.put(clientId, group);
                stateHelper.put(valuesKey, clientValues);
            }
        }
    }
}
