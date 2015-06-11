/*
 * Copyright 2004-2015 ICEsoft Technologies Canada Corp.
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

package org.icefaces.demo.emporium.util;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class FacesUtils {
	private static final Logger log = Logger.getLogger(FacesUtils.class.getName());
	
	public static String getStringFromCookie(String name) {
		Cookie toCheck = getCookie(name);
		
		if (toCheck != null) {
			return toCheck.getValue();
		}
		return null;
	}
	
	public static Cookie getCookie(String name) {
		FacesContext fc = FacesContext.getCurrentInstance();
		
		if (fc != null) {
			ExternalContext ec = fc.getExternalContext();
			
			if (ec != null) {
				Object mapLookup = ec.getRequestCookieMap().get(name);
				
				if ((mapLookup != null) && (mapLookup instanceof Cookie)) {
					return (Cookie)mapLookup;
				}
			}
		}
		return null;
	}
	
	public static boolean getHasCookieStartingWith(String nameStartsWith) {
		FacesContext fc = FacesContext.getCurrentInstance();
		
		if (fc != null) {
			ExternalContext ec = fc.getExternalContext();
			
			if (ec != null) {
				nameStartsWith = nameStartsWith.toLowerCase(); // Do a case insensitive check
				
				Collection<Object> cookieList = ec.getRequestCookieMap().values();
				Cookie currentCookie = null;
				for (Object loopObj : cookieList) {
					if ((loopObj != null) && (loopObj instanceof Cookie)) {
						currentCookie = (Cookie)loopObj;
						if (currentCookie.getName().toLowerCase().startsWith(nameStartsWith)) {
							return true;
						}
					}
				}
			}
		}
		
		return false;
	}
	
	public static void deleteCookie(String name) {
		deleteCookie(name, null);
	}
	
	public static void deleteCookie(String name, String value) {
		// As per the cookie standard setting the age to 0 will cause it to be deleted
		Map<String,Object> properties = new HashMap<String,Object>(1);
		properties.put("maxAge", 0);
		
		addCookie(name, value, properties);
	}
	
	public static void addCookie(String name, Object value) {
		Map<String,Object> defaultProperties = new HashMap<String,Object>(1);
		defaultProperties.put("maxAge", 5184000); // Expire after 60 days
		defaultProperties.put("path", "/");
		addCookie(name, value, defaultProperties);
	}
	
	public static void addCookie(String name, Object value, Map<String,Object> properties) {
		FacesContext fc = FacesContext.getCurrentInstance();
		
		if (fc != null) {
			ExternalContext ec = fc.getExternalContext();
			
			if (ec != null) {
				String valueToAdd = (value != null) ? value.toString() : null;
				ec.addResponseCookie(name, valueToAdd, properties);
			}
		}
	}
	
	public static Object loadFromCookie(String cookieName, Object defaultVal) {
		String toReturn = getStringFromCookie(cookieName);
		
		if (toReturn != null) {
			return toReturn;
		}
		
		return defaultVal;
	}
	
    public static Object getManagedBean(String beanName) {
        FacesContext fc = FacesContext.getCurrentInstance();
        if (fc != null) {
	        ELContext elc = fc.getELContext();
	        if (elc != null) {
		        ExpressionFactory ef = fc.getApplication().getExpressionFactory();
		        if (ef != null) {
			        ValueExpression ve = ef.createValueExpression(elc, getJsfEl(beanName), Object.class);
			        if (ve != null) {
			        	return ve.getValue(elc);
			        }
		        }
	        }
        }
        
        log.log(Level.WARNING, "getManagedBean failed for [" + beanName + "] with null context.");
        
        return null;
    }
    
    private static String getJsfEl(String value) {
        return "#{" + value + "}";
    }
	
    public static String getFacesParameter(String parameter) {
        try{
            // Get the servlet context based on the faces context
            ServletContext sc = (ServletContext)FacesContext.getCurrentInstance().getExternalContext().getContext();
            
            // Return the value read from the parameter
            return sc.getInitParameter(parameter);
        }catch (Exception failedParam) {
        	failedParam.printStackTrace();
        }
        
        return null;
    }
    
    public static String getFacesParameter(String parameter, String defaultValue) {
    	String toReturn = getFacesParameter(parameter);
    	
    	return toReturn != null ? toReturn : defaultValue;
    }
    
    public static void destroySession() {
    	// Kill our session and refresh the page
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		refreshPage();
    }
    
    public static Map<String,String[]> getRequestParameterMap() {
    	Object requestObj = FacesContext.getCurrentInstance().getExternalContext().getRequest();
    	if ((requestObj != null) && (requestObj instanceof HttpServletRequest)) {
    		HttpServletRequest request = (HttpServletRequest)requestObj;
    		
    		return request.getParameterMap();
    	}
    	
    	return null;
    }
    
    public static boolean redirectContext(String uri) {
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect(uri);
			
			return true;
		}catch (Exception failedRedirect) {
			failedRedirect.printStackTrace();
		}
		
		return false;
    }
    
    public static boolean refreshPage() {
    	return redirectContext(".");
    }
    
	public static FacesMessage generateMessage(FacesMessage.Severity type, String text) {
		return new FacesMessage(type, TimestampUtil.stamp() + text, text);
	}
    
	public static void addGlobalInfoMessage(String infoText) {
		// Passing a null id means put this as a global message
		FacesContext.getCurrentInstance().addMessage(null, generateMessage(FacesMessage.SEVERITY_INFO, infoText));		
	}
	
	public static void addGlobalWarnMessage(String warnText) {
		// Passing a null id means put this as a global message
		FacesContext.getCurrentInstance().addMessage(null, generateMessage(FacesMessage.SEVERITY_WARN, warnText));		
	}
	
	public static void addGlobalErrorMessage(String errorText) {
		// Passing a null id means put this as a global message
		FacesContext.getCurrentInstance().addMessage(null, generateMessage(FacesMessage.SEVERITY_ERROR, errorText));
	}
	
	public static void addInfoMessage(String id, String infoText) {
		FacesContext.getCurrentInstance().addMessage(id, generateMessage(FacesMessage.SEVERITY_INFO, infoText));		
	}
	
	public static void addWarnMessage(String id, String warnText) {
		FacesContext.getCurrentInstance().addMessage(id, generateMessage(FacesMessage.SEVERITY_WARN, warnText));		
	}
	
	public static void addErrorMessage(String id, String errorText) {
		FacesContext.getCurrentInstance().addMessage(id, generateMessage(FacesMessage.SEVERITY_ERROR, errorText));
	}
	
	public static HttpSession getHttpSession() {
		if (FacesContext.getCurrentInstance() != null) {
	    	Object session = FacesContext.getCurrentInstance().getExternalContext().getSession(false);
	    	if ((session != null) && (session instanceof HttpSession)) {
	    		return (HttpSession)session;
	    	}
    	}
    	
    	return null;
	}
    
    public static String getHttpSessionId() {
    	HttpSession toCheck = getHttpSession();
    	if (toCheck != null) {
    		return toCheck.getId();
    	}
    	
    	return null;
    }
    
    public static File getWebDirectory() {
    	if (FacesContext.getCurrentInstance() != null) {
    		return new File(((ServletContext)FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/"));
    	}
    	
    	return new File("web");
    }
    
    public static File getResourcesDirectory() {
        return new File(getWebDirectory(), "resources");
    }
}