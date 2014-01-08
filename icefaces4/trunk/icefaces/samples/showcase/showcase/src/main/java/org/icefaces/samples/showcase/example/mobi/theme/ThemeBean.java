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

package org.icefaces.samples.showcase.example.mobi.theme;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;



import org.icefaces.mobi.util.CSSUtils;

import java.io.Serializable;

/**
 * Simple Model for keeping track of the current them.  The class also handles
 * actionListener used to change the theme in the UI.
 */
@ManagedBean
@SessionScoped
public class ThemeBean implements Serializable {

    // empty theme name will default to auto on the select menu
    private String currentTheme = "";
    private boolean simulator = false;

    public String getCurrentTheme() {
        return currentTheme;
    }

    public void setCurrentTheme(String currentTheme) {
        this.currentTheme = currentTheme;
    }
    
    public String getDerivedTheme(){
        return CSSUtils.deriveTheme(
                (HttpServletRequest)FacesContext.getCurrentInstance()
                .getExternalContext().getRequest()).fileName();
    }

    public boolean isSimulator() {
        return simulator;
    }

    public void setSimulator(boolean simulator) {
        this.simulator = simulator;
    }
}
