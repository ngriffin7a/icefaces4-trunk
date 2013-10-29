
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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * ConfigStorage pick up properties file
 */
public class ConfigStorage {
    
    private String fileName;
    
    private ConfigStorage(String fileName) {
        this.fileName = fileName;
    }
 
    public static ConfigStorage getInstance(String fileName){
        
        return new ConfigStorage(fileName);
    }
    
    public Properties loadProperties(){
        
        Properties properties = new Properties();
        try {
            properties.load(new BufferedInputStream(new FileInputStream(new File(fileName))));
        } catch (FileNotFoundException e){
            e.printStackTrace();
            System.exit(1);
        } catch (IOException e){                        
            e.printStackTrace();
            System.exit(1);
        }
        
        return properties;
    }
    
    
    
}
