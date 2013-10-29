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

import org.icefaces.application.PushRenderer;
import org.icefaces.sample.portlet.chat.resources.ResourceUtil;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import java.util.*;
import java.util.logging.Logger;

/**
 * The ChatRoom class is the hub of the application.  It keeps track of
 * Participants (adding and removing) as well as the message history.
 * It is also responsible for firing Ajax Push requests when the state
 * of the application has changed.
 */
@ManagedBean (eager=true)
@ApplicationScoped
public class ChatRoom{

    private static Logger log = Logger.getLogger(Participant.class.getName());

    public static final String ROOM_RENDERER_NAME = "all";

    private Map participants = Collections.synchronizedMap(new HashMap());
    private List messages = Collections.synchronizedList(new ArrayList());


    public ChatRoom() {
    }

    public void addParticipant(Participant participant) {
        if (hasParticipant(participant)) {
            return;
        }
        participants.put(participant.getHandle(), participant);
        PushRenderer.addCurrentSession(ChatRoom.ROOM_RENDERER_NAME);
        String localizedMessage = ResourceUtil.getLocalizedMessage("joined", participant.getHandle());
        addMessage(participant, localizedMessage);
    }

    public void removeParticipant(Participant participant) {
        if (!hasParticipant(participant)) {
            return;
        }
        participants.remove(participant.getHandle());
        String localizedMessage = ResourceUtil.getLocalizedMessage("left", participant.getHandle());
        addMessage(participant, localizedMessage);
    }

    public String[] getHandles() {
        return (String[]) participants.keySet().toArray(new String[participants.size()]);
    }

    public int getNumberOfParticipants() {
        return participants.size();
    }

    public List getMessages() {
        return messages;
    }

    public int getNumberOfMessages() {
        return messages.size();
    }

    protected void addMessage(Message message) {
        messages.add(0, message.getFormattedMessage());
    }

    public void addMessage(Participant participant, String message) {
        if (participant != null && participant.getHandle() != null) {
            addMessage(new Message(participant, message));
            PushRenderer.render(ROOM_RENDERER_NAME);
        }
    }

    public boolean hasParticipant(Participant participant) {
        return participants.containsKey(participant.getHandle());
    }

    public List getMessages(int start, int number) {

        int totalMessages = messages.size();

        if (start > totalMessages) {
            start = 0;
        }

        if ((start + number) > totalMessages) {
            number = totalMessages - start;
        }

        // The use of subList can lead to ConcurrentModificationExceptions
//        return messages.subList(start, start + number);
        List subList = new ArrayList();
        for(int index = start; index < (start+number); index++){
            subList.add(messages.get(index));
        }
        return subList;

    }
}
