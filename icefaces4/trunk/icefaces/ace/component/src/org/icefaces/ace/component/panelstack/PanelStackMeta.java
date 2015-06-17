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

package org.icefaces.ace.component.panelstack;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.baseMeta.UIComponentBaseMeta;
import org.icefaces.ace.meta.baseMeta.UIOutputMeta;
import org.icefaces.ace.meta.baseMeta.UISeriesBaseMeta;
import org.icefaces.ace.resources.ACEResourceNames;
import org.icefaces.resources.ICEResourceDependencies;
import org.icefaces.resources.ICEResourceDependency;
import org.icefaces.resources.ICEResourceLibrary;


@Component(
        tagName = "panelStack",
        componentClass = "org.icefaces.ace.component.panelstack.PanelStack",
        rendererClass = "org.icefaces.ace.component.panelstack.PanelStackRenderer",
        generatedClass = "org.icefaces.ace.component.panelstack.PanelStackBase",
        componentType = "org.icefaces.PanelStack",
        rendererType = "org.icefaces.PanelStackRenderer",
        extendsClass = "javax.faces.component.UIOutput",
        componentFamily = "org.icefaces.PanelStack",
        tlddoc = "panelStack manages child contentPanes, controlling which child is visible.")

@ICEResourceLibrary(ACEResourceNames.ACE_LIBRARY)
@ICEResourceDependencies({
	@ICEResourceDependency(name = "util/ace-core.js"),
	@ICEResourceDependency(name = "jquery/jquery.js"),
	@ICEResourceDependency(name = "util/blockui.js"),
	@ICEResourceDependency(name = "panel/panel.js")
})
public class PanelStackMeta extends UIOutputMeta {

     @Property( tlddoc="The id of the panel that is visible.")
     private String selectedId;

     @Property(tlddoc="Style to apply to the container element.")
     private String style;

     @Property(tlddoc="Style class to apply to the container element.")
     private String styleClass;



}
