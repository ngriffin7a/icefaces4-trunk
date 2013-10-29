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
package org.icefaces.ace.component.message;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Implementation;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.annotation.Required;
import org.icefaces.ace.meta.baseMeta.UIMessageMeta;
import org.icefaces.ace.resources.ACEResourceNames;
import org.icefaces.resources.ICEResourceDependencies;
import org.icefaces.resources.ICEResourceDependency;
import org.icefaces.resources.ICEResourceLibrary;

@Component(
        tagName = "message",
        componentClass = "org.icefaces.ace.component.message.Message",
        rendererClass = "org.icefaces.ace.component.message.MessageRenderer",
        generatedClass = "org.icefaces.ace.component.message.MessageBase",
        extendsClass = "javax.faces.component.UIMessage",
        componentType = "org.icefaces.ace.component.Message",
        rendererType = "org.icefaces.ace.component.MessageRenderer",
        componentFamily = "org.icefaces.ace.Message",
        tlddoc = "The message tag renders the first Faces message (if redisplay is true) or " +
                "the first undisplayed Faces message (if redisplay is false) for a specific component. " +
                "Styling is done by predefined jQuery classes in theme stylesheets:<ul>" +
                "<li>Info: ui-icon-info w/ ui-state-highlight css</li>" +
                "<li>Warn: ui-icon-notice w/ ui-state-highlight css</li>" +
                "<li>Error: ui-icon-alert w/ ui-state-error css</li>" +
                "<li>Fatal: ui-icon-alert w/ ui-state-error css</li>" +
                "</ul>"
)
@ICEResourceLibrary(ACEResourceNames.ACE_LIBRARY)
@ICEResourceDependencies({
        @ICEResourceDependency(name = ACEResourceNames.COMPONENTS_JS)
})
public class MessageMeta extends UIMessageMeta {

    @Property(name = "for", implementation = Implementation.EXISTS_IN_SUPERCLASS, required = Required.yes)
    private String forValue;

    @Property(tlddoc = "CSS style(s) to be applied when this component is rendered.")
    private String style;

    @Property(tlddoc = "Space-separated list of CSS style class(es) to be applied when this element is rendered. " +
            "This value must be passed through as the \"class\" attribute on generated markup.")
    private String styleClass;

    @Property(tlddoc = "Flag indicating that characters that are sensitive in HTML and XML markup must be escaped. " +
            "Note: setting this to false may open up security issues. " +
            "See <a href=\"https://www.owasp.org/index.php/XSS_(Cross_Site_Scripting)_Prevention_Cheat_Sheet\">XSS (Cross Site Scripting) Prevention Cheat Sheet.</a>",
            defaultValue = "true")
    private boolean escape;

    @Property(tlddoc = "Code describing the language used in the generated markup for this component.")
    private String lang;

    @Property(tlddoc = "Advisory title information about markup elements generated for this component.")
    private String title;

    @Property(tlddoc = "Effect to run to show the message when there was no previous message. One of \"blind\", \"bounce\", \"clip\", \"drop\", \"explode\", \"fade\", \"fold\", \"highlight\", \"puff\", \"pulsate\", \"scale\", \"shake\", \"size\", \"slide\". Default is no effect.")
    private String initEffect;

    @Property(tlddoc = "Duration (also called \"speed\" in jQuery) of init effect. One of \"slow\", \"_default\", \"fast\", or an integer of milliseconds. Default is \"_default\". (\"slow\", \"_default\", \"fast\" currently equated to 600, 400 and 200 respectively in jQuery.)")
    private String initEffectDuration;

    @Property(tlddoc = "Effect to run to show the message when there was a different previous message. Values and default same as for initEffect.")
    private String changeEffect;

    @Property(tlddoc = "Duration (also called \"speed\" in jQuery) of change effect. Values and default same as for initEffectDuration.")
    private String changeEffectDuration;
}
