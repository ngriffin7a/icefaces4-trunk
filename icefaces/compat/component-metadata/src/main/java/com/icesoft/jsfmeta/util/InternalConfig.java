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



package com.icesoft.jsfmeta.util;

import com.icesoft.jsfmeta.MetadataXmlParser;
import com.sun.rave.jsfmeta.beans.FacesConfigBean;
import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author fye
 */
public class InternalConfig {
    
    
    private Properties properties;
    
    
    private static String WORKING_FOLDER;
    
    /** Creates a new instance of InternalConfig */
    public InternalConfig(Properties properties) {
        this.properties = properties;
    }
    
    public String getProperty(String keyValue){
        
        return properties.getProperty(keyValue);
    }
    
    
    public void clear(){
        
    }
}
