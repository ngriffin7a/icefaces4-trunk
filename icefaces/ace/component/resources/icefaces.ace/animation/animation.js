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

if (!window['ice']) window.ice = {};
if (!window.ice['ace']) window.ice.ace = {};
ice.ace.animation = {};
		
ice.ace.animation.run = function(args, options, speed, callback) {

	var node;
	if (typeof args.node == 'string') {
		node = ice.ace.jq(ice.ace.escapeClientId(args.node));
	} else {
		node = ice.ace.jq(args.node);
	}
	var effectName = args.name.toLowerCase();
	var easing = args.easing || 'easeOutQuad';

	node.queue(function() {

		if (effectName == 'anim') {

			var iterations = args.iterations || 1;
			var duration = args.duration || 500;
			
			for (var i = 0; i < iterations; i++) {
				node.animate(args.from, 0, easing);
				node.animate(args.to, duration, easing);
			}
		} else {
			if (!options) options = {};
			if (!options.easing) options.easing = easing;
			node.effect(effectName, options, speed, callback);
		}
		
		node.dequeue();
	});
}

ice.ace.animation.register = function(args, callback) {

}