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

package org.icefaces.ace.component.celleditor;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.baseMeta.UIComponentBaseMeta;

@Component(
        tagName = "cellEditor",
        componentClass = "org.icefaces.ace.component.celleditor.CellEditor",
        generatedClass = "org.icefaces.ace.component.celleditor.CellEditorBase",
        rendererClass = "org.icefaces.ace.component.celleditor.CellEditorRenderer",
        extendsClass = "javax.faces.component.UIComponentBase",
        componentType = "org.icefaces.ace.component.CellEditor",
        rendererType = "org.icefaces.ace.component.CellEditorRenderer",
        componentFamily = "org.icefaces.ace.CellEditor",
        tlddoc = "<p>Renders a hidden input and visible display facet to be toggled between and submitted via the ace:rowEditor.</p>" +
                 "<p>For more information, see the <a href=\"http://wiki.icefaces.org/display/ICE/CellEditor\">CellEditor Wiki Documentation</a>.</p>"
)
public class CellEditorMeta  extends UIComponentBaseMeta {
}
