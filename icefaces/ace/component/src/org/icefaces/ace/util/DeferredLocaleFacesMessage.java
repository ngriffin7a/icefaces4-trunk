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

import javax.faces.application.FacesMessage;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import java.util.Locale;
import java.util.logging.Logger;

/**
 * FacesMessage objects created by the FileEntry can exist before there is a
 * UIViewRoot, and so before there is a Locale. Additionally, if the
 * application changes the Locale during the lifecycle, then it would be best
 * for render-time evaluation of the Locale and therefore the message bundles
 * and their formatting.
 */
public class DeferredLocaleFacesMessage extends FacesMessage {
    private static Logger log = Logger.getLogger(DeferredLocaleFacesMessage.class.getName());

    private String messageId;
    /**
     * If true, messageId is the key for getting at the summary and detail
     * format strings in the resource bundles, else it is a format string
     * itself.
     */
    private boolean isKeyForResourceBundle;
    private Object[] params;
    private boolean loaded;

    public DeferredLocaleFacesMessage(Severity sev, String messageId,
            boolean isKeyForResourceBundle, Object[] params) {
        super(sev, "", "");
        this.messageId = messageId;
        this.isKeyForResourceBundle = isKeyForResourceBundle;
        this.params = params;
        this.loaded = false;
    }

    @Override
    public String getSummary() {
        load();
        return super.getSummary();
    }

    @Override
    public String getDetail() {
        load();
        return super.getDetail();
    }

    private void load() {
        if (!loaded) {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            UIViewRoot viewRoot = facesContext.getViewRoot();
            if (viewRoot != null) {
                Locale locale = viewRoot.getLocale();
                if (locale != null) {
                    if (isKeyForResourceBundle) {
                        String messageInfo[] = MessageUtils.loadMessageStrings(
                            facesContext, locale, messageId);
                        if (messageInfo != null && messageInfo.length >= 2) {
                            String summary = messageInfo[MessageUtils.SUMMARY];
                            summary = MessageUtils.formatString(
                                locale, summary, params);
                            setSummary(summary);

                            String detail = messageInfo[MessageUtils.DETAIL];
                            detail = MessageUtils.formatString(
                                locale, detail, params);
                            setDetail(detail);
                        }
                    } else {
                        String summaryDetail = MessageUtils.formatString(
                            locale, messageId, params);
                        setSummary(summaryDetail);
                        setDetail(summaryDetail);
                    }
                    loaded = true;
                }
            }
        }
    }
}
