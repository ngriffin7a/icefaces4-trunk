/*
 * Copyright 2004-2014 ICEsoft Technologies Canada Corp.
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

package org.icefaces.ace.component.stackpane;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import java.io.IOException;
import java.util.logging.Logger;
import javax.faces.view.facelets.*;

public class StackPaneHandler extends ComponentHandler {
    private static final Logger logger = Logger.getLogger(StackPaneHandler.class.getName());
    private static final Runnable NOOP = new Runnable() {
        public void run() {
        }
    };

    private final TagAttribute facelet;
    private final TagAttribute client;

    public StackPaneHandler(ComponentConfig componentConfig) {
        super(componentConfig);
        Tag tag = componentConfig.getTag();
        facelet = tag.getAttributes().get("facelet");
        client = tag.getAttributes().get("client");
    }

    public void applyNextHandler(final FaceletContext ctx, final UIComponent c) throws java.io.IOException, javax.faces.FacesException, javax.el.ELException {
        StackPane pane = (StackPane) c;
        boolean notClient = client == null || client.getObject(ctx) == null || !client.getBoolean(ctx);
        boolean isFacelet = facelet != null && facelet.getObject(ctx) != null && facelet.getBoolean(ctx);
        if (notClient && isFacelet) {
            Runnable run = new Runnable() {
                public void run() {
                    try {
                        StackPaneHandler.super.applyNextHandler(ctx, c);
                    } catch (IOException e) {
                        throw new FacesException(e);
                    }
                }
            };
            pane.delayChildrenCreation(run);
        } else {
            super.applyNextHandler(ctx, c);
            pane.delayChildrenCreation(NOOP);
        }
    }
}
