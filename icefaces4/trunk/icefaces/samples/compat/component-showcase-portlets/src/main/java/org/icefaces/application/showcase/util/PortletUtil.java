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

package org.icefaces.application.showcase.util;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.util.Iterator;
import java.util.Map;

@ManagedBean(name = "portletUtil")
@RequestScoped
public class PortletUtil {
    public static final String VIEW_PATH_KEY = "org.icefaces.demo.viewPath";

    public PortletUtil() {
    }

    /**
     * Get the init-param value for 'org.icefaces.demo.viewPath' from portlet.xml
     * @return
     */
    public String getPortletViewPath() {
        return getInitParam(VIEW_PATH_KEY);
    }

    /**
     * Due to vagueness in the various specifications relating to both JSF and portlets,
     * init-param values are not easily accessible via normal JSF API mechanisms.  This
     * method uses the PortletFaces bridge to access the PortletConfig so that we can get
     * at the values we want.
     * 
     * @param key The key for the init-param
     * @return The value for the init-param
     */
    private static String getInitParam(String key){
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        //With the PortletFaces bridge, calling getInitParameter is smart enough to provide
        //the web.xml or portlet.xml values which allows us to use intuitive JSF code to get
        //the parameter value we are interested in.
        return ec.getInitParameter(key);
    }

    private static void dumpMap(String msg, Map map){
        StringBuffer buff = new StringBuffer(msg);
        buff.append("\n");
        Iterator keys = map.keySet().iterator();
        while(keys.hasNext()){
            Object key = keys.next();
            buff.append("\n  ").append(key).append(" = ").append(map.get(key));
        }
        System.out.println(buff.toString());
    }
}
