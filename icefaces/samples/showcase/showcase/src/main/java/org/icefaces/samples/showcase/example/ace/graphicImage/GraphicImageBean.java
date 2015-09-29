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

package org.icefaces.samples.showcase.example.ace.graphicImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import org.icefaces.ace.util.IceOutputResource;


@ManagedBean(name = GraphicImageBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class GraphicImageBean implements Serializable {
    public static final String BEAN_NAME = "graphicImageBean";
	public String getBeanName() { return BEAN_NAME; }
    
    @PostConstruct
    public void initMetaData() {
		// byte array image
		String resourcePath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/media/icemobile.png");
		File file = new File(resourcePath);
		try {
			this.byteArrayImage = readIntoByteArray(new FileInputStream(file));
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			this.byteArrayImage = new byte[0];
		}
		// IceOutputResource image
		resourcePath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/media/icepdf.png");
		file = new File(resourcePath);
		try {
			this.resourceImage = new IceOutputResource("resourceGraphicImage", readIntoByteArray(new FileInputStream(file)), "image/png");
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			this.resourceImage = new IceOutputResource("resourceGraphicImage", new byte[0], "image/png");
		}
    }

	private IceOutputResource resourceImage;
	private byte[] byteArrayImage;

	public IceOutputResource getResourceImage() {
		return resourceImage;
	}

	public void setResourceImage(IceOutputResource resourceImage) {
		this.resourceImage = resourceImage;
	}

	public byte[] getByteArrayImage() {
		return byteArrayImage;
	}

	public void setByteArrayImage(byte[] byteArrayImage) {
		this.byteArrayImage = byteArrayImage;
	}

    private static byte[] readIntoByteArray(InputStream in) throws IOException {
        byte[] buffer = new byte[4096];
        int bytesRead;
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        while ((bytesRead = in.read(buffer)) != -1) {
            out.write(buffer, 0, bytesRead);
        }
        out.flush();

        return out.toByteArray();
    }
}
