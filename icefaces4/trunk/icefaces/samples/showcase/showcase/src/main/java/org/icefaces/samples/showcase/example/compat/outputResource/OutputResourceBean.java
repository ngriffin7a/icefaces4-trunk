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

package org.icefaces.samples.showcase.example.compat.outputResource;

import java.io.Serializable;
import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.net.URL;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import com.icesoft.faces.context.Resource;
import com.icesoft.faces.context.FileResource;

import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.Menu;
import org.icefaces.samples.showcase.metadata.annotation.MenuLink;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

@ComponentExample(
        title = "example.compat.outputResource.title",
        description = "example.compat.outputResource.description",
        example = "/resources/examples/compat/outputResource/outputResource.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="outputResource.xhtml",
                    resource = "/resources/examples/compat/"+
                               "outputResource/outputResource.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="OutputResourceBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/outputResource/OutputResourceBean.java")
        }
)
@Menu(
	title = "menu.compat.outputResource.subMenu.title",
	menuLinks = {
            @MenuLink(title = "menu.compat.outputResource.subMenu.main",
                    isDefault = true,
                    exampleBeanName = OutputResourceBean.BEAN_NAME),
            @MenuLink(title = "menu.compat.outputResource.subMenu.type",
                    exampleBeanName = OutputResourceType.BEAN_NAME),
            @MenuLink(title = "menu.compat.outputResource.subMenu.attachment",
                    exampleBeanName = OutputResourceAttachment.BEAN_NAME),
            @MenuLink(title = "menu.compat.outputResource.subMenu.filename",
                    exampleBeanName = OutputResourceFilename.BEAN_NAME),
            @MenuLink(title = "menu.compat.outputResource.subMenu.label",
                    exampleBeanName = OutputResourceLabel.BEAN_NAME)
})
@ManagedBean(name= OutputResourceBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class OutputResourceBean extends ComponentExampleImpl<OutputResourceBean> implements Serializable {
	
	public static final String BEAN_NAME = "outputResource";
	
	public static final String CUSTOM_NAME = "Custom-Name.pdf";
	public static final String PDF_NAME = "Training.pdf";
	public static final String IMAGE_NAME = "ICEfaces-2.gif";
	public static final Resource PDF_RESOURCE = new MyResource(PDF_NAME);
	public static final Resource NAMED_RESOURCE = new MyResource(CUSTOM_NAME, PDF_NAME);
	public static final Resource IMAGE_RESOURCE = new MyResource(IMAGE_NAME);
	
	public static final String RESOURCE_PATH = "/resources/outputresource/";
	
	public OutputResourceBean() {
		super(OutputResourceBean.class);
	}
	
    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

	public String getPdfName() { return PDF_NAME; }
	public String getImageName() { return IMAGE_NAME; }
	public Resource getPdfResource() { return PDF_RESOURCE; }
	public Resource getNamedResource() { return NAMED_RESOURCE; }
	public Resource getImageResource() { return IMAGE_RESOURCE; }
	
	private static File getFile(String name) {
	    URL loadUrl = OutputResourceBean.class.getResource(name);
	    
	    if (loadUrl != null) {
	        return new File(loadUrl.getFile());
	    }
	    
	    return null;
	}
	
	private static Resource loadResource(String name) {
	    File toLoad = getFile(name);
	    
	    if (toLoad != null) {
	        return new FileResource(toLoad);
	    }
	    
	    return null;
	}
	
    public static byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buf = new byte[4096];
        int len = 0;
        while ((len = input.read(buf)) > -1) output.write(buf, 0, len);
        return output.toByteArray();
    }
}
	
class NamedResource implements Resource, Serializable {
    private String name;
    private File file;

    public NamedResource(String name, File file) {
        this.name = name;
        this.file = file;
    }
    
    public InputStream open() throws IOException {
        return new FileInputStream(file);
    }
    
    public String calculateDigest() {
        return name;
    }

    public Date lastModified() {
        return new Date(file.lastModified());
    }

    public void withOptions(Options arg0) throws IOException {
    }
}

class MyResource implements Resource, Serializable {
	private String customName;
    private String resourceName;
    private InputStream inputStream;
    private final Date lastModified;

    public MyResource(String resourceName) {
		this.customName = resourceName;
        this.resourceName = resourceName;
        this.lastModified = new Date();
    }
	
    public MyResource(String customName, String resourceName) {
        this.customName = customName;
        this.resourceName = resourceName;
        this.lastModified = new Date();
    }

    /**
     * This intermediate step of reading in the files from the JAR, into a
     * byte array, and then serving the Resource from the ByteArrayInputStream,
     * is not strictly necessary, but serves to illustrate that the Resource
     * content need not come from an actual file, but can come from any source,
     * and also be dynamically generated. In most cases, applications need not
     * provide their own concrete implementations of Resource, but can instead
     * simply make use of com.icesoft.faces.context.ByteArrayResource,
     * com.icesoft.faces.context.FileResource, com.icesoft.faces.context.JarResource.
     */
    public InputStream open() throws IOException {
        if (inputStream == null) {
            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();
            InputStream stream = ec.getResourceAsStream(OutputResourceBean.RESOURCE_PATH + resourceName);
            byte[] byteArray = OutputResourceBean.toByteArray(stream);
            inputStream = new ByteArrayInputStream(byteArray);
        } else {
            inputStream.reset();
        }
        return inputStream;
    }

    public String calculateDigest() {
        return customName;
    }

    public Date lastModified() {
        return lastModified;
    }

    public void withOptions(Options arg0) throws IOException {
    }
}   