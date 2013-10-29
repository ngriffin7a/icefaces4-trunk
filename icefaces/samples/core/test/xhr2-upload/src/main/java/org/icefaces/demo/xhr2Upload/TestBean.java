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

package org.icefaces.demo.xhr2Upload;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ViewScoped;
import javax.faces.bean.ManagedBean;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Scanner;

@ManagedBean(name = "TestBean")
@ViewScoped
public class TestBean implements Serializable {
    private ArrayList<PartInfo> uploadedFiles = new ArrayList();
    private Part file;

    public void upload() {

    }

    public Part getFile() {
        return file;
    }

    public void setFile(Part file) {
        this.file = file;
        uploadedFiles.add(new PartInfo(file));
    }

    public ArrayList<PartInfo> getUploadedFiles() {
        return uploadedFiles;
    }

    public static class PartInfo implements Serializable {
        private Part part;

        public PartInfo(Part part) {
            this.part = part;
        }


        public String getFileName() {
            return part.getName();
        }

        public String getContentType() {
            return part.getContentType();
        }

        public String getContentDisposition() {
            return part.getHeader("content-disposition");
        }

        public String getContent() throws IOException {
            return new Scanner(part.getInputStream()).nextLine() + " .... ";
        }
    }
}