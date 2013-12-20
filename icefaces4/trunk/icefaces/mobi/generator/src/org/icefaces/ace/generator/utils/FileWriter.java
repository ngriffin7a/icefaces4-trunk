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

package org.icefaces.ace.generator.utils;
import java.io.BufferedWriter;
import java.io.File;
import java.io.Writer;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.logging.Level;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.JSP;
import org.icefaces.ace.meta.annotation.TagHandler;
import org.w3c.dom.Document;

public class FileWriter {
       private static final Logger logger = Logger.getLogger(FileWriter.class.getName());

    public static void write(String base, String path, String fileName, StringBuilder contents) {
        Writer writer = null;
        try {
            String workingDir = URLDecoder.decode(getBuildFolder() + base);
            File folder = new File(workingDir+ path);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            
            File file = new File(folder, fileName);

            writer = new BufferedWriter(new java.io.FileWriter(file));
            writer.write(contents.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getBuildFolder() {
        String ret = System.getProperty("generator.build.dir");
        if (logger.isLoggable(Level.FINE)){
            logger.info("getBuildFolder  property: " + ret);
        }
        if (ret == null) {
            ret = getWorkingFolder() + "../../component/build/";
        }
        return ret;
    }

    private static String getWorkingFolder() {
        try {
            ClassLoader classLoader = Thread.currentThread()
            .getContextClassLoader();
            URL localUrl = classLoader.getResource(".");
            if(localUrl != null){
                return localUrl.getPath();
            }
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    
    }
    
    public static void writeXML(Document doc, String folder, String filename, Properties properties) {
        try {
            // Prepare the DOM document for writing
            Source source = new DOMSource(doc);
            File fullFolder = new File(URLDecoder.decode(getBuildFolder()+
                "/"+folder+"/META-INF/"));
            
            if (!fullFolder.exists()) {
                fullFolder.mkdirs();
            }
            // Prepare the output file
            File file = new File(fullFolder, filename);
            Result result = new StreamResult(file);
    
            // Write the DOM document to the file
            Transformer xformer = TransformerFactory.newInstance().newTransformer();
            xformer.setOutputProperties(properties);
            xformer.transform(source, result);

        } catch (TransformerConfigurationException e) {
        } catch (TransformerException e) {
        }
    }
    
    public static void files(File folder) {

        File[] listOfFiles = folder.listFiles();
        for (int i = 0; i < listOfFiles.length; i++) {
          if (listOfFiles[i].isFile() && listOfFiles[i].getName().endsWith(".java")) {
            System.out.println("File " + listOfFiles[i].getName() );
          } else if (listOfFiles[i].isDirectory()) {
            files(listOfFiles[i]);
          }
        }
    }
    
    public static String getPropertyValue(Class clazz, String fieldName, String methodName) {
        try {   
            Field field = clazz.getField(fieldName);
            if (logger.isLoggable(Level.FINE)){
                logger.info("getPropertyValue field="+String.valueOf(field.get(field)));
            }
            return String.valueOf(field.get(field));
        } catch (Exception fe) {
            try {
                Method m = clazz.getMethod(methodName, (Class[]) null);
                Object o = clazz.newInstance();
                return String.valueOf(m.invoke(o, (Object[]) null));
            } catch (Exception me ) {
                me.printStackTrace();
            }
        }
    return null;
    }    
    
    public static List<Class> getAnnotatedCompsList() {
        File file = new File(URLDecoder.decode(getBuildFolder()+"/meta"));

        URLClassLoader clazzLoader = null;
        try {
            URL url = file.toURL();
            clazzLoader = new URLClassLoader(new URL[]{url});
        } catch (Exception e) {
            e.printStackTrace();
        }    
        List<Class> componentsList = new ArrayList<Class>();
        processRequest(file, file.getPath(), componentsList, clazzLoader);
        return componentsList;
    }
    
    public static void processRequest(File file, String pathPrefix, List<Class> componentsList, URLClassLoader loader) {
        
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                String path = files[i].getPath();
                if (path.endsWith(".class")) {
                    if (logger.isLoggable(Level.FINE)){
                        logger.info("1. separatorChar "+ files[i].separatorChar);
                        logger.info("2. separatorChar "+ path);
                    }
                    path = path.substring(pathPrefix.length()+1, path.indexOf(".class"));
                    path = path.replace(files[i].separatorChar, '.');
                    if (logger.isLoggable(Level.FINE)){
                        logger.info("3.separatorChar "+ path);
                    }
                    try {
                        Class c = loader.loadClass(path);
                        if (c.isAnnotationPresent(Component.class) ||
                            c.isAnnotationPresent(TagHandler.class) ||
                            c.isAnnotationPresent(JSP.class)) {
                            if (logger.isLoggable(Level.FINE)){
                                logger.info("Meta class found = "+ path);
                            }
                            componentsList.add(c);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }                     
   
                }
               
                if (files[i].isDirectory()) {
                    processRequest(files[i], pathPrefix, componentsList, loader);
                }
            }
        }
        
        

    }
}

