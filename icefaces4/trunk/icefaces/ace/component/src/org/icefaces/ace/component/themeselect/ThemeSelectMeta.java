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

import org.icefaces.ace.meta.annotation.ClientBehaviorHolder;
import org.icefaces.ace.meta.annotation.ClientEvent;
import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.baseMeta.UIInputMeta;
import org.icefaces.ace.resources.ACEResourceNames;
import org.icefaces.resources.ICEResourceDependencies;
import org.icefaces.resources.ICEResourceDependency;
import org.icefaces.resources.ICEResourceLibrary;

@Component(
        tagName = "themeSelect",
        componentClass = "org.icefaces.ace.component.themeselect.ThemeSelect",
        rendererClass = "org.icefaces.ace.component.themeselect.ThemeSelectRenderer",
        generatedClass = "org.icefaces.ace.component.themeselect.ThemeSelectBase",
        extendsClass = "javax.faces.component.UIInput",
        componentType = "org.icefaces.ace.component.ThemeSelect",
        rendererType = "org.icefaces.ace.component.ThemeSelectRenderer",
        componentFamily = "org.icefaces.ace.ThemeSelect",
        tlddoc = "The themeSelect component can be used to dynamically change the current ACE ThemeRoller theme in the application. " +
                "<p>For more information, see the <a href=\"http://wiki.icefaces.org/display/ICE/ThemeSelect\">ThemeSelect Wiki Documentation</a>."
)
@ICEResourceLibrary(ACEResourceNames.ACE_LIBRARY)
@ICEResourceDependencies({
        @ICEResourceDependency(name = ACEResourceNames.COMPONENTS_JS)
})
@ClientBehaviorHolder(events = {
        @ClientEvent(name = "valueChange", javadoc = "Fired when theme changes (default event). Theme change done on client side. Event used just for syncing with server side.",
                tlddoc = "Fired when theme changes (default event). Theme change done on client side. Event used just for syncing with server side.", defaultRender = "@this", defaultExecute = "@this")
}, defaultEvent = "valueChange")

public class ThemeSelectMeta extends UIInputMeta {

    @Property(tlddoc = "Access key that, when pressed, transfers focus to this element.")
    private String accesskey;

    @Property(tlddoc = "Direction indication for text that does not inherit directionality. Valid values are \"LTR\" (left-to-right) and \"RTL\" (right-to-left).")
    private String dir;

    @Property(tlddoc = "Flag indicating that this element must never receive focus or be included in a subsequent submit. A value of false causes no attribute to be rendered, while a value of true causes the attribute to be rendered as disabled=\"disabled\".")
    private boolean disabled;

    @Property(tlddoc = "Code describing the language used in the generated markup for this component.")
    private String lang;

    @Property(tlddoc = "A localized user presentable name for this component.")
    private String label;

    @Property(tlddoc = "CSS style(s) to be applied when this component is rendered.")
    private String style;

    @Property(tlddoc = "Space-separated list of CSS style class(es) to be applied when this element is rendered. This value must be passed through as the \"class\" attribute on generated markup.")
    private String styleClass;

    @Property(tlddoc = "Position of this element in the tabbing order for the current document. This value must be an integer between 0 and 32767.")
    private String tabindex;

    @Property(tlddoc = "Advisory title information about markup elements generated for this component.")
    private String title;

    @Property(tlddoc = "Alternate textual description of the element rendered by this component.")
    private String alt;
}
