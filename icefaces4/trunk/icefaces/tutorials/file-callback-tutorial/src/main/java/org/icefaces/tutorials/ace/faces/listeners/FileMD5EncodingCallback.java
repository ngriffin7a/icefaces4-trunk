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

package org.icefaces.tutorials.ace.faces.listeners;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.FilenameUtils;
import org.icefaces.ace.component.fileentry.FileEntryCallback;
import org.icefaces.ace.component.fileentry.FileEntryResults;
import org.icefaces.ace.component.fileentry.FileEntryStatus;
import org.icefaces.tutorials.util.TutorialMessageUtils;

public class FileMD5EncodingCallback implements FileEntryCallback, Serializable {
    private static final List<String> extensionList = Arrays.asList("pptx","ppt","zip","etc");

    private static Boolean md5NotFound;

    private boolean invalidExtension;
	private transient MessageDigest digest;

	// Set up a instance of a MD5 block-encoder
	public void begin(FileEntryResults.FileInfo fileInfo) {
        if (md5NotFound == null || !md5NotFound) {
            try {
                digest = MessageDigest.getInstance("MD5");
                md5NotFound = Boolean.FALSE;
            } catch (NoSuchAlgorithmException e) {
                md5NotFound = Boolean.TRUE;
            }
        }
        if (md5NotFound) {
            return;
        }

        // We can fail the file here for invalid file type, or some other reason
        invalidExtension = extensionList.contains(FilenameUtils.getExtension(
            fileInfo.getFileName()));
	}

	// Hash a block of bytes
	public void write(byte[] bytes, int offset, int length) {
		if (!md5NotFound && !invalidExtension) {
			digest.update(bytes, offset, length);
		}
	}

	// Hash a single byte
	public void write(int i) {
		if (!md5NotFound && !invalidExtension) {
			digest.update((byte) i);
		}
	}

	// When FileEntryCallback ends for a file:
	public void end(FileEntryResults.FileInfo fileInfo) {
		if (md5NotFound) {
			fileInfo.updateStatus(new EncodingNotFoundUploadStatus(), true);
        }
        else if (invalidExtension) {
			fileInfo.updateStatus(new InvalidFileStatus(), true);
		}
		// If the file upload was completed properly
		else if (fileInfo.getStatus().isSuccess()) {
			fileInfo.updateStatus(new EncodingSuccessStatus(
                String.valueOf(Hex.encodeHex(digest.digest()))), false);
		}
		digest = null;
	}

	private static class EncodingNotFoundUploadStatus implements
			FileEntryStatus {
		public boolean isSuccess() {
			return false;
		}

		public FacesMessage getFacesMessage(FacesContext facesContext,
				UIComponent uiComponent, FileEntryResults.FileInfo fileInfo) {
			return new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					TutorialMessageUtils
							.getMessage("content.callback.encode.fail.message"),
					TutorialMessageUtils
							.getMessage("content.callback.encode.fail.detail"));
		}
	}

	private static class EncodingSuccessStatus implements FileEntryStatus {
		private String hash;

		EncodingSuccessStatus(String hash) {
			this.hash = hash;
		}

		public boolean isSuccess() {
			return true;
		}

		public FacesMessage getFacesMessage(FacesContext facesContext,
				UIComponent uiComponent, FileEntryResults.FileInfo fileInfo) {
            return new FacesMessage(FacesMessage.SEVERITY_INFO,
                TutorialMessageUtils.getMessage(
                    "content.callback.success.message"), hash);
		}
	}

	private static class InvalidFileStatus implements FileEntryStatus {
		public boolean isSuccess() {
			return false;
		}

		public FacesMessage getFacesMessage(FacesContext facesContext,
				UIComponent uiComponent, FileEntryResults.FileInfo fileInfo) {
			
			return new FacesMessage(FacesMessage.SEVERITY_ERROR,
                TutorialMessageUtils.getMessage(
                    "content.callback.invalid.message"),
                    FilenameUtils.getExtension(fileInfo.getFileName()));
		}
	}
}