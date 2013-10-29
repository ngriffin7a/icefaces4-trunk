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

package org.icefaces.ace.component.accordion;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.baseMeta.UIComponentBaseMeta;
import org.icefaces.resources.ICEResourceDependencies;

@Component(
        tagName         = "accordionPane",
        componentClass  = "org.icefaces.ace.component.accordion.AccordionPane",
        generatedClass  = "org.icefaces.ace.component.accordion.AccordionPaneBase",
        extendsClass    = "javax.faces.component.UIComponentBase",
        componentType   = "org.icefaces.ace.component.AccordionPane",
        rendererType    = "",
		componentFamily = "org.icefaces.ace.component",
		tlddoc = "The AccordionPane is a component used by accordion as a container to display contents." +
                 "<p>For more information, see the " +
                "<a href=\"http://wiki.icefaces.org/display/ICE/AccordionPane\">AccordionPane Wiki Documentation</a>."
        )

@ICEResourceDependencies({

})
public class AccordionPaneMeta extends UIComponentBaseMeta {

    @Property(tlddoc="Text that appears in the header of the pane.")
    private String title;
}
