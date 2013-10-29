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
package org.icefaces.ace.component.pagepanel;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Facet;
import org.icefaces.ace.meta.annotation.Facets;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.baseMeta.UIPanelMeta;

import javax.faces.component.UIComponent;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

@Component(
        tagName = "pagePanel",
        componentClass = "org.icefaces.ace.component.pagepanel.PagePanel",
        rendererClass = "org.icefaces.ace.component.pagepanel.PagePanelRenderer",
        generatedClass = "org.icefaces.ace.component.pagepanel.PagePanelBase",
        componentType = "org.icefaces.PagePanel",
        rendererType = "org.icefaces.PagePanelRenderer",
        extendsClass = "javax.faces.component.UIPanel",
        componentFamily = "org.icefaces.PagePanel",
        tlddoc = "Renders a header/body/footer layout that " +
                "is defined using the facet tag.  The facets for header and footer " +
                "are optional."
)
@ResourceDependencies({
		@ResourceDependency(library = "org.icefaces.component.pagepanel", name = "pagepanel.css")
})
public class PagePanelMeta extends UIPanelMeta {

    @Property(tlddoc = "CSS style(s) to be applied when this component is rendered.")
    private String style;

    @Property(tlddoc = "Space-separated list of CSS style class(es) to be applied when this element is rendered.")
    private String styleClass;

    /**
     * The page component defines three sections headr, body and footer which
     * are define by the <f:facet/> component pattern.
     */
    @Facets
    class FacetsMeta {
        @Facet
        UIComponent header;
        @Facet
        UIComponent body;
        @Facet
        UIComponent footer;
    }

}
