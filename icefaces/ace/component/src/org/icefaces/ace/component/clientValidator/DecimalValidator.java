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

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;

public class DecimalValidator extends DecimalValidatorBase {

    public void encodeBegin(FacesContext context) throws IOException {
        UIComponent validatedComponent = getParent();
        if (validatedComponent instanceof Validateable) {
            final Validateable v = (Validateable) validatedComponent;
            final String id = v.getValidatedElementId();
            final String messageClientId = (String) validatedComponent.getAttributes().get(Message.class.getName());
            final UIComponent form = ComponentUtils.findParentForm(context, validatedComponent);
            final List<UIComponent> children = form.getChildren();
            final ResourceBundle bundle = CoreRenderer.getComponentResourceBundle(FacesContext.getCurrentInstance(), "org.icefaces.ace.resources.messages");
            final String message = CoreRenderer.getLocalisedMessageFromBundle(bundle,
                    "org.icefaces.ace.component.clientvalidation.", "decimal", "Decimal value is required.");

            final StringBuffer script = new StringBuffer();
            script.append("ice.ace.setupClientValidation('");
            script.append(id);
            script.append("', '");
            script.append("number");
            script.append("', ");
            script.append(isEnabled());
            script.append(", '");
            script.append(messageClientId);
            script.append("', '");
            script.append(message);
            script.append("')");

            children.add(new ScriptOutputWriter(script.toString()));
        }
    }

    public void encodeEnd(FacesContext context) throws IOException {
    }
}
