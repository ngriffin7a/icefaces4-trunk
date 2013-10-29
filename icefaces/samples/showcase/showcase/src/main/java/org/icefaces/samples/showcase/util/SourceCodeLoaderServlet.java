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

package org.icefaces.samples.showcase.util;

import com.uwyn.jhighlight.renderer.XhtmlRendererFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>The SourceCodeLoaderServlet class is responsible for displaying the JSF
 * source code for a particular example. </p>
 *
 * @since 0.3.0
 */      

public class SourceCodeLoaderServlet extends HttpServlet implements Serializable{

    private static final Pattern JSPX_PATTERN =
            Pattern.compile("<!--.*?-->", Pattern.DOTALL);
    private static final Pattern JAVA_PATTERN =
            Pattern.compile("/\\*.*?\\*/", Pattern.DOTALL);

	private static SourceCodeLoaderServlet instance = null;

	public void init() {
		if (instance == null) instance = this;
	}

	public static SourceCodeLoaderServlet getServlet() {
		return instance;
	}

	public InputStream getSource(String path) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintWriter writer = new PrintWriter(baos);
		getSource(path, null, writer);
		byte[] bytes = baos.toByteArray();
		return new ByteArrayInputStream(bytes);
	}

    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        // Setting the context type to text/xml provides style
        // attributes for most browsers which should make reading
        // the code easier.
        response.setContentType("text/html");
        
        // Relative path to where the source code is on the server
        String sourcePath = request.getParameter("path");
        
        if (sourcePath != null) {
			getSource(sourcePath, response, null);
		}
	}
	
	public void getSource(String sourcePath, HttpServletResponse response, PrintWriter writer) { // either response or writer must be non-null
		InputStream sourceStream = getServletContext().getResourceAsStream(sourcePath);
				
		if (sourceStream == null) {
			try {
				// Work around for websphere
				sourceStream = new FileInputStream(new File(getServletContext().getRealPath(sourcePath)));
			} catch (Exception failedWorkaround) {
				failedWorkaround.printStackTrace();
			}
		}
		
		if (sourceStream != null) {
			PrintWriter responseStream = null;
			
			try {
				responseStream = response != null ? response.getWriter() : writer;
				
				// Pass the source stream to the response stream
				StringBuffer stringBuffer = new StringBuffer();
				int ch;
				while ((ch = sourceStream.read()) != -1) {
					stringBuffer.append((char) ch);
				}
				
				// Remove the license from the source code
				Matcher m = JSPX_PATTERN.matcher(stringBuffer);
				String toReturn = "";
				if (m.find(0)) {
					toReturn = m.replaceFirst("// MPL License text (see http://www.mozilla.org/MPL/)");
				} else {
					m = JAVA_PATTERN.matcher(stringBuffer);
					toReturn = m.replaceFirst("/* MPL License text (see http://www.mozilla.org/MPL/) */\n");
				}
				
				// Check the extension
				String name = sourcePath.substring(sourcePath.lastIndexOf("/") + 1);
				String type = "";
				if (sourcePath.endsWith(".java")) {
					type = XhtmlRendererFactory.JAVA;
				} else if (sourcePath.endsWith(".xhtml")) {
					type = XhtmlRendererFactory.XHTML;
				}
				
				// Highlight properly and print
				String highlight = XhtmlRendererFactory.getRenderer(type).highlight(name, toReturn, "utf8", false);
				responseStream.print(highlight);
			} catch (Exception failedWrite) {
				failedWrite.printStackTrace();
			} finally {
				// Close the source
				if (sourceStream != null) {
					try{
						sourceStream.close();
					} catch (Exception ignoredClose) { }
				}
				// Close the response
				if (responseStream != null) {
					try{
						responseStream.close();
					} catch (Exception ignoredClose) { }
				}
			}
		}
    }

}
