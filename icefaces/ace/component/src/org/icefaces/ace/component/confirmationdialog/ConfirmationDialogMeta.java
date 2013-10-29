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

package org.icefaces.ace.component.confirmationdialog;

import org.icefaces.ace.resources.ACEResourceNames;
import org.icefaces.resources.ICEResourceDependencies;
import org.icefaces.resources.ICEResourceDependency;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.baseMeta.UIPanelMeta;
import org.icefaces.resources.ICEResourceLibrary;

@Component(
        tagName         = "confirmationDialog",
        componentClass  = "org.icefaces.ace.component.confirmationdialog.ConfirmationDialog",
        rendererClass   = "org.icefaces.ace.component.confirmationdialog.ConfirmationDialogRenderer",
        generatedClass  = "org.icefaces.ace.component.confirmationdialog.ConfirmationDialogBase",
        extendsClass    = "javax.faces.component.UIPanel",
        componentType   = "org.icefaces.ace.component.ConfirmationDialog",
        rendererType    = "org.icefaces.ace.component.ConfirmationDialogRenderer",
		componentFamily = "org.icefaces.ace.component",
		tlddoc = "The Confirmation Dialog is a component that displays a dialog that asks users to confirm or cancel their actions." +
                " If position of dialog is out of place, try putting it as last child of body." +
                "<p>For more information, see the <a href=\"http://wiki.icefaces.org/display/ICE/ConfirmationDialog\">ConfirmationDialog Wiki Documentation</a>."
        )
@ICEResourceLibrary(ACEResourceNames.ACE_LIBRARY)
@ICEResourceDependencies({
	@ICEResourceDependency(name= ACEResourceNames.COMPONENTS_JS)
})

public class ConfirmationDialogMeta extends UIPanelMeta {

	@Property(tlddoc="Name of the widget to access client side api")
	private String widgetVar;
	
	@Property(tlddoc="Text to be displayed in body.")
	private String message;
	
	@Property(tlddoc="Text for the header.")
	private String header;
	
	@Property(tlddoc="Message severity for the dislayed icon. Value can be whatever is appended to \".ui-icon-\" in the theme stylesheet.", defaultValue="alert")
	private String severity;
	
	@Property(tlddoc="Controls draggability.", defaultValue="true")
	private boolean draggable;
	
	@Property(tlddoc="Boolean value that specifies whether the document should be shielded with a partially transparent mask to require the user to close the Panel before being able to activate any elements in the document.", defaultValue="false")
	private boolean modal;
	
	@Property(tlddoc="Width of the dialog in pixels.", defaultValue="300")
	private int width;
	
	@Property(tlddoc="Height of the dialog in pixels.", defaultValue="Integer.MIN_VALUE")
	private int height;
	
	@Property(tlddoc="zindex property to control overlapping with other elements.", defaultValue="1000")
	private int zindex;
	
	@Property(tlddoc="Style class of the dialog container.")
	private String styleClass;
	
	@Property(tlddoc="Style to apply to the container element.")
	private String style;
	
	@Property(tlddoc="Effect to use when showing the dialog. Possible values are 'blind', 'clip', 'drop', 'explode, 'fade', 'fold', 'puff', 'slide', 'scale', 'bounce', 'highlight', 'pulsate', and 'shake' (Some effects are not supported in IE7, see wiki page for more information).")
	private String showEffect;
	
	@Property(tlddoc="Effect to use when hiding the dialog. Possible values are 'blind', 'clip', 'drop', 'explode, 'fade', 'fold', 'puff', 'slide', 'scale', 'bounce', 'highlight', and 'shake' (Some effects are not supported in IE browsers, see wiki page for more information).")
	private String hideEffect;
	
	@Property(tlddoc="Specifies where the dialog should be displayed. Possible values: \n1) a single string representing position within viewport: 'center', 'left', 'right', 'top', 'bottom'.\n2) an array containing an x,y coordinate pair in pixel offset from left, top corner of viewport (e.g. [350,100])\n3) an array containing x,y position string values (e.g. ['right','top'] for top right corner).")
	private String position;
	
	@Property(tlddoc="Specifies if dialog should be closed when escape key is pressed.", defaultValue="true")
	private boolean closeOnEscape;
	
	@Property(tlddoc="Specifies if close button should be displayed or not.", defaultValue="true")
	private boolean closable;
	
	@Property(tlddoc = "Specifies the jQuery selector(s) of the elements inside the dialog container that will be used as the drag handle(s). If this attribute isn't specified, any point of the dialog container can initiate the dragging action. The selectors are relative to the dialog's root element. When using multiple selectors, separate them by commas.")
	String dragHandle;
	
//	@Property(tlddoc="Appends dialog as a child of document body.", defaultValue="false")
//	private boolean appendToBody;
}
