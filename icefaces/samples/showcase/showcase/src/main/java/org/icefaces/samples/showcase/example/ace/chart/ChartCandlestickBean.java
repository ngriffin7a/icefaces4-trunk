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

package org.icefaces.samples.showcase.example.ace.chart;

import org.icefaces.ace.component.chart.Axis;
import org.icefaces.ace.component.chart.AxisType;
import org.icefaces.ace.model.chart.OHLCSeries;
import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import java.util.ArrayList;
import java.util.List;
import java.util.GregorianCalendar;
import java.io.Serializable;

@ComponentExample(
        parent = ChartBean.BEAN_NAME,
        title = "example.ace.chart.candlestick.title",
        description = "example.ace.chart.candlestick.description",
        example = "/resources/examples/ace/chart/chartCandlestick.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="ChartCandlestick.xhtml",
                    resource = "/resources/examples/ace/chart/chartCandlestick.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="ChartCandlestickBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase/example/ace/chart/ChartCandlestickBean.java")
        }
)
@ManagedBean(name= ChartCandlestickBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class ChartCandlestickBean extends ComponentExampleImpl<ChartCandlestickBean> implements Serializable
{
    public static final String BEAN_NAME = "chartCandlestickBean";
    
    private List<OHLCSeries> ohlcData = new ArrayList<OHLCSeries>() {{
        add(new OHLCSeries(){{
            setType(OHLCType.CANDLESTICK);
            add(new GregorianCalendar(2009,5,15).getTime(), 136.01, 139.5,  134.53, 139.48);
            add(new GregorianCalendar(2009,5,8).getTime(),  143.82, 144.56, 136.04, 136.97);
            add(new GregorianCalendar(2009,5,1).getTime(),  136.47, 146.4,  136,    144.67);
            add(new GregorianCalendar(2009,4,26).getTime(), 124.76, 135.9,  124.55, 135.81);
            add(new GregorianCalendar(2009,4,18).getTime(), 123.73, 129.31, 121.57, 122.5);
            add(new GregorianCalendar(2009,4,18).getTime(), 123.73, 129.31, 121.57, 122.5);
            add(new GregorianCalendar(2009,4,11).getTime(), 127.37, 130.96, 119.38, 122.42);
            add(new GregorianCalendar(2009,4,4).getTime(),  128.24, 133.5,  126.26, 129.19);
            add(new GregorianCalendar(2009,3,27).getTime(), 122.9,  127.95, 122.66, 127.24);
            add(new GregorianCalendar(2009,3,20).getTime(), 121.73, 127.2,  118.6,  123.9);
            add(new GregorianCalendar(2009,3,13).getTime(), 120.01, 124.25, 115.76, 123.42);
            add(new GregorianCalendar(2009,3,6).getTime(),  114.94, 120,    113.28, 119.57);
            add(new GregorianCalendar(2009,2,30).getTime(), 104.51, 116.13, 102.61, 115.99);
            add(new GregorianCalendar(2009,2,23).getTime(), 102.71, 109.98, 101.75, 106.85);
            add(new GregorianCalendar(2009,2,16).getTime(), 96.53,  103.48, 94.18,  101.59);
            add(new GregorianCalendar(2009,2,9).getTime(),  84.18,  97.2,   82.57,  95.93);
            add(new GregorianCalendar(2009,2,2).getTime(),  88.12,  92.77,  82.33,  85.3);
            add(new GregorianCalendar(2009,1,23).getTime(), 91.65,  92.92,  86.51,  89.31);
            add(new GregorianCalendar(2009,1,17).getTime(), 96.87,  97.04,  89,     91.2);
            add(new GregorianCalendar(2009,1,9).getTime(),  100,    103,    95.77,  99.16);
            add(new GregorianCalendar(2009,1,2).getTime(),  89.1,   100,    88.9,   99.72);
            add(new GregorianCalendar(2009,0,26).getTime(), 88.86,  95,     88.3,   90.13);
            add(new GregorianCalendar(2009,0,20).getTime(), 81.93,  90,     78.2,   88.36);
            add(new GregorianCalendar(2009,0,12).getTime(), 90.46,  90.99,  80.05,  82.33);
            add(new GregorianCalendar(2009,0,5).getTime(),  93.17,  97.17,  90.04,  90.58);
            add(new GregorianCalendar(2008,11,29).getTime(),86.52,  91.04,  84.72,  90.75);
            add(new GregorianCalendar(2008,11,22).getTime(),90.02,  90.03,  84.55,  85.81);
            add(new GregorianCalendar(2008,11,15).getTime(),95.99,  96.48,  88.02,  90);
            add(new GregorianCalendar(2008,11,8).getTime(), 97.28,  103.6,  92.53,  98.27);
            add(new GregorianCalendar(2008,11,1).getTime(), 91.3,   96.23,  86.5,   94);
            add(new GregorianCalendar(2008,10,24).getTime(),85.21,  95.25,  84.84,  92.67);
            add(new GregorianCalendar(2008,10,17).getTime(),88.48,  91.58,  79.14,  82.58);
            add(new GregorianCalendar(2008,10,10).getTime(),100.17, 100.4,  86.02,  90.24);
            add(new GregorianCalendar(2008,10,3).getTime(), 105.93, 111.79, 95.72,  98.24);
            add(new GregorianCalendar(2008,9,27).getTime(), 95.07,  112.19, 91.86,  107.59);
            add(new GregorianCalendar(2008,9,20).getTime(), 99.78,  101.25, 90.11,  96.38);
            add(new GregorianCalendar(2008,9,13).getTime(), 104.55, 116.4,  85.89,  97.4);
            add(new GregorianCalendar(2008,9,6).getTime(),  91.96,  101.5,  85,     96.8);
            add(new GregorianCalendar(2008,8,29).getTime(), 119.62, 119.68, 94.65,  97.07);
            add(new GregorianCalendar(2008,8,22).getTime(), 139.94, 140.25, 123,    128.24);
            add(new GregorianCalendar(2008,8,15).getTime(), 142.03, 147.69, 120.68, 140.91);
        }});
    }};
    
    private Axis ohlcXAxis = new Axis() {{
        setType(AxisType.DATE);
        setFormatString("%b %e");
        setMin("09-01-2008");
        setMax("06-22-2009");
        setTickInterval("6 weeks");
    }};

    private Axis[] ohlcYAxes = new Axis[] {
       new Axis() {{ setTickPrefix("$"); }}
    };
    
    private String formatString = "<table class=\"jqplot-highlighter\"><tr><td>date:</td><td>%s</td></tr> \n" +
            "<tr><td>open:</td><td>%s</td></tr>\n" +
            "<tr><td>hi:</td><td>%s</td></tr>\n" +
            "<tr><td>low:</td><td>%s</td></tr> \n" +
            "<tr><td>close:</td><td>%s</td></tr></table>";
    
    public ChartCandlestickBean() {
        super(ChartCandlestickBean.class);
    }
    
    public List<OHLCSeries> getOhlcData() {
        return ohlcData;
    }
    
    public void setOhlcData(List<OHLCSeries> ohlcData) {
        this.ohlcData = ohlcData;
    }

    public Axis getOhlcXAxis() {
        return ohlcXAxis;
    }

    public void setOhlcXAxis(Axis ohlcXAxis) {
        this.ohlcXAxis = ohlcXAxis;
    }

    public Axis[] getOhlcYAxes() {
        return ohlcYAxes;
    }

    public void setOhlcYAxes(Axis[] ohlcYAxes) {
        this.ohlcYAxes = ohlcYAxes;
    }
    
    public String getFormatString() {
        return formatString;
    }

    public void setFormatString(String formatString) {
        this.formatString = formatString;
    }
    
    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }
}
