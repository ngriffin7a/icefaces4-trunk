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

package com.icesoft.faces.component.ext;

import com.icesoft.faces.component.CSS_DEFAULT;
import com.icesoft.faces.component.ext.taglib.Util;
import com.icesoft.faces.context.effects.CurrentStyle;
import com.icesoft.faces.context.effects.Effect;
import com.icesoft.faces.context.effects.JavascriptContext;
import org.icefaces.resources.BrowserType;
import org.icefaces.resources.ICEResourceDependencies;
import org.icefaces.resources.ICEResourceDependency;

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;


/**
 * This is an extension of javax.faces.component.html.HtmlMessages, which
 * provides some additional behavior to this component such as: <ul> <li>changes
 * the component's rendered state based on the authentication</li> <li>adds
 * effects to the component</li> <ul>
 */
@ICEResourceDependencies({
	@ICEResourceDependency(name="icefaces-compat.js", library="ice.compat",target="head", browser=BrowserType.ALL, browserOverride={}),
	@ICEResourceDependency(name="compat.js", library="ice.compat",target="head", browser=BrowserType.ALL, browserOverride={})
})
public class HtmlMessages extends javax.faces.component.html.HtmlMessages {
    public static final String COMPONENT_TYPE =
            "com.icesoft.faces.HtmlMessages";
    public static final String RENDERER_TYPE = "com.icesoft.faces.Messages";
    private static final boolean DEFAULT_VISIBLE = true;
	private static final boolean DEFAULT_ESCAPE = true;
    private String renderedOnUserRole = null;
    private Effect effect;
    private Boolean visible = null;
	private Boolean escape = null;
    private String errorClass = null;
    private String fatalClass = null;
    private String infoClass = null;
    private String warnClass = null;
    private String styleClass = null;
    private CurrentStyle currentStyle;

    public HtmlMessages() {
        super();
        setRendererType(RENDERER_TYPE);
    }

    public void setValueBinding(String s, ValueBinding vb) {
        if (s != null && s.indexOf("effect") != -1) {
            // If this is an effect attribute make sure Ice Extras is included
            JavascriptContext.includeLib(JavascriptContext.ICE_EXTRAS,
                                         getFacesContext());
        }
        super.setValueBinding(s, vb);
    }

    /**
     * <p>Set the value of the <code>effect</code> property.</p>
     */
    public void setEffect(Effect effect) {
        this.effect = effect;
        JavascriptContext
                .includeLib(JavascriptContext.ICE_EXTRAS, getFacesContext());
    }

    /**
     * <p>Return the value of the <code>effect</code> property.</p>
     */
    public Effect getEffect() {
        if (effect != null) {
            return effect;
        }
        ValueBinding vb = getValueBinding("effect");
        return vb != null ? (Effect) vb.getValue(getFacesContext()) : null;
    }

    /**
     * <p>Set the value of the <code>visible</code> property.</p>
     */
    public void setVisible(boolean visible) {
        this.visible = Boolean.valueOf(visible);
    }

    /**
     * <p>Return the value of the <code>visible</code> property.</p>
     */
    public boolean getVisible() {
        if (visible != null) {
            return visible.booleanValue();
        }
        ValueBinding vb = getValueBinding("visible");
        Boolean boolVal =
                vb != null ? (Boolean) vb.getValue(getFacesContext()) : null;
        return boolVal != null ? boolVal.booleanValue() : DEFAULT_VISIBLE;
    }
	
    /**
     * <p>Set the value of the <code>escape</code> property.</p>
     */
    public void setEscape(boolean escape) {
        this.escape = Boolean.valueOf(escape);
    }

    /**
     * <p>Return the value of the <code>escape</code> property.</p>
     */
    public boolean getEscape() {
        if (escape != null) {
            return escape.booleanValue();
        }
        ValueBinding vb = getValueBinding("escape");
        Boolean boolVal =
                vb != null ? (Boolean) vb.getValue(getFacesContext()) : null;
        return boolVal != null ? boolVal.booleanValue() : DEFAULT_ESCAPE;
    }

    /**
     * <p>Set the value of the <code>renderedOnUserRole</code> property.</p>
     */
    public void setRenderedOnUserRole(String renderedOnUserRole) {
        this.renderedOnUserRole = renderedOnUserRole;
    }

    /**
     * <p>Return the value of the <code>renderedOnUserRole</code> property.</p>
     */
    public String getRenderedOnUserRole() {
        if (renderedOnUserRole != null) {
            return renderedOnUserRole;
        }
        ValueBinding vb = getValueBinding("renderedOnUserRole");
        return vb != null ? (String) vb.getValue(getFacesContext()) : null;
    }

    /**
     * <p>Return the value of the <code>rendered</code> property.</p>
     */
    public boolean isRendered() {
        if (!Util.isRenderedOnUserRole(this)) {
            return false;
        }
        return super.isRendered();
    }

    /**
     * <p>Return the value of the <code>currentStyle</code> property.</p>
     */
    public CurrentStyle getCurrentStyle() {
        return currentStyle;
    }

    /**
     * <p>Set the value of the <code>currentStyle</code> property.</p>
     */
    public void setCurrentStyle(CurrentStyle currentStyle) {
        this.currentStyle = currentStyle;
    }

    /**
     * <p>Gets the state of the instance as a <code>Serializable</code>
     * Object.</p>
     */
    public Object saveState(FacesContext context) {
        Object values[] = new Object[13];
        values[0] = super.saveState(context);
        values[1] = renderedOnUserRole;
        values[2] = effect;
        values[3] = currentStyle;
        values[4] = visible;
        values[5] = dir;
        values[6] = lang;
        values[7] = errorClass ;
        values[8] = fatalClass ;
        values[9] = infoClass ;
        values[10] = warnClass ;
        values[11] = styleClass ;        
		values[12] = escape;
        return ((Object) (values));
    }

    /**
     * <p>Perform any processing required to restore the state from the entries
     * in the state Object.</p>
     */
    public void restoreState(FacesContext context, Object state) {
        Object values[] = (Object[]) state;
        super.restoreState(context, values[0]);
        renderedOnUserRole = (String) values[1];
        effect = (Effect) values[2];
        currentStyle = (CurrentStyle) values[3];
        visible = (Boolean) values[4];
        dir = (String)values[5];
        lang = (String)values[6];
        errorClass = (String)values[7];
        fatalClass = (String)values[8];
        infoClass = (String)values[9];
        warnClass = (String)values[10];
        styleClass = (String)values[11];         
		escape = (Boolean) values[12];
    }

    /**
     * <p>Set the value of the <code>styleClass</code> property.</p>
     */
    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    /**
     * <p>Return the value of the <code>styleClass</code> property.</p>
     */
    public String getStyleClass() {
        return Util.getQualifiedStyleClass(this, 
                styleClass,
                CSS_DEFAULT.MESSAGES_STYLE_CLASS,
                "styleClass");  
    }


    /**
     * <p>Set the value of the <code>errorClass</code> property.</p>
     */
    public void setErrorClass(String errorClass) {
        this.errorClass = errorClass;
    }

    /**
     * <p>Return the value of the <code>errorClass</code> property.</p>
     */
    public String getErrorClass() {
        return Util.getQualifiedStyleClass(this, 
                errorClass,
                CSS_DEFAULT.ERROR_STYLE_CLASS,
                "errorClass");  
    }


    /**
     * <p>Set the value of the <code>fataClass</code> property.</p>
     */
    public void setFatalClass(String fatalClass) {
        this.fatalClass = fatalClass;
    }

    /**
     * <p>Return the value of the <code>fataClass</code> property.</p>
     */
    public String getFatalClass() {
        return Util.getQualifiedStyleClass(this, 
                fatalClass,
                CSS_DEFAULT.FATAL_STYLE_CLASS,
                "fatalClass"); 
    }


    /**
     * <p>Set the value of the <code>infoClass</code> property.</p>
     */
    public void setInfoClass(String infoClass) {
        this.infoClass = infoClass;
    }

    /**
     * <p>Return the value of the <code>infoClass</code> property.</p>
     */
    public String getInfoClass() {
        return Util.getQualifiedStyleClass(this, 
                infoClass,
                CSS_DEFAULT.INFO_STYLE_CLASS,
                "infoClass"); 
    }

    /**
     * <p>Set the value of the <code>warnClass</code> property.</p>
     */
    public void setWarnClass(String warnClass) {
        this.warnClass = warnClass;
    }

    /**
     * <p>Return the value of the <code>warnClass</code> property.</p>
     */
    public String getWarnClass() {
        return Util.getQualifiedStyleClass(this, 
                warnClass,
                CSS_DEFAULT.WARN_STYLE_CLASS,
                "warnClass");  
    }
    
      private java.lang.String dir;

  /**
   * <p>Return the value of the <code>dir</code> property.  Contents:</p><p>
   * Direction indication for text that does not inherit directionality.
   *           Valid values are "LTR" (left-to-right) and "RTL" (right-to-left).
   * </p>
   */
  public java.lang.String getDir() {
    if (null != this.dir) {
      return this.dir;
    }
    ValueBinding _vb = getValueBinding("dir");
    if (_vb != null) {
      return (java.lang.String) _vb.getValue(getFacesContext());
    } else {
      return null;
    }
  }

  /**
   * <p>Set the value of the <code>dir</code> property.</p>
   */
  public void setDir(java.lang.String dir) {
    this.dir = dir;
  }
  
    private java.lang.String lang;

  /**
   * <p>Return the value of the <code>lang</code> property.  Contents:</p><p>
   * Code describing the language used in the generated markup
   *           for this component.
   * </p>
   */
  public java.lang.String getLang() {
    if (null != this.lang) {
      return this.lang;
    }
    ValueBinding _vb = getValueBinding("lang");
    if (_vb != null) {
      return (java.lang.String) _vb.getValue(getFacesContext());
    } else {
      return null;
    }
  }

  /**
   * <p>Set the value of the <code>lang</code> property.</p>
   */
  public void setLang(java.lang.String lang) {
    this.lang = lang;
  }

}
