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

package org.icefaces.ace.component.themeselect;

import org.icefaces.ace.util.Constants;

import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.application.ResourceHandlerWrapper;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

public class ThemeSymbolicResourceHandler extends ResourceHandlerWrapper {
    private ResourceHandler handler;

    public ThemeSymbolicResourceHandler(ResourceHandler handler) {
        this.handler = handler;
    }

    public Resource createResource(String resourceName) {
        return this.createResource(resourceName, null, null);
    }

    public Resource createResource(String resourceName, String libraryName) {
        return this.createResource(resourceName, libraryName, null);
    }

    public Resource createResource(String resourceName, String libraryName, String contentType) {
        if (resourceName.equals("theme.css") && libraryName != null && libraryName.equals("icefaces.ace")) {
            FacesContext context = FacesContext.getCurrentInstance();
            ExternalContext externalContext = context.getExternalContext();
            String theme = (String) externalContext.getSessionMap().get(Constants.THEME_PARAM);
            if (theme == null) {
                String defaultTheme = externalContext.getInitParameter(Constants.THEME_PARAM);
                theme = defaultTheme == null ? "sam" : defaultTheme;
            } else {
                theme = theme.trim();
            }
            //acquire the selected theme
            String name;
            String library;
            if (theme.equalsIgnoreCase("sam")) {
                library = "icefaces.ace";
                name = "themes/sam/theme.css";
            } else if (theme.equalsIgnoreCase("rime")) {
                library = "icefaces.ace";
                name = "themes/rime/theme.css";
            } else {
                library = "ace-" + theme;
                name = "theme.css";
            }

            return super.createResource(name, library);
        } else {
            return super.createResource(resourceName, libraryName, contentType);
        }
    }

    public ResourceHandler getWrapped() {
        return handler;
    }
}
