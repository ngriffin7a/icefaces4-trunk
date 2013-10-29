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

package com.icesoft.faces.component.ext.renderkit;

import com.icesoft.faces.component.ext.HtmlOutputLink;

import javax.faces.component.UIComponent;

import java.net.URI;

public class OutputLinkRenderer
        extends com.icesoft.faces.renderkit.dom_html_basic.OutputLinkRenderer {

    protected boolean checkDisabled(UIComponent uiComponent) {

        HtmlOutputLink link = (HtmlOutputLink) uiComponent;
        return link.isDisabled();
    }
	
	protected String escapeIllegalCharacters(UIComponent uiComponent, String link) {
		HtmlOutputLink outputLink = (HtmlOutputLink) uiComponent;
		if (!outputLink.isPercentEncode()) return link;
		String result;
		try {
			URI uri = new URI(null, link, null);
			result = uri.toASCIIString();
		} catch (Exception e) {
			e.printStackTrace();
			result = link;
		}
		return result;
	}
}
