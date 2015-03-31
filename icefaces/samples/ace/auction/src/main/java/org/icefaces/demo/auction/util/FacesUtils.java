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

package org.icefaces.demo.auction.util;

import java.util.Map;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * JSF utilities.
 *
 * @author ICEsoft Technologies Inc.
 * @since 2.0
 */
public class FacesUtils {
	public static Cookie getCookie(String name) {
		Object plainRequest = FacesContext.getCurrentInstance().getExternalContext().getRequest();
		if ((plainRequest != null) && (plainRequest instanceof HttpServletRequest)) {
			Cookie[] cookieList = ((HttpServletRequest)plainRequest).getCookies();
			if ((cookieList != null) && (cookieList.length > 0)) {
				for (Cookie currentCookie : cookieList) {
					if (name.equals(currentCookie.getName())) {
						return currentCookie;
					}
				}
			}
		}
		
		return null;
	}
	
	public static void addCookie(String name, String value, String comment) {
		Object plainResponse = FacesContext.getCurrentInstance().getExternalContext().getResponse();
		if (plainResponse != null) {
			Cookie visitCookie = new Cookie(name, value);
			visitCookie.setComment(comment);
			visitCookie.setMaxAge(60 * 60 * 24 * 365); // Set for a year, since that's pretty standard
			visitCookie.setPath("/");
			
			((HttpServletResponse)plainResponse).addCookie(visitCookie);
		}
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
		return new FacesMessage(type, text, text);
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
    
    public static String getHttpSessionId() {
    	if (FacesContext.getCurrentInstance() != null) {
	    	Object session = FacesContext.getCurrentInstance().getExternalContext().getSession(false);
	    	if ((session != null) && (session instanceof HttpSession)) {
	    		return ((HttpSession)session).getId();
	    	}
    	}
    	
    	return null;
    }
}