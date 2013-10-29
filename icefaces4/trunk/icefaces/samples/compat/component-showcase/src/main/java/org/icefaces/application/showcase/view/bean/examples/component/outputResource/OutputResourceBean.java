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

package org.icefaces.application.showcase.view.bean.examples.component.outputResource;

import com.icesoft.faces.context.Resource;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;

@ManagedBean(name = "resourceBean")
@ViewScoped
public class OutputResourceBean implements Serializable {

    private Resource imgResource;
    private Resource pdfResource;
    private Resource pdfResourceDynFileName;
    private String fileName = "Choose-a-new-file-name";
    public static final String RESOURCE_PATH = "/WEB-INF/classes/org/icefaces/application/showcase/view/resources/";


	public OutputResourceBean(){
		try{
            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();
            imgResource = new MyResource(ec,"logo.jpg");
			pdfResource =  new MyResource(ec,"WP_Security_Whitepaper.pdf");
			pdfResourceDynFileName = new MyResource(ec,"WP_Security_Whitepaper.pdf");
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

    public Resource getImgResource() {
        return imgResource;
    }


    public Resource getPdfResource() {
        return pdfResource;
    }

    public static byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buf = new byte[4096];
        int len = 0;
        while ((len = input.read(buf)) > -1) output.write(buf, 0, len);
        return output.toByteArray();
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Resource getPdfResourceDynFileName() {
        return pdfResourceDynFileName;
    }

    public void setPdfResourceDynFileName(Resource pdfResourceDynFileName) {
        this.pdfResourceDynFileName = pdfResourceDynFileName;
    }
}

class MyResource implements Resource, Serializable {
    private String resourceName;
    private byte[] content;
    private final Date lastModified;

    public MyResource(ExternalContext ec, String resourceName) {
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
        if (content == null) {
            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();
            InputStream stream = ec.getResourceAsStream(OutputResourceBean.RESOURCE_PATH + resourceName);
            content = OutputResourceBean.toByteArray(stream);
        }

        return new ByteArrayInputStream(content);
    }

    public String calculateDigest() {
        return resourceName;
    }

    public Date lastModified() {
        return lastModified;
    }

    public void withOptions(Options arg0) throws IOException {
    }
}   


