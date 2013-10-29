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

package org.icefaces.application.showcase.view.bean.examples.component.inputRichText;

import java.util.HashMap;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.icefaces.application.showcase.view.bean.BaseBean;

/**
 * <p>The InputRichTextBean class stores the value of the inputRichText
 * component on the server.  When the save button is pressed on the RichText
 * component the backing bean value is updated.  The bean also contains the
 * two menubar states available; default and basic. </p>
 *
 * @since 1.7
 */
@ManagedBean(name = "inputRichTextBean")
@ViewScoped
public class InputRichTextBean extends BaseBean {

    public String getToolbarModeDefault() { return "Default"; }
    public String getToolbarModeBasic() { return "Basic"; }
    
    private String toolbarMode = getToolbarModeDefault();
    private Map skinMapping = new HashMap();
    
    public InputRichTextBean() {
    	skinMapping.put("rime", "kama");
    	skinMapping.put("xp", "v2");
    	skinMapping.put("royale", "office2003");    	
    }
    
    
    
    public Map getSkinMapping() {
		return skinMapping;
	}
	public void setSkinMapping(Map skinMapping) {
		this.skinMapping = skinMapping;
	}
	

	private String value = "";

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        // highlight backing bean display. 
        valueChangeEffect.setFired(false);
        this.value = value;
    }

    public String getToolbarMode() {
        return toolbarMode;
    }

    public void setToolbarMode(String toolbarMode) {
        this.toolbarMode = toolbarMode;
    }
}
