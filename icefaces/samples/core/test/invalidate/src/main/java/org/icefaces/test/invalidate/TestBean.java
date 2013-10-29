/*
 * Copyright 2004-2013 ICEsoft Technologies Canada Corp.
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

package org.icefaces.test.invalidate;

import org.icefaces.application.PushRenderer;

import javax.faces.bean.*;
import javax.faces.context.FacesContext;
import javax.faces.context.ExternalContext;
import javax.faces.event.ActionEvent;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.logging.Logger;

@ManagedBean(name = "bean")
//@RequestScoped
//@ViewScoped
@SessionScoped
//@CustomScoped(value="#{window}")
public class TestBean implements Serializable {

    private static final long serialVersionUID = -5395176310517774113L;
    private static Logger log = Logger.getLogger(TestBean.class.getName());

    private String message = "";
    private static String scope = "[unknown]";

    private boolean pushOn = false;

    static {
        Annotation[] anns = TestBean.class.getAnnotations();
        for (int index = 0; index < anns.length; index++) {
            Annotation ann = anns[index];
            if (ann instanceof RequestScoped) {
                scope = "Request";
            } else if (ann instanceof ViewScoped) {
                scope = "View";
            } else if (ann instanceof SessionScoped) {
                scope = "Session";
            } else if (ann instanceof CustomScoped) {
                scope = "Window";
            }
        }
    }

    public TestBean() {
        log.info("TestBean created: " + this);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getThisBean() {
        return this.toString();
    }

    public void setThisBean(String bean) {
    }

    public String getThisSession() {
        Object sessObj = FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        if( sessObj != null ){
            return sessObj.toString();
        }
        return "[no active session]";
    }

    public String getResponseWriter() {
        FacesContext fc = FacesContext.getCurrentInstance();
        return fc.getResponseWriter().toString();
    }

    public String getScope() {
        return scope;
    }

    public void setThisSession(String session) {
    }

    public void submit(ActionEvent event) {
        log.info("TestBean.submit: called for " + this);
    }

    public String invalidateStandard() {
        log.info("TestBean.invalidateStandard action: called for " + this);
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        ec.invalidateSession();
        return null;
    }

    public void invalidateStandard(ActionEvent event) {
        log.info("TestBean.invalidateStandard actionListener: called for " + this);
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        ec.invalidateSession();
    }

    public void activatePush(ActionEvent event) {
        if( !pushOn ){
            PushRenderer.addCurrentSession(this.getClass().getName());
            pushOn = true;
        }
        log.info("TestBean.togglePush: " + pushOn);
    }

    public boolean isPushOn() {
        return pushOn;
    }
}
