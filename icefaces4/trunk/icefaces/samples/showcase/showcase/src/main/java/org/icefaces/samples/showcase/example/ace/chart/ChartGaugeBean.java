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

package org.icefaces.samples.showcase.example.ace.chart;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;

import org.icefaces.ace.model.chart.GaugeSeries;

@ManagedBean(name= ChartGaugeBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class ChartGaugeBean implements Serializable {
    public static final String BEAN_NAME = "chartGaugeBean";
	public String getBeanName() { return BEAN_NAME; }

    public List<GaugeSeries> gaugeData = new ArrayList<GaugeSeries>() {{
        add(new GaugeSeries(5) {{
            setMin(0);
            setMax(10);
        }});
    }};

    public List<GaugeSeries> getGaugeData() {
        return gaugeData;
    }

    public void setGaugeData(List<GaugeSeries> gaugeData) {
        this.gaugeData = gaugeData;
    }
}
