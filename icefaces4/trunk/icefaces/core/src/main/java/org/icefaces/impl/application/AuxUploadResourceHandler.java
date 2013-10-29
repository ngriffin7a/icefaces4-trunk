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

package org.icefaces.impl.application;

import org.icefaces.util.EnvUtils;
import org.icefaces.impl.util.Util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import javax.servlet.http.HttpSession;
import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.application.ResourceHandlerWrapper;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.UUID;
import java.util.Collection;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AuxUploadResourceHandler extends ResourceHandlerWrapper  {
    private static Logger log = Logger.getLogger(AuxUploadResourceHandler.class.getName());
    public static String AUX_REQ_MAP_KEY = 
            "iceAuxRequestMap";
    public static String CLOUD_PUSH_KEY = 
            "iceCloudPushId";
    private ResourceHandler wrapped;
    private Resource tokenResource = null;

    public AuxUploadResourceHandler(ResourceHandler wrapped)  {
        this.wrapped = wrapped;
    }

    public ResourceHandler getWrapped() {
        return wrapped;
    }

    private Resource getTokenResource()  {
        if (null == tokenResource)  {
            tokenResource = createResource("auxupload.txt");
        }
        return tokenResource;
    }

    public void handleResourceRequest(FacesContext facesContext) throws IOException {
        ExternalContext externalContext = facesContext.getExternalContext();
    
        if ( getTokenResource().getRequestPath().equals(
            getResourcePath(facesContext)) )  {
            storeParts(externalContext);
            externalContext.setResponseContentType("text/plain");
            OutputStream out = externalContext.getResponseOutputStream();
            out.write("handled by AuxUploadResourceHandler".getBytes());
            return;
        }
        
        wrapped.handleResourceRequest(facesContext);
        
    }

    /**
     * Return the Map containing any request parameters or parts
     * that have been uploaded outside this particular JSF request.
     * This map is obtained non-destructively so is intended only 
     * for inspecting the contents of the auxiliary upload.
     * 
     * @return Map of request parameters from auxiliary upload.
     */
    public static Map pollAuxRequestMap()  {
        ExternalContext externalContext = FacesContext.getCurrentInstance()
                .getExternalContext();
        //if moved into request scope, then the upload is already processed
        //so only check in the session
        Map sessionMap = externalContext.getSessionMap();
        Map auxRequestMap = (Map) sessionMap.get(AUX_REQ_MAP_KEY);
        return auxRequestMap;
    }

	/**
     * Return the Map containing any request parameters or parts
     * that have been uploaded outside this particular JSF request.
     * After this map is obtained, it is moved from session scope
     * to request scope to allow garbage collection.
     * 
     * @return Map of request parameters from auxiliary upload.
     */
    public static Map getAuxRequestMap()  {
        ExternalContext externalContext = FacesContext.getCurrentInstance()
                .getExternalContext();
        Map sessionMap = externalContext.getSessionMap();
        Map requestMap = externalContext.getRequestMap();
        Map auxRequestMap;
        auxRequestMap = (Map) requestMap.get(AUX_REQ_MAP_KEY);
        if (null != auxRequestMap)  {
            return auxRequestMap;
        }
        auxRequestMap = (Map) sessionMap.get(AUX_REQ_MAP_KEY);
        if (null != auxRequestMap)  {
            //once the auxiliary upload is used, it is only valid
            //for the current request to allow cleanup
            sessionMap.put(AUX_REQ_MAP_KEY, new HashMap());
        } else {
            auxRequestMap = new HashMap();
        }
        requestMap.put(AUX_REQ_MAP_KEY, auxRequestMap);
        return auxRequestMap;
    }

    private void storeParts(ExternalContext externalContext)  {
        HttpServletRequest request = 
                (HttpServletRequest) externalContext.getRequest();
        HttpSession session = request.getSession();
        try {
        
            Map auxRequestMap = 
                    (Map) session.getAttribute(AUX_REQ_MAP_KEY);
            if (null == auxRequestMap)  {
                auxRequestMap = new HashMap();
            }

            Collection<Part> parts = new ArrayList();
            try {
                parts = request.getParts();
            } catch (Throwable t)  {
                //If Servlet 3.0 Parts are not available fall back
                //to ICEmobile uploads stored as Request attributes
                Map requestMap =
                        externalContext.getRequestMap();
                for (Object keyObj : requestMap.keySet())  {
                    if (keyObj instanceof String)  {
                        String key = (String) keyObj;
                        if (key.startsWith("org.icemobile.file."))  {
                            auxRequestMap.put(
                                    key, requestMap.get(key) );
                        }
                    }
                }

                //must copy all parameters since they will not
                //appear as untyped parts in this case
                Map requestParameterMap =
                        externalContext.getRequestParameterMap();
                for (Object key : requestParameterMap.keySet())  {
                    auxRequestMap.put(key, requestParameterMap.get(key) );
                    if (CLOUD_PUSH_KEY.equals(key))  {
                        session.setAttribute(CLOUD_PUSH_KEY,
                                requestParameterMap.get(key));
                    }
                }
            
            }

            for (Part part : parts) {
                String partType = part.getContentType();
                String partName = part.getName();
                Object partParameter = request.getParameter(partName);
                if (null != partParameter)  {
                    auxRequestMap.put(partName, partParameter );
                    if (CLOUD_PUSH_KEY.equals(partName))  {
                        session.setAttribute(CLOUD_PUSH_KEY, partParameter);
                    }
                } else {
                    auxRequestMap.put(partName,
                            new PersistentPart(externalContext, part));
                }
            }
            
            session.setAttribute(AUX_REQ_MAP_KEY, auxRequestMap);

        } catch (Exception e) {
            log.log(Level.WARNING, "Failed to decode auxUpload Parts", e);
        }
    }

    public String getTokenResourcePath()  {
        return getTokenResource().getRequestPath();
    }

    private static String getResourcePath(FacesContext facesContext)  {
        ExternalContext externalContext = facesContext.getExternalContext();
        String path = externalContext.getRequestServletPath();
        if (null == path)  {
            path = externalContext.getRequestPathInfo();
        }
        return (externalContext.getRequestContextPath() + path);
    }

    public String getCloudPushId()  {
        String cloudPushId = (String) FacesContext.getCurrentInstance()
                .getExternalContext().getSessionMap().get(CLOUD_PUSH_KEY);
        return cloudPushId;
    }

}

class PersistentPart implements Part  {
    Part part;
    File partFile;

    public PersistentPart(ExternalContext externalContext, Part part)
        throws IOException  {
        this.part = part;
        File tempDir = (File) externalContext.getApplicationMap()
                .get("javax.servlet.context.tempdir");
        partFile = File.createTempFile("auxupload", ".tmp", tempDir);
        Util.copyStream(part.getInputStream(),
                new FileOutputStream(partFile));
    }

    public InputStream getInputStream() throws IOException  {
        return new FileInputStream(partFile);
    }

    public String getContentType()  {
        return part.getContentType();
    }

    public String getName()  {
        return part.getName();
    }

    public long getSize()  {
        return part.getSize();
    }

    public void write(String name) throws IOException  {
        part.write(name);
    }

    public void delete() throws IOException  {
        part.delete();
    }

    public String getHeader(String name)  {
        return part.getHeader(name);
    }

    public Collection getHeaders(String name)  {
        return part.getHeaders(name);
    }

    public Collection getHeaderNames()  {
        return part.getHeaderNames();
    }
}