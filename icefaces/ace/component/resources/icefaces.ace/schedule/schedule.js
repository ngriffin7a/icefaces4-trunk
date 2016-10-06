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
	this.jq = ice.ace.jq(this.jqId).find('.ice-ace-schedule-body');
	this.cfg = cfg;
	this.events = cfg.events;
	var self = this;
	
	var configuration = {};
	if (this.cfg.viewMode == 'week') {
		configuration.render = function(data) { return self.renderWeekView.call(self, data); };
		configuration.doneRendering = function() { self.renderWeekEvents.call(self, self.data); };
	} else if (this.cfg.viewMode == 'day') {

	} else {
		configuration.template = ice.ace.jq(this.jqId + '_template').html();
	}
	configuration.events = this.events;
	configuration.forceSixRows = true;
	if (this.cfg.isLazy) {
		configuration.startWithMonth = cfg.lazyYear + '-' + (cfg.lazyMonth + 1) + '-01'; // CLNDR month is 1-relative
	}
	this.jq.clndr(configuration);

	if (this.cfg.isLazy) {
		var previousButton = this.jq.find('.clndr-previous-button'); // maybe we don't use these classes to avoid those listeners
		var nextButton = this.jq.find('.clndr-next-button');
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
					} else if (lazyMonth == 7) {
						if (lazyDay <= 7) {
							lazyMonth--;
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
					if (lazyMonth == 2) {
						if (isLeapYear) {
							if (lazyDay <= 7) {
								lazyDay = lazyDay - 7 + 29;
								lazyMonth = 1;
							} else {
								lazyDay = lazyDay - 7;
							}
						} else {
							if (lazyDay <= 7) {
								lazyDay = lazyDay - 7 + 28;
								lazyMonth = 1;
							} else {
								lazyDay = lazyDay - 7;
							}
						}
					} else if (lazyDay <= 7) {
						lazyMonth--;
						lazyDay = lazyDay - 7 + 31;
					} else {
						lazyDay = lazyDay - 7;
					}
				}
			} else if (view == 'day') {

			} else {
				if (lazyMonth == 0) {
					lazyYear--;
					lazyMonth = 11;
				} else lazyMonth--;
				lazyDay = 1;
			}
			self.sendLazyNavigationRequest(e, lazyYear, lazyMonth, lazyDay);
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

			} else {
				if (lazyMonth == 11) {
					lazyYear++;
					lazyMonth = 0;
				} else lazyMonth++;
				lazyDay = 1;
			}
			self.sendLazyNavigationRequest(e, lazyYear, lazyMonth, lazyDay);
		});
	}

	this.eventsMap = this.createEventsMap(this.events);

	if (cfg.displayEventDetails != 'disabled') {
		if (cfg.displayEventDetails == 'tooltip') {
			this.jq.delegate('.schedule-event', 'mouseover', function(event) {
				var node = event['target'];
				var parent = node.parentNode;
				var eventNumber = self.extractEventNumber(node);
				var date = self.extractEventDate(parent);
				var eventArray = self.eventsMap[date];
				var eventData = null;
				if (eventArray) eventData = eventArray[eventNumber];
				var markup = self.getEventDetailsMarkup(eventData);
				self.displayEventDetailsTooltip(markup, node);
			});
			this.jq.delegate('.schedule-event', 'mouseout', function(event) {
				self.hideEventDetailsTooltip();
			});
		} else {
			this.jq.delegate('.schedule-event', 'click', function(event) {
				event.stopImmediatePropagation();
				var node = event['target'];
				var parent = node.parentNode;
				var eventNumber = self.extractEventNumber(node);
				var date = self.extractEventDate(parent);
				var eventArray = self.eventsMap[date];
				var eventData = null;
				if (eventArray) eventData = eventArray[eventNumber];
				var markup = self.getEventDetailsMarkup(eventData, false,
					self.cfg.isEventEditing, self.cfg.isEventDeletion);
				if (self.cfg.displayEventDetails == 'popup')
					self.displayEventDetailsPopup(markup);
				else
					self.displayEventDetailsSidebar(markup);
			});
		}
	}
	if (self.cfg.isEventAddition) {
		this.jq.delegate('.day', 'click', function(event) {
			var node = event['target'];
			var date = self.extractEventDate(node);
			var eventData = {date: date, time: '', title: '', location: '', notes: '', index: ''};
			var markup = self.getEventDetailsMarkup(eventData, true, false, false);
			self.displayEventDetailsPopup(markup);
		});
	}
};

ice.ace.Schedule.prototype.createEventsMap = function(eventsArray) {
	var eventsMap = {};
	var i;
	for (i = 0; i < eventsArray.length; i++) {
		var event = eventsArray[i];
		var date = event.date;
		if (!eventsMap[date]) eventsMap[date] = [];
		eventsMap[date].push(event);
	}
	return eventsMap;
};

ice.ace.Schedule.prototype.extractEventNumber = function(node) {
	var result = 0;
	var classes = node.className.split(' ');
	var i;
	for (i = 0; i < classes.length; i++) {
		var styleClass = classes[i];
		if (styleClass.indexOf('event-') == 0) {
			result = styleClass.substring(6);
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

ice.ace.Schedule.prototype.getEventDetailsMarkup = function(data, isEventAddition, isEventEditing, isEventDeletion) {
	if (data) {// *** escape HTML characters
		var markup;
		if (isEventAddition || isEventEditing) {
			markup = '<table><tr><td>Date:</td><td><input type="date" name="'+this.id+'_date" value="'+data.date+'"/></td></tr><tr><td>Time:</td><td><input type="time" name="'+this.id+'_time" value="'+data.time+'"/></td></tr><tr><td>Title:</td><td><input type="text" name="'+this.id+'_title" value="'+data.title+'"/></td></tr><tr><td>Location:</td><td><input type="text" name="'+this.id+'_location" value="'+data.location+'"/></td></tr><tr><td>Notes:</td><td><textarea name="'+this.id+'_notes">'+data.notes+'</textarea></td></tr></table><input type="hidden" name="'+this.id+'_index" value="'+data.index+'"/>';
		} else {
			markup = '<table><tr><td>Date:</td><td>'+data.date+'</td></tr><tr><td>Time:</td><td>'+data.time+'</td></tr><tr><td>Title:</td><td>'+data.title+'</td></tr><tr><td>Location:</td><td>'+data.location+'</td></tr><tr><td>Notes:</td><td>'+data.notes+'</td></tr></table>';
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
	var eventDetails = ice.ace.jq(this.jqId).find('.event-details-popup-body');
	eventDetails.html(markup);
	eventDetails.dialog({dialogClass: 'event-details-popup'});
};

ice.ace.Schedule.prototype.displayEventDetailsSidebar = function(markup) {
	var eventDetails = ice.ace.jq(this.jqId).find('.event-details .event-details-body');
	eventDetails.html(markup);
};

ice.ace.Schedule.prototype.displayEventDetailsTooltip = function(markup, node) {
	var eventDetails = ice.ace.jq(this.jqId).find('.event-details-tooltip-body');
	eventDetails.html(markup);
	eventDetails.dialog({resizable: false, draggable: false, dialogClass: 'event-details-tooltip', 
		position: { my: "left top", at: "right bottom", of: node }});
	ice.ace.jq(this.jqId).find('.event-details-tooltip').show();
};

ice.ace.Schedule.prototype.hideEventDetailsTooltip = function() {
	ice.ace.jq(this.jqId).find('.event-details-tooltip').hide();
};

ice.ace.Schedule.prototype.sendLazyNavigationRequest = function(event, lazyYear, lazyMonth, lazyDay) {
    var options = {
		source: this.id,
		render: this.id,
		execute: this.id
    };

    var params = {};
    params[this.id + "_lazyYear"] = lazyYear;
    params[this.id + "_lazyMonth"] = lazyMonth;
    params[this.id + "_lazyDay"] = lazyDay;
    options.params = params;

	ice.ace.AjaxRequest(options);
};

ice.ace.Schedule.prototype.sendEditRequest = function(event, type) {
    var options = {
		source: this.id,
		render: this.id,
		execute: this.id
    };

    var params = {};
	if (type == 'add') params[this.id + "_add"] = true;
    else if (type == 'edit') params[this.id + "_edit"] = true;
    else if (type == 'delete') params[this.id + "_delete"] = true;
    options.params = params;

	ice.ace.AjaxRequest(options);
};

ice.ace.Schedule.prototype.renderWeekView = function(data) {
	this.data = data;
	var i, j;
	var markup =
	"<div class=\"clndr-controls ui-state-active\">"
		+"<div class=\"clndr-previous-button\">&lt;</div>"
		+"<div class=\"clndr-next-button\">&gt;</div>"
		+"<div class=\"current-month\">" + data.month + " " + data.year + "</div>"
	+"</div>"

	+"<div class=\"schedule-content\">"

		/* time grid */
		+"<div class=\"clndr-grid ui-widget-content\">"
			+"<table><thead class=\"days-of-the-week ui-state-default\"><tr>"
				+"<th class=\"header-day\">Time</th>";

				for (i = 0; i < data.daysOfTheWeek.length; i++) {
					var day = data.daysOfTheWeek[i];
					markup +="<th class=\"header-day dow-" + i + "\">" + day + "</th>";
				}

			markup += "</tr></thead>"
			+"<tbody class=\"days\">";

				for (i = 0; i < 24; i++) {
					var iString = i < 10 ? '0' + i : i;
					markup += "<tr>"
						+"<td class=\"ui-widget-content schedule-cell\">" + i + ":00</td>"
						+"<td class=\"ui-widget-content schedule-cell dow-0 time-" + iString + "00\"></td>"
						+"<td class=\"ui-widget-content schedule-cell dow-1 time-" + iString + "00\"></td>"
						+"<td class=\"ui-widget-content schedule-cell dow-2 time-" + iString + "00\"></td>"
						+"<td class=\"ui-widget-content schedule-cell dow-3 time-" + iString + "00\"></td>"
						+"<td class=\"ui-widget-content schedule-cell dow-4 time-" + iString + "00\"></td>"
						+"<td class=\"ui-widget-content schedule-cell dow-5 time-" + iString + "00\"></td>"
						+"<td class=\"ui-widget-content schedule-cell dow-6 time-" + iString + "00\"></td>"
					+"</tr><tr>"
						+"<td class=\"ui-widget-content schedule-cell\">" + i + ":30</td>"
						+"<td class=\"ui-widget-content schedule-cell dow-0 time-" + iString + "30\"></td>"
						+"<td class=\"ui-widget-content schedule-cell dow-1 time-" + iString + "30\"></td>"
						+"<td class=\"ui-widget-content schedule-cell dow-2 time-" + iString + "30\"></td>"
						+"<td class=\"ui-widget-content schedule-cell dow-3 time-" + iString + "30\"></td>"
						+"<td class=\"ui-widget-content schedule-cell dow-4 time-" + iString + "30\"></td>"
						+"<td class=\"ui-widget-content schedule-cell dow-5 time-" + iString + "30\"></td>"
						+"<td class=\"ui-widget-content schedule-cell dow-6 time-" + iString + "30\"></td>"
					+"</tr>";
				}

			markup += "</tbody></table>"
		+"</div>";

		markup += "<div class=\"schedule-sidebar ui-widget-content\">"

			+"<div class=\"event-listing\">"
				+"<div class=\"event-listing-title ui-state-default\">Events this Month</div>"
				+"<div class=\"event-listing-body\">";

					for (i = 0; i < data.eventsThisMonth.length; i++) {
						var event = data.eventsThisMonth[i];
						markup += "<div class=\"event-item\">"
							+"<div class=\"event-item-name\">" + event.title + "</div>"
							+"<div class=\"event-item-location\">" + event.location + "</div>"
						+"</div>";
					}

				markup += "</div>"
			+"</div>"

			+"<div class=\"event-details\">"
				+"<div class=\"event-details-title ui-state-default\">Event Details</div>"
				+"<div class=\"event-details-body\"></div>"
			+"</div>"

		+"</div>"

	+"</div>";

	return markup;
};

ice.ace.Schedule.prototype.renderWeekEvents = function(data) {
	//special div, sibling of the tooltip and the popup, .html('') to remove previous events
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
	this.jq.find('.current-month').html(title);
	var dowCount = 0;
	for (i = 0; i < data.days.length; i++) {
		var day = data.days[i];
		var date = day.date.toDate();
		if (date > weekStartDate && date < weekEndDate) {
			var count = 0;
			var dow = i % 7;
			var dayHeader = this.jq.find('.header-day.dow-'+dowCount);
			dayHeader.html(dayHeader.html() + ', ' + this.getMonthNameShort(date.getMonth()) + '/' + date.getDate());
			for (j = 0; j < day.events.length; j++) {
				var event = day.events[j];
				var hour = event.time.substring(0,2);
				var selector = '.dow-'+dow+'.time-'+hour+'00';
				var timeCell = this.jq.find(selector);
				var offset = timeCell.offset();
				var width = timeCell.width();
				var eventElement = ice.ace.jq('<div class=\"ui-state-hover ui-corner-all schedule-event event-'+count+'\"></div');
				eventElement.html(event.time + ' ' + event.title);
				eventElement.css({position:'absolute', top:offset.top+1, left:offset.left+1, width: width + 'px'}).appendTo(this.jq);
				//appentTo special div, sibling of the tooltip and the popup
				count++;
			}
			dowCount++;
		}
	}
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