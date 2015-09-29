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

package org.icefaces.samples.showcase.example.ace.autocompleteentry;

import java.io.Serializable;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;

@ManagedBean(name= AutoCompleteEntryMatchBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class AutoCompleteEntryMatchBean implements Serializable
{
    public static final String BEAN_NAME = "autoCompleteEntryMatchBean";
	public String getBeanName() { return BEAN_NAME; }
    
    private String selectedText;
    private String filterMode = "startsWith";
    private boolean caseSensitive = false;
    
    public String getSelectedText() {
        return selectedText;
    }
    
    public String getFilterMode() {
        return filterMode;
    }
    
    public boolean getCaseSensitive() {
        return caseSensitive;
    }
    
    public void setSelectedText(String selectedText) {
        this.selectedText = selectedText;
    }
    
    public void setFilterMode(String filterMode) {
        this.filterMode = filterMode;
    }
    
    public void setCaseSensitive(boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
    }
}
