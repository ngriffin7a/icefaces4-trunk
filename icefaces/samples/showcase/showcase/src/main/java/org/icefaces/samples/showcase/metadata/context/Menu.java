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

package org.icefaces.samples.showcase.metadata.context;

import java.io.Serializable;
import java.util.ArrayList;

public class Menu<T> implements ContextBase, Serializable {

    private Class<T> parentClass;

    protected String title;
    protected MenuLink defaultExample;
    protected ArrayList<MenuLink> menuLinks;

   public Menu(Class<T> parentClass) {
        this.parentClass = parentClass;
        menuLinks = new ArrayList<MenuLink>();
    }

    public void initMetaData() {

        // check for the menu annotation and parse out any child menu links.
        if (parentClass.isAnnotationPresent(
                org.icefaces.samples.showcase.metadata.annotation.Menu.class)) {
            org.icefaces.samples.showcase.metadata.annotation.Menu menu =
                    parentClass.getAnnotation(org.icefaces.samples.showcase.metadata.annotation.Menu.class);
            title = menu.title();
            org.icefaces.samples.showcase.metadata.annotation.MenuLink[] menuExample = menu.menuLinks();
            MenuLink menuLink;
            for (org.icefaces.samples.showcase.metadata.annotation.MenuLink link : menuExample) {
                menuLink = new MenuLink(link.title(), link.isDefault(),
                        link.isNew(), link.isDisabled(), link.exampleBeanName());
                menuLinks.add(menuLink);
                if (menuLink.isDefault()){
                    defaultExample = menuLink;
                }
            }
        }
    }

    public ArrayList<MenuLink> getMenuLinks() {
        return menuLinks;
    }

    public MenuLink getDefaultExample() {
        return defaultExample;
    }

    public String getTitle() {
        return title;
    }

    public String getBeanName(){
        return null;
    }

}
