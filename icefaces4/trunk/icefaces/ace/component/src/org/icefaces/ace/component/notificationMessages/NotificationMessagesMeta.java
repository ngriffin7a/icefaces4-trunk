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

package org.icefaces.ace.component.notificationMessages;

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
        tagName = "notificationMessages",
        componentClass = "org.icefaces.ace.component.notificationMessages.NotificationMessages",
        rendererClass = "org.icefaces.ace.component.notificationMessages.NotificationMessagesRenderer",
        generatedClass = "org.icefaces.ace.component.notificationMessages.NotificationMessagesBase",
        extendsClass = "javax.faces.component.UIMessage",
        componentType = "org.icefaces.ace.component.NotificationMessages",
        rendererType = "org.icefaces.ace.component.NotificationMessagesRenderer",
        componentFamily = "org.icefaces.ace.NotificationMessages",
        tlddoc = "The message tag use Notification API to render Faces messages."
)
@ICEResourceLibrary(ACEResourceNames.ACE_LIBRARY)
@ICEResourceDependencies({
        @ICEResourceDependency(name = "notificationMessages/notificationMessages.js"),
})
public class NotificationMessagesMeta extends UIMessageMeta {

    @Property(name = "for", implementation = Implementation.EXISTS_IN_SUPERCLASS, required = Required.yes)
    private String forValue;

    @Property(tlddoc = "Code describing the writing direction used in the generated markup for this component.", defaultValue = "auto")
    private String dir;

    @Property(tlddoc = "Code describing the language used in the generated markup for this component.", defaultValue = "")
    private String lang;

    @Property(tlddoc = "Header to prefix a message. Default = ''.", defaultValue = "")
    private String header;

    @Property(tlddoc = "The icon URL used for the notification message. Overrides the icon used to match the message severity.", defaultValue = "")
    private String iconURL;

    @Property(tlddoc = "Types of messages to auto hide after display: \"true\" (auto hide all messages), \"false\" (don't auto hide any message), \"info\", \"warn\", \"error\", or \"fatal\" (auto hide messages at or below specified severity.)", defaultValue = "true")
    private boolean autoHide;
}
