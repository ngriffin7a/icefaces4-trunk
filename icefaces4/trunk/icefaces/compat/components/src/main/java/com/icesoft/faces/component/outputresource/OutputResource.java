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

package com.icesoft.faces.component.outputresource;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.net.URLEncoder;

import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UIData;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.icesoft.faces.component.CSS_DEFAULT;
import com.icesoft.faces.component.ext.taglib.Util;
import com.icesoft.faces.context.FileResource;
import com.icesoft.faces.context.Resource;
import com.icesoft.faces.context.ResourceRegistry;
import com.icesoft.faces.context.ResourceRegistryLocator;

import com.icesoft.faces.component.tree.Tree;
import org.icefaces.resources.BrowserType;
import org.icefaces.resources.ICEResourceDependencies;
import org.icefaces.resources.ICEResourceDependency;

@ICEResourceDependencies({
        @ICEResourceDependency(name="icefaces-compat.js", library="ice.compat",target="head", browser= BrowserType.ALL, browserOverride={}),
        @ICEResourceDependency(name="compat.js", library="ice.compat",target="head", browser=BrowserType.ALL, browserOverride={})
})
public class OutputResource extends UIComponentBase {

	public static final String COMPONENT_FAMILY = "com.icesoft.faces.OutputResource";
	public static final String COMPONENT_TYPE = "com.icesoft.faces.OutputResource";
	public static final String DEFAULT_RENDERER_TYPE = "com.icesoft.faces.OutputResourceRenderer";
    private static Log log = LogFactory.getLog(OutputResource.class);
	protected Resource resource;
	protected transient RegisteredResource registeredResource;
	private String mimeType;
	private Date lastModified;
	private String fileName;
	private String image;
	private String type;
	private String label;
	private String style;
	private String styleClass;
	private String renderedOnUserRole;
	private Boolean attachment;
	private transient int lastResourceHashCode;	
	transient String path;
	private Boolean shared;
    private String target;
    private Boolean UIDataChild = null;
	public static final String TYPE_IMAGE = "image";
	public static final String TYPE_BUTTON = "button";
	private transient Map  resources;
    private Boolean disabled = null;

    /*
	 * (non-Javadoc)
	 * 
	 * @see javax.faces.component.UIComponent#getFamily()
	 */
	public String getFamily() {
		return COMPONENT_FAMILY;
	}

	public String getComponentType() {
		return COMPONENT_TYPE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.faces.component.UIComponent#getRendererType()
	 */
	public String getRendererType() {
		return DEFAULT_RENDERER_TYPE;
	}

	public Resource getResource() {
	    final Resource currResource;
	    if (this.resource != null) {
	        currResource = this.resource;
	    }else {
            ValueBinding vb = getValueBinding("resource");
    		if (vb == null) {
                addInfo();
    		    return null;
    		}
    		currResource = (Resource) vb.getValue(getFacesContext());
	    }
	    if (currResource == null) return null;
		final String fileName = getFileName();
		if (!isUIDataChild()) {
            if( registeredResource == null ){
                registeredResource = new RegisteredResource(this, currResource,
                    fileName);
    		}
            else {
            registeredResource.updateContents(this, currResource, fileName);
            }
            path = ResourceRegistryLocator.locate(
                    FacesContext.getCurrentInstance())
                            .registerResource(registeredResource).getRawPath();
            registeredResource.setPath(path);
		} else {
		    if (resources == null) {
		        resources = new HashMap();
		    }
	        if (!resources.containsKey(currResource)) {
		          registeredResource = new RegisteredResource(this, currResource, fileName);
                resources.put(currResource, registeredResource);
		    } else {
		        registeredResource = (RegisteredResource)resources.get(currResource);
		        registeredResource.updateContents(this, currResource, fileName);
		    }
            path = ResourceRegistryLocator.locate(
                    FacesContext.getCurrentInstance())
                            .registerResource(registeredResource).getRawPath();
            registeredResource.setPath(path);
		}
		
		return currResource;
	}
	
    public void addInfo() {
        if (log.isInfoEnabled()) {
            log.info("The \"resource\" is not defined");
        }
    }

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	public String getMimeType() {
        if (mimeType != null) {
			return mimeType;
		}
		ValueBinding vb = getValueBinding("mimeType");
		return vb != null ? (String) vb.getValue(getFacesContext()) : null;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileName() {
		//must check fileName as valuebinding first and set local as 
		//ResourceDispatcher may not have access to FacesContext
		ValueBinding vb = getValueBinding("fileName");
		if( vb != null ){
			fileName = (String) vb.getValue(getFacesContext());
		}
		return fileName;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	public Date getLastModified() {
		if (lastModified != null)
			return lastModified;
		ValueBinding vb = getValueBinding("lastModified");
		return vb != null ? (Date) vb.getValue(getFacesContext()) : null;
	}

	public void setImage(String img) {
		this.image = img;
	}

	public String getImage() {
		if (image != null) {
			return image;
		}
		ValueBinding vb = getValueBinding("image");
		return vb != null ? (String) vb.getValue(getFacesContext()) : null;

	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		if (type != null) {
			return type;
		}
		ValueBinding vb = getValueBinding("type");
		return vb != null ? (String) vb.getValue(getFacesContext()) : null;

	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getLabel() {
		if (label != null) {
			return label;
		}
		ValueBinding vb = getValueBinding("label");
		if( vb != null )
			return (String) vb.getValue(getFacesContext());
		String fileName = getFileName();
		if( fileName != null && fileName.length() > 0 )
			return fileName;
		vb = getValueBinding("resource");
		Resource r = null;
		if( vb != null )
			r = (Resource) vb.getValue(getFacesContext());
		if( r != null && r instanceof FileResource )
			return ((FileResource)r).getFile().getName();
		return null;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getStyle() {
		if (style != null) {
			return style;
		}
		ValueBinding vb = getValueBinding("style");
		return vb != null ? (String) vb.getValue(getFacesContext()) : null;
	}

	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}

	/**
	 * <p>
	 * Return the value of the <code>styleClass</code> property.
	 * </p>
	 * 
	 * @return String styleClass
	 */
	public String getStyleClass() {
		String defaultStyle = CSS_DEFAULT.OUTPUT_LINK_DEFAULT_STYLE_CLASS;
		if (TYPE_BUTTON.equals(getType()))
			defaultStyle = CSS_DEFAULT.COMMAND_BTN_DEFAULT_STYLE_CLASS;
		return Util.getQualifiedStyleClass(this, styleClass, defaultStyle,
				"styleClass", false);
	}

	public void setRenderedOnUserRole(String role) {
		this.renderedOnUserRole = role;
	}

	/**
	 * <p>
	 * Return the value of the <code>renderedOnUserRole</code> property.
	 * </p>
	 */
	public String getRenderedOnUserRole() {
		if (renderedOnUserRole != null) {
			return renderedOnUserRole;
		}
		ValueBinding vb = getValueBinding("renderedOnUserRole");
		return vb != null ? (String) vb.getValue(getFacesContext()) : null;
	}

	/**
	 * <p>
	 * Return the value of the <code>rendered</code> property.
	 * </p>
	 */
	public boolean isRendered() {
		if (!Util.isRenderedOnUserRole(this)) {
			return false;
		}
		return super.isRendered();
	}

	public void setAttachment(boolean b) {
		this.attachment = Boolean.valueOf(b);
	}

	public boolean isAttachment() {
		if (attachment != null) {
			return (this.attachment.booleanValue());
		}
		ValueBinding vb = getValueBinding("attachment");
		if (vb != null) {
			return ((Boolean) vb.getValue(getFacesContext())).booleanValue();
		} else {
			return (true);
		}

	}

	/**
	 * <p>
	 * Gets the state of the instance as a <code>Serializable</code> Object.
	 * </p>
	 */
	public Object saveState(FacesContext context) {
		Object values[] = new Object[16];
		values[0] = super.saveState(context);
		values[1] = resource;
		values[2] = mimeType;
		values[3] = lastModified;
		values[4] = fileName;
		values[5] = image;
		values[6] = type;
		values[7] = label;
		values[8] = style;
		values[9] = styleClass;
		values[10] = renderedOnUserRole;
		values[11] = attachment;
		values[12] = shared;
		values[13] = target;
		values[14] = UIDataChild;
        values[15] = disabled;
        return ((Object) (values));
	}

	/**
	 * <p>
	 * Perform any processing required to restore the state from the entries in
	 * the state Object.
	 * </p>
	 */
	public void restoreState(FacesContext context, Object state) {
		Object values[] = (Object[]) state;
		super.restoreState(context, values[0]);
		resource = (Resource) values[1];
		mimeType = (String) values[2];
		lastModified = (Date) values[3];
		fileName = (String) values[4];
		image = (String) values[5];
		type = (String) values[6];
		label = (String) values[7];
		style = (String) values[8];
		styleClass = (String) values[9];
		renderedOnUserRole = (String) values[10];
		attachment = (Boolean) values[11];
		shared = (Boolean)values[12];
        target = (String) values[13];
        UIDataChild = (Boolean) values[14];
        disabled = (Boolean) values[15];
    }

	public boolean getAttachment() {
		return this.isAttachment();
	}

	public int getLastResourceHashCode() {
		return lastResourceHashCode;
	}

	public String getPath() {
		return  path; 
	}
	
	public boolean isShared(){
		if (shared != null) {
			return (this.shared.booleanValue());
		}
		ValueBinding vb = getValueBinding("shared");
		if (vb != null) {
			return ((Boolean) vb.getValue(getFacesContext())).booleanValue();
		} else {
			return (true);
		}
	}
	
	public void setShared(boolean s){
		this.shared = Boolean.valueOf(s);
	}

    public String getTarget() {
        if (target != null) return target;
        ValueBinding vb = getValueBinding("target");
        if (vb == null) return "_blank";
        Object value = vb.getValue(getFacesContext());
        if (value == null) return "_blank";
        return (String) value;
    }

    public void setTarget(String target) {
        this.target = target;
    }
    
    private boolean isUIDataChild() {
        if (UIDataChild == null) {
            UIDataChild = Boolean.FALSE;
            UIComponent parent = this.getParent();
            while(parent != null) {
                if (parent instanceof UIData || parent instanceof Tree) {
                    UIDataChild = Boolean.TRUE;
                    break;
                }
                parent = parent.getParent();
            }
        }
        return UIDataChild.booleanValue();
    }

    public void setDisabled(boolean disabled) {
        this.disabled = new Boolean(disabled);
        ValueBinding vb = getValueBinding("disabled");
        if (vb != null) {
            vb.setValue(getFacesContext(), this.disabled);
            this.disabled = null;
        }
    }

    public boolean isDisabled() {
        if (!Util.isEnabledOnUserRole(this)) {
            return true;
        }
        if (disabled != null) {
            return disabled.booleanValue();
        }
        ValueBinding vb = getValueBinding("disabled");
        Boolean v =
                vb != null ? (Boolean) vb.getValue(getFacesContext()) : null;
        return v != null ? v.booleanValue() : false;
    }
}

class RegisteredResource implements Resource, Serializable {
    private Resource resource;
    private String fileName;
    private String label;
    private Date lastModified;
    private String mimeType;
    private boolean isAttachment;
    private boolean isShared;
    private String path;
    
    public RegisteredResource(OutputResource outputResource, Resource resource, String fileName) {
        updateContents(outputResource, resource, fileName);
    }

    public String calculateDigest() {
        return resource.calculateDigest() + (isShared ? "" : String.valueOf(resource.hashCode()));
    }

    public Date lastModified() {
        return lastModified;
    }

    public InputStream open() throws IOException {
        return resource.open();
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path= path;
    }

    void updateContents(OutputResource outputResource, Resource resource, String fileName) {
        this.resource= resource;
        this.fileName = fileName;
        label = outputResource.getLabel();
        lastModified = outputResource.getLastModified();
        mimeType = outputResource.getMimeType();
        isAttachment = outputResource.isAttachment();
        isShared = outputResource.isShared();
    }
    
    public void withOptions(Options options) {
        ResourceOptions resourceOptions = new ResourceOptions();
        try {
            resource.withOptions(resourceOptions);
        } catch (IOException e) {
        }
        String fName = null;
        
        if (resourceOptions.fileName != null)
            fName = resourceOptions.fileName;
        else if (fileName != null) {
            fName = fileName;            
        } else if (resource instanceof FileResource) {
            fName =(((FileResource) resource).getFile()
                    .getName());
        } else if (label != null)
            fName = label.replace(' ', '_');
        
        options.setFileName(fName);

        if (resourceOptions.lastModified != null)
            options.setLastModified(resourceOptions.lastModified);
        else if (lastModified != null)
            options.setLastModified(lastModified);

        if (resourceOptions.mimeType != null)
            options.setMimeType(resourceOptions.mimeType);
        else if (mimeType != null)
            options.setMimeType(mimeType);

        if (resourceOptions.isAttachment)
            options.setAsAttachement();
        else if (isAttachment)
            options.setAsAttachement();


    }

    private class ResourceOptions implements Resource.Options {
        private Date lastModified;
        private String mimeType;
        private String fileName;
        private boolean isAttachment;

        public void setMimeType(String mimeType) {
            this.mimeType = mimeType;
        }

        public void setLastModified(Date lastModified) {
            this.lastModified = lastModified;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public void setExpiresBy(Date date) {
        }

        public void setAsAttachement() {
            isAttachment = true;
        }
    }
}
