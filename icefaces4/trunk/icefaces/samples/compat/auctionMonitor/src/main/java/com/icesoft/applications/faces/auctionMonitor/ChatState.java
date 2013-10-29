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

import com.icesoft.applications.faces.auctionMonitor.beans.UserBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Vector;
import java.util.Random;

/**
 * Class used to contain application wide state information Holds a list of all
 * users currently in the chat Can also update everyone in this list by calling
 * reRender on each UserBean This is done in a seperate thread so processing can
 * continue normally
 */
public class ChatState {
    public static final String DEFAULT_COLOR = "#000000";
    private static final String[] ALL_COLORS = {"33CC33", "660033", "FF0033",
                                                "FF6633", "FFCC33", "FFFF33",
                                                "3333CC", "33CCCC", "6600CC",
                                                "FF00CC", "CC00CC", "00FF99",
                                                "99FF33", "996633", "990033",
                                                "999999", "CCCCCC", "000000"};
    private static Log log = LogFactory.getLog(ChatState.class);
    private static ChatState singleton = null;
    private Random generator = new Random(System.currentTimeMillis());
    private Vector userList = new Vector(0);
    private Vector colorList = new Vector(Arrays.asList(ALL_COLORS));
    private boolean stamp = true; // timestamps enabled on chat message log, default = true

    /**
     * Default constructor with no parameters, is private to fulfill the
     * singleton
     */
    private ChatState() {
    }

    public int getNumParticipants() {
        return (userList.size());
    }

    public boolean getTimeStampEnabled() {
        return (stamp);
    }

    public boolean toggleTimeStamp() {
        stamp = !stamp;
        return (stamp);
    }

    /**
     * Method to return a singleton instance of this class
     *
     * @return this ChatState object
     */
    public static synchronized ChatState getInstance() {
        if (singleton == null) {
            singleton = new ChatState();
        }
        return (singleton);
    }

    /**
     * Method to add the passed UserBean to the current user list
     *
     * @param child UserBean to add
     */
    public void addUserChild(UserBean child) {
        // Give the user a color, then add them to the list
        child.setColor(generateColorCode());
        userList.add(child);
    }

    /**
     * Method to remove the passed UserBean from the current user list
     *
     * @param child UserBean to remove
     * @return boolean true if the removal succeeded
     */
    public boolean removeUserChild(UserBean child) {
        try{
            // Make the user's color available again
            colorList.add(child.getColor());
            
            int index = 0;
            Iterator users = userList.iterator();
            UserBean current;
            while (users.hasNext()) {
                current = (UserBean) users.next();
    
                // Ensure the current object is not null
                // Otherwise casting it will cause an exception
                if (current != null) {
                    // Check if the current object equals the child to remove
                    if (current.equals(child)) {
                        userList.remove(index);
                        return (true);
                    }
                } else {
                    userList.remove(index);
                }
    
                index++;
            }
        }catch (Exception failedRemove) {
            failedRemove.printStackTrace();
        }

        return (false);
    }

    /**
     * Method to loop through all children UserBeans and move each
     * to the bottom of the chat log
    */
    public void updateAll() {
        Iterator users = userList.iterator();
        UserBean current;
        while (users.hasNext()) {
            current = (UserBean) users.next();

            if (current != null) {
                current.moveToBottom();
            } else {
                users.remove();
            }
        }

    }
    
    /**
     * Method to randomly select an HTML color code from a preset list
     * eg: #C62FD5
     *
     * @return String hex value (to be used directly in HTML tags)
     */
    private String generateColorCode() {
        // Try to regenerate the color list if it's empty
        if (colorList.isEmpty()) {
            colorList = new Vector(Arrays.asList(ALL_COLORS));
        }
        
        // Return a generated color
        try{
            return "#" + colorList.remove(generator.nextInt(colorList.size())).toString();
        }catch (Exception failed) { }
        
        return DEFAULT_COLOR;
    }
}
