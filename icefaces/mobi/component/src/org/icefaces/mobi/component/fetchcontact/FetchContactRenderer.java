/*
 * Copyright 2004-2014 ICEsoft Technologies Canada Corp.
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

package org.icefaces.mobi.component.fetchcontact;

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
import org.icefaces.mobi.util.CSSUtils;
import org.icefaces.mobi.util.MobiJSFUtils;
import org.icefaces.util.ClientDescriptor;
import org.icefaces.util.EnvUtils;

import static org.icefaces.ace.util.HTML.*;

public class FetchContactRenderer extends Renderer {
    
    private static final Logger log = Logger.getLogger(FetchContactRenderer.class.getName());
    
    @Override
    public void decode(FacesContext facesContext, UIComponent uiComponent) {
        FetchContact contactList = (FetchContact) uiComponent;
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

        //
        /*boolean targetAudience = cd.isICEmobileContainer() | cd.isSXRegistered();
        if (! targetAudience ) {
            component.setDisabled( true);
        }*/
        //writeStandardAttributes(writer, component, CSSUtils.STYLECLASS_BUTTON, CSSUtils.STYLECLASS_BUTTON_DISABLED);
        FetchContact contactList = (FetchContact) uiComponent;
		ResponseWriterWrapper writer = new ResponseWriterWrapper(facesContext.getResponseWriter());
		String clientId = contactList.getClientId();

		writer.startElement(SPAN_ELEM, contactList);
		writer.writeAttribute(ID_ATTR, clientId);
		writer.writeAttribute(CLASS_ATTR, "mobi-fetch-contact");
        StringBuilder baseClass = new StringBuilder(CSSUtils.STYLECLASS_BUTTON);
        ClientDescriptor cd = contactList.getClient();
		// button element
		writer.startElement(BUTTON_ELEM, contactList);
		writer.writeAttribute(ID_ATTR, clientId + "_button");
		writer.writeAttribute(NAME_ATTR, clientId + "_button");
		writer.writeAttribute(TYPE_ATTR, "button");
		writer.writeAttribute(TABINDEX_ATTR, contactList.getTabindex());
		//writeStandardAttributes(writer, contactList, baseClass.toString(), IDevice.DISABLED_STYLE_CLASS);
		//default value of unset in params is Integer.MIN_VALUE
		String script = "bridgeit.fetchContact('" + clientId + "', '', {postURL:'" + contactList.getPostURL() + "', "
			+ "cookies:{'JSESSIONID':'" + MobiJSFUtils.getSessionIdCookie(facesContext) + "'}, "
			+ "fields: '" +contactList.getFields() + "'});";
		writer.writeAttribute(ONCLICK_ATTR, script);
		boolean disabled = contactList.isDisabled();
		if (disabled) writer.writeAttribute(DISABLED_ATTR, "disabled");
		String style = contactList.getStyle();
		if (style != null) writer.writeAttribute(STYLE_ATTR, style);
		String styleClass = contactList.getStyleClass();
		if (styleClass != null) writer.writeAttribute(CLASS_ATTR, styleClass);
		writer.startElement(SPAN_ELEM, contactList);
		writer.writeText(contactList.getButtonLabel());
		writer.endElement(SPAN_ELEM);

		// themeroller support
		writer.startElement("span", contactList);
		writer.startElement("script", contactList);
		writer.writeAttribute("type", "text/javascript");
		writer.writeText("new ice.mobi.button('"+clientId+"_button');");
		writer.endElement("script");
		writer.endElement("span");

		writer.endElement(BUTTON_ELEM);

		writer.endElement(SPAN_ELEM);
    }

}

