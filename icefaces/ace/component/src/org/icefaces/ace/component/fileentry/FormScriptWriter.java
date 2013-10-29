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

package org.icefaces.ace.component.fileentry;

import org.icefaces.ace.util.Utils;
import org.icefaces.ace.util.ScriptWriter;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.component.UIOutput;
import javax.faces.component.UINamingContainer;
import javax.faces.component.UIForm;
import java.io.IOException;
import java.util.logging.Logger;
import java.text.MessageFormat;

/**
 * Writer to add common javascript blurb to form as a &lt;script&gt; element
 */
public class FormScriptWriter extends UIOutput {
    private static Logger log = Logger.getLogger(FileEntry.class.getName()+".script");

    private String disablingAttribute;
    private String id;

    /**
     * Write a snippet of un-escaped javascript. It is apparently unnecessary
     * for javascript elements to have id's, but if one is passed, it's used.
     *
     * @param disablingAttribute If the form has this attribute set, don't
     *        render the javascript.
     * @param id An id for the script element.
     */
    public FormScriptWriter(String disablingAttribute, String id) {
        this.disablingAttribute = disablingAttribute;
        this.id = id;
        // Always give the component an id, even if the markup doesn't get one
        if (id != null) {
            setId(id);
        }
        else {
            setId(FacesContext.getCurrentInstance().getViewRoot().createUniqueId());
        }
        setTransient(true);
        this.setRendererType(null);
    }

    @Override
    public void encodeBegin(FacesContext context) throws IOException {
        UIForm form = Utils.findParentForm(this);
        if (form == null) {
            return;
        }
        String formClientId = form.getClientId(context);
        if (disablingAttribute != null && disablingAttribute.length() > 0) {
            if (form.getAttributes().get(disablingAttribute) != null) {
                log.finest("FormScriptWriter  "+disablingAttribute+"  " + formClientId);
                return;
            }
        }

        //TODO render into javascript. Probably have to scrape out notion of MessageFormat.
        String progressResourcePath = "";
        String progressPushId = "";
        if (PushUtils.isPushPresent()) {
            progressResourcePath = PushUtils.getProgressResourcePath(context, form);
            progressPushId = PushUtils.getPushId(context, form);
            log.finest(
                "FormScriptWriter\n" +
                "  progressResourcePath: " + progressResourcePath + "\n" +
                "  progressPushId: " + progressPushId);
        }

        String iframeClientIdSuffix =
                UINamingContainer.getSeparatorChar(context) +
                FileEntryFormSubmit.IFRAME_ID;
        String script = "ice.ace.fileentry.captureFormOnsubmit('" + formClientId +
                "', '" + formClientId + iframeClientIdSuffix + "', '" +
                progressPushId + "', '" + progressResourcePath + "');";
        log.finer("FormScriptWriter  script: " + script);

        ScriptWriter.insertScript(context, this, script, getClientId(context));
    }

    @Override
    public void encodeEnd(FacesContext context) throws IOException {
    }
}