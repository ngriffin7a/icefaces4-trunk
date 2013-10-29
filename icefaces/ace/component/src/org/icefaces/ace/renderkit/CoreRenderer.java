/*
 * Original Code Copyright Prime Technology.
 * Subsequent Code Modifications Copyright 2011-2012 ICEsoft Technologies Canada Corp. (c)
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

import org.icefaces.ace.util.Constants;
import org.icefaces.ace.util.HTML;
import org.icefaces.util.EnvUtils;

import javax.faces.application.ProjectStage;
import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.component.behavior.AjaxBehavior;
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

import org.icefaces.ace.util.JSONBuilder;

public class CoreRenderer extends Renderer {

    public static String resolveWidgetVar(UIComponent component) {
		FacesContext context = FacesContext.getCurrentInstance();
		String userWidgetVar = (String)component.getAttributes().get("widgetVar");

		if (userWidgetVar != null) return userWidgetVar;
		 else return "widget_" + component.getClientId(context).replaceAll("-|" + UINamingContainer.getSeparatorChar(context), "_");
	}

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
	
	protected String getActionURL(FacesContext facesContext) {
		String actionURL = facesContext.getApplication().getViewHandler().getActionURL(facesContext, facesContext.getViewRoot().getViewId());
		
		return facesContext.getExternalContext().encodeActionURL(actionURL);
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
    	
	public boolean isPostback(FacesContext facesContext) {
		return facesContext.getRenderKit().getResponseStateManager().isPostback(facesContext);
	}

    public boolean isAjaxRequest(FacesContext facesContext) {
		return facesContext.getPartialViewContext().isAjaxRequest();
	}

	protected void renderPassThruAttributes(FacesContext facesContext, UIComponent component, String var, String[] attrs) throws IOException {
		ResponseWriter writer = facesContext.getResponseWriter();
		
		for(String event : attrs) {			
			String eventHandler = (String) component.getAttributes().get(event);
			
			if(eventHandler != null)
				writer.write(var + ".addListener(\"" + event.substring(2, event.length()) + "\", function(e){" + eventHandler + ";});\n");
		}
	}
	
	protected void renderPassThruAttributes(FacesContext facesContext, UIComponent component, String[] attrs) throws IOException {
		ResponseWriter writer = facesContext.getResponseWriter();
		
		for(String attribute : attrs) {
			Object value = component.getAttributes().get(attribute);
			
			if(shouldRenderAttribute(value))
				writer.writeAttribute(attribute, value.toString(), attribute);
		}
	}
	
	protected void renderPassThruAttributes(FacesContext facesContext, UIComponent component, String[] attrs, String[] ignoredAttrs) throws IOException {
		ResponseWriter writer = facesContext.getResponseWriter();
		
		for(String attribute : attrs) {
			if(isIgnoredAttribute(attribute, ignoredAttrs)) {
				continue;
			}
			
			Object value = component.getAttributes().get(attribute);
			
			if(shouldRenderAttribute(value))
				writer.writeAttribute(attribute, value.toString(), attribute);
		}
	}
	
	private boolean isIgnoredAttribute(String attribute, String[] ignoredAttrs) {
		for(String ignoredAttribute : ignoredAttrs) {
			if(attribute.equals(ignoredAttribute)) {
				return true;
			}
		}
		
		return false;
	}
	
    protected boolean shouldRenderAttribute(Object value) {
        if(value == null)
            return false;
      
        if(value instanceof Boolean) {
            return ((Boolean) value).booleanValue();
        }
        else if(value instanceof Number) {
        	Number number = (Number) value;
        	
            if (value instanceof Integer)
                return number.intValue() != Integer.MIN_VALUE;
            else if (value instanceof Double)
                return number.doubleValue() != Double.MIN_VALUE;
            else if (value instanceof Long)
                return number.longValue() != Long.MIN_VALUE;
            else if (value instanceof Byte)
                return number.byteValue() != Byte.MIN_VALUE;
            else if (value instanceof Float)
                return number.floatValue() != Float.MIN_VALUE;
            else if (value instanceof Short)
                return number.shortValue() != Short.MIN_VALUE;
        }
        
        return true;
    }
    
    protected boolean isPostBack() {
    	FacesContext facesContext = FacesContext.getCurrentInstance();
    	return facesContext.getRenderKit().getResponseStateManager().isPostback(facesContext);
    }
   
    public String getEscapedClientId(String clientId){
    	return clientId.replaceAll(":", "\\\\\\\\:");
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

	protected String escapeText(String value) {
		return value == null ? "" : value.replaceAll("'", "\\\\'");
	}

    protected void encodeClientBehaviors(FacesContext context, ClientBehaviorHolder component, JSONBuilder jb) throws IOException {
        Map<String,List<ClientBehavior>> behaviorEvents = component.getClientBehaviors();

        if (behaviorEvents.isEmpty()) return;

        String clientId = ((UIComponent) component).getClientId(context);
        List<ClientBehaviorContext.Parameter> params = Collections.emptyList();

        jb.beginMap("behaviors");

        for(Iterator<String> eventIterator = behaviorEvents.keySet().iterator(); eventIterator.hasNext();) {
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
        else if(event.equalsIgnoreCase("action"))       //commands
            domEvent = "click";

        return domEvent;
    }

    protected boolean themeForms() {
        FacesContext context = FacesContext.getCurrentInstance();
        String value = context.getExternalContext().getInitParameter(Constants.THEME_FORMS_PARAM);

        return value == null ? true : Boolean.valueOf(value);
    }

    protected void addToAutoUpdate(String clientId) {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String,Object> viewMap = context.getViewRoot().getViewMap();
        Collection<String> autoUpdateIds = (Collection<String>) viewMap.get(Constants.AUTO_UPDATE);

        if(autoUpdateIds == null) {
            autoUpdateIds = new HashSet<String>();
            autoUpdateIds.add(clientId);
        }

        viewMap.put(Constants.AUTO_UPDATE, autoUpdateIds);
    }
	
    protected void decodeBehaviors(FacesContext context, UIComponent component)  {
		decodeBehaviors(context, component, null);
	}

    protected void decodeBehaviors(FacesContext context, UIComponent component, String proxyClientId)  {
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

           if(behaviorSource != null && behaviorSource.equals(clientId)) {
               for (ClientBehavior behavior: behaviorsForEvent) {
                   behavior.decode(context, component);
               }
           }
        }
    }

	/* ------------------------------- */
	/* --- imported from icemobile --- */
	/* ------------------------------- */
	
    /**
      * Non-obstrusive way to apply client behaviors.  Brought over from implementation of ace components for ace ajax.
      * will be replaced in 1.4 Beta to reflect support for both mobi:transition and mobi:ajax behaviors
      * Behaviors are rendered as options to the client side widget and applied by widget to necessary dom element
      */
    protected StringBuilder encodeClientBehaviors(FacesContext context, ClientBehaviorHolder component, String eventDef) throws IOException {
       StringBuilder sb = new StringBuilder(255);
         //ClientBehaviors
       Map<String,List<ClientBehavior>> eventBehaviors = component.getClientBehaviors();
       if(!eventBehaviors.isEmpty()) {
           String clientId = ((UIComponent) component).getClientId(context);
           List<ClientBehaviorContext.Parameter> params = Collections.emptyList();

           sb.append(",behaviors:{");

           for(Iterator<String> eventIterator = eventBehaviors.keySet().iterator(); eventIterator.hasNext();) {
               String event = eventIterator.next();
               if (null==event){
                   event = eventDef;
               }
               String domEvent = getDomEvent(event);
               sb.append(domEvent + ":");
               sb.append("function() {");
               ClientBehaviorContext cbContext = ClientBehaviorContext.createClientBehaviorContext(context, (UIComponent) component, event, clientId, params);
               for(Iterator<ClientBehavior> behaviorIter = eventBehaviors.get(event).iterator(); behaviorIter.hasNext();) {
                   ClientBehavior behavior = behaviorIter.next();
                   String script = behavior.getScript(cbContext);
                   if(script != null) {
                       sb.append(script);
                   }
               }
               sb.append("}");
               if(eventIterator.hasNext()) {
                   sb.append(",");
               }
           }
           sb.append("}");
       }
       return sb;
    }
	
    protected void writeJavascriptFile(FacesContext facesContext,
            UIComponent component, String JS_NAME, String JS_MIN_NAME,
            String JS_LIBRARY, String JS2_NAME, String JS2_MIN_NAME, String JS2_LIB) throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = component.getClientId(facesContext);
        writer.startElement(HTML.SPAN_ELEM, component);
        writer.writeAttribute(HTML.CLASS_ATTR, "mobi-hidden", null);
        writer.writeAttribute(HTML.ID_ATTR, clientId+"_libJS", HTML.ID_ATTR);
        if (!isScriptLoaded(facesContext, JS_NAME)) {
            String jsFname = JS_NAME;
            if (facesContext.isProjectStage(ProjectStage.Production)){
                jsFname = JS_MIN_NAME;
            }
            //set jsFname to min if development stage
            Resource jsFile = facesContext.getApplication().getResourceHandler().createResource(jsFname, JS_LIBRARY);
            String src = jsFile.getRequestPath();
            writer.startElement("script", component);
            writer.writeAttribute("type", "text/javascript", null);
            writer.writeAttribute("src", src, null);
            writer.endElement("script");
            setScriptLoaded(facesContext, JS_NAME);
        }
        if (!isScriptLoaded(facesContext, JS2_NAME)) {
            String jsFname = JS2_NAME;
            if (facesContext.isProjectStage(ProjectStage.Production)){
                jsFname = JS2_MIN_NAME;
            }
            //set jsFname to min if development stage
            Resource jsFile = facesContext.getApplication().getResourceHandler().createResource(jsFname, JS2_LIB);
            String src = jsFile.getRequestPath();
            writer.startElement("script", component);
            writer.writeAttribute("type", "text/javascript", null);
            writer.writeAttribute("src", src, null);
            writer.endElement("script");
            setScriptLoaded(facesContext, JS2_NAME);
        }
        writer.endElement(HTML.SPAN_ELEM);
    }

    protected void writeJavascriptFile(FacesContext facesContext, 
            UIComponent component, String JS_NAME, String JS_MIN_NAME, 
            String JS_LIBRARY) throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = component.getClientId(facesContext);
        writer.startElement(HTML.SPAN_ELEM, component);
        writer.writeAttribute(HTML.ID_ATTR, clientId+"_libJS", HTML.ID_ATTR);
        writer.writeAttribute(HTML.CLASS_ATTR, "mobi-hidden", null);
        if (!isScriptLoaded(facesContext, JS_NAME)) {
            String jsFname = JS_NAME;
            if (facesContext.isProjectStage(ProjectStage.Production)){
                jsFname = JS_MIN_NAME;
            }
            //set jsFname to min if development stage
            Resource jsFile = facesContext.getApplication().getResourceHandler().createResource(jsFname, JS_LIBRARY);
            String src = jsFile.getRequestPath();
            writer.startElement("script", component);
            writer.writeAttribute("type", "text/javascript", null);
            writer.writeAttribute("src", src, null);
            writer.endElement("script");
            setScriptLoaded(facesContext, JS_NAME);
        } 
        writer.endElement(HTML.SPAN_ELEM);
    }

    protected void setScriptLoaded(FacesContext facesContext, 
            String JS_NAME) {
        InlineScriptEventListener.setScriptLoaded(facesContext, JS_NAME);
    }

    protected boolean isScriptLoaded(FacesContext facesContext, String JS_NAME) {
        return InlineScriptEventListener.isScriptLoaded(facesContext, JS_NAME);
    }
	
    /**
     * this method created for mobi:inputText
     * @param context
     * @param component
     * @param inEvent
     * @return
     */
    protected String buildAjaxRequest(FacesContext context, ClientBehaviorHolder component, String inEvent) {
        Map<String,List<ClientBehavior>> behaviorEvents = component.getClientBehaviors();
        if (behaviorEvents.isEmpty()){
            return null;
        }

        String clientId = ((UIComponent) component).getClientId(context);

        StringBuilder req = new StringBuilder();

        List<ClientBehaviorContext.Parameter> params = Collections.emptyList();

        for(Iterator<String> eventIterator = behaviorEvents.keySet().iterator(); eventIterator.hasNext();) {
            String event = eventIterator.next();
       //     logger.info("eventIterator returning="+event);
            String domEvent = event;
            if (null != inEvent) {
                domEvent = inEvent;
   //             logger.info("passed in event="+event);
            }
            domEvent = getDomEvent(event);
      //      logger.info("getDomEvent returns event="+domEvent);
            if (behaviorEvents.get(event)==null){
                //logger.warning(" NO behavior for event="+event+" component="+((UIComponent) component).getClientId());
                return null;
            }  //don't do anything with domEvent yet as have to use the one the behavior is registered with.
       //     logger.info("before interation event="+event);
            for(Iterator<ClientBehavior> behaviorIter = behaviorEvents.get(event).iterator(); behaviorIter.hasNext();) {
                ClientBehavior behavior = behaviorIter.next();
                ClientBehaviorContext cbc = ClientBehaviorContext.createClientBehaviorContext(context, (UIComponent) component, event, clientId, params);
                String script = behavior.getScript(cbc);    //could be null if disabled
                if(script != null) {
                    req.append(script);
                }
            }
            if(eventIterator.hasNext()) {
                req.append(",");
            }
        }
        return req.toString();
    }


    /**
     * Not all text for components are easy to make attributes for components,
     * especially those for accessibility.  Default values are made available
     * within the ace jar messages properties files.  Users may override the
     * key if they prefer their own, or their locale is not available.
     * @return reference to resource bundle to get localised text for rendering
     */
    public ResourceBundle getComponentResourceBundle(FacesContext context, String ACE_MESSAGES_BUNDLE){
        Locale locale = context.getViewRoot().getLocale();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String bundleName = context.getApplication().getMessageBundle();
        if (classLoader == null) {
            classLoader = bundleName.getClass().getClassLoader();
        }
        if (bundleName == null) {
            bundleName = ACE_MESSAGES_BUNDLE;
        }
        ResourceBundle bundle = ResourceBundle.getBundle(bundleName, locale, classLoader);
        return ResourceBundle.getBundle(bundleName, locale, classLoader);
    }

    public static String getLocalisedMessageFromBundle(ResourceBundle bundle,
                                                       String MESSAGE_KEY_PREFIX,
                                                       String key){
        String label = bundle.getString(MESSAGE_KEY_PREFIX + key);
        if (null == label){
            label=" ";//at least return the empty string so no NPE
        }
        return label;
    }


}
