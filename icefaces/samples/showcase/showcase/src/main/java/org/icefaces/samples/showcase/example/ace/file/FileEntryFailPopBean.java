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

package org.icefaces.samples.showcase.example.ace.file;

import java.io.Serializable;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;

import org.icefaces.ace.component.fileentry.FileEntry;
import org.icefaces.ace.component.fileentry.FileEntryEvent;
import org.icefaces.ace.component.fileentry.FileEntryResults;

@ManagedBean(name= FileEntryFailPopBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class FileEntryFailPopBean implements Serializable { 
    public static final String BEAN_NAME = "fileEntryFailPop";
	public String getBeanName() { return BEAN_NAME; }

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
}
