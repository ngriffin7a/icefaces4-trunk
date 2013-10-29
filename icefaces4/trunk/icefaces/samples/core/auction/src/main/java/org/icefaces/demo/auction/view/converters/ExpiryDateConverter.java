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

package org.icefaces.demo.auction.view.converters;

import org.icefaces.demo.auction.view.beans.AuctionItemBean;
import org.icefaces.demo.auction.view.names.BeanNames;
import org.icefaces.demo.auction.view.util.FacesUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import java.util.PropertyResourceBundle;

/**
 * Converts a GregorianCalendar Object stored in a AuctionItem  to a simple
 * formatted String output.  The converter is only for output it does not
 * go the other way back to an object.
 *
 * @author ICEsoft Technologies Inc.
 * @since 2.0
 */
@FacesConverter(value = "auctionItemDateConverter")
public class ExpiryDateConverter implements Converter {

    public String getAsString(FacesContext facesContext, UIComponent
            uiComponent, Object o) {

        if (o instanceof AuctionItemBean) {
            // convert the date and subtract the current date.
            AuctionItemBean auctionItemBean = (AuctionItemBean) o;
            long[] countDownTime = auctionItemBean.getTimeLeftBrokenDown();
            if (auctionItemBean.isExpired(countDownTime)) {
                PropertyResourceBundle msgs = (PropertyResourceBundle)
                        FacesUtils.getManagedBean(BeanNames.MSGS_BEAN);
                return msgs.getString("auction.converter.timeLeft.expired");
            }
            StringBuffer buf = new StringBuffer();
            if (0 != countDownTime[AuctionItemBean.DAY_COMPONENT]) {
                buf.append(String.valueOf(countDownTime[AuctionItemBean.DAY_COMPONENT]));
                buf.append("d ");
            }
            if (0 != countDownTime[AuctionItemBean.HOUR_COMPONENT]) {
                buf.append(String.valueOf(countDownTime[AuctionItemBean.HOUR_COMPONENT]));
                buf.append(":");
                if (countDownTime[AuctionItemBean.MINUTE_COMPONENT] < 10) {
                    buf.append("0");
                }
            }
            buf.append(String.valueOf(countDownTime[AuctionItemBean.MINUTE_COMPONENT]));
            buf.append(":");
            if (countDownTime[AuctionItemBean.SECOND_COMPONENT] < 10) {
                buf.append("0");
            }
            buf.append(Long.toString(countDownTime[AuctionItemBean.SECOND_COMPONENT]));

            return buf.toString();
        }
        PropertyResourceBundle msgs = (PropertyResourceBundle)
                        FacesUtils.getManagedBean(BeanNames.MSGS_BEAN);
        return msgs.getString("auction.converter.timeLeft.na");
    }

    /**
     * Not Implemented, returns null.
     *
     * @return null
     */
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        return null;
    }
}
