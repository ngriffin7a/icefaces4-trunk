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

package org.icefaces.samples.showcase.example.ace.progressbar.utilityBeans;

import java.io.Serializable;
import java.util.ArrayList;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;


@ManagedBean(name="pushManagementBean")
@ApplicationScoped
public class PushManagementBean implements Serializable
{
    private static int MAX_SIZE = 13; //limit amount of messages
    private ArrayList<PushMessage> messages;

    public PushManagementBean() {
        messages = new ArrayList<PushMessage>(0);
    }
    
    public void addMessage(String id, String message) 
    {
        messages.add(createPushMessage(id, message));
        flushMessagesIfOverLimit();
    }
    
    public void addMessage(PushMessage message) 
    {
        messages.add(message);
        flushMessagesIfOverLimit();
    }
    
    public void clearMessages(){
        messages.clear();
    }
    
    private void flushMessagesIfOverLimit()
    {
        if (messages.size() > MAX_SIZE)
            clearMessages();
    }
    
    private PushMessage createPushMessage(String id, String message) {
        return new PushMessage("ID: " +id +": "+"", message);
    }

    public ArrayList<PushMessage> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<PushMessage> messages) {
        this.messages = messages;
    }
}
