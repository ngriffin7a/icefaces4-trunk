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

package org.icefaces.ace.component.clientValidator;

import org.icefaces.ace.component.message.Message;
import org.icefaces.ace.renderkit.CoreRenderer;
import org.icefaces.ace.util.ComponentUtils;
import org.icefaces.impl.event.UIOutputWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;

public class PatternValidator extends PatternValidatorBase {

    public void encodeBegin(FacesContext context) throws IOException {
        final UIComponent validatedComponent = getParent();
        if (validatedComponent instanceof Validateable) {
            final UIComponent form = ComponentUtils.findParentForm(context, validatedComponent);
            final List<UIComponent> children = form.getChildren();
            final Validateable v = (Validateable) validatedComponent;
            final String id = v.getValidatedElementId();
            final String messageClientId = (String) validatedComponent.getAttributes().get(Message.class.getName());
            final ResourceBundle bundle = CoreRenderer.getComponentResourceBundle(FacesContext.getCurrentInstance(), "org.icefaces.ace.resources.messages");
            final String message = CoreRenderer.getLocalisedMessageFromBundle(bundle,
                    "org.icefaces.ace.component.patternvalidator.", "message", "Invalid format.");

            final StringBuffer script = new StringBuffer();
            script.append("ice.ace.jq('");
            script.append(ComponentUtils.idTojQuerySelector(form.getClientId()));
            script.append("').validate().settings.showErrors = function(){}; ice.ace.jq('");
            script.append(ComponentUtils.idTojQuerySelector(id));
            script.append("').rules('add', {pattern: /");
            script.append(getPattern());
            script.append("/, messages: {pattern: ice.ace.clientValidationMessageFor('");
            script.append(messageClientId);
            script.append("_msg', '");
            script.append(message);
            script.append("')");
            script.append("}})");

            children.add(new ScriptOutputWriter(script.toString()));
        }
    }

    public void encodeEnd(FacesContext context) throws IOException {
    }
}
