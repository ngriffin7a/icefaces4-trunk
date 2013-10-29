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

package org.icefaces.sample.portlet.chat;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Data bean for timestamping and formatting chat messages.
 */
public class Message {

    private String message;
    private String formattedMessage;
    private Participant participant;
    private Date timestamp;

    private static SimpleDateFormat formatter =
            new SimpleDateFormat("hh:mm:ss");


    public Message() {
        timestamp = new Date();
    }

    public Message(Participant participant, String message) {
        this();
        this.message = message;
        this.participant = participant;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return formatter.format(timestamp);
    }

    public void setTimestamp(Date timestamp) {
        //
    }

    public Participant getParticipant() {
        return participant;
    }

    public void setParticipant(Participant participant) {
        this.participant = participant;
    }

    public String getFormattedMessage() {
        if (formattedMessage == null) {
            StringBuffer buff = new StringBuffer();
            buff.append("[");
            buff.append(getTimestamp());
            buff.append(" - ");
            buff.append(participant.getHandle());
            buff.append("] ");
            buff.append(message);
            formattedMessage = buff.toString();
        }
        return formattedMessage;
    }

    public String toString() {
        return getFormattedMessage();
    }
}
