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

package org.icefaces.ace.util;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 * This class has been designed, so the components can get FacesMessages 
 * either from their own ResourceBundle or an application's ResourceBundle. 
 * The component's ResourceBundle package is: org.icefaces.ace.resources
 */

public class MessageUtils {
    private static String DETAIL_SUFFIX = "_detail";
    static int SUMMARY = 0;
    static int DETAIL = 1;
    private static String ICE_MESSAGES_BUNDLE =
        "org.icefaces.ace.resources.messages";

    /**
     * Uses DeferredLocaleFacesMessage
     *
     * @param sev The FacesMessage Severity
     * @param messageId Either a key in the resource bundles for the summary
     * and detail format strings, or a format string itself like the
     * @param isKeyForResourceBundle If true, messageId is the key for getting
     * at the summary and detail format strings in the resource bundles, else
     * it is a format string itself like requiredMessage from UIData.
     * @param params The parameters to the string formatting
     */
    public static FacesMessage getMessage(
            FacesMessage.Severity sev, String messageId, boolean isKeyForResourceBundle, Object[] params) {
        return new DeferredLocaleFacesMessage(sev, messageId, isKeyForResourceBundle, params);
    }

    /**
     * Uses regular FacesMessage
     */
    public static FacesMessage getMessage(FacesContext facesContext,
            FacesMessage.Severity sev, String messageId, Object[] params) {
        Locale locale = facesContext.getViewRoot().getLocale();
        String messageInfo[] = loadMessageStrings(facesContext, locale, messageId);
        return getMessage(
            locale, sev, messageInfo[SUMMARY], messageInfo[DETAIL], params);
    }
    
    public static FacesMessage getMessage(Locale locale, 
            FacesMessage.Severity sev, String summary, String detail,
            Object[] params) {
        summary = formatString(locale, summary, params);
        detail = formatString(locale, detail, params);
        return new FacesMessage(sev, summary, detail);
    }

    static String[] loadMessageStrings(FacesContext facesContext, Locale locale, String messageId) {
//System.out.println("MessageUtils.loadMessageStrings()  messageId: " + messageId);
//System.out.println("MessageUtils.loadMessageStrings()    locale: " + locale);
        String messageInfo[] = new String[2];
        String bundleName = facesContext.getApplication().getMessageBundle();
//System.out.println("MessageUtils.loadMessageStrings()    application bundleName: " + bundleName);
        //see if the message has been overridden by the application
        if (bundleName != null) {
            try {
                loadMessageInfo(bundleName, locale, messageId, messageInfo);
            } catch (Exception e)  {
//System.out.println("MessageUtils.loadMessageStrings()    application bundle exception: " + e);
            }
        }

        //if not overridden then check in Icefaces message bundle.
        if (messageInfo[SUMMARY] == null && messageInfo[DETAIL]== null) {
            try {
            loadMessageInfo(ICE_MESSAGES_BUNDLE, locale, messageId, messageInfo);
            }
            catch(Exception e) {
//System.out.println("MessageUtils.getMessage()    EXCEPTION  e: " + e);
            }
        }
        return messageInfo;
    }

    private static void loadMessageInfo(String bundleName, 
                                Locale locale,
                                String messageId,  
                                String[] messageInfo) {
        ResourceBundle bundle = ResourceBundle.
                    getBundle(bundleName, locale, getClassLoader(bundleName));
        try {
            messageInfo[SUMMARY] = bundle.getString(messageId);
            messageInfo[DETAIL] = bundle.getString(messageId + DETAIL_SUFFIX);
        } catch (MissingResourceException e) {
//System.out.println("MessageUtils.loadMessageInfo()  MISSING  bundleName: " + bundleName + "  locale: " + locale + "  messageId: " + messageId);
        }
    }
    
    static String formatString(Locale locale, String pattern, Object[] params) {
        if (pattern != null && params != null) {
            MessageFormat format = new MessageFormat(pattern, locale);
            pattern = format.format(params);
        }
        return pattern;
    }
    
    public static ClassLoader getClassLoader(Object fallback) {
        ClassLoader classLoader = Thread.currentThread()
                                    .getContextClassLoader();
        if (classLoader == null) {
            classLoader = fallback.getClass().getClassLoader();
        }
        return classLoader;
    }
}
