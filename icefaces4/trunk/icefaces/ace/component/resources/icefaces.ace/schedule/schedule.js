/*
 * Copyright 2004-2016 ICEsoft Technologies Canada Corp.
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

if (!window['ice']) window.ice = {};
if (!window.ice['ace']) window.ice.ace = {};

ice.ace.Schedule = function(id, cfg) {
	this.id = id;
	this.jqId = ice.ace.escapeClientId(id);
	this.jqRoot = ice.ace.jq(this.jqId);
	this.jq = this.jqRoot.find('.schedule-main');
	this.cfg = cfg;
	this.events = cfg.events;
	var self = this;

	// order events according to their server-side index
	this.eventsMap = {};
	var i;
	for (i = 0; i < cfg.events.length; i++) {
		var event = cfg.events[i];
		this.eventsMap[''+event.index] = event;
	}

	this.cfg.currentYear = parseInt(document.getElementById(this.id + '_currentYear').getAttribute('value'));
	this.cfg.currentMonth = parseInt(document.getElementById(this.id + '_currentMonth').getAttribute('value'));
	this.cfg.currentDay = parseInt(document.getElementById(this.id + '_currentDay').getAttribute('value'));
	
	this.renderConfiguration = {};
	if (this.cfg.viewMode == 'week') {
		this.renderConfiguration.render = function() { return self.renderWeekView.call(self); };
		this.renderConfiguration.doneRendering = function() { self.renderWeekEvents.call(self); };
	} else if (this.cfg.viewMode == 'day') {
		this.renderConfiguration.render = function() { return self.renderDayView.call(self); };
		this.renderConfiguration.doneRendering = function() { self.renderDayEvents.call(self); };
	} else {
		this.renderConfiguration.render = function() { return self.renderMonthView.call(self); };
		this.renderConfiguration.doneRendering = function() { self.renderMonthEvents.call(self); };
	}

	this.render();

	var selectedDate = document.getElementById(this.id + '_selectedDate').getAttribute('value');
	// add selected styling
	if (self.cfg.viewMode == 'week') {
		self.jqRoot.find('.schedule-selected').removeClass('schedule-selected');
		var dow = self.extractDayOfWeek(self.jqRoot.find('.calendar-day-' + selectedDate).get(0));
		self.jqRoot.find('.schedule-dow-header.schedule-dow-' + dow).addClass('schedule-selected');
	} else {
		self.jqRoot.find('.schedule-selected').removeClass('schedule-selected');
		self.jqRoot.find('.calendar-day-' + selectedDate + ' .schedule-state').addClass('schedule-selected');
	}

	var behaviors = self.cfg.behaviors;
	this.jqRoot.delegate('.schedule-event', 'click', function(event) {
		event.stopPropagation();
		var node = event['target'];
		var eventIndex = self.extractEventIndex(node);
		var eventData = self.eventsMap[''+eventIndex];
		// add selected styling
		if (self.cfg.viewMode == 'week') {
			self.jqRoot.find('.schedule-selected').removeClass('schedule-selected');
			var dow = self.extractDayOfWeek(self.jqRoot.find('.calendar-day-' + eventData.startDate).get(0));
			self.jqRoot.find('.schedule-dow-header.schedule-dow-' + dow).addClass('schedule-selected');
		} else {
			self.jqRoot.find('.schedule-selected').removeClass('schedule-selected');
			self.jqRoot.find('.calendar-day-' + eventData.startDate + ' .schedule-state').addClass('schedule-selected');
		}
		document.getElementById(self.id + '_selectedDate').setAttribute('value', eventData.startDate);
		if (behaviors && behaviors.eventClick) {
			self.sendClickRequest(event, 'event', eventIndex);
		}
	});
	if (cfg.eventDetails != 'disabled') {
		this.jqRoot.delegate('.schedule-event, .schedule-list-event', 'click', function(event) {
			event.stopImmediatePropagation();
			var node = event['target'];
			var eventIndex = self.extractEventIndex(node);
			var eventData = self.eventsMap[''+eventIndex];
			var markup = self.getEventDetailsMarkup(eventData, false,
				self.cfg.isEventEditing, self.cfg.isEventDeletion);
			if (self.cfg.eventDetails == 'sidebar')
				self.displayEventDetailsSidebar(markup);
			else
				self.displayEventDetailsPopup(markup);
		});
	}
	if (cfg.displayTooltip) {
		this.jqRoot.delegate('.schedule-event', 'mouseover', function(event) {
			var node = event['target'];
			var eventIndex = self.extractEventIndex(node);
			var eventData = self.eventsMap[''+eventIndex];
			var markup = self.getEventDetailsMarkup(eventData);
			if (node.tagName == 'DIV') {
				self.displayEventDetailsTooltip(markup, node);
			} else {
				self.displayEventDetailsTooltip(markup, node.parentNode);
			}
		});
		this.jqRoot.delegate('.schedule-event', 'mouseout', function(event) {
			self.hideEventDetailsTooltip();
		});
	}
	if (behaviors && behaviors.dayDblclick) {
		this.jqRoot.delegate('.day', 'dblclick', function(event) {
			var node = event['target'];
			node = node.className.indexOf('day-number') > -1 ? node.parentNode : node;
			node = node.className.indexOf('schedule-state') > -1 ? node.parentNode : node;
			var date = self.extractEventDate(node);
			self.sendClickRequest(event, 'day', date);
		});
	}
	if (behaviors && behaviors.timeDblclick) {
		this.jqRoot.delegate('.schedule-cell', 'dblclick', function(event) {
			var node = event['target'];
			node = node.className.indexOf('day-number') > -1 ? node.parentNode : node;
			node = node.className.indexOf('schedule-state') > -1 ? node.parentNode : node;
			var date = self.extractEventDate(node);
			var time = self.extractEventTime(node);
			self.sendClickRequest(event, 'time', date + ' ' + time);
		});
	}
	if (self.cfg.isEventAddition) {
		this.jqRoot.delegate('.day, .schedule-cell', 'dblclick', function(event) {
			var date, time;
			var node = event['target'];
			node = node.className.indexOf('day-number') > -1 ? node.parentNode : node;
			node = node.className.indexOf('schedule-state') > -1 ? node.parentNode : node;
			date = self.extractEventDate(node);
			time = self.extractEventTime(node);
			var eventData = {startDate: date, startTime: time, endDate: date, endTime: '', title: '', location: '', notes: '', index: ''};
			var markup = self.getEventDetailsMarkup(eventData, true, false, false);
			if (self.cfg.eventDetails == 'sidebar')
				self.displayEventDetailsSidebar(markup);
			else
				self.displayEventDetailsPopup(markup);
		});
	}
	this.jqRoot.delegate('.schedule-state', 'mouseenter mouseover', function(event) {
		event.stopPropagation();
		var node = ice.ace.jq(event['target']);
		var stateNode = node.closest('.schedule-state');
		if (stateNode.hasClass('schedule-state') && !stateNode.hasClass('ui-state-hover')) {
			stateNode.addClass('ui-state-hover');
			if (stateNode.hasClass('ui-state-highlight')) {
				stateNode.removeClass('ui-state-highlight');
				stateNode.addClass('ui-state-highlight-disabled');
			}
		}
	});
	this.jqRoot.delegate('.schedule-state', 'mouseleave', function(event) {
		event.stopPropagation();
		var node = ice.ace.jq(event['target']);
		var stateNode = node.closest('.schedule-state');
		if (stateNode.hasClass('schedule-state') && stateNode.hasClass('ui-state-hover')) {
			self.jqRoot.find('.schedule-state').removeClass('ui-state-hover');
			if (stateNode.hasClass('ui-state-highlight-disabled')) {
				stateNode.removeClass('ui-state-highlight-disabled');
				stateNode.addClass('ui-state-highlight');
			}
		}
	});
	if (this.cfg.viewMode == 'month' || this.cfg.viewMode == 'week') {
		this.jqRoot.delegate('.schedule-state', 'click', function(event) {
			event.stopPropagation();
			var node = ice.ace.jq(event['target']);
			var stateNode = node.closest('.schedule-state');
			if (stateNode.hasClass('schedule-state')) {
				// add selected styling
				if (self.cfg.viewMode == 'week') {
					self.jqRoot.find('.schedule-selected').removeClass('schedule-selected');
					var dow = self.extractDayOfWeek(stateNode.parent().get(0));
					self.jqRoot.find('.schedule-dow-header.schedule-dow-' + dow).addClass('schedule-selected');
				} else {
					self.jqRoot.find('.schedule-selected').removeClass('schedule-selected');
					stateNode.addClass('schedule-selected');
				}
				var date = self.extractEventDate(stateNode.parent().get(0));
				document.getElementById(self.id + '_selectedDate').setAttribute('value', date);
			}
		});
	}
	this.jqRoot.delegate('.schedule-list-title', 'click', function(event) {
		self.expandEventList();
	});
	this.jqRoot.delegate('.schedule-details-title', 'click', function(event) {
		self.expandEventDetails();
	});
	if (self.cfg.eventDetails == 'popup') {
		this.jqRoot.on('keydown', function(e) {
			if (e.keyCode == 27) {
				ice.ace.jq(document.getElementById(self.id)).find('.schedule-details-popup-content').dialog('close');
			}
		});
	}
};

ice.ace.Schedule.prototype.render = function() {
	var mainContainer = this.jqRoot.find('.schedule-main');
	mainContainer.children().remove();
	mainContainer.html(this.renderConfiguration.render.apply(this, []));

	this.addNavigationListeners();
	this.addResizeListeners();

	this.renderConfiguration.doneRendering.apply(this, []);
};

ice.ace.Schedule.prototype.extractEventIndex = function(node) {
	if (node.tagName == 'SPAN') node = node.parentNode; // event text in month view
	var result = 0;
	var classes = node.className.split(' ');
	var i;
	for (i = 0; i < classes.length; i++) {
		var styleClass = classes[i];
		if (styleClass.indexOf('schedule-event-') == 0) {
			result = styleClass.substring(15);
			break;
		}
	}
	return result;
};

ice.ace.Schedule.prototype.extractEventDate = function(node) {
	var result = '';
	var classes = node.className.split(' ');
	var i;
	for (i = 0; i < classes.length; i++) {
		var styleClass = classes[i];
		if (styleClass.indexOf('calendar-day-') == 0) {
			result = styleClass.substring(13);
			break;
		}
	}
	return result;
};

ice.ace.Schedule.prototype.extractEventTime = function(node) {
	var result = '';
	var classes = node.className.split(' ');
	var i;
	for (i = 0; i < classes.length; i++) {
		var styleClass = classes[i];
		if (styleClass.indexOf('schedule-time-') == 0) {
			result = styleClass.substring(14);
			break;
		}
	}
	if (result) result = result.substring(0,2) + ':' + result.substring(2);
	return result;
};

ice.ace.Schedule.prototype.extractDayOfWeek = function(node) {
	var result = '';
	var classes = node.className.split(' ');
	var i;
	for (i = 0; i < classes.length; i++) {
		var styleClass = classes[i];
		if (styleClass.indexOf('schedule-dow-') == 0) {
			result = styleClass.substring(13);
			break;
		}
	}
	return result;
};

ice.ace.Schedule.prototype.getEventDetailsMarkup = function(data, isEventAddition, isEventEditing, isEventDeletion) {
	if (data) {// *** escape HTML characters
		var markup;
		if (isEventAddition || isEventEditing) {
			markup = '<table><tr><td>Start&nbsp;Date:</td><td><input type="text" name="'+this.id+'_date" value="'+data.startDate+'"/></td></tr><tr><td>Start&nbsp;Time:</td><td>'+this.getHourSelectionMarkup(data.startTime)+'&nbsp;:&nbsp;'+this.getMinuteSelectionMarkup(data.startTime)+'</td></tr><tr><td>End&nbsp;Date:</td><td><input type="text" name="'+this.id+'_endDate" value="'+data.endDate+'"/></td></tr><tr><td>End&nbsp;Time:</td><td>'+this.getHourSelectionMarkup(data.endTime)+'&nbsp;:&nbsp;'+this.getMinuteSelectionMarkup(data.endTime)+'</td></tr><tr><tr><td>Title:</td><td><input type="text" name="'+this.id+'_title" value="'+data.title+'"/></td></tr><tr><td>Location:</td><td><input type="text" name="'+this.id+'_location" value="'+data.location+'"/></td></tr><tr><td>Notes:</td><td><textarea name="'+this.id+'_notes">'+data.notes+'</textarea></td></tr></table><input type="hidden" name="'+this.id+'_index" value="'+data.index+'"/>';
			if (isEventEditing) {
				markup += '<input type="hidden" name="'+this.id+'_old_startDate" value="'+data.startDate+'"/><input type="hidden" name="'+this.id+'_old_startTime" value="'+data.startTime+'"/><input type="hidden" name="'+this.id+'_old_endDate" value="'+data.endDate+'"/><input type="hidden" name="'+this.id+'_old_endTime" value="'+data.endTime+'"/><input type="hidden" name="'+this.id+'_old_title" value="'+data.title+'"/><input type="hidden" name="'+this.id+'_old_location" value="'+data.location+'"/><input type="hidden" name="'+this.id+'_old_notes" value="'+data.notes+'"/>';
			}
		} else {
			markup = '<table><tr><td>Start Date:</td><td>'+data.startDate+'</td></tr><tr><td>Start Time:</td><td>'+data.startTime+'</td></tr><tr><td>End Date:</td><td>'+data.endDate+'</td></tr><tr><td>End Time:</td><td>'+data.endTime+'</td></tr><tr><td>Title:</td><td>'+data.title+'</td></tr><tr><td>Location:</td><td>'+data.location+'</td></tr><tr><td>Notes:</td><td>'+data.notes+'</td></tr></table>';
		}
		if (data.styleClass) markup += '<input type="hidden" name="'+this.id+'_styleClass" value="'+data.styleClass+'"/>';
		if (data.id) markup += '<input type="hidden" name="'+this.id+'_id" value="'+data.id+'"/>';
		if (isEventAddition) markup += '<button onclick="ice.ace.instance(\''+this.id+'\').sendEditRequest(event, \'add\');return false;">Add</button>';
		else {
			if (isEventEditing) {
				var closeDetailsMarkup = '';
				if (this.cfg.eventDetails == 'popup') {
					closeDetailsMarkup = 'ice.ace.jq(document.getElementById(\''+this.id+'\')).find(\'.schedule-details-popup-content\').dialog(\'close\');';
				} else if (this.cfg.eventDetails == 'sidebar') {
					closeDetailsMarkup = 'ice.ace.instance(\''+this.id+'\').expandEventList();';
				}
				markup += '<button onclick="ice.ace.instance(\''+this.id+'\').sendEditRequest(event, \'edit\');'
					+ closeDetailsMarkup + 'return false;">Save</button> ';
			}
			if (isEventDeletion) markup += '<span><button onclick="ice.ace.instance(\''+this.id+'\').confirmDeletion(this);return false;">Delete</button><span style="display:none;"><span>Are you sure? </span><button onclick="ice.ace.instance(\''+this.id+'\').sendEditRequest(event, \'delete\');return false;">Yes</button> <button onclick="ice.ace.instance(\''+this.id+'\').cancelDeletion(this);return false;">No</button></span></span>';
		}
		return markup;
	} else {
		return '<div>No Data</div>';
	}
}

ice.ace.Schedule.prototype.getHourSelectionMarkup = function(time) {
	var hour = time.substring(0,2);
	var markup = '<select><option value="hh">hh</option>';
	var i;
	for (i = 0; i < 24; i++) {
		var value = ( i < 10 ? '0' : '') + i;
		var selected = hour == value ? ' selected' : '';
		markup += '<option value="'+value+'"'+selected+'>'+value+'</option>';
	}
	markup += '<select>';
	return markup;
};

ice.ace.Schedule.prototype.getMinuteSelectionMarkup = function(time) {
	var minute = time.substring(3);
	var markup = '<select><option value="mm">mm</option>';
	var i;
	for (i = 0; i < 60; i = i + 5) {
		var value = ( i < 10 ? '0' : '') + i;
		var selected = minute == value ? ' selected' : '';
		markup += '<option value="'+value+'"'+selected+'>'+value+'</option>';
	}
	markup += '<select>';
	return markup;
};

ice.ace.Schedule.prototype.addTimeParameters = function(params) {
	var detailsContainerClass = this.cfg.eventDetails == 'sidebar' ?
		'.schedule-details-content' : '.schedule-details-popup-content';
	var timeInputs = this.jqRoot.find(detailsContainerClass).find('select');

	if (timeInputs.size() >= 4) {
		var startHour = timeInputs.get(0).value;
		var startMinute = timeInputs.get(1).value;
		var endHour = timeInputs.get(2).value;
		var endMinute = timeInputs.get(3).value;

		if (startHour == 'hh') params[this.id + "_time"] = '';
		else {
			if (startMinute == 'mm') startMinute = '00';
			params[this.id + "_time"] = startHour + ':' + startMinute;
		}

		if (endHour == 'hh') params[this.id + "_endTime"] = '';
		else {
			if (endMinute == 'mm') endMinute = '00';
			params[this.id + "_endTime"] = endHour + ':' + endMinute;
		}
	}
};

ice.ace.Schedule.prototype.confirmDeletion = function(button) {
	ice.ace.jq(button).hide().siblings().show();
};

ice.ace.Schedule.prototype.cancelDeletion = function(button) {
	ice.ace.jq(button.parentNode).hide().siblings().show();
};

ice.ace.Schedule.prototype.displayEventDetailsPopup = function(markup) {
	var eventDetails = ice.ace.jq(this.jqId).find('.schedule-details-popup-content');
	eventDetails.html(markup);
	eventDetails.dialog({dialogClass: 'schedule-details-popup', resizable: false, width: 320});
	eventDetails.find('input[type="text"]:eq(0),input[type="text"]:eq(1)').datepicker({dateFormat: 'yy-mm-dd'});
	eventDetails.find('button').button();
	this.addDefaultDurationFunctionality()
};

ice.ace.Schedule.prototype.addDefaultDurationFunctionality = function() {
	var self = this;
	var displayLocation = self.cfg.eventDetails == 'sidebar' ? '' : 'popup-';
	var timeInputs = ice.ace.jq(this.jqId).find('.schedule-details-'+displayLocation+'content').find('select');

	if (timeInputs.size() >= 4) {
		var startHour = timeInputs.get(0);
		var startMinute = timeInputs.get(1);
		var endHour = timeInputs.get(2);
		var endMinute = timeInputs.get(3);
		var startDate = ice.ace.jq(self.jqId).find('.schedule-details-'+displayLocation+'content')
			.find('input[type="text"]:eq(0)');
		var endDate = ice.ace.jq(self.jqId).find('.schedule-details-'+displayLocation+'content')
			.find('input[type="text"]:eq(1)');

		var addDefaultDuration = function() {
			if (startHour.value != 'hh' && startMinute.value != 'mm') {
				var startDateValue = startDate.datepicker('getDate');
				if (startDateValue) {
					var endDateValue = new Date();
					endDateValue.setFullYear(startDateValue.getFullYear());
					endDateValue.setMonth(startDateValue.getMonth());
					endDateValue.setDate(startDateValue.getDate());
					endDateValue.setHours(parseInt(startHour.value));
					endDateValue.setMinutes(parseInt(startMinute.value) + self.cfg.defaultDuration);

					if (endHour.value == 'hh' && endMinute.value == 'mm') {
						endDate.datepicker('setDate', endDateValue.getFullYear() + '-'
							+ (endDateValue.getMonth() + 1) + '-'
							+ endDateValue.getDate());
						var endHours = endDateValue.getHours();
						ice.ace.jq(endHour).val(endHours < 10 ? '0' + endHours : '' + endHours);
						var endMinutes = endDateValue.getMinutes();
						ice.ace.jq(endMinute).val(endMinutes < 10 ? '0' + endMinutes : '' + endMinutes);
					}
				}
			}
		};

		ice.ace.jq(startHour).add(startMinute).on('change', function() {
			addDefaultDuration();
		});

		addDefaultDuration();
	}
};

ice.ace.Schedule.prototype.displayEventDetailsSidebar = function(markup) {
	var eventDetails = ice.ace.jq(this.jqId).find('.schedule-details-content');
	eventDetails.html(markup);
	this.expandEventDetails();
	eventDetails.find('input[type="text"]:eq(0),input[type="text"]:eq(1)').datepicker({dateFormat: 'yy-mm-dd'});
	eventDetails.find('button').button();
	this.addDefaultDurationFunctionality()
};

ice.ace.Schedule.prototype.displayEventDetailsTooltip = function(markup, node) {
	var eventDetails = ice.ace.jq(this.jqId).find('.schedule-details-tooltip-content');
	eventDetails.html(markup);
	eventDetails.dialog({resizable: false, draggable: false, dialogClass: 'schedule-details-tooltip', 
		position: { my: "left top", at: "right top", of: node }});
	ice.ace.jq(this.jqId).find('.schedule-details-tooltip').show();
};

ice.ace.Schedule.prototype.hideEventDetailsTooltip = function() {
	ice.ace.jq(this.jqId).find('.schedule-details-tooltip').hide();
};

ice.ace.Schedule.prototype.sendNavigationRequest = function(event, year, month, day, oldYear, oldMonth, oldDay, type) {
    var options = {};
	var behaviors = this.cfg.behaviors || {};

	document.getElementById(this.id + '_selectedDate').setAttribute('value', ''); // clear selected date

	if ((type == 'next' && !behaviors.next) || (type == 'previous' && !behaviors.previous)) {
		if (!this.cfg.isLazy) return;
		options = {
			source: this.id,
			render: this.id,
			execute: this.id
		};
	}

    var params = {};
	params[this.id + "_navigation"] = true;
	params[this.id + "_oldYear"] = oldYear;
	params[this.id + "_oldMonth"] = oldMonth;
	params[this.id + "_oldDay"] = oldDay;

	var endYear = year;
	var endMonth = month;
	var endDay = day;
	if (this.cfg.viewMode == 'month') {
		endDay = this.determineLastDayOfMonth(year, month);
	} else if (this.cfg.viewMode = 'week') {
		var lastDayOfWeek = this.determineLastDayOfWeek(year, month, day);
		endYear = lastDayOfWeek.year;
		endMonth = lastDayOfWeek.month;
		endDay = lastDayOfWeek.day;
	}
	month++; endMonth++; // make 1-relative for delivery
	params[this.id + "_startDate"] = year + "-" + (month < 10 ? '0' + month : month) + "-" + (day < 10 ? '0' + day : day);
	params[this.id + "_endDate"] = endYear + "-" + (endMonth < 10 ? '0' + endMonth : endMonth) 
		+ "-" + (endDay < 10 ? '0' + endDay : endDay);

    options.params = params;

	if (type == 'next' && behaviors && behaviors.next) {
		ice.ace.AjaxRequest(ice.ace.extendAjaxArgs(behaviors.next, options));
	} else if (type == 'previous' && behaviors && behaviors.previous) {
		ice.ace.AjaxRequest(ice.ace.extendAjaxArgs(behaviors.previous, options));
	} else {
		ice.ace.AjaxRequest(options);
	}
};

ice.ace.Schedule.prototype.sendEditRequest = function(event, type) {
    var options = {};
	var behaviors = this.cfg.behaviors || {};

	if ((type == 'add' && !behaviors.addEvent) ||
		(type == 'edit' && !behaviors.editEvent) ||
		(type == 'delete' && !behaviors.deleteEvent)) {
		options = {
			source: this.id,
			render: this.id,
			execute: this.id
		};
	}

    var params = {};
	if (type == 'add') params[this.id + "_add"] = true;
    else if (type == 'edit') params[this.id + "_edit"] = true;
    else if (type == 'delete') params[this.id + "_delete"] = true;
	this.addTimeParameters(params);
    options.params = params;

	// save scroll position
	window[this.id + '_lastScrollTop'] = this.jqRoot.find('.schedule-days').get(0).scrollTop;

	if (type == 'add' && behaviors.addEvent) {
		ice.ace.AjaxRequest(ice.ace.extendAjaxArgs(behaviors.addEvent, options));
	} else if (type == 'edit' && behaviors.editEvent) {
		ice.ace.AjaxRequest(ice.ace.extendAjaxArgs(behaviors.editEvent, options));
	} else if (type == 'delete' && behaviors.deleteEvent) {
		ice.ace.AjaxRequest(ice.ace.extendAjaxArgs(behaviors.deleteEvent, options));
	} else {
		ice.ace.AjaxRequest(options);
	}
};

ice.ace.Schedule.prototype.sendClickRequest = function(event, type, data) {
    var options = {};
	var behaviors = this.cfg.behaviors || {};

	if (!behaviors.eventClick && !behaviors.dayDblclick && !behaviors.timeDblclick)
		return;

    var params = {};
	if (type == 'event') params[this.id + "_eventClick"] = data;
    else if (type == 'day') params[this.id + "_dayDblclick"] = data;
    else if (type == 'time') params[this.id + "_timeDblclick"] = data;
	else return;

	if (type == 'event') {
		var eventData = this.events[data];
		params[this.id + "_date"] = eventData.startDate;
		params[this.id + "_time"] = eventData.startTime;
		params[this.id + "_endDate"] = eventData.endDate;
		params[this.id + "_endTime"] = eventData.endTime;
		params[this.id + "_title"] = eventData.title;
		params[this.id + "_location"] = eventData.location;
		params[this.id + "_notes"] = eventData.notes;
		if (eventData.styleClass) params[this.id + "_styleClass"] = eventData.styleClass;
		if (eventData.id) params[this.id + "_id"] = eventData.id;
	}

    options.params = params;

	if (type == 'event' && behaviors.eventClick) {
		ice.ace.AjaxRequest(ice.ace.extendAjaxArgs(behaviors.eventClick, options));
	} else if (type == 'day' && behaviors.dayDblclick) {
		ice.ace.AjaxRequest(ice.ace.extendAjaxArgs(behaviors.dayDblclick, options));
	} else if (type == 'time' && behaviors.timeDblclick) {
		ice.ace.AjaxRequest(ice.ace.extendAjaxArgs(behaviors.timeDblclick, options));
	}
};

ice.ace.Schedule.prototype.renderMonthView = function(data) {
	var currentYear = this.cfg.currentYear;
	var currentMonth = this.cfg.currentMonth;
	var i, j;
	var markup =
	"<div class=\"schedule-title ui-state-active\">"
		+"<div class=\"schedule-button-previous\"><i class=\"fa fa-arrow-left\"></i></div>"
		+"<div class=\"schedule-button-next\"><i class=\"fa fa-arrow-right\"></i></div>"
		+"<div class=\"schedule-showing\">" + this.getMonthName(currentMonth) + " " + currentYear + "</div>"
	+"</div>"

	+"<div class=\"schedule-content\">"

		+"<div class=\"schedule-grid ui-widget-content\">"
			+"<table><thead class=\"schedule-dow ui-state-default\"><tr>";

				for (i = 0; i < 7; i++) {
					markup += "<th class=\"schedule-dow-header schedule-dow-" + i + "\">"
						+ this.getDayOfTheWeekNameShort(i) + "</th>";
				}

			markup += "</tr></thead>"
			+"<tbody class=\"schedule-days\">";

				// first day of the month
				var date = new Date(currentYear, currentMonth, 1, 0, 0, 0, 0);
				// set to previous Sunday
				date.setDate(date.getDate() - date.getDay());

				for (i = 0; i < 42; i++) { // 42 days == 6 weeks

					if (i % 7 == 0) markup += "<tr>";
					var adjacentMonth = date.getMonth() != currentMonth;
					var dateClass = 'calendar-day-' + date.getFullYear()
						+ '-' + this.addLeadingZero(date.getMonth() + 1)
						+ '-' + this.addLeadingZero(date.getDate());
					markup += "<td class=\"day " + dateClass + (adjacentMonth? ' adjacent-month' : '')
						+ " ui-widget-content\">";
					if (this.isToday(date)) markup+= "<div class=\"schedule-state ui-state-highlight\">";
					else markup+= "<div class=\"schedule-state\">";

					markup += "<div class=\"day-number\">" + date.getDate() + "</div>";
					markup += "</div></td>";
					if (i % 7 == 6) markup += "</tr>";

					// set to next day
					date.setDate(date.getDate() + 1);
				}

			markup += "</tbody></table>"
		+"</div>"

		+"<div class=\"schedule-sidebar ui-widget-content\">"

			+"<div class=\"schedule-list-title ui-state-default\">Events this Month</div>"
			+"<div class=\"schedule-list-content\">";

			markup += "</div>"

			+"<div class=\"schedule-details-title ui-state-default\">Event Details</div>"
			+"<div class=\"schedule-details-content\"></div>"

		+"</div>"

	+"</div>";

	return markup;
};

ice.ace.Schedule.prototype.isToday = function(date) {
	var today = new Date();
	return (today.getFullYear() == date.getFullYear()
		&& today.getMonth() == date.getMonth()
		&& today.getDate() == date.getDate())
};

ice.ace.Schedule.prototype.renderMonthEvents = function(data) {
	var sidebarEventsContainer = ice.ace.jq(this.jqId).find('.schedule-list-content');
	var currentYear = this.cfg.currentYear;
	var currentMonth = this.cfg.currentMonth;
	var currentDay = this.cfg.currentDay;

	// add event divs at appropriate day divs
	var i;
	var listing = 0;
	for (i = 0; i < this.events.length; i++) {
		var event = this.events[i];
		var year = event.startDate.substring(0,4);
		var month = parseInt(event.startDate.substring(5,7)) - 1;
		var day = parseInt(event.startDate.substring(8,10));
		if (currentYear == year && currentMonth == month) {
			var dayDiv = this.jq.find('.calendar-day-' + year
				+ '-' + this.addLeadingZero(month + 1)
				+ '-' + this.addLeadingZero(day) + ' .schedule-state');
			var customStyleClass = event.styleClass ? ' ' + event.styleClass : '';
			var eventElement = ice.ace.jq('<div class=\"ui-state-default ui-corner-all schedule-event schedule-event-' + event.index + customStyleClass + '\"></div>');
			eventElement.html('<span>' + event.startTime + ' ' + event.title + '</span>');
			eventElement.appendTo(dayDiv);
			var highlightClass = listing % 2 == 1 ? ' ui-state-highlight' : '';
			ice.ace.jq('<div class="schedule-list-event schedule-event-' + event.index + highlightClass + '"><span class="schedule-list-event-day">'+event.startDate.substring(8,10)+'</span><span class="schedule-list-event-name">'+event.title+'</span><span class="schedule-list-event-location">'+event.location+'</span></div>').appendTo(sidebarEventsContainer);
			listing++;
		}
	}

	this.expandEventList();
};

ice.ace.Schedule.prototype.renderWeekView = function() {
	var i, j;
	var markup =
	"<div class=\"schedule-title ui-state-active\">"
		+"<div class=\"schedule-button-previous\"><i class=\"fa fa-arrow-left\"></i></div>"
		+"<div class=\"schedule-button-next\"><i class=\"fa fa-arrow-right\"></i></div>"
		+"<div class=\"schedule-showing\"></div>"
	+"</div>"

	+"<div class=\"schedule-content\">"

		/* time grid */
		+"<div class=\"schedule-grid ui-widget-content\">"
			+"<table><thead class=\"schedule-dow ui-state-default\"><tr>"
				+"<th class=\"schedule-dow-header schedule-cell-time\">Time</th>";

				for (i = 0; i < 7; i++) {
					markup += "<th class=\"schedule-dow-header schedule-dow-" + i + "\"></th>";
				}

			markup += "<th></th>"; // scrollbar width

			markup += "</tr></thead></table>"
			+"<div class=\"schedule-days\"><table><tbody>";

					for (i = 0; i < 24; i++) {
						var iString = i < 10 ? '0' + i : i;
						markup += "<tr>"
							+"<td class=\"ui-widget-content schedule-cell schedule-cell-time\">" + i + ":00</td>"
							+"<td class=\"ui-widget-content schedule-cell schedule-dow-0 schedule-time-" + iString + "00\"><div class=\"schedule-state\"></div></td>"
							+"<td class=\"ui-widget-content schedule-cell schedule-dow-1 schedule-time-" + iString + "00\"><div class=\"schedule-state\"></div></td>"
							+"<td class=\"ui-widget-content schedule-cell schedule-dow-2 schedule-time-" + iString + "00\"><div class=\"schedule-state\"></div></td>"
							+"<td class=\"ui-widget-content schedule-cell schedule-dow-3 schedule-time-" + iString + "00\"><div class=\"schedule-state\"></div></td>"
							+"<td class=\"ui-widget-content schedule-cell schedule-dow-4 schedule-time-" + iString + "00\"><div class=\"schedule-state\"></div></td>"
							+"<td class=\"ui-widget-content schedule-cell schedule-dow-5 schedule-time-" + iString + "00\"><div class=\"schedule-state\"></div></td>"
							+"<td class=\"ui-widget-content schedule-cell schedule-dow-6 schedule-time-" + iString + "00\"><div class=\"schedule-state\"></div></td>"
						+"</tr><tr>"
							+"<td class=\"ui-widget-content schedule-cell schedule-cell-time\">" + i + ":30</td>"
							+"<td class=\"ui-widget-content schedule-cell schedule-dow-0 schedule-time-" + iString + "30\"><div class=\"schedule-state\"></div></td>"
							+"<td class=\"ui-widget-content schedule-cell schedule-dow-1 schedule-time-" + iString + "30\"><div class=\"schedule-state\"></div></td>"
							+"<td class=\"ui-widget-content schedule-cell schedule-dow-2 schedule-time-" + iString + "30\"><div class=\"schedule-state\"></div></td>"
							+"<td class=\"ui-widget-content schedule-cell schedule-dow-3 schedule-time-" + iString + "30\"><div class=\"schedule-state\"></div></td>"
							+"<td class=\"ui-widget-content schedule-cell schedule-dow-4 schedule-time-" + iString + "30\"><div class=\"schedule-state\"></div></td>"
							+"<td class=\"ui-widget-content schedule-cell schedule-dow-5 schedule-time-" + iString + "30\"><div class=\"schedule-state\"></div></td>"
							+"<td class=\"ui-widget-content schedule-cell schedule-dow-6 schedule-time-" + iString + "30\"><div class=\"schedule-state\"></div></td>"
						+"</tr>";
					}

				markup += "</tbody></table>"
			+"<div class=\"schedule-event-container\"></div></div>"
		+"</div>";

		markup += "<div class=\"schedule-sidebar ui-widget-content\">"

		+"<div class=\"schedule-list-title ui-state-default\">Events this Week</div>"
		+"<div class=\"schedule-list-content\"></div>"

		+"<div class=\"schedule-details-title ui-state-default\">Event Details</div>"
		+"<div class=\"schedule-details-content\"></div>"

		+"</div>"

	+"</div>";

	return markup;
};

ice.ace.Schedule.prototype.renderWeekEvents = function() {
	var eventsContainer = ice.ace.jq(this.jqId).find('.schedule-event-container');
	eventsContainer.html('');
	var sidebarEventsContainer = ice.ace.jq(this.jqId).find('.schedule-list-content');
	var currentYear = this.cfg.currentYear;
	var currentMonth = this.cfg.currentMonth;
	var currentDay = this.cfg.currentDay;
	var weekStartDate = new Date(currentYear, currentMonth, currentDay, 0, 0, 0, 0);
	var lastDayOfWeek = this.determineLastDayOfWeek(currentYear, currentMonth, currentDay);
	var weekEndDate = new Date(lastDayOfWeek.year, lastDayOfWeek.month, lastDayOfWeek.day, 23, 59, 59, 999);
	var weekStartYear = weekStartDate.getFullYear();
	var weekEndYear = weekEndDate.getFullYear();
	weekStartYear = currentYear == weekEndYear ? '' : ', ' + weekStartYear;
	var title = this.getMonthName(weekStartDate.getMonth()) + ' ' + weekStartDate.getDate() + weekStartYear + ' - '
		+ this.getMonthName(weekEndDate.getMonth()) + ' ' + weekEndDate.getDate() + ', ' + weekEndYear;
	this.jq.find('.schedule-showing').html(title);
	// set the scrollable height, if supplied
	if  (this.cfg.scrollHeight) this.jq.find('.schedule-days').css({height:this.cfg.scrollHeight});
	// set day of the week headers and calendar day CSS classes
	var dowCount;
	var dowDate = new Date(weekStartDate.getTime());
	for (dowCount = 0; dowCount < 7; dowCount++) {
		var dayHeader = this.jq.find('.schedule-dow-header.schedule-dow-'+dowCount);
		dayHeader.html(this.getDayOfTheWeekNameShort(dowCount) + ', ' 
			+ this.getMonthNameShort(dowDate.getMonth()) + '/' + dowDate.getDate());
		var month = dowDate.getMonth() + 1;
		var day = dowDate.getDate();
		this.jq.find('.schedule-cell.schedule-dow-'+dowCount).addClass('calendar-day-'+dowDate.getFullYear()+'-'+(month < 10 ? '0' + month : month)+'-'+(day < 10 ? '0' + day : day));
		dowDate.setDate(dowDate.getDate() + 1);
	}
	var today = new Date();
	this.jqRoot.find('.calendar-day-' + today.getFullYear() + '-' + (today.getMonth() + 1) + '-'
		+ today.getDate() + ' .schedule-state').addClass('ui-state-highlight');
	// add event divs at appropriate positions
	var i,j;
	var timeSlots = [[], [], [], [], [], [], []];
	for (i = 0; i < 7; i++) {
		for (j = 0; j < 48; j++) timeSlots[i][j] = 0;
	}
	var listing = 0;
	for (i = 0; i < this.events.length; i++) {
		var event = this.events[i];
		var date = new Date();
		date.setFullYear(event.startDate.substring(0,4));
		date.setMonth(parseInt(event.startDate.substring(5,7) - 1));
		date.setDate(event.startDate.substring(8,10));
		if (date >= weekStartDate && date < weekEndDate) {
			// determine the day of the week
			var dateMillis = date.getTime();
			var startDateMillis = weekStartDate.getTime();
			var millisDelta = dateMillis - startDateMillis;
			var dow = Math.floor(millisDelta / 86400000);

			var hour = event.startTime.substring(0,2);
			var minutes = parseInt(event.startTime.substring(3,5));
			var startingTimeSlot = hour+(minutes >= 30 ? '30' : '00');
			var selector = '.schedule-dow-'+dow+'.schedule-time-'+startingTimeSlot;
			var timeCell = this.jq.find(selector);
			var position = timeCell.position();
			var width = timeCell.outerWidth() - 1;
			var endHour = event.endTime.substring(0,2);
			var endMinutes = parseInt(event.endTime.substring(3,5));
			var endingTimeSlot = this.determinePreviousTimeCell(endHour, endMinutes);
			var endSelector = '.schedule-dow-'+dow+'.schedule-time-'+endingTimeSlot;
			var endTimeCell = this.jq.find(endSelector);
			var endPosition = endTimeCell.position();
			var height = endTimeCell.outerHeight() - 1;
			var customStyleClass = event.styleClass ? ' ' + event.styleClass : '';
			var eventElement = ice.ace.jq('<div class=\"ui-state-default schedule-dow-' + dow + ' schedule-event schedule-event-' + event.index + customStyleClass + '\"></div>');
			eventElement.html(event.startTime + ' ' + event.title);
			var timeSlotMultiplicity = timeSlots[dow][this.timeSlotIndexMap[startingTimeSlot]];
			timeSlotMultiplicity = timeSlotMultiplicity < 4 ? timeSlotMultiplicity : 4;
			var multiplicityAdjustment = timeSlotMultiplicity * 5;
			var isChrome = ice.ace.browser.isChrome() && navigator.userAgent.indexOf('Edge\/') == -1;
			eventElement.css({position:'absolute',
				top:position.top+(isChrome?1:0),
				left:position.left+multiplicityAdjustment+(isChrome?1:0),
				width: (width - multiplicityAdjustment) + 'px', 
				height: (endPosition.top - position.top + height) + 'px'}).appendTo(eventsContainer);
			var highlightClass = listing % 2 == 1 ? ' ui-state-highlight' : '';
			ice.ace.jq('<div class="schedule-list-event schedule-event-' + event.index + highlightClass + '"><span class="schedule-list-event-day">'+this.getMonthNameShort(date.getMonth())+' '+event.startDate.substring(8)+'</span><span class="schedule-list-event-name">'+event.title+'</span><span class="schedule-list-event-location">'+event.location+'</span></div>').appendTo(sidebarEventsContainer);
			this.markUsedTimeSlots(timeSlots[dow], startingTimeSlot, endingTimeSlot);
			listing++;
		}
	}
	this.expandEventList();
	if  (this.cfg.scrollHeight) {
		if (window[this.id + '_lastScrollTop']) {
			this.jqRoot.find('.schedule-days').animate({scrollTop: window[this.id + '_lastScrollTop']}, 1);
		}
		delete window[this.id + '_lastScrollTop'];
	}
};

ice.ace.Schedule.prototype.renderDayView = function() {
	var i, j;
	var markup =
	"<div class=\"schedule-title ui-state-active\">"
		+"<div class=\"schedule-button-previous\"><i class=\"fa fa-arrow-left\"></i></div>"
		+"<div class=\"schedule-button-next\"><i class=\"fa fa-arrow-right\"></i></div>"
		+"<div class=\"schedule-showing\"></div>"
	+"</div>"

	+"<div class=\"schedule-content\">"

		/* time grid */
		+"<div class=\"schedule-grid ui-widget-content\">"
			+"<table><thead class=\"schedule-dow ui-state-default\"><tr>"
				+"<th class=\"schedule-dow-header schedule-cell-time\">Time</th>"
				+"<th class=\"schedule-dow-header schedule-dow-single\"></th>"
				+"<th></th>" // scrollbar width
			+"</tr></thead></table>"

			+"<div class=\"schedule-days\"><table><tbody>";

					for (i = 0; i < 24; i++) {
						var iString = i < 10 ? '0' + i : i;
						markup += "<tr>"
							+"<td class=\"ui-widget-content schedule-cell schedule-cell-time\">" + i + ":00</td>"
							+"<td class=\"ui-widget-content schedule-cell schedule-dow-single schedule-time-" + iString + "00\"><div class=\"schedule-state\"></div></td>"
						+"</tr><tr>"
							+"<td class=\"ui-widget-content schedule-cell schedule-cell-time\">" + i + ":30</td>"
							+"<td class=\"ui-widget-content schedule-cell schedule-dow-single schedule-time-" + iString + "30\"><div class=\"schedule-state\"></div></td>"
						+"</tr>";
					}

				markup += "</tbody></table>"
			+"<div class=\"schedule-event-container\"></div></div>"
		+"</div>";

		markup += "<div class=\"schedule-sidebar ui-widget-content\">"

			+"<div class=\"schedule-list-title ui-state-default\">Events this Day</div>"
			+"<div class=\"schedule-list-content\"></div>"

			+"<div class=\"schedule-details-title ui-state-default\">Event Details</div>"
			+"<div class=\"schedule-details-content\"></div>"

		+"</div>"

	+"</div>";

	return markup;
};

ice.ace.Schedule.prototype.renderDayEvents = function() {
	var eventsContainer = ice.ace.jq(this.jqId).find('.schedule-event-container');
	eventsContainer.html('');
	var sidebarEventsContainer = ice.ace.jq(this.jqId).find('.schedule-list-content');
	var currentYear = this.cfg.currentYear;
	var currentMonth = this.cfg.currentMonth;
	var currentDay = this.cfg.currentDay;
	var title = this.getMonthName(currentMonth) + ' ' + currentDay + ', ' + currentYear;
	this.jq.find('.schedule-showing').html(title);
	// set the scrollable height, if supplied
	if  (this.cfg.scrollHeight) this.jq.find('.schedule-days').css({height:this.cfg.scrollHeight});
	// set day of the week header
	var dayHeader = this.jq.find('.schedule-dow-header.schedule-dow-single');
	var date = new Date();
	date.setFullYear(currentYear);
	date.setMonth(currentMonth);
	date.setDate(currentDay);
	dayHeader.html(this.getDayOfTheWeekName(date.getDay()));
	// add calendar day CSS classes
	var displayMonth = currentMonth + 1;
	this.jq.find('.schedule-cell.schedule-dow-single').addClass('calendar-day-'+currentYear+'-'+(displayMonth < 10 ? '0' + displayMonth : displayMonth)+'-'+(currentDay < 10 ? '0' + currentDay : currentDay));
	// add event divs at appropriate positions
	var i;
	var timeSlots = [];
	for (i = 0; i < 48; i++) timeSlots[i] = 0;
	var listing = 0;
	for (i = 0; i < this.events.length; i++) {
		var event = this.events[i];
		var date = new Date();
		date.setFullYear(event.startDate.substring(0,4));
		date.setMonth(parseInt(event.startDate.substring(5,7) - 1));
		date.setDate(event.startDate.substring(8,10));
		if (date.getFullYear() == currentYear && date.getMonth() == currentMonth && date.getDate() == currentDay) {
			var hour = event.startTime.substring(0,2);
			var minutes = parseInt(event.startTime.substring(3,5));
			var startingTimeSlot = hour+(minutes >= 30 ? '30' : '00');
			var selector = '.schedule-dow-single.schedule-time-'+startingTimeSlot;
			var timeCell = this.jq.find(selector);
			var position = timeCell.position();
			var width = timeCell.outerWidth() - 1;
			var endHour = event.endTime.substring(0,2);
			var endMinutes = parseInt(event.endTime.substring(3,5));
			var endingTimeSlot = this.determinePreviousTimeCell(endHour, endMinutes);
			var endSelector = '.schedule-dow-single.schedule-time-'+endingTimeSlot;
			var endTimeCell = this.jq.find(endSelector);
			var endPosition = endTimeCell.position();
			var height = endTimeCell.outerHeight() - 1;
			var customStyleClass = event.styleClass ? ' ' + event.styleClass : '';
			var eventElement = ice.ace.jq('<div class=\"ui-state-default schedule-event schedule-event-' + event.index + customStyleClass + '\"></div>');
			eventElement.html(event.startTime + ' ' + event.title);
			var timeSlotMultiplicity = timeSlots[this.timeSlotIndexMap[startingTimeSlot]];
			timeSlotMultiplicity = timeSlotMultiplicity < 4 ? timeSlotMultiplicity : 4;
			var multiplicityAdjustment = timeSlotMultiplicity * 5;
			var isChrome = ice.ace.browser.isChrome() && navigator.userAgent.indexOf('Edge\/') == -1;
			eventElement.css({position:'absolute',
				top:position.top+(isChrome?1:0),
				left:position.left+multiplicityAdjustment+(isChrome?1:0),
				width: (width - multiplicityAdjustment) + 'px',
				height: (endPosition.top - position.top + height) + 'px'}).appendTo(eventsContainer);
			var highlightClass = listing % 2 == 1 ? ' ui-state-highlight' : '';
			ice.ace.jq('<div class="schedule-list-event schedule-event-' + event.index + highlightClass + '"><span class="schedule-list-event-day">'+event.startTime+'</span><span class="schedule-list-event-name">'+event.title+'</span><span class="schedule-list-event-location">'+event.location+'</span></div>').appendTo(sidebarEventsContainer);
			this.markUsedTimeSlots(timeSlots, startingTimeSlot, endingTimeSlot);
			listing++;
		}
	}
	this.expandEventList();
	if  (this.cfg.scrollHeight) {
		if (window[this.id + '_lastScrollTop']) {
			this.jqRoot.find('.schedule-days').animate({scrollTop: window[this.id + '_lastScrollTop']}, 1);
		}
		delete window[this.id + '_lastScrollTop'];
	}
};

ice.ace.Schedule.prototype.markUsedTimeSlots = function(timeSlots, startingTimeSlot, endingTimeSlot) {
	var startingIndex = this.timeSlotIndexMap[startingTimeSlot];
	var endingIndex = this.timeSlotIndexMap[endingTimeSlot];
	var i;
	if (endingIndex > startingIndex) // temporary measure to avoid errors with events that span multiple days
	for (i = startingIndex; i <= endingIndex; i++) timeSlots[i] = timeSlots[i] + 1;
};

ice.ace.Schedule.prototype.timeSlotIndexMap = {'0000':0, '0030':1, '0100':2, '0130':3, '0200':4, '0230':5,
	'0300':6, '0330':7, '0400':8, '0430':9, '0500':10, '0530':11, '0600':12, '0630':13, '7000':14, '0730':15,
	'0800':16, '0830':17, '0900':18, '0930':19, '1000':20, '1030':21, '1100':22, '1130':23, '1200':24, '1230':25,
	'1300':26, '1330':27, '1400':28, '1430':29, '1500':30, '1530':31, '1600':32, '1630':33, '1700':34, '1730':35,
	'1800':36, '1830':37, '1900':38, '1930':39, '2000':40, '2030':41, '2100':42, '2130':43, '2200':44, '2230':45,
	'2300':46, '2330':47
};

ice.ace.Schedule.prototype.addNavigationListeners = function() {
	var self = this;
	var previousButton = this.jq.find('.schedule-button-previous');
	var nextButton = this.jq.find('.schedule-button-next');
	var view = this.cfg.viewMode;
	var currentYear = this.cfg.currentYear;
	var currentMonth = this.cfg.currentMonth;
	var currentDay = this.cfg.currentDay;
	var is31DaysMonth = currentMonth == 0 || currentMonth == 2 || currentMonth == 4 || currentMonth == 6
		|| currentMonth == 7 || currentMonth == 9 || currentMonth == 11;
	var isLeapYear = ((currentYear % 4 == 0) && (currentYear % 100 != 0)) || (currentYear % 400 == 0);
	previousButton.on('click', function(e) {
		e.stopPropagation();
		if (view == 'week') {
			if (is31DaysMonth) {
				if (currentMonth == 0) {
					if (currentDay <= 7) {
						currentYear--;
						currentMonth = 11;
						currentDay = currentDay - 7 + 31;
					} else {
						currentDay = currentDay - 7;
					}
				} else if (currentMonth == 2) {
					if (isLeapYear) {
						if (currentDay <= 7) {
							currentMonth = 1;
							currentDay = currentDay - 7 + 29;
						} else {
							currentDay = currentDay - 7;
						}
					} else {
						if (currentDay <= 7) {
							currentMonth = 1;
							currentDay = currentDay - 7 + 28;
						} else {
							currentDay = currentDay - 7;
						}
					}
				} else if (currentMonth == 7) {
					if (currentDay <= 7) {
						currentMonth = 6;
						currentDay = currentDay - 7 + 31;
					} else {
						currentDay = currentDay - 7;
					}
				} else if (currentDay <= 7) {
					currentMonth--;
					currentDay = currentDay - 7 + 30;
				} else {
					currentDay = currentDay - 7;
				}
			} else {
				if (currentDay <= 7) {
					currentMonth--;
					currentDay = currentDay - 7 + 31;
				} else {
					currentDay = currentDay - 7;
				}
			}
		} else if (view == 'day') {
			if (is31DaysMonth) {
				if (currentMonth == 0) {
					if (currentDay == 1) {
						currentYear--;
						currentMonth = 11;
						currentDay = 31;
					} else {
						currentDay--;
					}
				} else if (currentMonth == 2) {
					if (isLeapYear) {
						if (currentDay == 1) {
							currentMonth = 1;
							currentDay = 29;
						} else {
							currentDay--;
						}
					} else {
						if (currentDay == 1) {
							currentMonth = 1;
							currentDay = 28;
						} else {
							currentDay--;
						}
					}
				} else if (currentMonth == 7) {
					if (currentDay == 1) {
						currentMonth = 6;
						currentDay = 31;
					} else {
						currentDay--;
					}
				} else if (currentDay == 1) {
					currentMonth--;
					currentDay = 30;
				} else {
					currentDay--;
				}
			} else {
				if (currentDay == 1) {
					currentMonth--;
					currentDay = 31;
				} else {
					currentDay--;
				}
			}
		} else {
			if (currentMonth == 0) {
				currentYear--;
				currentMonth = 11;
			} else currentMonth--;
			currentDay = 1;
		}
		var oldYear = self.cfg.currentYear;
		var oldMonth = self.cfg.currentMonth;
		var oldDay = self.cfg.currentDay;
		self.cfg.currentYear = currentYear;
		self.cfg.currentMonth = currentMonth;
		self.cfg.currentDay = currentDay;
		document.getElementById(self.id + '_currentYear').setAttribute('value', currentYear);
		document.getElementById(self.id + '_currentMonth').setAttribute('value', currentMonth);
		document.getElementById(self.id + '_currentDay').setAttribute('value', currentDay);
		if (!self.cfg.isLazy) {
			self.render();
		}
		self.sendNavigationRequest(e, currentYear, currentMonth, currentDay, oldYear, oldMonth, oldDay, 'previous');
	});
	nextButton.on('click', function(e) {
		e.stopPropagation();
		if (view == 'week') {
			if (is31DaysMonth) {
				if (currentMonth == 11) {
					if (currentDay >= 25) {
						currentYear++;
						currentMonth = 0;
						currentDay = currentDay + 7 - 31;
					} else {
						currentDay = currentDay + 7;
					}
				} else if (currentDay >= 24) {
					currentMonth++;
					currentDay = currentDay + 7 - 31;
				} else {
					currentDay = currentDay + 7;
				}
			} else {
				if (currentMonth == 1) {
					if (isLeapYear) {
						if (currentDay >= 23) {
							currentMonth = 2;
							currentDay = currentDay + 7 - 29;
						} else {
							currentDay = currentDay + 7;
						}
					} else {
						if (currentDay >= 22) {
							currentMonth = 2;
							currentDay = currentDay + 7 - 28;
						} else {
							currentDay = currentDay + 7;
						}
					}
				} else if (currentDay >= 24) {
					currentMonth++;
					currentDay = currentDay + 7 - 30;
				} else {
					currentDay = currentDay + 7;
				}
			}
		} else if (view == 'day') {
			if (is31DaysMonth) {
				if (currentMonth == 11) {
					if (currentDay == 31) {
						currentYear++;
						currentMonth = 0;
						currentDay = 1;
					} else {
						currentDay++;
					}
				} else if (currentDay == 31) {
					currentMonth++;
					currentDay = 1;
				} else {
					currentDay++;
				}
			} else {
				if (currentMonth == 1) {
					if (isLeapYear) {
						if (currentDay == 29) {
							currentMonth = 2;
							currentDay = 1;
						} else {
							currentDay++;
						}
					} else {
						if (currentDay == 28) {
							currentMonth = 2;
							currentDay = 1;
						} else {
							currentDay++;
						}
					}
				} else if (currentDay == 30) {
					currentMonth++;
					currentDay = 1;
				} else {
					currentDay++;
				}
			}
		} else {
			if (currentMonth == 11) {
				currentYear++;
				currentMonth = 0;
			} else currentMonth++;
			currentDay = 1;
		}
		var oldYear = self.cfg.currentYear;
		var oldMonth = self.cfg.currentMonth;
		var oldDay = self.cfg.currentDay;
		self.cfg.currentYear = currentYear;
		self.cfg.currentMonth = currentMonth;
		self.cfg.currentDay = currentDay;
		document.getElementById(self.id + '_currentYear').setAttribute('value', currentYear);
		document.getElementById(self.id + '_currentMonth').setAttribute('value', currentMonth);
		document.getElementById(self.id + '_currentDay').setAttribute('value', currentDay);
		if (!self.cfg.isLazy) {
			self.render();
		}
		self.sendNavigationRequest(e, currentYear, currentMonth, currentDay, oldYear, oldMonth, oldDay, 'next');
	});
};

ice.ace.Schedule.windowResizeListeners = {};

ice.ace.Schedule.prototype.addResizeListeners = function() {
	var self = this;
	// sidebar
	if (this.jq.hasClass('schedule-config-sidebar-right')
			|| this.jq.hasClass('schedule-config-sidebar-left')) {
		var handles = this.jq.hasClass('schedule-config-sidebar-right') ? 'w' : 'e';
		self.sidebarOriginalSize = 0;
		self.gridOriginalWidth = 0;
		this.jq.find('.schedule-sidebar').resizable({handles: handles, containment: 'parent', 
			create: function (event, ui) {
				ice.ace.jq(this).parent().on('resize', function (e) {
					e.stopPropagation();
				});
			},
			start: function(event, ui) {
				event.stopPropagation();
				self.sidebarOriginalSize = ui.originalSize.width;
				self.gridOriginalWidth = self.jq.find('.schedule-grid').width();
				if (self.jq.hasClass('schedule-config-sidebar-left'))
					self.jq.find('.schedule-grid').css({position: 'absolute', top: 0, right: 0});
			},
			resize: function(event, ui) {
				event.stopPropagation();
				var grid = self.jq.find('.schedule-grid');
				grid.width(self.gridOriginalWidth + (self.sidebarOriginalSize - ui.size.width));

				var isChrome = ice.ace.browser.isChrome() && navigator.userAgent.indexOf('Edge\/') == -1;
				if (self.cfg.viewMode == 'day') {
					var timeCell = self.jq.find('.schedule-dow-single.schedule-time-0000');
					var timeCellWidth = timeCell.outerWidth() - 1;
					self.jq.find('.schedule-event').css({width: timeCellWidth});
					var timeCellLeft = timeCell.position().left;
					self.jq.find('.schedule-event').css({left: timeCellLeft + (isChrome?1:0)});
				} else if (self.cfg.viewMode == 'week') {
					var i;
					for (i = 0; i < 7; i++) {
						var timeCell = self.jq.find('.schedule-dow-' + i + '.schedule-time-0000');
						var timeCellWidth = timeCell.outerWidth() - 1;
						self.jq.find('.schedule-event.schedule-dow-' + i).css({width: timeCellWidth});
						var timeCellLeft = timeCell.position().left;
						self.jq.find('.schedule-event.schedule-dow-' + i).css({left: timeCellLeft + (isChrome?1:0)});
					}
				}
			}
		});
	}

	// window
	if (ice.ace.Schedule.windowResizeListeners[this.id])
		ice.ace.jq(window).off('resize', ice.ace.Schedule.windowResizeListeners[this.id]);

	ice.ace.Schedule.windowResizeListeners[this.id] = function(e) {
		var root = ice.ace.jq(self.jqId);
		var grid = root.find('.schedule-grid');
		var sidebar = root.find('.schedule-sidebar');
		grid.attr('style', '');
		sidebar.attr('style', '');

		var isChrome = ice.ace.browser.isChrome() && navigator.userAgent.indexOf('Edge\/') == -1;
		if (self.cfg.viewMode == 'day') {
			var timeCell = root.find('.schedule-dow-single.schedule-time-0000');
			var timeCellWidth = timeCell.outerWidth() - 1;
			root.find('.schedule-event').css({width: timeCellWidth});
			var timeCellLeft = timeCell.position().left;
			root.find('.schedule-event').css({left: timeCellLeft + (isChrome?1:0)});
		} else if (self.cfg.viewMode == 'week') {
			var i;
			for (i = 0; i < 7; i++) {
				var timeCell = root.find('.schedule-dow-' + i + '.schedule-time-0000');
				var timeCellWidth = timeCell.outerWidth() - 1;
				root.find('.schedule-event.schedule-dow-' + i).css({width: timeCellWidth});
				var timeCellLeft = timeCell.position().left;
				root.find('.schedule-event.schedule-dow-' + i).css({left: timeCellLeft + (isChrome?1:0)});
			}
		}
	};

	ice.ace.jq(window).on('resize', ice.ace.Schedule.windowResizeListeners[this.id]);
};

ice.ace.Schedule.prototype.determineLastDayOfWeek = function(currentYear, currentMonth, currentDay) {
	var endYear = currentYear;
	var endMonth = currentMonth;
	var endDay = currentDay;
	var is31DaysMonth = currentMonth == 0 || currentMonth == 2 || currentMonth == 4 || currentMonth == 6
		|| currentMonth == 7 || currentMonth == 9 || currentMonth == 11;
	var isLeapYear = ((currentYear % 4 == 0) && (currentYear % 100 != 0)) || (currentYear % 400 == 0);
	if (is31DaysMonth) {
		if (currentMonth == 11) {
			if (currentDay >= 26) {
				endYear = currentYear + 1;
				endMonth = 0;
				endDay = currentDay + 6 - 31;
			} else {
				endDay = currentDay + 6;
			}
		} else if (currentDay >= 26) {
			endMonth = currentMonth + 1;
			endDay = currentDay + 6 - 31;
		} else {
			endDay = currentDay + 6;
		}
	} else {
		if (currentMonth == 1) {
			if (isLeapYear) {
				if (currentDay >= 24) {
					endMonth = 2;
					endDay = currentDay + 6 - 29;
				} else {
					endDay = currentDay + 6;
				}
			} else {
				if (currentDay >= 23) {
					endMonth = 2;
					endDay = currentDay + 6 - 28;
				} else {
					endDay = currentDay + 6;
				}
			}
		} else if (currentDay >= 25) {
			endMonth = currentMonth + 1;
			endDay = currentDay + 6 - 30;
		} else {
			endDay = currentDay + 6;
		}
	}
	return {year: endYear, month: endMonth, day: endDay};
};

ice.ace.Schedule.prototype.determineLastDayOfMonth = function(currentYear, currentMonth) {
	var is31DaysMonth = currentMonth == 0 || currentMonth == 2 || currentMonth == 4 || currentMonth == 6
		|| currentMonth == 7 || currentMonth == 9 || currentMonth == 11;
	var isLeapYear = ((currentYear % 4 == 0) && (currentYear % 100 != 0)) || (currentYear % 400 == 0);
	if (currentMonth == 1) {
		if (isLeapYear) return 29;
		else return 28;
	} else if (is31DaysMonth) {
		return 31;
	} else {
		return 30;
	}
};

ice.ace.Schedule.prototype.determinePreviousTimeCell = function(hour, minutes) {
	var previousHour, previousMinutes;
	if (parseInt(minutes) >= 30) {
		previousMinutes = '00';
		previousHour = hour;
	} else {
		if (hour == '00') {
			previousMinutes = '00';
			previousHour = '00';
		} else {
			previousMinutes = '30';
			previousHour = parseInt(hour) - 1;
			previousHour = previousHour < 10 ? '0' + previousHour : previousHour;
		}
	}
	return previousHour + previousMinutes;
};

ice.ace.Schedule.prototype.expandEventList = function() {
	var contentHeight = this.getSidebarContentHeight();
	var listContent = ice.ace.jq(this.jqId).find('.schedule-list-content');
	var detailsContent = ice.ace.jq(this.jqId).find('.schedule-details-content');
	detailsContent.css('height', '0');
	listContent.css('height', contentHeight + 'px');
};

ice.ace.Schedule.prototype.expandEventDetails = function() {
	var listContent = ice.ace.jq(this.jqId).find('.schedule-list-content');
	var detailsContent = ice.ace.jq(this.jqId).find('.schedule-details-content');
	listContent.css('height', '0');
	detailsContent.css('height', 'auto');
};

ice.ace.Schedule.prototype.getSidebarContentHeight = function() {
	var sidebar = ice.ace.jq(this.jqId).find('.schedule-sidebar');
	var sidebarHeight = sidebar.outerHeight();
	var listTitle = ice.ace.jq(this.jqId).find('.schedule-list-title');
	var listTitleHeight = listTitle.outerHeight();
	var detailsTitle = ice.ace.jq(this.jqId).find('.schedule-details-title');
	var detailsTitleHeight = detailsTitle.outerHeight();
	return sidebarHeight - listTitleHeight - detailsTitleHeight;
};

ice.ace.Schedule.prototype.getMonthName = function(monthNumber) {
	var months = ['January', 'February', 'March', 'April', 'May', 'June',
		'July', 'August', 'September', 'October', 'November', 'December'];
	return months[monthNumber];
};
ice.ace.Schedule.prototype.getMonthNameShort = function(monthNumber) {
	var months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun',
		'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];
	return months[monthNumber];
};
ice.ace.Schedule.prototype.getDayOfTheWeekName = function(dayNumber) {
	var days = ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 
		'Thursday', 'Friday', 'Saturday'];
	return days[dayNumber];
};
ice.ace.Schedule.prototype.getDayOfTheWeekNameShort = function(dayNumber) {
	var days = ['Sun', 'Mon', 'Tue', 'Wed', 
		'Thu', 'Fri', 'Sat'];
	return days[dayNumber];
};

ice.ace.Schedule.prototype.addLeadingZero = function(number) {
	if (number < 10) return ('0' + number);
	else return ('' + number);
};