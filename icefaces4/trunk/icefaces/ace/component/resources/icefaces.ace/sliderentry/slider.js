/* 
* Original Code Copyright Prime Technology.
* Subsequent Code Modifications Copyright 2011-2012 ICEsoft Technologies Canada Corp. (c)
* 
* Licensed under the Apache License, Version 2.0 (the "License"); 
* you may not use this file except in compliance with the License. 
* You may obtain a copy of the License at 
* 
* http://www.apache.org/licenses/LICENSE-2.0 
* 
* Unless required by applicable law or agreed to in writing, software 
* distributed under the License is distributed on an "AS IS" BASIS, 
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
* See the License for the specific language governing permissions and 
* limitations under the License. 
* 
* NOTE THIS CODE HAS BEEN MODIFIED FROM ORIGINAL FORM 
* 
* Subsequent Code Modifications have been made and contributed by ICEsoft Technologies Canada Corp. (c). 
* 
* Code Modification 1: Integrated with ICEfaces Advanced Component Environment. 
* Contributors: ICEsoft Technologies Canada Corp. (c) 
* 
* Code Modification 2: [ADD BRIEF DESCRIPTION HERE] 
* Contributors: ______________________ 
* Contributors: ______________________ 
* 
*/

/**
 *  Slider Widget
 */
ice.ace.Slider = function(id, cfg) {
	this.id = id;
	this.cfg = cfg;
    this.jq = ice.ace.jq(ice.ace.escapeClientId(this.id)).children('div').eq(1);
	this.input = ice.ace.jq(ice.ace.escapeClientId(this.cfg.input));
    var _self = this;
	
	// disable animation for IE 7/8
	if (!ice.ace.jq.support.leadingWhitespace) {
		this.cfg.animate = false;
	}
    
    //Create slider
	if (this.cfg.clickableRail == false) {
		this.jq.mousedown(function(event) { if (event.target == this) event.stopImmediatePropagation(); }).slider(this.cfg);
	} else {
		this.jq.slider(this.cfg);
	}
	var handle = ice.ace.jq(ice.ace.escapeClientId(this.id) + " .ui-slider-handle");
    handle.attr('id', this.id + '_handle');
	if (this.cfg.tabindex) handle.attr('tabindex', this.cfg.tabindex);

    //Slide handler
	this.jq.bind('slide', function(event, ui) {
        _self.onSlide(event, ui);
    });

    //Slide start handler
    if(this.cfg.onSlideStart) {
        this.jq.bind('slidestart', function(event, ui) {_self.cfg.onSlideStart.call(this, event, ui);});
    }
	if (this.cfg.behaviors) {
		if (this.cfg.behaviors.slideStart) {
			this.jq.bind('slidestart', function(event, ui) { ice.ace.ab(_self.cfg.behaviors.slideStart); });
		}
	}

    //Slide end handler
    this.jq.bind('slidestop', function(event, ui) {_self.onSlideEnd(event, ui);});

    
    // This call required to init slider when inside tabset on IE browsers.
    // Else slider remains hidden until some tab action occurs.
    var jq = this.jq;
    if (this.cfg.disabled)
        window.setTimeout(function () { jq.slider('disable'); } , 1);
    else
        window.setTimeout(function () { jq.slider('enable'); } , 1);
}

ice.ace.Slider.prototype.onSlide = function(event, ui) {
    //User callback
    if(this.cfg.onSlide) {
        this.cfg.onSlide.call(this, event, ui);
    }

    //Update input
	this.input.val(ui.value);
	
	if (this.cfg.behaviors) {
		if (this.cfg.behaviors.slide) {
			ice.ace.ab(this.cfg.behaviors.slide);
		}
	}
}

ice.ace.Slider.prototype.onSlideEnd = function(event, ui) {
    //User callback
    if(this.cfg.onSlideEnd) {
        this.cfg.onSlideEnd.call(this, event, ui);
    }
	
	if (this.cfg.behaviors) {
		if (this.cfg.behaviors.slideEnd) {
			ice.ace.ab(this.cfg.behaviors.slideEnd);
		}
	}
}

ice.ace.Slider.prototype.getValue = function() {
    return this.jq.slider('value');
}

ice.ace.Slider.prototype.setValue = function(value) {
    this.jq.slider('value', value);
}

ice.ace.Slider.prototype.enable = function() {
    this.jq.slider('enable');
}

ice.ace.Slider.prototype.disable = function() {
    this.jq.slider('disable');
}

