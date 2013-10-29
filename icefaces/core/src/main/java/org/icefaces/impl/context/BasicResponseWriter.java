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

package org.icefaces.impl.context;

import javax.faces.component.UIComponent;
import javax.faces.context.ResponseWriter;
import javax.faces.context.ResponseWriterWrapper;
import java.io.IOException;
import java.io.Writer;

import org.icefaces.impl.util.DOMUtils;

public class BasicResponseWriter extends ResponseWriterWrapper {

    private Writer writer;
    boolean closeStart = false;
    private ResponseWriter wrapped;
    private String encoding;
    private String contentType;

    public BasicResponseWriter(Writer writer, String encoding,  String contentType)  {
        this.writer = writer;

        if (writer instanceof ResponseWriter) {
            wrapped = (ResponseWriter) writer;
        }

        this.encoding = (encoding != null) ? encoding: DOMResponseWriter.DEFAULT_ENCODING;
        this.contentType = (contentType != null) ? contentType: DOMResponseWriter.DEFAULT_TYPE;
    }

    public ResponseWriter getWrapped() {
        return wrapped;
    }

    public String getContentType()  {
        return contentType;
    }

    public String getCharacterEncoding()  {
        return encoding;
    }

    public void flush() throws IOException  {
        closeStartIfNecessary();
        writer.flush();
    }

    public void startDocument() throws IOException  {
    }

    public void endDocument() throws IOException {
    }

    public void startElement(String name, UIComponent component) throws IOException  {
        closeStartIfNecessary();
        writer.write('<');
        writer.write(name);
        closeStart = true;
    }

    public void endElement(String name) throws IOException  {
        closeStartIfNecessary();
        writer.write("</");
        writer.write(name);
        writer.write('>');
    }

    public void startCDATA() throws IOException  {
        closeStartIfNecessary();
        writer.write("<![CDATA[");
    }

    public void endCDATA() throws IOException  {
        writer.write("]]>");
    }

    public void writeAttribute(String name, Object value, String componentPropertyName) throws IOException  {
        writer.write(' ');
        writer.write(name);
        writer.write("=\"");
        writer.write(DOMUtils.escapeAttribute(String.valueOf(value)));
        writer.write("\"");
    }

    public void writeURIAttribute(String name, Object value, String componentPropertyName) throws IOException  {
        throw new UnsupportedOperationException("Implement writeURIAttribute");
    }

    public void writeComment(Object comment) throws IOException  {
        closeStartIfNecessary();
        //this may require escaping as well
        writer.write("<!--");
        writer.write(comment.toString());
        writer.write("-->");
    }

    public void writeText(Object text, UIComponent component, String componentPropertyName) throws IOException  {
        closeStartIfNecessary();
        writer.write(text.toString());
    }

    public void writeText(char[] text, int off, int len) throws IOException {
        closeStartIfNecessary();
        writer.write(text, len, off);
    }

    public void writeText(java.lang.Object text, java.lang.String property) throws IOException {
        closeStartIfNecessary();
        writer.write(text.toString());
    }


    public ResponseWriter cloneWithWriter(Writer writer)  {
        return new BasicResponseWriter(writer, getCharacterEncoding(), getContentType());
    }

    public void close() throws IOException  {
        closeStartIfNecessary();
        writer.close();
    }

    public void write(char[] chars, int offset, int length) throws IOException  {
        closeStartIfNecessary();
        writer.write(chars, offset, length);
    }

    public void write(String chars) throws IOException  {
        closeStartIfNecessary();
        writer.write(chars);
    }

    private void closeStartIfNecessary() throws IOException {
        if (closeStart)  {
            writer.write('>');
            closeStart = false;
        }
    }
}