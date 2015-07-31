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

package org.icefaces.mobi.component.sms;


import org.icefaces.ace.meta.annotation.ClientBehaviorHolder;
import org.icefaces.ace.meta.annotation.ClientEvent;
import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.annotation.Required;
import org.icefaces.ace.meta.annotation.Facet;
import org.icefaces.ace.meta.annotation.Facets;
import org.icefaces.ace.meta.baseMeta.UIComponentBaseMeta;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;

@Component(
    tagName = "sms",
    componentClass = "org.icefaces.mobi.component.sms.Sms",
    rendererClass = "org.icefaces.mobi.component.sms.SmsRenderer",
    generatedClass = "org.icefaces.mobi.component.sms.SmsBase",
    componentType = "org.icefaces.Sms",
    rendererType = "org.icefaces.SmsRenderer",
    extendsClass = "javax.faces.component.UIComponentBase",
    componentFamily = "org.icefaces.Sms",
    tlddoc = "The mobi:sms component renders a button that allows sending an SMS message, using the bridgeit app."
)
@ResourceDependencies({
        @ResourceDependency(library = "icefaces.mobi", name = "core/bridgeit.js"),
        @ResourceDependency(library = "org.icefaces.component.util", name = "component.js")
})
public class SmsMeta extends UIComponentBaseMeta {

    @Property( tlddoc = org.icefaces.mobi.util.TLDConstants.TABINDEX)
    private int tabindex;

    @Property(tlddoc = org.icefaces.mobi.util.TLDConstants.STYLE)
    private String style;

    @Property(tlddoc = org.icefaces.mobi.util.TLDConstants.STYLECLASS)
    private String styleClass;

    @Property(defaultValue = "false",
            tlddoc = org.icefaces.mobi.util.TLDConstants.DISABLED)
    private boolean disabled;

    @Property(defaultValue="SMS", tlddoc="The label to be displayed on the button.")
    private String buttonLabel;

    @Property(required=Required.yes, tlddoc="The phone number to send the message to.")
    private String number;

    @Property(required=Required.yes, tlddoc="The message to send.")
    private String message;

    @Property(required=Required.yes, tlddoc="The client-side id of the input or textarea element that contains the phone number to send the message to. It's also possible to specify a component id of ace:textEntry, ace:textAreaEntry, ace:autoCompleteEntry, ace:comboBox, and ace:maskedEntry components. This attribute takes precedence over the 'number' attribute. It's recommended to use this client-side approach.")
    private String numberInputId;

    @Property(required=Required.yes, tlddoc="The client-side id of the input or textarea element that contains the message to send. It's also possible to specify a component id of ace:textEntry, ace:textAreaEntry, ace:autoCompleteEntry, ace:comboBox, and ace:maskedEntry components. This attribute takes precedence over the 'message' attribute. It's recommended to use this client-side approach.")
    private String messageInputId;

    @Facets
    class FacetsMeta{
        @Facet(tlddoc = "Allows rendering of nested components that are displayed if the BridgeIt app cannot be used, either due to running on an unsupported platform (such as a desktop OS), or the BridgeIt app not being installed on the device.")
        UIComponent fallback;
    }
}
