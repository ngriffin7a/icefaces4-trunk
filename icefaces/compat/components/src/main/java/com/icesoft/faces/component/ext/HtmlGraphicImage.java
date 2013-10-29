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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URI;
import java.util.Date;

import com.icesoft.faces.component.CSS_DEFAULT;
import com.icesoft.faces.component.ext.taglib.Util;
import com.icesoft.faces.context.Resource;
import com.icesoft.faces.context.ResourceRegistry;
import com.icesoft.faces.context.ResourceRegistryLocator;
import com.icesoft.faces.context.effects.CurrentStyle;
import com.icesoft.faces.context.effects.Effect;
import com.icesoft.faces.context.effects.JavascriptContext;
import com.icesoft.util.pooling.ClientIdPool;
import org.icefaces.resources.BrowserType;
import org.icefaces.resources.ICEResourceDependencies;
import org.icefaces.resources.ICEResourceDependency;

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

/**
 * This is an extension of javax.faces.component.html.HtmlGraphicImage, which
 * provides some additional behavior to this component such as: <ul> <li>changes
 * the component's rendered state based on the authentication</li> <li>adds
 * effects to the component</li> <ul>
 */
@ICEResourceDependencies({
	@ICEResourceDependency(name="icefaces-compat.js", library="ice.compat",target="head", browser=BrowserType.ALL, browserOverride={}),
	@ICEResourceDependency(name="compat.js", library="ice.compat",target="head", browser=BrowserType.ALL, browserOverride={})
})
public class HtmlGraphicImage
        extends javax.faces.component.html.HtmlGraphicImage {
    public static final String COMPONENT_TYPE =
            "com.icesoft.faces.HtmlGraphicImage";
    public static final String RENDERER_TYPE = "com.icesoft.faces.Image";

    private static final boolean DEFAULT_VISIBLE = true;
    private String renderedOnUserRole = null;
    private Effect effect;
    private Boolean visible = null;
    private String styleClass = null;
    private Effect onclickeffect;
    private Effect ondblclickeffect;
    private Effect onmousedowneffect;
    private Effect onmouseupeffect;
    private Effect onmousemoveeffect;
    private Effect onmouseovereffect;
    private Effect onmouseouteffect;
    private Effect onkeypresseffect;
    private Effect onkeydowneffect;
    private Effect onkeyupeffect;
    private CurrentStyle currentStyle;
    private String mimeType = null;

    public HtmlGraphicImage() {
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
	
    public void encodeBegin(FacesContext context) throws IOException {
        Object value = getValue();
        if (value instanceof byte[]) {
            getImageByteArrayResource(context).setContent((byte[]) value);
        }
        super.encodeBegin(context);
    }

    /**
     * <p>Set the value of the <code>effect</code> property.</p>
     */
    public void setEffect(Effect effect) {
        JavascriptContext
                .includeLib(JavascriptContext.ICE_EXTRAS, getFacesContext());
        this.effect = effect;
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
     * <p>Return the value of the <code>onclickeffect</code> property.</p>
     */
    public Effect getOnclickeffect() {
        if (onclickeffect != null) {
            return onclickeffect;
        }
        ValueBinding vb = getValueBinding("onclickeffect");

        return vb != null ? (Effect) vb.getValue(getFacesContext()) : null;
    }

    /**
     * <p>Set the value of the <code>onclickeffect</code> property.</p>
     */
    public void setOnclickeffect(Effect onclickeffect) {
        JavascriptContext
                .includeLib(JavascriptContext.ICE_EXTRAS, getFacesContext());
        this.onclickeffect = onclickeffect;
    }

    /**
     * <p>Return the value of the <code>ondblclickeffect</code> property.</p>
     */
    public Effect getOndblclickeffect() {
        if (ondblclickeffect != null) {
            return ondblclickeffect;
        }
        ValueBinding vb = getValueBinding("ondblclickeffect");

        return vb != null ? (Effect) vb.getValue(getFacesContext()) : null;
    }

    /**
     * <p>Set the value of the <code>ondblclickeffect</code> property.</p>
     */
    public void setOndblclickeffect(Effect ondblclickeffect) {
        JavascriptContext
                .includeLib(JavascriptContext.ICE_EXTRAS, getFacesContext());
        this.ondblclickeffect = ondblclickeffect;
    }

    /**
     * <p>Return the value of the <code>onmousedowneffect</code> property.</p>
     */
    public Effect getOnmousedowneffect() {
        if (onmousedowneffect != null) {
            return onmousedowneffect;
        }
        ValueBinding vb = getValueBinding("onmousedowneffect");

        return vb != null ? (Effect) vb.getValue(getFacesContext()) : null;
    }

    /**
     * <p>Set the value of the <code>onmousedowneffect</code> property.</p>
     */
    public void setOnmousedowneffect(Effect onmousedowneffect) {
        JavascriptContext
                .includeLib(JavascriptContext.ICE_EXTRAS, getFacesContext());
        this.onmousedowneffect = onmousedowneffect;
    }

    /**
     * <p>Return the value of the <code>onmouseupeffect</code> property.</p>
     */
    public Effect getOnmouseupeffect() {
        if (onmouseupeffect != null) {
            return onmouseupeffect;
        }
        ValueBinding vb = getValueBinding("onmouseupeffect");

        return vb != null ? (Effect) vb.getValue(getFacesContext()) : null;
    }

    /**
     * <p>Set the value of the <code>onmouseupeffect</code> property.</p>
     */
    public void setOnmouseupeffect(Effect onmouseupeffect) {
        JavascriptContext
                .includeLib(JavascriptContext.ICE_EXTRAS, getFacesContext());
        this.onmouseupeffect = onmouseupeffect;
    }

    /**
     * <p>Return the value of the <code>onmousemoveeffect</code> property.</p>
     */
    public Effect getOnmousemoveeffect() {
        if (onmousemoveeffect != null) {
            return onmousemoveeffect;
        }
        ValueBinding vb = getValueBinding("onmousemoveeffect");

        return vb != null ? (Effect) vb.getValue(getFacesContext()) : null;
    }

    /**
     * <p>Set the value of the <code>onmousemoveeffect</code> property.</p>
     */
    public void setOnmousemoveeffect(Effect onmousemoveeffect) {
        JavascriptContext
                .includeLib(JavascriptContext.ICE_EXTRAS, getFacesContext());
        this.onmousemoveeffect = onmousemoveeffect;
    }

    /**
     * <p>Return the value of the <code>onmouseovereffect</code> property.</p>
     */
    public Effect getOnmouseovereffect() {
        if (onmouseovereffect != null) {
            return onmouseovereffect;
        }
        ValueBinding vb = getValueBinding("onmouseovereffect");

        return vb != null ? (Effect) vb.getValue(getFacesContext()) : null;
    }

    /**
     * <p>Set the value of the <code>onmouseovereffect</code> property.</p>
     */
    public void setOnmouseovereffect(Effect onmouseovereffect) {
        JavascriptContext
                .includeLib(JavascriptContext.ICE_EXTRAS, getFacesContext());
        this.onmouseovereffect = onmouseovereffect;
    }

    /**
     * <p>Return the value of the <code>onmouseouteffect</code> property.</p>
     */
    public Effect getOnmouseouteffect() {
        if (onmouseouteffect != null) {
            return onmouseouteffect;
        }
        ValueBinding vb = getValueBinding("onmouseouteffect");

        return vb != null ? (Effect) vb.getValue(getFacesContext()) : null;
    }

    /**
     * <p>Set the value of the <code>onmouseouteffect</code> property.</p>
     */
    public void setOnmouseouteffect(Effect onmouseouteffect) {
        JavascriptContext
                .includeLib(JavascriptContext.ICE_EXTRAS, getFacesContext());
        this.onmouseouteffect = onmouseouteffect;
    }

    /**
     * <p>Return the value of the <code>onkeypresseffect</code> property.</p>
     */
    public Effect getOnkeypresseffect() {
        if (onkeypresseffect != null) {
            return onkeypresseffect;
        }
        ValueBinding vb = getValueBinding("onkeypresseffect");

        return vb != null ? (Effect) vb.getValue(getFacesContext()) : null;
    }

    /**
     * <p>Set the value of the <code>onkeypresseffect</code> property.</p>
     */
    public void setOnkeypresseffect(Effect onkeypresseffect) {
        JavascriptContext
                .includeLib(JavascriptContext.ICE_EXTRAS, getFacesContext());
        this.onkeypresseffect = onkeypresseffect;
    }

    /**
     * <p>Return the value of the <code>onkeydowneffect</code> property.</p>
     */
    public Effect getOnkeydowneffect() {
        if (onkeydowneffect != null) {
            return onkeydowneffect;
        }
        ValueBinding vb = getValueBinding("onkeydowneffect");

        return vb != null ? (Effect) vb.getValue(getFacesContext()) : null;
    }

    /**
     * <p>Set the value of the <code>onkeydowneffect</code> property.</p>
     */
    public void setOnkeydowneffect(Effect onkeydowneffect) {
        JavascriptContext
                .includeLib(JavascriptContext.ICE_EXTRAS, getFacesContext());
        this.onkeydowneffect = onkeydowneffect;
    }

    /**
     * <p>Return the value of the <code>onkeyupeffect</code> property.</p>
     */
    public Effect getOnkeyupeffect() {
        if (onkeyupeffect != null) {
            return onkeyupeffect;
        }
        ValueBinding vb = getValueBinding("onkeyupeffect");

        return vb != null ? (Effect) vb.getValue(getFacesContext()) : null;
    }

    /**
     * <p>Set the value of the <code>onkeyupeffect</code> property.</p>
     */
    public void setOnkeyupeffect(Effect onkeyupeffect) {
        JavascriptContext
                .includeLib(JavascriptContext.ICE_EXTRAS, getFacesContext());
        this.onkeyupeffect = onkeyupeffect;
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
                CSS_DEFAULT.GRAPHIC_IMAGE_STYLE_CLASS,
                "styleClass");
    }
    
    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }
    
    public String getMimeType() {
        return mimeType;
    }

    /**
     * <p>Gets the state of the instance as a <code>Serializable</code>
     * Object.</p>
     */
    public Object saveState(FacesContext context) {
        Object values[] = new Object[21];
        values[0] = super.saveState(context);
        values[1] = renderedOnUserRole;
        values[2] = effect;
        values[3] = onclickeffect;
        values[4] = onclickeffect;
        values[5] = ondblclickeffect;
        values[6] = onmousedowneffect;
        values[7] = onmouseupeffect;
        values[8] = onmousemoveeffect;
        values[9] = onmouseovereffect;
        values[10] = onmouseouteffect;
        values[14] = onkeypresseffect;
        values[15] = onkeydowneffect;
        values[16] = onkeyupeffect;
        values[17] = currentStyle;
        values[18] = visible;
        values[19] = mimeType;
        values[20] = styleClass;
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
        onclickeffect = (Effect) values[4];
        ondblclickeffect = (Effect) values[5];
        onmousedowneffect = (Effect) values[6];
        onmouseupeffect = (Effect) values[7];
        onmousemoveeffect = (Effect) values[8];
        onmouseovereffect = (Effect) values[9];
        onmouseouteffect = (Effect) values[10];
        onkeypresseffect = (Effect) values[14];
        onkeydowneffect = (Effect) values[15];
        onkeyupeffect = (Effect) values[16];
        currentStyle = (CurrentStyle) values[17];
        visible = (Boolean)values[18];
        mimeType = (String)values[19];
        styleClass = (String)values[20];
    }
    public String getByteArrayImagePath(FacesContext context) {
        return getImageByteArrayResource(context).getPath();
    }
    
    private ImageByteArrayResource getImageByteArrayResource(FacesContext context) {
        if(!getAttributes().containsKey(ClientIdPool.get(getClientId(context)+"ImgBytArrRes"))) {
            ImageByteArrayResource imageByteArrayResource = new ImageByteArrayResource(context,this);
            getAttributes().put(ClientIdPool.get(getClientId(context)+"ImgBytArrRes"), imageByteArrayResource);
            
        }
        return (ImageByteArrayResource)getAttributes().get(ClientIdPool.get(getClientId(context)+"ImgBytArrRes"));
    }
}

class ImageByteArrayResource implements Resource, Serializable {
    private Date lastModified;
    private byte[] content;
    private String mimetype;
    private URI uri;

    public ImageByteArrayResource(FacesContext context, HtmlGraphicImage component) {
        this.lastModified =  new Date(System.currentTimeMillis());
        this.mimetype = component.getMimeType();
		ResourceRegistry registry = ResourceRegistryLocator.locate(context);
        uri = registry.registerResource(this);
    }
    
    public String calculateDigest() {
        return String.valueOf(this.hashCode());
    }
    
    public Date lastModified() {
        return lastModified;
    }
    
    public InputStream open() throws IOException {
          return new ByteArrayInputStream(content);
    }

    public void withOptions(Options options) throws IOException {
        Date now = new Date();
        options.setExpiresBy(now);
        options.setLastModified(now);
        options.setMimeType(mimetype);
    }
    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getPath() {
        return uri.getPath()+ "?"+ content.hashCode();
    }
}
