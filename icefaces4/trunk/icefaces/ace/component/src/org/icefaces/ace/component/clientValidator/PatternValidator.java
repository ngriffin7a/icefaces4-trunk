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
import org.icefaces.util.JavaScriptRunner;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.ResourceBundle;

public class PatternValidator extends PatternValidatorBase {

    public void encodeBegin(FacesContext context) throws IOException {
        UIComponent validatedComponent = getParent();
        if (validatedComponent instanceof Validateable) {
            Validateable v = (Validateable) validatedComponent;
            String id = v.getValidatedElementId();
            String messageClientId = (String) validatedComponent.getAttributes().get(Message.class.getName());
            UIComponent form = ComponentUtils.findParentForm(context, validatedComponent);
            ResourceBundle bundle = CoreRenderer.getComponentResourceBundle(FacesContext.getCurrentInstance(), "org.icefaces.ace.resources.messages");
            String message = CoreRenderer.getLocalisedMessageFromBundle(bundle,
                    "org.icefaces.ace.component.patternvalidator.", "message", "Invalid format.");

            StringBuffer script = new StringBuffer();
            script.append("ice.ace.jq('");
            script.append(ComponentUtils.idTojQuerySelector(form.getClientId()));
            script.append("').validate(); ice.ace.jq('");
            script.append(ComponentUtils.idTojQuerySelector(id));
            script.append("').rules('add', {pattern: /");
            script.append(getPattern());
            script.append("/, messages: {pattern: ice.ace.clientValidationMessageFor('");
            script.append(messageClientId);
            script.append("_msg', '");
            script.append(message);
            script.append("')");
            script.append("}})");

            JavaScriptRunner.runScript(context, script.toString());
        }
    }

    public void encodeEnd(FacesContext context) throws IOException {
    }
}
