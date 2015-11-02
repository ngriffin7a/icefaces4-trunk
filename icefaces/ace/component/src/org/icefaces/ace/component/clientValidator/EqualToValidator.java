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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.List;
import java.util.ResourceBundle;

public class EqualToValidator extends EqualToValidatorBase {

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
                    "org.icefaces.ace.component.clientvalidation.", "equalTo", "Input value not equal to '{0}''s value.");

            final UIComponent otherComponent = ComponentUtils.findComponent(context.getViewRoot(), getTo());
            if (otherComponent instanceof Validateable) {
                final String otherId = ((Validateable) otherComponent).getValidatedElementId();
                String label = "";
                try {
                    Method m = otherComponent.getClass().getMethod("getLabel");
                    label = (String) m.invoke(otherComponent);
                } catch (NoSuchMethodException e) {
                    label = "";
                } catch (InvocationTargetException e) {
                    label = "";
                } catch (IllegalAccessException e) {
                    label = "";
                }

                final StringBuffer script = new StringBuffer();
                script.append("ice.ace.setupClientValidation('");
                script.append(id);
                script.append("', 'equalTo', '");
                script.append(ComponentUtils.idTojQuerySelector(otherId));
                script.append("', '");
                script.append(messageClientId);
                script.append("', '");
                script.append(MessageFormat.format(message, label));
                script.append("')");

                children.add(new ScriptOutputWriter(script.toString()));
            }
        }
    }

    public void encodeEnd(FacesContext context) throws IOException {
    }
}
