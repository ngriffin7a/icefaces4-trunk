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

package org.icefaces.samples.showcase.example.ace.date;

import java.io.Serializable;
import java.util.Date;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;

@ManagedBean(name= DateReqStyleBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class DateReqStyleBean implements Serializable {
    public static final String BEAN_NAME = "dateReqStyle";
	public String getBeanName() { return BEAN_NAME; }
    
    private Date selectedDate1 = new Date(System.currentTimeMillis());
    private Date selectedDate2 = new Date(System.currentTimeMillis());
    private String reqColor = "redRS";
    private String optColor = "greenRS";

    public Date getSelectedDate1() {
        return selectedDate1;
    }
    
    public Date getSelectedDate2() {
        return selectedDate2;
    }
    
    public String getReqColor() {
        return reqColor;
    }
    
    public String getOptColor() {
        return optColor;
    }
    
    public void setSelectedDate1(Date selectedDate1) {
        this.selectedDate1 = selectedDate1;
    }
    
    public void setSelectedDate2(Date selectedDate2) {
        this.selectedDate2 = selectedDate2;
    }
    
    public void setReqColor(String reqColor) {
        this.reqColor = reqColor;
    }
    
    public void setOptColor(String optColor) {
        this.optColor = optColor;
    }
    
	private boolean useTheme = false;

    public boolean getUseTheme() {
        return useTheme;
    }

    public void setUseTheme(boolean useTheme) {
        this.useTheme = useTheme;
    }
}
