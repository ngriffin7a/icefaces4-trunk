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

package com.icesoft.icefaces.tutorial.component.tree.links;

import com.icesoft.faces.component.tree.IceUserObject;

import javax.swing.tree.DefaultMutableTreeNode;
import java.io.Serializable;

/**
 * The UrlNodeUserObject object is responsible for storing extra data
 * for a url.  The url along with text is bound to a ice:commanLink object which
 * will launch a new browser window pointed to the url.
 */
public class UrlNodeUserObject extends IceUserObject implements Serializable {

    private static final long serialVersionUID = 3836607336402377527L;
    // url to show when a node is clicked
    private String url;

    private boolean selected;

    public UrlNodeUserObject(DefaultMutableTreeNode wrapper) {
        super(wrapper);
    }

    /**
     * Gets the url value of this IceUserObject.
     *
     * @return string representing a URL.
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets the URL.
     *
     * @param url a valid URL with protocol information such as
     *            http://icesoft.com
     */
    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        System.out.println("Setting value " + selected);
    }

}
