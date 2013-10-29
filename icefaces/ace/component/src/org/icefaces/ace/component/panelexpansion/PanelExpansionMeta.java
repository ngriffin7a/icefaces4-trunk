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

package org.icefaces.ace.component.panelexpansion;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Expression;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.baseMeta.UIColumnMeta;

import javax.el.MethodExpression;

@Component(
        tagName = "panelExpansion",
        componentClass = "org.icefaces.ace.component.panelexpansion.PanelExpansion",
        generatedClass = "org.icefaces.ace.component.panelexpansion.PanelExpansionBase",
        extendsClass = "javax.faces.component.UIColumn",
        componentType = "org.icefaces.ace.component.PanelExpansion",
        componentFamily = "org.icefaces.ace.PanelExpansion",
        tlddoc = "Renders a table-width panel filled with its child components, located underneath the row where ace:expansionToggler was activated." +
                 "<p>For more information, see the <a href=\"http://wiki.icefaces.org/display/ICE/PanelExpansion\">PanelExpansion Wiki Documentation</a>.</p>"
)
public class PanelExpansionMeta extends UIColumnMeta {}
