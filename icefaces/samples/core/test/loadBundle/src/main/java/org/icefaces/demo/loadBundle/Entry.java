/*
 * Copyright 2004-2012 ICEsoft Technologies Canada Corp.
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

package org.icefaces.demo.loadBundle;

import java.io.Serializable;
import java.util.Locale;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

@ManagedBean(name = "Entry")
@SessionScoped
public class Entry implements Serializable {
    private String varName = "A";
    private String basenameName = "org.icefaces.demo.loadBundle.A";

    public void toggleLanguage() {
        UIViewRoot viewRoot = FacesContext.getCurrentInstance().getViewRoot();
        if ("fr".equals(viewRoot.getLocale().getLanguage())) {
            viewRoot.setLocale(Locale.ENGLISH);
        } else {
            viewRoot.setLocale(Locale.FRENCH);
        }
    }

    public void toggleVarName() {
        if ("A".equals(varName)) {
            varName = "B";
        } else {
            varName = "A";
        }
    }

    public void toggleBasenameName() {
        if ("org.icefaces.demo.loadBundle.A".equals(basenameName)) {
            basenameName = "org.icefaces.demo.loadBundle.B";
        } else {
            basenameName = "org.icefaces.demo.loadBundle.A";
        }
    }

    public String getVarName() {
        return varName;
    }

    public String getBasenameName() {
        return basenameName;
    }

    public String getLanguage() {
        return FacesContext.getCurrentInstance().getViewRoot().getLocale().getLanguage();
    }
}
