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
	
	var configuration = {};
	configuration.template = ice.ace.jq(this.jqId + '_template').html()
	configuration.events = this.events;
	configuration.forceSixRows = true;
	if (this.cfg.isLazy) {
		configuration.startWithMonth = cfg.lazyYear + '-' + (cfg.lazyMonth + 1) + '-01'; // CLNDR month is 1-relative
	}
	this.jq.clndr(configuration);

	if (this.cfg.isLazy) {
		var self = this;
		var previousButton = this.jq.find('.clndr-previous-button');
		var nextButton = this.jq.find('.clndr-next-button');
		var lazyYear = cfg.lazyYear;
		var lazyMonth = cfg.lazyMonth;
		previousButton.on('click', function(e) {
			e.stopPropagation()
			if (lazyMonth == 0) {
				lazyYear--;
				lazyMonth = 11;
			} else lazyMonth--;
			self.sendLazyNavigationRequest(e, lazyYear, lazyMonth);
		});
		nextButton.on('click', function(e) {
			e.stopPropagation()
			if (lazyMonth == 11) {
				lazyYear++;
				lazyMonth = 0;
			} else lazyMonth++;
			self.sendLazyNavigationRequest(e, lazyYear, lazyMonth);
		});
	}

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
		var self = this;
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

ice.ace.Schedule.prototype.sendLazyNavigationRequest = function(event, lazyYear, lazyMonth) {
    var options = {
		source: this.id,
		render: this.id,
		execute: this.id
    };

    var params = {};
    params[this.id + "_lazyYear"] = lazyYear;
    params[this.id + "_lazyMonth"] = lazyMonth;
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