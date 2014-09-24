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

package org.icefaces.samples.showcase.example.core.loadBundle;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import java.io.Serializable;
import java.util.Locale;

@ManagedBean(name = LoadBundleViewScopeBean.BEAN_NAME)
@ViewScoped
public class LoadBundleViewScopeBean implements Serializable {
    public static final String BEAN_NAME = "loadBundleViewScopeBean";

    public LoadBundleViewScopeBean() { }

    private String language = "en";

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;

        UIViewRoot viewRoot = FacesContext.getCurrentInstance().getViewRoot();
        if ("en".equals(language)) {
            viewRoot.setLocale(Locale.ENGLISH);
        } else if ("fr".equals(language)) {
            viewRoot.setLocale(Locale.FRENCH);
        } else if ("it".equals(language)) {
            viewRoot.setLocale(Locale.ITALIAN);
        }
    }
}
