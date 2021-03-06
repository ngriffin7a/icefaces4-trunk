/*
 * Copyright 2004-2014 ICEsoft Technologies Canada Corp.
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

package org.icefaces.mobi.component.dataview;

import org.icefaces.impl.util.DOMUtils;
import org.icefaces.ace.component.textentry.TextEntry;
import org.icefaces.ace.util.ComponentUtils;
import org.icefaces.ace.util.HTML;
import org.icefaces.mobi.model.dataview.DataViewColumnModel;
import org.icefaces.mobi.model.dataview.DataViewColumnsModel;
import org.icefaces.mobi.model.dataview.DataViewDataModel;
import org.icefaces.mobi.model.dataview.IndexedIterator;
import org.icefaces.mobi.renderkit.CoreRenderer;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.component.*;
import javax.faces.component.html.*;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.io.OptionalDataException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;

public class DataViewRenderer extends CoreRenderer {
    private static final Logger logger = Logger.getLogger(DataViewRenderer.class.getName());

	public void decode(FacesContext facesContext, UIComponent uiComponent) {
		Map requestMap = facesContext.getExternalContext().getRequestParameterMap();
		Object sourceId = requestMap.get("ice.event.captured");
		if (sourceId != null && sourceId.toString().equals(uiComponent.getClientId(facesContext))) { 
			decodeBehaviors(facesContext, (DataView) uiComponent);
		}
	}

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        DataView dataView = (DataView) component;
        String dvId = dataView.getClientId();
        ResponseWriter writer = context.getResponseWriter();

        writer.startElement(HTML.DIV_ELEM, component);
        writer.writeAttribute(HTML.ID_ATTR, dvId, null);

        String styleClass = DataView.DATAVIEW_CLASS;
        String userClass = dataView.getStyleClass();
        if (userClass != null) styleClass += " " + userClass;
        writer.writeAttribute(HTML.CLASS_ATTR, styleClass, null);

        String userStyle = dataView.getStyle();
        if (userStyle != null)
            writer.writeAttribute(HTML.STYLE_ATTR, userStyle, null);
    }

    @Override
    public void encodeChildren(FacesContext context,
                               UIComponent component) throws IOException {
        DataView dataView = (DataView) component;
        ResponseWriter writer = context.getResponseWriter();

        encodeDetails(context, writer, dataView);
        encodeColumns(context, writer, dataView);
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();

        encodeScript(context, writer, (DataView)component);

        writer.endElement(HTML.DIV_ELEM);
    }

    private void encodeScript(FacesContext context, ResponseWriter writer, DataView dataView) throws IOException {
        String dvId = dataView.getClientId();

        writer.startElement(HTML.SPAN_ELEM, null);
        writer.writeAttribute(HTML.ID_ATTR, dvId + "_jswrp", null);

        writer.startElement(HTML.SCRIPT_ELEM, null);
        writer.writeAttribute(HTML.TYPE_ATTR, HTML.SCRIPT_TYPE_TEXT_JAVASCRIPT, null);

		String activationMode = dataView.getClientBehaviors().isEmpty() ? "client" : "server";
        String cfg = "{";
        cfg += "active:'" + activationMode + "'";
        cfg += ",disabled:" + dataView.isDisabled();
		cfg += encodeClientBehaviors(context, dataView, "action").toString();
        cfg += "}";

        String js =
            "ice.mobi.dataView.create("
                + '"' + dvId + '"'
                + ", " + cfg
            + ");";

        writer.writeText(js, null);
        writer.endElement(HTML.SCRIPT_ELEM);
        writer.endElement(HTML.SPAN_ELEM);
    }

    private void encodeColumns(FacesContext context,
                               ResponseWriter writer,
                               DataView dataView) throws IOException {
        DataViewColumns columns = dataView.getColumns();
        String var = dataView.getVar();

        if (columns == null) encodeEmptyBodyTable(writer);
        else {
            writer.startElement(HTML.DIV_ELEM, null);
            writer.writeAttribute(HTML.CLASS_ATTR, DataView.DATAVIEW_MASTER_CLASS, null);

            DataViewColumnsModel columnModel = columns.getModel();
            DataViewDataModel dataModel = dataView.getDataModel();

            if (columnModel.hasHeaders())
                encodeHeaders(writer, columnModel, dataModel, true);

            encodeRows(context, writer, dataView, var, columnModel, dataModel);

            if (columnModel.hasFooters())
                encodeFooters(writer, columnModel, dataModel, true);

            writer.endElement(HTML.DIV_ELEM);
        }
    }

    private void encodeEmptyBodyTable(ResponseWriter writer) throws IOException {
        writer.startElement(HTML.DIV_ELEM, null);
        writer.writeAttribute(HTML.CLASS_ATTR, DataView.DATAVIEW_MASTER_CLASS, null);
        writer.startElement(HTML.DIV_ELEM, null);
        writer.startElement(HTML.TABLE_ELEM, null);
        writer.writeAttribute(HTML.CLASS_ATTR, DataView.DATAVIEW_BODY_CLASS, null);
        writer.endElement(HTML.TABLE_ELEM);
        writer.endElement(HTML.DIV_ELEM);
        writer.endElement(HTML.DIV_ELEM);
    }

    private void encodeHeaders(ResponseWriter writer,
                               DataViewColumnsModel columnModel,
                               DataViewDataModel dataModel,
                               boolean writeTable) throws IOException {
        /* Skip table when writing duplicate alignment header */
        if (writeTable) {
            writer.startElement(HTML.TABLE_ELEM, null);
            writer.writeAttribute(HTML.CLASS_ATTR, DataView.DATAVIEW_HEAD_CLASS, null);
        }

        writer.startElement(HTML.THEAD_ELEM, null);
        writer.startElement(HTML.TR_ELEM, null);
        writer.writeAttribute(HTML.CLASS_ATTR, DataView.DATAVIEW_HEADER_ROW_CLASS, null);

        for (IndexedIterator<DataViewColumnModel> columnIter = columnModel.iterator(); columnIter.hasNext();) {
            DataViewColumnModel column = columnIter.next();
            int index = columnIter.getIndex();

            if (column.isRendered()) {
                writer.startElement(HTML.TH_ELEM, null);

                String className = getColumnStyleClass(column, index);

                writer.writeAttribute(HTML.CLASS_ATTR, className, null);

                if (column.getHeaderText() != null)
                    writer.write(column.getHeaderText());

                if (column.isSortable()) {
                    writer.startElement("i", null);
                    writer.writeAttribute(HTML.CLASS_ATTR, DataView.DATAVIEW_SORT_INDICATOR_CLASS, null);
                    writer.endElement("i");
                }
                writer.endElement(HTML.TH_ELEM);
            }
        }

        writer.endElement(HTML.TR_ELEM);
        writer.endElement(HTML.THEAD_ELEM);

        if (writeTable) writer.endElement(HTML.TABLE_ELEM);
    }

    private void encodeFooters(ResponseWriter writer,
                               DataViewColumnsModel columnModel,
                               DataViewDataModel dataModel, boolean writeTable) throws IOException {
        if (writeTable) {
            writer.startElement(HTML.TABLE_ELEM, null);
            writer.writeAttribute(HTML.CLASS_ATTR, DataView.DATAVIEW_FOOT_CLASS, null);
        }

        writer.startElement(HTML.TFOOT_ELEM, null);
        writer.startElement(HTML.TR_ELEM, null);

        for (IndexedIterator<DataViewColumnModel> columnIter = columnModel.iterator(); columnIter.hasNext();) {
            DataViewColumnModel column = columnIter.next();
            int index = columnIter.getIndex();

            if (column.isRendered()) {
                writer.startElement(HTML.TD_ELEM, null);

                String className = getColumnStyleClass(column, index);

                writer.writeAttribute(HTML.CLASS_ATTR, className, null);

                if (column.getFooterText() != null)
                    writer.write(column.getFooterText());
                writer.endElement(HTML.TD_ELEM);
            }
        }


        writer.endElement(HTML.TR_ELEM);
        writer.endElement(HTML.TFOOT_ELEM);

        if (writeTable) writer.endElement(HTML.TABLE_ELEM);
    }

    private String getColumnStyleClass(DataViewColumnModel column, int index) {
        String colStyleClass = column.getStyleClass();
        String className = DataView.DATAVIEW_COLUMN_CLASS + "-" + index;

        if (colStyleClass != null) className += " " + colStyleClass;
        return className;
    }

    private void encodeRows(FacesContext context,
                            ResponseWriter writer,
                            DataView dataView,
                            String var,
                            DataViewColumnsModel columnModel,
                            DataViewDataModel dataModel) throws IOException {
        String dvId = dataView.getClientId();
        ELContext elContext = context.getELContext();
        Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
        String clientId = dvId;
        String bodyClass = DataView.DATAVIEW_BODY_CLASS;

        if (dataView.isRowStroke()) bodyClass += " stroke";
        if (dataView.isRowStripe()) bodyClass += " stripe";

        writer.startElement(HTML.DIV_ELEM, null);
        writer.writeAttribute(HTML.CLASS_ATTR, "overthrow", null);
        writer.startElement(HTML.TABLE_ELEM, null);
        writer.writeAttribute(HTML.CLASS_ATTR, bodyClass, null);

        if (columnModel.hasHeaders()) encodeHeaders(writer, columnModel, dataModel, false);

        writer.startElement(HTML.TBODY_ELEM, null);

        List<UIComponent> detailHolders = getDetailHolders(dataView.getDetails());
        String rowIndexVar = dataView.getRowIndexVar();
        Integer activeRowIndex = dataView.getActiveRowIndex();
        int selectedRowIndex = activeRowIndex == null ? -1 : activeRowIndex;
        for (IndexedIterator<Object> dataModelIterator = dataModel.iterator(); dataModelIterator.hasNext();) {
            Object rowData = dataModelIterator.next();
            int index = dataModelIterator.getIndex();
            // Init row context
            requestMap.put(var, rowData);
            if (rowIndexVar != null)
                requestMap.put(rowIndexVar, index);

            writer.startElement(HTML.TR_ELEM, null);

            writer.writeAttribute(HTML.ID_ATTR, clientId + "_" + dataModelIterator.getIndex(), null);
            writer.writeAttribute("data-index", index, null);
            if (index == selectedRowIndex) {
                writer.writeAttribute("class", "ui-state-active", null);
            }
            if (dataView.getClientBehaviors().isEmpty())
                writer.writeAttribute("data-state", encodeRowDetailString(context, dvId, detailHolders), null);

            for (IndexedIterator<DataViewColumnModel> columnModelIterator = columnModel.iterator(); columnModelIterator.hasNext();)
                writeColumn(writer, elContext, columnModelIterator.next(), columnModelIterator.getIndex(), dataView);

            writer.endElement(HTML.TR_ELEM);
        }

        requestMap.remove(var);
        if (rowIndexVar != null)
            requestMap.remove(rowIndexVar);

        writer.endElement(HTML.TBODY_ELEM);

        if (columnModel.hasFooters()) encodeFooters(writer, columnModel, dataModel, false);

        writer.endElement(HTML.TABLE_ELEM);
        writer.endElement(HTML.DIV_ELEM);
    }

    private void writeColumn(ResponseWriter writer, ELContext elContext, DataViewColumnModel column, int index, DataView dataView) throws IOException {
        if (!column.isRendered()) return;

        ValueExpression ve = column.getValueExpression();
        Object value = ve == null ? column.getValue() : ve.getValue(elContext); // use value expression if available, value will have been pre-evaluated
        String type = column.getType();
		boolean escape = column.isEscape();
        String colClass = getColumnStyleClass(column, index);

        writer.startElement(HTML.TD_ELEM, null);

        if (type.equals("bool")) {
            colClass += " " + DataView.DATAVIEW_BOOL_COLUMN_CLASS;
            writer.startElement("i", null);
            if (value != null) {
                Boolean bval = (Boolean)value;
                String resUrl;
                if (bval) resUrl = "fa fa-check-square-o";
                else resUrl = "fa fa-square-o";

                writer.writeAttribute(HTML.CLASS_ATTR, resUrl, null);
            }
            writer.endElement("i");
        }
        else if (type.equals("date")) {
            writer.write(getDateFormat(column).format(value));
        }
        else if (type.equals("image")) {
            writer.startElement(HTML.IMG_ELEM, null);
            if (value != null) writer.writeAttribute(HTML.SRC_ATTR, value.toString(), null);
            writer.endElement(HTML.IMG_ELEM);
        }
        else if (type.equals("list")) {
            writer.startElement(HTML.UL_ELEM, null);
            if (value != null && value instanceof List)
                for (Object i : (List)value) {
                    writer.startElement(HTML.LI_ELEM, null);
					if (escape) writer.writeText(i.toString(), dataView, null);
                    else writer.write(i.toString());
                    writer.endElement(HTML.LI_ELEM);
                }
            writer.endElement(HTML.UL_ELEM);
        }
        else if (value != null) {
			if (escape) writer.writeText(value.toString(), dataView, null);
			else writer.write(value.toString());
		}

        writer.writeAttribute(HTML.CLASS_ATTR, colClass, null);

        writer.endElement(HTML.TD_ELEM);
    }

    private DateFormat getDateFormat(DataViewColumnModel column) {
        String pattern = column.getDatePattern();
        String type = column.getDateType();
        TimeZone timeZone = column.getTimeZone();
        Locale locale = column.getLocale();
        Integer dateStyle = column.getTimeStyle();
        Integer timeStyle = column.getDateStyle();

        if (pattern == null && type == null) {
            throw new IllegalArgumentException("Either pattern or type must be specified.");
        }

        DateFormat df;
        if (pattern != null) {
            df = new SimpleDateFormat(pattern, locale);
        } else if (type.equals("both")) {
            df = DateFormat.getDateTimeInstance(dateStyle, timeStyle, locale);
        } else if (type.equals("date")) {
            df = DateFormat.getDateInstance(dateStyle, locale);
        } else if (type.equals("time")) {
            df = DateFormat.getTimeInstance(timeStyle, locale);
        } else {
            // PENDING(craigmcc) - i18n
            throw new IllegalArgumentException("Invalid type: " + type);
        }
        df.setLenient(false);
        df.setTimeZone(timeZone);

        return (df);
    }

    private String encodeRowDetailString(FacesContext context, String dvId, List<UIComponent> detailHolders) {
        StringBuilder detStr = new StringBuilder();

        for (Iterator<UIComponent> valueHolderIterator = detailHolders.iterator();
                valueHolderIterator.hasNext();) {
            appendUpdateString(dvId, detStr, context, valueHolderIterator);
        }

        return DOMUtils.escapeAnsi(detStr.toString());
    }

    private void appendUpdateString(String dvId, StringBuilder detStr, FacesContext context, Iterator<UIComponent> valueHolderIterator) {
        String cId = dvId + UINamingContainer.getSeparatorChar(context);
        ELContext elContext = context.getELContext();
        UIComponent vhComponent = valueHolderIterator.next();
        /* getValueExpressions - point for optimization */
        Set<String> propertyNames = getPropNames(vhComponent);
        boolean first = true;

        for (Iterator<String> propIterator = propertyNames.iterator(); propIterator.hasNext();) {
            String value;
            String prop = propIterator.next();
            ValueExpression ve = vhComponent.getValueExpression(prop);

            if (ve == null) continue; /* If component property isn't dynamic don't record its state */

            if (!first) detStr.append("|");

            if (vhComponent instanceof ValueHolder && "value".equals(prop)) {
                value = ComponentUtils.getStringValueToRender(context, vhComponent);
            } else {
                value = ve.getValue(elContext).toString();
            }

            // TOOD : More detailed conversion to string form
            // TODO : Detail id caching
            // TODO : '|' & '=' escaping

            // Write Target Id
            detStr.append(vhComponent.getClientId().replaceFirst(cId,"")).append("=");
            // Write Update Directive
            detStr.append(getDirective(vhComponent, prop)).append("=");
            // Write Update Value
            detStr.append(value); /* may change as directives evolve */

            first = false;
        }

        // If we wrote something and there are still more valueHolders
        if (!first && valueHolderIterator.hasNext()) detStr.append("|");
    }

    private static final HashSet emptySet = new HashSet();
    private static final HashSet mobiTextEntryProperties = new HashSet() {{
        add("value"); add("type"); add("placeholder"); add("readonly"); add("maxlength");
        add("size"); add("required"); add("results"); add("title"); add("min"); add("max");
        add("step"); add("disabled"); add("style"); add("styleClass");
    }};
    private static final HashSet uiCommandProperties = new HashSet() {{ add("value"); }};
    private static final HashSet uiInputProperties = new HashSet() {{ add("value"); }};
    private static final HashSet uiOutputProperties = new HashSet() {{ add("value"); }};

    private static final List<Class> htmlValueHolders = new ArrayList<Class>() {{
        add(HtmlInputTextarea.class);
        add(HtmlOutputText.class);
        add(HtmlOutputLabel.class);
        add(HtmlCommandLink.class);
    }};

    private static final List<Class> attrValueHolders = new ArrayList<Class>() {{
        add(HtmlInputText.class);
        add(HtmlInputSecret.class);
        add(HtmlInputText.class);
        add(HtmlCommandButton.class);
        add(TextEntry.class);
    }};

    private boolean instanceOf(Object x, List<Class> y) {
        Class xc = x.getClass();

        for (Class c : y)
            if (c.isInstance(x)) return true;

        return false;
    }

    private Set<String> getPropNames(UIComponent vhComponent) {
        if (vhComponent instanceof TextEntry) return mobiTextEntryProperties;
        if (vhComponent instanceof UICommand) return uiCommandProperties;
        if (vhComponent instanceof UIInput) return uiInputProperties;
        if (vhComponent instanceof UIOutput) return uiOutputProperties;

        return emptySet;
    }

    private String getDirective(UIComponent c, String propertyName) {
        if (propertyName == "value") {
            if (instanceOf(c, htmlValueHolders) || isHtmlValueHolder(c)) {
                return "html"; /* swap inner html */
            } else if (instanceOf(c, attrValueHolders)) {
                return "attr=value";
            } else if (c instanceof HtmlSelectBooleanCheckbox) {
                return "attr=checked";
            }
        }

        /* if no specified directive set as prop as dom attr */
        return "attr="+propertyName;
    }

    /* Check if component is an html value holder by configuration */
    private boolean isHtmlValueHolder(UIComponent c) {
        /* Only inspect specific component types */
        if (c instanceof TextEntry) {
            TextEntry textEntry = (TextEntry)c;
            if (textEntry.getType().equals("textarea"))
                return true;
        }

        return false;
    }

    private void encodeDetails(FacesContext context,
                               ResponseWriter writer,
                               DataView dataView) throws IOException {
        String dvId = dataView.getClientId();
        DataViewDetails details = dataView.getDetails();

        // Init row context
        Integer index = dataView.getActiveRowIndex();
        String var = dataView.getVar();
        String rowIndexVar = dataView.getRowIndexVar();
        String activationMode = dataView.getClientBehaviors().isEmpty() ? "client" : "server";
        boolean active = "client".equals(activationMode) || ("server".equals(activationMode) && index != null && index >= 0);
        Map<String, Object> requestMap = context.getExternalContext().getRequestMap();

        if (index != null && index >= 0) {
            DataViewDataModel dataModel = dataView.getDataModel();
            requestMap.put(var, dataModel.getDataByIndex(index));
            if (rowIndexVar != null) requestMap.put(rowIndexVar, index);
        }

        // Write detail region
        writer.startElement(HTML.DIV_ELEM, null);
        writer.writeAttribute(HTML.ID_ATTR, dvId + "_det", null);
        writer.writeAttribute(HTML.CLASS_ATTR, DataView.DATAVIEW_DETAIL_CLASS, null);

        if (details != null && active)
            details.encodeAll(context);

        writer.startElement(HTML.INPUT_ELEM, null);
        writer.writeAttribute(HTML.ID_ATTR, dvId + "_active", null);
        writer.writeAttribute(HTML.NAME_ATTR, dvId + "_active", null);
        writer.writeAttribute(HTML.TYPE_ATTR, HTML.INPUT_TYPE_HIDDEN, null);
        if (index == null)
            writer.writeAttribute(HTML.VALUE_ATTR, "", null);
        else
            writer.writeAttribute(HTML.VALUE_ATTR, index, null);
        writer.endElement(HTML.INPUT_ELEM);

		writer.startElement(HTML.SPAN_ELEM, null);
		writer.writeAttribute(HTML.ID_ATTR, dvId + "_indexScript", null);
		writer.startElement(HTML.SCRIPT_ELEM, null);
		writer.writeText("document.getElementById('"+dvId+"_det"+"').setAttribute('data-index',"+index+");", null);
		writer.endElement(HTML.SCRIPT_ELEM);
		writer.endElement(HTML.SPAN_ELEM);

        writer.endElement(HTML.DIV_ELEM);

        requestMap.remove(var);
        if (rowIndexVar != null) requestMap.remove(rowIndexVar);
    }

    public boolean getRendersChildren() {
        return true;
    }

    private List<UIComponent> getDetailHolders(UIComponent component) {
        if (component.getChildCount() > 0) {
            ArrayList<UIComponent> valueHolders = new ArrayList<UIComponent>();

            for (UIComponent child : component.getChildren()) {
                if (child instanceof ValueHolder) valueHolders.add(child);
                if (child instanceof UICommand) valueHolders.add(child);
                valueHolders.addAll(getDetailHolders(child));
            }
            return valueHolders;
        }
        else return Collections.emptyList();
    }
}
