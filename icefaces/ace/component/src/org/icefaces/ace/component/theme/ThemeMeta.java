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

package org.icefaces.ace.component.theme;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.baseMeta.UIComponentBaseMeta;


@Component(
        tagName = "theme",
        componentClass = "org.icefaces.ace.component.theme.Theme",
        generatedClass = "org.icefaces.ace.component.theme.ThemeBase",
        handlerClass = "org.icefaces.ace.component.theme.ThemeHandler",
        componentType = "org.icefaces.AceTheme",
        rendererType = "org.icefaces.AceThemeRenderer",
        extendsClass = "javax.faces.component.UIOutput",
        componentFamily = "org.icefaces.aceConfigCode",
        tlddoc = "theme provides a way to change global parameters for ACE components."
)

public class ThemeMeta extends UIComponentBaseMeta {

    @Property(tlddoc="Name of the theme to apply to the page")
    private String value;
}
