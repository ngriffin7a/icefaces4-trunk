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
	this.jq = ice.ace.jq(this.jqId).find('.ice-ace-schedule');
	this.events = cfg.events;
	
	var configuration = {};
	configuration.template = ice.ace.jq(this.jqId + '_template').html()
	configuration.events = this.events;
	configuration.forceSixRows = true;
	this.jq.clndr(configuration);

	this.eventsMap = this.createEventsMap(this.events);

	var self = this;
	this.jq.delegate('.schedule-event', 'click', function(event) {
		var node = event['target'];
		var parent = node.parentNode;
		var eventNumber = self.extractEventNumber(node);
		var date = self.extractEventDate(parent);
		var eventArray = self.eventsMap[date];
		var eventData = null;
		if (eventArray) eventData = eventArray[eventNumber];
		self.displayEventDetailsPopup(eventData);
	});
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

ice.ace.Schedule.prototype.displayEventDetailsPopup = function(data) {
	var eventDetails = ice.ace.jq(this.jqId).find('.event-details');
	if (data) {
		eventDetails.html('<p>Date: '+data.date+'</p><p>Time: '+data.time+'</p><p>Title: '+data.title+'</p><p>Location: '+data.location+'</p><p>Notes: '+data.notes+'</p>');
	} else {
		eventDetails.html('No Data');
	}
	eventDetails.dialog();
};