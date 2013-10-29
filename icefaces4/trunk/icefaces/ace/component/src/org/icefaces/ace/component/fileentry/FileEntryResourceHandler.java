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

package org.icefaces.ace.component.fileentry;

import org.icefaces.apache.commons.fileupload.*;
import org.icefaces.impl.application.WindowScopeManager;
import org.icefaces.impl.context.DOMPartialViewContext;
import org.icefaces.impl.util.CoreUtils;
import org.icefaces.apache.commons.fileupload.servlet.ServletFileUpload;
import org.icefaces.apache.commons.fileupload.util.Streams;
import org.icefaces.util.EnvUtils;

import javax.faces.application.ProjectStage;
import javax.faces.context.ExternalContext;
import javax.faces.context.PartialViewContext;
import javax.faces.context.FacesContext;
import javax.faces.application.FacesMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.faces.application.ResourceHandlerWrapper;
import javax.faces.application.ResourceHandler;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.*;
import java.io.InputStream;
import java.io.File;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.lang.reflect.*;

public class FileEntryResourceHandler extends ResourceHandlerWrapper {
    private static Logger log = Logger.getLogger(FileEntry.class.getName()+".multipart");
    private ResourceHandler wrapped;

    public FileEntryResourceHandler(ResourceHandler wrapped)  {
        this.wrapped = wrapped;
        log.fine("FileEntryResourceHandler  wrapped: " + wrapped);
    }

    public ResourceHandler getWrapped() {
        return wrapped;
    }

    private static int counter;

    @Override
    public boolean isResourceRequest(FacesContext facesContext) {
        ExternalContext externalContext = facesContext.getExternalContext();
        Object requestObject = facesContext.getExternalContext().getRequest();
        HttpServletRequest request = EnvUtils.getSafeRequest(facesContext);
        Object fileEntryMarker = request.getParameter(FileEntryFormSubmit.FILE_ENTRY_MULTIPART_MARKER);  // String "true"
        log.finest("FileEntryResourceHandler  fileEntryMarker: " + fileEntryMarker +
            "  requireJS: " + EnvUtils.isFileEntryRequireJavascript(facesContext) + "  this: " + this + "  count: " + counter++);
        if (fileEntryMarker == null && EnvUtils.isFileEntryRequireJavascript(facesContext)) {
            return wrapped.isResourceRequest(facesContext);
        }

        String reqContentType = externalContext.getRequestContentType();
        boolean contentTypeNotMultipart = ( (null == reqContentType) ||
                !reqContentType.startsWith("multipart") );
        log.finest(
            "FileEntryResourceHandler\n" +
            "  requestContextPath: " + externalContext.getRequestContextPath() + "\n" +
            "  requestPathInfo   : " + externalContext.getRequestPathInfo() + "\n" +
            "  requestContentType: " + reqContentType + "\n" +
            "  multipart         : " + (!contentTypeNotMultipart));
        if (contentTypeNotMultipart)  {
            return wrapped.isResourceRequest(facesContext);
        }

        boolean isPortlet = EnvUtils.instanceofPortletRequest(requestObject);
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        log.finer("FileEntryResourceHandler\n" +
            "  isMultipart: " + isMultipart + "\n" +
            "  isPortlet: " + isPortlet);

        if (isMultipart) {
            long requestContentLength = (long) request.getContentLength();
            String reqCharEnc = request.getCharacterEncoding();
            String extCharEnc = externalContext.getRequestCharacterEncoding();
            String iceHandlerCharEnc = org.icefaces.impl.util.CharacterEncodingHandler.calculateCharacterEncoding(facesContext);
            String resolvedCharacterEncoding = (reqCharEnc != null) ? reqCharEnc : ((extCharEnc != null) ? extCharEnc : (iceHandlerCharEnc));
            log.finer("FileEntryResourceHandler\n" +
                "  requestContentLength: " + requestContentLength + "\n" +
                "  request.getCharacterEncoding: " + reqCharEnc + "\n" +
                "  externalContext.getRequestCharacterEncoding: " + extCharEnc + "\n" +
                "  CharacterEncodingHandler.calculateCharacterEncoding: " + iceHandlerCharEnc + "\n" +
                "  resolvedCharacterEncoding: " + resolvedCharacterEncoding + "\n" +
                "  Charset.defaultCharset().displayName(): " + java.nio.charset.Charset.defaultCharset().displayName());

            Map<String, FileEntryResults> clientId2Results =
                    new HashMap<String, FileEntryResults>(6);
            ProgressListenerResourcePusher progressListenerResourcePusher =
                    new ProgressListenerResourcePusher(clientId2Results);
            Map<String, FileEntryCallback> clientId2Callbacks =
                    new HashMap<String, FileEntryCallback>(6);
            Map<String, List<String>> parameterListMap =
                    new HashMap<String, List<String>>();
            byte[] buffer = new byte[16*1024];
			
			Class partClass;
			try {
				partClass = Class.forName("javax.servlet.http.Part");
			} catch (ClassNotFoundException e) {
				partClass = null;
			}

            try {
				if (partClass != null) {
					Collection<javax.servlet.http.Part> parts = request.getParts();
					log.finer("FileEntryResourceHandler  Parts size: " + parts.size());
					PartsManualProgress partsManualProgress = new PartsManualProgress(
						(ProgressListener) progressListenerResourcePusher,
						requestContentLength);
					for (javax.servlet.http.Part part : parts) {
						handleMultipartPortion(facesContext,
							resolvedCharacterEncoding, clientId2Results,
							clientId2Callbacks, parameterListMap,
							partsManualProgress,
							(PushResourceSetup) progressListenerResourcePusher,
							buffer, new PartFile(part, partsManualProgress));
					}
                }

                final ServletFileUpload uploader = new ServletFileUpload();
                if (resolvedCharacterEncoding != null) {
                    uploader.setHeaderEncoding(resolvedCharacterEncoding);
                }
                uploader.setProgressListener(progressListenerResourcePusher);
                FileItemIterator iter = uploader.getItemIterator(request);
                log.finer("FileEntryResourceHandler  Commons FileUpload has data: " + iter.hasNext());
                while (iter.hasNext()) {
                    handleMultipartPortion(facesContext,
                        resolvedCharacterEncoding, clientId2Results,
                        clientId2Callbacks, parameterListMap,
                        null, // ServletFileUpload uses ProgressListener
                        (PushResourceSetup) progressListenerResourcePusher,
                        buffer, new CommonsFileUploadFile(iter.next()));
                }
            }
            catch(Exception e) {
                FacesMessage fm = FileEntryStatuses.PROBLEM_READING_MULTIPART.
                    getFacesMessage(facesContext, null, null);
                facesContext.addMessage(null, fm);
                if (facesContext.isProjectStage(ProjectStage.Development)) {
                    log.log(Level.WARNING, "Problem reading multi-part form " +
                        "upload, likely due to a file upload being cut off " +
                        "from the client losing their network connection or " +
                        "closing their browser tab or window.", e);
                }
            }

            FileEntry.storeResultsForLaterInLifecycle(facesContext, clientId2Results);
            progressListenerResourcePusher.clear();
            clientId2Callbacks.clear();
            Arrays.fill(buffer, (byte) 0);
            buffer = null;

            // Map<String, List<String>> parameterListMap = new HashMap<String, List<String>>();
            Map<String, String[]> parameterMap = new HashMap<String, String[]>(
                ((parameterListMap.size() > 0) ? parameterListMap.size() : 1) );
            // ICE-6448 Support javascript-less environments by making the 
            // javascript set a flag that will determine if we do a full page
            // render of a partial page ajax update render
            boolean ajaxResponse = false;
            for(String key : parameterListMap.keySet()) {
                List<String> parameterList = parameterListMap.get(key);
                if (key.equals(FileEntryFormSubmit.FILE_ENTRY_AJAX_RESPONSE_MARKER)) {
                    ajaxResponse = true;
                    log.finest("FileEntryResourceHandler  ajaxResponse: " + parameterList);
                }
                String[] values = new String[parameterList.size()];
                values = parameterList.toArray(values);
                parameterMap.put(key, values);
            }

            if (!parameterMap.isEmpty()) {
                Object wrapper = null;
                if (isPortlet) {
                    wrapper = getPortletRequestWrapper(requestObject, parameterMap);
                    setPortletRequestWrapper(wrapper);
                } else {
                    wrapper = new FileUploadRequestWrapper((HttpServletRequest) requestObject, parameterMap);
                }
                facesContext.getExternalContext().setRequest(wrapper);

                log.finer("FileEntryResourceHandler  determined partial/ajax request: " + ajaxResponse);
                PartialViewContext pvc = facesContext.getPartialViewContext();
                if (pvc instanceof DOMPartialViewContext) {
                    ((DOMPartialViewContext) pvc).setAjaxRequest(ajaxResponse);
                }
                pvc.setPartialRequest(ajaxResponse);
            }
        }

        return wrapped.isResourceRequest(facesContext);
    }

    private static Object getPortletRequestWrapper(Object requestObject, Map map){
        Object wrapper = null;
        try {
            Class wrapperClass = Class.forName("org.icefaces.ace.component.fileentry.FileUploadPortletRequestWrapper");
            Class paramClasses[] = new Class[2];
            paramClasses[0] = Object.class;
            paramClasses[1] = Map.class;
            Constructor constructor = wrapperClass.getConstructor(paramClasses);
            wrapper = constructor.newInstance(requestObject,map);
        } catch (Exception e) {
            throw new RuntimeException("Problem getting FileUploadPortletRequestWrapper", e);
        }
        return wrapper;
    }
    
    private static void setPortletRequestWrapper(Object wrappedRequest){
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        Map requestMap = ec.getRequestMap();
        try {
            Object bridgeContext = requestMap.get("javax.portlet.faces.bridgeContext");
            Class requestClass = Class.forName("javax.portlet.PortletRequest");
            Class paramClasses[] = new Class[1];
            paramClasses[0] = requestClass;
            Method setPortletRequestMethod = bridgeContext.getClass().getMethod("setPortletRequest", paramClasses);
            Object paramObj[] = new Object[1];
            paramObj[0] = wrappedRequest;
            setPortletRequestMethod.invoke(bridgeContext,paramObj);
        } catch (Exception e) {
            throw new RuntimeException("Problem setting FileUploadPortletRequestWrapper", e);
        }
    }

    private static void handleMultipartPortion(
            FacesContext facesContext,
            String resolvedCharacterEncoding,
            Map<String, FileEntryResults> clientId2Results,
            Map<String, FileEntryCallback> clientId2Callbacks,
            Map<String, List<String>> parameterListMap,
            PartsManualProgress partsManualProgress,
            PushResourceSetup pushResourceSetup,
            byte[] buffer,
            MultipartFile mf)
            throws IOException {
        if (mf.isFormField()) {
            captureFormField(facesContext, parameterListMap,
                    resolvedCharacterEncoding, mf);
        } else {
            uploadFile(facesContext, clientId2Results, clientId2Callbacks,
                pushResourceSetup, buffer, mf);
        }
        if (partsManualProgress != null) {
            partsManualProgress.nextChunk();
        }
    }
    
    private static void captureFormField(
            FacesContext facesContext,
            Map<String, List<String>> parameterListMap,
            String resolvedCharacterEncoding,
            MultipartFile mf)
            throws IOException {
        String value;
        InputStream valueStream = mf.getInputStream();
        if (null != resolvedCharacterEncoding){
            value = Streams.asString(valueStream, resolvedCharacterEncoding);
        } else {
            value = Streams.asString(valueStream);
        }
        valueStream.close();

        String name = mf.getFieldName();
        log.finer("FileEntryResourceHandler  Form field name: " + name + "  value: " + value);

        List<String> parameterList = parameterListMap.get(name);
        if (parameterList == null) {
            parameterList = new ArrayList<String>(6);
            parameterListMap.put(name, parameterList);
        }
        parameterList.add(value);

        if ("ice.window".equals(name)) {
            WindowScopeManager.associateWindowIDToRequest(value, facesContext);
        }
    }

    private static void uploadFile(
            FacesContext facesContext,
            Map<String, FileEntryResults> clientId2Results,
            Map<String, FileEntryCallback> clientId2Callbacks,
            PushResourceSetup pushResourceSetup,
            byte[] buffer, MultipartFile item) {
        FileEntryResults results = null;
        FileEntryCallback callback = null;
        FileEntryResults.FileInfo fileInfo = null;
        FileEntryConfig config = null;

        File file = null;
        long[] fileSizeRead = new long[] {0L};
        FileEntryStatus status = FileEntryStatuses.UPLOADING;

        log.fine("vvvvvvvvvvvvvvv");
        try {
            String name = item.getFileName();
            String fieldName = item.getFieldName();
            String contentType = item.getContentType();
            log.fine(
                "File  name: " + name + "\n" +
                "File  fieldName: " + fieldName + "\n" +
                "File  contentType: " + contentType);

            // Remove \ escaping for ; ' "
            // IE gives us the whole path on the client, but we just
            //  want the client end file name, not the path
            String fileName = null;
            if (name != null && name.length() > 0) {
                fileName = name.replace("\\;", ";");
                fileName = fileName.replace("\\'", "'");
                fileName = fileName.replace("\\\"", "\"");
                fileName = trimInternetExplorerPath(fileName);
            }
            log.fine("File    IE adjusted fileName: " + fileName);
            
            // When no file name is given, that means the user did
            // not upload a file
            if (fileName != null && fileName.length() > 0) {
                String identifier = fieldName;
                config = FileEntry.retrieveConfigFromPreviousLifecycle(facesContext, identifier);
                if (config != null) {
                    // config being null might be indicative of a non-ICEfaces' file upload component in the form
                    log.fine("File    config: " + config);

                    results = clientId2Results.get(config.getClientId());
                    if (results == null) {
                        results = new FileEntryResults(config.isViaCallback());
                        clientId2Results.put(config.getClientId(), results);
                    }
                    log.fine("File    results: " + results);

                    fileInfo = new FileEntryResults.FileInfo();
                    fileInfo.begin(fileName, contentType);

                    pushResourceSetup.setPushResourcePathAndGroupName(
                            facesContext, config.getProgressResourceName(),
                            config.getProgressGroupName());

                    if (config.isViaCallback()) {
                        callback = clientId2Callbacks.get(config.getClientId());
                        if (callback == null) {
                            try {
                                callback = evaluateCallback(facesContext, config);
                            } catch(javax.el.ELException e) {
                                throw new CallbackException(e);
                            }
                        }
                    }
                    log.fine("File    callback: " + callback);

                    long availableTotalSize = results.getAvailableTotalSize(config.getMaxTotalSize());
                    long availableFileSize = config.getMaxFileSize();
                    int maxFileCount = config.getMaxFileCount();
                    log.finer(
                        "File    availableTotalSize: " + availableTotalSize + "\n" +
                        "File    availableFileSize: " + availableFileSize + "\n" +
                        "File    maxFileCount: " + maxFileCount);
                    if (results.getFiles().size() >= maxFileCount) {
                        status = FileEntryStatuses.MAX_FILE_COUNT_EXCEEDED;
                        fileInfo.prefail(status);
                        item.discardPrefailedFile(buffer);
                        if (callback != null) {
                            try {
                                callback.begin(fileInfo);
                                callback.end(fileInfo);
                            } catch(RuntimeException e) {
                                throw new CallbackException(e);
                            }
                        }
                    }
                    else {
                        if (callback != null) {
                            try {
                                callback.begin(fileInfo);
                            } catch(RuntimeException e) {
                                throw new CallbackException(e);
                            }
                        }
                        else {
                            String folder = calculateFolder(facesContext, config);
                            file = makeFile(config, folder, fileName);
                            log.fine("File    file: " + file);
                        }
                        
                        item.transferFile(callback, file, buffer, fileSizeRead, availableFileSize, availableTotalSize);
                        log.fine("File    fileSizeRead: " + fileSizeRead[0]);
                        if (fileSizeRead[0] > availableFileSize) {
                            status = FileEntryStatuses.MAX_FILE_SIZE_EXCEEDED;
                        } else if (fileSizeRead[0] > availableTotalSize) {
                            status = FileEntryStatuses.MAX_TOTAL_SIZE_EXCEEDED;
                        } else if (status == FileEntryStatuses.UPLOADING) {
                            status = FileEntryStatuses.SUCCESS;
                        }
                    }
                }
            }
            else { // If no file name specified
                log.fine("File    UNSPECIFIED_NAME");
                status = FileEntryStatuses.UNSPECIFIED_NAME;
                item.discardPrefailedFile(buffer);
            }
        }
        catch(CallbackException e) {
            status = FileEntryStatuses.PROBLEM_WITH_CALLBACK;
            handleCallbackException(
                facesContext, config.getClientId(), e.getCause());
        }
        catch(Exception e) {
            log.fine("File    Exception: " + e);
            if (status == FileEntryStatuses.UPLOADING ||
                    status == FileEntryStatuses.SUCCESS) {
                status = FileEntryStatuses.INVALID;
            }
            if (facesContext.isProjectStage(ProjectStage.Development)) {
                log.log(Level.WARNING, "Problem processing uploaded file", e);
            }
        }

        if (!status.isSuccess()) {
            fileSizeRead[0] = 0L;
            if (file != null) {
                log.fine("File    Unsuccessful file being deleted");
                file.delete();
                file = null;
            }
        }
        
        log.fine("File    Ending  status: " + status);
        if (results != null && fileInfo != null) {
            log.fine("File    Have results and fileInfo to fill-in");
            fileInfo.finish(file, fileSizeRead[0], status);
            results.addCompletedFile(fileInfo);
            if (callback != null) {
                try {
                    callback.end(fileInfo);
                } catch(RuntimeException e) {
                    status = FileEntryStatuses.PROBLEM_WITH_CALLBACK;
                    fileInfo.postfail(status);
                    handleCallbackException(
                            facesContext, config.getClientId(), e);
                }
            }
            log.fine("File    Added completed file");
        }
        log.fine("^^^^^^^^^^^^^^^");
    }

    protected static FileEntryCallback evaluateCallback(
            FacesContext facesContext, FileEntryConfig config) {
        String callbackEL = config.getCallbackEL();
        log.finer("File    evaluateCallback()  callbackEL: " + callbackEL);
        FileEntryCallback callback = null;
        try {
            callback = facesContext.getApplication().evaluateExpressionGet(
                    facesContext, callbackEL, FileEntryCallback.class);
            log.finer("File    evaluateCallback()  callback: " + callback);
            if (callbackEL != null && callback == null &&
                    facesContext.isProjectStage(ProjectStage.Development)) {
                log.warning("For the fileEntry component with the clientId " +
                        "of '" + config.getClientId() + "', the callback " +
                        "property is set but resolves to null. This might " +
                        "indicate an application error. The uploaded file " +
                        "will be saved to the server file-system.");
            }
        } catch(javax.el.ELException e) {
            if (facesContext.isProjectStage(ProjectStage.Development)) {
                log.log(Level.SEVERE, "For the fileEntry component with the " +
                        "clientId of '" + config.getClientId() + "'", e);
            }
            throw e;
        }
        return callback;
    }

    protected static void handleCallbackException(FacesContext facesContext,
            String clientId, Throwable e) {
        if (facesContext.isProjectStage(ProjectStage.Development)) {
            log.log(Level.SEVERE, "An exception was thrown by the callback " +
                    "for the fileEntry component with clientId of '" +
                    clientId + "'", e);
        }
    }

    protected static String calculateFolder(
            FacesContext facesContext, FileEntryConfig config) {
        String folder = null;
        // absolutePath takes precedence over relativePath
        if (config.getAbsolutePath() != null && config.getAbsolutePath().length() > 0) {
            folder = config.getAbsolutePath();
            log.finer("File    Using absolutePath: " + folder);
        }
        else {
            folder = CoreUtils.getRealPath(facesContext, config.getRelativePath());
            log.finer("File    Using relativePath: " + folder);
        }
        if (folder == null) {
            log.finer("File    folder is null");
            folder = "";
        }

        if (config.isUseSessionSubdir()) {
            String sessionId = CoreUtils.getSessionId(facesContext);
            if (sessionId != null && sessionId.length() > 0) {
                String FILE_SEPARATOR = System.getProperty("file.separator");
                if (folder != null && folder.trim().length() > 0) {
                    folder = folder + FILE_SEPARATOR;
                }
                folder = folder + sessionId;
                log.finer("File    Using sessionSubdir: " + folder);
            }
        }
        return folder;
    }
    
    protected static File makeFile(
            FileEntryConfig config, String folder, String fileName)
            throws IOException {
        File file = null;
        File folderFile = new File(folder);
        if (!folderFile.exists())
            folderFile.mkdirs();
        if (config.isUseOriginalFilename()) {
            file = new File(folderFile, fileName);
            log.finer("File    original  file: " + file);
        }
        else {
            file = File.createTempFile("ice_file_", null, folderFile);
            log.finer("File    sanitise  file: " + file);
        }
        return file;
    }

    protected static String trimInternetExplorerPath(String path) {
        String[] seps = new String[] {File.separator, "/", "\\"};
        for (String sep : seps) {
            String ret = afterLast(path, sep);
            if (!path.equals(ret)) {
                return ret;
            }
        }
        return path;
    }

    protected static String afterLast(String str, String seek) {
        int index = str.lastIndexOf(seek);
        if (index >= 0) {
            return str.substring(index + seek.length());
        }
        return str;
    }


    public static class CallbackException extends RuntimeException {
        CallbackException(RuntimeException e) {
            super(e);
        }
    }

    private static interface MultipartFile {
        public boolean isFormField();

        /**
         * @return content-type header
         */
        public String getContentType();

        /**
         * @return content-disposition header's name entry
         */
        public String getFieldName();

        /**
         * @return content-disposition header's filename entry
         */
        public String getFileName();

        public InputStream getInputStream() throws IOException;

        public void discardPrefailedFile(byte[] buffer) throws IOException;

        public void transferFile(FileEntryCallback callback, File file,
                byte[] buffer, long[] fileSizeRead, long availableFileSize,
                long availableTotalSize) throws Exception;
    }


    private static class CommonsFileUploadFile implements MultipartFile {
        private FileItemStream item;

        private CommonsFileUploadFile(FileItemStream item) {
            this.item = item;
        }

        public boolean isFormField() {
            return item.isFormField();
        }

        /**
         * @return content-type header
         */
        public String getContentType() {
            return item.getContentType();
        }

        /**
         * @return content-disposition header's name entry
         */
        public String getFieldName() {
            return item.getFieldName();
        }

        /**
         * @return content-disposition header's filename entry
         */
        public String getFileName() {
            return item.getName();
        }

        public InputStream getInputStream() throws IOException {
            return item.openStream();
        }

        /**
         * Since we use the ServletFileUpload.getItemIterator, it parses the
         * multipart as we read from it, so we need to read and discard bytes
         */
        public void discardPrefailedFile(byte[] buffer) throws IOException {
            InputStream in = getInputStream();
            try {
                while (in.read(buffer) >= 0) {}
            } finally {
                in.close();
            }
        }

        public void transferFile(FileEntryCallback callback, File file,
                byte[] buffer, long[] fileSizeRead, long availableFileSize,
                long availableTotalSize) throws Exception {
            OutputStream output = (file != null) ? new FileOutputStream(file) : null;
            InputStream in = getInputStream();
            try {
                boolean overQuota = false;
                while (true) {
                    int read = in.read(buffer);
                    if (read < 0) {
                        break;
                    }
                    fileSizeRead[0] += read;
                    if (!overQuota) {
                        if (fileSizeRead[0] > availableFileSize || fileSizeRead[0] > availableTotalSize) {
                            overQuota = true;
                        } else {
                            if (callback != null) {
                                try {
                                    callback.write(buffer, 0, read);
                                } catch(RuntimeException e) {
                                    throw new CallbackException(e);
                                }
                            }
                            else if (output != null) {
                                output.write(buffer, 0, read);
                            }
                        }
                    }
                }
            }
            catch(Exception e) {
                throw e;
            }
            finally {
                if (output != null) {
                    output.flush();
                    output.close();
                }
                in.close();
            }
        }
    }


    private static class PartFile implements MultipartFile {
        private javax.servlet.http.Part part;
        private PartsManualProgress partsManualProgress;

        private PartFile(javax.servlet.http.Part part, PartsManualProgress partsManualProgress) {
            this.part = part;
            this.partsManualProgress = partsManualProgress;
        }

        public boolean isFormField() {
            return (getFileName() == null);
        }

        /**
         * @return content-type header
         */
        public String getContentType() {
            return part.getContentType();
        }

        /**
         * @return content-disposition header's name entry
         */
        public String getFieldName() {
            return part.getName();
        }

        /**
         * @return content-disposition header's filename entry
         */
        public String getFileName() {
            String contentDispositionHeader = part.getHeader("content-disposition");
            return FileUploadBase.getFileName(contentDispositionHeader);
        }

        public InputStream getInputStream() throws IOException {
            return part.getInputStream();
        }

        public void discardPrefailedFile(byte[] buffer) throws IOException {
            partsManualProgress.updateRead(part.getSize());
            part.delete();
        }

        public void transferFile(FileEntryCallback callback, File file,
                byte[] buffer, long[] fileSizeRead, long availableFileSize,
                long availableTotalSize) throws Exception {
            try {
                long size = part.getSize();
                fileSizeRead[0] = size;
                if (size <= availableFileSize && size <= availableTotalSize) {
                    if (callback != null) {
                        InputStream in = getInputStream();
                        try {
                            while (true) {
                                int read = in.read(buffer);
                                if (read < 0) {
                                    break;
                                }
                                try {
                                    callback.write(buffer, 0, read);
                                } catch(RuntimeException e) {
                                    throw new CallbackException(e);
                                }
                                partsManualProgress.updateRead(read);
                            }
                        }
                        finally {
                            in.close();
                        }
                    } else if (file != null) {
                        part.write(file.getAbsolutePath());
                        partsManualProgress.updateRead(size);
                    }
                }
            }
            finally {
                if (callback != null) {
                    part.delete();
                }
            }
        }
    }


    /**
     * The request contentLength is the number of bytes in the whole request,
     * which includes all the multipart protocol overhead, form field data,
     * and file data. That means, to use contentLength to provide exact
     * progress information would require parsing the multipart ourselves,
     * which we're not, since the Parts are just handed to us. So, we'll use
     * a rough approximation, where we just apply the file data sizing to the
     * contentLength, and then when we're done we adjust it to 100%. The under-
     * reporting shouldn't be too far off as long as protocol and form field
     * data sizing is overshadowed by the file sizing. For very small files
     * this would not be the case, but then we'd likely not even get to show
     * progress then anyway.
     */
    private static class PartsManualProgress {
        private ProgressListener progressListener;
        private long read;
        private long total;
        private int chunkIndex;

        private PartsManualProgress(ProgressListener progressListener,
                long total) {
            this.progressListener = progressListener;
            this.total = total;
        }

        public void nextChunk() {
            this.chunkIndex++;
        }

        public void updateRead(long deltaRead) {
            read += deltaRead;
            if (deltaRead > 0) {
                progressListener.update(read, total, chunkIndex);
            }
        }
    }

    
    static final String APPLICATION_FORM_URLENCODED = "application/x-www-form-urlencoded";

    private static class FileUploadRequestWrapper extends HttpServletRequestWrapper {
        private static final String FACES_REQUEST = "Faces-Request";
        private static final String PARTIAL_AJAX = "partial/ajax";
        private static final String CONTENT_TYPE = "content-type";

        private Map<String,String[]> parameterMap;

        public FileUploadRequestWrapper(HttpServletRequest httpServletRequest,
                                        Map<String,String[]> parameterMap)
        {
            super(httpServletRequest);
            this.parameterMap = parameterMap;
        }

        public String getHeader(String name) {
            log.finest("getHeader()  " + name);
            if (name != null) {
                if (name.equals(FACES_REQUEST)) {
                    log.finest("getHeader()  FACES_REQUEST -> PARTIAL_AJAX");
                    return PARTIAL_AJAX;
                }
                else if (name.equals(CONTENT_TYPE)) {
                    return APPLICATION_FORM_URLENCODED;
                }
            }
            return super.getHeader(name);
        }

        public java.util.Enumeration<String> getHeaders(java.lang.String name) {
            log.finest("getHeaders()  " + name);
            if (name != null) {
                if (name.equals(FACES_REQUEST)) {
                    Vector<String> list = new Vector<String>(1);
                    list.add(PARTIAL_AJAX);
                    log.finest("getHeader()  FACES_REQUEST -> PARTIAL_AJAX");
                    return list.elements();
                }
                else if (name.equals(CONTENT_TYPE)) {
                    Vector<String> list = new Vector<String>(1);
                    list.add(APPLICATION_FORM_URLENCODED);
                    return list.elements();
                }
            }
            return super.getHeaders(name);
        }

        public int getIntHeader(java.lang.String name) {
            if (name != null) {
                if (name.equals(FACES_REQUEST))
                    throw new NumberFormatException("Can not convert " + FACES_REQUEST + " to integer");
                else if (name.equals(CONTENT_TYPE))
                    throw new NumberFormatException("Can not convert " + CONTENT_TYPE + " to integer");
            }
            return super.getIntHeader(name);
        }

        public java.util.Enumeration<String> getHeaderNames() {
            java.util.Vector<String> list = new java.util.Vector<String>();
            java.util.Enumeration<String> names = super.getHeaderNames();
            while (names != null && names.hasMoreElements()) {
                list.add(names.nextElement());
            }
            if (!list.contains(FACES_REQUEST))
                list.add(FACES_REQUEST);
            if (!list.contains(CONTENT_TYPE))
                list.add(CONTENT_TYPE);
            return list.elements();
        }
        
        
        // Returns a java.util.Map of the parameters of this request.
        public Map<String, String[]> getParameterMap() {
            if (parameterMap != null) {
                return Collections.unmodifiableMap(parameterMap);
            }
            return super.getParameterMap();
        }

        // Returns an Enumeration of String objects containing the names of
        // the parameters contained in this request.
        public Enumeration<String> getParameterNames() {
            if (parameterMap != null) {
                Vector<String> keyVec = new Vector<String>(parameterMap.keySet());
                return keyVec.elements();
            }
            return super.getParameterNames();
        }

        // Returns the value of a request parameter as a String, or null if
        // the parameter does not exist.
        public String getParameter(String name) {
            if (parameterMap != null) {
                if (!parameterMap.containsKey(name)) {
                    return null;
                }
                String[] values = parameterMap.get(name);
                if (values != null && values.length >= 1) {
                    return values[0];
                }
                return null; // Or "", since the key does exist?
            }
            return super.getParameter(name);
        }

        // Returns an array of String objects containing all of the values the
        // given request parameter has, or null if the parameter does not exist.
        public String[] getParameterValues(String name) {
            if (parameterMap != null) {
                if (!parameterMap.containsKey(name)) {
                    return null;
                }
                return parameterMap.get(name);
            }
            return super.getParameterValues(name);
        }

        public String getContentType() {
            return APPLICATION_FORM_URLENCODED;
        }
    }

}
