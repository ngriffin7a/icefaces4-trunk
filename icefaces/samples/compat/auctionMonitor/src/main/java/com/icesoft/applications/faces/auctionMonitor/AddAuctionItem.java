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

package com.icesoft.applications.faces.auctionMonitor;

import com.icesoft.applications.faces.auctionMonitor.beans.AuctionBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Hashtable;

/**
 * Class used to handle an auction item that is added to the current auction
 * monitor list This class will also handle parsing an items file into a usable
 * item list
 */
public class AddAuctionItem extends AuctionBean {
    private static Log log = LogFactory.getLog(AddAuctionItem.class);

    public static Hashtable parseFile(Reader reader) {
        BufferedReader in = new BufferedReader(reader);
        Hashtable params = new Hashtable();
        String line;
        try {
            String name;
            String value;
            while (null != (line = in.readLine())) {
                int index = line.indexOf(' ');
                name = line.substring(0, index);
                value = line.substring(index + 1);
                params.put(name, value);
            }
        } catch (IOException e) {
            if (log.isErrorEnabled()) {
                log.error(
                        "Error while parsing an auction item file into the hashtable");
            }
        }
        return params;
    }

    public static String getPictureName(String fileName) {
        int index = fileName.lastIndexOf('.');
        return (fileName.substring(0, index) + ".jpg");
    }

}
