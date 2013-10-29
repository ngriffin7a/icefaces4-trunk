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


if (!window['mobi']) {
    window.mobi = {};
}

mobi.flipswitch = {
    lastTime: 0,

    init: function(clientId, cfg){
        // Mobi-526 filter double clicks
        if (cfg.transHack) {
            var currentTimeMillis = new Date().getTime();
            if ( (currentTimeMillis - mobi.flipswitch.lastTime) < 100 ) {
                console.log("Double click suppression required");
                return;
            }
            mobi.flipswitch.lastTime = currentTimeMillis;
        }

        this.id = clientId;
        this.cfg = cfg;
        this.flipperEl = cfg.elVal;
        this.singleSubmit = cfg.singleSubmit;
        this.event = cfg.event;

        var hasBehaviors = false;
        if (this.cfg.behaviors){
           hasBehaviors = true;
        }
        if (this.flipperEl){
            var oldClass = this.flipperEl.className;
            var value = "off";
            var onClass = this.flipperEl.children[0].className;
            var offClass = this.flipperEl.children[2].className;
            if (oldClass.indexOf('-off ')>0){
            	this.flipperEl.className='mobi-flipswitch mobi-flipswitch-on ui-widget';
            	this.flipperEl.children[0].className = 'mobi-flipswitch-txt-on ui-state-active';
            	this.flipperEl.children[2].className = 'mobi-flipswitch-txt-off';
                value = true;
            }else{
             	this.flipperEl.className='mobi-flipswitch mobi-flipswitch-off ui-widget';
             	this.flipperEl.children[0].className = 'mobi-flipswitch-txt-on';
                this.flipperEl.children[2].className = 'mobi-flipswitch-txt-off';
               	value = false;
            }
            var hidden = this.id+"_hidden";
            var thisEl = document.getElementById(hidden);
            if (thisEl){
               thisEl.value=value.toString();
            }
            if (this.singleSubmit){
                    ice.se(this.event, this.id);
                }
            if (hasBehaviors){
                if (this.cfg.behaviors.activate){
                    this.cfg.behaviors.activate();
                }
            }
         }
    }
};

