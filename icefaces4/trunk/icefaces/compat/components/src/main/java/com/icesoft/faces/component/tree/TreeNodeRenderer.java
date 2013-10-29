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

package com.icesoft.faces.component.tree;

import com.icesoft.faces.component.util.CustomComponentUtils;
import com.icesoft.faces.context.DOMContext;
import com.icesoft.faces.renderkit.dom_html_basic.DomBasicRenderer;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.swing.tree.DefaultMutableTreeNode;
import java.io.IOException;
import java.util.Iterator;


/**
 * TreeNodeRenderer is an ICEfaces D2D renderer for the TreeNode component.
 */
public class TreeNodeRenderer extends DomBasicRenderer {


    /* (non-Javadoc)
     * @see com.icesoft.faces.renderkit.dom_html_basic.DomBasicRenderer#decode(javax.faces.context.FacesContext, javax.faces.component.UIComponent)
     */
    public void decode(FacesContext facesContext, UIComponent uiComponent) {
    }

    /* (non-Javadoc)
     * @see com.icesoft.faces.renderkit.dom_html_basic.DomBasicRenderer#encodeEnd(javax.faces.context.FacesContext, javax.faces.component.UIComponent)
     */
    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {

        DOMContext domContext =
                DOMContext.attachDOMContext(facesContext, uiComponent);

        validateParameters(facesContext, uiComponent, TreeNode.class);
        resetDescendentIds(uiComponent);
        UIComponent iconFacet = ((TreeNode) uiComponent).getIcon();
        UIComponent contentFacet = ((TreeNode) uiComponent).getContent();

        if (iconFacet != null && iconFacet.isRendered()) {
            CustomComponentUtils.renderChild(facesContext, iconFacet);
        }
        domContext.getCursorParent().appendChild(domContext.createTextNode(" "));
        if (contentFacet != null && contentFacet.isRendered()) {
            CustomComponentUtils.renderChild(facesContext, contentFacet);
        }

    }

    /**
     * Reset ids so the client id will be nullified and regenerated when
     * attachDOMContext is called. This is necessary so that each child
     * component of the TreeNode has a unique client id instead of using the
     * cached id. If the cached id is used, the DOMContext from the previous
     * node is reused which is totally incorrect. the same DOMContext and
     *
     * @param uiComponent
     */
    public static void resetDescendentIds(UIComponent uiComponent) {
        uiComponent.setId(uiComponent.getId());
        Iterator it = uiComponent.getFacetsAndChildren();
        while (it.hasNext()) {
            UIComponent next = (UIComponent) it.next();
            resetDescendentIds(next);
        }
    }
}
