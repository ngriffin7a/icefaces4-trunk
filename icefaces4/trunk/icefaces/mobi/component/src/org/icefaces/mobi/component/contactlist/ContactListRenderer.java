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

package org.icefaces.mobi.component.contactlist;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;

import org.icefaces.mobi.api.IContactList;
import org.icefaces.mobi.renderkit.ResponseWriterWrapper;
import org.icefaces.mobi.renderkit.ContactListCoreRenderer;
import org.icefaces.impl.application.AuxUploadResourceHandler;

public class ContactListRenderer extends Renderer {
    
    private static final Logger log = Logger.getLogger(ContactListRenderer.class.getName());
    
    @Override
    public void decode(FacesContext facesContext, UIComponent uiComponent) {
        ContactList contactList = (ContactList) uiComponent;
        String clientId = contactList.getClientId();
        try {
            Map requestParameterMap = facesContext.getExternalContext()
                .getRequestParameterMap();
            String contactListResult = (String) requestParameterMap.get(clientId);
            if (null == contactListResult)  {
                Map auxMap = AuxUploadResourceHandler.getAuxRequestMap();
                contactListResult = (String) auxMap.get(clientId);
            }
            if (null != contactListResult)  {
                contactList.setValue(contactListResult);
                ContactDecoder modelHelper = new ContactDecoder(contactListResult);
                contactList.setName(modelHelper.getName());
                contactList.setEmail(modelHelper.getEmail());
                contactList.setPhone(modelHelper.getPhone());
            }
        } catch (Exception e) {
            log.log(Level.WARNING, "Error decoding fetchContacts request paramaters.", e);
        }
    }


    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        ResponseWriterWrapper writer = new ResponseWriterWrapper(facesContext.getResponseWriter());
        ContactListCoreRenderer renderer = new ContactListCoreRenderer();
        renderer.encode((IContactList)uiComponent, writer);
    }

}

