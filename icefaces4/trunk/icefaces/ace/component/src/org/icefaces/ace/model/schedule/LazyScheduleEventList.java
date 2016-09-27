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

package org.icefaces.ace.model.schedule;

import java.util.List;
import java.io.Serializable;

/**
 * This list represents a collection of schedule events that are loaded lazily.
 * All the events of a given month are loaded at a time in memory and in the client.
 * The load method must return a new list, based on the given month and year values.
 * In order to support persistence of the Add, Delete and Modify operations performed
 * by the ace:scheulde component, the following methods must be overriden as well:
 * add(ScheduleEvent e), remove(int index), and set(int index, ScheduleEvent element)
 */
public abstract class LazyScheduleEventList extends WrappedList<ScheduleEvent> implements Serializable {

	/**
     * Produce and return a new list of ScheduleEvent objects for a given month
     *
     * @param	year	the year number
     * @param	month	the zero-relative month number, according to java.util.Calendar
     * @return			a list fo ScheduleEvent objects
     */
	public abstract List<ScheduleEvent> load(int year, int month);
}