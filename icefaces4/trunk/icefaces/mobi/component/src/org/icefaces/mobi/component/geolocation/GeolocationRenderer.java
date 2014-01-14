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

package org.icefaces.mobi.component.geolocation;

import org.icefaces.mobi.util.HTML;
import org.icefaces.mobi.renderkit.CoreRenderer;
import org.icefaces.mobi.util.MobiJSFUtils;
import org.icefaces.mobi.util.ClientDescriptor;

import javax.faces.application.ProjectStage;
import javax.faces.application.Resource;
import javax.faces.component.UIComponent;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


public class GeolocationRenderer extends CoreRenderer {
    private static final Logger log = Logger.getLogger(GeolocationRenderer.class.getName());
    private static final String JS_NAME = "geolocation.js";
    private static final String JS_MIN_NAME = "geolocation-min.js";
    private static final String JS_LIBRARY = "org.icefaces.component.geolocation";

    private static final int UNDEFINED_TIMEOUT_VALUE = 0;   // no timeout
    private static final int UNDEFINED_MAXAGE_VALUE = 3600; // 1 hour

    @Override
    public void decode(FacesContext facesContext, UIComponent uiComponent) {
        Geolocation geolocation = (Geolocation) uiComponent;
        if (geolocation.isDisabled()) {
            return;
        }
        String clientId = geolocation.getClientId();
        try {
            Map requestParameterMap = facesContext.getExternalContext()
                .getRequestParameterMap();
            String nameHidden = clientId + "_field";
            String locationString = String.valueOf(
                requestParameterMap.get(nameHidden));
            if (null == locationString) {
                log.warning("No Location string present in GEOLocation upload");
                return;
            }
            if ("".equals(locationString)) {
                log.warning("Empty Location string in GEOLocation upload (position not found yet?)");
                return;
            }

            String[] params = locationString.split(",");
            Double[] decoded = new Double[4];
            for (int i = 0; i < params.length; i++) {
                try {
                    decoded[i] = Double.parseDouble(params[i]);
                } catch (Exception e) {
                    log.log(Level.FINE,
                            "Malformed geolocation " + i +
                                " " + locationString, e);
                }
            }
            //malformed or unprovided values are not propagated
            if (null != decoded[0]) {
                geolocation.setLatitude(decoded[0]);
            }
            if (null != decoded[1]) {
                geolocation.setLongitude(decoded[1]);
            }
            if (null != decoded[2]) {
                geolocation.setAltitude(decoded[2]);
            }
            if (null != decoded[3]) {
                geolocation.setDirection(decoded[3]);
            }


          //  decodeBehaviors(facesContext, geolocation);
        } catch (Exception e) {
            log.log(Level.WARNING, "Error decoding geo-location request paramaters.", e);
        }
    }


    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
        throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = uiComponent.getClientId(facesContext);
        Geolocation locator = (Geolocation) uiComponent;
    //    ClientBehaviorHolder cbh = (ClientBehaviorHolder) uiComponent;
     //   boolean hasBehaviors = !cbh.getClientBehaviors().isEmpty();
        // root element
        writer.startElement(HTML.SPAN_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, clientId, null);

        writer.startElement("input", uiComponent);
        writer.writeAttribute("type", "hidden", null);
        writer.writeAttribute(HTML.ID_ATTR, clientId + "_locHidden", null);
        writer.writeAttribute(HTML.NAME_ATTR, clientId + "_field", null);
        writer.writeAttribute(HTML.VALUE_ATTR, "", null);
        boolean disabled = locator.isDisabled();
        boolean singleSubmit = locator.isSingleSubmit();
        if (disabled) {
            writer.writeAttribute("disabled", "disabled", null);
        }
        writer.endElement("input");
        if (!disabled) {
            boolean includeHighPrecision;
            StringBuilder sb = new StringBuilder(255);
            String highPrecision = locator.getEnableHighPrecision();

            if ("asneeded".equalsIgnoreCase(highPrecision)) {
                includeHighPrecision = sniffDevices();
            } else {
                includeHighPrecision = Boolean.valueOf(highPrecision);
            }

            int maxAge = locator.getMaximumAge();
            int timeout = locator.getTimeout();
            boolean continuous = locator.isContinuousUpdates();

            if (continuous) {
                sb.append("ice.mobi.geolocation.watchLocation('").append(clientId).append("','");
            } else {
                sb.append("ice.mobi.geolocation.getLocation('").append(clientId).append("','");
            }

            sb.append(includeHighPrecision).append("', '");
            sb.append(maxAge).append("', '").append(timeout).append("'); ");

        /*    if (hasBehaviors) {
                sb.append(this.buildAjaxRequest(facesContext, cbh, "activate"));
            } else */
            if (singleSubmit) {
                String ssCall = "ice.se(null, '" + clientId + "');";
                sb.append(ssCall);
            }

            writer.startElement(HTML.SPAN_ELEM, uiComponent);
            writer.writeAttribute("id", clientId + "_script", "id");
            writer.startElement("script", uiComponent);
            writer.write(sb.toString());
            writer.endElement("script");
            writer.endElement(HTML.SPAN_ELEM);
        }
        writer.endElement(HTML.SPAN_ELEM);
    }

    private boolean sniffDevices() {
        ClientDescriptor client = MobiJSFUtils.getClientDescriptor();
        return (client.isAndroidOS() & client.isTabletBrowser()) || client.isBlackBerryOS() || client.isBlackBerry10OS();
    }
}
