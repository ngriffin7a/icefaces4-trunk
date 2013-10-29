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

package com.icesoft.icefaces.tutorial.component.converter.custom;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/*
 * User defined converter for phone numbers.  Converts a String into a
 * PhoneNumber onject instance.  Also converts a PhoneNumber instance
 * to a String.
 */
public class PhoneConverter implements Converter{



    public Object getAsObject(FacesContext context, UIComponent component,
            String value){
        if(value == null || value.trim().length() == 0){
            return null;
        }

        PhoneNumber phone = new PhoneNumber();

        String delim = "[\\+\\s,\\(\\)\\-]+";
        String[] phoneComps = value.replaceFirst("^"+delim, "").split(delim);

        String countryCode = phoneComps[0];

        phone.setCountryCode(countryCode);

        if(countryCode.equals("1")){
            String areaCode = phoneComps[1];
            String prefix = phoneComps[2];
            String number = phoneComps[3];
            phone.setAreaCode(areaCode);
            phone.setPrefix(prefix);
            phone.setNumber(number);
        }
        else{
            phone.setNumber(value);
        }

        return phone;

    }

    public String getAsString(FacesContext context, UIComponent component,
            Object value){
        if(value == null){
		    return "";
		}
        return value.toString();
    }




}
