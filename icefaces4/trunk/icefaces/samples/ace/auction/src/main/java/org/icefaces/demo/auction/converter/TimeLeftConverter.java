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
