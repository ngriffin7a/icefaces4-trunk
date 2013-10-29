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

package org.icefaces.ace.component.qrcode.generator;

import org.icefaces.ace.util.zxing.qrcode.QRCodeWriter;
import org.icefaces.ace.util.zxing.common.BitMatrix;
import org.icefaces.ace.util.zxing.BarcodeFormat;
import org.icefaces.util.EnvUtils;

import javax.faces.application.ResourceHandler;
import javax.faces.application.ResourceHandlerWrapper;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;

public class QRCodeResourceHandler extends ResourceHandlerWrapper  {
    private static Logger log = Logger.getLogger(QRCodeResourceHandler.class.getName());
    private static final String RESOURCE_KEY = "javax.faces.resource";
    private static String RESOURCE_PREFIX = "/javax.faces.resource/";
    private static String QR_PREFIX = "qrcode:";
    private ResourceHandler wrapped;

    public QRCodeResourceHandler(ResourceHandler wrapped)  {
        this.wrapped = wrapped;
    }

    public ResourceHandler getWrapped() {
        return wrapped;
    }

    public void handleResourceRequest(FacesContext facesContext) throws IOException {
        ExternalContext externalContext = facesContext.getExternalContext();
        String key = extractResourceId(facesContext);
        if (null == key)  {
            wrapped.handleResourceRequest(facesContext);
            return;
        }
        if (key.startsWith(QR_PREFIX))  {
            String encoded = key.substring(QR_PREFIX.length());
            String data = URLDecoder.decode(encoded);
            externalContext.setResponseContentType("image/png");
            OutputStream out = externalContext.getResponseOutputStream();
            writeQR(data, out);
        } else {
            wrapped.handleResourceRequest(facesContext);
        }
    }

    public static String getQRImageURL(String data)  {
        String[] pathTemplate = EnvUtils.getPathTemplate();
        //double encode to hide URL characters from Servlet decode
        String key = QR_PREFIX + URLEncoder.encode(
                URLEncoder.encode(data));
        String result = pathTemplate[0] + key + pathTemplate[1];
        return result;
    }

    private static String extractResourceId(FacesContext facesContext)  {
        ExternalContext externalContext = facesContext.getExternalContext();

        int markerStart;
        String path = externalContext.getRequestServletPath();
        markerStart = path.indexOf(RESOURCE_PREFIX);
        if (-1 == markerStart)  {
            path = externalContext.getRequestPathInfo();
            markerStart = path.indexOf(RESOURCE_PREFIX);
        }
        if (-1 == markerStart)  {
            return null;
        }
        try {
            //strip off the javax.faces.resource prefix and remove
            //any extension found in the path template
            String key = path.substring(
                    markerStart + RESOURCE_PREFIX.length(), 
                    path.length() - EnvUtils.getPathTemplate()[1].length());
            return key;
        } catch (Exception e)  {
            return null;
        }
    }

    public static void writeQR(String data, OutputStream out)  {
        try {
            int bigEnough = 80;
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix matrix = writer.encode(data,
                BarcodeFormat.QR_CODE, bigEnough,
                bigEnough, null);
            
            int width = 160, height = 160;

            BufferedImage bi = new BufferedImage(
                width, height, BufferedImage.TYPE_INT_ARGB);

            Graphics2D ig2 = bi.createGraphics();
            ig2.setColor(Color.WHITE);
            ig2.fillRect(0, 0, width, height);
            ig2.setColor(Color.BLACK);
            int d = width / bigEnough;
            int bitWidth = matrix.getWidth();
            int bitHeight = matrix.getHeight();
            for (int i = 0; i < bitHeight; i++)  {
                for (int j = 0; j < bitWidth; j++)  {
                    boolean isSet = matrix.get(j, i);
                    if (isSet)  {
                        ig2.fillRect(d * j, d * i, d, d);
                    }                 }
            }

            ImageIO.write(bi, "PNG", out);
        } catch (Exception e)  {
            log.log(Level.WARNING, "Error create QR Code image", e);
        }
    }

}
