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

package org.icefaces.mobi.component.fetchcontact;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Model utility class for the Contact List component mobi:fetchContact that will extract 
 * various fields from the raw encoded contact string.
 * 
 *
 */
public class ContactDecoder {
    
    private String name;
    private String phone;
    private String email;
    
    public ContactDecoder(String rawContactString){
        if( rawContactString != null && !"".equals(rawContactString)){
            try {
                //contact string has to be decoded
                String decoded = URLDecoder.decode(rawContactString,"UTF-8");
                String[] tokens = decoded.split("&");
                for( int i = 0 ; i < tokens.length ; i++ ){
                    //each contact field will have a key and value
                    String key = tokens[i].substring(0,tokens[i].indexOf("="));
                    String val = tokens[i].substring(tokens[i].indexOf("=")+1);
                    //possible keys are 'name', 'phone', and 'email'
                    if( "name".equals(key)){
                        name = val;
                    }
                    else if( "phone".equals(key)){
                        phone = val;
                    }
                    else if( "email".equals(key)){
                        email = val;
                    }
                    
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

}
