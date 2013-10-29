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

package com.icesoft.applications.faces.auctionMonitor;

import java.util.ArrayList;

/**
 * Class used to add chat log functionality to the standard ArrayList Some
 * examples are timestamps, colored nicknames, etc.
 */
public class MessageLog extends ArrayList {
    private static final String DEFAULT_COLOR = "Black";

    public MessageLog(int size) {
        super(size);
    }

    public String getMessageAt(int index) {
        return (((Message) get(index)).get());
    }

    /**
     * Override method to add a chat message, with a colored username
     *
     * @param nick nickname of sender, message to use, and color of nickname
     * @return true (as per the general contract of Collection.add)
     */
    public boolean addMessage(String nick, String message, String color) {
        if (color == null) {
            color = DEFAULT_COLOR;
        }

        add(new Message(nick, message, color));

        return (true);
    }
}
