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

import org.icefaces.ace.util.JSONBuilder;
import org.icefaces.ace.util.Utils;
import org.icefaces.render.MandatoryResourceComponent;
import org.icefaces.util.EnvUtils;

import javax.faces.FacesException;
import javax.faces.render.Renderer;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.component.UIComponent;
import java.io.IOException;
import java.util.logging.Logger;
import java.util.Locale;
import java.util.ResourceBundle;

@MandatoryResourceComponent(tagName="fileEntry", value="org.icefaces.ace.component.fileentry.FileEntry")
public class FileEntryRenderer extends Renderer {
    private static final Logger log = Logger.getLogger(FileEntry.class.getName());
    private static final String ACE_MESSAGES_BUNDLE = "org.icefaces.ace.resources.messages";
    private static final String MESSAGE_KEY_PREFIX = "org.icefaces.ace.component.fileEntry.";
    
    @Override
    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        UIComponent form = Utils.findParentForm(uiComponent);
        if (form == null) {
            throw new FacesException("FileEntry component must be contained in a form.");
        }

        FileEntry fileEntry = (FileEntry) uiComponent;
        String clientId = uiComponent.getClientId(facesContext);
        log.finer("FileEntryRenderer.encode  clientId: " + clientId);
        
        FileEntryConfig config = fileEntry.storeConfigForNextLifecycle(facesContext, clientId);
        
        ResponseWriter writer = facesContext.getResponseWriter();
        writer.startElement("div", uiComponent);
        writer.writeAttribute("id", clientId, "clientId");
        boolean disabled = fileEntry.isDisabled();
        boolean multiple = fileEntry.isMultiple();
        boolean autoUpload = fileEntry.isAutoUpload();
        /* Ideally we'd add these styles, but Firefox makes the Browse button flow outside
         * of the widget bordering.
         * ui-widget ui-widget-content ui-corner-all
         */
        Utils.writeConcatenatedStyleClasses(writer, "ice-file-entry",
            fileEntry.getStyleClass());
		writer.writeAttribute("style", fileEntry.getStyle(), "style");

        writer.startElement("div", uiComponent);
        // If multiple then render the buttons for  [+]Add Files  [^]Upload  [x]Delete
        // And render the hidden input type=file behind the AddFiles button
        /*
<div class="fileupload-buttonbar">
    <div class="fileupload-buttons">
        <!-- The fileinput-button span is used to style the file input field as button -->
        <span class="fileinput-button ui-button ui-widget ui-state-default ui-corner-all ui-button-text-icon-primary" role="button" aria-disabled="false">
            <span class="ui-button-icon-primary ui-icon ui-icon-plusthick"></span>
            <span class="ui-button-text">
                <span>Add files...</span>
            </span>
            <input type="file" name="files[]" multiple="">
        </span>
        <button type="submit" class="start ui-button ui-widget ui-state-default ui-corner-all ui-button-text-icon-primary" role="button" aria-disabled="false">
            <span class="ui-button-icon-primary ui-icon ui-icon-circle-arrow-e"/>
            <span class="ui-button-text">Start upload</span>
            </button>
        <button type="reset" class="cancel ui-button ui-widget ui-state-default ui-corner-all ui-button-text-icon-primary" role="button" aria-disabled="false">
            <span class="ui-button-icon-primary ui-icon ui-icon-cancel"/>
            <span class="ui-button-text">Cancel upload</span>
        </button>
        <button type="button" class="delete ui-button ui-widget ui-state-default ui-corner-all ui-button-text-icon-primary" role="button" aria-disabled="false">
            <span class="ui-button-icon-primary ui-icon ui-icon-trash"/>
            <span class="ui-button-text">Delete</span>
        </button>
        <input type="checkbox" class="toggle">
        <!-- The loading indicator is shown during file processing -->
        <span class="fileupload-loading"></span>
    </div>
</div>
         */
        Locale locale;
        ClassLoader classLoader;
        ResourceBundle bundle = null;
        if (multiple) {
            locale = facesContext.getViewRoot().getLocale();
            classLoader = Thread.currentThread().getContextClassLoader();
            String bundleName = facesContext.getApplication().getMessageBundle();
            if (classLoader == null) classLoader = bundleName.getClass().getClassLoader();
            if (bundleName == null) bundleName = ACE_MESSAGES_BUNDLE;
            bundle = ResourceBundle.getBundle(bundleName, locale, classLoader);
			
            writer.writeAttribute("class", "buttonbar", "styleClass");

            writer.startElement("div", uiComponent);
            writer.writeAttribute("class", "buttons", "styleClass");

            // Add files button (CSS classes are added dynamically in the client)
            writer.startElement("span", uiComponent);
            writer.writeAttribute("role", "button", null);
            writer.writeAttribute("aria-disabled", disabled ? "true" : "false", "disbled");

            // +
            writer.startElement("span", uiComponent);
            writer.writeAttribute("class", "ui-button-icon-primary ui-icon ui-icon-plusthick", "styleClass");
			writer.writeAttribute("style", "display:none;", null);
            writer.endElement("span");

            writer.startElement("span", uiComponent);
            writer.writeAttribute("class", "ui-button-text", "styleClass");
			writer.writeAttribute("style", "display:none;", null);

            // "Add files"
            writer.startElement("span", uiComponent);
            writer.writeText(bundle.getString(MESSAGE_KEY_PREFIX + "ADD_FILES"), uiComponent, null);
            writer.endElement("span");

            writer.endElement("span");  // ui-button-text

            // <input type="file"> goes here
        }
        writer.startElement("input", uiComponent);
        writer.writeAttribute("type", "file", "type");
        writer.writeAttribute("id", config.getIdentifier(), "clientId");
        writer.writeAttribute("name", config.getIdentifier(), "clientId");
        if (multiple) {
            writer.writeAttribute("multiple", "multiple", "multiple");
        }
        if (disabled) {
            writer.writeAttribute("disabled", "true", "disabled");
        }
        boolean ariaEnabled = EnvUtils.isAriaEnabled(facesContext);
        Integer tabindex = fileEntry.getTabindex();
        if (ariaEnabled && tabindex == null) tabindex = 0;
        if (tabindex != null) {
            writer.writeAttribute("tabindex", tabindex, "tabindex");
        }
        int size = fileEntry.getSize();
        if (size > 0) {
            writer.writeAttribute("size", size, "size");
        }
        if (multiple || autoUpload) {
            String onchange = JSONBuilder.create().
                beginFunction("ice.ace.fileentry.onchange").
                    item("event", false).
                    item(clientId).
                    item(multiple).
                    item(autoUpload).
                endFunction().toString();
            writer.writeAttribute("onchange", onchange, null);
        }
        writer.endElement("input");
        if (multiple) {
            writer.endElement("span");  // add-files ...

            writeMultipleButton(writer, uiComponent, disabled, bundle.getString(MESSAGE_KEY_PREFIX + "START_UPLOAD"),
                "submit", "start", "ui-icon-circle-arrow-e", clientId+"_start");
            writeMultipleButton(writer, uiComponent, disabled, bundle.getString(MESSAGE_KEY_PREFIX + "CANCEL_UPLOAD"),
                "button", "cancel", "ui-icon-cancel", clientId+"_cancel");
            /*
            writeMultipleButton(writer, uiComponent, disabled, "Remove file(s)",
                "button", "delete", "ui-icon-trash", clientId+"_delete");

            writer.startElement("input", uiComponent);
            writer.writeAttribute("type", "checkbox", null);
            writer.writeAttribute("class", "toggle", null);
            if (disabled) {
                writer.writeAttribute("disabled", "disabled", null);
            }
            writer.endElement("input");

            writer.startElement("span", uiComponent);
            writer.writeAttribute("class", "fileupload-loading", null);
            writer.endElement("span");
            */
            
            writer.endElement("div");   // buttons
        }
        writer.endElement("div");

        writer.startElement("div", uiComponent);
        writer.writeAttribute("class", "inactive", null);
		writer.startElement("div", uiComponent);
		writer.writeAttribute("class", "ui-progressbar ui-widget ui-widget-content ui-corner-all", null);
        writer.startElement("div", uiComponent);
        writer.writeAttribute("class", "ui-progressbar-value ui-widget-header ui-corner-left ui-corner-right", null);
		writer.endElement("div");
        writer.endElement("div");
        writer.endElement("div");

        if (multiple) {
            writer.startElement("div", uiComponent);
            writer.writeAttribute("id", clientId + "_multSel", "clientId");

            writer.startElement("table", uiComponent);
            writer.writeAttribute("class", "multiple-select-table", null);
            writer.writeAttribute("id", clientId + "_multSelTbl", "clientId");
            writer.endElement("table");

            writer.endElement("div");
			
			writer.startElement("script", uiComponent);
			writer.writeAttribute("type", "text/javascript", null);
			writer.writeText("ice.ace.jq(function(){"
				+ "var root = ice.ace.jq(ice.ace.escapeClientId('" + clientId + "'));"
				+ "var addFilesButton = root.find('.buttons').find('> span').eq(0);"
				+ "addFilesButton.addClass('add-files ui-button ui-widget ui-state-default ui-corner-all ui-button-text-icon-primary');"
				+ "addFilesButton.find('span').attr('style', '');"
				+ "root.find('.start').attr('style', '');"
				+ "root.find('.cancel').attr('style', '');"
				+ "});", uiComponent, null);
			writer.endElement("script");
        }

        writer.endElement("div");
    }

    protected void writeMultipleButton(ResponseWriter writer,
            UIComponent uiComponent, boolean disabled, String label,
            String buttonType, String buttonClass, String iconClass, String id)
            throws IOException {
        writer.startElement("button", uiComponent);
        writer.writeAttribute("id", id, null);
        writer.writeAttribute("class", buttonClass + " ui-button ui-widget ui-state-default ui-corner-all ui-button-text-icon-primary", null);
        writer.writeAttribute("type", buttonType, null);
		writer.writeAttribute("style", "display:none;", null);
        writer.writeAttribute("role", "button", null);
        writer.writeAttribute("aria-disabled", disabled ? "true" : "false", null);
        if (disabled) {
            writer.writeAttribute("disabled", "disabled", null);
        }
		if (id.endsWith("_cancel")) {
			writer.writeAttribute("onclick", "ice.ace.fileentry.clearFileSelection('" + id.substring(0,id.length()-7) + "');return false;", null);
		}

        writer.startElement("span", uiComponent);
        writer.writeAttribute("class", "ui-button-icon-primary ui-icon " + iconClass, null);
        writer.endElement("span");

        writer.startElement("span", uiComponent);
        writer.writeAttribute("class", "ui-button-text", null);
        writer.writeText(label, uiComponent, null);
        writer.endElement("span");
        
        writer.endElement("button");
    }
    
    @Override
    public void decode(FacesContext facesContext, UIComponent uiComponent) {
        FileEntry fileEntry = (FileEntry) uiComponent;
        String clientId = uiComponent.getClientId(facesContext);
        log.finer("FileEntryRenderer.decode  clientId: " + clientId);
        FileEntryResults results = FileEntry.retrieveResultsFromEarlierInLifecycle(facesContext, clientId);
        // If no new files have been uploaded, leave the old upload results in-place.
        if (results != null || fileEntry.isRequired()) {
            log.finer(
                "FileEntryRenderer.decode\n" +
                "  results ve: " + uiComponent.getValueExpression("results") + "\n" +
                "  results   : " + results);
            fileEntry.setResults(results);
        }

        boolean filesUploadedThisLifecycle = (results != null);
        FileEntryEvent event = new FileEntryEvent(
                fileEntry, filesUploadedThisLifecycle);
        fileEntry.queueEvent(event);
    }
}
