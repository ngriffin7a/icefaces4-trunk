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

package org.icefaces.ace.renderkit;

import javax.faces.application.ProjectStage;
import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.application.ResourceHandlerWrapper;
import javax.faces.context.FacesContext;


public class AceSymbolicResourceHandler extends ResourceHandlerWrapper {
    private ResourceHandler handler;

    public AceSymbolicResourceHandler(ResourceHandler handler) {
        this.handler = handler;
    }

    public Resource createResource(String resourceName) {
        return this.createResource(resourceName, null, null);
    }

    public Resource createResource(String resourceName, String libraryName) {
        return this.createResource(resourceName, libraryName, null);
    }

    public Resource createResource(String resourceName, String libraryName, String contentType) {
        boolean uncompress = FacesContext.getCurrentInstance().isProjectStage(ProjectStage.Development);
        if (uncompress && libraryName != null &&
                ((libraryName.equals("icefaces.ace") &&
                        (resourceName.equals("util/ace-yui.js") ||
                                resourceName.equals("util/ace-core.js") ||
                                resourceName.equals("util/ace-jquery-ui.js") ||
                                resourceName.equals("util/ace-datatable.js") ||
                                resourceName.equals("util/ace-menu.js") ||
                                resourceName.equals("util/ace-components.js") ||
                                resourceName.equals("chart/ace-chart.js"))))) {
            String uncompressedResourceName = resourceName.replaceAll("\\.", ".uncompressed.");
            return super.createResource(uncompressedResourceName, libraryName, contentType);
		} else if (!uncompress && libraryName != null &&
                ((libraryName.equals("icefaces.ace") &&
					(resourceName.equals("jquery/jquery.js") ||
                                resourceName.equals("accordion/accordion.js") ||
                                resourceName.equals("animation/animation.js") ||
                                resourceName.equals("autocompleteentry/autocompleteentry.js") ||
                                resourceName.equals("checkboxbutton/checkboxbutton.js") ||
                                resourceName.equals("combobox/combobox.js") ||
                                resourceName.equals("confirmationdialog/confirmationdialog.js") ||
                                resourceName.equals("datetimeentry/datetimeentry.js") ||
                                resourceName.equals("datetimeentry/jquery-ui-timepicker-addon.js") ||
                                resourceName.equals("dialog/dialog.js") ||
                                resourceName.equals("dnd/dragdrop.js") ||
                                resourceName.equals("fileEntry/fileEntry.js") ||
                                resourceName.equals("gmap/gmap.js") ||
                                resourceName.equals("growlmessages/growlmessages.js") ||
                                resourceName.equals("growlmessages/jquery.jgrowl.js") ||
                                resourceName.equals("linkbutton/linkbutton.js") ||
                                resourceName.equals("list/list.js") ||
                                resourceName.equals("listcontrol/listcontrol.js") ||
                                resourceName.equals("maskedentry/maskedentry.js") ||
                                resourceName.equals("messages/messages.js") ||
                                resourceName.equals("notificationpanel/notificationpanel.js") ||
                                resourceName.equals("panel/panel.js") ||
                                resourceName.equals("printer/printer.js") ||
                                resourceName.equals("progressbar/progressbar.js") ||
                                resourceName.equals("pushbutton/pushbutton.js") ||
                                resourceName.equals("radiobutton/radiobutton.js") ||
                                resourceName.equals("resizable/resizable.js") ||
                                resourceName.equals("richtextentry/richtextentry.js") ||
                                resourceName.equals("selectmenu/selectmenu.js") ||
                                resourceName.equals("simpleselectonemenu/simpleselectonemenu.js") ||
                                resourceName.equals("sliderentry/slider.js") ||
                                resourceName.equals("splitpane/splitpane.js") ||
                                resourceName.equals("submitmonitor/submitmonitor.js") ||
                                resourceName.equals("textareaentry/textareaentry.js") ||
                                resourceName.equals("textentry/textentry.js") ||
                                resourceName.equals("themeselect/themeselect.js") ||
                                resourceName.equals("tooltip/tooltip.js") ||
                                resourceName.equals("tooltip/jquery.qtip-2.0.0.js") ||
                                resourceName.equals("tree/tree.js") ||
                                resourceName.equals("util/blockui.js"))))) {
            String compressedResourceName = resourceName.replaceAll("\\.js$", ".c.js");
            return super.createResource(compressedResourceName, libraryName, contentType);
        } else {
            return super.createResource(resourceName, libraryName, contentType);
        }
    }

    public ResourceHandler getWrapped() {
        return handler;
    }
}
