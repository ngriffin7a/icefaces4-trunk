/*
 * Original Code Copyright Prime Technology.
 * Subsequent Code Modifications Copyright 2011-2012 ICEsoft Technologies Canada Corp. (c)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * NOTE THIS CODE HAS BEEN MODIFIED FROM ORIGINAL FORM
 *
 * Subsequent Code Modifications have been made and contributed by ICEsoft Technologies Canada Corp. (c).
 *
 * Code Modification 1: Integrated with ICEfaces Advanced Component Environment.
 * Contributors: ICEsoft Technologies Canada Corp. (c)
 *
 * Code Modification 2: [ADD BRIEF DESCRIPTION HERE]
 * Contributors: ______________________
 * Contributors: ______________________
 */
package org.icefaces.ace.component.menu;

import java.io.IOException;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UIParameter;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.icefaces.ace.component.menuitem.MenuItem;
import org.icefaces.ace.component.menucolumn.MenuColumn;
import org.icefaces.ace.component.multicolumnsubmenu.MultiColumnSubmenu;
import org.icefaces.ace.component.submenu.Submenu;
import org.icefaces.ace.component.menuseparator.MenuSeparator;
import org.icefaces.ace.renderkit.CoreRenderer;
import org.icefaces.ace.util.ComponentUtils;
import org.icefaces.ace.util.Utils;
import org.icefaces.impl.event.AjaxDisabledList;

import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.component.behavior.ClientBehaviorHolder;
import java.util.*;
import org.icefaces.ace.component.ajax.AjaxBehavior;

public abstract class BaseMenuRenderer extends CoreRenderer {

    @Override
	public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
		AbstractMenu menu = (AbstractMenu) component;

		if(menu.shouldBuildFromModel()) {
			menu.buildMenuFromModel();
		}

		encodeMarkup(context, menu);
		encodeScript(context, menu);
	}

    protected abstract void encodeMarkup(FacesContext context, AbstractMenu abstractMenu) throws IOException;

    protected abstract void encodeScript(FacesContext context, AbstractMenu abstractMenu) throws IOException;
	
	protected void encodeMenuItem(FacesContext context, MenuItem menuItem) throws IOException {
		encodeMenuItem(context, menuItem, false);
	}

    protected void encodeMenuItem(FacesContext context, MenuItem menuItem, boolean disabledParent) throws IOException {
		String clientId = menuItem.getClientId(context);
        ResponseWriter writer = context.getResponseWriter();
        String icon = menuItem.getIcon();

		if(menuItem.shouldRenderChildren()) {
			renderChildren(context, menuItem);
		}
        else {
            writer.startElement("a", null);
			if (menuItem.isDisabled() || disabledParent) {
				writer.writeAttribute("class", "ui-state-disabled", null);
			} else {
				String url = menuItem.getUrl();
				if(url != null) {
					writer.writeAttribute("href", getEncodedURL(context, menuItem.getUrlEncoding(), url, menuItem.getUrlParameters()), null);
					if(menuItem.getOnclick() != null) writer.writeAttribute("onclick", menuItem.getOnclick(), null);
					if(menuItem.getTarget() != null) writer.writeAttribute("target", menuItem.getTarget(), null);
				} else {
					writer.writeAttribute("style", "cursor:pointer;", null);

					UIComponent form = ComponentUtils.findParentForm(context, menuItem);
					if(form == null) {
						throw new FacesException("Menubar must be inside a form element");
					}

					String formClientId = form.getClientId(context);
					
					boolean hasAjaxBehavior = false;
					
					StringBuilder command = new StringBuilder();
					command.append("var self = this; setTimeout(function() { var f = function(opt){"); // dynamically set the id to the node so that it can be handled by the submit functions
					// ClientBehaviors
					Map<String,List<ClientBehavior>> behaviorEvents = menuItem.getClientBehaviors();
					if(!behaviorEvents.isEmpty()) {
						List<ClientBehaviorContext.Parameter> params = Collections.emptyList();
						for(Iterator<ClientBehavior> behaviorIter = behaviorEvents.get("activate").iterator(); behaviorIter.hasNext();) {
							ClientBehavior behavior = behaviorIter.next();
							if (behavior instanceof AjaxBehavior)
								hasAjaxBehavior = true;
							ClientBehaviorContext cbc = ClientBehaviorContext.createClientBehaviorContext(context, menuItem, "activate", clientId, params);
							String script = behavior.getScript(cbc);    //could be null if disabled

							if(script != null) {
								command.append("ice.ace.ab(ice.ace.extendAjaxArgs(");
								command.append(script);
								command.append(", opt));");
							}
						}
					}
					command.append("}; ");
					
					if (!hasAjaxBehavior && (menuItem.getActionExpression() != null || menuItem.getActionListeners().length > 0)) {
						command.append("self.id = '" + clientId + "'; ice.s(event, self");
						
						StringBuilder parameters = new StringBuilder();
						parameters.append(",function(p){");
						for(UIComponent child : menuItem.getChildren()) {
							if(child instanceof UIParameter) {
								UIParameter param = (UIParameter) child;
								
								parameters.append("p('");
								parameters.append(param.getName());
								parameters.append("','");
								parameters.append(String.valueOf(param.getValue()));
								parameters.append("');");
							}
						}
						parameters.append("});");
						
						command.append(parameters.toString());
					} else {
						command.append("f({node:self});"); // call behaviors function
					}
					
					command.append("}, 10);"); // close timeout

					String customOnclick = menuItem.getOnclick();
					String onclick = customOnclick == null ? command.toString() : customOnclick + ";" + command.toString();

					writer.writeAttribute("onclick", onclick, null);
				}
			}

            if(icon != null) {
                writer.startElement("span", null);
                writer.writeAttribute("class", icon + " wijmo-wijmenu-icon-left", null);
                writer.endElement("span");
            }

			if(menuItem.getValue() != null) {
                writer.startElement("span", null);
                String style = menuItem.getStyle();
                if (style != null && style.trim().length() > 0) {
                    writer.writeAttribute("style", style, "style");
                }
                Utils.writeConcatenatedStyleClasses(writer, "wijmo-wijmenu-text", menuItem.getStyleClass());
                writer.write((String) menuItem.getValue());
                writer.endElement("span");
            }

            writer.endElement("a");
		}
	}
	
	protected void encodeMenuSeparator(FacesContext context) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		writer.startElement("li", null);
		writer.endElement("li");
	}

    @Override
	public void encodeChildren(FacesContext facesContext, UIComponent component) throws IOException {
		//Do nothing
	}

    @Override
	public boolean getRendersChildren() {
		return true;
	}
	
	protected void encodeMultiColumnSubmenu(FacesContext context, MultiColumnSubmenu submenu) throws IOException {
		encodeMultiColumnSubmenu(context, submenu, false);
	}
	
	protected void encodeMultiColumnSubmenu(FacesContext context, MultiColumnSubmenu submenu, boolean isPlainMultiColumnMenu) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		String label = submenu.getLabel();
		String icon = submenu.getIcon();
		boolean disabled = submenu.isDisabled();
		
		List<MenuColumn> menuColumns = new ArrayList<MenuColumn>();
		for (UIComponent child : submenu.getChildren()) {
			if (child instanceof MenuColumn) menuColumns.add((MenuColumn) child);
		}

		if (!isPlainMultiColumnMenu) {
			writer.startElement("a", null);
			if (disabled) {
				writer.writeAttribute("class", "ui-state-disabled", null);
			} else {
				writer.writeAttribute("style", "cursor:pointer;", null);
			}
			
			if(icon != null) {
				writer.startElement("span", null);
				writer.writeAttribute("class", icon + " wijmo-wijmenu-icon-left", null);
				writer.endElement("span");
			}

			if(label != null) {
				writer.startElement("span", null);
				String style = submenu.getStyle();
				if (style != null && style.trim().length() > 0) {
					writer.writeAttribute("style", style, "style");
				}
				Utils.writeConcatenatedStyleClasses(writer, "wijmo-wijmenu-text", submenu.getStyleClass());
				writer.write(submenu.getLabel());
				writer.endElement("span");
			}

			writer.endElement("a");
		}
		
		if (disabled) return;
		
		int totalChildren = 0;
		Map<MenuColumn, ArrayList<ArrayList<UIComponent>>> columnMap = new HashMap<MenuColumn, ArrayList<ArrayList<UIComponent>>>();
		for (MenuColumn menuColumn : menuColumns) {
			columnMap.put(menuColumn, divideColumn(menuColumn));
			for (ArrayList<UIComponent> sublist : columnMap.get(menuColumn)) {
				totalChildren += sublist.size();
			}
		}
		int totalWidth = 0;
		for (MenuColumn menuColumn : menuColumns) {
			if (menuColumn.isRendered()) {
				int columnWidth = menuColumn.getWidth();
				int autoflow = menuColumn.getAutoflow();
				int multiplier = 1;
				if (autoflow > 0) {
					multiplier = totalChildren / autoflow;
					if ((totalChildren % autoflow) > 0) multiplier++;
				}
				totalWidth += (columnWidth * multiplier);
			}
		}
		
		if (isPlainMultiColumnMenu) {
			writer.startElement("ul", null);
		} else {
			writer.startElement("div", null);
		}
		writer.writeAttribute("class", "wijmo-wijmenu ui-menu-multicolumn", "class");
		writer.writeAttribute("style", "width: " + totalWidth + "px;", "style");
		Integer top = submenu.getPositionTop();
		if (top != null) {
			writer.writeAttribute("top", top.intValue(), null);
		}
		Integer left = submenu.getPositionLeft();
		if (left != null) {
			writer.writeAttribute("left", submenu.getPositionLeft(), null);
		}
		String relativeTo = submenu.getRelativeTo();
		if (relativeTo != null) {
			writer.writeAttribute("relativeto", relativeTo.toLowerCase(), null);
		}
		
		// header
		writer.startElement("div", null);
		writer.writeAttribute("style", "width: 100%;", null);
		String headerClass = submenu.getHeaderClass();
		headerClass = headerClass == null ? "" : " " + headerClass;
		writer.writeAttribute("class", "ui-menu-multicolumn-header" + headerClass, null);
		UIComponent header = (UIComponent) submenu.getFacet("header");
		if (header != null) {
			encodeParentAndChildren(context, header);
		} else {
			writer.startElement("div", null);
			writer.endElement("div");
		}
		writer.endElement("div");
			
		for (MenuColumn menuColumn : menuColumns) {
			if (menuColumn.isRendered()) {
				for (ArrayList<UIComponent> sublist : columnMap.get(menuColumn)) {
					writer.startElement("div", null);
					writer.writeAttribute("style", "width: " + menuColumn.getWidth() + "px;", "style");

					if(sublist.size() > 0) {
						writer.startElement("ul", null);

						boolean disabledSubmenu = false;
						for (UIComponent item : sublist) {
							if(item.isRendered()) {

								if(item instanceof MenuItem) {
									writer.startElement("li", null);
									encodeMenuItem(context, (MenuItem) item, disabledSubmenu);
									writer.endElement("li");
								} else if(item instanceof MenuSeparator) {
									encodeMenuSeparator(context);
								} else if(item instanceof Submenu) {
									Submenu sm = (Submenu) item;
									disabledSubmenu = sm.isDisabled();
									encodeFlatSubmenu(context, sm, disabledSubmenu);
								}
								
							}
						}

						writer.endElement("ul");
					}

					writer.endElement("div");
				}
			}
		}
		
		// footer
		writer.startElement("div", null);
		writer.writeAttribute("style", "width: 100%;", null);
		String footerClass = submenu.getFooterClass();
		footerClass = footerClass == null ? "" : " " + footerClass;
		writer.writeAttribute("class", "ui-menu-multicolumn-footer" + footerClass, null);
		UIComponent footer = (UIComponent) submenu.getFacet("footer");
		if (footer != null) {
			encodeParentAndChildren(context, footer);
		} else {
			writer.startElement("div", null);
			writer.endElement("div");
		}
		writer.endElement("div");
		
		if (isPlainMultiColumnMenu) {
			writer.endElement("ul");
		} else {
			writer.endElement("div");
		}
	}

    protected void encodeFlatSubmenu(FacesContext context, Submenu submenu, boolean disabled) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String label = submenu.getLabel();

        writer.startElement("li", null);
        writer.startElement("h3", null);
        if(label != null) {
			if (disabled) {
				writer.writeAttribute("class", "ui-state-disabled", null);
			} else {
				String style = submenu.getStyle();
				if (style != null && style.trim().length() > 0) {
					writer.writeAttribute("style", style, "style");
				}
				Utils.writeConcatenatedStyleClasses(writer, "", submenu.getStyleClass());
			}
            writer.write(label);
        }
        writer.endElement("h3");
        writer.endElement("li");
	}
	
	protected ArrayList<ArrayList<UIComponent>> divideColumn(MenuColumn menuColumn) {
	
		ArrayList<ArrayList<UIComponent>> columnList = new ArrayList<ArrayList<UIComponent>>();
		if (!menuColumn.isRendered()) return columnList;
		int maxRows = menuColumn.getAutoflow();
		if (maxRows == 0) {
			ArrayList<UIComponent> column = flattenContents(menuColumn, new ArrayList<UIComponent>());
			columnList.add(column);
		} else {
			ArrayList<UIComponent> children = flattenContents(menuColumn, new ArrayList<UIComponent>());
			
			if (children.size() <= maxRows) {
				columnList.add(children);
			} else {
				int rowCount = 0;
				ArrayList<UIComponent> currentColumn = new ArrayList<UIComponent>();
				for (UIComponent child : children) {
					rowCount++;
					currentColumn.add(child);
					if (rowCount == maxRows) {
						rowCount = 0;
						columnList.add(currentColumn);
						currentColumn = new ArrayList<UIComponent>();
					}
				}
				if (rowCount > 0) {
					columnList.add(currentColumn);
				}
			}
		}
		
		return columnList;
	}
	
	protected ArrayList<UIComponent> flattenContents(UIComponent component, ArrayList<UIComponent> result) {
		
		for (UIComponent child : component.getChildren()) {
			if (child.isRendered()) {
				if (child instanceof MenuItem || child instanceof MenuSeparator || child instanceof Submenu) {
					result.add(child);
					if (child instanceof Submenu) {
						flattenContents(child, result);
					}
				}
			}
		}
		
		return result;
	}
	
	public static void encodeParentAndChildren(FacesContext facesContext, UIComponent parent) throws IOException {
        parent.encodeBegin(facesContext);
        if (parent.getRendersChildren()) {
            parent.encodeChildren(facesContext);
        } else {
            if (parent.getChildCount() > 0) {
                Iterator children = parent.getChildren().iterator();
                while (children.hasNext()) {
                    UIComponent nextChild = (UIComponent) children.next();
                    if (nextChild.isRendered()) {
                        encodeParentAndChildren(facesContext, nextChild);
                    }
                }
            }
        }
        parent.encodeEnd(facesContext);
    }
}
