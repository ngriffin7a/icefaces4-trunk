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
	this.cfg = cfg;
	this.events = cfg.events;
	
	var configuration = {};
	configuration.template = ice.ace.jq(this.jqId + '_template').html()
	configuration.events = this.events;
	configuration.forceSixRows = true;
	this.jq.clndr(configuration);

	this.eventsMap = this.createEventsMap(this.events);

	if (cfg.displayEventDetails != 'disabled') {
		var self = this;
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
				var node = event['target'];
				var parent = node.parentNode;
				var eventNumber = self.extractEventNumber(node);
				var date = self.extractEventDate(parent);
				var eventArray = self.eventsMap[date];
				var eventData = null;
				if (eventArray) eventData = eventArray[eventNumber];
				var markup = self.getEventDetailsMarkup(eventData);
				if (self.cfg.displayEventDetails == 'popup')
					self.displayEventDetailsPopup(markup);
				else
					self.displayEventDetailsSidebar(markup);
			});
		}
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

ice.ace.Schedule.prototype.getEventDetailsMarkup = function(data) {
	if (data) {
		return '<span>Date: '+data.date+'</span><br/><span>Time: '+data.time+'</span><br/><span>Title: '+data.title+'</span><br/><span>Location: '+data.location+'</span><br/><span>Notes: '+data.notes+'</span>';
	} else {
		return '<div>No Data</div>';
	}
}

ice.ace.Schedule.prototype.displayEventDetailsPopup = function(markup) {
	var eventDetails = ice.ace.jq(this.jqId).find('.event-details-popup');
	eventDetails.html(markup);
	eventDetails.dialog();
};

ice.ace.Schedule.prototype.displayEventDetailsSidebar = function(markup) {
	var eventDetails = ice.ace.jq(this.jqId).find('.event-details .event-details-content');
	eventDetails.html(markup);
};

ice.ace.Schedule.prototype.displayEventDetailsTooltip = function(markup, node) {
	var eventDetails = ice.ace.jq(this.jqId).find('.event-details-tooltip');
	eventDetails.html(markup);
	eventDetails.dialog({resizable: false, draggable: false, dialogClass: 'ice-ace-schedule-details-tooltip', 
		position: { my: "left top", at: "right bottom", of: node }});
	ice.ace.jq(this.jqId).find('.ice-ace-schedule-details-tooltip').show();
};

ice.ace.Schedule.prototype.hideEventDetailsTooltip = function() {
	ice.ace.jq(this.jqId).find('.ice-ace-schedule-details-tooltip').hide();
};