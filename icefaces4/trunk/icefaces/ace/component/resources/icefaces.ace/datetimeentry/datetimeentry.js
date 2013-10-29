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

/**
 *  Calendar Widget
 */
ice.ace.Calendar = function(id, cfg) {
    var behavior, altFieldVal;
    this.id = id;
    this.cfg = cfg;
    this.jqId = ice.ace.escapeClientId(id);
    this.jqElId = this.cfg.popup ? this.jqId + '_input' : this.jqId + '_inline';
    this.jq = ice.ace.jq(this.jqElId);
    this.cfg.formId = this.jq.parents('form:first').attr('id');

    //i18n and l7n
    this.configureLocale();

    //Override locale pattern with user pattern
    if(this.cfg.pattern) {
        this.cfg.dateFormat = this.cfg.pattern;
    }

    //Select listener
    this.bindDateSelectListener();

    //Form field to use in inline mode
    if(!this.cfg.popup) {
        this.cfg.altField = ice.ace.jq(this.jqId + '_input');
        altFieldVal = this.cfg.altField.val();
    }

    var hasTimePicker = this.hasTimePicker();

    //Setup timepicker
    if(hasTimePicker) {
        this.configureTimePicker();
    }

    if (this.cfg.withinSingleSubmit) {
        ice.cancelSingleSubmit(this.cfg.clientId);
    }

    //Initialize calendar
    if(!this.cfg.disabled) {
        if(hasTimePicker) {
            if (this.cfg.timeOnly) {
                this.jq.timepicker(this.cfg);
                this.jq.timepicker("setTime", ice.ace.jq.trim(altFieldVal));
                this.pickerFn = "timepicker";
            }
            else {
                this.cfg.altFieldTimeOnly = false;
                this.jq.datetimepicker(this.cfg);
                this.pickerFn = "datetimepicker";
                if (!this.cfg.popup && ice.ace.jq.type(altFieldVal) === "string") {
//                    this.cfg.altField.val(altFieldVal);
                    this.jq.datetimepicker("setDate", ice.ace.jq.trim(altFieldVal));
                }
            }
        }
        else {
            this.jq.datepicker(this.cfg);
            this.pickerFn = "datepicker";
            if (!this.cfg.popup && ice.ace.jq.type(altFieldVal) === "string") {
                this.jq.datepicker("setDate", ice.ace.jq.trim(altFieldVal));
            }
        }
    }

    //Client behaviors and input skinning
    if(this.cfg.popup) {
        if(this.cfg.behaviors) {
            ice.ace.attachBehaviors(this.jq, this.cfg.behaviors);
        }

        //Visuals
        if(this.cfg.popup && this.cfg.theme != false) {
            ice.ace.util.bindHoverFocusStyle(this.jq);
        }
        behavior = this.cfg && this.cfg.behaviors && this.cfg.behaviors.dateTextChange;
    }
};

ice.ace.Calendar.prototype.configureLocale = function() {
    var localeSettings = ice.ace.locales[this.cfg.locale];

    if(localeSettings) {
        for(var setting in localeSettings) {
            this.cfg[setting] = localeSettings[setting];
        }
    }
};

ice.ace.Calendar.prototype.bindDateSelectListener = function() {
    var _self = this;
    var behavior = this.cfg && this.cfg.behaviors && this.cfg.behaviors.dateSelect;

    if(this.cfg.behaviors) {
        this.cfg.onSelect = function(dateText, input) {
            var dateSelectBehavior = _self.cfg.behaviors['dateSelect'];

            if (dateSelectBehavior) {
                var inputID = input[input.input ? "input" : "$input"][0].id;
                dateSelectBehavior.oncomplete = function() {
                    var inputElement= document.getElementById(inputID);
                    if (inputElement.nodeName.toLowerCase() == 'input') {
                        ice.ace.jq(inputElement).unbind('focus', ice.ace.jq.datepicker._showDatepicker);
                        inputElement.focus();
                        setTimeout(function() {
                            ice.ace.jq(inputElement).bind('focus', ice.ace.jq.datepicker._showDatepicker);
                        }, 350);
                    }
                };
                ice.ace.ab.call(_self, dateSelectBehavior);
            }
        };
    }
    if (!behavior && this.cfg.singleSubmit) {
        this.cfg.onSelect = function(dateText, inst) {
            ice.se(null, _self.cfg.clientId);
        };
    }

};

ice.ace.Calendar.prototype.configureTimePicker = function() {
    var pattern = this.cfg.dateFormat,
    timeSeparatorIndex = pattern.toLowerCase().indexOf('h');
    
    this.cfg.dateFormat = pattern.substring(0, timeSeparatorIndex - 1);
    this.cfg.timeFormat = pattern.substring(timeSeparatorIndex, pattern.length);

    //second
    if(this.cfg.timeFormat.indexOf('ss') != -1) {
        this.cfg.showSecond = true;
    }

    //ampm
    if(this.cfg.timeFormat.indexOf('TT') != -1) {
        this.cfg.ampm = true;
    }
};

ice.ace.Calendar.prototype.hasTimePicker = function() {
    return this.cfg.dateFormat.toLowerCase().indexOf('h') != -1;
};

ice.ace.Calendar.prototype.setDate = function(date) {
    this.jq.datetimepicker('setDate', date);
};

ice.ace.Calendar.prototype.getDate = function() {
    return this.jq.datetimepicker('getDate');
};

ice.ace.Calendar.prototype.enable = function() {
    this.jq.datetimepicker('enable');
};

ice.ace.Calendar.prototype.disable = function() {
    this.jq.datetimepicker('disable');
};

ice.ace.Calendar.prototype.destroy = function() {
    if (this.pickerFn) this.jq[this.pickerFn]("destroy");
    window[this.cfg.widgetVar] = this.jq = this.cfg.altField = null;
};

ice.ace.CalendarInit = function(options) {
    ice.ace.jq().ready(function() {
        var widgetVar = options.widgetVar, id = options.id;
        var input = ice.ace.jq(ice.ace.escapeClientId(id) + "_input");
        var trigger = null, triggerClass = ice.ace.jq.datepicker._triggerClass;
        var defaults = ice.ace.jq.datepicker._defaults;
        var showOn = options.showOn || defaults.showOn;
        var buttonText = options.buttonText || defaults.buttonText;
        var buttonImage = options.buttonImage || defaults.buttonImage;
        var buttonImageOnly = options.buttonImageOnly || defaults.buttonImageOnly;
        var isRTL = options.isRTL || defaults.isRTL;
        var initEltSet = ice.ace.jq();
        var create = function () {
            var widget = ice.ace.lazy("Calendar", [id, options]);
            ice.onElementUpdate(id, function () {
                widget.destroy();
                initEltSet.remove();
                window[widgetVar] = null;
                delete window[widgetVar];
            });
            return widget;
        };
        var initAndShow = function() {
            if (window[widgetVar]) return;
            if (trigger) trigger.remove();
            window[widgetVar] = create();
            if (!window[widgetVar].pickerFn) return;
            window[widgetVar].jq[window[widgetVar].pickerFn]("show");
        };
        var behavior = options.behaviors && options.behaviors.dateTextChange;

        if (!options.popup) {
            window[widgetVar] = create();
            return;
        }

        input.one("focus", function() {
            if (behavior) {
                input.bind('change', function() {
                    ice.setFocus();
                    ice.ace.ab(behavior);
                });
            } else if (options.singleSubmit) {
                input.bind('change', function(event) {
                    ice.setFocus();
                    ice.se(event, id);
                });
            }
        });

        initEltSet = initEltSet.add(input);

        window[widgetVar] = null;
        if (ice.ace.jq.inArray(showOn, ["button","both"]) >= 0) {
            trigger = buttonImageOnly ?
                ice.ace.jq('<img/>').addClass(triggerClass).
                    attr({ src: buttonImage, alt: buttonText, title: buttonText }) :
                ice.ace.jq('<button type="button"></button>').addClass(triggerClass).
                    html(buttonImage == '' ? buttonText : ice.ace.jq('<img/>').attr(
                    { src:buttonImage, alt:buttonText, title:buttonText }));
            input[isRTL ? 'before' : 'after'](trigger);
            trigger.one("click", initAndShow);
            initEltSet = initEltSet.add(trigger);
        }
        if (ice.ace.jq.inArray(showOn, ["focus","both"]) >= 0) {
            input.one("focus", initAndShow);
            initEltSet = initEltSet.add(input);
        }

        ice.onElementUpdate(id, function() {
            // .remove cleans jQuery state unlike .unbind
            initEltSet.remove();
            window[widgetVar] = null;
            delete window[widgetVar];
        });
    });
};
