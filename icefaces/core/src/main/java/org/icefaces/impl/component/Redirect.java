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

import org.icefaces.util.JavaScriptRunner;

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

public class Redirect
extends UIComponentBase {
    private static final Logger LOGGER = Logger.getLogger(Redirect.class.getName());

    private enum PropertyKeys {
        href;

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

    @Override
    public void encodeAll(final FacesContext facesContext)
    throws IOException {
        String _href = getHref();
        if (_href != null && _href.trim().length() != 0) {
            JavaScriptRunner.runScript(getFacesContext(), "window.location = \"" + _href.replace("\"", "\\\"") + "\";");
        }
    }

    @Override
    public String getFamily() {
        return "org.icefaces.impl.component.Redirect";
    }

    public String getHref() {
        String _returnValue = "top";
        ValueExpression _valueExpression = getValueExpression(PropertyKeys.href.name());
        if (_valueExpression != null) {
            _returnValue = (String)_valueExpression.getValue(getFacesContext().getELContext());
        } else {
            StateHelper _stateHelper = getStateHelper();
            String _valuesKey = PropertyKeys.href.name() + "_rowValues";
            Map _clientValueMap = (Map)_stateHelper.get(_valuesKey);
            boolean _mapNoValue = false;
            if (_clientValueMap != null) {
                String _clientID = getClientId();
                if (_clientValueMap.containsKey(_clientID)) {
                    _returnValue = (String)_clientValueMap.get(_clientID);
                } else {
                    _mapNoValue = true;
                }
            }
            if (_mapNoValue || _clientValueMap == null) {
                String _defaultKey = PropertyKeys.href.name() + "_defaultValues";
                Map _defaultValueMap = (Map)_stateHelper.get(_defaultKey);
                if (_defaultValueMap != null) {
                    if (_defaultValueMap.containsKey("defValue")) {
                        _returnValue = (String)_defaultValueMap.get("defValue");
                    }
                }
            }
        }
        return _returnValue;
    }

    public void setHref(final String href) {
        ValueExpression _valueExpression = getValueExpression(PropertyKeys.href.name());
        if (_valueExpression != null) {
            _valueExpression.setValue(getFacesContext().getELContext(), href);
        } else {
            PhaseId _phaseID = getFacesContext().getCurrentPhaseId();
            StateHelper _stateHelper = getStateHelper();
            if (_phaseID.equals(PhaseId.RENDER_RESPONSE) || _phaseID.equals(PhaseId.RESTORE_VIEW)) {
                String _defaultKey = PropertyKeys.href.name() + "_defaultValues";
                Map _clientDefaultMap = (Map)_stateHelper.get(_defaultKey);
                if (_clientDefaultMap == null) {
                    _clientDefaultMap = new HashMap();
                    _clientDefaultMap.put("defValue", href);
                    _stateHelper.put(_defaultKey, _clientDefaultMap);
                }
            } else {
                String _clientID = getClientId();
                String _valuesKey = PropertyKeys.href.name() + "_rowValues";
                Map _clientValueMap = (Map)_stateHelper.get(_valuesKey);
                if (_clientValueMap == null) {
                    _clientValueMap = new HashMap();
                }
                _clientValueMap.put(_clientID, href);
                _stateHelper.put(_valuesKey, _clientValueMap);
            }
        }
    }
}
