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
import org.icefaces.resources.BrowserType;
import org.icefaces.resources.ICEResourceDependencies;
import org.icefaces.resources.ICEResourceDependency;

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

@ICEResourceDependencies({
	@ICEResourceDependency(name="icefaces-compat.js", library="ice.compat",target="head", browser=BrowserType.ALL, browserOverride={}),
	@ICEResourceDependency(name="compat.js", library="ice.compat",target="head", browser=BrowserType.ALL, browserOverride={})
})
public class HtmlOutputFormat
        extends javax.faces.component.html.HtmlOutputFormat {


    public static final String COMPONENT_TYPE =
            "com.icesoft.faces.HtmlOutputFormat";
    public static final String COMPONENT_FAMILY = "javax.faces.Output";
    public static final String RENDERER_TYPE = "com.icesoft.faces.Format";
    private String styleClass = null;
    private String lang;
    private String dir;
    public HtmlOutputFormat() {
        super();
        setRendererType(RENDERER_TYPE);
    }

    public String getRendererType() {
        return RENDERER_TYPE;
    }

    public String getFamily() {
        return COMPONENT_FAMILY;
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
                CSS_DEFAULT.OUTPUT_FORMAT_DEFAULT_STYLE_CLASS,
                "styleClass"); 
    }

        
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

  /**
   * <p>Gets the state of the instance as a <code>Serializable</code>
   * Object.</p>
   */
  public Object saveState(FacesContext context) {
      Object values[] = new Object[4];
      values[0] = super.saveState(context);
      values[1] = styleClass;
      values[2] = dir;
      values[3] = lang;
      return ((Object) (values));
  }

  /**
   * <p>Perform any processing required to restore the state from the entries
   * in the state Object.</p>
   */
  public void restoreState(FacesContext context, Object state) {
      Object values[] = (Object[]) state;
      super.restoreState(context, values[0]);
      styleClass = (String) values[1];
      dir = (String) values[2];
      lang = (String) values[3];
  }
}
