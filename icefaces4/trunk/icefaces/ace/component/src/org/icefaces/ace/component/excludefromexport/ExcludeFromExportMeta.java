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

package org.icefaces.ace.component.excludefromexport;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.baseMeta.UIComponentBaseMeta;

@Component(
        tagName = "excludeFromExport",
        componentClass = "org.icefaces.ace.component.excludefromexport.ExcludeFromExport",
        generatedClass = "org.icefaces.ace.component.excludefromexport.ExcludeFromExportBase",
        extendsClass = "javax.faces.component.UIComponentBase",
        componentType = "org.icefaces.ace.component.ExcludeFromExport",
        componentFamily = "org.icefaces.ace.component",
        tlddoc = "Embedding this component inside any component causes the data exporter to avoid including the values of such component in the exported file. It has no other effects." +
                 "<p>For more information, see the " +
                 "<a href=\"http://wiki.icefaces.org/display/ICE/ExcludeFromExport\">ExcludeFromExport Wiki Documentation</a>."
)
public class ExcludeFromExportMeta extends UIComponentBaseMeta {}
