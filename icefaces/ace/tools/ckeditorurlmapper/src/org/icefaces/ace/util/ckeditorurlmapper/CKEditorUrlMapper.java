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

package org.icefaces.ace.util.ckeditorurlmapper;

import java.io.*;
import java.util.*;

public class CKEditorUrlMapper {

    private static final String LIB_NAME = "icefaces.ace"; // name of the JSF library that contains the CKEditor resources
	private static final String LIB_ABSOLUTE_PATH = "***"; // absolute path in your file system to the root directory of JSF library that contains the CKEditor resources (e.g. C:/ice/repo/3.0/ace/component/resources/icefaces.ace/)
    private static final String CKEDITOR_ROOT_DIR = "richtextentry/ckeditor/"; // root directory of the original CKEditor resources
    private static final String CKEDITOR_MAPPING_JS = "ckeditor.mapping.js"; // the name of the file to be produced

	public static void main(String[] args) {
	
		String CKEDITOR_RESOURCES_LIST_FILE = args[0];

		ArrayList<String> cssResources = new ArrayList();
        ArrayList<String> imageResources = new ArrayList();
        ArrayList<String> allResources = new ArrayList();
		
		try {
			// collect resource relative paths
			File resources = new File(CKEDITOR_RESOURCES_LIST_FILE);
			InputStream in = new FileInputStream(resources);
			String resourceList = new String(readIntoByteArray(in), "UTF-8");
			String[] paths = resourceList.split(" ");
			for (int i = 0; i < paths.length; i++) {
				String localPath = paths[i];
				if (localPath.endsWith(".css")) {
					cssResources.add(localPath);
				} else if (localPath.endsWith(".jpg") || localPath.endsWith(".gif") || localPath.endsWith(".png")) {
					imageResources.add(localPath);
				} else {
					allResources.add(localPath);
				}
			}

			// rewrite relative request paths in CSS files
			Iterator<String> i = cssResources.iterator();
			while (i.hasNext()) {
				String css = i.next();
				File file = new File(css);
				InputStream fis = new FileInputStream(file);
				String content = new String(readIntoByteArray(fis), "UTF-8");
				
				String dir = toRelativeFolderPath(toRelativeLocalDir(css));

				Iterator<String> ri = imageResources.iterator();
				while (ri.hasNext()) {
					String entry = ri.next();
					String path = toRelativeFolderPath(entry);
					if (path.startsWith(dir)) {
						String relativePath = path.substring(dir.length() + 1);
						String requestPath = "\"#{resource['" + LIB_NAME + ":" + toRelativeRequestPath(entry) + "']}\"";
						content = content.replaceAll(relativePath, requestPath);
					}
				}
				
				// replace CSS file
				PrintWriter o = new PrintWriter(css);
				o.println(content);
				o.close();
			}
			
			
			// create mapping js file
			allResources.addAll(cssResources);
			allResources.addAll(imageResources);

			StringBuffer code = new StringBuffer();
			code.append("window.CKEDITOR_GETURL = function(r) { var mappings = [");
			Iterator<String> entries = allResources.iterator();
			while (entries.hasNext()) {
				String next = entries.next();
				code.append("{i: \"");
				code.append(toRelativeFolderPath(next));
				code.append("\", o: \"");
				code.append("#{resource['" + LIB_NAME + ":" + toRelativeRequestPath(next) + "']}");
				code.append("\"}");
				if (entries.hasNext()) {
					code.append(",");
				}
			}
			code.append("]; if (r.indexOf('://') > -1) { var i = document.location.href.lastIndexOf('/'); r = r.substring(i + 1); }; for (var i = 0, l = mappings.length; i < l; i++) { var m = mappings[i]; if (m.i == r) { return m.o;} } return false; };");

			PrintWriter out = new PrintWriter(LIB_ABSOLUTE_PATH + CKEDITOR_ROOT_DIR + CKEDITOR_MAPPING_JS);
			out.println(code.toString());
			out.close();
		} catch (Exception e) {
		
		}
    }

    private static byte[] readIntoByteArray(InputStream in) throws IOException {
        byte[] buffer = new byte[4096];
        int bytesRead;
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        while ((bytesRead = in.read(buffer)) != -1) {
            out.write(buffer, 0, bytesRead); // write
        }
        out.flush();

        return out.toByteArray();
    }

    private static String toRelativeFolderPath(String path) {
        return path.substring(path.indexOf(CKEDITOR_ROOT_DIR) + CKEDITOR_ROOT_DIR.length());
    }

    private static String toRelativeRequestPath(String path) {
        return path.substring(path.indexOf(CKEDITOR_ROOT_DIR));
    }

    private static String toRelativeLocalDir(String localPath) {
        int position = localPath.lastIndexOf("/");
        return CKEDITOR_ROOT_DIR.length() > position ? "/" : localPath.substring(CKEDITOR_ROOT_DIR.length(), position);
    }
}
