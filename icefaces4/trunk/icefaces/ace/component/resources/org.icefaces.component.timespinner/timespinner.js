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
mobi.timespinner = {
    pattern:{}, //only supports 'hh:mm a' at this time.
    opened:{},
    centerCalculation:{},
    scrollEvent:{},
    init:function (clientId, hrSel, mSel, aSel, format) {
        var idPanel = clientId + "_bg";
        if (!document.getElementById(idPanel).className) {
            document.getElementById(idPanel).className = 'mobi-date-bg-inv';
        }
        var intAmPm = parseInt(aSel);
        var intMinute = parseInt(mSel);
        var intHr = parseInt(hrSel);
        if (format) {
            this.pattern[clientId] = format;
            ice.log.debug(ice.log, ' pattern change not yet implemented =' + this.pattern);
        }
        this.opened[clientId] = false;
        //have to set the value controls to the correct integer
        var hrEl = document.getElementById(clientId + "_hrInt");
        var minEl = document.getElementById(clientId + "_mInt");
        var ampmEl = document.getElementById(clientId + "_ampmInt");
        if (minEl) {
            minEl.innerHTML = intMinute;
        }
        if (hrEl) {
            hrEl.innerHTML = intHr;
        }
        var ampm = 'AM';
        if (intAmPm > 0) {
            ampm = 'PM';
        }
        //       ice.log.debug(ice.log, 'val of intAmpm ='+intAmPm + ' ampm = '+ampm);
        if (ampmEl) {
            ampmEl.innerHTML = ampm;
        }
        this.updateTime(clientId);
        this.scrollEvent = 'ontouchstart' in window ? "touchmove" : "scroll";
    },
    mUp:function (clientId) {
        var mId = clientId + '_mInt';
        var minEl = document.getElementById(mId);
        if (minEl) {
            var mInt = this.getIntValue(mId);
            if (mInt == 59) {
                minEl.innerHTML = 0;
            }
            else {
                minEl.innerHTML = mInt + 1;
            }
        }
        this.updateTime(clientId);
    },
    mDn:function (clientId) {
        var mId = clientId + '_mInt';
        var minEl = document.getElementById(mId);
        if (minEl) {
            var mInt = this.getIntValue(mId);
            if (mInt == 0) {
                minEl.innerHTML = 59;
            }
            else {
                minEl.innerHTML = mInt - 1;
            }
        }
        this.updateTime(clientId);
    },
    hrUp:function (clientId) {
        var hrId = clientId + '_hrInt';
        var hrEl = document.getElementById(hrId);
        if (hrEl) {
            var hrInt = this.getIntValue(hrId);
            if (hrInt == 12) {
                hrEl.innerHTML = 1;
            }
            else {
                hrEl.innerHTML = hrInt + 1;
            }
        }
        this.updateTime(clientId);
    },
    hrDn:function (clientId) {
        var hrEl = document.getElementById(clientId + '_hrInt');
        if (hrEl) {
            var hrInt = this.getIntValue(clientId + '_hrInt');
            if (hrInt == 1) {
                hrEl.innerHTML = 12;
            }
            else {
                hrEl.innerHTML = hrInt - 1;
            }
        }
        this.updateTime(clientId);
    },
    ampmToggle:function (clientId) {
        var aId = clientId + '_ampmInt';
        var aEl = document.getElementById(aId);
        if (aEl.innerHTML == "AM") {
            aEl.innerHTML = 'PM';
        }
        else {
            aEl.innerHTML = 'AM';
        }
        this.updateTime(clientId);
    },
    updateTime:function (clientId) {
        var ampm = clientId + "_ampmInt";
        var ampmEl = document.getElementById(ampm);
        var mInt = this.getIntValue(clientId + "_mInt");
        var hrInt = this.getIntValue(clientId + "_hrInt");
        this.writeTitle(clientId, hrInt, mInt, ampmEl.innerHTML);
    },

    writeTitle:function (clientId, hr, min, ampm) {
        if (hr < 10) {
            hr = '0' + hr;
        }
        if (min < 10) {
            min = '0' + min;
        }
        var time = hr + ':' + min + ' ' + ampm;
        var titleEl = document.getElementById(clientId + '_title');
        titleEl.childNodes[1].innerHTML = time;
    },

    getIntValue:function (id) {
        var element = document.getElementById(id);
        if (element) {
            var stringEl = element.innerHTML;
            return parseInt(stringEl);
        } else return 1;
    },
    select:function (clientId, cfg) {
        this.cfg = cfg;
        var singleSubmit = this.cfg.singleSubmit;
        var event = this.cfg.event;
        var hasBehaviors = false;
        var behaviors = this.cfg.behaviors;
        if (behaviors) {
            hasBehaviors = true;
        }
        var inputEl = document.getElementById(clientId + '_input');
        var titleEl = document.getElementById(clientId + '_title');
        inputEl.value = titleEl.childNodes[1].innerHTML;
        if (hasBehaviors) {
            if (behaviors.change) {
                behaviors.change();
            }
        }
        if (!hasBehaviors && singleSubmit) {
            ice.se(event, clientId);
        }
        this.close(clientId);
    },
    toggle:function (clientId) {
        if (!this.opened[clientId]) {
            this.open(clientId);
        } else {
            this.close(clientId);
        }
    },
    open:function (clientId) {
        var idPanel = clientId + "_bg";
        var idPopPanel = clientId + "_popup";

        document.getElementById(idPanel).className = "mobi-time-bg";
        document.getElementById(idPopPanel).className = "mobi-time-container ui-widget ui-widget-content";

        // add scroll listener
        this.centerCalculation[clientId] = function () {
            mobi.panelAutoCenter(idPopPanel);
        };

        ice.mobi.addListener(window, this.scrollEvent, this.centerCalculation[clientId]);
        ice.mobi.addListener(window, 'resize', this.centerCalculation[clientId]);

        // set visible
        this.opened[clientId] = true;
        // calculate center for first view
        mobi.panelAutoCenter(idPopPanel);
    },
    close:function (clientId) {
        var idPanel = clientId + "_bg";
        // remove scroll listener
        if (window.removeEventListener) {
            window.removeEventListener(this.scrollEvent, this.centerCalculation[clientId], false);
            window.removeEventListener('resize', this.centerCalculation[clientId], false);
        } else { // older ie cleanup
            window.detachEvent(this.scrollEvent, this.centerCalculation[clientId], false);
            window.detachEvent('resize', this.centerCalculation[clientId], false);
        }
        // hide the panels
        document.getElementById(idPanel).className = "mobi-time";
        document.getElementById(clientId + "_popup").className = "mobi-time-container-inv ui-widget ui-widget-content";
        this.opened[clientId] = false;
        this.centerCalculation[clientId] = undefined;
    },
    unload:function (clientId) {
        alert("unloading for clientId");
        /* var titleEl = document.getElementById(clientId+'_title');
         titleEl.innerHTML = "";  */
        this.pattern[clientId] = null;
        this.opened[clientId] = null;
    }

};