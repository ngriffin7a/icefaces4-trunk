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

import java.io.Serializable;

/**
 * The uploaded files need to be processed before RestoreViewPhase, meaning
 * that the FileEntry component is not yet available, to direct how and where
 * to save the files. So, any information that is needed to process uploaded
 * files needs to be accessible without a direct reference to the FileEntry.
 */
public class FileEntryConfig implements Serializable {
    private String identifier;
    private String clientId;
    
    private String absolutePath;
    private String relativePath;
    private boolean useSessionSubdir;
    private boolean useOriginalFilename;
    private String callbackEL;
    private long maxTotalSize;
    private long maxFileSize;
    private int maxFileCount;
    private boolean required;

    private String progressResourceName;
    private String progressGroupName;
    
    /**
     * InputFile uses this for publishing its own property configuration
     */
    public FileEntryConfig(
        String identifier,
        String clientId,
        String absolutePath,
        String relativePath,
        boolean useSessionSubdir,
        boolean useOriginalFilename,
        String callbackEL,
        long maxTotalSize,
        long maxFileSize,
        int maxFileCount,
        boolean required,
        String progressResourcePath,
        String progressGroupName) {
        
        this.identifier = identifier;
        this.clientId = clientId;
        this.absolutePath = absolutePath;
        this.relativePath = relativePath;
        this.useSessionSubdir = useSessionSubdir;
        this.useOriginalFilename = useOriginalFilename;
        this.callbackEL = callbackEL;
        this.maxTotalSize = maxTotalSize;
        this.maxFileSize = maxFileSize;
        this.maxFileCount = maxFileCount;
        this.required = required;
        this.progressResourceName = progressResourcePath;
        this.progressGroupName = progressGroupName;
    }


    public String getIdentifier() {
        return identifier;
    }
    
    public String getClientId() {
        return clientId;
    }
    
    public String getAbsolutePath() {
        return absolutePath;
    }
    
    public String getRelativePath() {
        return relativePath;
    }
    
    public boolean isUseSessionSubdir() {
        return useSessionSubdir;
    }
    
    public boolean isUseOriginalFilename() {
        return useOriginalFilename;
    }
    
    public String getCallbackEL() {
        return callbackEL;
    }
    
    public boolean isViaCallback() {
        return callbackEL != null;
    }
    
    public long getMaxTotalSize() {
        return maxTotalSize;
    }
    
    public long getMaxFileSize() {
        return maxFileSize;
    }
    
    public int getMaxFileCount() {
        return maxFileCount;
    }

    public boolean isRequired() {
        return required;
    }

    public String getProgressResourceName() {
        return progressResourceName;
    }

    public String getProgressGroupName() {
        return progressGroupName;
    }


    public String toString() {
        return
            "FileEntryConfig: {" +
            "\n  clientId=" + clientId +
            ",\n  absolutePath=" + absolutePath +
            ",\n  relativePath=" + relativePath +
            ",\n  useSessionSubdir=" + useSessionSubdir +
            ",\n  useOriginalFilename=" + useOriginalFilename +
            ",\n  callbackEL=" + callbackEL +
            ",\n  maxTotalSize=" + maxTotalSize +
            ",\n  maxFileSize=" + maxFileSize +
            ",\n  maxFileCount=" + maxFileCount +
            ",\n  required=" + required +
            ",\n  identifier=" + identifier +
            ",\n  progressResourceName=" + progressResourceName +
            ",\n  progressGroupName=" + progressGroupName +
            "\n}";
    }
}
