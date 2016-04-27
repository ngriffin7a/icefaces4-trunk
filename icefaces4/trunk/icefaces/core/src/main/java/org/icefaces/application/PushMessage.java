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
package org.icefaces.application;

import static org.icesoft.util.ObjectUtilities.*;
import static org.icesoft.util.PreCondition.checkArgument;
import static org.icesoft.util.StringUtilities.isNotNullAndIsNotEmpty;

import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PushMessage
extends PushOptions {
    private static final Logger LOGGER = Logger.getLogger(PushMessage.class.getName());

    public static final String SUBJECT = "subject";
    public static final String DETAIL = "detail";
    public static final String TARGET_URI = "targetURI";
    public static final String FORCED = "forced";

    public PushMessage(final String subject, final String detail, final URI targetURI)
    throws IllegalArgumentException {
        this(subject, detail, targetURI, false);
    }

    public PushMessage(final String subject, final String detail, final URI targetURI, final boolean forced)
    throws IllegalArgumentException {
        setSubject(subject);
        setDetail(detail);
        setTargetURI(targetURI);
        setForced(forced);
    }

    public PushMessage(final String subject, final URI targetURI)
    throws IllegalArgumentException {
        this(subject, targetURI, false);
    }

    public PushMessage(final String subject, final URI targetURI, final boolean forced)
    throws IllegalArgumentException {
        setSubject(subject);
        setTargetURI(targetURI);
        setForced(forced);
    }

    public String getDetail() {
        return (String)getAttribute(DETAIL);
    }

    public String getSubject() {
        return (String)getAttribute(SUBJECT);
    }

    public String getTargetUri() {
        return (String)getAttribute(TARGET_URI);
    }

    public boolean isForced() {
        return (Boolean)getAttribute(FORCED);
    }

    public void setDetail(final String detail)
    throws IllegalArgumentException {
        checkArgument(
            isNotNullAndIsNotEmpty(detail),
            "Illegal argument detail: '" + detail + "'.  Argument cannot be null or empty."
        );
        putAttribute(DETAIL, detail);
    }

    public void setForced(final boolean forced)
    throws IllegalArgumentException {
        putAttribute(FORCED, forced);
    }

    public void setSubject(final String subject)
    throws IllegalArgumentException {
        checkArgument(
            isNotNullAndIsNotEmpty(subject),
            "Illegal argument subject: '" + subject + "'.  Argument cannot be null or empty."
        );
        putAttribute(SUBJECT, subject);
    }

    public void setTargetURI(final URI targetURI)
    throws IllegalArgumentException {
        checkArgument(
            isNotNull(targetURI),
            "Illegal argument targetURI: '" + targetURI + "'.  Argument cannot be null."
        );
        putAttribute(TARGET_URI, targetURI.toString());
    }

    @Override
    public String toString() {
        return
            new StringBuilder().
                append("PushMessage[").
                    append(classMembersToString()).
                append("]").
                    toString();
    }
}