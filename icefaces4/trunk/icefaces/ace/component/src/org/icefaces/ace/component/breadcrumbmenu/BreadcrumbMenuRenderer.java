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
package org.icefaces.ace.component.breadcrumbmenu;

import org.icefaces.ace.component.menu.AbstractMenu;
import org.icefaces.ace.component.menu.BaseMenuRenderer;
import org.icefaces.ace.component.menuitem.MenuItem;
import org.icefaces.render.MandatoryResourceComponent;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@MandatoryResourceComponent(tagName = "breadcrumbMenu", value = "org.icefaces.ace.component.breadcrumbmenu.BreadcrumbMenu")
public class BreadcrumbMenuRenderer extends BaseMenuRenderer {
    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        AbstractMenu menu = (AbstractMenu) component;
        if (menu.shouldBuildFromModel()) {
            menu.buildMenuFromModel();
        }
        BreadcrumbMenu breadcrumbMenu = (BreadcrumbMenu) component;

        ResponseWriter writer = context.getResponseWriter();
        String clientId = component.getClientId(context);
        String style = breadcrumbMenu.getStyle();
        String styleClass = breadcrumbMenu.getStyleClass();

        writer.startElement("div", component);
        writer.writeAttribute("id", clientId, "id");
        if (style != null) {
            writer.writeAttribute("style", style, "style");
        }
        writer.writeAttribute("class", "ui-breadcrumb-menu" + (styleClass == null ? "" : " " + styleClass), null);

        writer.startElement("ul", component);
        writer.writeAttribute("id", clientId + "_ul", null);

        List<UIComponent> children = component.getChildren();
        List<MenuItem> menuItems = new ArrayList<MenuItem>(children.size());
        for (UIComponent child : children) {
            if (child instanceof MenuItem) {
                menuItems.add((MenuItem) child);
            }
        }
        MenuItem menuItem;
        int size = menuItems.size(), last = size - 1;
        for (int i = 0; i < size; i++) {
            menuItem = menuItems.get(i);
            if (i == 0) {
                menuItem.setIcon("ui-icon ui-icon-home");
            } else {
                menuItem.setIcon("ui-icon ui-icon-arrowthick-1-e");
            }
            if (i == last) {
                menuItem.setDisabled(true);
            }
            writer.startElement("li", component);
            encodeMenuItem(context, menuItem);
            writer.endElement("li");
        }
        writer.endElement("ul");

        writer.startElement("script", component);
        writer.writeAttribute("type", "text/javascript", null);
        writer.write("ice.ace.create('BreadcrumbMenu',['" + clientId + "',{}]);//" + UUID.randomUUID());
        writer.endElement("script");

        writer.endElement("div");
    }

    @Override
    protected void encodeMarkup(FacesContext context, AbstractMenu abstractMenu) throws IOException {
    }

    @Override
    protected void encodeScript(FacesContext context, AbstractMenu abstractMenu) throws IOException {
    }
}
