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

package org.icefaces.demo.basic;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;

@ManagedBean(name = "basic")
@SessionScoped
public class Basic implements Serializable  {
    Boolean visible = false;
    static String VISIBLE = "display: block";
    static String INVISIBLE = "display: none";

    public Boolean getVisible() {
        return visible;
    }

    public void toggle(ActionEvent ae) {
        visible = !visible;
    }
    
    public String getOneStyle()  {
        if (visible)  {
            return VISIBLE;
        }
        return INVISIBLE;
    }

    public String getTwoStyle()  {
        if (visible)  {
            return INVISIBLE;
        }
        return VISIBLE;
    }

    public String getTime()  {
        return Long.toString(System.currentTimeMillis());
    }

    String newItem = "";
    
    public String getNewItem()  {
        return newItem;
    }

    public void setNewItem(String newItem)  {
        this.newItem = newItem;
    }

    List<String> list = new ArrayList();
    public String add()  {
        list.add(newItem);
        newItem = "";
        return "";
    }

    public List<String> getItems()  {
        return list;
    }

    String target = "";
    public void setTarget(String target)  {
        this.target = target;
    }
    
    public String delete()  {
        list.remove(target);
        return "";
    }
    
}
