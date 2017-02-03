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
package org.icefaces.ace.renderkit;

import org.icefaces.ace.util.ComponentUtils;
import org.icefaces.ace.util.Constants;
import org.icefaces.ace.util.HTML;
import org.icefaces.util.EnvUtils;

import javax.faces.application.ProjectStage;
import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIComponent;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;
import java.io.IOException;
import java.util.*;
import java.util.ResourceBundle;
import java.util.MissingResourceException;
import java.util.logging.Logger;
import java.util.logging.Level;

import org.icefaces.ace.util.JSONBuilder;

public class CoreRenderer extends Renderer {
    private static final Logger logger = Logger.getLogger(CoreRenderer.class.getName());

	protected void renderChildren(FacesContext facesContext, UIComponent component) throws IOException {
		for (Iterator<UIComponent> iterator = component.getChildren().iterator(); iterator.hasNext();) {
			UIComponent child = (UIComponent) iterator.next();
			renderChild(facesContext, child);
		}
	}
	
	protected void renderChild(FacesContext facesContext, UIComponent child) throws IOException {
		if (!child.isRendered()) {
			return;
		}

		child.encodeBegin(facesContext);

		if (child.getRendersChildren()) {
			child.encodeChildren(facesContext);
		} else {
			renderChildren(facesContext, child);
		}
		child.encodeEnd(facesContext);
	}
    
    protected String getResourceURL(FacesContext facesContext, String value) {
        if (value.contains(ResourceHandler.RESOURCE_IDENTIFIER)) {
            return value;
        } else {
            String url = facesContext.getApplication().getViewHandler().getResourceURL(facesContext, value);

            return facesContext.getExternalContext().encodeResourceURL(url);
        }
    }
	
	protected String getEncodedURL(FacesContext facesContext, String type, String baseUrl, Map parameters) {
		if (type != null) {
			type = type.toLowerCase();
			ExternalContext externalContext = facesContext.getExternalContext();
			ViewHandler viewHandler = facesContext.getApplication().getViewHandler();
			if (type.equals("action")) {
				String viewUrl = viewHandler.getActionURL(facesContext, baseUrl);
				return externalContext.encodeActionURL(viewUrl);
			} else if (type.equals("partialaction")) {
				String viewUrl = viewHandler.getActionURL(facesContext, baseUrl);
				return externalContext.encodePartialActionURL(viewUrl);
			} else if (type.equals("bookmarkable")) {
				String viewUrl = viewHandler.getBookmarkableURL(facesContext, baseUrl, parameters, false);
				return externalContext.encodeBookmarkableURL(viewUrl, parameters);
			} else if (type.equals("redirect")) {
				String viewUrl = viewHandler.getRedirectURL(facesContext, baseUrl, parameters, false);
				return externalContext.encodeRedirectURL(viewUrl, parameters);
			} else if (type.equals("resource")) {
				if (baseUrl.contains(ResourceHandler.RESOURCE_IDENTIFIER)) {
					return baseUrl;
				} else {
					String viewUrl = viewHandler.getResourceURL(facesContext, baseUrl);
					return externalContext.encodeResourceURL(viewUrl);
				}
			}
		}
		return baseUrl; // default, no encoding
	}

    protected String getResourceRequestPath(FacesContext facesContext, String resourceName) {
		Resource resource = facesContext.getApplication().getResourceHandler().createResource(resourceName, "icefaces.ace");

        return resource.getRequestPath();
	}

	protected void renderPassThruAttributes(FacesContext facesContext, UIComponent component, String[] attrs) throws IOException {
        ComponentUtils.renderPassThroughAttributes(facesContext.getResponseWriter(), component, attrs);
	}
	
    public String convertClientId(FacesContext context, String clientId) {
        boolean compressID = EnvUtils.isCompressIDs(context);
        if (!compressID)  {
            return clientId;
        }
        long extendedHash = clientId.hashCode();
        return Long.toString(extendedHash, 36);
    }

    public boolean isValueEmpty(String value) {
		if (value == null || "".equals(value))
			return true;

		return false;
	}

	public boolean isValueBlank(String value) {
		if(value == null)
			return true;

		return value.trim().equals("");
	}

    protected void encodeClientBehaviors(FacesContext context, ClientBehaviorHolder component, JSONBuilder jb) throws IOException {
        Map<String,List<ClientBehavior>> behaviorEvents = component.getClientBehaviors();

        if (behaviorEvents.isEmpty()) return;

        String clientId = ((UIComponent) component).getClientId(context);
        List<ClientBehaviorContext.Parameter> params = Collections.emptyList();

        jb.beginMap("behaviors");

		List<String> sortedBehaviourList = new ArrayList<String>(behaviorEvents.keySet());
		Collections.sort(sortedBehaviourList);

        for(Iterator<String> eventIterator = sortedBehaviourList.iterator(); eventIterator.hasNext();) {
            String event = eventIterator.next();
            String domEvent = getDomEvent(event);
            ClientBehaviorContext cbc = ClientBehaviorContext.createClientBehaviorContext(context, (UIComponent) component, event, clientId, params);
            List<ClientBehavior> cbList = behaviorEvents.get(event);
            Boolean writeArray = false;
            JSONBuilder cbJson = JSONBuilder.create();

            if (cbList.size() > 1) {
                writeArray = true;
                cbJson.beginArray();
            }

            for (ClientBehavior cb : cbList) {
                if (cb instanceof javax.faces.component.behavior.AjaxBehavior) continue; // ignore f:ajax

                String script = cb.getScript(cbc);    //could be null if disabled
                if (script != null) {
                    if (writeArray) cbJson.item(script, false); // item: false == no escape
                    else jb.entry(domEvent, script, true); // entry: true == no escape
                }
            }

            if (writeArray) {
                cbJson.endArray();
                jb.entry(domEvent, cbJson.toString(), true);
            }
        }

        jb.endMap();
    }

    private String getDomEvent(String event) {
        String domEvent = event;

        if (event.equalsIgnoreCase("valueChange"))       //editable value holders
            domEvent = "change";

        return domEvent;
    }
	
    protected boolean themeForms() {
        FacesContext context = FacesContext.getCurrentInstance();
        String value = context.getExternalContext().getInitParameter(Constants.THEME_FORMS_PARAM);

        return value == null ? true : Boolean.valueOf(value);
    }

	/* ------------------------------- */
	/* --- imported from icemobile --- */
	/* ------------------------------- */
	
    protected void decodeBehaviors(FacesContext context, UIComponent component)  {
		decodeBehaviors(context, component, null);
	}
	
    protected void decodeBehaviors(FacesContext context, UIComponent component, String proxyClientId) {
        if (!(component instanceof ClientBehaviorHolder))
            return;

        Map<String, List<ClientBehavior>> behaviors = ((ClientBehaviorHolder) component).getClientBehaviors();
        Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        String behaviorEvent = params.get("javax.faces.behavior.event");

        if (behaviors.isEmpty() || behaviorEvent == null)
            return;

        List<ClientBehavior> behaviorsForEvent = behaviors.get(behaviorEvent);

        if (behaviorsForEvent != null && !behaviorsForEvent.isEmpty()) {
            String behaviorSource = params.get("javax.faces.source");
            String clientId = proxyClientId == null ? component.getClientId() : proxyClientId;

            if (behaviorSource != null && behaviorSource.equals(clientId)) {
                for (ClientBehavior behavior : behaviorsForEvent) {
                    behavior.decode(context, component);
                }
            }
        }
    }


}
