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

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.icefaces.ace.component.fileentry.FileEntryCallback;
import org.icefaces.ace.component.fileentry.FileEntryResults;
import org.icefaces.ace.component.fileentry.FileEntryStatus;
import org.icefaces.samples.showcase.example.ace.fileentry.utils.FileEntryMessageUtils;

@ManagedBean(name = FileEntryCallbackBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class FileEntryCallbackBean implements FileEntryCallback, Serializable {
		
	public static final String BEAN_NAME = "fileEntryCallback";
	public String getBeanName() { return BEAN_NAME; }
	private static Logger logger = Logger.getLogger(FileEntryCallbackBean.class
			.getName());
	private static transient MessageDigest digest;
	private static boolean noAlgorithm = false;
	private boolean validFile = true;

	// Executed before the upload starts
	public void begin(FileEntryResults.FileInfo fileInfo) {
		// Check the file type
		validFile = !fileInfo.getContentType().equals("application/pdf");
		// Initialize the message digest
		try {
			digest = MessageDigest.getInstance("SHA");
		} catch (NoSuchAlgorithmException e) {
			noAlgorithm = true;
		}
	}

	public void write(int i) {
		if (!noAlgorithm && validFile)
			digest.update((byte) i);

	}

	public void write(byte[] bytes, int offset, int length) {
		if (!noAlgorithm && validFile)
			digest.update(bytes, offset, length);

	}

	// Executed at the end of a file upload
	public void end(FileEntryResults.FileInfo fileEntryInfo) {
		if (validFile)
			fileEntryInfo.updateStatus(new UploadSuccessStatus(), false);
		else
			fileEntryInfo.updateStatus(new InvalidFileStatus(), false);

	}

	private static class InvalidFileStatus implements FileEntryStatus, Serializable {

		private static final long serialVersionUID = -8970901096973731657L;

		public boolean isSuccess() {
			return false;
		}

		public FacesMessage getFacesMessage(FacesContext facesContext,
				UIComponent uiComponent, FileEntryResults.FileInfo fileInfo) {
			return new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					FileEntryMessageUtils
							.getMessage("example.ace.fileentry.callback.error"),
					fileInfo.getFileName());
		}
	}

	private static class UploadSuccessStatus implements FileEntryStatus, Serializable {

		private static final long serialVersionUID = 5737086952765222416L;
		
		public boolean isSuccess() {
			return true;
		}

		public FacesMessage getFacesMessage(FacesContext facesContext,
				UIComponent uiComponent, FileEntryResults.FileInfo fileInfo) {
			return new FacesMessage(
					FacesMessage.SEVERITY_INFO,
					FileEntryMessageUtils
							.getMessage("example.ace.fileentry.callback.success"),
					fileInfo.getFileName());
		}
	}

}