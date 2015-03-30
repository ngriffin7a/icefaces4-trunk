package org.icefaces.demo.auction.converter;

import java.util.Date;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(value="TimeLeftConverter")
public class TimeLeftConverter implements Converter {
	private static final int SECONDS_IN_A_DAY = 60 * 60 * 24;
	
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
	        long diffSec = (Long.parseLong(o.toString()) - new Date().getTime()) / 1000;
	        long days = diffSec / SECONDS_IN_A_DAY;
	        long secondsDay = diffSec % SECONDS_IN_A_DAY;
	        
	        String toReturn = "";
	        if (days > 0) {
	        	toReturn += days + " d ";
	        }
	        
	        toReturn += (secondsDay / 3600) + ":" + ((secondsDay / 60) % 60) + ":" + (secondsDay % 60);
	        
	        return toReturn;
		}
		return "?";
	}
}
