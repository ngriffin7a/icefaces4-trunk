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
import java.util.Scanner;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;

import org.icefaces.samples.showcase.util.FacesUtils;

@ManagedBean(name= FileEntryMultipleBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class FileEntryMultipleBean implements Serializable {

    public static final String BEAN_NAME = "fileEntryMultiple";
	public String getBeanName() { return BEAN_NAME; }

    private Integer maxFileCount = 0;
    private String maxFileCountMessage = "The total number of selected files exceeds the allowed maximum.";

    private Long maxTotalSize = (long)0;
    private String maxTotalMessage = "The total size of the selected files exceeds the allowed maximum.";

    public String getMaxTotalSizeString() {
        return maxTotalSize + " (bytes)";
    }

    public void setMaxTotalSizeString(String maxTotalSizeString) {
        try { this.maxTotalSize = new Scanner(maxTotalSizeString).nextLong(); }
        catch (Exception e) {
            this.maxTotalSize = (long)0;
            FacesUtils.addErrorMessage("Max total size could not be set to: " + maxTotalSizeString);
        }
    }

    public long getMaxTotalSize() {
        return maxTotalSize;
    }

    public void setMaxTotalSize(long maxTotalSize) {
        this.maxTotalSize = maxTotalSize;
    }

    public String getMaxTotalMessage() {
        return maxTotalMessage;
    }

    public void setMaxTotalMessage(String maxTotalMessage) {
        this.maxTotalMessage = maxTotalMessage;
    }

    public Integer getMaxFileCount() {
        return maxFileCount;
    }

    public void setMaxFileCount(Integer maxFileCount) {
        this.maxFileCount = maxFileCount;
    }

    public String getMaxFileCountMessage() {
        return maxFileCountMessage;
    }

    public void setMaxFileCountMessage(String maxFileCountMessage) {
        this.maxFileCountMessage = maxFileCountMessage;
    }
}
