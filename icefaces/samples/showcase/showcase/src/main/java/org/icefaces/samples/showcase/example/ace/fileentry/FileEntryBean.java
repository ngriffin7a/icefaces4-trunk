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

package org.icefaces.samples.showcase.example.ace.fileentry;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import org.icefaces.ace.component.fileentry.FileEntry;
import org.icefaces.ace.component.fileentry.FileEntryEvent;
import org.icefaces.ace.component.fileentry.FileEntryResults;

@ManagedBean(name= FileEntryBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class FileEntryBean implements Serializable {

    public static final String BEAN_NAME = "fileEntry";
	public String getBeanName() { return BEAN_NAME; }
    private List<UploadedFile> fileData = new ArrayList<UploadedFile>();
    private String totalFiles;
    private String totalSize;

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


    public static class UploadedFile implements Serializable {
        private static final long serialVersionUID = 4803879439245875558L;
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
