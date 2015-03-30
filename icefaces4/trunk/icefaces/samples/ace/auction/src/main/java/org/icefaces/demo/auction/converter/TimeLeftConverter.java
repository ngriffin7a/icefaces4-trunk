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

package org.icefaces.demo.auction.converter;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(value="TimeLeftConverter")
public class TimeLeftConverter implements Converter {
	private static final int SECONDS_IN_A_DAY = 60 * 60 * 24;
	private static final NumberFormat LEADING_ZERO_FORMATTER = new DecimalFormat("00");
	
	@Override
	public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
		return getAsString(facesContext, uiComponent, s);
	}

	/**
	 * Take in a millisecond value that our item expires on
	 * Then we'll get the current date, and figure out the time left between the two
	 * The result will be formatted from milliseconds to a human readable form
	 */
	@Override
	public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
		if (o != null) {
			return convertExpiryToTimeLeft(Long.parseLong(o.toString()));
		}
		return "?";
	}
	
	public static String convertExpiryToTimeLeft(long expiry) {
        long diffSec = (expiry - new Date().getTime()) / 1000;
        long days = diffSec / SECONDS_IN_A_DAY;
        long secondsDay = diffSec % SECONDS_IN_A_DAY;
        
        String toReturn = days + " days ";
        toReturn += LEADING_ZERO_FORMATTER.format((secondsDay / 3600)) + ":" +
                    LEADING_ZERO_FORMATTER.format(((secondsDay / 60) % 60)) + ":" +
        		    LEADING_ZERO_FORMATTER.format((secondsDay % 60));
        
        return toReturn;
	}
}
