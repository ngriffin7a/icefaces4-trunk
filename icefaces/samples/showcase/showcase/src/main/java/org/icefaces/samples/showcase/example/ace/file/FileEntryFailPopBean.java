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

package org.icefaces.samples.showcase.example.ace.file;

import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import org.icefaces.ace.component.fileentry.*;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;

@ComponentExample(
        parent = FileEntryBean.BEAN_NAME,
        title = "example.ace.fileentry.failpop.title",
        description = "example.ace.fileentry.failpop.description",
        example = "/resources/examples/ace/fileentry/failpop.xhtml"
)
@ExampleResources(
resources ={
    // xhtml
    @ExampleResource(type = ResourceType.xhtml,
            title="failpop.xhtml",
            resource = "/resources/examples/ace/"+
                       "fileentry/failpop.xhtml"),
    // Java Source
    @ExampleResource(type = ResourceType.java,
            title="FileEntryFailPopBean.java",
            resource = "/WEB-INF/classes/org/icefaces/samples/"+
                       "showcase/example/ace/file/FileEntryFailPopBean.java")
}
)
@ManagedBean(name= FileEntryFailPopBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class FileEntryFailPopBean extends ComponentExampleImpl<FileEntryFailPopBean>
        implements Serializable {

    public static final String BEAN_NAME = "fileEntryFailPop";

    private boolean useListener;
    private String popupMessage;
    private String fileExtension;

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public boolean isUseListener() {
        return useListener;
    }

    public void setUseListener(boolean useListener) {
        this.useListener = useListener;
    }

    public String getPopupMessage() {
        return popupMessage;
    }

    public void setPopupMessage(String popupMessage) {
        this.popupMessage = popupMessage;
    }

    public void popupValidationListener(FileEntryEvent event) {
        if (!useListener) return;
            for (FileEntryResults.FileInfo file : ((FileEntry)event.getComponent()).getResults().getFiles()) {
                if (file.getFileName().endsWith(fileExtension)) {
                    file.updateStatus(file.getStatus(), true, true);
                    // throw a popup
                }
            }
        }


    public FileEntryFailPopBean() {
        super(FileEntryFailPopBean.class);
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }
}
