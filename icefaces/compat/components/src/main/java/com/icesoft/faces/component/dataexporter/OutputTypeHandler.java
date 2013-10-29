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

package com.icesoft.faces.component.dataexporter;

import java.io.File;
import java.io.IOException;

public abstract class OutputTypeHandler {

	protected File file;
	protected String mimeType;

	public String getMimeType() {
		return mimeType;
	}

	public OutputTypeHandler(String path) {
		if (!"no-data".equals(path)) {
			try {
				file = new File(path);
				file.createNewFile();
				file.deleteOnExit();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
	}

	public abstract void writeHeaderCell(String text, int col);

    /**
     * The row indexing is zero based, from the perspective of the row data,
     * ignoring how many rows were used for the header 
     */
	public abstract void writeCell(Object output, int col, int row);
    
    /**
     * The row indexing is zero based, from the perspective of the row data,
     * ignoring how many rows were used for the header 
     */
    public void writeFooterCell(Object output, int col, int row) {
        // Empty, instead of abstract, so we don't break any other classes 
        // which extend this
    }

	public abstract void flushFile();

	public File getFile() {
		return file;
	}

}
