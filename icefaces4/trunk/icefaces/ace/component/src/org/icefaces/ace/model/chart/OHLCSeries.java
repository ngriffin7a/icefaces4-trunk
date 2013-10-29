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

package org.icefaces.ace.model.chart;

import org.icefaces.ace.model.SimpleEntry;
import org.icefaces.ace.util.JSONBuilder;

import javax.faces.component.UIComponent;
import java.util.Date;
import java.util.Map;

public class OHLCSeries extends ChartSeries {
    public static enum OHLCType implements ChartType {
        OHLC,
        CANDLESTICK;
    }

    boolean isHLC; // Ignores opening value if indicated.
    int tickLength; // pixels
    int bodyWidth;
    String openColor;
    String closeColor;
    String wickColor;
    String upBodyColor;
    String downBodyColor;
    boolean fillUpBody;
    boolean fillDownBody;

    public OHLCSeries() {}

    public void add(Date x, Number o, Number h, Number l, Number c) {
        getData().add(new SimpleEntry<Date, Number[]>(x, new Number[]{o, h, l, c}));
    }

    @Override
    public JSONBuilder getDataJSON(UIComponent chart) {
        JSONBuilder json = super.getDataJSON(chart);

        for (Object o : getData()) {
            Map.Entry entry = (Map.Entry)o;
            Date key = (Date) entry.getKey();
            Number[] value = (Number[]) entry.getValue();

            json.beginArray();
            json.item(key.getTime());
            for (Number n : value)
                if (n != null) json.item(n.doubleValue());
                else json.item("undefined", false);
            json.endArray();
        }

        json.endArray();

        return json;
    }

    /**
     * Used by the ChartRenderer to produce a JSON representation of the data of this series.
     * @return the JSON object
     * @param component
     */
    @Override
    public JSONBuilder getConfigJSON(UIComponent component) {
        JSONBuilder cfg = super.getConfigJSON(component);


        cfg.entry("renderer", "ice.ace.jq.jqplot.OHLCRenderer", true);

        if (rendererOptionsSet()) {
            cfg.beginMap("rendererOptions");
            if (OHLCType.CANDLESTICK.equals(type))
                cfg.entry("candleStick", true);
            cfg.endMap();
        }

        cfg.endMap();
        return cfg;
    }

    private boolean rendererOptionsSet() {
        return OHLCType.CANDLESTICK.equals(type);
    }

    @Override
    public ChartType getDefaultType() {
        return OHLCType.OHLC;
    }
}
