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

package org.icefaces.tutorial.easyajaxpush.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.icefaces.tutorial.easyajaxpush.model.TextModel;

public class MessageBean implements Serializable {
	private static final int MAX_SIZE = 25;
	
	private List<TextModel> textList = new ArrayList<TextModel>(0);

	public MessageBean() {
	}
	
	public List<TextModel> getTextList() {
		return textList;
	}

	public void setTextList(List<TextModel> textList) {
		this.textList = textList;
	}
	
	public void addToList(String sessionId, String color) {
		textList.add(makeTextModel(sessionId, color));
		
		if (textList.size() > MAX_SIZE) {
			textList.clear();
		}
	}
	
	private TextModel makeTextModel(String sessionId, String color) {
		return new TextModel("User with session ID of " + sessionId + " selected color \"" + color + "\".",
						     color);
	}
}
