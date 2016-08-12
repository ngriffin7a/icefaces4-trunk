package org.icefaces.ace.component.colorpicker;


import org.icefaces.ace.renderkit.InputRenderer;
import org.icefaces.ace.util.HTML;
import org.icefaces.ace.util.JSONBuilder;
import org.icefaces.ace.util.Utils;
import org.icefaces.render.MandatoryResourceComponent;
import org.icefaces.util.CoreComponentUtils;
import org.icefaces.util.EnvUtils;
import org.icefaces.ace.util.ComponentUtils;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ActionEvent;
import javax.faces.event.PhaseId;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jguglielmin on 2016-07-25.
 */
@MandatoryResourceComponent(tagName="colorPicker", value="org.icefaces.ace.component.colorpicker.ColorPicker")
public class ColorPickerRenderer extends InputRenderer {
    @Override
     public void decode(FacesContext facesContext, UIComponent component) {
         ColorPicker picker = (ColorPicker) component;
         String clientId = picker.getClientId(facesContext);
         Map<String, String> params = facesContext.getExternalContext().getRequestParameterMap();
         if(picker.isDisabled() || picker.isReadonly()) {
                 return;
 		 }
         String submittedValue = params.get(clientId + "_input");
         if (!isValueEmpty(submittedValue)) {
             picker.setSubmittedValue(submittedValue);
         }
         decodeBehaviors(facesContext, component);
     }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        ColorPicker picker = (ColorPicker) component;
        ResponseWriter writer = context.getResponseWriter();
        String clientId = picker.getClientId(context);
        /* TODO aria support */
        boolean ariaEnabled = EnvUtils.isAriaEnabled(context);
        writer.startElement("span", component);
        writer.writeAttribute("id", clientId, "clientId");
        renderResetSettings(context, component);

        String styleClass = picker.getStyleClass();
        String defaultClass = themeForms() ? ColorPicker.THEME_INPUT_CLASS : ColorPicker.PLAIN_INPUT_CLASS;
        defaultClass += getStateStyleClasses(picker);
        Map<String, Object> labelAttributes = getLabelAttributes(component);
        labelAttributes.put("fieldClientId", clientId + "_input");

        writer.startElement("span", null);

        writeLabelAndIndicatorBefore(labelAttributes);

        writer.startElement("input", null);
        writer.writeAttribute("type", "text", null);
        writer.writeAttribute("id", clientId + "_input", null);
        writer.writeAttribute("data-ice-clear-ignore", "true", null);
        if (ariaEnabled) {
            writer.writeAttribute("role", "region", null);
        }
        ComponentUtils.enableOnElementUpdateNotify(writer, clientId);

        String embeddedLabel = null;
        String nameToRender = clientId + "_input";
        String valueToRender = null;

        boolean validity = picker.isValid();
    	//	if (picker.isValid() || picker.getSubmittedValue() == null) {
        if (picker.getSubmittedValue() == null || isValueEmpty(String.valueOf(picker.getSubmittedValue()))) {
    			valueToRender = picker.isRedisplay() ? ComponentUtils.getStringValueToRender(context, picker) : "";
    		} else {
    			valueToRender = (String) picker.getSubmittedValue();
    		}
            boolean hasLabel = (Boolean) labelAttributes.get("hasLabel");
            String labelPosition = (String) labelAttributes.get("labelPosition");
            String label = (String) labelAttributes.get("label");

            writer.writeAttribute("name", nameToRender, null);
            writer.writeAttribute("value", valueToRender , null);

            if (ariaEnabled) {
                final ColorPicker comp = (ColorPicker) component;
                Map<String, Object> ariaAttributes = new HashMap<String, Object>() {{
                    put("readonly", comp.isReadonly());
                    put("required", comp.isRequired());
                    put("disabled", comp.isDisabled());
                    put("invalid", !comp.isValid());
                }};
                writeAriaAttributes(ariaAttributes, labelAttributes);
            }

            if(picker.isDisabled()) writer.writeAttribute("disabled", "disabled", "disabled");
            if(picker.isReadonly()) writer.writeAttribute("readonly", "readonly", "readonly");
            String style = picker.getStyle();
            if(style != null) writer.writeAttribute("style", style, "style");
        //    String styleClass= picker.getStyleClass();
         //   Utils.writeConcatenatedStyleClasses(writer, defaultClass, styleClass);


        JSONBuilder jb = JSONBuilder.create();
        jb.beginFunction("ice.ace.create")
                .item("ColorPicker")
                .beginArray()
                .item(clientId)
                .beginMap()
                .beginMap("options")
                .entry("preferredFormat", picker.getPreferredFormat().getValue())
                .entry("showPalette", picker.isShowPalette())
                .entry("showPaletteOnly", picker.isShowPaletteOnly())
                .entry("showInitial", picker.isShowInitial())
                .entry("showInput", picker.isShowInput())
                .entry("showButtons", picker.isShowButtons())
                .entry("disabled", picker.isDisabled())
                .entry("chooseText", picker.getChooseText())
                .entry("disabled", picker.isDisabled())
                .entry("cancelText", picker.getCancelButtonText())
                .entry("maxSelectionSize", picker.getMaxSelectionSize())
                .entry("showSelectionPalette", picker.isShowSelectionPalette());

        if (picker.getColor() !=null) {
            jb.entry("color", picker.getColor());
        }
        jb.endMap();

        encodeClientBehaviors(context, picker, jb);

        jb.endMap().endArray().endFunction();
        String script = jb.toString() + "});";
        writer.endElement("input");
        writeLabelAndIndicatorAfter(labelAttributes);
        writer.endElement("span");
        writer.startElement("script", null);
        writer.writeAttribute("type", "text/javascript", null);
        writer.write("ice.ace.jq(function() {");
        writer.write(script);
        writer.endElement("script");

        writer.startElement("span", null);
        writer.writeAttribute("style", "display:none;", null);
        writer.writeAttribute("data-hashcode", script.hashCode(), null);
        writer.endElement("span");

        writer.endElement("span");
    }

    protected void renderResetSettings(FacesContext context, UIComponent component) throws IOException {
    		ResponseWriter writer = context.getResponseWriter();
    		ColorPicker picker = (ColorPicker) component;

    		String clientId = picker.getClientId(context);
    		String labelPosition = (String) component.getAttributes().get("labelPosition");

    		JSONBuilder jb = JSONBuilder.create();
    		jb.beginArray();
    		jb.item("ColorPicker");
    		jb.beginArray();
    		jb.item(clientId);

    		jb.endArray();
    		jb.endArray();

    		writer.writeAttribute("data-ice-reset", jb.toString(), null);
    }
}
