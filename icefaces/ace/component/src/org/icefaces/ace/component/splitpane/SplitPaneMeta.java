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

package org.icefaces.ace.component.splitpane;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Facet;
import org.icefaces.ace.meta.annotation.Facets;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.annotation.Implementation;
import org.icefaces.ace.meta.baseMeta.UIPanelMeta;
import org.icefaces.ace.resources.ACEResourceNames;
import org.icefaces.resources.ICEResourceDependencies;
import org.icefaces.resources.ICEResourceDependency;
import org.icefaces.resources.ICEResourceLibrary;

import javax.faces.component.UIComponent;

@Component(
        tagName = "splitPane",
        componentClass = "org.icefaces.ace.component.splitpane.SplitPane",
        rendererClass = "org.icefaces.ace.component.splitpane.SplitPaneRenderer",
        generatedClass = "org.icefaces.ace.component.splitpane.SplitPaneBase",
        componentType = "org.icefaces.SplitPane",
        rendererType = "org.icefaces.SplitPaneRenderer",
        extendsClass = "javax.faces.component.UIPanel",
        componentFamily = "org.icefaces.SplitPane",
        tlddoc = "splitPane renders a div with two children that can be defined for  " +
                "page layout.  It can be scrollable, have columnDivider at certain location. " +
                " Requires a left and right facet for two, side by side, panels." +
                " Eventually will also be resizable.")
@ICEResourceLibrary(ACEResourceNames.ACE_LIBRARY)
@ICEResourceDependencies({
	@ICEResourceDependency(name = "util/ace-core.js"),
	@ICEResourceDependency(name = "jquery/jquery.js"),
	@ICEResourceDependency(name = "splitpane/splitpane.js")
})
public class SplitPaneMeta extends UIPanelMeta {
    @Property(defaultValue="true", tlddoc="Determines if the contents of this panel are both scrollable.")
    private boolean scrollable;

    @Property(defaultValue="25", tlddoc="An integer value representing the % of the total width assigned to the left pane.")
    private int columnDivider;

    @Property(tlddoc="Sets the CSS style definition to be applied to this component.", implementation=Implementation.GENERATE)
    private String style;

    @Property(tlddoc="Sets the CSS class to apply to this component.", implementation=Implementation.GENERATE)
    private String styleClass;

    @Facets
    class FacetsMeta {
        @Facet
        UIComponent left;
        @Facet
        UIComponent right;
    }
}
