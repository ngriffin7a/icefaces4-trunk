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

package org.icefaces.samples.showcase.example.compat.graphicimage;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;

import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.Menu;
import org.icefaces.samples.showcase.metadata.annotation.MenuLink;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import com.icesoft.faces.context.FileResource;

import java.io.*;

@ComponentExample(
        title = "example.compat.graphicimage.title",
        description = "example.compat.graphicimage.description",
        example = "/resources/examples/compat/graphicimage/graphicImage.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="graphicImage.xhtml",
                    resource = "/resources/examples/compat/"+
                               "graphicimage/graphicImage.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="GraphicImageBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/graphicimage/GraphicImageBean.java")
        }
)
@Menu(
	title = "menu.compat.graphicimage.subMenu.title",
	menuLinks = {
            @MenuLink(title = "menu.compat.graphicimage.subMenu.main",
                    isDefault = true,
                    exampleBeanName = GraphicImageBean.BEAN_NAME)
})
@ManagedBean(name= GraphicImageBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class GraphicImageBean extends ComponentExampleImpl<GraphicImageBean> implements Serializable {
	
    public static final String BEAN_NAME = "graphicImage";
	
	private static final String FILE_PATH = "/resources/outputresource/icefaces.png";
    private FileResource fileResource;
	private byte[] bytes;
	
    public GraphicImageBean() {
            super(GraphicImageBean.class);
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
		
		FacesContext fc = FacesContext.getCurrentInstance();
		ExternalContext ec = fc.getExternalContext();
		String path = ec.getRealPath(FILE_PATH);
		File file = new File(path);
		fileResource = new FileResource(file);
		
		try {
			bytes = readIntoByteArray(ec.getResourceAsStream(FILE_PATH));
		} catch (IOException e) {
			bytes = new byte[0];
		}
    }

	public FileResource getFileResource() {
		return fileResource;
	}
	
	public byte[] getBytes() {
		return bytes;
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
