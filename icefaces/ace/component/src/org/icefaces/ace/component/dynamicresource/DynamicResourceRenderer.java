package org.icefaces.ace.component.dynamicresource;

import org.icefaces.ace.util.HTML;
import org.icefaces.util.CoreComponentUtils;
import org.w3c.dom.Element;

import javax.faces.application.Resource;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;
import java.io.IOException;

public class DynamicResourceRenderer extends Renderer {

    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent) throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = uiComponent.getClientId(facesContext);
        DynamicResource dynamicResource = (DynamicResource) uiComponent;
        boolean disabled = dynamicResource.isDisabled();
        Resource resource = dynamicResource.getResource();
        if (resource != null) {
            writer.startElement(HTML.DIV_ELEM, uiComponent);
            writer.writeAttribute(HTML.ID_ATTR, uiComponent.getClientId(facesContext) + "_cont", null);

            String label = dynamicResource.getLabel();
            if ("button".equals(dynamicResource.getType())) {
                writer.startElement(HTML.INPUT_ELEM, null);
                if (disabled) {
                    writer.writeAttribute(HTML.DISABLED_ATTR, HTML.DISABLED_ATTR, null);
                }
                writer.writeAttribute(HTML.TYPE_ATTR, "button", null);
                writer.writeAttribute(HTML.VALUE_ATTR, label, null);
                writer.writeAttribute(HTML.ONCLICK_ATTR, "window.open('" + resource.getRequestPath() + "');", null);
                writeStyleAttributes(dynamicResource, writer);
                writer.endElement(HTML.INPUT_ELEM);
            } else {
                if (disabled) {
                    writer.startElement(HTML.SPAN_ELEM, null);
                } else {
                    writer.startElement(HTML.ANCHOR_ELEM, null);
                    writer.writeAttribute(HTML.HREF_ATTR, resource.getRequestPath(), null);
                    String target = dynamicResource.getTarget();
                    if (target != null) {
                        writer.writeAttribute(HTML.TARGET_ATTR, target, null);
                    }
                }
                writer.writeAttribute(HTML.ID_ATTR, clientId, null);

                String image = dynamicResource.getImage();
                if (image != null) {
                    image = facesContext.getApplication().getResourceHandler().createResource(image).getRequestPath();
                    writer.startElement(HTML.IMG_ELEM, null);
                    writer.writeAttribute(HTML.SRC_ATTR, image, null);
                    writer.writeAttribute(HTML.ALT_ATTR, label, null);
                    writer.endElement(HTML.IMG_ELEM);
                } else {
                    writer.writeText(label, null);
                }

                writeStyleAttributes(dynamicResource, writer);
                writer.endElement(disabled ? HTML.SPAN_ELEM : HTML.ANCHOR_ELEM);
            }

            writer.endElement(HTML.DIV_ELEM);
        }
    }

    private void writeStyleAttributes(DynamicResource dynamicResource, ResponseWriter writer) throws IOException {
        String styleClass = dynamicResource.getStyleClass();
        if (styleClass != null) {
            writer.writeAttribute(HTML.CLASS_ATTR, styleClass, null);
        }
        String style = dynamicResource.getStyle();
        if (style != null) {
            writer.writeAttribute(HTML.STYLE_ATTR, style, null);
        }
    }
}
