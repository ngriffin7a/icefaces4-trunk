/*
 * Original Code Copyright Prime Technology.
 * Subsequent Code Modifications Copyright 2011-2014 ICEsoft Technologies Canada Corp. (c)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * NOTE THIS CODE HAS BEEN MODIFIED FROM ORIGINAL FORM
 *
 * Subsequent Code Modifications have been made and contributed by ICEsoft Technologies Canada Corp. (c).
 *
 * Code Modification 1: Integrated with ICEfaces Advanced Component Environment.
 * Contributors: ICEsoft Technologies Canada Corp. (c)
 *
 * Code Modification 2: [ADD BRIEF DESCRIPTION HERE]
 * Contributors: ______________________
 * Contributors: ______________________
 */

/*
 * Generated, Do Not Modify
 */

package org.icefaces.ace.component.submenu;

import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import java.util.Map;
import java.util.List;
import java.util.Collections;
import java.util.Iterator;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.component.behavior.ClientBehaviorHolder;
import org.icefaces.ace.component.ajax.AjaxBehavior;

public class Submenu extends SubmenuBase implements java.io.Serializable {

    public void decode(FacesContext facesContext) {
	
        Map<String, String> params = facesContext.getExternalContext().getRequestParameterMap();
        String clientId = getClientId(facesContext);
        String source = String.valueOf(params.get("ice.event.captured"));
  
        if (clientId.equals(source)) this.queueEvent(new ActionEvent(this));

        // decode bahaviors 
		Map<String, List<ClientBehavior>> behaviors = getClientBehaviors();
        if(behaviors.isEmpty()) {
            return;
        }
		
        String behaviorEvent = params.get("javax.faces.behavior.event");

        if(null != behaviorEvent) {
            List<ClientBehavior> behaviorsForEvent = behaviors.get(behaviorEvent);

            if(behaviors.size() > 0) {
               String behaviorSource = params.get("javax.faces.source");

               if(behaviorSource != null && behaviorSource.startsWith(clientId)) {
                   for (ClientBehavior behavior: behaviorsForEvent) {
                       behavior.decode(facesContext, this);
                   }
               }
            }
        }
    }

	public String getScript() {

		final FacesContext context = getFacesContext();
		String clientId = getClientId(context);
		boolean hasAjaxBehavior = false;

		StringBuilder submitWithBehavioursCommand = new StringBuilder();
		submitWithBehavioursCommand.append("ice.ace.Menubar.submitWithBehaviours(event, this, ["); // dynamically set the id to the node so that it can be handled by the submit functions
		// ClientBehaviors
		Map<String,List<ClientBehavior>> behaviorEvents = getClientBehaviors();
		if(!behaviorEvents.isEmpty()) {
			List<ClientBehaviorContext.Parameter> params = Collections.emptyList();
			for(Iterator<ClientBehavior> behaviorIter = behaviorEvents.get("action").iterator(); behaviorIter.hasNext();) {
				ClientBehavior behavior = behaviorIter.next();
				if (behavior instanceof AjaxBehavior)
					hasAjaxBehavior = true;
				ClientBehaviorContext cbc = ClientBehaviorContext.createClientBehaviorContext(context, this, "action", clientId, params);
				String script = behavior.getScript(cbc);    //could be null if disabled

				if(script != null) {
					submitWithBehavioursCommand.append(script);
					if (behaviorIter.hasNext()) {
						submitWithBehavioursCommand.append(",");
					}
				}
			}
		}
		submitWithBehavioursCommand.append("]); ");

		StringBuilder submitWithParametersCommand = new StringBuilder();
		if (!hasAjaxBehavior && (getActionExpression() != null || getActionListeners().length > 0)) {
			submitWithParametersCommand.append("ice.ace.Menubar.submitWithParameters(event, this, '" + clientId + "'");
			for(UIComponent child : getChildren()) {
				if(child instanceof UIParameter) {
					submitWithParametersCommand.append(", '");
					UIParameter param = (UIParameter) child;
					submitWithParametersCommand.append(param.getName());
					submitWithParametersCommand.append("','");
					submitWithParametersCommand.append(String.valueOf(param.getValue()));
					submitWithParametersCommand.append("'");
				}
			}
			submitWithParametersCommand.append(");");
		}

		StringBuilder command = hasAjaxBehavior ? submitWithBehavioursCommand : submitWithParametersCommand;
		String customOnclick = getOnclick();
		return customOnclick == null ? command.toString() : customOnclick + ";" + command.toString();
	}

    protected FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }
}