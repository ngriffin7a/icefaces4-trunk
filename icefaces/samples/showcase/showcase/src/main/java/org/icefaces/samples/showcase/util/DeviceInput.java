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

package org.icefaces.samples.showcase.util;

import org.icefaces.ace.util.IceOutputResource;

import javax.faces.application.Resource;
import java.io.*;
import java.util.Formatter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Base class for all device input beans.  The class aids in the common
 * task of putting an input source file into a Resource object.
 * <p/>
 * {@link org.icemobile.samples.mobileshowcase.view.examples.device.camcorder.CamcorderBean}
 * {@link org.icemobile.samples.mobileshowcase.view.examples.device.microphone.MicrophoneBean}
 * {@link org.icemobile.samples.mobileshowcase.view.examples.device.camera.CameraBean}
 */
public class DeviceInput implements Serializable{

    private static final Logger logger =
            Logger.getLogger(DeviceInput.class.toString());

    public static Resource createResourceObject(File resourceFile,
                                                String resourceName,
                                                String contentType) throws IOException {
        FileInputStream fis = new FileInputStream(resourceFile);
        return createResourceObject(fis, resourceName, contentType);
    }

    // copy the bytes into the resource object.
    public static Resource createResourceObject(InputStream fis,
                                                String resourceName,
                                                String contentType) throws IOException {
        Resource outputResource = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[4096];
        for (int readNum; (readNum = fis.read(buf)) != -1; ) {
            bos.write(buf, 0, readNum);
        }
        outputResource = new IceOutputResource(resourceName,
                bos.toByteArray(), contentType);
        bos.close();
        fis.close();

        return outputResource;
    }

    public static byte[] createByteArray(File resourceFile) throws IOException {
        FileInputStream fis = new FileInputStream(resourceFile);
        return createByteArray(fis);
    }

    public static byte[] createByteArray(InputStream fis) throws IOException {
        Resource outputResource = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[4096];
        for (int readNum; (readNum = fis.read(buf)) != -1; ) {
            bos.write(buf, 0, readNum);
        }

        bos.close();
        fis.close();

        return bos.toByteArray();
    }

    // process video file to common output for cross platform playback.
    public static File convertFileToExtensionType(File inputFile, String commandTemplate,
                             String outputExtension) {
        StringBuilder command = new StringBuilder();
        try {
            File converted = File.createTempFile("out", outputExtension);
            Formatter formatter = new Formatter(command);
            formatter.format(commandTemplate,
                    inputFile.getAbsolutePath(),
                    converted.getAbsolutePath());
            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec(command.toString());
            int exitValue = process.waitFor();
            if (0 != exitValue) {
                logger.log(Level.WARNING, "Transcoding failure: " + command);
                StringBuilder errorString = new StringBuilder();
                InputStream errorStream = process.getErrorStream();
                byte[] buf = new byte[1000];
                int len = -1;
                while ((len = errorStream.read(buf)) > 0) {
                    errorString.append(new String(buf, 0, len));
                }
                logger.log(Level.WARNING, errorString.toString());
            }
            return converted;
        } catch (Exception e) {
            //conversion fails, but we may proceed with original file
            logger.log(Level.WARNING, command + " Error processing file.", e);
        }
        return null;
    }
}
