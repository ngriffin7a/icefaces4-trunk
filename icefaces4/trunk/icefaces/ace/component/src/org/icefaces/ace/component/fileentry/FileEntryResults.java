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

package org.icefaces.ace.component.fileentry;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import java.util.ArrayList;
import java.io.Serializable;
import java.io.File;

public class FileEntryResults implements Serializable, Cloneable {
    private ArrayList<FileInfo> fileInfos;
    private boolean viaCallback;
    private long totalSize;

    FileEntryResults(boolean viaCallback) {
        fileInfos = new ArrayList<FileInfo>(6);
        this.viaCallback = viaCallback;
    }

    public ArrayList<FileInfo> getFiles() {
        return fileInfos;
    }
    
    public boolean isViaCallback() {
        return viaCallback;
    }

    /**
     * @return Deep copy
     */
    public Object clone() {
        FileEntryResults results = new FileEntryResults(viaCallback);
        int numFileEntries = fileInfos.size();
        results.fileInfos = new ArrayList<FileInfo>(Math.max(1,numFileEntries));
        for (FileInfo fi : fileInfos) {
            results.fileInfos.add( (FileInfo) fi.clone() );
        }
        return results;
    }
    
    public String toString() {
        String pre = "FileEntryResults: {" +
                     "\n  viaCallback=" + viaCallback +
                     ",\n  totalSize=" + totalSize +
                     ",\n  files:\n";
        StringBuilder mid = new StringBuilder();
        for (FileInfo fi : fileInfos) {
            mid.append(fi.toString());
        }
        String post ="\n}";
        return pre + mid + post;
    }
    
    public boolean equals(Object ob) {
        if (!(ob instanceof FileEntryResults)) {
            return false;
        }
        FileEntryResults results = (FileEntryResults) ob;
        if (this.viaCallback != results.viaCallback) {
            return false;
        }
        if ((this.fileInfos == null && results.fileInfos != null) ||
            (this.fileInfos != null && results.fileInfos == null)) {
            return false;
        }
        if (this.fileInfos != null) {
            int sz = this.fileInfos.size();
            int isz = results.fileInfos.size();
            if (sz != isz) {
                return false;
            }
            for (int i = 0; i < sz; i++) {
                FileInfo fi = this.fileInfos.get(i);
                FileInfo ifi = results.fileInfos.get(i);
                if (!fi.equals(ifi)) {
                    return false;
                }
            }
        }
        return true;
    }
    
    void addCompletedFile(FileInfo fileInfo) {
        fileInfos.add(fileInfo);
        totalSize += fileInfo.getSize();
    }
    
    long getAvailableTotalSize(long maxTotalSize) {
        return Math.max(0, maxTotalSize - totalSize);
    }

    boolean isLifecycleAndUploadsSuccessful(FacesContext context) {
        if (context.isValidationFailed()) {
            return false;
        }
        for (FileEntryResults.FileInfo fi : fileInfos) {
            if (!fi.isSaved()) {
                return false;
            }
        }
        return true;
    }


    public static class FileInfo implements Serializable, Cloneable {
        private String fileName;
        private String contentType;
        private File file;
        private long size;
        private FileEntryStatus status;
        
        FileInfo() {
        }

        void begin(String fileName, String contentType) {
            this.fileName = fileName;
            this.contentType = contentType;
        }

        /**
         * When a file fails uploading, before even processing the contents
         * @param status The failed status
         */
        void prefail(FileEntryStatus status) {
            this.status = status;
        }

        /**
         * When a file fails uploading, after the upload is complete, which
         * can only happen when the FileEntryCallback's end method throws a
         * RuntimeException.
         * @param status The failed status
         */
        void postfail(FileEntryStatus status) {
            this.status = status;
        }

        void finish(File file, long size, FileEntryStatus status) {
            this.file = file;
            this.size = size;
            this.status = status;
        }

        /**
         * In the fileEntryListener, applications may override the uploaded
         * files' statuses, if the files fail the application's custom
         * validation, by using this method. They can even set a custom status,
         * with its own message format for the faces message.
         *
         * @param newStatus The new status for the uploaded file
         * @param invalidate If should invalidate the component, and the form.
         * This will only have an effect if this method is called before
         * UpdateModel phase, such as when called from the fileEntryListener
         * when the fileEntry has immediate=true.
         * @param deleteFile If should delete the file from the file-system
         */
        public void updateStatus(FileEntryStatus newStatus, boolean invalidate,
                boolean deleteFile) {
            updateStatus(newStatus, invalidate);
            if (deleteFile && file != null && file.exists()) {
                file.delete();
            }
        }

        /**
         * In the callback, applications may override the uploaded
         * files' statuses, if the files fail the application's custom
         * validation, by using this method. They can even set a custom status,
         * with its own message format for the faces message.
         *
         * @param newStatus The new status for the uploaded file
         * @param invalidate If should invalidate the component, and the form.
         */
        public void updateStatus(FileEntryStatus newStatus, boolean invalidate) {
            if (newStatus != null) {
                status = newStatus;
            }
            if (invalidate) {
                FacesContext context = FacesContext.getCurrentInstance();
                context.validationFailed();
                PhaseId phase = context.getCurrentPhaseId();
                if (phase != null && !PhaseId.RESTORE_VIEW.equals(phase)) {
                    context.renderResponse();
                }
            }
        }

        public String getFileName() { return fileName; }
        public String getContentType() { return contentType; }
        public File getFile() { return file; }
        public long getSize() { return size; }
        public FileEntryStatus getStatus() { return status; }
        
        public boolean isSaved() { return status.isSuccess(); }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            FileInfo fileInfo = (FileInfo) o;

            if (fileName != null ? !fileName.equals(fileInfo.fileName) : fileInfo.fileName != null) {
                return false;
            }
            if (contentType != null ? !contentType.equals(fileInfo.contentType) : fileInfo.contentType != null) {
                return false;
            }
            if (file != null ? !file.equals(fileInfo.file) : fileInfo.file != null) {
                return false;
            }
            if (size != fileInfo.size) {
                return false;
            }
            if (status != fileInfo.status) {
                return false;
            }
            
            return true;
        }

        public int hashCode() {
            int result;
            result = (int) (size ^ (size >>> 32));
            result = 31 * result + (fileName != null ? fileName.hashCode() : 0);
            result = 31 * result + (contentType != null ? contentType.hashCode() : 0);
            result = 31 * result + (file != null ? file.hashCode() : 0);
            result = 31 * result + (status != null ? status.hashCode() : 0);
            return result;
        }

        public Object clone() {
            FileInfo fileInfo = new FileInfo();
            fileInfo.fileName = this.fileName;
            fileInfo.contentType = this.contentType;
            fileInfo.file = this.file;
            fileInfo.size = this.size;
            fileInfo.status = this.status;
            return fileInfo;
        }

        public String toString() {
            return
                "FileEntryResults.FileInfo: {" +
                    "\n  fileName=" + fileName +
                    ",\n  contentType=" + contentType +
                    ",\n  file=" + file +
                    ",\n  size=" + size +
                    ",\n  status=" + status +
                    "\n}";
        }
    }
}
