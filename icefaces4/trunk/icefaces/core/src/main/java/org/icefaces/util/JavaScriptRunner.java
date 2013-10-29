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

package org.icefaces.util;

import javax.faces.context.FacesContext;
import java.util.*;

/**
 * Utility API for evaluating Javascript code on the client.
 */
public class JavaScriptRunner {

    /**
     * Send immediately Javascript code to the client for evaluation and execution.
     *
     * @param context the current FacesContext
     * @param script  the Javascript code
     */
    public static void runScript(FacesContext context, String script) {
        Map requestMap = context.getExternalContext().getRequestMap();
        Collection scripts = (Collection) requestMap.get(JavaScriptRunner.class.getName());
        if (scripts == null) {
            scripts = new ArrayList();
            requestMap.put(JavaScriptRunner.class.getName(), scripts);
        }

        scripts.add(escapeJavaScript(script));
    }

    public static String collateScripts(FacesContext context) {
        Map requestMap = context.getExternalContext().getRequestMap();
        Collection scripts = (Collection) requestMap.get(JavaScriptRunner.class.getName());
        if (scripts == null) {
            return "";
        } else {
            StringBuffer buffer = new StringBuffer();
            Iterator i = scripts.iterator();
            while (i.hasNext()) {
                String script = ((String) i.next()).trim();
                buffer.append(script);
                if (!script.endsWith(";")) {
                    buffer.append(";");
                }
            }

            return buffer.toString();
        }
    }

    public static String escapeJavaScript(String str) {
        if (str == null) {
            return null;
        }

        StringBuffer writer = new StringBuffer();

        int size = str.length();
        for (int i = 0; i < size; i++) {
            final char ch = str.charAt(i);
            switch (ch) {
                case '\b':
                    writer.append('\\');
                    writer.append('b');
                    break;
                case '\n':
                    writer.append('\\');
                    writer.append('n');
                    break;
                case '\t':
                    writer.append('\\');
                    writer.append('t');
                    break;
                case '\f':
                    writer.append('\\');
                    writer.append('f');
                    break;
                case '\r':
                    writer.append('\\');
                    writer.append('r');
                    break;
                default:
                    writer.append(ch);
                    break;
            }
        }

        return writer.toString();
    }
}
