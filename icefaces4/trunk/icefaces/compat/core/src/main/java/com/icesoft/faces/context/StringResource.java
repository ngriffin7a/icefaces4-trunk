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

package com.icesoft.faces.context;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

public class StringResource implements Resource, java.io.Serializable {
    private static final long serialVersionUID = 1204666667233178571L;
    private final Date lastModified = new Date();
    private String content;
    private String encoding;

    public StringResource(String content) {
        this(content, "UTF-8");
    }

    public StringResource(String content, String encoding) {
        this.content = content;
        this.encoding = encoding;
    }

    public String calculateDigest() {
        return content;
    }

    public Date lastModified() {
        return lastModified;
    }

    public InputStream open() throws IOException {
        return new ByteArrayInputStream(content.getBytes(encoding));
    }

    public void withOptions(Options options) throws IOException {
        options.setMimeType("text/plain; encoding=" + encoding);
    }
}
