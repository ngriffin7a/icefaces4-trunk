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

package org.icefaces.sample.portlet.chat.resources;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * The ResourceUtil is used to retrieve localised messages and such from the resource
 * bundle.  It can also add localized FacesMessages to the chat page.
 */
public class ResourceUtil {

    private static final String BUNDLE = "org.icefaces.sample.portlet.chat.resources.messages";

    public static void addLocalizedMessage(String messagePatternKey) {
        String[] messageArgs = {};
        addLocalizedMessage(messagePatternKey,messageArgs);
    }

    public static void addLocalizedMessage(String messagePatternKey, String message) {
        String[] messageArgs = {message};
        addLocalizedMessage(messagePatternKey,messageArgs);
    }

    public static void addLocalizedMessage(String messagePatternKey, String[] messageArgs) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        String localizedMessage = getLocalizedMessage(messagePatternKey, messageArgs);
        facesContext.addMessage(null,new FacesMessage(localizedMessage));
    }

    public static String getLocalizedMessage(String messagePatternKey) {
        String[] messageArgs = {};
        return getLocalizedMessage(messagePatternKey,messageArgs);
    }

    public static String getLocalizedMessage(String messagePatternKey, String message) {
        String[] messageArgs = {message};
        return getLocalizedMessage(messagePatternKey,messageArgs);
    }

    public static String getLocalizedMessage(String messagePatternKey, String[] messageArgs){
        Locale locale = Locale.getDefault();
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if( facesContext != null ){
            UIViewRoot root = facesContext.getViewRoot();
            locale = root.getLocale();
        }
        String localizedPattern = ResourceUtil.getI18NString(locale,messagePatternKey);
        return MessageFormat.format(localizedPattern,(Object[])messageArgs);
    }

    public static String getI18NString(Locale locale, String key) {

        String text;
        try {
            ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE, locale);
            text = bundle.getString(key);
        } catch (Exception e) {
            text = "?UNKNOWN?";
        }
        return text;
    }


}
