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

package org.icefaces.tutorials;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class ChangingContentServlet extends HttpServlet {
    public void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        String content = processCookie(req, res);
        PrintWriter out = res.getWriter();
        out.println("Hello, world! - " + content);
        out.close();
    }

    protected static String processCookie(HttpServletRequest req, HttpServletResponse res) {
        StringBuilder letter = new StringBuilder();

        // Each view that references this servlet gives an identifier for itself as part of the uri
        final String requestURI = req.getRequestURI();
        final String uriPrefix = "changing-content/";
        int begin = requestURI.indexOf(uriPrefix) + uriPrefix.length();
        int end = requestURI.indexOf("/", begin);
        if (end < 0) {
            end = requestURI.length();
        }
        String iframeId = requestURI.substring(begin, end);

        // Using the iframe identifier, find the cookie that tracks what the last content was
        final String cookiePrefix = "ChangingContent." + req.getSession().getId();
        final String cookieName = cookiePrefix + "." + iframeId;
        Cookie[] cookies = req.getCookies();
        for (Cookie c : cookies) {
            if (cookieName.equals(c.getName())) {
                letter.append(c.getValue().trim());
                break;
            }
        }

        // Update the content to the next value to display, and update the cookie
        if (letter.length() == 0) {
            letter.append('A');
        }
        else {
            char cletter = letter.charAt(letter.length()-1);
            cletter++;
            if (cletter > 'Z') {
                letter.append('A');
            }
            else {
                letter.setCharAt(letter.length()-1, cletter);
            }
        }
        String content = letter.toString();
        res.addCookie(new Cookie(cookieName, content));
        return content;
    }
}
