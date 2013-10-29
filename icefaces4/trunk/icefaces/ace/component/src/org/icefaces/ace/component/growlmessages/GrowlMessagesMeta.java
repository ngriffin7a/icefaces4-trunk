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
package org.icefaces.ace.component.growlmessages;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Implementation;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.baseMeta.UIMessagesMeta;
import org.icefaces.ace.resources.ACEResourceNames;
import org.icefaces.resources.ICEResourceDependencies;
import org.icefaces.resources.ICEResourceDependency;
import org.icefaces.resources.ICEResourceLibrary;

@Component(
        tagName = "growlMessages",
        componentClass = "org.icefaces.ace.component.growlmessages.GrowlMessages",
        rendererClass = "org.icefaces.ace.component.growlmessages.GrowlMessagesRenderer",
        generatedClass = "org.icefaces.ace.component.growlmessages.GrowlMessagesBase",
        extendsClass = "javax.faces.component.UIMessages",
        componentType = "org.icefaces.ace.component.GrowlMessages",
        rendererType = "org.icefaces.ace.component.GrowlMessagesRenderer",
        componentFamily = "org.icefaces.ace.Messages",
        tlddoc = "Display Faces messages in a Growl container positioned on one of the four corners of the browser window.")
@ICEResourceLibrary(ACEResourceNames.ACE_LIBRARY)
@ICEResourceDependencies({
        @ICEResourceDependency(name = ACEResourceNames.COMPONENTS_JS)
})
public class GrowlMessagesMeta extends UIMessagesMeta {

    @Property(name = "for", implementation = Implementation.EXISTS_IN_SUPERCLASS,
            tlddoc = "Identifier of the component for which to render error " +
                    "messages. If this component is within the same NamingContainer " +
                    "as the target component, this must be the component " +
                    "identifier. Otherwise, it must be an absolute component " +
                    "identifier (starting with \":\")." +
                    " Leave out or use \"@all\" to output global messages.")
    private String forValue;

    @Property(tlddoc = "Flag indicating that characters that are sensitive in HTML and XML markup must be escaped. " +
            "Note: setting this to false may open up security issues. " +
            "See <a href=\"https://www.owasp.org/index.php/XSS_(Cross_Site_Scripting)_Prevention_Cheat_Sheet\">XSS (Cross Site Scripting) Prevention Cheat Sheet.</a>",
            defaultValue = "true")
    private boolean escape;

    @Property(tlddoc = "Limit the number of messages appearing at a given time to this number. Default is 0, no limit.", defaultValue = "0")
    private int maxVisibleMessages;

    @Property(tlddoc = "Header to prefix a message. Default = ''.", defaultValue = "")
    private String header;

    @Property(tlddoc = "A CSS class to be applied to each notification when they are created. Default = ''.", defaultValue = "")
    private String messageStyleClass;

    @Property(tlddoc = "Types of messages to auto hide after display: \"true\" (auto hide all messages), \"false\" (don't auto hide any message), \"info\", \"warn\", \"error\", or \"fatal\" (auto hide messages at or below specified severity.)", defaultValue = "true")
    private String autoHide;

    @Property(tlddoc = "Designates a class which is applied to the growl container and controls its position on the screen. Options: top-left, top-right, bottom-left, bottom-right.", defaultValue = "top-right")
    private String position;

    @Property(tlddoc = "Designates whether a notification should be appended to the container after all notifications, or whether it should be prepended to the container before all notifications. Options are \"after\" or \"before\".", defaultValue = "after")
    private String messageOrder;

    @Property(tlddoc = "The lifespan (milliseconds) of an auto-hide message on the screen.", defaultValue = "3000")
    private int displayDuration;

    @Property(tlddoc = "Whether or not the close-all button should be used when more than one notification appears on the screen. (Close visible messages only, not hidden messages.)", defaultValue = "true")
    private boolean closeAll;

    @Property(tlddoc = "Duration (also called \"speed\" in jQuery) of show effect. One of \"slow\", \"_default\", \"fast\", or an integer of milliseconds. (\"slow\", \"_default\", \"fast\" currently equated to 600, 400 and 200 respectively in jQuery.)", defaultValue = "_default")
    private String showEffectDuration;

    @Property(tlddoc = "Duration (also called \"speed\" in jQuery) of hide effect. Values and default same as for showEffectDuration.", defaultValue = "_default")
    private String hideEffectDuration;

/*
    @Property(tlddoc = "Effect to run to show a message. One of \"blind\", \"bounce\", \"clip\", \"drop\", \"explode\", \"fade\", \"fold\", \"highlight\", \"puff\", \"pulsate\", \"scale\", \"shake\", \"size\", \"slide\".", defaultValue = "fade")
    private String showEffect;

    @Property(tlddoc = "Effect to run to hide a message. Values and default same as for showEffect.", defaultValue = "fade")
    private String hideEffect;
*/
}
