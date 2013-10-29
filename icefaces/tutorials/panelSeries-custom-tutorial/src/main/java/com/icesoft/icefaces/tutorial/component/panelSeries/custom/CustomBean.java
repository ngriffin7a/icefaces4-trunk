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

package com.icesoft.icefaces.tutorial.component.panelSeries.custom;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;

/**
 * <p>A custom backing bean uses a Java List to store a list of personals</p>
 */
public class CustomBean implements Serializable {

	private List customObjectList;

	public List getCustomObjectList(){
		return this.customObjectList;
	}

	public void setCustomObjectList(List customObjectList){
		this.customObjectList = customObjectList;
	}


	public CustomBean(){

		customObjectList = new ArrayList(5);

		customObjectList.add(new CustomObject("Jessica Cooper","dcooper@icesoft.com",23,"403-123-4567",
											  false,"",new Date()
											  ));
		customObjectList.add(new CustomObject("Carlo DiNossio","cdinossio@icesoft.com",45,"403-234-5671",
											  false,"",new Date()
											  ));
		customObjectList.add(new CustomObject("Carolyn Jackson","achiu@icesoft.com",21,"403-345-6712",
											  false,"",new Date()
											  ));
		customObjectList.add(new CustomObject("Ben Wallace","bwallace@icesoft.com",32,"403-456-7123",
											  false,"",new Date()
											  ));
		customObjectList.add(new CustomObject("Justin Norton","jnorton@icesoft.com",27,"403-123-4567",
											  false,"",new Date()
											  ));

	}
}