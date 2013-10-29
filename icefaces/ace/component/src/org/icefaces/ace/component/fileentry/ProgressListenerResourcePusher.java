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

import org.icefaces.apache.commons.fileupload.ProgressListener;
import org.icefaces.application.ResourceRegistry;
import org.icefaces.ace.util.JSONBuilder;
import javax.faces.context.FacesContext;
import javax.faces.application.Resource;
import java.util.Map;
import java.util.logging.Logger;

/**
 * When the upload has progress, pushes the progress resource to the browser
 */
public class ProgressListenerResourcePusher implements ProgressListener, PushResourceSetup {
    private static Logger log = Logger.getLogger(FileEntry.class.getName()+".push");
    final private static long MIN_INTERVAL = 2000L;

    private Map<String, FileEntryResults> clientId2Results;
    private ProgressResource pushResource;
    private String pushGroupName;
    private long lastPushTime;
    private long lastPercent;
    private int lastDeltaGottenPushed;

    ProgressListenerResourcePusher(
            Map<String, FileEntryResults> clientId2Results) {
        this.clientId2Results = clientId2Results;
        log.finest("ProgressListenerResourcePusher  clientId2Results: " + clientId2Results);
    }

    public void update(long read, long total, int chunkIndex) {
        if (read > 0 && total > 0) {
            boolean force = read == total;
            long currPercent = (read * 100L) / total;
            if (force || currPercent > lastPercent) {
                long now = System.currentTimeMillis();
                if (force || (now - lastPushTime) >= MIN_INTERVAL) {
                    if (tryPush(currPercent, force)) {
                        lastPushTime = now;
                        lastPercent = currPercent;
                    }
                }
            }
        }
        else {
            lastPercent = 0L;
        }
    }

    protected boolean tryPush(long percent, boolean force) {
        if (pushResource == null || pushGroupName == null) {
            log.finest("tryPush()  pushResource: " + pushResource + "  pushGroupName: " + pushGroupName);
            return false;
        }
        log.fine("tryPush()  percent: " + percent);

        int deltaGottenPushed = updateResourceContents(percent);
        log.finer("deltaGottenPushed: " + deltaGottenPushed + "  lastDeltaGottenPushed: " + lastDeltaGottenPushed);
        int localLastDeltaGottenPushed = lastDeltaGottenPushed;
        if (!force && deltaGottenPushed > 2 && deltaGottenPushed > localLastDeltaGottenPushed + 1) {
            lastDeltaGottenPushed = deltaGottenPushed;
            return true;
        }

        //POLL: Comment this section
        PushUtils.push(pushGroupName);

        log.finer("tryPush()  Pushed progress update");
        return true;
    }

    public void setPushResourcePathAndGroupName(FacesContext facesContext,
            String pushResourceName, String pushGroupName) {
        Resource res = ResourceRegistry.getResourceByName(facesContext, pushResourceName);
        log.fine("setPushResourcePathAndGroupName()\n" +
            "\n  pushResourceName: " + pushResourceName +
            "\n  pushResource: " + res +
            "\n  pushResource.class: " + (res == null ? "null" : res.getClass().getName()));
        this.pushResource = ((res instanceof ProgressResource) ?
                (ProgressResource) res : null);
        this.pushGroupName = pushGroupName;
    }

    void clear() {
        clientId2Results = null;
        pushResource = null;
        pushGroupName = null;
        lastPushTime = 0L;
        lastPercent = 0L;
        lastDeltaGottenPushed = 0;
    }

    private int updateResourceContents(long percent) {
        JSONBuilder contents = JSONBuilder.create().beginMap();
        contents.entry("percent", percent);
        contents.beginArray("results");
        for (String clientId : clientId2Results.keySet()) {
            contents.item(clientId);
        }
        contents.endArray();
        contents.endMap();
        log.finer("updateResourceContents()  contents: " + contents.toString());
        return pushResource.updateProgressInfo(contents.toString());
    }
}
