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
import org.icefaces.samples.showcase.util.FacesUtils;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;
import java.util.Scanner;

@ComponentExample(
        parent = FileEntryBean.BEAN_NAME,
        title = "menu.ace.fileentry.subMenu.multiple",
        description = "example.ace.fileentry.multiple.description",
        example = "/resources/examples/ace/fileentry/multiple.xhtml"
)
@ExampleResources(
resources ={
    // xhtml
    @ExampleResource(type = ResourceType.xhtml,
            title="multiple.xhtml",
            resource = "/resources/examples/ace/"+
                       "fileentry/multiple.xhtml"),
    // Java Source
    @ExampleResource(type = ResourceType.java,
            title="FileEntryMultipleBean.java",
            resource = "/WEB-INF/classes/org/icefaces/samples/"+
                       "showcase/example/ace/file/FileEntryMultipleBean.java")
}
)
@ManagedBean(name= FileEntryMultipleBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class FileEntryMultipleBean extends ComponentExampleImpl<FileEntryMultipleBean> implements Serializable {

    public static final String BEAN_NAME = "fileEntryMultiple";

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

    public FileEntryMultipleBean() {
        super(FileEntryMultipleBean.class);
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }
}
