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

package org.icefaces.application.showcase.util;

import org.icefaces.application.showcase.view.bean.NavigationNames;

import javax.faces.context.ExternalContext;
import javax.faces.model.SelectItem;
import javax.faces.event.ValueChangeEvent;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.io.Serializable;

/**
 * <p>The StyleBean class is the backing bean which manages the demonstrations'
 * active theme.  There are currently two themes supported by the bean; XP and
 * Royale. </p>
 * <p/>
 * <p>The webpages' style attributes are modified by changing link in the header
 * of the HTML document.  The selectInputDate and tree components' styles are
 * changed by changing the location of their image src directories.</p>
 *
 * @since 0.3.0
 */
@ManagedBean
@SessionScoped
public class StyleBean implements Serializable {

    // possible theme choices
    private final String RIME = "rime";
    private final String XP = "xp";
    private final String ROYALE = "royale";

    // default theme
    protected String currentStyle = RIME;
    protected String tempStyle = RIME;

    // available style list
    protected ArrayList styleList;

    protected HashMap styleMap;

    /**
     * Creates a new instance of the StyleBean.
     */
    public StyleBean() {
        // initialize the style list
        styleList = new ArrayList();
        styleList.add(new SelectItem(RIME, RIME));
        styleList.add(new SelectItem(XP, XP));
        styleList.add(new SelectItem(ROYALE, ROYALE));

        styleMap = new HashMap(3);
        styleMap.put(RIME, new StylePath(
                "./xmlhttp/css/rime/rime.css",
                "/xmlhttp/css/rime/css-images/"));
        styleMap.put(XP, new StylePath(
                "./xmlhttp/css/xp/xp.css",
                "/xmlhttp/css/xp/css-images/"));
        styleMap.put(ROYALE, new StylePath(
                "./xmlhttp/css/royale/royale.css",
                "/xmlhttp/css/royale/css-images/"));
    }

    /**
     * Gets the current style.
     *
     * @return current style
     */
    public String getCurrentStyle() {
        return currentStyle;
    }

    /**
     * Sets the current style of the application to one of the predetermined
     * themes.
     *
     * @param currentStyle name of new style
     */
    public void setCurrentStyle(String currentStyle) {
        this.tempStyle = currentStyle;
    }

    /**
     * Gets the html needed to insert a valid css link tag.
     *
     * @return the tag information needed for a valid css link tag
     */
    public String getStyle() {
        return "<link rel=\"stylesheet\" type=\"text/css\" href=\"" +
                ((StylePath)styleMap.get(currentStyle)).getCssPath() + "\"/>";
    }

    /**
     * Gets the image directory to use for the selectinputdate and tree
     * theming.
     *
     * @return image directory used for theming
     */
    public String getImageDirectory() {
        return ((StylePath)styleMap.get(currentStyle)).imageDirPath;
    }

    /**
     * Applies temp style to to the current style and image directory and
     * manually refreshes the icons in the navigation tree. The page will reload
     * based on navigation rules to ensure the theme is applied; this is
     * necessary because of difficulties encountered by updating the stylesheet
     * reference within the <HEAD> of the document.
     *
     * @return the reload navigation attribute
     */
    public void changeStyle(ValueChangeEvent e) throws java.io.IOException{
        tempStyle = (String)e.getNewValue();
        if (!currentStyle.equalsIgnoreCase(tempStyle)) {
            currentStyle = tempStyle;
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            ec.redirect(ec.getRequestContextPath() + "/index.jsp");
        }
    }

    /**
     * Gets a list of available theme names that can be applied.
     *
     * @return available theme list
     */
    public List getStyleList() {
        return styleList;
    }

    /**
     * Utility class to manage different cssPath and imageDir namd
     */
    public class StylePath implements Serializable{

        private String cssPath;
        private String imageDirPath;

        public StylePath(String cssPath, String imageDirPath) {
            this.cssPath = cssPath;
            this.imageDirPath = imageDirPath;
        }

        public String getCssPath() {
            return cssPath;
        }

        public String getImageDirPath() {
            return imageDirPath;
        }
    }

}
