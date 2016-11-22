/*
 *
 */

if (!window['ice']) window.ice = {};
if (!window.ice['ace']) window.ice.ace = {};
ice.ace.schedule = {};
ice.ace.schedule.registry = {};

ice.ace.Schedule = function(id, cfg) {
	this.id = id;
	this.jqId = ice.ace.escapeClientId(id);
	this.jqRoot = ice.ace.jq(this.jqId);
	this.jq = this.jqRoot.find('.schedule-main');
	this.cfg = cfg;
	this.events = cfg.events;
	var self = this;
	
	var configuration = {};
	if (this.cfg.viewMode == 'week') {
		configuration.render = function(data) { return self.renderWeekView.call(self, data); };
		if (this.cfg.isLazy) configuration.doneRendering = function() { self.renderLazyWeekEvents.call(self, self.data); };
		else configuration.doneRendering = function() { self.renderWeekEvents.call(self, self.data); };
	} else if (this.cfg.viewMode == 'day') {
		configuration.render = function(data) { return self.renderDayView.call(self, data); };
		if (this.cfg.isLazy) configuration.doneRendering = function() { self.renderLazyDayEvents.call(self, self.data); };
		else configuration.doneRendering = function() { self.renderDayEvents.call(self, self.data); };
	} else {
		configuration.render = function(data) { return self.renderMonthView.call(self, data); };
		configuration.doneRendering = function() { self.renderMonthEvents.call(self, self.data); };
	}
	configuration.events = this.events;
	configuration.forceSixRows = true;
	if (this.cfg.isLazy) {
		configuration.startWithMonth = cfg.lazyYear + '-' + (cfg.lazyMonth + 1) + '-01'; // CLNDR month is 1-relative
	}
	configuration.dateParameter = 'startDate';
	this.clndr = this.jq.clndr(configuration);

	var behaviors = self.cfg.behaviors;
	if (behaviors && behaviors.eventClick) {
		this.jqRoot.delegate('.schedule-event', 'click', function(event) {
			event.stopPropagation();
			var node = event['target'];
			var eventIndex = self.extractEventIndex(node);
			self.sendClickRequest(event, 'event', eventIndex);
		});
	}
	if (cfg.displayEventDetails != 'disabled') {
		if (cfg.displayEventDetails == 'tooltip') {
			this.jqRoot.delegate('.schedule-event', 'mouseover', function(event) {
				var node = event['target'];
				var eventIndex = self.extractEventIndex(node);
				var eventData = self.events[eventIndex];
				var markup = self.getEventDetailsMarkup(eventData);
				self.displayEventDetailsTooltip(markup, node);
			});
			this.jqRoot.delegate('.schedule-event', 'mouseout', function(event) {
				self.hideEventDetailsTooltip();
			});
		} else {
			this.jqRoot.delegate('.schedule-event', 'click', function(event) {
				event.stopImmediatePropagation();
				var node = event['target'];
				var eventIndex = self.extractEventIndex(node);
				var eventData = self.events[eventIndex];
				var markup = self.getEventDetailsMarkup(eventData, false,
					self.cfg.isEventEditing, self.cfg.isEventDeletion);
				if (self.cfg.displayEventDetails == 'popup')
					self.displayEventDetailsPopup(markup);
				else
					self.displayEventDetailsSidebar(markup);
			});
		}
	}
	if (behaviors && behaviors.dayClick) {
		this.jqRoot.delegate('.day', 'click', function(event) {
			var node = event['target'];
			node = node.className.indexOf('day-number') > -1 ? node.parentNode : node;
			var date = self.extractEventDate(node);
			self.sendClickRequest(event, 'day', date);
		});
	}
	if (behaviors && behaviors.timeClick) {
		this.jqRoot.delegate('.schedule-cell', 'click', function(event) {
			var node = event['target'];
			node = node.className.indexOf('day-number') > -1 ? node.parentNode : node;
			var date = self.extractEventDate(node);
			var time = self.extractEventTime(node);
			self.sendClickRequest(event, 'time', date + ' ' + time);
		});
	}
	if (self.cfg.isEventAddition) {
		this.jqRoot.delegate('.day, .schedule-cell', 'click', function(event) {
			var date, time;
			var node = event['target'];
			node = node.className.indexOf('day-number') > -1 ? node.parentNode : node;
			date = self.extractEventDate(node);
			time = self.extractEventTime(node);
			var eventData = {startDate: date, startTime: time, endDate: date, endTime: '', title: '', location: '', notes: '', index: ''};
			var markup = self.getEventDetailsMarkup(eventData, true, false, false);
			self.displayEventDetailsPopup(markup);
		});
	}
	this.jqRoot.delegate('.schedule-list-title', 'click', function(event) {
		self.expandEventList();
	});
	this.jqRoot.delegate('.schedule-details-title', 'click', function(event) {
		self.expandEventDetails();
	});
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
	if (node.className == 'ui-state-highlight') node = node.parentNode; // today's day
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
	if (node.className == 'ui-state-highlight') node = node.parentNode; // today's day
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

ice.ace.Schedule.prototype.getEventDetailsMarkup = function(data, isEventAddition, isEventEditing, isEventDeletion) {
	if (data) {// *** escape HTML characters
		var markup;
		if (isEventAddition || isEventEditing) {
			markup = '<table><tr><td>Start Date:</td><td><input type="date" name="'+this.id+'_date" value="'+data.startDate+'"/></td></tr><tr><td>Start Time:</td><td><input type="time" name="'+this.id+'_time" value="'+data.startTime+'"/></td></tr><tr><td>End Date:</td><td><input type="date" name="'+this.id+'_endDate" value="'+data.endDate+'"/></td></tr><tr><td>End Time:</td><td><input type="time" name="'+this.id+'_endTime" value="'+data.endTime+'"/></td></tr><tr><tr><td>Title:</td><td><input type="text" name="'+this.id+'_title" value="'+data.title+'"/></td></tr><tr><td>Location:</td><td><input type="text" name="'+this.id+'_location" value="'+data.location+'"/></td></tr><tr><td>Notes:</td><td><textarea name="'+this.id+'_notes">'+data.notes+'</textarea></td></tr></table><input type="hidden" name="'+this.id+'_index" value="'+data.index+'"/>';
		} else {
			markup = '<table><tr><td>Start Date:</td><td>'+data.startDate+'</td></tr><tr><td>Start Time:</td><td>'+data.startTime+'</td></tr><tr><td>End Date:</td><td>'+data.endDate+'</td></tr><tr><td>End Time:</td><td>'+data.endTime+'</td></tr><tr><td>Title:</td><td>'+data.title+'</td></tr><tr><td>Location:</td><td>'+data.location+'</td></tr><tr><td>Notes:</td><td>'+data.notes+'</td></tr></table>';
		}
		if (isEventAddition) markup += '<button onclick="ice.ace.instance(\''+this.id+'\').sendEditRequest(event, \'add\');return false;">Add</button>';
		else {
			if (isEventEditing) markup += '<button onclick="ice.ace.instance(\''+this.id+'\').sendEditRequest(event, \'edit\');return false;">Save</button> ';
			if (isEventDeletion) markup += '<span><button onclick="ice.ace.instance(\''+this.id+'\').confirmDeletion(this);return false;">Delete</button><span style="display:none;">Are you sure? <button onclick="ice.ace.instance(\''+this.id+'\').sendEditRequest(event, \'delete\');return false;">Yes</button> <button onclick="ice.ace.instance(\''+this.id+'\').cancelDeletion(this);return false;">No</button></span></span>';
		}
		return markup;
	} else {
		return '<div>No Data</div>';
	}
}

ice.ace.Schedule.prototype.confirmDeletion = function(button) {
	ice.ace.jq(button).hide().siblings().show();
};

ice.ace.Schedule.prototype.cancelDeletion = function(button) {
	ice.ace.jq(button.parentNode).hide().siblings().show();
};

ice.ace.Schedule.prototype.displayEventDetailsPopup = function(markup) {
	var eventDetails = ice.ace.jq(this.jqId).find('.schedule-details-popup-content');
	eventDetails.html(markup);
	eventDetails.dialog({dialogClass: 'schedule-details-popup'});
};

ice.ace.Schedule.prototype.displayEventDetailsSidebar = function(markup) {
	var eventDetails = ice.ace.jq(this.jqId).find('.schedule-details-content');
	eventDetails.html(markup);
	this.expandEventDetails();
};

ice.ace.Schedule.prototype.displayEventDetailsTooltip = function(markup, node) {
	var eventDetails = ice.ace.jq(this.jqId).find('.schedule-details-tooltip-content');
	eventDetails.html(markup);
	eventDetails.dialog({resizable: false, draggable: false, dialogClass: 'schedule-details-tooltip', 
		position: { my: "left top", at: "right bottom", of: node }});
	ice.ace.jq(this.jqId).find('.schedule-details-tooltip').show();
};

ice.ace.Schedule.prototype.hideEventDetailsTooltip = function() {
	ice.ace.jq(this.jqId).find('.schedule-details-tooltip').hide();
};

ice.ace.Schedule.prototype.sendNavigationRequest = function(event, year, month, day, type) {
    var options = {};
	var behaviors = this.cfg.behaviors || {};

	if ((type == 'next' && !behaviors.next) || (type == 'previous' && !behaviors.previous)) {
		if (!this.cfg.isLazy) return;
		options = {
			source: this.id,
			render: this.id,
			execute: this.id
		};
	}

    var params = {};
	if (this.cfg.isLazy) {
		params[this.id + "_lazyYear"] = year;
		params[this.id + "_lazyMonth"] = month;
		params[this.id + "_lazyDay"] = day;
	} else {
		params[this.id + "_currentYear"] = year;
		params[this.id + "_currentMonth"] = month;
		params[this.id + "_currentDay"] = day;
	}
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
    options.params = params;

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

	if (!behaviors.eventClick && !behaviors.dayClick && !behaviors.timeClick)
		return;

    var params = {};
	if (type == 'event') params[this.id + "_eventClick"] = data;
    else if (type == 'day') params[this.id + "_dayClick"] = data;
    else if (type == 'time') params[this.id + "_timeClick"] = data;
	else return;
    options.params = params;

	if (type == 'event' && behaviors.eventClick) {
		ice.ace.AjaxRequest(ice.ace.extendAjaxArgs(behaviors.eventClick, options));
	} else if (type == 'day' && behaviors.dayClick) {
		ice.ace.AjaxRequest(ice.ace.extendAjaxArgs(behaviors.dayClick, options));
	} else if (type == 'time' && behaviors.timeClick) {
		ice.ace.AjaxRequest(ice.ace.extendAjaxArgs(behaviors.timeClick, options));
	}
};

ice.ace.Schedule.prototype.renderMonthView = function(data) {
	this.data = data;
	var i, j;
	var markup =
	"<div class=\"schedule-title ui-state-active\">"
		+"<div class=\"schedule-button-previous\"><i class=\"fa fa-arrow-left\"></i></div>"
		+"<div class=\"schedule-button-next\"><i class=\"fa fa-arrow-right\"></i></div>"
		+"<div class=\"schedule-showing\">" + data.month + " " + data. year + "</div>"
	+"</div>"

	+"<div class=\"schedule-content\">"

		+"<div class=\"schedule-grid ui-widget-content\">"
			+"<table><thead class=\"schedule-dow ui-state-default\"><tr>";

				for (i = 0; i < data.daysOfTheWeek.length; i++) {
					var day = data.daysOfTheWeek[i];
					markup += "<th class=\"schedule-dow-header schedule-dow-" + i + "\">" + day + "</th>";
				}

			markup += "</tr></thead>"
			+"<tbody class=\"schedule-days\">";

				for (i = 0; i < data.days.length; i++) {
					var day = data.days[i];
					if (i % 7 == 0) markup += "<tr>";
					markup += "<td class=\"" + day.classes + " ui-widget-content\" id=\"" + day.id + "\">";
					if (day.classes.indexOf('today') > -1) markup+= "<div class=\"ui-state-highlight\">";
					markup += "<div class=\"day-number\">" + day.day + "</div>";
					for (j = 0; j < day.events.length; j++) {
						var event = day.events[j];
						if (day.classes.indexOf('adjacent-month') == -1) {
							var customStyleClass = event.styleClass ? ' ' + event.styleClass : '';
							markup += "<div class=\"ui-state-hover ui-corner-all schedule-event schedule-event-" + event.index + customStyleClass +"\"><span>"+ event.startTime + " " + event.title + "</span></div>";
						}
					}
					if (day.classes.indexOf('today') > -1) markup+= "</div>";
					markup += "</td>";
					if (i % 7 == 6) markup += "</tr>";
				}

			markup += "</tbody></table>"
		+"</div>"

		+"<div class=\"schedule-sidebar ui-widget-content\">"

			+"<div class=\"schedule-list-title ui-state-default\">Events this Month</div>"
			+"<div class=\"schedule-list-content\">";

				for (i = 0; i < data.eventsThisMonth.length; i++) {
					var event = data.eventsThisMonth[i];
					markup += "<div class=\"schedule-list-event\">"
						+"<div class=\"schedule-list-event-day\">" + event.startDate.substring(8) + "</div>"
						+"<div class=\"schedule-list-event-name\">" + event.title + "</div>"
						+"<div class=\"schedule-list-event-location\">" + event.location + "</div>"
					+"</div>";
				}

			markup += "</div>"

			+"<div class=\"schedule-details-title ui-state-default\">Event Details</div>"
			+"<div class=\"schedule-details-content\"></div>"

		+"</div>"

	+"</div>";

	return markup;
};

ice.ace.Schedule.prototype.renderMonthEvents = function(data) {
	this.addListeners();
	this.expandEventList();
};

ice.ace.Schedule.prototype.renderWeekView = function(data) {
	this.data = data;
	var i, j;
	var markup =
	"<div class=\"schedule-title ui-state-active\">"
		+"<div class=\"schedule-button-previous\"><i class=\"fa fa-arrow-left\"></i></div>"
		+"<div class=\"schedule-button-next\"><i class=\"fa fa-arrow-right\"></i></div>"
		+"<div class=\"schedule-showing\">" + data.month + " " + data.year + "</div>"
	+"</div>"

	+"<div class=\"schedule-content\">"

		/* time grid */
		+"<div class=\"schedule-grid ui-widget-content\">"
			+"<table><thead class=\"schedule-dow ui-state-default\"><tr>"
				+"<th class=\"schedule-dow-header schedule-cell-time\">Time</th>";

				for (i = 0; i < data.daysOfTheWeek.length; i++) {
					var day = data.daysOfTheWeek[i];
					markup += "<th class=\"schedule-dow-header schedule-dow-" + i + "\">" + day + "</th>";
				}

			markup += "<th></th>"; // scrollbar width

			markup += "</tr></thead></table>"
			+"<div class=\"schedule-days\"><table><tbody>";

					for (i = 0; i < 24; i++) {
						var iString = i < 10 ? '0' + i : i;
						markup += "<tr>"
							+"<td class=\"ui-widget-content schedule-cell schedule-cell-time\">" + i + ":00</td>"
							+"<td class=\"ui-widget-content schedule-cell schedule-dow-0 schedule-time-" + iString + "00\"></td>"
							+"<td class=\"ui-widget-content schedule-cell schedule-dow-1 schedule-time-" + iString + "00\"></td>"
							+"<td class=\"ui-widget-content schedule-cell schedule-dow-2 schedule-time-" + iString + "00\"></td>"
							+"<td class=\"ui-widget-content schedule-cell schedule-dow-3 schedule-time-" + iString + "00\"></td>"
							+"<td class=\"ui-widget-content schedule-cell schedule-dow-4 schedule-time-" + iString + "00\"></td>"
							+"<td class=\"ui-widget-content schedule-cell schedule-dow-5 schedule-time-" + iString + "00\"></td>"
							+"<td class=\"ui-widget-content schedule-cell schedule-dow-6 schedule-time-" + iString + "00\"></td>"
						+"</tr><tr>"
							+"<td class=\"ui-widget-content schedule-cell schedule-cell-time\">" + i + ":30</td>"
							+"<td class=\"ui-widget-content schedule-cell schedule-dow-0 schedule-time-" + iString + "30\"></td>"
							+"<td class=\"ui-widget-content schedule-cell schedule-dow-1 schedule-time-" + iString + "30\"></td>"
							+"<td class=\"ui-widget-content schedule-cell schedule-dow-2 schedule-time-" + iString + "30\"></td>"
							+"<td class=\"ui-widget-content schedule-cell schedule-dow-3 schedule-time-" + iString + "30\"></td>"
							+"<td class=\"ui-widget-content schedule-cell schedule-dow-4 schedule-time-" + iString + "30\"></td>"
							+"<td class=\"ui-widget-content schedule-cell schedule-dow-5 schedule-time-" + iString + "30\"></td>"
							+"<td class=\"ui-widget-content schedule-cell schedule-dow-6 schedule-time-" + iString + "30\"></td>"
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

ice.ace.Schedule.prototype.renderWeekEvents = function(data) {
	this.addListeners();
	var eventsContainer = ice.ace.jq(this.jqId).find('.schedule-event-container');
	eventsContainer.html('');
	var sidebarEventsContainer = ice.ace.jq(this.jqId).find('.schedule-list-content');
	var currentYear = this.cfg.currentYear;
	var currentMonth = this.cfg.currentMonth;
	var currentDay = this.cfg.currentDay;
	var is31DaysMonth = currentMonth == 0 || currentMonth == 2 || currentMonth == 4 || currentMonth == 6
		|| currentMonth == 7 || currentMonth == 9 || currentMonth == 11;
	var isLeapYear = ((currentYear % 4 == 0) && (currentYear % 100 != 0)) || (currentYear % 400 == 0);
	var weekStartDate = new Date(currentYear, currentMonth, currentDay, 0, 0, 0, 0);
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
	var weekEndDate = new Date(currentYear, currentMonth, currentDay, 0, 0, 0, 0);
	var title = this.getMonthName(weekStartDate.getMonth()) + ' ' + weekStartDate.getDate() + ' - '
		+ this.getMonthName(weekEndDate.getMonth()) + ' ' + weekEndDate.getDate();
	this.jq.find('.schedule-showing').html(title);
	// set the scrollable height, if supplied
	if  (this.cfg.scrollHeight) this.jq.find('.schedule-days').css({height:this.cfg.scrollHeight});
	// set day of the week headers and calendar day CSS classes
	var dowCount;
	for (dowCount = 0; dowCount < 7; dowCount++) {
		var dowDate = new Date(weekStartDate.getTime() + (86400000 * dowCount));
		var dayHeader = this.jq.find('.schedule-dow-header.schedule-dow-'+dowCount);
		dayHeader.html(this.getDayOfTheWeekNameShort(dowCount) + ', ' 
			+ this.getMonthNameShort(dowDate.getMonth()) + '/' + dowDate.getDate());
		var month = dowDate.getMonth() + 1;
		var day = dowDate.getDate();
		this.jq.find('.schedule-cell.schedule-dow-'+dowCount).addClass('calendar-day-'+dowDate.getFullYear()+'-'+(month < 10 ? '0' + month : month)+'-'+(day < 10 ? '0' + day : day));
	}
	// add event divs at appropriate positions
	var i;
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
			var selector = '.schedule-dow-'+dow+'.schedule-time-'+hour+(minutes >= 30 ? '30' : '00');
			var timeCell = this.jq.find(selector);
			var position = timeCell.position();
			var width = timeCell.width();
			var endHour = event.endTime.substring(0,2);
			var endMinutes = parseInt(event.endTime.substring(3,5));
			var endSelector = '.schedule-dow-'+dow+'.schedule-time-'+this.determinePreviousTimeCell(endHour, endMinutes);
			var endTimeCell = this.jq.find(endSelector);
			var endPosition = endTimeCell.position();
			var height = endTimeCell.height();
			var customStyleClass = event.styleClass ? ' ' + event.styleClass : '';
			var eventElement = ice.ace.jq('<div class=\"ui-state-hover ui-corner-all schedule-event schedule-event-' + event.index + customStyleClass + '\"></div>');
			eventElement.html(event.startTime + ' ' + event.title);
			eventElement.css({position:'absolute', top:position.top+2, left:position.left+2, width: width + 'px', 
				height: (endPosition.top - position.top + height) + 'px'}).appendTo(eventsContainer);
			ice.ace.jq('<div class="schedule-list-event"><div class="schedule-list-event-day">'+event.startDate.substring(8)+'</div><div class="schedule-list-event-name">'+event.title+'</div><div class="schedule-list-event-location">'+event.location+'</div></div>').appendTo(sidebarEventsContainer);
		}
	}
	this.expandEventList();
};

ice.ace.Schedule.prototype.renderLazyWeekEvents = function(data) {
	this.addListeners();
	var eventsContainer = ice.ace.jq(this.jqId).find('.schedule-event-container');
	eventsContainer.html('');
	var sidebarEventsContainer = ice.ace.jq(this.jqId).find('.schedule-list-content');
	var i,j;
	var lazyYear = this.cfg.lazyYear;
	var lazyMonth = this.cfg.lazyMonth;
	var lazyDay = this.cfg.lazyDay;
	var is31DaysMonth = lazyMonth == 0 || lazyMonth == 2 || lazyMonth == 4 || lazyMonth == 6
		|| lazyMonth == 7 || lazyMonth == 9 || lazyMonth == 11;
	var isLeapYear = ((lazyYear % 4 == 0) && (lazyYear % 100 != 0)) || (lazyYear % 400 == 0);
	var weekStartDate = new Date(lazyYear, lazyMonth, lazyDay, 0, 0, 0, 0);
	if (is31DaysMonth) {
		if (lazyMonth == 11) {
			if (lazyDay >= 25) {
				lazyYear++;
				lazyMonth = 0;
				lazyDay = lazyDay + 7 - 31;
			} else {
				lazyDay = lazyDay + 7;
			}
		} else if (lazyDay >= 24) {
			lazyMonth++;
			lazyDay = lazyDay + 7 - 31;
		} else {
			lazyDay = lazyDay + 7;
		}
	} else {
		if (lazyMonth == 1) {
			if (isLeapYear) {
				if (lazyDay >= 23) {
					lazyMonth = 2;
					lazyDay = lazyDay + 7 - 29;
				} else {
					lazyDay = lazyDay + 7;
				}
			} else {
				if (lazyDay >= 22) {
					lazyMonth = 2;
					lazyDay = lazyDay + 7 - 28;
				} else {
					lazyDay = lazyDay + 7;
				}
			}
		} else if (lazyDay >= 24) {
			lazyMonth++;
			lazyDay = lazyDay + 7 - 30;
		} else {
			lazyDay = lazyDay + 7;
		}
	}
	var weekEndDate = new Date(lazyYear, lazyMonth, lazyDay, 0, 0, 0, 0);
	var title = this.getMonthName(weekStartDate.getMonth()) + ' ' + weekStartDate.getDate() + ' - '
		+ this.getMonthName(weekEndDate.getMonth()) + ' ' + weekEndDate.getDate();
	this.jq.find('.schedule-showing').html(title);
	// set the scrollable height, if supplied
	if  (this.cfg.scrollHeight) this.jq.find('.schedule-days').css({height:this.cfg.scrollHeight});
	// set day of the week headers and calendar day CSS classes
	var dowCount;
	for (dowCount = 0; dowCount < 7; dowCount++) {
		var dowDate = new Date(weekStartDate.getTime() + (86400000 * dowCount));
		var dayHeader = this.jq.find('.schedule-dow-header.schedule-dow-'+dowCount);
		dayHeader.html(this.getDayOfTheWeekNameShort(dowCount) + ', ' 
			+ this.getMonthNameShort(dowDate.getMonth()) + '/' + dowDate.getDate());
		var month = dowDate.getMonth() + 1;
		var day = dowDate.getDate();
		this.jq.find('.schedule-cell.schedule-dow-'+dowCount).addClass('calendar-day-'+dowDate.getFullYear()+'-'+(month < 10 ? '0' + month : month)+'-'+(day < 10 ? '0' + day : day));
	}
	ice.ace.jq('.calendar-day-'+moment().format("YYYY-MM-DD")).html('<div class="ui-state-highlight"></div>');
	// add event divs at appropriate positions
	for (i = 0; i < data.days.length; i++) {
		var day = data.days[i];
		var date = day.date.toDate();
		if (date >= weekStartDate && date < weekEndDate) {
			var dow = i % 7;
			for (j = 0; j < day.events.length; j++) {
				var event = day.events[j];
				var hour = event.startTime.substring(0,2);
				var minutes = parseInt(event.startTime.substring(3,5));
				var selector = '.schedule-dow-'+dow+'.schedule-time-'+hour+(minutes >= 30 ? '30' : '00');
				var timeCell = this.jq.find(selector);
				var position = timeCell.position();
				var width = timeCell.width();
				var endHour = event.endTime.substring(0,2);
				var endMinutes = parseInt(event.endTime.substring(3,5));
				var endSelector = '.schedule-dow-'+dow+'.schedule-time-'+this.determinePreviousTimeCell(endHour, endMinutes);
				var endTimeCell = this.jq.find(endSelector);
				var endPosition = endTimeCell.position();
				var height = endTimeCell.height();
				var customStyleClass = event.styleClass ? ' ' + event.styleClass : '';
				var eventElement = ice.ace.jq('<div class=\"ui-state-hover ui-corner-all schedule-event schedule-event-' + event.index + customStyleClass + '\"></div>');
				eventElement.html(event.startTime + ' ' + event.title);
				eventElement.css({position:'absolute', top:position.top+2, left:position.left+2, width: width + 'px',
					height: (endPosition.top - position.top + height) + 'px'}).appendTo(eventsContainer);
				ice.ace.jq('<div class="schedule-list-event"><div class="schedule-list-event-day">'+event.startDate.substring(8)+'</div><div class="schedule-list-event-name">'+event.title+'</div><div class="schedule-list-event-location">'+event.location+'</div></div>').appendTo(sidebarEventsContainer);
			}
		}
	}
	this.expandEventList();
};

ice.ace.Schedule.prototype.renderDayView = function(data) {
	this.data = data;
	var i, j;
	var markup =
	"<div class=\"schedule-title ui-state-active\">"
		+"<div class=\"schedule-button-previous\"><i class=\"fa fa-arrow-left\"></i></div>"
		+"<div class=\"schedule-button-next\"><i class=\"fa fa-arrow-right\"></i></div>"
		+"<div class=\"schedule-showing\">" + data.month + " " + data.year + "</div>"
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
							+"<td class=\"ui-widget-content schedule-cell schedule-dow-single schedule-time-" + iString + "00\"></td>"
						+"</tr><tr>"
							+"<td class=\"ui-widget-content schedule-cell schedule-cell-time\">" + i + ":30</td>"
							+"<td class=\"ui-widget-content schedule-cell schedule-dow-single schedule-time-" + iString + "30\"></td>"
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

ice.ace.Schedule.prototype.renderDayEvents = function(data) {
	this.addListeners();
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
	this.jq.find('.schedule-cell.schedule-dow-single').addClass('calendar-day-'+currentYear+'-'+(currentMonth < 10 ? '0' + currentMonth : currentMonth)+'-'+(currentDay < 10 ? '0' + currentDay : currentDay));
	// add event divs at appropriate positions
	var i;
	for (i = 0; i < this.events.length; i++) {
		var event = this.events[i];
		var date = new Date();
		date.setFullYear(event.startDate.substring(0,4));
		date.setMonth(parseInt(event.startDate.substring(5,7) - 1));
		date.setDate(event.startDate.substring(8,10));
		if (date.getFullYear() == currentYear && date.getMonth() == currentMonth && date.getDate() == currentDay) {
			var hour = event.startTime.substring(0,2);
			var minutes = parseInt(event.startTime.substring(3,5));
			var selector = '.schedule-dow-single.schedule-time-'+hour+(minutes >= 30 ? '30' : '00');
			var timeCell = this.jq.find(selector);
			var position = timeCell.position();
			var width = timeCell.width();
			var endHour = event.endTime.substring(0,2);
			var endMinutes = parseInt(event.endTime.substring(3,5));
			var endSelector = '.schedule-dow-single.schedule-time-'+this.determinePreviousTimeCell(endHour, endMinutes);
			var endTimeCell = this.jq.find(endSelector);
			var endOffset = endTimeCell.position();
			var height = endTimeCell.height();
			var customStyleClass = event.styleClass ? ' ' + event.styleClass : '';
			var eventElement = ice.ace.jq('<div class=\"ui-state-hover ui-corner-all schedule-event schedule-event-' + event.index + customStyleClass + '\"></div>');
			eventElement.html(event.startTime + ' ' + event.title);
			eventElement.css({position:'absolute', top:position.top+2, left:position.left+2, width: width + 'px',
				height: (endOffset.top - position.top + height) + 'px'}).appendTo(eventsContainer);
			ice.ace.jq('<div class="schedule-list-event"><div class="schedule-list-event-day">'+event.startTime+'</div><div class="schedule-list-event-name">'+event.title+'</div><div class="schedule-list-event-location">'+event.location+'</div></div>').appendTo(sidebarEventsContainer);
		}
	}
	this.expandEventList();
};

ice.ace.Schedule.prototype.renderLazyDayEvents = function(data) {
	this.addListeners();
	var eventsContainer = ice.ace.jq(this.jqId).find('.schedule-event-container');
	eventsContainer.html('');
	var sidebarEventsContainer = ice.ace.jq(this.jqId).find('.schedule-list-content');
	var lazyYear = this.cfg.lazyYear;
	var lazyMonth = this.cfg.lazyMonth;
	var lazyDay = this.cfg.lazyDay;
	var title = this.getMonthName(lazyMonth) + ' ' + lazyDay + ', ' + lazyYear;
	this.jq.find('.schedule-showing').html(title);
	// set the scrollable height, if supplied
	if  (this.cfg.scrollHeight) this.jq.find('.schedule-days').css({height:this.cfg.scrollHeight});
	// set day of the week header
	var dayHeader = this.jq.find('.schedule-dow-header.schedule-dow-single');
	var date = new Date();
	date.setFullYear(lazyYear);
	date.setMonth(lazyMonth);
	date.setDate(lazyDay);
	dayHeader.html(this.getDayOfTheWeekName(date.getDay()));
	// add calendar day CSS classes
	this.jq.find('.schedule-cell.schedule-dow-single').addClass('calendar-day-'+lazyYear+'-'+(lazyMonth < 10 ? '0' + lazyMonth : lazyMonth)+'-'+(lazyDay < 10 ? '0' + lazyDay : lazyDay));
	// add event divs at appropriate positions
	var i;
	for (i = 0; i < this.events.length; i++) {
		var event = this.events[i];
		var date = new Date();
		date.setFullYear(event.startDate.substring(0,4));
		date.setMonth(parseInt(event.startDate.substring(5,7) - 1));
		date.setDate(event.startDate.substring(8,10));
		if (date.getFullYear() == lazyYear && date.getMonth() == lazyMonth && date.getDate() == lazyDay) {
			var hour = event.startTime.substring(0,2);
			var minutes = parseInt(event.startTime.substring(3,5));
			var selector = '.schedule-dow-single.schedule-time-'+hour+(minutes >= 30 ? '30' : '00');
			var timeCell = this.jq.find(selector);
			var position = timeCell.position();
			var width = timeCell.width();
			var endHour = event.endTime.substring(0,2);
			var endMinutes = parseInt(event.endTime.substring(3,5));
			var endSelector = '.schedule-dow-single.schedule-time-'+this.determinePreviousTimeCell(endHour, endMinutes);
			var endTimeCell = this.jq.find(endSelector);
			var endPosition = endTimeCell.position();
			var height = endTimeCell.height();
			var customStyleClass = event.styleClass ? ' ' + event.styleClass : '';
			var eventElement = ice.ace.jq('<div class=\"ui-state-hover ui-corner-all schedule-event schedule-event-' + event.index + customStyleClass + '\"></div>');
			eventElement.html(event.startTime + ' ' + event.title);
			eventElement.css({position:'absolute', top:position.top+2, left:position.left+2, width: width + 'px',
				height: (endPosition.top - position.top + height) + 'px'}).appendTo(eventsContainer);
			ice.ace.jq('<div class="schedule-list-event"><div class="schedule-list-event-day">'+event.startTime+'</div><div class="schedule-list-event-name">'+event.title+'</div><div class="schedule-list-event-location">'+event.location+'</div></div>').appendTo(sidebarEventsContainer);
		}
	}
	this.expandEventList();
};

ice.ace.Schedule.prototype.addListeners = function() {
	var self = this;
	if (this.cfg.isLazy) {
		var previousButton = this.jq.find('.schedule-button-previous');
		var nextButton = this.jq.find('.schedule-button-next');
		var view = this.cfg.viewMode;
		var lazyYear = this.cfg.lazyYear;
		var lazyMonth = this.cfg.lazyMonth;
		var lazyDay = this.cfg.lazyDay;
		var is31DaysMonth = lazyMonth == 0 || lazyMonth == 2 || lazyMonth == 4 || lazyMonth == 6
			|| lazyMonth == 7 || lazyMonth == 9 || lazyMonth == 11;
		var isLeapYear = ((lazyYear % 4 == 0) && (lazyYear % 100 != 0)) || (lazyYear % 400 == 0);
		previousButton.on('click', function(e) {
			e.stopPropagation();
			if (view == 'week') {
				if (is31DaysMonth) {
					if (lazyMonth == 0) {
						if (lazyDay <= 7) {
							lazyYear--;
							lazyMonth = 11;
							lazyDay = lazyDay - 7 + 31;
						} else {
							lazyDay = lazyDay - 7;
						}
					} else if (lazyMonth == 2) {
						if (isLeapYear) {
							if (lazyDay <= 7) {
								lazyMonth = 1;
								lazyDay = lazyDay - 7 + 29;
							} else {
								lazyDay = lazyDay - 7;
							}
						} else {
							if (lazyDay <= 7) {
								lazyMonth = 1;
								lazyDay = lazyDay - 7 + 28;
							} else {
								lazyDay = lazyDay - 7;
							}
						}
					} else if (lazyMonth == 7) {
						if (lazyDay <= 7) {
							lazyMonth = 6;
							lazyDay = lazyDay - 7 + 31;
						} else {
							lazyDay = lazyDay - 7;
						}
					} else if (lazyDay <= 7) {
						lazyMonth--;
						lazyDay = lazyDay - 7 + 30;
					} else {
						lazyDay = lazyDay - 7;
					}
				} else {
					if (lazyDay <= 7) {
						lazyMonth--;
						lazyDay = lazyDay - 7 + 31;
					} else {
						lazyDay = lazyDay - 7;
					}
				}
			} else if (view == 'day') {
				if (is31DaysMonth) {
					if (lazyMonth == 0) {
						if (lazyDay == 1) {
							lazyYear--;
							lazyMonth = 11;
							lazyDay = 31;
						} else {
							lazyDay--;
						}
					} else if (lazyMonth == 2) {
						if (isLeapYear) {
							if (lazyDay == 1) {
								lazyMonth = 1;
								lazyDay = 29;
							} else {
								lazyDay--;
							}
						} else {
							if (lazyDay == 1) {
								lazyMonth = 1;
								lazyDay = 28;
							} else {
								lazyDay--;
							}
						}
					} else if (lazyMonth == 7) {
						if (lazyDay == 1) {
							lazyMonth = 6;
							lazyDay = 31;
						} else {
							lazyDay--;
						}
					} else if (lazyDay == 1) {
						lazyMonth--;
						lazyDay = 30;
					} else {
						lazyDay--;
					}
				} else {
					if (lazyDay == 1) {
						lazyMonth--;
						lazyDay = 31;
					} else {
						lazyDay--;
					}
				}
			} else {
				if (lazyMonth == 0) {
					lazyYear--;
					lazyMonth = 11;
				} else lazyMonth--;
				lazyDay = 1;
			}
			self.sendNavigationRequest(e, lazyYear, lazyMonth, lazyDay, 'previous');
		});
		nextButton.on('click', function(e) {
			e.stopPropagation();
			if (view == 'week') {
				if (is31DaysMonth) {
					if (lazyMonth == 11) {
						if (lazyDay >= 25) {
							lazyYear++;
							lazyMonth = 0;
							lazyDay = lazyDay + 7 - 31;
						} else {
							lazyDay = lazyDay + 7;
						}
					} else if (lazyDay >= 24) {
						lazyMonth++;
						lazyDay = lazyDay + 7 - 31;
					} else {
						lazyDay = lazyDay + 7;
					}
				} else {
					if (lazyMonth == 1) {
						if (isLeapYear) {
							if (lazyDay >= 23) {
								lazyMonth = 2;
								lazyDay = lazyDay + 7 - 29;
							} else {
								lazyDay = lazyDay + 7;
							}
						} else {
							if (lazyDay >= 22) {
								lazyMonth = 2;
								lazyDay = lazyDay + 7 - 28;
							} else {
								lazyDay = lazyDay + 7;
							}
						}
					} else if (lazyDay >= 24) {
						lazyMonth++;
						lazyDay = lazyDay + 7 - 30;
					} else {
						lazyDay = lazyDay + 7;
					}
				}
			} else if (view == 'day') {
				if (is31DaysMonth) {
					if (lazyMonth == 11) {
						if (lazyDay == 31) {
							lazyYear++;
							lazyMonth = 0;
							lazyDay = 1;
						} else {
							lazyDay++;
						}
					} else if (lazyDay == 31) {
						lazyMonth++;
						lazyDay = 1;
					} else {
						lazyDay++;
					}
				} else {
					if (lazyMonth == 1) {
						if (isLeapYear) {
							if (lazyDay == 29) {
								lazyMonth = 2;
								lazyDay = 1;
							} else {
								lazyDay++;
							}
						} else {
							if (lazyDay == 28) {
								lazyMonth = 2;
								lazyDay = 1;
							} else {
								lazyDay++;
							}
						}
					} else if (lazyDay == 30) {
						lazyMonth++;
						lazyDay = 1;
					} else {
						lazyDay++;
					}
				}
			} else {
				if (lazyMonth == 11) {
					lazyYear++;
					lazyMonth = 0;
				} else lazyMonth++;
				lazyDay = 1;
			}
			self.sendNavigationRequest(e, lazyYear, lazyMonth, lazyDay, 'next');
		});
	} else {
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
			self.cfg.currentYear = currentYear;
			self.cfg.currentMonth = currentMonth;
			self.cfg.currentDay = currentDay;
			if (view == 'week' || view == 'day') {
				self.clndr.render();
			} else {
				self.clndr.backAction({data:{context:self.clndr}});
			}
			self.sendNavigationRequest(e, currentYear, currentMonth, currentDay, 'previous');
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
			self.cfg.currentYear = currentYear;
			self.cfg.currentMonth = currentMonth;
			self.cfg.currentDay = currentDay;
			if (view == 'week' || view == 'day') {
				self.clndr.render();
			} else {
				self.clndr.forwardAction({data:{context:self.clndr}});
			}
			self.sendNavigationRequest(e, currentYear, currentMonth, currentDay, 'next');
		});
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
	var contentHeight = this.getSidebarContentHeight();
	var listContent = ice.ace.jq(this.jqId).find('.schedule-list-content');
	var detailsContent = ice.ace.jq(this.jqId).find('.schedule-details-content');
	listContent.css('height', '0');
	detailsContent.css('height', contentHeight + 'px');
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