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

package org.icefaces.mobi.renderkit;

import java.io.IOException;

import org.icefaces.mobi.api.IDevice;
import org.icefaces.mobi.util.CSSUtils;
import org.icefaces.mobi.util.ClientDescriptor;

import java.lang.StringBuilder;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.icefaces.mobi.util.HTML.*;
import javax.servlet.http.HttpSession;

public class DeviceCoreRenderer extends BaseCoreRenderer{
    private static final Logger logger =
            Logger.getLogger(DeviceCoreRenderer.class.toString());
    public void encode(IDevice component, IResponseWriter writer, boolean isJSP)
            throws IOException {
        String clientId = component.getClientId();
        StringBuilder baseClass = new StringBuilder(CSSUtils.STYLECLASS_BUTTON);
        String comptype = component.getComponentType();
        ClientDescriptor cd = component.getClient();
        boolean isEnhanced = cd.isICEmobileContainer()  || cd.isSXRegistered();
    //    logger.info("is SX="+cd.isSXRegistered()+" and useCookie="+ component.isUseCookie());
        if (cd.isICEmobileContainer())  {
            // button element
            writer.startElement(BUTTON_ELEM, component);
            writer.writeAttribute(ID_ATTR, clientId);
            writer.writeAttribute(NAME_ATTR, clientId + "_button");
            writer.writeAttribute(TYPE_ATTR, "button");
            writeStandardAttributes(writer, component, baseClass.toString(), IDevice.DISABLED_STYLE_CLASS);
            //default value of unset in params is Integer.MIN_VALUE
            String script = component.getScript(clientId, cd.isSXRegistered());
      //     logger.info("script = "+script);
            writer.writeAttribute(ONCLICK_ATTR, script);
            writer.writeText(component.getButtonLabel());
            writer.endElement(BUTTON_ELEM);
        }
        else if (cd.isSXRegistered())  {
      //      logger.info(" SX and using Cookie");
            //for iOS until we can store the ICEmobile-SX registration
            //without a session (likely a cookie)  for JSP
            writer.startElement(BUTTON_ELEM, component);
            writer.writeAttribute(ID_ATTR, clientId);
            writer.writeAttribute(NAME_ATTR, clientId+"_button");
            writer.writeAttribute(TYPE_ATTR, "button");
            writer.writeAttribute("data-id", component.getClientId());
        /*   Ithink this is only used for augmented reality..must check */
            if (null != component.getParams())  {
                writer.writeAttribute("data-params", component.getParams());
            }

            if (null != component.getSessionId())  {
                writer.writeAttribute("data-jsessionid", component.getSessionId());
            }
            writeStandardAttributes(writer, component, baseClass.toString(), IDevice.DISABLED_STYLE_CLASS);
            writer.writeAttribute("data-command", component.getComponentType());
            if (isJSP)  {
                writer.writeAttribute(ONCLICK_ATTR,
                        "ice.mobilesx(this)");
            } else {
                String postURL = component.getPostURL();
                if (null != postURL)  {
                    writer.writeAttribute("data-postURL", postURL);
                }
                writer.writeAttribute(ONCLICK_ATTR,
                        "ice.mobi.invoke(this)");
            }
            writer.writeText(component.getButtonLabel());
            writer.endElement(BUTTON_ELEM);
        }
        /** use html5 input type of file as default */
        // else  (!isEnhanced || component.isUseNative()){
        else {
      //      logger.info("comptype="+comptype);
            writer.startElement(INPUT_ELEM, component);
            if (comptype.equals("scan") || comptype.equals("aug")){
                writer.writeAttribute(TYPE_ATTR, INPUT_TYPE_TEXT);
                writer.writeAttribute(CLASS_ATTR, "mobi-input-text ui-input-text");
            }else {
                writer.writeAttribute(TYPE_ATTR, INPUT_TYPE_FILE);
            }
            writer.writeAttribute(ID_ATTR, clientId);
            writer.writeAttribute(NAME_ATTR, clientId);
            writeStandardAttributes(writer, component, "", "");
            if (comptype.equals("camera")){
                writer.writeAttribute("accept", "image/*");
            }
            if (!cd.isDesktopBrowser()){
                if (comptype.equals("camcorder")){
                    writer.writeAttribute("accept", "video/*");
                }
                if (comptype.equals("microphone")){
                    writer.writeAttribute("accept", "audio/*");
                }
            }
            writer.endElement(INPUT_ELEM);
        }
    }

}
