/*
 *
 */

if (!window['ice']) window.ice = {};
if (!window.ice['ace']) window.ice.ace = {};
ice.ace.schedule = {};
ice.ace.schedule.registry = {};

ice.ace.schedule.renderSchedule = function(id, events) {
	var configuration = {};
	var escapedClientId = ice.ace.escapeClientId(id);
	configuration.template = ice.ace.jq(escapedClientId + '_template').html()
	configuration.events = events;
	configuration.forceSixRows = true;
	ice.ace.jq(escapedClientId).find('.ice-ace-schedule').clndr(configuration);
};

ice.ace.schedule.renderFunction = function(data) {
	var wrapper = document.createElement('div');
	var currentRowDiv = document.createElement('div');
	var days = data.days;
	var i;
	for (i = 0; i < days.length; i++) {
		var day = days[i];
		var daySpan = document.createElement('span');
		daySpan.innerHTML = day.day;
		ice.ace.jq(daySpan).attr('class', day.classes);
		if (i % 7 == 0) {
			wrapper.appendChild(currentRowDiv);
			currentRowDiv = document.createElement('div');
		}
		currentRowDiv.appendChild(daySpan);
	}
	wrapper.appendChild(currentRowDiv); // last row
	return wrapper.innerHTML;
};
