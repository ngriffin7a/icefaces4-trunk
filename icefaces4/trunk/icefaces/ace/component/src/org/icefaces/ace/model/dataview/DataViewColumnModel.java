package org.icefaces.ace.model.dataview;

import javax.el.ValueExpression;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Copyright 2010-2013 ICEsoft Technologies Canada Corp.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * <p/>
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * <p/>
 * User: Nils Lundquist
 * Date: 2013-04-01
 * Time: 2:54 PM
 */
public interface DataViewColumnModel {
    public String getHeaderText();
    public String getFooterText();
    public String getMarkup();
    public String getType();
    public String getDateType();
    public String getDatePattern();
    public ValueExpression getValueExpression();
    public Object getValue();
    public Integer getTimeStyle();
    public Integer getDateStyle();
    public TimeZone getTimeZone();
    public Locale getLocale();
    public boolean isRendered();
    public Integer getReactivePriority();
    public String getStyleClass();
}
