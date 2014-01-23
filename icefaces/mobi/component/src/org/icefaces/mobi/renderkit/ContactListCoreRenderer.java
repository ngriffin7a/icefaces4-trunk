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

import static org.icefaces.mobi.util.HTML.*;

import java.io.IOException;

import org.icefaces.mobi.api.IContactList;
import org.icefaces.mobi.util.CSSUtils;
import org.icefaces.ace.util.ClientDescriptor;

public class ContactListCoreRenderer extends BaseCoreRenderer {
    
    public void encode(IContactList component, IResponseWriter writer)
            throws IOException {
        
        String clientId = component.getClientId();
        ClientDescriptor cd = component.getClient();

        writer.startElement(INPUT_ELEM);
        writer.writeAttribute(ID_ATTR, clientId);
        writer.writeAttribute(TYPE_ATTR, "button");
        //
        boolean targetAudience = cd.isICEmobileContainer() | cd.isSXRegistered();
        if (! targetAudience ) {
            component.setDisabled( true);
        }
        writeStandardAttributes(writer, component, CSSUtils.STYLECLASS_BUTTON, CSSUtils.STYLECLASS_BUTTON_DISABLED);
        StringBuilder args = new StringBuilder();

        String fields = component.getFields();
        if (fields != null && !"".equals(fields)) {
            if (args.length() > 0) {
                args.append("&");
            }
            args.append("fields=").append(fields);
        }

        writer.writeAttribute("data-command", "fetchContacts");
        writer.writeAttribute("data-id", clientId);
        if (args.length() > 0)  {
            writer.writeAttribute("data-params", args.toString());
        }
        String postURL = component.getPostURL();
        if (null != postURL)  {
            writer.writeAttribute("data-postURL", postURL);
        }
        String sessionId = component.getSessionId();
        if (null != sessionId)  {
            writer.writeAttribute("data-jsessionid", sessionId);
        }
        writer.writeAttribute(ONCLICK_ATTR, "ice.mobi.invoke(this)");

        writer.writeAttribute(VALUE_ATTR, component.getButtonLabel());
        writer.endElement(INPUT_ELEM);
    }

}
