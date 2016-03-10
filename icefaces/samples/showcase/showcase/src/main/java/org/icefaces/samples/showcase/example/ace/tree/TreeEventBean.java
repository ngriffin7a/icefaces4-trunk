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

package org.icefaces.samples.showcase.example.ace.tree;

import java.io.Serializable;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

import org.icefaces.ace.event.TreeEvent;

@ManagedBean(name = TreeEventBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class TreeEventBean implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final String BEAN_NAME = "treeEventBean";

	public String getBeanName() {
		return BEAN_NAME;
	}

	public final String DEFAULT_MESSAGE = "Please select/deselect/expand/contract/reorder tree nodes";
	public final int MAX_LIST_SIZE = 20;
	private Format formatter;
	private String message;
	private ArrayList<String> list;

	public TreeEventBean() {
		formatter = new SimpleDateFormat("HH:mm:ss");
		message = DEFAULT_MESSAGE;
		list = new ArrayList<String>(MAX_LIST_SIZE);
		list.add(DEFAULT_MESSAGE);
	}

	public void treeEventListener(AjaxBehaviorEvent event) {
		LocationNodeImpl locationNode;
		if (event instanceof TreeEvent) {
			TreeEvent treeEvent = (TreeEvent) event;
			Object data = treeEvent.getObject();
			if (data != null) {
				locationNode = (LocationNodeImpl) data;

				if (treeEvent.isExpandEvent()) {
					message = locationNode.getName() + " " + locationNode.getType() + " expanded @ " + formatter.format(new Date());
				} else if (treeEvent.isSelectEvent()) {
					message = locationNode.getName() + " " + locationNode.getType() + " selected @ " + formatter.format(new Date());
				} else if (treeEvent.isDeselectEvent()) {
					message = locationNode.getName() + " " + locationNode.getType() + " deselected @ " + formatter.format(new Date());
				} else if (treeEvent.isReorderEvent()) {
					message = locationNode.getName() + " " + locationNode.getType() + " reordered @ " + formatter.format(new Date());
				} else if (treeEvent.isContractEvent()) {
					message = locationNode.getName() + " " + locationNode.getType() + " contracted @ " + formatter.format(new Date());
				}
				
				if (list.get(0).equals(DEFAULT_MESSAGE)) {
					list.clear();
				}
				if (list.size() < MAX_LIST_SIZE) {
					list.add(message);
				} else {
					list.clear();
					list.add(message);
				}
			}

		}
	}

	public ArrayList<String> getList() {
		return list;
	}

	public void setList(ArrayList<String> list) {
		this.list = list;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
