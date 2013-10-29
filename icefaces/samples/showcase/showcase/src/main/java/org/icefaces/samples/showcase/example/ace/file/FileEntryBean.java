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

import org.icefaces.samples.showcase.dataGenerators.ImageSet.ImageInfo;
import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;
import org.icefaces.ace.component.fileentry.*;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.icefaces.samples.showcase.dataGenerators.ImageSet;

@ComponentExample(
        title = "example.ace.fileentry.title",
        description = "example.ace.fileentry.description",
        example = "/resources/examples/ace/fileentry/fileentry.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="FileEntry.xhtml",
                    resource = "/resources/examples/ace/"+
                               "fileentry/fileentry.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="FileEntryBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/ace/file/FileEntryBean.java")
        }
)
@Menu(
	title = "menu.ace.fileentry.subMenu.title",
	menuLinks = {
                    @MenuLink(title = "menu.ace.fileentry.subMenu.main", isDefault = true, exampleBeanName = FileEntryBean.BEAN_NAME), 
                    @MenuLink(title = "menu.ace.fileentry.subMenu.listener", exampleBeanName = FileEntryListenerBean.BEAN_NAME),     
                    @MenuLink(title = "menu.ace.fileentry.subMenu.validation",exampleBeanName = FileEntryValidationOptionsBean.BEAN_NAME),
                    @MenuLink(title = "menu.ace.fileentry.subMenu.callback",exampleBeanName = FileEntryCallbackBean.BEAN_NAME)
})
@ManagedBean(name= FileEntryBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class FileEntryBean extends ComponentExampleImpl<FileEntryBean> implements Serializable {

    public static final String BEAN_NAME = "fileEntry";
    private List<UploadedFile> fileData = new ArrayList<UploadedFile>();
    private String totalFiles;
    private String totalSize;

    public FileEntryBean() {
        super(FileEntryBean.class);
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    public void sampleListener(FileEntryEvent e) {
        FileEntry fe = (FileEntry)e.getComponent();
        FileEntryResults results = fe.getResults();
        File parent = null;

        for (FileEntryResults.FileInfo i : results.getFiles()) {
            fileData.add(
                new UploadedFile(
                    i.getFileName(),
                    i.getSize() + " bytes",
                    i.getContentType(),
                    i.isSaved() ? null : ("File was not saved because: " +
                        i.getStatus().getFacesMessage(
                            FacesContext.getCurrentInstance(),
                            fe, i).getSummary()) ));

            if (i.isSaved()) {
                File file = i.getFile();
                if (file != null) {
                    parent = file.getParentFile();
                }
            }
        }

        if (parent != null) {
            long dirSize = 0;
            int fileCount = 0;
            for (File file : parent.listFiles()) {
                fileCount++;
                dirSize += file.length();
            }
            totalFiles = "Total Files in Upload Directory: " + fileCount;
            totalSize = "Total Size of Files In Directory: " + dirSize + " bytes";
        }
    }

    public List getFileData() {
        return fileData;
    }

    public String getTotalFiles() {
        return totalFiles;
    }

    public String getTotalSize() {
        return totalSize;
    }


    public static class UploadedFile {
        private String name;
        private String size;
        private String contentType;
        private String info;

        UploadedFile(String name, String size, String contentType, String info) {
            this.name = name;
            this.size = size;
            this.contentType = contentType;
            this.info = info;
        }

        public String getName() { return name; }
        public String getSize() { return size; }
        public String getContentType() { return contentType; }
        public String getInfo() { return info; }
    }
}
